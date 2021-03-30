package com.ijudy.races.controller;

import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ijudy.races.service.captcha.ICaptchaService;
import com.ijudy.races.exception.*;
import com.ijudy.races.dto.PasswordResetTokenDTO;
import com.ijudy.races.dto.UserDTO;
import com.ijudy.races.security.SocialUserKey;
import com.ijudy.races.security.UserUtils;
import com.ijudy.races.service.email.PostMasterService;
import com.ijudy.races.service.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.hazlewood.connor.bottema.emailaddress.EmailAddressValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.security.InvalidParameterException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/api/v2")
@Slf4j
public class UsersController extends BaseController{

    @Value( "${login.postsignin.url}")
    private String socialPostSignInUrl;

    private final ICaptchaService captchaService;
    private final PostMasterService postMasterService;

    public UsersController(ICaptchaService captchaService, PostMasterService postMasterService) {
        this.captchaService = captchaService;
        this.postMasterService = postMasterService;
    }

    @GetMapping(value ="/users")
    @ResponseBody
    public List<UserDTO> getAllUsers() {
        return userService.findAllUsers();
    }

    @PostMapping(value ="/registration")
    @ResponseBody
    public UserDTO registerUser(@RequestBody UserDTO user) {

        // Setting email to lower case just in case UI
        // Messed us up
        String email = user.getEmail().toLowerCase();
        user.setEmail(email);

        log.debug("Attempting to register User : " + email);
        try {

            captchaService.processResponse(user.getToken());

        } catch (ReCaptchaInvalidException e) {
            String msg ="User " + email + " failed captcha";
            log.warn(msg);
            throw new FailedCaptcha(msg);
        }

        boolean emailValid = EmailAddressValidator.isValid(email );
        Set<ConstraintViolation<UserDTO>> constraintViolations = validate(user);
        final UserDTO saveUser;

        if (emailValid && constraintViolations.isEmpty()) {
            final Optional<UserDTO> foundUser = userService.findByEmailAndSocialProvider(email);

            if (foundUser.isEmpty()) {
                saveUser = userService.registerLocalUser(user);
                saveUser.setPassword("");
                saveUser.setConfirmPassword(saveUser.getPassword());
            } else {
                throw new UserIdExistsException(email);
            }
        } else {
            Set<String> messages = extractUserMessages(constraintViolations);
            log.warn("email or password is invalid : " + messages);
            throw new CredentialsNotValid(messages.toString());
        }

        return saveUser;
    }

    private Set<ConstraintViolation<UserDTO>> validate(UserDTO user){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator= factory.getValidator();
        return validator.validate( user );
    }


    private Set<String> extractUserMessages(Set<ConstraintViolation<UserDTO>> constraintViolations) {

        Set<String> messages = new HashSet<>(constraintViolations.size());

        messages.addAll(constraintViolations.stream()
                .map(constraintViolation ->
                            String.format("%s value '%s' %s",
                            constraintViolation.getPropertyPath(),
                            constraintViolation.getInvalidValue(),
                            constraintViolation.getMessage()))
                .collect(Collectors.toList()));
        return messages;
    }

    @GetMapping(value="/principal")
    @ResponseBody
    public ResponseEntity<?> getPrincipal(Principal principal) throws JsonProcessingException {

        if (principal == null) {
            log.error("User Principal was null");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {

            if (principal instanceof UsernamePasswordAuthenticationToken) {
                UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
                Object p = token.getPrincipal();
                if (p instanceof UserPrincipal) {
                    UserPrincipal user = (UserPrincipal) p;
                    String email = user.getUsername();
                    log.debug("UserPrincipal Found : {}", email);
                    Optional<UserDTO> userDTO = userService.findByEmailAndSocialProvider(email);
                    if (userDTO.isPresent()){
                        UserDTO realDTO  =userDTO.get();
                        realDTO.setPassword("");
                        log.debug("UserPrincipal Returning user JSON (minus password) as : {}", realDTO.getEmail());
                        return ResponseEntity.ok(realDTO);
                    } else {
                        log.error("Could not Find user in data repo");
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    }
                } else if (p instanceof User){
                    User user = (User)p;
                    Optional<UserDTO> userDTO = userService.findByEmailAndSocialProvider(user.getUsername());
                    if (userDTO.isPresent()){
                        UserDTO realDTO = userDTO.get();
                        realDTO.setPassword("");
                        log.debug("User implementing UserDetails returning user JSON (minus password) as : {}", realDTO.getEmail());
                        return ResponseEntity.ok(realDTO);
                    } else {
                        log.error("Could not Find user in data repo");
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    }
                } else {
                    log.error("Principal not an instance of  UserPrincipal nor UserDetails: {}", principal.getName());
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
            } else if (principal instanceof OAuth2AuthenticationToken) {
                OAuth2AuthenticationToken token = (OAuth2AuthenticationToken)principal;
                Object p = token.getPrincipal();
                if (p instanceof com.ijudy.races.service.security.UserPrincipal) {
                    com.ijudy.races.service.security.UserPrincipal userPrincipal = (com.ijudy.races.service.security.UserPrincipal)p;
                    final String socialProvider = userPrincipal.getSocialProvider();
                    log.debug("OAuth2AuthenticationToken Found : {}", userPrincipal);
                    Optional<UserDTO> userDTO = userService.findByEmailAndSocialProvider(userPrincipal.getUsername(), socialProvider);
                    if (userDTO.isPresent()){
                        UserDTO realDTO  =userDTO.get();
                        realDTO.setPassword("");
                        return ResponseEntity.ok(realDTO);
                    } else {
                        log.error("Could not Find user in data repo");
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    }
                } else {
                    log.error("getPrincipal was not of type com.ijudy.races.service.security.UserPrincipal");
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }
            else {
                log.error("getPrincipal was not UsernamePasswordAuthenticationToken nor OAuth2AuthenticationToken");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

    }

    @GetMapping(value="/user-exists")
    @ResponseBody
    public boolean userExists(@RequestParam String email) {

        log.debug("Looking up {}", email);
        boolean result;

        List<UserDTO> list = userService.findAllUsersByEmail(email);

        if (list.isEmpty()) {
            result = false;
        } else {
            log.info("Found existing local user with alias {}", email);
            result = true;
        }
        return result;
    }

    @PostMapping(value ="/reset")
    @ResponseBody
    public UserDTO resetPassword(@RequestBody UserDTO user) {

        log.debug("Attempting to reset password for UserEntity " + user.getEmail());

        // Validate UserEntity's Email
        boolean emailValid = EmailAddressValidator.isValid(user.getEmail() );

        // Validate UserEntity Object
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator= factory.getValidator();
        Set<ConstraintViolation<UserDTO>> constraintViolations = validator.validate( user );

        if (emailValid && constraintViolations.isEmpty()) {
             userService.resetPassword(user);
        } else {
            Set<String> messages = new HashSet<>(constraintViolations.size());
            messages.addAll(constraintViolations.stream()
                    .map(constraintViolation -> String.format("%s value '%s' %s", constraintViolation.getPropertyPath(),
                            constraintViolation.getInvalidValue(), constraintViolation.getMessage()))
                    .collect(Collectors.toList()));
            throw new InvalidParameterException("email or password is invalid " + messages);
        }

        log.debug("Reset Password for UserEntity " + user.getEmail());
        log.debug("Stripping password on the way out ");
        user.setPassword("NOPE");
        return user;
    }

    @PostMapping(value ="/forgot")
    @ResponseBody
    public ResponseEntity<?> forgot(@RequestBody UserDTO user) {

        log.debug("Attempting to reset email " + user.getEmail());
        String email = user.getEmail();
        boolean emailValid = EmailAddressValidator.isValid(email );
        if (emailValid ) {
            Optional<UserDTO> foundUser = userService.findByEmailAndSocialProvider(email);
            if (foundUser.isEmpty()) {
                log.error("Did not find user in data repo with " + email + " for forgot(...)");
                throw new NotFoundException(email);
            } else {
                log.debug("Reset Password for " + email);
                final SendEmailResult result = postMasterService.sendResetEmail(foundUser.get());
                log.info("Sent reset email for " + email + " with sendEmailResult ID : " + result.getMessageId());
                return new ResponseEntity<>(HttpStatus.OK);
            }
        } else {
            throw new InvalidParameterException("email or password is invalid");
        }
    }

    @GetMapping("/user/{userId}")
    @ResponseBody
    public Optional<UserDTO> getUserById(@PathVariable Long userId) {
        return userService.findById(userId);
    }

    @PatchMapping(value ="/user")
    @ResponseBody
    public ResponseEntity<?> updateProfile(@RequestBody UserDTO user, Principal principal) {
        // Create user from Principal and not the user's request
        SocialUserKey key = UserUtils.getSocialUserKey(principal);
        if (user.getEmail().equalsIgnoreCase(key.email)){
            user.setEmail(key.email);
            user.setSocialProvider(key.socialProvider);
            log.debug("Updating profile with key - email [" + key.email + "], social provider [" + key.socialProvider + "]");
            Optional<UserDTO> updatedUser = userService.updateProfile(user);
            if (updatedUser.isPresent()){
                updatedUser.get().setPassword("");
                return ResponseEntity.ok(updatedUser);
            } else{
                log.error("Could not find user in data repo");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            log.error("User Email and Principal do not match");
            log.debug("User [email " + user.getEmail() + "], principal email [" + key.email + "]");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }


    /**
     *
     * @param email user's email
     * @param token generated token
     * @param model model map
     * @return redirect to socialPostSignInUrl
     */
    @GetMapping(value = "/changePassword")
    public ModelAndView showChangePasswordPage(@RequestParam("email") String email, @RequestParam("token") String token, ModelMap model) {

        final Optional<UserDTO> userDTO = userService.findByEmailAndSocialProvider(email);
        if (userDTO.isPresent()){
            String result = validatePasswordResetToken(userDTO.get(), token);
            if (result != null) {
                // Redirect to some error page
                model.addAttribute("attribute", "changePassword");
                String redirectUrl = "redirect:" + socialPostSignInUrl + "#/forgot?tokenExpired=true";
                return new ModelAndView(redirectUrl, model);
            }

            // Redirect to reset-password page with the granted privilege
            String redirectUrl = "redirect:" + socialPostSignInUrl + "#/reset-password/" + email + "/" + token;
            return new ModelAndView(redirectUrl, model);
        } else {
            // Redirect to some error page
            model.addAttribute("attribute", "changePassword");
            String redirectUrl = "redirect:" + socialPostSignInUrl + "#/forgot?userNotFound=true";
            return new ModelAndView(redirectUrl, model);
        }
    }

    private String validatePasswordResetToken(UserDTO user, String token) {

        final PasswordResetTokenDTO passwordResetTokenDTO = userService.findByUserIdAndToken(user.getId(), token);
        boolean expired = LocalDateTime.now().isAfter(passwordResetTokenDTO.getExpiryDate());

        if (passwordResetTokenDTO == null) {
            log.debug("Token not found for email " + user.getEmail());
            return "invalidToken";
        } else if (expired) {
            log.debug("Token expired for email " + user.getEmail());
            return "invalidTokenOrUserId";
        }
        List<SimpleGrantedAuthority> change_password_privilege = Arrays.asList(new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE"));
        // Found a valid token in this case grant the limited AUTH and then return with a null
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, change_password_privilege);
        SecurityContextHolder.getContext().setAuthentication(auth);
        log.debug("Setting Auth CHANGE_PASSWORD_PRIVILEGE for for email " + user.getEmail());
        return null;
    }
}

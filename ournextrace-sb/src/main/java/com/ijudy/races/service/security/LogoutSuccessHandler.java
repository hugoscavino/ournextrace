package com.ijudy.races.service.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    @Value( "${login.postlogout.url}")
    private String logoutUrl;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {
        if (authentication != null && authentication.getDetails() != null) {
            try {
                request.getSession().invalidate();
                // TODO Add more codes here when the user successfully logs out,
                // TODO such as updating the database for last active.
            } catch (Exception e) {
                log.info("Error when logging out" + e.getMessage());
            }
        }
        // response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        // response.setStatus(HttpServletResponse.SC_OK);
        // response.getWriter().flush();

        String refererUrl = request.getHeader("Referer");
        log.info("Referer : " + refererUrl);
        // auditService.track("Logout from: " + refererUrl);

        // super.onLogoutSuccess(request, response, authentication);
        //redirect to referer page
        response.setStatus(HttpServletResponse.SC_OK);
        response.setStatus(HttpStatus.NO_CONTENT.value());
        log.info("Redirecting to  : " + logoutUrl);
        response.sendRedirect(logoutUrl);
    }

}


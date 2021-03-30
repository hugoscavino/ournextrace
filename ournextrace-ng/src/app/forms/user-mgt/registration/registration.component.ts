import { Component, OnInit, } from '@angular/core';
import { Validators, FormControl, FormGroup} from '@angular/forms';
import { HttpStatusCode} from '../../../service/error-handler/status-codes';
import { HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';

import { MessageService} from 'primeng/api';
import { AuthService} from '../../../service/auth';
import { IJudyConstants } from '../../../util/constants';
import { User } from '../../../domain/user';
import { environment} from '../../../../environments/environment';

import { Gtag } from 'angular-gtag';
import { MetadataService } from 'src/app/service/metadata';
import { SetUser } from 'src/app/ngrx/race.actions';
import { Store } from '@ngrx/store';
import { RaceStore } from 'src/app/ngrx/race.app.state';

@Component({
  selector: 'ijudy-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  public passwordPolicy: string;
  public emailTaken = false;
  public userExists = false;
  public captchaResponse: string;
  public notRobot = false;

  constructor(public store: Store<RaceStore>,
              public metadataService: MetadataService,
              public router: Router,
              public messageService: MessageService,
              public authService: AuthService,
              public gtag: Gtag) {
  }

  public registrationForm = new FormGroup({
    email: new FormControl(null, Validators.compose([Validators.required, Validators.minLength(8)])),
    password: new FormControl(null, Validators.compose([Validators.required, Validators.minLength(8), Validators.maxLength(14)])),
    confirmPassword: new FormControl(null,
              Validators.compose([
              Validators.required,
              Validators.minLength(8),
              Validators.maxLength(14)
            ]
            )
          ),
  });

  public passwordMatchValidator(fg: FormGroup) {
    const password = fg.controls['password'].value;
    const confirmPassword = fg.controls['confirmPassword'].value;
    if ( password === confirmPassword) {
      return true;
    } else {
      return false;
    }
  }

  public getPasswordsMatch(): boolean {
    const password        = this.registrationForm.controls['password'].value;
    const confirmPassword = this.registrationForm.controls['confirmPassword'].value;
    // console.log('Password : ' + password + ', Confirm Password : ' + confirmPassword);
    if ( password === confirmPassword) {
      return true;
    } else {
      return false;
    }
  }

  public hasLower(): boolean {
    const password        = this.registrationForm.controls['password'].value;
    const regEx = new RegExp('(?=.*[a-z])'); // has at least one lower case letter
    if (regEx.test(password)) {
      return true;
    } else {
      return false;
    }
  }

  public hasUpper(): boolean {
    const password        = this.registrationForm.controls['password'].value;
    const regEx = new RegExp('(?=.*[A-Z])'); // has at least one Upper case letter
    if (regEx.test(password)) {
      return true;
    } else {
      return false;
    }
  }

  public hasNumber(): boolean {
    const password        = this.registrationForm.controls['password'].value;
    const regEx = new RegExp('(?=.*\\d)'); // has at least one number
    if (regEx.test(password)) {
      return true;
    } else {
      return false;
    }
  }

  public hasSpecial(): boolean {
    const password        = this.registrationForm.controls['password'].value;
    const regEx = new RegExp('[!@#$%^&*(),.?\":{}|<>]');
    if (regEx.test(password)) {
      return true;
    } else {
      return false;
    }
  }

  ngOnInit() {
    this.loadDocs();
  }

  public loadDocs() {
    this.metadataService.getDocument(environment.password_policy_fileName).subscribe(
        data => {
          this.passwordPolicy = data;
        }
    );
  }

  /**
   * Go back to main page
   * @param event
   */
  cancel(event: any) {
    this.router.navigate([IJudyConstants.RACES_URI]);
  }

  public getUserExists() {
    const email            = this.registrationForm.value['email'];
    this.authService.userExists(email).subscribe(
      data => {
        this.userExists = data;
        // console.log('this.userExists : ' + this.userExists);
      })
      ;
  }

  public showResponse(event: any) {
    this.captchaResponse = event.response;
    this.notRobot       = (this.captchaResponse && this.captchaResponse.length > 0);
  }

  public validForm() {
    const ok = this.registrationForm.valid && this.notRobot;
    return ok;
  }

  public onRegister(event: any) {

    const name             = this.registrationForm.value['email'];
    const email            = this.registrationForm.value['email'];
    const password         = this.registrationForm.value['password'];
    const confirmPassword  = this.registrationForm.value['confirmPassword'];
    if (!this.passwordMatchValidator(this.registrationForm)) {
      this.messageService.add(
        {severity: 'error',
        summary: 'Passwords',
        detail: 'Passwords must Match '});
    } else {
      this.gtag.event('ijudy_registration', {
        method: 'onRegister',
        event_category: 'registration',
        event_label: 'New user registered using local registration'
      });
      const userToPost: User = {
          email: email,
          password: password,
          confirmPassword: confirmPassword,
          name: name,
          token: this.captchaResponse,
          user: true
      };

      this.authService.registration(userToPost).subscribe(
        user => {
          this.router.navigate([IJudyConstants.RACES_URI]);
          this.store.dispatch(SetUser({user: user}));
        },
        error => {
          // ('Error with Registration' + JSON.stringify(error));
          if (error instanceof HttpErrorResponse) {
            const httpErrorCode = error.status;
            // console.error('httpErrorCode :' + httpErrorCode);
            switch (httpErrorCode) {
                case HttpStatusCode.CONFLICT:
                this.emailTaken = true;
                this.messageService.add(
                  {severity: 'error',
                  summary: 'Email Exists',
                  detail: 'Sorry there is a user already registered with ' + email});
                    break;
                default:
                    throw error;
            }
          }
              },
        () =>  this.messageService.add(
        {severity: 'success',
        summary: 'Welcome',
        detail: 'Registered with ' + email})
      );
    }
  }
}

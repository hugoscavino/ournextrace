import { Component, OnInit, } from '@angular/core';
import { Validators, FormControl, FormGroup} from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { Router, ActivatedRoute } from '@angular/router';

import { MessageService} from 'primeng/api';
import { AuthService} from '../../../service/auth';
import { IJudyConstants } from '../../../util/constants';
import { environment} from '../../../../environments/environment';

import { Gtag } from 'angular-gtag';
import { MetadataService } from 'src/app/service/metadata';

@Component({
  selector: 'ijudy-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent implements OnInit {

  public passwordPolicy: string;
  public passwordsMatch = true;
  public email: string;
  public token: string;


  public resetPasswordForm = new FormGroup({
    password: new FormControl(null, Validators.compose([Validators.required, Validators.minLength(8), Validators.maxLength(14)])),
    confirmPassword: new FormControl(null, Validators.compose([Validators.required, Validators.minLength(8), Validators.maxLength(14)])),
  });

  public passwordMatchValidator(fg: FormGroup) {

    const password = fg.controls['password'].value;
    const confirmPassword = fg.controls['confirmPassword'].value;
    // ('Password : ' + password + ', Confirm Password : ' + confirmPassword);
    if ( password === confirmPassword) {
      return true;
    } else {
      return false;
    }
  }

  constructor(
    private metadataService: MetadataService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private messageService: MessageService,
    private authService: AuthService,
    private gtag: Gtag) {

    }

  ngOnInit() {
    this.email = this.activatedRoute.snapshot.params['email'];
    this.token = this.activatedRoute.snapshot.params['token'];
    this.loadDocs();
  }

  public loadDocs() {
    this.metadataService.getDocument(environment.password_policy_fileName).subscribe(
        data => {
          this.passwordPolicy = data;
        }
    );
  }
  public getPasswordsMatch(): boolean {
    const password        = this.resetPasswordForm.controls['password'].value;
    const confirmPassword = this.resetPasswordForm.controls['confirmPassword'].value;
    // console.log('Password : ' + password + ', Confirm Password : ' + confirmPassword);
    if ( password === confirmPassword) {
      return true;
    } else {
      return false;
    }
  }

  public hasLower(): boolean {
    const password        = this.resetPasswordForm.controls['password'].value;
    const regEx = new RegExp('(?=.*[a-z])'); // has at least one lower case letter
    if (regEx.test(password)) {
      return true;
    } else {
      return false;
    }
  }

  public hasUpper(): boolean {
    const password        = this.resetPasswordForm.controls['password'].value;
    const regEx = new RegExp('(?=.*[A-Z])'); // has at least one Upper case letter
    if (regEx.test(password)) {
      return true;
    } else {
      return false;
    }
  }

  public hasNumber(): boolean {
    const password        = this.resetPasswordForm.controls['password'].value;
    const regEx = new RegExp('(?=.*\\d)'); // has at least one number
    if (regEx.test(password)) {
      return true;
    } else {
      return false;
    }
  }

  public hasSpecial(): boolean {
    const password        = this.resetPasswordForm.controls['password'].value;
    const regEx = new RegExp('[!@#$%^&*(),.?\":{}|<>]');
    if (regEx.test(password)) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Go back to main page
   * @param event
   */
  public cancel(event) {
    this.router.navigate([IJudyConstants.RACES_URI]);
  }

  public onReset(event) {

    const password         = this.resetPasswordForm.value['password'];
    const confirmPassword  = this.resetPasswordForm.value['confirmPassword'];
    if (!this.passwordMatchValidator(this.resetPasswordForm)) {
      this.messageService.add(
        {severity: 'error',
        summary: 'Passwords',
        detail: 'Passwords must Match '});
    } else {
      this.gtag.event('ijudy_reset_password', {
        method: 'onReset',
        event_category: 'security',
        event_label: 'User resetting password'
      });

      this.authService.resetPassword(this.email, password, confirmPassword, this.token).subscribe(
        data => {
          this.router.navigate([IJudyConstants.RACES_URI], { queryParams: { authenticated: true } });
        },
        error => {
            // ('Error with ijudy_reset_password' + JSON.stringify(error));
            if (error instanceof HttpErrorResponse) {
              throw error;
            }
        },
        () =>  this.messageService.add(
        {severity: 'success',
        summary: 'Password Reset',
        detail: 'User ' + this.email + ' has had their password reset. Please LOGIN again.'})
      );
    }
  }
}

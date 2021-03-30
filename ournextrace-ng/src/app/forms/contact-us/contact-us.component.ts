import { Component, OnInit, } from '@angular/core';
import { Validators, FormControl, FormGroup} from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { MessageService} from 'primeng/api';
import { AuthService} from '../../service/auth';
import { IJudyConstants } from '../../util/constants';
import { HttpStatusCode} from '../../service/error-handler/status-codes';

import { Gtag } from 'angular-gtag';
import { ContactUs } from 'src/app/domain/contact-us';
import { Observable, Subscription } from 'rxjs';
import { RaceAppState, RaceStore, moduleKeyName } from 'src/app/ngrx/race.app.state';
import { Store, select } from '@ngrx/store';
import { map } from 'rxjs/operators';
import { User } from 'src/app/domain/user';

@Component({
  selector: 'ijudy-contact-us',
  templateUrl: './contact-us.component.html',
  styleUrls: ['./contact-us.component.css']
})
export class ContactUsComponent implements OnInit {

  public contactForm: FormGroup;
  public contactUs: ContactUs;
  public captchaResponse: string;
  public raceStateObs$: Observable<RaceAppState>;
  public raceReducerSubscription: Subscription;
  public user: User;

  constructor(
    public store: Store<RaceStore>,
    private router: Router,
    private messageService: MessageService,
    private authService: AuthService,
    private gtag: Gtag) {
    this.raceStateObs$ = store.pipe(select(moduleKeyName));

  }

  ngOnInit() {
    this.contactUs = new ContactUs();
    this.contactForm = new FormGroup({
      email: new FormControl(null, Validators.compose([Validators.required, Validators.minLength(8)])),
      message: new FormControl(null),
    });



    this.raceReducerSubscription = this.raceStateObs$
    .pipe(
      map(state => {
            this.user = state.user;
            this.contactForm.patchValue({
              email: this.user.email,
              // formControlName2: myValue2 (can be omitted)
            });
         }
      )
    ).subscribe();

  }

  public validForm(): boolean {
    const isValid         = this.contactForm.controls['email'].valid;
    const notARobot       = (this.captchaResponse && this.captchaResponse.length > 0);
    return isValid && notARobot;
  }

  public showResponse(event: any) {
    this.captchaResponse = event.response;
    // console.log('captchaResponse response');
  }

  public initRecaptcha() {
    // console.log('Captcha Initialized');
  }

  public onExpire() {
    this.captchaResponse = '';
    // console.log('Captcha Expired');
  }

  /**
   * Go back to main page
   * @param event
   */
  cancel(event: any) {
    this.router.navigate([IJudyConstants.RACES_URI]);
  }

  public onContactUs(event: any) {

    this.contactUs.email   = this.contactForm.value['email'];
    this.contactUs.message = this.contactForm.value['message'];
    this.contactUs.captchaResponse = this.captchaResponse;

    this.gtag.event('ijudy_contact_us', {
      method: 'onContactUs',
      event_category: 'contact_us',
      event_label: 'User sent us a message'
    });

    // console.log('contactUs : ' + JSON.stringify(this.contactUs));

      this.authService.contactUs(this.contactUs).subscribe(
        data => {
          this.messageService.add(
            {severity: 'info',
            summary: 'Message',
            detail: 'Your Message was Sent'});
            this.router.navigate([IJudyConstants.RACES_URI]);
        },
        error => {
          if (error instanceof HttpErrorResponse) {
            const httpErrorCode = error.status;
            // console.error('httpErrorCode :' + httpErrorCode);
            switch (httpErrorCode) {
                case HttpStatusCode.BAD_REQUEST:
                this.messageService.add(
                  {severity: 'warn',
                  summary: 'Message',
                  detail: 'Your Message was not Sent, Please try again later'});
                    break;
                default:
                this.messageService.add(
                  {severity: 'warn',
                  summary: 'Message',
                  detail: 'Your Message was not Sent, Please return to main page'});
            }
          } else {
            console.error('Error Captcha : ' + JSON.stringify(error));
            throw error;
          }
        },
      );
    }
}

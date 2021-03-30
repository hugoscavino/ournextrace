import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Validators, FormControl, FormGroup} from '@angular/forms';
import { HttpStatusCode} from '../../../service/error-handler/status-codes';
import { HttpErrorResponse } from '@angular/common/http';
import { MessageService} from 'primeng/api';
import { AuthService} from '../../../service/auth';
import { IJudyConstants } from '../../../util/constants';
import { environment} from '../../../../environments/environment';
import { Gtag } from 'angular-gtag';
import { MetadataService } from 'src/app/service/metadata';


@Component({
  selector: 'ijudy-forgot',
  templateUrl: './forgot.component.html',
  styleUrls: ['./forgot.component.css']
})
export class ForgotComponent implements OnInit {

  public passwordResetPolicy: string;
  public tokenExpired = false;

  public forgotForm = new FormGroup({
    email: new FormControl(null, Validators.compose([Validators.required, Validators.minLength(8)])),
  });

  constructor(
    private metadataService: MetadataService,
    private router: Router,
    private messageService: MessageService,
    private authService: AuthService,
    private activatedRoute: ActivatedRoute,
    private gtag: Gtag) {
      this.activatedRoute.queryParams.subscribe(params => {
        this.tokenExpired = params['tokenExpired'];
        // console.log('tokenExpired ' + this.tokenExpired);
      });
    }


  ngOnInit() {
    this.loadDocs();
  }

  public loadDocs() {
    this.metadataService.getDocument(environment.password_reset_policy_fileName).subscribe(
        data => {
          this.passwordResetPolicy = data;
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

  public onForget(event: any) {
    const email = this.forgotForm.value['email'];
      this.gtag.event('ijudy_registration', {
        method: 'onReset',
        event_category: 'password_recovery',
        event_label: 'User Resetting Password'}
      );

      this.authService.forgotPassword(email).subscribe(
        data => {
          this.router.navigate([IJudyConstants.RACES_URI]);
        },
        error => {
          // ('Error with password_recovery' + JSON.stringify(error));
          if (error instanceof HttpErrorResponse) {
            const httpErrorCode = error.status;
            // console.error('httpErrorCode :' + httpErrorCode);
            switch (httpErrorCode) {
                case HttpStatusCode.NOT_FOUND:
                this.messageService.add(
                  {severity: 'error',
                  summary: 'Email Does Not Exists',
                  detail: 'Sorry there is not a user registered with ' + email});
                    break;
                default:
                    throw error;
            }
          }
              },
        () =>  this.messageService.add(
        {severity: 'success',
        summary: 'Welcome',
        detail: 'Password Reset sent to ' + email})
      );
    }

}

import { Component, OnInit, Output, Input, EventEmitter } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpStatusCode} from '../../service/error-handler/status-codes';
import { HttpErrorResponse } from '@angular/common/http';
import { MessageService} from 'primeng/api';
import { Gtag } from 'angular-gtag';
import { AppErrorHandler} from '../../service/error-handler/app-error-handler';
import { AuthService} from '../../service/auth';
import { environment} from '../../../environments/environment';
import { IJudyConstants } from '../../util/constants';
import { User } from 'src/app/domain/user';
import { Store, select } from '@ngrx/store';
import { RaceStore, RaceAppState, moduleKeyName } from 'src/app/ngrx/race.app.state';
import { MetadataService } from 'src/app/service/metadata';
import { Observable, Subscription } from 'rxjs';
import { map } from 'rxjs/operators';
import { SetUser } from 'src/app/ngrx/race.actions';

@Component({
  selector: 'ijudy-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  private visibleVal = false;
  @Output() visibleChange = new EventEmitter();

  @Input()
  get visible(): boolean {
    return this.visibleVal;
  }

  set visible(val: boolean) {
    this.visibleVal = val;
    this.visibleChange.emit(val);
  }

  public socialPolicy: string;
  public whyLoginText: string;
  public errorMessage: string;
  public enableLoginButton: boolean;
  
  public loginForm = new FormGroup({
    email: new FormControl(null, Validators.required),
    password: new FormControl(null, Validators.required),
  });

  public raceStateObs$: Observable<RaceAppState>;
  public raceReducerSubscription: Subscription;
  public selectedRedirectUrl: string;

  constructor(
              public store: Store<RaceStore>,
              private gtag: Gtag,
              private router: Router,
              private appErrorHandler: AppErrorHandler,
              private messageService: MessageService,
              private metadataService: MetadataService,
              private authService: AuthService) {
                this.raceStateObs$ = store.pipe(select(moduleKeyName));
              }

  ngOnInit() {
    this.raceReducerSubscription = this.raceStateObs$
      .pipe(
        map(state => {
              this.selectedRedirectUrl = state.redirectUrl;
              // console.log('LoginComponent::ngOnInit => state.redirectUrl : ' + JSON.stringify(state.redirectUrl));
          }
        )
      ).subscribe();
    this.errorMessage = null;
    this.enableLoginButton = this.loginForm.valid;
    this.loadDocs();
  }

  public loadDocs() {
    this.metadataService.getDocument(environment.social_policy_fileName).subscribe(
        data => {
          this.socialPolicy = data;
        }
    );

    this.metadataService.getDocument(environment.why_login_filename).subscribe(
      data => {
        this.whyLoginText = data;
      }
  );
  }

  /**
   * Navigate to Registration Page
   */
  public onRegister(event: any) {
    this.visible = false;
    this.router.navigate([IJudyConstants.REGISTER_URI]);
  }

  public onForgot(event: any) {
    this.visible = false;
    this.router.navigate([IJudyConstants.FORGOT_URI]);
  }

  public closeSidebar($event: any) {
    this.visible = false;
  }

  public onSubmit() {

    const email     = this.loginForm.value['email'];
    const password  = this.loginForm.value['password'];

    this.authService.login(email, password).subscribe(
      () => {
            this.postLogin();
      },
      error => {
        if (error instanceof HttpErrorResponse) {
          const httpErrorCode = error.status;
          switch (httpErrorCode) {
              case HttpStatusCode.OK:
                  console.info('ignoring 200, not an error : ' + httpErrorCode);
                  this.postLogin();
                  break;
               case HttpStatusCode.UNAUTHORIZED:
                  this.errorMessage = 'Your Credentials did not match any we have on record. Consider resetting your password';
                  break;
               case HttpStatusCode.FORBIDDEN:
                  this.errorMessage = 'Your Credentials are valid nut you do not have access to this page';
                  break;
              default:
                  this.showError(AppErrorHandler.REFRESH_PAGE_ON_TOAST_CLICK_MESSAGE);
          }
        } else {
          console.error('unknown error :' + error);
          this.appErrorHandler.handleError(error);
        }
      }
    );
  }

  private postLogin() {

    this.visible = false;

    this.authService.getUser().subscribe(
                    (foundUser: User) => {
                            this.showSuccess(foundUser.email);
                            this.store.dispatch(SetUser({user: foundUser}));
                    }
    );

    this.gtag.event('ijudy_login', {
      method: 'loginOK',
      event_category: 'security',
      event_label: 'User logged in'
    });

    if (this.selectedRedirectUrl) {
      this.router.navigate([this.selectedRedirectUrl]);
    } else {
      const url = IJudyConstants.RACES_URI;
      this.router.navigate([url]);
    }

  }

  private showSuccess(email?: string) {
    let msg: string;
    if (email) {
      msg = 'with user email : ' + email;
    }
    this.messageService.add(
      {severity: 'success',
      summary: 'Welcome',
      detail: 'Logged in ' + msg
    });
  }

  private showError(message: string) {
    this.messageService.add({severity: 'error', summary: 'Login',  detail: message});
  }
}

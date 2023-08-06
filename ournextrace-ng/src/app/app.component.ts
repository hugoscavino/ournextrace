import { Component, OnInit, OnDestroy } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { environment} from '../environments/environment';
import { Observable, Subscription } from 'rxjs';
import { RaceAppState, RaceStore, moduleKeyName } from './ngrx/race.app.state';
import { ResponsiveSizeInfoRx } from 'ngx-responsive';
import { Store, select } from '@ngrx/store';
import { SetScreenSize, SetUser, RaceTypeInit } from './ngrx/race.actions';
import { AuthService } from './service/auth';
import { RacesService } from './service/races';
import { ConfirmationService, MessageService } from 'primeng/api';

@Component({
  selector: 'ijudy-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent  implements OnInit, OnDestroy {

  public title = 'Our Next Race';
  public showLogin: boolean;
  public raceStateObs$: Observable<RaceAppState>;
  public raceReducerSubscription: Subscription;
  private _subscriptions: Subscription[] = [];

  public idleState = 'Not started.';
  public timedOut = false;
  public displayModal: boolean;

  constructor(
              public messageService: MessageService,
              public confirmationService: ConfirmationService,
              public router: Router,
              public responsiveSizeInfoRx: ResponsiveSizeInfoRx,
              public authService: AuthService,
              public racesService: RacesService,
              public store: Store<RaceStore>) {

    this.raceStateObs$ = store.pipe(select(moduleKeyName));

    if (environment.production) {
      this.router.events.subscribe(event => {
        if (event instanceof NavigationEnd) {
          (<any>window).ga('set', 'page', event.urlAfterRedirects);
          (<any>window).ga('send', 'pageview');
        }
      });
    }
  }

  ngOnInit() {

    this.reset();

    this._subscribe();
    this.responsiveSizeInfoRx.connect();
   }

  reset() {
    this.displayModal = false;
    this.idleState = 'Started.';
    this.timedOut = false;
  }

  public logout() {
      // Actual logic to perform a confirmation
      this.displayModal = false;
  }

  private _subscribe(): void {
    this._subscriptions.push(
      this.responsiveSizeInfoRx.getResponsiveSize.subscribe((data) => {
        // console.log('AppComponent::responsiveSizeInfoRx.getResponsiveSize ===>', data);
        this.store.dispatch(SetScreenSize({screenSize: data}));
      }, (err) => {
        console.error('ResponsiveSizeInfoRx Error', err);
      })
    );

    this._subscriptions.push(
      this.authService.getUser().subscribe((user) => {
        // console.log('AppComponent::Found a User in Session ===>', user);
        this.store.dispatch(SetUser({user: user}));
      }, (err) => {
        console.error('authService Error', err);
      })
    );
    this._subscriptions.push(
      this.racesService.getAllRaceTypes().subscribe((raceTypes) => {
        // console.log('AppComponent::Loaded Race Types  ===>', raceTypes.length);
        this.store.dispatch(RaceTypeInit({raceTypes: raceTypes}));
      }, (err) => {
        console.error('RacesService Error', err);

      })
    );
  }

  private _unsubscribe(): void {
      this._subscriptions.forEach((subscription: Subscription) => subscription.unsubscribe());
  }

  public ngOnDestroy(): void {
    this._unsubscribe();
    this.responsiveSizeInfoRx.disconnect();
  }
}

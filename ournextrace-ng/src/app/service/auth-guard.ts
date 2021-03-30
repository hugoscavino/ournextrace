import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot } from '@angular/router';
import { AuthService} from './auth';
import { IJudyConstants} from '../util/constants';
import { User} from '../domain/user';
import { Observable, Subscription } from 'rxjs';
import { RaceAppState, RaceStore, moduleKeyName } from '../ngrx/race.app.state';
import { Store, select } from '@ngrx/store';
import { SetUser } from '../ngrx/race.actions';

/**
 * Possible Roles used in the application
 */
export enum Role {
  /**
   * Admin user
   */
  ADMIN       = 'admin',

  /**
   * Default role for authenticated user
   */
  USER        = 'user',

  /**
   * Granted to users who can perform
   * functions that effect descriptions and limited race information
   */
  POWER_USER  = 'powerUser'
}

@Injectable()
export class AuthGuardService implements CanActivate {


  public raceStateObs$: Observable<RaceAppState>;
  public raceReducerSubscription: Subscription;

  public user: User;
  public isAdmin: boolean;
  public isPowerUser: boolean;

  constructor(public authService: AuthService,
              public store: Store<RaceStore>,
              public router: Router) {
    this.raceStateObs$ = store.pipe(select(moduleKeyName));

    this.raceReducerSubscription = this.raceStateObs$.subscribe(
      state => {
        this.user = state.user;
        if (this.user && this.user.user) {
          this.isAdmin      = state.user.admin;
          this.isPowerUser  = state.user.powerUser;
          // console.log('AuthGuardService email :  + admin:' + this.isAdmin + ', powerUser:' + this.isPowerUser);
        }

      });

  }

  public hasRole(expectedRole: Role): boolean {
      let result: boolean;
      if (!this.user) {
        console.warn('this.user was null');

        // Check if still in session just in case
        this.authService.getUserAsync().then(
          (userInSession: User) => {
            console.warn('ooh looks like you are still in session, so let grab that');
            this.user = userInSession;
            this.store.dispatch(SetUser({user: userInSession}));
            return true;
          }
        );
        return false;
      } else {
          switch (expectedRole) {
            case (Role.USER):
              if (this.user.user) {
                result = true;
              }
              break;
             case (Role.POWER_USER):
                if (this.user.powerUser) {
                  result = true;
                }
                break;
             case (Role.ADMIN):
                if (this.user.admin) {
                  result = true;
                }
                break;
              }
          return result;
      }

  }

  canActivate(route: ActivatedRouteSnapshot): boolean {
    // this will be passed from the route config
    // on the data property
    const expectedRole = route.data.expectedRole;
    const canProceed = this.hasRole(expectedRole);
    if (!canProceed) {
      console.warn('User does not have Expected Role : ' + expectedRole + ' routing to main page');
      this.router.navigate([IJudyConstants.RACES_URI]);
    }
    return canProceed;
  }
}

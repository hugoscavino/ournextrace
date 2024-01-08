import { Component, OnInit, Input } from '@angular/core';
import { Router, ResolveStart } from '@angular/router';
import { Store, select } from '@ngrx/store';
import { Observable, Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { MessageService } from 'primeng/api';
import { User} from '../../domain/user';
import { Header } from '../../domain/mode';
import { IJudyConstants} from '../../util/constants';
import { RaceStore, RaceAppState, moduleKeyName } from '../../ngrx/race.app.state';
import { AuthService } from 'src/app/service/auth';

@Component({
  selector: 'ijudy-toolbar',
  templateUrl: './toolbar.component.html',
  styleUrls: ['./toolbar.component.css']
})
export class ToolbarComponent implements OnInit  {

  private showLoginValue: boolean;

  @Input()
  set showLogin(val: boolean) {
    this.showLoginValue = val;
  }

  get showLogin() {
    return this.showLoginValue;
  }

  public photo: string;
  public user: User;

  public pageTitle = 'Search for a Race';
  public raceStateObs$: Observable<RaceAppState>;
  public raceReducerSubscription: Subscription;

  constructor(
              private messageService: MessageService,
              private router: Router,
              public authService: AuthService,
              public store: Store<RaceStore>) {

        this.raceStateObs$ = store.pipe(select(moduleKeyName));
  }

  ngOnInit() {

    this.raceReducerSubscription = this.raceStateObs$.subscribe(
        state => {
          this.user = state.user;
        }
    );

    // @see https://github.com/angular/angular/issues/19420
    this.router.events.pipe(
      filter(event => event instanceof ResolveStart),
      map(event => {
        let data = null;
        let route = event['state'].root;
        while (route) {
          data = route.data || data;
          route = route.firstChild;
        }
        return data;
      }),
      ).subscribe(
        data => {
          // console.log('Loading header : ' + data.header);
          const header = data.header as string;
          switch (header) {
            case Header.WELCOME:
                this.pageTitle  = IJudyConstants.WELCOME_TITLE;
                this.photo      = IJudyConstants.IMG_ROOT + IJudyConstants.RACE_IMG;
                break;
            case Header.ABOUT_US:
                this.pageTitle  = IJudyConstants.ABOUT_US_TITLE;
                this.photo      = IJudyConstants.IMG_ROOT + IJudyConstants.RACE_IMG;
                break;
            case Header.RACES:
                this.pageTitle  = IJudyConstants.RACE_TITLE;
                this.photo      = IJudyConstants.IMG_ROOT + IJudyConstants.RACE_IMG;
                break;
            case Header.FORGOT:
                this.pageTitle  = IJudyConstants.FORGOT_TITLE;
                this.photo      = IJudyConstants.IMG_ROOT + IJudyConstants.FORGOT_IMG;
                break;
            case Header.REGISTRATION:
                this.pageTitle  = IJudyConstants.REGISTER_TITLE;
                this.photo      = IJudyConstants.IMG_ROOT + IJudyConstants.REGISTER_IMG;
                break;
            case Header.CONTACT_US:
                this.pageTitle  = IJudyConstants.CONTACT_US_TITLE;
                this.photo      = IJudyConstants.IMG_ROOT + IJudyConstants.CONTACT_US_IMG;
                break;
            default:
                console.warn('Loading Defaults');
                this.pageTitle = IJudyConstants.RACE_TITLE;
                this.photo = IJudyConstants.IMG_ROOT + IJudyConstants.RACE_IMG;
          }
           // console.log('Loading pageTitle : ' + this.pageTitle);
           // console.log('Loading photo : ' + this.photo);
          });

  }
    public routeToUpdateProfile() {
    this.router.navigate([IJudyConstants.UPDATE_PROFILE_URI]);
  }
  public routeToAddRace() {
    this.router.navigate([IJudyConstants.ADD_RACE_URI]);
  }
  public SearchForEvents() {
    this.router.navigate([IJudyConstants.RACES_URI]);
  }
  public routeToHomePage() {
    this.routeToRaces();
  }
  public routeToRaces() {
    this.router.navigate([IJudyConstants.RACES_URI]);
  }

}

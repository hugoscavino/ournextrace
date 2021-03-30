import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { MessageService } from 'primeng/api';
import { select, Store } from '@ngrx/store';
import { RaceAppState, moduleKeyName, RaceStore } from '../../ngrx/race.app.state';
import { User } from '../../domain/user';
import { IJudyConstants } from '../../util/constants';
import { LogoutUser } from 'src/app/ngrx/race.actions';
import { AuthService } from 'src/app/service/auth';

@Component({
  selector: 'ijudy-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {

  public showLogin: boolean;
  public user: User;
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

  public raceStateObs$: Observable<RaceAppState>;
  public raceReducerSubscription: Subscription;

  constructor(public messageService: MessageService,
              public authService: AuthService,
              public router: Router,
              public store: Store<RaceStore>) {

              this.raceStateObs$ = store.pipe(select(moduleKeyName));
  }

  ngOnInit() {

    this.raceReducerSubscription = this.raceStateObs$.subscribe(
      state => {
        this.user = state.user;
      }
    );
  }

  public routeToUpdateProfile() {
    this.showSideMenu(false);
    this.router.navigate([IJudyConstants.UPDATE_PROFILE_URI]);
  }
  public routeToAddRace() {
    this.showSideMenu(false);
    this.router.navigate([IJudyConstants.ADD_RACE_URI]);
  }
  public SearchForEvents() {
    this.showSideMenu(false);
    this.router.navigate([IJudyConstants.RACES_URI]);
  }
  public routeToHomePage() {
    this.routeToRaces();
  }
  public routeToRaces() {
    this.showSideMenu(false);
    this.router.navigate([IJudyConstants.RACES_URI]);
  }
  public routeToManageRaces() {
    this.showSideMenu(false);
    this.router.navigate([IJudyConstants.RACE_MGR_URI]);
  }
  public routeToManageLocations() {
    this.showSideMenu(false);
    this.router.navigate([IJudyConstants.LOCATION_MGR_URI]);
  }
  public routeToRegistration() {
    this.showSideMenu(false);
    this.router.navigate([IJudyConstants.REGISTER_URI]);
  }
  public routeToAddLocation() {
    this.showSideMenu(false);
    this.router.navigate([IJudyConstants.LOCATION_URI]);
  }

  public routeToContactUs() {
    this.showSideMenu(false);
    this.router.navigate([IJudyConstants.CONTACT_US_URI]);
  }
  public showLoginPanel() {
    this.visible = false;
    this.showLogin = true;
  }

  public logoff() {
        this.store.dispatch(LogoutUser());
        this.visible = false;
        this.authService.logout().subscribe(
          () => {
            this.messageService.add({severity: 'success', summary: 'Logged off', detail: 'You are successfully Logged off'});
            this.router.navigate([IJudyConstants.WELCOME_URI]);
          },
          (error) => {
            console.error(error);
            this.messageService.add({severity: 'error', summary: 'Not Logged off', detail: 'You Are Not Logged off. Exit Browser'});
          }
        );
  }

  public showSideMenu(value: boolean) {
    this.visible = value;
  }

  public userLoaded(): boolean {
    return (this.user !== null) && (this.user.user);
  }

  public adminLoaded(): boolean {
    return this.userLoaded() && (this.user.admin);
  }

  public userNotLoaded(): boolean {
    return this.userLoaded() === false;
  }

}

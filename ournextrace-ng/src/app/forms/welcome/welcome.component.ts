import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Store, select } from '@ngrx/store';
import { RaceAppState, RaceStore, moduleKeyName } from '../../ngrx/race.app.state';
import { SetRaceFilter } from '../../ngrx/race.actions';

import { ISearchFilters } from '../../domain/search-filters';
import { RaceType } from '../../domain/race';
import { IJudyConstants } from '../../util/constants';
import { Observable, Subscription } from 'rxjs';
import { map } from 'rxjs/operators';
import { User } from 'src/app/domain/user';
import { ScreenSizeType, ScreenSizeValues } from 'src/app/domain/screen-size';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'ijudy-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.css']
})
export class WelcomeComponent implements OnInit {

  public raceTypes: RaceType[];
  public selectedRaceTypes: RaceType[];
  public _showSideMenu: boolean;
  public raceStateObs$: Observable<RaceAppState>;
  public raceReducerSubscription: Subscription;
  public user: User;
  public screenSizeType: ScreenSizeType;
  public defaultLabel = 'Search for all Race Types';
  public filterPlaceHolder = 'Choose from the list';

  constructor(public router: Router,
              public store: Store<RaceStore>) {
              this.raceStateObs$ = store.pipe(select(moduleKeyName)); }

  ngOnInit() {
    this.raceReducerSubscription = this.raceStateObs$
      .pipe(
        map(state => {
              this.user = state.user;
              this.screenSizeType = state.screenSize;
          }
        )
      ).subscribe();
  }

  public isDeskTop() {
    return (this.screenSizeType === ScreenSizeValues.XL) || (this.screenSizeType === ScreenSizeValues.LG);
  }

  public showSideMenu(value: boolean) {
    this._showSideMenu = value;
  }

  public redirectToSearchPage(event: any): void {
    this.router.navigate([IJudyConstants.RACES_URI]);
  }

}

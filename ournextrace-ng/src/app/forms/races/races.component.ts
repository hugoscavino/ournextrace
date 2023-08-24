import {Component, OnInit, ViewChild} from '@angular/core';
import {LocationStrategy, PathLocationStrategy} from '@angular/common';
import {select, Store} from '@ngrx/store';
import {map} from 'rxjs/operators';
import {Observable, Subscription} from 'rxjs';
import {Router} from '@angular/router';

import * as moment from 'moment';

import {ConfirmationService, MessageService, SelectItem} from 'primeng/api';
import {InputText} from 'primeng/inputtext';

import {moduleKeyName, RaceAppState, RaceStore} from '../../ngrx/race.app.state';

import {MyRacesService} from '../../service/my-races';
import {RacesService} from '../../service/races';
import {AppErrorHandler} from '../../service/error-handler/app-error-handler';

import {User} from '../../domain/user';
import {MyRace, RaceStatus, RaceType} from '../../domain/race';
import {IJudyConstants} from '../../util/constants';
import {ISearchFilters} from '../../domain/search-filters';
import * as _ from 'underscore';

@Component({
  selector: 'ijudy-races',
  providers: [Location, {provide: LocationStrategy, useClass: PathLocationStrategy}],
  templateUrl: './races.component.html',
  styleUrls: ['./races.component.css']
})
export class RacesComponent implements OnInit {

  public readonly justMineButtonTextLabel = 'Only My Races';
  public readonly allButtonTextLabel = 'All Races';
  public readonly reapplyFiltersTextLabel = 'Unfiltered';
  public readonly clearFiltersTextLabel = 'Filtered';

  public displayLogin = false;
  public filterVisible = false;

  @ViewChild('gb1',  {static: true})
  private inputtext: InputText;

  public attendingStatus: RaceStatus = RaceStatus.INTERESTED;

  public selectedRaceTypesObs: Observable<RaceType[]>;
  public selectedRaceTypes: RaceType[];

  public searchFilters: ISearchFilters;

  public justMineAllButtonText = this.justMineButtonTextLabel;

  public racesViews: MyRace[];
  public raceTypeFilter: string;
  public originalRaceViews: MyRace[];
  public viewOptions: SelectItem[];
  public viewKey: string;

  public sortDateToggle: string;
  public sortNameToggle: string;
  public sortLocationToggle: string;
  public filterLikeToggle: string;

  public emptyMessage: string;

  public user: User;
  public userFound = false;

  public displaySideMenu = false;


  public raceStateObs$: Observable<RaceAppState>;
  public raceReducerSubscription: Subscription;

  constructor(
                public errorHandler: AppErrorHandler,
                public router: Router,
                public myRacesService: MyRacesService,
                public store: Store<RaceStore>) {
                this.raceStateObs$ = store.pipe(select(moduleKeyName));
  }

  ngOnInit() {

    this.sortDateToggle     = 'date';
    this.sortNameToggle     = 'name';
    this.sortLocationToggle = 'loc';
    this.filterLikeToggle   = 'like';

    this.raceReducerSubscription = this.raceStateObs$.pipe(
        map((state: RaceAppState) => {
              this.user       = state.user;
              this.getMyRaces();
          }
        )
      ).subscribe();

  }


public getMyRaces() {

    const beginDateStr = moment().format(IJudyConstants.ISO_DATE_FMT);
    const endDateStr = moment().add(1, 'year').format(IJudyConstants.ISO_DATE_FMT);
    //console.log('getMyRaces called');
    this.myRacesService.getMyRaces(beginDateStr, endDateStr).subscribe(
        (races: MyRace[]) => {
            //console.log('getMyRaces subscribe called' + races.length);
            this.originalRaceViews = races;
          this.racesViews = races;
        },
        err => {
          console.error(err);
          this.errorHandler.handleError(err);
        }
      );
  }

  public onSortDateClicked(event: any) {

    if (this.sortDateToggle.indexOf('!') === 0) {
      this.sortDateToggle = this.sortDateToggle.substring(1, this.sortDateToggle.length);
      this.racesViews = _.sortBy( this.racesViews, function(raceViews) {
                                  return raceViews.race.date;
                                });
    } else {
      this.sortDateToggle = '!' + this.sortDateToggle;
      this.racesViews = _.sortBy( this.racesViews, function(raceViews) {
                                  return raceViews.race.date;
                              }).reverse();
    }

 
}


public onSortNameClicked(event: any) {

    if (this.sortNameToggle.indexOf('!') === 0) {
      this.sortNameToggle = this.sortNameToggle.substring(1, this.sortNameToggle.length);
      this.racesViews = _.sortBy( this.racesViews, function(raceViews) {
                                      return raceViews.race.name.toLocaleLowerCase();
                                  });
    } else {
      this.sortNameToggle = '!' + this.sortNameToggle;
      this.racesViews = _.sortBy( this.racesViews, function(raceViews) {
                                    return raceViews.race.name.toLocaleLowerCase();
                                  }).reverse();
    }
  }

  public onSortLocationClicked(event: any) {

    if (this.sortLocationToggle.indexOf('!') === 0) {
      this.sortLocationToggle = this.sortLocationToggle.substring(1, this.sortLocationToggle.length);
      this.racesViews = _.sortBy( this.racesViews, function(raceViews) {
                                      return raceViews.race.address.location.toLocaleLowerCase();
                                  });
    } else {
      this.sortLocationToggle = '!' + this.sortLocationToggle;
      this.racesViews = _.sortBy( this.racesViews, function(raceViews) {
                                    return raceViews.race.address.location.toLocaleLowerCase();
                                  }).reverse();
    }
  }

}

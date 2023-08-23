import { Component, OnInit, ViewChild } from '@angular/core';
import { LocationStrategy, PathLocationStrategy} from '@angular/common';
import { Store, select } from '@ngrx/store';
import { map } from 'rxjs/operators';
import { Observable, Subscription } from 'rxjs';
import { Router } from '@angular/router';

import * as moment from 'moment';

import { SelectItem, MessageService, ConfirmationService} from 'primeng/api';
import { InputText} from 'primeng/inputtext';

import { RaceAppState, RaceStore, moduleKeyName } from '../../ngrx/race.app.state';

import { MyRacesService } from '../../service/my-races';
import { RacesService} from '../../service/races';
import { AppErrorHandler} from '../../service/error-handler/app-error-handler';

import { User} from '../../domain/user';
import { MyRace, RaceStatus, RaceType} from '../../domain/race';
import { IJudyConstants} from '../../util/constants';
import { ISearchFilters } from '../../domain/search-filters';
import * as _ from 'underscore';
import { HttpErrorResponse } from '@angular/common/http';
import { HttpStatusCode } from 'src/app/service/error-handler/status-codes';

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

  constructor(  public confirmationService: ConfirmationService,
                public messageService: MessageService,
                public errorHandler: AppErrorHandler,
                public router: Router,
                public racesService: RacesService,
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
              this.userFound  = state.user !== null;
              this.getMyRaces();
          }
        )
      ).subscribe();

  }


public getMyRaces() {

    const beginDateStr = moment().format(IJudyConstants.ISO_DATE_FMT);
    const endDateStr = moment().add(1, 'year').format(IJudyConstants.ISO_DATE_FMT);

    this.myRacesService.getMyRaces(beginDateStr, endDateStr).subscribe(
        (races: MyRace[]) => {
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

  public register() {
    this.displayLogin = true;
  }

  public showSideMenu(){
    this.displaySideMenu = true;
  }

  public raceLikedClicked(myRace: MyRace) {

    if (this.userNotLoaded() ) {
      this.displayLogin = true;
    } else {
      this.displayLogin = false;
      if (myRace.myRaceStatus === RaceStatus.INTERESTED){
        myRace.myRaceStatus = RaceStatus.NOT_ASSIGNED;
      } else {
        myRace.myRaceStatus = RaceStatus.INTERESTED;
      }
      this.likeUnLikeTheRace(myRace);

    }   
  }

  private likeUnLikeTheRace(myRace: MyRace){
    console.info('Setting Like to ' + myRace.myRaceStatus)
    this.myRacesService.likeUnLikeRace(myRace).subscribe(
      (myRace) => {
          this.getMyRaces();
          var summaryMsg = 'UnLiked a Race';
          var detailMsg = myRace.race.name + ' unLiked'; 
          if (myRace.myRaceStatus === RaceStatus.INTERESTED){
            summaryMsg = 'Liked a Race';
            detailMsg = myRace.race.name + ' liked'
          }
          this.messageService.add(
            {severity: 'success',
            summary: summaryMsg,
            detail: detailMsg});
      },
      error => {
          if (error instanceof HttpErrorResponse) {
              const httpErrorCode = error.status;
              switch (httpErrorCode) {
                  case HttpStatusCode.CONFLICT:
                  this.messageService.add(
                      {severity: 'warn',
                      summary: 'Liked Races',
                      detail: 'Race likeness not changed'});
                      this.router.navigate([IJudyConstants.RACES_URI]);
                      break;
                  default:
                      throw error;
              }
          }
      }
    );
  }

  public userNotLoaded(): boolean {
    const userNotFound = this.user === null || this.user === undefined || this.user.user === false;
    return userNotFound;
  }

  
  public onApplyJustMyRacesFilter(event?: any) {

    if (this.filterLikeToggle.indexOf('!') === 0) {
      this.filterLikeToggle = this.filterLikeToggle.substring(1, this.filterLikeToggle.length);
      this.racesViews = RacesComponent.filterJustMyRaces(this.originalRaceViews);
    } else {
      this.filterLikeToggle = '!' + this.filterLikeToggle;
      this.racesViews = this.originalRaceViews;
    }

  }

  public static filterJustMyRaces(racesViews: MyRace[]): MyRace[] {
    const filtered: MyRace[] = racesViews.filter( OneRace => OneRace.myRaceStatus !== RaceStatus.NOT_ASSIGNED );
    return filtered;
  }
}

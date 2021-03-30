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
import { SetRedirectUrl, SetFormEditMode } from '../../ngrx/race.actions';

import { MyRacesService } from '../../service/my-races';
import { RacesService} from '../../service/races';
import { AppErrorHandler} from '../../service/error-handler/app-error-handler';

import { User} from '../../domain/user';
import { Race, MyRace, RaceStatus, RaceType, IRaceType, IMyRace, RaceStatusPrettyPrinter} from '../../domain/race';
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

  public sortToggle: string;

  public emptyMessage: string;

  public dateFilters: any;

  public user: User;
  public userFound = false;
  public racesNameSortSelected = true;
  public racesNameSortSelectedByAlpha = false;

  public displaySideMenu = false;
  public dateSortSelected = false;
  public dateSortSelectedAlpha = false;

  public myRacesSelected = false;

  public selectedRedirectUrl: string;
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

  public static resetMyRaceStatus(racesViews: MyRace[], raceId: number): void {
    const racesToDelete: MyRace[] = racesViews.filter( OneRace => OneRace.race.id === raceId );
    for (const myRace of racesToDelete) {
      myRace.myRaceStatus = RaceStatus.NOT_ASSIGNED;
    }
  }

  public static filterJustMyRaces(racesViews: MyRace[]): MyRace[] {
      const filtered: MyRace[] = racesViews.filter( OneRace => OneRace.myRaceStatus !== RaceStatus.NOT_ASSIGNED );
      return filtered;
  }

  private static filterByRacesTypeCallBack(element: MyRace, index: number, array: MyRace[]): boolean {
    if (element.race.raceTypes) {
      const filter = this as unknown as RaceType[];
      for (const raceType of element.race.raceTypes) {
          for (const filterRaceType of filter) {
            if (raceType.name === filterRaceType.name) {
              return true;
            }
          }
      }
    }
    return false;
  }

  public static filterMyRacesByRaceType(raceViews: MyRace[], raceTypes: RaceType[]): MyRace[] {
    if (raceViews) {
      const filteredViews = raceViews.filter(this.filterByRacesTypeCallBack, raceTypes);
      return filteredViews;
    }
  }


  ngOnInit() {

    this.sortToggle = 'race';

    this.raceReducerSubscription = this.raceStateObs$.pipe(
        map((state: RaceAppState) => {
              this.user       = state.user;
              this.userFound  = this.user !== null;
              this.selectedRedirectUrl = state.redirectUrl;
              if (state.filters) {
                // console.log('getMyRaces with filter : ' + JSON.stringify(state.filters));
                this.getMyRaces(state.filters);
              } else {
                this.selectedRaceTypes  = undefined;
                // console.log('getMyRaces with no filter');
                if (this.originalRaceViews) {
                  this.racesViews = this.originalRaceViews;
                } else {
                  // go to db
                  this.getMyRacesUnFiltered();
                }
              }
          }
        )
      ).subscribe();

  }

/**
 * Load the list of Races publicly visible
 *
 */
public getMyRaces(filters: ISearchFilters) {

  if (filters === undefined) {
    return;
  }

  this.selectedRaceTypes  = filters.selectedRaceTypes;

  let beginDateStr = moment().format(IJudyConstants.ISO_DATE_FMT);
  let endDateStr = moment().add(1, 'year').format(IJudyConstants.ISO_DATE_FMT);
  if (filters.minDate && filters.maxDate) {
    beginDateStr      = moment(filters.minDate).format(IJudyConstants.ISO_DATE_FMT);
    endDateStr        = moment(filters.maxDate).format(IJudyConstants.ISO_DATE_FMT);
  }

  this.myRacesService.getMyRaces(beginDateStr, endDateStr).subscribe(
      (races: MyRace[]) => {

        this.originalRaceViews = races;

        const myRaces = RacesComponent.filterJustMyRaces(this.originalRaceViews);
        const myRacesExist = myRaces !== undefined && myRaces.length > 0;
        this.dateSortSelected  = false;

        if (myRacesExist) {
              this.racesNameSortSelected  = false;
              this.myRacesSelected        = true;
              this.racesViews = myRaces;
        } else {
            if (this.selectedRaceTypes && this.selectedRaceTypes.length > 0) {
              this.racesViews = RacesComponent.filterMyRacesByRaceType(races, this.selectedRaceTypes);
              this.racesNameSortSelected  = true;
              this.myRacesSelected        = false;
            } else {
              this.racesNameSortSelected  = false;
              this.myRacesSelected        = true;
              this.racesViews = races;
            }
        }

      },
      err => {
        console.error(err);
        this.errorHandler.handleError(err);
      }
    );
}

public getMyRacesUnFiltered() {

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

  public isAssigned(myRace: IMyRace): boolean {
      if (this.user) {
        const isMyRace = (myRace.myRaceStatus !== RaceStatus.NOT_ASSIGNED);
        return isMyRace;
      }
      return false;
  }

  public isOwner(myRace: IMyRace): boolean {
    if (this.user) {
      const isOwner = (myRace.race.author.id !== this.user.id);
      return isOwner;
    }
    return false;
}

  public registeredForRaceType(myRace: IMyRace, raceType: IRaceType) {
    const myRaceTypes = myRace.raceTypes;
    if ( myRaceTypes ) {
      myRaceTypes.forEach(
        (myRaceType: IRaceType) => {
            if (myRaceType.id === raceType.id) {
                return true;
            }
          }
      );

    }
    return false;
  }

  public isPrivateRace(myRace: MyRace): boolean {
      const isPrivate = this.user && (myRace.race.public === false) && (this.user.id === myRace.race.author.id);
      return isPrivate;
  }
  public onDateClicked(event: any) {

    if (this.selectedRaceTypes === undefined || this.selectedRaceTypes.length === 0) {
      this.racesViews = this.originalRaceViews;
    } else {
      this.racesViews = RacesComponent.filterMyRacesByRaceType(this.originalRaceViews, this.selectedRaceTypes);
    }

  }

  public onSortDateClicked(event: any) {

    if (this.selectedRaceTypes === undefined || this.selectedRaceTypes.length === 0) {
      this.racesViews = this.originalRaceViews;
    } else {
      this.racesViews = RacesComponent.filterMyRacesByRaceType(this.originalRaceViews, this.selectedRaceTypes);
    }

    if (this.sortToggle.indexOf('!') === 0) {
      this.dateSortSelectedAlpha = false;
      this.sortToggle = this.sortToggle.substring(1, this.sortToggle.length);
      this.racesViews = _.sortBy( this.racesViews, function(raceViews) {
                                  return raceViews.race.date;
                                });
    } else {
      this.dateSortSelectedAlpha = true;
      this.sortToggle = '!' + this.sortToggle;
      this.racesViews = _.sortBy( this.racesViews, function(raceViews) {
                                  return raceViews.race.date;
                              }).reverse();
    }

    this.dateSortSelected     = true;
    this.racesNameSortSelected = false;
    this.myRacesSelected  = false;

}

public onNameClicked(event: any) {

  if (this.selectedRaceTypes === undefined || this.selectedRaceTypes.length === 0) {
    this.racesViews = this.originalRaceViews;
  } else {
    this.racesViews = RacesComponent.filterMyRacesByRaceType(this.originalRaceViews, this.selectedRaceTypes);
  }

  this.dateSortSelected     = false;
  this.racesNameSortSelected = true;
  this.myRacesSelected  = false;

}

public onSortNameClicked(event: any) {

  if (this.selectedRaceTypes === undefined || this.selectedRaceTypes.length === 0) {
    this.racesViews = this.originalRaceViews;
  } else {
    this.racesViews = RacesComponent.filterMyRacesByRaceType(this.originalRaceViews, this.selectedRaceTypes);
  }

  if (this.sortToggle.indexOf('!') === 0) {
    this.racesNameSortSelectedByAlpha = false;
    this.sortToggle = this.sortToggle.substring(1, this.sortToggle.length);
    this.racesViews = _.sortBy( this.racesViews, function(raceViews) {
                                    return raceViews.race.name.toLocaleLowerCase();
                                });
  } else {
    this.racesNameSortSelectedByAlpha = true;
    this.sortToggle = '!' + this.sortToggle;
    this.racesViews = _.sortBy( this.racesViews, function(raceViews) {
                                  return raceViews.race.name.toLocaleLowerCase();
                                }).reverse();
  }


}

public register() {
  this.displayLogin = true;
}

public showSideMenu(){
  this.displaySideMenu = true;
}

/**
 * Add a race to potentially share with
 * community it is made private initially
 */
 public onAddRace() {
    if (this.userNotLoaded() ) {
      this.displayLogin = true;
    } else {
      const url = IJudyConstants.ADD_RACE_URI_STEP;
      this.router.navigate([url, 0]);
    }
 }

/**
 * Add a Race to My Calendar
 * @param raceId from the UI list
 */
public addMyRace(raceId: number) {

    this.store.dispatch(SetFormEditMode());
    const addUrl     = IJudyConstants.ADD_MY_RACE_URI + '/' + raceId;

    if (this.userNotLoaded() ) {
      this.store.dispatch(SetRedirectUrl({redirectUrl: addUrl}));
      this.displayLogin = true;
    } else {
      this.displayLogin = false;
      const editUrl = IJudyConstants.EDIT_MY_RACE_URI + '/' + raceId;
      this.myRacesService.getMyRaceCheck(raceId).then(
        (found: boolean) => {
            if (found) {
              this.router.navigate([editUrl]);
            } else {
              this.router.navigate([addUrl]);
            }
        }
      );
    }

  }

  public raceLiked(raceId: number) {

    const addUrl     = IJudyConstants.ADD_MY_RACE_URI + '/' + raceId;

    if (this.userNotLoaded() ) {
      this.store.dispatch(SetRedirectUrl({redirectUrl: addUrl}));
      this.displayLogin = true;
    } else {
      this.displayLogin = false;
      const myRace = this.toMyRace(raceId);
      this.myRacesService.likeRace(myRace).subscribe(
        (myRace) => {
            this.messageService.add(
              {severity: 'success',
              summary: 'Add Race to Schedule',
              detail: myRace.race.name + ' liked'});
              this.router.navigate([IJudyConstants.RACES_URI]);
        },
        error => {
            if (error instanceof HttpErrorResponse) {
                const httpErrorCode = error.status;
                switch (httpErrorCode) {
                    case HttpStatusCode.CONFLICT:
                    this.messageService.add(
                        {severity: 'warn',
                        summary: 'My Races',
                        detail: 'Race already liked'});
                        this.router.navigate([IJudyConstants.RACES_URI]);
                        break;
                    default:
                        throw error;
                }
            }
        }
      );
    }
        

  }

  private toMyRace(raceId: number): MyRace {
    const myRace = new MyRace();
    myRace.race = new Race();
    myRace.race.id      = raceId;
    myRace.myRaceStatus = RaceStatus.INTERESTED;
    return myRace;
  }

  public userNotLoaded(): boolean {
    const userNotFound = this.user === null || this.user === undefined || this.user.user === false;
    return userNotFound;
  }

  public updateMyRace(raceId: string) {
    if (this.user ) {
      this.router.navigate([IJudyConstants.EDIT_MY_RACE_URI + '/' + raceId, {editMode : 'edit'}]);
    } else {
      this.displayLogin = true;
    }
  }

  public updateRaceStatus(raceStatus: RaceStatus, raceId: number) {

    if (this.userFound) {
      if (raceStatus === RaceStatus.NOT_ASSIGNED) {
        this.router.navigate([IJudyConstants.EDIT_MY_RACE_URI + '/' + raceId, {editMode : 'edit'}]);
      } else {
        if (raceStatus === RaceStatus.DELETE_ME) {
          // delete the race
          // optional prompt
          this.deleteMyRace(raceId);
        } else {
          this.myRacesService.updateRaceStatus(raceId, raceStatus).subscribe(
            (myRace: IMyRace) => {
                  const msg = 'Now ' + RaceStatusPrettyPrinter.toPrettyString(raceStatus) + ' for ' + myRace.race.name;
                  this.messageService.add({severity: 'success', summary: 'Status Updated', detail: msg});
              }
            );
          }
        }
    } else {
      console.error('User not set');
    }
  }

  public deleteMyRace(raceId: number) {
    this.confirmationService.confirm({
        message: 'Are you sure that you want to remove this race from your plan?',
        accept: () => {
          this.deleteMyRaceApi(raceId);
          RacesComponent.resetMyRaceStatus(this.originalRaceViews, raceId);
          if (this.myRacesSelected) {
            this.onApplyJustMyRacesFilter();
          }
        }
      });
  }

  public deleteMyRaceApi(raceId: number) {
        this.myRacesService.deleteMyRace(raceId).subscribe(
          (result: IMyRace) => {
              this.messageService.add(
                {
                  severity: 'success',
                  summary: 'My Race',
                  detail: 'Deleted ' + result.race.name + ' from your plan'}
                );
          },
          (error) => {
            this.messageService.add(
              {
                severity: 'error',
                summary: 'My Race',
                detail: 'Could not delete race from your race plan - ' + error}
              );
        }
        );
  }


  public onRemoveFilters() {
        this.racesViews = this.originalRaceViews;
        this.raceTypeFilter = undefined;
  }


  public filterClicked(event: any) {
      this.filterVisible = true;
  }

  public onApplyJustMyRacesFilter(event?: any) {

    this.racesViews = RacesComponent.filterJustMyRaces(this.originalRaceViews);

    if (this.sortToggle.indexOf('!') === 0) {
      this.sortToggle = this.sortToggle.substring(1, this.sortToggle.length);
      this.racesViews = _.sortBy( this.racesViews, function(raceViews) {
                                      return raceViews.race.name.toLocaleLowerCase();
                                  });
    } else {
      this.sortToggle = '!' + this.sortToggle;
      this.racesViews = _.sortBy( this.racesViews, function(raceViews) {
                                    return raceViews.race.name.toLocaleLowerCase();
                                  }).reverse();
    }
  }
}

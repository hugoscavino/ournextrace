import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Observable, Subscription } from 'rxjs';
import { map } from 'rxjs/operators';
import { Store, select } from '@ngrx/store';
import * as moment from 'moment';

import { RaceAppState, RaceStore, moduleKeyName } from '../../ngrx/race.app.state';
import { SetRaceFilter } from '../../ngrx/race.actions';

import { ISearchFilters, initialFilter } from '../../domain/search-filters';
import { RaceType } from '../..//domain/race';
import { User } from '../../domain/user';
import { IJudyConstants } from '../../util/constants';

@Component({
  selector: 'ijudy-filter',
  templateUrl: './filter.component.html',
  styleUrls: ['./filter.component.css']
})
export class FilterComponent implements OnInit {

  private visibleVal = false;
  @Output() visibleChange = new EventEmitter<boolean>();

  @Input()
  public get visible() {
      return this.visibleVal;
  }
  public set visible(val: boolean) {
    this.visibleVal = val;
    this.visibleChange.emit(val);
  }

  public nativeMinDate: Date;
  public nativeMaxDate: Date;
  public searchFilters: ISearchFilters;
  public allPastRaces: boolean;
  public selectedRaceTypes: RaceType[];
  public user: User;

  public raceStateObs$: Observable<RaceAppState>;
  public raceReducerSubscription: Subscription;

  constructor(public store: Store<RaceStore>) {
    this.raceStateObs$ = store.pipe(select(moduleKeyName));
  }

  ngOnInit() {
    this.raceReducerSubscription = this.raceStateObs$
    .pipe(
      map(state => {
            this.user = state.user;
            this.selectedRaceTypes = state.filters.selectedRaceTypes;
            this.nativeMinDate = moment(state.filters.minDate).toDate();
            this.nativeMaxDate = moment(state.filters.maxDate).toDate();
         }
      )
    ).subscribe();
  }

  public onApplyFilter(event: any) {
    const filters: ISearchFilters = {
      minDate: moment(this.nativeMinDate).format(IJudyConstants.ISO_DATE_FMT),
      maxDate: moment(this.nativeMaxDate).format(IJudyConstants.ISO_DATE_FMT),
      selectedRaceTypes:  this.selectedRaceTypes
    };
    console.log('Chose to filter : ' + JSON.stringify(filters));

    this.store.dispatch(SetRaceFilter({filters: filters}));
  }

  public filtersChanged(event: any) {
    this.onApplyFilter(event);
    this.visible = true;
  }

  public onReset(event: any) {
    this.store.dispatch(SetRaceFilter({filters: initialFilter}));
    this.selectedRaceTypes = [];
    this.visible = false;

  }

  public onHide(event: any) {
    this.onApplyFilter(event);
    this.visible = false;
  }

  /**
   * event.month: New month
   * event.year: New year
   * @param $event
   * @param minDate are we adjusting the Min Date otherwise the Max Date
   */
  public yearMonthChanged($event: any, minDate: boolean) {
    const newDate = moment($event.year + '-' + $event.month + '-' + 1, 'YYYY-MM-DD');
    if (minDate) {
      this.nativeMinDate = newDate.toDate();
    } else {
      this.nativeMaxDate = newDate.toDate();
    }
    this.onApplyFilter($event);
    this.visible = true;

  }

}

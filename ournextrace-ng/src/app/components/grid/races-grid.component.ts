import { Component, OnInit, ViewChild } from '@angular/core';
import { AgGridAngular } from 'ag-grid-angular';
import { AllCommunityModules, Module } from '@ag-grid-community/all-modules';
import { GridApi, GridOptions, ColumnApi, AgGridEvent, FirstDataRenderedEvent, ColDef } from 'ag-grid-community';

import { SelectItem } from 'primeng/api/selectitem';

import { AddressService } from '../../service/address';
import { RacesService } from '../..//service/races';
import { RacesDefaultDefinitions } from './races-col-def';
import { Race, RaceType, IRaceType } from '../../domain/race';
import { Observable, Subscription } from 'rxjs';
import { RaceAppState, RaceStore, moduleKeyName } from 'src/app/ngrx/race.app.state';
import { Store, select } from '@ngrx/store';
import { map } from 'rxjs/operators';
import { Address, IAddress } from 'src/app/domain/address';

@Component({
    selector: 'ijudy-grid',
    templateUrl: './races-grid.component.html',
    styleUrls: ['./races-grid.component.css']
})
export class RacesGridComponent implements OnInit {

    @ViewChild('agGrid') agGrid: AgGridAngular;

    public rowData: Race[] = [];
    public raceTypes: IRaceType[] = [];
    public locations: IAddress[]  = [];
    public gridApi: GridApi;
    public gridColumnApi: ColumnApi;
    public gridOptions: GridOptions                 = RacesDefaultDefinitions.gridOptions;
    public defaultColDef: any                       = RacesDefaultDefinitions.defaultColDef;
    public columnDefs: ColDef []                    = RacesDefaultDefinitions.columnDefs;
    public modules: Module[] = AllCommunityModules;
    public raceStateObs$: Observable<RaceAppState>;
    public raceReducerSubscription: Subscription;

    public addressServiceSubscription: Subscription;
    /**
     *
     * @param addressService ctor
     * @param racesService
     */
    constructor(
        public addressService: AddressService,
        public racesService: RacesService,
        public store: Store<RaceStore>) {
          this.raceStateObs$ = store.pipe(select(moduleKeyName));
          this.loadRaceTypes();
    }

   private loadRaceTypes() {
      this.raceReducerSubscription = this.raceStateObs$
      .pipe(
        map(state => {
              this.raceTypes = state.raceTypes;
          }
        )
      ).subscribe();

    }

    ngOnInit() {
      this.loadRaces();
      this.loadLocations();
    }
    public loadRaces() {
        this.racesService.getAllRacesForManager().subscribe(
            races => {
              this.rowData = races;
            },
            err => {
              console.error(err);
            }
          );
    }

    private loadLocations() {
      this.addressServiceSubscription = this.addressService.getAddresses().subscribe(
        (locations: Address[]) => {
          this.locations = locations;
        }
      );
    }

    public onGridReady(event: AgGridEvent) {
        this.gridApi = event.api;

        const raceTypeColDef: ColDef = this.gridApi.getColumnDef(RacesDefaultDefinitions.raceTypeCol.colId);
        raceTypeColDef.cellEditorParams = { values: this.raceTypes};

        this.gridColumnApi = event.columnApi;
    }

    public onFirstDataRendered(params: FirstDataRenderedEvent) {
        params.api.sizeColumnsToFit();
    }

    public onCellValueChanged($event: any) {

        console.log('OnCellValueChanged : ' + JSON.stringify($event.data.id));
        const raceToUpdate: Race = $event.data;

        this.racesService.updateRace(raceToUpdate).subscribe(
            (updatedRace: Race) => {
                console.log('Race : ' + updatedRace.name + ' [' + updatedRace.id + '] was updated');
            },
            error => {
                console.log('Race Update Error: ' + JSON.stringify(error));
            }
        );

    }
}

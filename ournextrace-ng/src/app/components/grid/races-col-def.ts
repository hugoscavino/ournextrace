import { GridOptions, ColDef, ValueFormatterParams } from 'ag-grid-community';
import * as moment from 'moment';
import { AgGridRaceTypeRendererComponent } from './ag-grid-primeng-race-types/renderer/ag-grid-race-type-renderer.component';
import { AgGridRaceTypeEditorComponent } from './ag-grid-primeng-race-types/editor/ag-grid-race-type-editor.component';
import { AgGridPrimengDatepickerEditorComponent } from './ag-grid-primeng-datepicker/ag-grid-primeng-datepicker-editor.component';
import { AgGridPrimengBooleanEditorComponent } from './ag-grid-primeng-boolean/editor/ag-grid-boolean-editor.component';
import { AgGridPrimeNgUserRendererComponent } from './ag-grid-primeng-user/ag-grid-primeng-user-renderer.component';
import { AgGridAddressEditorComponent } from './../grid/ag-grid-primeng-location/editor/address-editor.component';
import { AgGridAddressRendererComponent } from './../grid/ag-grid-primeng-location/renderer/address-renderer.component';
import { AgGridCrudRendererComponent } from './ag-grid-primeng-crud/ag-grid-crud-renderer.component';
import { AgGridBooleanRendererComponent } from './ag-grid-primeng-boolean/renderer/ag-grid-boolean-renderer.component';

export class RacesDefaultDefinitions {

    public static readonly raceIdCol: ColDef = {
        headerName: 'Id',
        maxWidth: 60,
        field: 'id',
        sortable: true,
        filter: true,
        editable: false,
    };

    public static readonly raceNameCol: ColDef = {
        headerName: 'Race Name',
        field: 'name',
        sortable: true,
        filter: true,
        editable: true
    };

    public static readonly raceDateCol: ColDef = {
        headerName: 'Race Date',
        field: 'date',
        sortable: true,
        filter: true,
        editable: true,
        cellEditorFramework: AgGridPrimengDatepickerEditorComponent,
        valueFormatter: (data: ValueFormatterParams) => {
            return data.value ? moment(data.value).format('YYYY-MM-DD') : null;
        }
    };

    public static readonly raceDescCol: ColDef = {
        headerName: 'Desc',
        field: 'description',
        sortable: true,
        filter: true,
        editable: true
    };

    public static readonly raceUrlCol: ColDef = {
        headerName: 'URL',
        field: 'url',
        sortable: true,
        filter: true,
        editable: true
    };
    public static readonly raceTypeCol: ColDef = {
        headerName: 'Race Types',
        minWidth: 320,
        colId: 'raceTypes',
        field: 'raceTypes',
        editable: true,
        sortable: false,
        cellEditorFramework: AgGridRaceTypeEditorComponent,
        cellEditorParams: {values: []},
        cellRendererFramework: AgGridRaceTypeRendererComponent,
    };

    public static readonly locationCol: ColDef = {
        headerName: 'Location',
        minWidth: 280,
        colId: 'location',
        field: 'address',
        editable: true,
        sortable: false,
        cellEditorFramework: AgGridAddressEditorComponent,
        cellRendererFramework: AgGridAddressRendererComponent
    };

    public static readonly publicCol: ColDef = {
        headerName: 'Public',
        field: 'public',
        editable: true,
        sortable: true,
        cellEditorFramework: AgGridPrimengBooleanEditorComponent,
        cellRendererFramework: AgGridBooleanRendererComponent
    };

    public static readonly activeCol: ColDef = {
        headerName: 'Active',
        field: 'active',
        editable: true,
        sortable: true,
        cellEditorFramework: AgGridPrimengBooleanEditorComponent,
        cellRendererFramework: AgGridBooleanRendererComponent
    };

    public static readonly cancelledCol: ColDef = {
        headerName: 'Cancelled',
        field: 'cancelled',
        editable: true,
        sortable: true,
        cellEditorFramework: AgGridPrimengBooleanEditorComponent,
        cellRendererFramework: AgGridBooleanRendererComponent
    };

    public static readonly authorCol: ColDef = {
        headerName: 'Author',
        field: 'author',
        sortable: true,
        filter: true,
        editable: false,
        cellRendererFramework: AgGridPrimeNgUserRendererComponent,
    };
    public static readonly couponCol: ColDef = {
        headerName: 'Coupon Code',
        field: 'couponCode',
        sortable: true,
        filter: true,
        editable: true,
    };
    public static readonly crudCol: ColDef = {
        headerName: 'CRUD',
        colId: 'crud',
        sortable: false,
        filter: false,
        editable: false,
        cellRendererFramework: AgGridCrudRendererComponent,
    };

    // All Column Definitions
    public static readonly columnDefs: ColDef [] = [
        RacesDefaultDefinitions.raceIdCol,
        RacesDefaultDefinitions.raceNameCol,
        RacesDefaultDefinitions.raceDescCol,
        RacesDefaultDefinitions.raceDateCol,
        RacesDefaultDefinitions.raceUrlCol,
        RacesDefaultDefinitions.publicCol,
        RacesDefaultDefinitions.activeCol,
        RacesDefaultDefinitions.raceTypeCol,
        RacesDefaultDefinitions.locationCol,
        RacesDefaultDefinitions.cancelledCol,
        RacesDefaultDefinitions.authorCol,
        RacesDefaultDefinitions.couponCol,
        RacesDefaultDefinitions.crudCol
    ];

    public static readonly gridOptions: GridOptions = {
        pagination: true,
        rowHeight: 46,
        rowSelection: 'single',
    };

    public static readonly defaultColDef: ColDef = {
        resizable: true,
        editable: true,
    };

}

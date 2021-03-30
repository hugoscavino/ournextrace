import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { BrowserModule} from '@angular/platform-browser';

import { ConfirmDialogModule} from 'primeng/confirmdialog';
import { AutoCompleteModule} from 'primeng/autocomplete';
import { SplitButtonModule} from 'primeng/splitbutton';
import { StepsModule} from 'primeng/steps';
import { CalendarModule} from 'primeng/calendar';
import { ToastModule} from 'primeng/toast';
import { SidebarModule} from 'primeng/sidebar';
import { InputSwitchModule} from 'primeng/inputswitch';
import { TabMenuModule} from 'primeng/tabmenu';
import { MenubarModule} from 'primeng/menubar';
import { ToolbarModule} from 'primeng/toolbar';
import { CardModule} from 'primeng/card';
import { MultiSelectModule} from 'primeng/multiselect';
import { DropdownModule} from 'primeng/dropdown';
import { FieldsetModule} from 'primeng/fieldset';
import { ConfirmationService, MessageService} from 'primeng/api';

import { AgGridModule } from 'ag-grid-angular';
import { AgGridPrimengDatepickerEditorComponent} from './grid/ag-grid-primeng-datepicker/ag-grid-primeng-datepicker-editor.component';
import { AgGridPrimeNgSelectEditorComponent} from './grid/ag-grid-primeng-select/ag-grid-primeng-select-editor.component';
import { AgGridRaceTypeEditorComponent } from './grid/ag-grid-primeng-race-types/editor/ag-grid-race-type-editor.component';
import { AgGridRaceTypeRendererComponent } from './grid/ag-grid-primeng-race-types/renderer/ag-grid-race-type-renderer.component';
import { AgGridPrimengBooleanEditorComponent} from './grid/ag-grid-primeng-boolean/editor/ag-grid-boolean-editor.component';
import { AgGridBooleanRendererComponent} from './grid/ag-grid-primeng-boolean/renderer/ag-grid-boolean-renderer.component';
import { AgGridAddressRendererComponent} from './grid/ag-grid-primeng-location/renderer/address-renderer.component';
import { AgGridAddressEditorComponent} from './grid/ag-grid-primeng-location/editor/address-editor.component';
import { AgGridPrimeNgUserRendererComponent} from './grid/ag-grid-primeng-user/ag-grid-primeng-user-renderer.component';
import { AgGridCrudRendererComponent} from './grid/ag-grid-primeng-crud/ag-grid-crud-renderer.component';
import { RacesService} from '../service/races';
import { AuthService } from '../service/auth';

import { InterestedComponent } from './interested/interested.component';
import { OptionsComponent } from './options/options.component';
import { SidebarComponent } from './sidebar/sidebar.component';
import { FooterComponent } from './footer/footer.component';
import { StepsComponent } from './steps/steps.component';
import { ToolbarComponent } from './toolbar/toolbar.component';
import { LoginComponent } from './login/login.component';
import { RacesGridComponent } from './grid/races-grid.component';

@NgModule({
imports: [
    BrowserModule,
    BrowserAnimationsModule,
    SplitButtonModule,
    StepsModule,
    CalendarModule,
    CommonModule,
    ToastModule,
    SidebarModule,
    TabMenuModule,
    MenubarModule,
    ToolbarModule,
    InputSwitchModule,
    AutoCompleteModule,
    CardModule,
    FormsModule,
    MultiSelectModule,
    DropdownModule,
    ConfirmDialogModule,
    ReactiveFormsModule,
    FieldsetModule,
    AgGridModule.withComponents([
        AgGridPrimengDatepickerEditorComponent,
        AgGridPrimeNgSelectEditorComponent,
        AgGridRaceTypeEditorComponent,
        AgGridPrimengBooleanEditorComponent,
        AgGridPrimeNgUserRendererComponent,
        AgGridAddressRendererComponent,
        AgGridAddressEditorComponent,
        AgGridCrudRendererComponent,
        AgGridRaceTypeRendererComponent,
        AgGridBooleanRendererComponent
    ]),
],
providers: [
    RacesService,
    AuthService,
    ConfirmationService,
    MessageService
],
declarations: [
    InterestedComponent,
    OptionsComponent,
    FooterComponent,
    StepsComponent,
    ToolbarComponent,
    LoginComponent,
    SidebarComponent,
    AgGridPrimengDatepickerEditorComponent,
    AgGridPrimeNgSelectEditorComponent,
    AgGridRaceTypeEditorComponent,
    AgGridPrimengBooleanEditorComponent,
    AgGridPrimeNgUserRendererComponent,
    AgGridAddressRendererComponent,
    AgGridAddressEditorComponent,
    AgGridCrudRendererComponent,
    AgGridRaceTypeRendererComponent,
    AgGridBooleanRendererComponent,
    RacesGridComponent
],
exports: [
        InterestedComponent,
        OptionsComponent,
        LoginComponent,
        FooterComponent,
        StepsComponent,
        ToolbarComponent,
        RacesGridComponent,
        SidebarComponent,
        SplitButtonModule,
        ToastModule,
        SplitButtonModule,
        TabMenuModule,
        MenubarModule,
        ToolbarModule,
        CardModule,
        FieldsetModule,
        SidebarModule,
    ]
})

export class SharedComponentModule {

}


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

import { RacesService} from '../service/races';
import { AuthService } from '../service/auth';
import { OurNextRaceToolbarModule } from './toolbar/toolbar-module';
import {SelectRacesModule} from "./select-races/select-races-module";
import {FilterComponentModule} from "./filter/filter-component-module";
import {FooterComponentModule} from "./footer/footer-module";


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
    OurNextRaceToolbarModule,
    SelectRacesModule,
    FilterComponentModule,
    FooterComponentModule
],
providers: [
    RacesService,
    AuthService,
    ConfirmationService,
    MessageService
],
declarations: [

],
exports: [

        OurNextRaceToolbarModule,
        SplitButtonModule,
        ToastModule,
        SplitButtonModule,
        TabMenuModule,
        MenubarModule,
        ToolbarModule,
        CardModule,
        FieldsetModule,
        SidebarModule,
        SelectRacesModule,
        FilterComponentModule,
        FooterComponentModule
    ]
})

export class SharedComponentModule {

}


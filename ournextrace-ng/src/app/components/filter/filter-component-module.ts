import {CommonModule, NgOptimizedImage} from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { InputSwitchModule} from 'primeng/inputswitch';
import { MultiSelectModule} from 'primeng/multiselect';
import { CalendarModule} from 'primeng/calendar';
import {ConfirmDialogModule} from 'primeng/confirmdialog';

import { ToastModule} from 'primeng/toast';
import {InputMaskModule} from 'primeng/inputmask';
import {DropdownModule} from 'primeng/dropdown';

import { EditorModule} from 'primeng/editor';
import { FilterComponent } from './filter.component';
import { ConfirmationService} from 'primeng/api';
import {SelectRacesModule} from "../select-races/select-races-module";

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        EditorModule,
        InputSwitchModule,
        NgOptimizedImage,
        ToastModule,
        InputMaskModule,
        ConfirmDialogModule,
        DropdownModule,
        MultiSelectModule,
        CalendarModule,
        SelectRacesModule
    ],
    declarations: [
        FilterComponent
    ],
    providers: [ConfirmationService],
    exports: [
        CommonModule,
        FilterComponent
    ]
})

export class FilterComponentModule {

}
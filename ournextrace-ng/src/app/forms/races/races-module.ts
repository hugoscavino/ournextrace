import {CommonModule, NgOptimizedImage} from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { InputSwitchModule} from 'primeng/inputswitch';
import { ConfirmDialogModule} from 'primeng/confirmdialog';

import { ToastModule} from 'primeng/toast';

import { EditorModule} from 'primeng/editor';
import { FilterComponentModule } from '../../components/filter/filter-component-module';
import { RacesComponent } from './races.component';
import { ConfirmationService} from 'primeng/api';
import {DataViewModule} from "primeng/dataview";
import {RouterLink} from "@angular/router";
import {InputTextModule} from "primeng/inputtext";

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        EditorModule,
        InputSwitchModule,
        NgOptimizedImage,
        ToastModule,
        ConfirmDialogModule,
        FilterComponentModule,
        DataViewModule,
        RouterLink,
        InputTextModule
    ],
    declarations: [
        RacesComponent
    ],
    providers: [ConfirmationService],
    exports: [
        CommonModule,
        RacesComponent
    ]
})

export class RacesModule {

}
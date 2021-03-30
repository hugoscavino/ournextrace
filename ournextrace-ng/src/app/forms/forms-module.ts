import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {InputSwitchModule} from 'primeng/inputswitch';
import { AccordionModule} from 'primeng/accordion';
import { ConfirmDialogModule} from 'primeng/confirmdialog';
import { CalendarModule} from 'primeng/calendar';
import { MultiSelectModule} from 'primeng/multiselect';
import { EditorModule} from 'primeng/editor';
import { SharedComponentModule} from '../../app/components/shared-components';
import { EditMyRaceComponent } from '../forms/myRace/edit-myrace/edit-myrace.component';
import { ViewRaceComponent } from '../forms/race/view-race/view-race.component';
import { AddMyRaceComponent } from '../forms/myRace/add-myrace/add-myrace.component';
import {ConfirmationService} from 'primeng/api';

@NgModule({
    imports: [
        CommonModule,
        SharedComponentModule,
        ConfirmDialogModule,
        AccordionModule,
        FormsModule,
        MultiSelectModule,
        ReactiveFormsModule,
        CalendarModule,
        EditorModule,
        InputSwitchModule

    ],
    declarations: [
        EditMyRaceComponent,
        AddMyRaceComponent,
        ViewRaceComponent
    ],
    providers: [ConfirmationService],
    exports: [
        CommonModule,
        SharedComponentModule,
        ConfirmDialogModule,
        EditMyRaceComponent,
        AddMyRaceComponent,
        ViewRaceComponent
    ]
    })

    export class AppFormsModule {

    }

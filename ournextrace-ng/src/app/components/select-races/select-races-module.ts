import {CommonModule, NgOptimizedImage} from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { InputSwitchModule} from 'primeng/inputswitch';
import { MultiSelectModule} from 'primeng/multiselect';

import {ConfirmDialogModule} from 'primeng/confirmdialog';

import { ToastModule} from 'primeng/toast';
import {InputMaskModule} from 'primeng/inputmask';
import {DropdownModule} from 'primeng/dropdown';

import { EditorModule} from 'primeng/editor';
import { SelectRacesComponent } from './select-races.component';
import { ConfirmationService} from 'primeng/api';

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
        MultiSelectModule
    ],
    declarations: [
        SelectRacesComponent
    ],
    providers: [ConfirmationService],
    exports: [
        CommonModule,
        SelectRacesComponent
    ]
})

export class SelectRacesModule {

}
import {CommonModule, NgOptimizedImage} from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { InputSwitchModule} from 'primeng/inputswitch';
import {ConfirmDialogModule} from 'primeng/confirmdialog';

import { ToastModule} from 'primeng/toast';
import {InputMaskModule} from 'primeng/inputmask';
import {DropdownModule} from 'primeng/dropdown';

import { EditorModule} from 'primeng/editor';
import { LocationComponent } from './location.component';
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
        DropdownModule
    ],
    declarations: [
        LocationComponent
    ],
    providers: [ConfirmationService],
    exports: [
        CommonModule,
        LocationComponent
    ]
})

export class LocationModule {

}
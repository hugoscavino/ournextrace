import {CommonModule, NgOptimizedImage} from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';


import { EditorModule} from 'primeng/editor';
import { FooterComponent } from './footer.component';
import { ConfirmationService} from 'primeng/api';
import {SelectRacesModule} from "../select-races/select-races-module";
import {RouterModule} from '@angular/router';
@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        EditorModule,
        NgOptimizedImage,
        SelectRacesModule,
        RouterModule
    ],
    declarations: [
        FooterComponent
    ],
    providers: [ConfirmationService],
    exports: [
        CommonModule,
        FooterComponent
    ]
})

export class FooterComponentModule {

}
import {CommonModule, NgOptimizedImage} from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { InputSwitchModule} from 'primeng/inputswitch';
import { ButtonModule} from 'primeng/button';
import { EditorModule} from 'primeng/editor';
import { ViewRaceComponent } from './view-race.component';
import { ConfirmationService} from 'primeng/api';
import { AccordionModule} from "primeng/accordion";

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        AccordionModule,
        EditorModule,
        InputSwitchModule,
        ButtonModule,
        NgOptimizedImage

    ],
    declarations: [
        ViewRaceComponent
    ],
    providers: [ConfirmationService],
    exports: [
        CommonModule,
        ViewRaceComponent
    ]
})

export class ViewRaceModule {

}

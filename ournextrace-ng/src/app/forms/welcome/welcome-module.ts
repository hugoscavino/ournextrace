import {CommonModule, NgOptimizedImage} from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { InputSwitchModule} from 'primeng/inputswitch';
import { CalendarModule} from 'primeng/calendar';
import { ButtonModule} from 'primeng/button';
import { EditorModule} from 'primeng/editor';
import { SelectRacesModule } from '../../components/select-races/select-races-module';
import { WelcomeComponent } from './welcome.component';
import { ConfirmationService} from 'primeng/api';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        CalendarModule,
        EditorModule,
        InputSwitchModule,
        ButtonModule,
        NgOptimizedImage,
        SelectRacesModule

    ],
    declarations: [
        WelcomeComponent
    ],
    providers: [ConfirmationService],
    exports: [
        CommonModule,
        WelcomeComponent
    ]
})

export class WelcomeModule {

}

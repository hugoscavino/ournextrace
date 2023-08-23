import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { WelcomeModule } from './welcome/welcome-module';
import { RacesModule } from './races/races-module';
import {ViewRaceModule} from "./race/view-race/view-race-module";
import {LocationModule} from "./location/location-module";
import {ContactUsModule} from "./contact-us/contact-us-module";

@NgModule({
    imports: [
        CommonModule,
        ReactiveFormsModule,
        HttpClientModule,
        RouterModule,
        WelcomeModule,
        RacesModule,
        ViewRaceModule,
        LocationModule,
        ContactUsModule
    ],
    declarations: [
    ],
    exports: [
        CommonModule,
        ReactiveFormsModule,
        HttpClientModule,
        RouterModule,
        WelcomeModule,
        RacesModule,
        ViewRaceModule,
        LocationModule,
        ContactUsModule
    ]
})
export class OurNextRaceFormsModule {}

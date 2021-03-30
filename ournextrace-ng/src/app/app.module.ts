import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { DatePipe, APP_BASE_HREF } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { LocationStrategy, HashLocationStrategy } from '@angular/common';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

// ngrx
import { StoreModule } from '@ngrx/store';
import { StoreDevtoolsModule } from '@ngrx/store-devtools';
import { RaceReducer } from './ngrx/race.reducer';

// Resolution Detector
import { ResponsiveModule, IResponsiveConfig } from 'ngx-responsive';

// PrimeNg
import { SharedModule} from '../app/shared/shared.module';

// Custom Modules
import { SharedComponentModule } from '../../src/app/components/shared-components';

// PrimeNg Services
import { ConfirmationService} from 'primeng/api';
import { MessageService} from 'primeng/api';

// Google
import { GtagModule } from 'angular-gtag';

import { MyRacesService} from './service/my-races';
import { AddressService} from './service/address';
import { AppErrorHandler} from './service/error-handler/app-error-handler';
import { AuthService} from './service/auth';
import { AuthGuardService } from './service/auth-guard';
import { MetadataService} from './service/metadata';

// this includes the core NgIdleModule but includes keepalive providers for easy wire up
import { NgIdleModule } from '@ng-idle/core';

// Custom Modules
import { AppFormsModule } from '../../src/app/forms/forms-module';

import { RacesComponent } from './forms/races/races.component';
import { PageNotFoundComponent } from './forms/page-not-found/page-not-found.component';
import { ResetPasswordComponent } from './forms/user-mgt/reset-password/reset-password.component';

import { AddRaceComponent } from './forms/race/add-race/add-race.component';
import { UpdateRaceComponent } from './forms/race/update-race/update-race.component';

import { LocationComponent } from './forms/location/location.component';

import { UpdateUserComponent } from './forms/user-mgt/update-user/update-user.component';

import { RegistrationComponent } from './forms/user-mgt/registration/registration.component';
import { ForgotComponent } from './forms/user-mgt/forgot/forgot.component';
import { ContactUsComponent } from './forms/contact-us/contact-us.component';
import { RacesManagerComponent } from './forms/race/races-manager/races-manager.component';
import { LocationManagerComponent} from './forms/location-manager/location-manager.component';
import { WelcomeComponent } from './forms/welcome/welcome.component';
import { AboutUsComponent } from './forms/about-us/about-us.component';
import { FilterComponent } from './components/filter/filter.component';
import { SelectRacesComponent } from './components/select-races/select-races.component';
import { environment } from 'src/environments/environment';

const config: IResponsiveConfig = {
  breakPoints: {
    xs: { max: 600 },
    sm: { min: 601, max: 959 },
    md: { min: 960, max: 1279 },
    lg: { min: 1280, max: 1919 },
    xl: { min: 1920 }
  },
  debounceTime: 100
};

@NgModule({
  declarations: [
    AppComponent,
    RacesComponent,
    PageNotFoundComponent,
    AddRaceComponent,
    UpdateRaceComponent,
    LocationComponent,
    UpdateUserComponent,
    RegistrationComponent,
    ForgotComponent,
    ResetPasswordComponent,
    ContactUsComponent,
    RacesManagerComponent,
    LocationManagerComponent,
    WelcomeComponent,
    AboutUsComponent,
    FilterComponent,
    SelectRacesComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    ResponsiveModule.forRoot(config),
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    SharedModule,
    SharedComponentModule,
    AppFormsModule,
    NgIdleModule.forRoot(),
    GtagModule.forRoot({ trackingId: 'UA-47879324-6', trackPageviews: true }),
    StoreModule.forRoot(
      {
        raceReducer: RaceReducer
      },
      {
        runtimeChecks: {
          strictStateImmutability: true,
          strictActionImmutability: true,
          strictStateSerializability: true,
          strictActionSerializability: true,
        },
      }),  // Instrumentation must be imported after importing StoreModule (config is optional)
    StoreDevtoolsModule.instrument({
      maxAge: 25, // Retains last 25 states
      logOnly: environment.production, // Restrict extension to log-only mode
    })
  ],
  providers: [
    DatePipe,
    { provide: LocationStrategy,
      useClass: HashLocationStrategy
    },
    {
      provide: APP_BASE_HREF,
      useValue: '/'
    },
    MessageService,
    MyRacesService,
    AuthService,
    AppErrorHandler,
    AddressService,
    AuthGuardService,
    MetadataService,
    ConfirmationService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

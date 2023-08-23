import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { DatePipe, APP_BASE_HREF, NgOptimizedImage} from '@angular/common';
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

// PrimeNg
import { SharedModule} from './shared/shared.module';

// Custom Components
import { SharedComponentModule } from './components/shared-components';

// Custom Forms
import { OurNextRaceFormsModule } from './forms/forms-module';

// PrimeNg Services
import { ConfirmationService} from 'primeng/api';
import { MessageService} from 'primeng/api';

import { MyRacesService} from './service/my-races';
import { AddressService} from './service/address';
import { AppErrorHandler} from './service/error-handler/app-error-handler';
import { AuthService} from './service/auth';
import { AuthGuardService } from './service/auth-guard';
import { MetadataService} from './service/metadata';

import { environment } from 'src/environments/environment';

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    SharedModule,
    SharedComponentModule,
    OurNextRaceFormsModule,
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
    }),
    NgOptimizedImage
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

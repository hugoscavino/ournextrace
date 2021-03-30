import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { RacesComponent } from './forms/races/races.component';

import { AddRaceComponent } from './forms/race/add-race/add-race.component';
import { UpdateRaceComponent } from './forms/race/update-race/update-race.component';
import { ViewRaceComponent } from './forms/race/view-race/view-race.component';

import { LocationComponent } from './forms/location/location.component';
import { ForgotComponent } from './forms/user-mgt/forgot/forgot.component';

import { AddMyRaceComponent } from './forms/myRace/add-myrace/add-myrace.component';
import { EditMyRaceComponent } from './forms/myRace/edit-myrace/edit-myrace.component';

import { UpdateUserComponent } from './forms/user-mgt/update-user/update-user.component';

import { PageNotFoundComponent } from '../app/forms/page-not-found/page-not-found.component';
import { RegistrationComponent } from './forms/user-mgt/registration/registration.component';
import { ResetPasswordComponent } from './forms/user-mgt/reset-password/reset-password.component';

import { ContactUsComponent } from '../app/forms/contact-us/contact-us.component';
import { WelcomeComponent } from './forms/welcome/welcome.component';
import { AboutUsComponent } from '../app/forms/about-us/about-us.component';

import { RacesManagerComponent } from './forms/race/races-manager/races-manager.component';
import { LocationManagerComponent } from './forms/location-manager/location-manager.component';

import { LoginComponent } from './components/login/login.component';

import { AuthGuardService, Role } from '../app/service/auth-guard';
import { Header } from './domain/mode';


const routes: Routes = [
  {
    path: '',  component: WelcomeComponent,
    pathMatch: 'full',
    data: {
      header: Header.WELCOME
    }
  },

  {
    path: 'welcome',  component: WelcomeComponent,
    data: {
      header: Header.WELCOME
    }
  },

  {
    path: '_',  redirectTo: '/races',
    pathMatch: 'full',
    data: {
      header: Header.RACES
    }
  },
  {
    path: '404', component: PageNotFoundComponent
  },
  {
    path: 'contact-us', component: ContactUsComponent,
    data: {
      header: Header.CONTACT_US
    }

  },
  {
    path: 'about-us', component: AboutUsComponent,
    data: {
      header: Header.ABOUT_US
    }
  },

  {
    path: 'login', component: LoginComponent
  },
  {
    path: 'registration', component: RegistrationComponent,
    data: {
      header: Header.REGISTRATION
    }
  },
  {
    path: 'reset-password/:email/:token', component: ResetPasswordComponent
  },

  {
    path: 'forgot', component: ForgotComponent,
    data: {
      header: Header.FORGOT,
    }
  },
  {
    path: 'races', component: RacesComponent,
    data: {
      header: Header.RACES
    }
  },
  {
    path: 'events', component: RacesComponent,
    data: {
      header: Header.RACES
    }
  },
  {
    path: 'update-user', component: UpdateUserComponent,
    canActivate: [AuthGuardService],
    data: {
      expectedRole: Role.USER
    }
  },
  {
    path: 'add-race', component: AddRaceComponent,
    canActivate: [AuthGuardService],
    data: {
       expectedRole: Role.USER,
       header: Header.ADD_RACE_STEP
    }
  },
  {
    path: 'location', component: LocationComponent,
    canActivate: [AuthGuardService],
    data: {
      expectedRole: Role.USER,
      header: Header.LOCATION,
      mode: 'value'
    }
  },
  {
    path: 'location/:raceId', component: LocationComponent,
    canActivate: [AuthGuardService],
    data: {
      expectedRole: Role.USER,
      mode: 'value'
    }
  },
  {
    path: 'update-race/:raceId/:raceTypeId', component: UpdateRaceComponent,
    canActivate: [AuthGuardService],
    data: {
      expectedRole: Role.POWER_USER
    }
  },
  {
    path: 'update-race/:raceId', component: UpdateRaceComponent,
     canActivate: [AuthGuardService],
    data: {
      expectedRole: Role.POWER_USER
    }
  },
  {
    path: 'races-manager', component: RacesManagerComponent,
    canActivate: [AuthGuardService],
    data: {
      expectedRole: Role.USER,
      header: Header.RACES
    }
  },
  {
    path: 'location-manager', component: LocationManagerComponent,
    canActivate: [AuthGuardService],
    data: {
      expectedRole: Role.USER,
      header: Header.LOCATION
    }
  },

  {
    path: 'view-race/:raceId', component: ViewRaceComponent,
    data: {
      header: Header.RACES
    }
  },
  {
    path: 'add-myrace/:raceId', component: AddMyRaceComponent,
    canActivate: [AuthGuardService],
    data: {
      expectedRole: Role.USER,
      header: Header.ADD_MY_RACE,
      mode: 'value'
    }
  },
  {
    path: 'edit-myrace/:raceId', component: EditMyRaceComponent,
    canActivate: [AuthGuardService],
    data: {
      expectedRole: Role.USER,
      header: Header.EDIT_MY_RACE,
      mode: 'value'
    }
  },
  { path: '**', component: PageNotFoundComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { enableTracing: false } )],
  exports: [RouterModule]
})
export class AppRoutingModule {

}

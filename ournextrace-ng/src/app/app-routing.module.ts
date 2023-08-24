import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { RacesComponent } from './forms/races/races.component';

import { ViewRaceComponent } from './forms/race/view-race/view-race.component';

import { PageNotFoundComponent } from './forms/page-not-found/page-not-found.component';

import { ContactUsComponent } from './forms/contact-us/contact-us.component';
import { WelcomeComponent } from './forms/welcome/welcome.component';
import { AboutUsComponent } from './forms/about-us/about-us.component';

import { Header } from './domain/mode';


const routes: Routes = [
  {
    path: '',  component: RacesComponent,
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
    path: 'view-race/:raceId', component: ViewRaceComponent,
    data: {
      header: Header.RACES
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

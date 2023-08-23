import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { RaceAppState, RaceStore, moduleKeyName } from './ngrx/race.app.state';
import { Store, select } from '@ngrx/store';

@Component({
  selector: 'ijudy-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent  implements OnInit, OnDestroy {

  public raceStateObs$: Observable<RaceAppState>;


  constructor(
              public router: Router,
              public store: Store<RaceStore>) {

    this.raceStateObs$ = store.pipe(select(moduleKeyName));

  }

  ngOnDestroy(): void {

    }

  ngOnInit() {

  }




}

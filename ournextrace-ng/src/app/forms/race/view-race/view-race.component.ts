import { Component, OnInit} from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Location, LocationStrategy, PathLocationStrategy} from '@angular/common';
import { Observable, Subscription } from 'rxjs';
import { Store, select } from '@ngrx/store';
import { RaceStore, RaceAppState, moduleKeyName } from '../../../ngrx/race.app.state';

import { Gtag } from 'angular-gtag';
import { MessageService } from 'primeng/api';


import { Race, IRace, IMyRace, RaceStatus} from '../../../domain/race';
import { User } from '../../../domain/user';

import { MyRacesService } from '../../../service/my-races';
import { RacesService} from '../../../service/races';
import { AppErrorHandler} from '../../../service/error-handler/app-error-handler';

import { IJudyConstants} from '../../../util/constants';
import { SetRedirectUrl, SetFormEditMode } from 'src/app/ngrx/race.actions';

@Component({
  selector: 'ijudy-view-race',
  providers: [
    Location,
    {provide: LocationStrategy, useClass: PathLocationStrategy}
  ],
  templateUrl: './view-race.component.html',
  styleUrls: ['./view-race.component.css']
})
export class ViewRaceComponent implements OnInit {

  public race: IRace;
  public myRace: IMyRace;

  public selectedRaceId: number;
  public raceStateObs$: Observable<RaceAppState>;
  public raceReducerSubscription: Subscription;

  constructor(
    private gtag: Gtag,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private errorHandler: AppErrorHandler,
    public messageService: MessageService,
    private myRacesService: MyRacesService,
    private raceService: RacesService,
    public location: Location,
    public store: Store<RaceStore>) {
      this.raceStateObs$ = store.pipe(select(moduleKeyName));

  }

  ngOnInit() {
    this.selectedRaceId = this.activatedRoute.snapshot.params['raceId'];
    console.log('selected RaceId ' + this.selectedRaceId);

    // Analytics
    this.gtag.event('race_view', {
      method: 'view_race',
      event_category: 'race_crud',
      event_label: 'Viewed race ' + this.selectedRaceId
    });

    this.race = new Race();
    this.race.id = this.selectedRaceId;

    this.loadRace();
  }

  public loadRace() {

        this.raceService.getRace(this.selectedRaceId).subscribe(
          race => {
            this.race = race;
                  this.gtag.event('race_view', {
                    method: 'view_race',
                    event_category: 'race_crud',
                    event_label: 'Viewed race ' + this.race.name
                  });
                },
          err => {
                  this.errorHandler.handleError(err);
                }
        );

  }


  /**
    * Go back to main page
    * @param event
    */
  public cancel(event: any) {
     this.router.navigate([IJudyConstants.RACES_URI]);
  }

}

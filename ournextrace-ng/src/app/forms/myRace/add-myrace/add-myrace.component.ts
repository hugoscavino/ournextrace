import { Component, OnInit} from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormGroup, FormControl} from '@angular/forms';
import { HttpStatusCode} from '../../../service/error-handler/status-codes';
import { HttpErrorResponse } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { Observable, Subscription } from 'rxjs';

import { MessageService} from 'primeng/api';
import { Store, select } from '@ngrx/store';
import { RaceAppState, RaceStore, moduleKeyName } from '../../../ngrx/race.app.state';

import { Race, MyRace, RaceType, RaceStatus, IMyRace} from '../../../domain/race';
import { User} from '../../../domain/user';
import { RaceMode } from '../../../domain/mode';
import { MyRacesService} from '../../../service/my-races';
import { RacesService} from '../../../service/races';
import { AppErrorHandler} from '../../../service/error-handler/app-error-handler';
import { IJudyConstants } from '../../../util/constants';
import { ISearchFilters } from 'src/app/domain/search-filters';
import { SetRaceFilter } from 'src/app/ngrx/race.actions';

@Component({
  selector: 'ijudy-add-myrace',
  templateUrl: './add-myrace.component.html',
  styleUrls: ['./add-myrace.component.css']
})
export class AddMyRaceComponent implements OnInit {
 public static readonly NO_ADDRESS_ID = 0;

  public race: Race;
  public selectedRaceId: number;
  public raceTypes: RaceType[];
  public user: User;
  public mode: RaceMode;
  public displaySteps: boolean;
  public readonly activeIndex = 1;

  public raceForm: FormGroup;
  public attendingStatus: RaceStatus;

  public raceStateObs$: Observable<RaceAppState>;
  public raceReducerSubscription: Subscription;
  constructor(public store: Store<RaceStore>,
              public messageService: MessageService,
              public errorHandler: AppErrorHandler,
              public activatedRoute: ActivatedRoute,
              public router: Router,
              public myRacesService: MyRacesService,
              public racesService: RacesService,
              ) {

      this.attendingStatus = RaceStatus.GOING;
      this.raceStateObs$ = store.pipe(select(moduleKeyName));
      this.selectedRaceId = activatedRoute.snapshot.params['raceId'];

      this.race = new Race();
      this.race.id = this.selectedRaceId;
      this.race.name = 'Loading...';
      this.race.description = '';
      this.race.raceDateDesc = '';

      this.raceForm = new FormGroup({
        raceRegDate: new FormControl(),
        selectedRaceTypes: new FormControl(),
        racePaid: new FormControl(true),
        raceCost: new FormControl(0),
        raceHotel: new FormControl(''),
        notes: new FormControl(''),
      });

     }

    ngOnInit() {
      this.raceReducerSubscription = this.raceStateObs$
      .pipe(
        map(state => {
              const editMode = state.editFormMode;
              // console.log('AddMyRaceComponent::ngOnInit => state.editFormMode : ' + editMode);
              if (editMode) {
                this.displaySteps = false;
                this.mode = RaceMode.EDIT;
              } else {
                this.mode = RaceMode.STEP;
                this.displaySteps = true;
              }
          }
        )
      ).subscribe();

      console.log('Attempting to load : ' + this.selectedRaceId);

      this.myRacesService.getMyRace(this.selectedRaceId).subscribe(
        (foundRace: IMyRace) => {
          console.log('Found this race in your plan. navigating to edit rac for [' + this.selectedRaceId + ']');
          const editUrl = IJudyConstants.EDIT_MY_RACE_URI + '/' + this.selectedRaceId;
          this.router.navigate([editUrl]);
        },
        (error: any) => {
          if (error instanceof HttpErrorResponse) {
              const httpErrorCode = error.status;
              switch (httpErrorCode) {
                  case HttpStatusCode.NOT_FOUND:
                    console.warn('Didn\'t find this race in your plan. Loading add race for [' + this.selectedRaceId + ']');
                    this.loadRace(this.selectedRaceId);
                    break;
                  default:
                      throw error;
              }
          }
      }
      );

    }

    public shouldDisplayAddLocation(): boolean {
      return this.race.address === null || this.race.address.id === AddMyRaceComponent.NO_ADDRESS_ID;
    }

    public isEditMode(): boolean {
      return this.mode === RaceMode.EDIT;
    }

    public isStepMode(): boolean {
        return this.mode === RaceMode.STEP;
    }

    public loadRace(selectedRaceId: number) {
      this.racesService.getRace(selectedRaceId).subscribe(
         race => {
           this.race = race;
           this.raceForm.controls['selectedRaceTypes'].setValue([]);
           this.raceForm.controls['racePaid'].setValue(true);
           this.raceForm.controls['raceCost'].setValue(0);
           this.raceForm.controls['raceRegDate'].setValue( new Date());
          },
         err => {
           console.error(err);
           this.errorHandler.handleError(err);
         },
       );
    }

    public onSubmit() {
      this.myRacesService.saveMyRace(this.toMyRace(this.raceForm)).subscribe(
        (myRace) => {
            this.messageService.add(
              {severity: 'success',
              summary: 'Add Race to Schedule',
              detail: 'Added ' + myRace.race.name + ' to your race schedule'});
              this.router.navigate([IJudyConstants.RACES_URI]);
        },
        error => {
            if (error instanceof HttpErrorResponse) {
                const httpErrorCode = error.status;
                switch (httpErrorCode) {
                    case HttpStatusCode.CONFLICT:
                    this.messageService.add(
                        {severity: 'warn',
                        summary: 'My Races',
                        detail: 'Race  ' + this.race.name + ' already exists'});
                        this.router.navigate([IJudyConstants.RACES_URI]);
                        break;
                    default:
                        throw error;
                }
            }
        }
);
    }

    public onSubmitLocation() {
      this.myRacesService.saveMyRace(this.toMyRace(this.raceForm)).subscribe(
        (myRace) => {
            this.messageService.add(
              {severity: 'success',
               summary: 'My Race',
               detail: 'Created ' + myRace.race.name});
               const url = IJudyConstants.LOCATION_URI + '/' + myRace.race.id;
               this.router.navigate([url, {mode: 'step'}]);
        },
        error => {
            if (error instanceof HttpErrorResponse) {
                const httpErrorCode = error.status;
                switch (httpErrorCode) {
                    case HttpStatusCode.CONFLICT:
                    this.messageService.add(
                        {severity: 'warn',
                        summary: 'My Races',
                        detail: 'Race  ' + this.race.name + ' already exists'});
                        this.router.navigate([IJudyConstants.RACES_URI]);
                        break;
                    default:
                        throw error;
                }
            }
        });
    }

    private toMyRace(myRaceForm: FormGroup): MyRace {
      const myRace = new MyRace();
      myRace.race = new Race();

      myRace.race.id      = this.selectedRaceId;
      myRace.raceTypes    = myRaceForm.value['selectedRaceTypes'];
      myRace.registrationDate     = myRaceForm.value['raceRegDate'];
      myRace.paid         = myRaceForm.value['racePaid'];
      myRace.cost         = myRaceForm.value['raceCost'];
      myRace.hotelName    = myRaceForm.value['raceHotel'];
      myRace.notes        = myRaceForm.value['notes'];

      myRace.myRaceStatus = RaceStatus.GOING;
      if (this.attendingStatus === RaceStatus.INTERESTED) {
         myRace.myRaceStatus = RaceStatus.INTERESTED;
      } else if (this.attendingStatus === RaceStatus.NOT_ASSIGNED) {
         myRace.myRaceStatus = RaceStatus.NOT_ASSIGNED;
      } else if (this.attendingStatus === RaceStatus.VOLUNTEERING) {
         myRace.myRaceStatus = RaceStatus.VOLUNTEERING;
      }

      return myRace;
    }

    /**
     * Go back to main page
     * @param event
     */
    onCancel(event: any ) {
      this.router.navigate([IJudyConstants.RACES_URI]);
    }
}

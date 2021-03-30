import { Component, OnInit} from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { of } from 'rxjs';
import { delay } from 'rxjs/operators';

import * as moment from 'moment';

import { ConfirmationService, MessageService} from 'primeng/api';
import { MenuItem} from 'primeng/api';

import { MyRacesService} from '../../../service/my-races';
import { RaceType, RaceStatus, IMyRace} from '../../../domain/race';
import { IJudyConstants} from '../../../util/constants';
import { AppErrorHandler} from '../../../service/error-handler/app-error-handler';
import { RaceMode } from 'src/app/domain/mode';
import { RaceStore } from 'src/app/ngrx/race.app.state';
import { Store, select } from '@ngrx/store';

@Component({
  selector: 'ijudy-edit-myrace',
  templateUrl: './edit-myrace.component.html',
  styleUrls: ['./edit-myrace.component.css']
})
export class EditMyRaceComponent implements OnInit {

  public myRace: IMyRace;
  public raceTypes: RaceType[];
  public allRaceTypes: RaceType[];
  public showDelete: false;
  public mode: RaceMode;
  public raceForm: FormGroup;
  public minDateValue: Date;
  public goingInterestedItems: MenuItem[];
  public attendingStatus: RaceStatus;
  public selectedRaceId: number;

  constructor(public errorHandler: AppErrorHandler,
              public activatedRoute: ActivatedRoute,
              public router: Router,
              public confirmationService: ConfirmationService,
              public myRacesService: MyRacesService,
              public messageService: MessageService,
              public store: Store<RaceStore>) {

      this.selectedRaceId = this.activatedRoute.snapshot.params['raceId'];

      const value = this.activatedRoute.snapshot.params['mode'] as string;
      if (value === RaceMode.STEP) {
        this.mode = RaceMode.STEP;
      } else {
        this.mode = RaceMode.EDIT;
      }

     }

    ngOnInit() {
        this.raceForm = new FormGroup({
        raceName: new FormControl(null, Validators.required),
        selectedRaceTypes: new FormControl(),
        raceRegDate: new FormControl(''),
        racePaid: new FormControl(false),
        raceCost: new FormControl(0),
        raceHotel: new FormControl(''),
        notes: new FormControl(''),
      });

        this.minDateValue = new Date();
        this.loadMyRace(this.selectedRaceId);

      }

      public isEditMode(): boolean {
          return this.mode === RaceMode.EDIT;
      }

      public isStepMode(): boolean {
          return this.mode === RaceMode.STEP;
      }

      public loadMyRace(selectedRaceId: number) {

       this.myRacesService.getMyRace(selectedRaceId).subscribe(
         (myRace: IMyRace) => {
           this.myRace = myRace;
           this.raceForm.controls['selectedRaceTypes'].setValue(this.myRace.raceTypes );
           this.raceForm.controls['racePaid'].setValue(this.myRace.paid);
           this.raceForm.controls['raceCost'].setValue(this.myRace.cost);
           this.raceForm.controls['raceHotel'].setValue(this.myRace.hotelName);
           this.raceForm.controls['notes'].setValue(this.myRace.notes);
           this.attendingStatus = myRace.myRaceStatus;

           if (this.myRace.registrationDate) {
              const momentDateObj = moment(this.myRace.registrationDate);
              this.raceForm.controls['raceRegDate'].setValue(momentDateObj.toDate());
           }

          this.allRaceTypes = this.myRace.race.raceTypes;

          },
         err => {
           console.error(err);
           this.errorHandler.handleError(err);
         }
       );
    }

    /**
     * Update the MyRace meta Information
     */
    public onSubmit() {
      // User and Race IDs are immutable
      // Modified Date should be updated by Database
      this.myRace.paid              = this.raceForm.value['racePaid'];
      this.myRace.cost              = this.raceForm.value['raceCost'];
      this.myRace.raceTypes         = this.raceForm.value['selectedRaceTypes'];
      this.myRace.hotelName         = this.raceForm.value['raceHotel'];
      this.myRace.registrationDate  = this.raceForm.value['raceRegDate'];
      this.myRace.notes             = this.raceForm.value['notes'];
      this.myRace.myRaceStatus      = this.attendingStatus;
      if (this.attendingStatus !== RaceStatus.DELETE_ME) {
        this.saveMyRace(this.myRace);
      } else {
        this.deleteMyRace({});
      }
    }

    private saveMyRace(myRace: IMyRace) {
      this.myRacesService.saveMyRace(myRace).subscribe(
        (data) => {
            this.myRace = data;
            this.messageService.add(
              {severity: 'success',
              summary: 'My Race',
              detail: 'Updated ' + this.myRace.race.name});
            const fakeObservable = of('dummy').pipe(delay(5000));
            if (this.isEditMode()) {
              this.router.navigate([IJudyConstants.RACES_URI]);
            } else {
              const url = IJudyConstants.LOCATION_URI + '/' + this.myRace.race.id;
              this.router.navigate([url]);
            }

        },
        err => {
          this.errorHandler.handleError(err);
        }
        );
    }

    deleteMyRace(race: any) {

      this.confirmationService.confirm({
          message: 'Sure you want to Delete Your Race from your Schedule?',
          header: 'Confirmation',
          icon: 'pi pi-exclamation-triangle',
          accept: () => {
            // console.log('Deleting race id : ' + this.myRace.race.id);
            this.myRacesService.deleteMyRace(this.myRace.race.id).subscribe(
              (result) => {
                  this.messageService.add(
                    {severity: 'success',
                    summary: 'My Race',
                    detail: 'Deleted ' + this.myRace.race.name + ' from your race schedule'});
                  const url = IJudyConstants.RACES_URI;
                  this.router.navigate([url]);
              },
              err => {
                console.error(err);
                this.messageService.add(
                  {severity: 'error',
                  summary: 'My Race',
                  detail: 'Your Race was not deleted '});
              }
            );
          },
          reject: () => {
            this.messageService.add(
              {severity: 'rejected',
              summary: 'My Race',
              detail: 'Did not delete race from your schedule '});
          }
      });
    }

    /**
     * Go back to main page
     * @param race
     */
    cancel(event: any) {
      this.router.navigate([IJudyConstants.RACES_URI]);
    }
}

import { Component, OnInit} from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

import { MessageService} from 'primeng/api';

import { Gtag } from 'angular-gtag';

import { RacesService} from '../../../service/races';
import { Race, RaceType} from '../../../domain/race';
import { Address} from '../../../domain/address';
import { IJudyConstants} from '../../../util/constants';
import { AppErrorHandler} from '../../../service/error-handler/app-error-handler';
import { HttpStatusCode} from '../../../service/error-handler/status-codes';

@Component({
  selector: 'ijudy-add-race',
  templateUrl: './add-race.component.html',
  styleUrls: ['./add-race.component.css']
})
export class AddRaceComponent implements OnInit {
  public displaySteps = true;
  public readonly activeIndex = 0;
  public race: Race;
  public raceTypes: RaceType[];
  public selectedAddressId: number;
  public addresses: Address[];
  public raceForm: FormGroup;
  public minDateValue: Date;

  constructor(
    private gtag: Gtag,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private errorHandler: AppErrorHandler,
    private racesService: RacesService,
    private messageService: MessageService) { }

  ngOnInit() {

    this.selectedAddressId = this.activatedRoute.snapshot.params['address_id'];
    // console.log('address_id ' + this.selectedAddressId);

    this.minDateValue = new Date();

    this.raceForm = new FormGroup({
      raceId: new FormControl(null),
      selectedRaceTypes: new FormControl('', Validators.required),
      raceName: new FormControl('', Validators.required),
      raceDate: new FormControl('', Validators.required),
      raceDescription: new FormControl(''),
      raceUrl: new FormControl(''),
    });

    this.race = new Race();
    this.getAllRaceTypes();

  }

  public getAllRaceTypes() {
    this.racesService.getAllRaceTypes().subscribe(
       raceTypes => {
         this.raceTypes = raceTypes;
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

   /**
    * Go back to main page
    * @param event
    */
   public back(event: any) {
      this.router.navigate([IJudyConstants.LOCATION_URI]);
   }

  onSubmit() {
     //  console.log('POST for raceForm');
      this.race.raceTypes      = this.raceForm.value['selectedRaceTypes'];
      this.race.name           = this.raceForm.value['raceName'];
      this.race.date           = this.raceForm.value['raceDate'];
      this.race.description    = this.raceForm.value['raceDescription'];
      this.race.address.id     = this.selectedAddressId;
      this.race.url            = this.raceForm.value['raceUrl'];

      // console.log('selectedAddressId : ' + this.selectedAddressId);
      // console.log('race.address.id : ' + this.raceForm.value['raceAddressId']);

      this.racesService.saveRace(this.race).subscribe(
        data => {
         //  console.log('Inserted Race');
          this.race = data;

          // Analytics
          this.gtag.event('race_add', {
            method: 'add_race',
            event_category: 'race_crud',
            event_label: 'Added race ' + this.race.name
          });

          const url = IJudyConstants.ADD_MY_RACE_URI + '/' + this.race.id;
          this.router.navigate([url, { mode: 'step'}]);
        },

        error => {
          this.handleError(error);
        },

        () =>  this.messageService.add(
         {severity: 'success',
         summary: 'Added Race to Plan',
         detail: 'Added ' + this.race.name})
      );
    }

    public handleError(error: any): void {
      // console.error('handleError :' + JSON.stringify(error));
      if (error instanceof HttpErrorResponse) {
          const httpErrorCode = error.status;
          // console.error('httpErrorCode :' + httpErrorCode);
          switch (httpErrorCode) {
               case HttpStatusCode.UNAUTHORIZED:
                  this.messageService.add({severity: 'error', summary: 'Add Race',
                  detail: 'You have not authenticated (logged on) with the application'});
                  break;
               case HttpStatusCode.FORBIDDEN:
                  this.messageService.add({severity: 'error', summary: 'Add Race',
                  detail: 'You do not have access to add an event'});
                  break;
              case HttpStatusCode.BAD_REQUEST:
                    this.messageService.add({severity: 'error', summary: 'Race Message',
                    detail: 'There was a internal application error. Please contact the support team'});
                  break;
              case HttpStatusCode.CONFLICT:
                    this.messageService.add({severity: 'error', summary: 'Race Message',
                    detail: 'An Race with this name and date already exists'});
                  break;
              default:
                this.messageService.add({severity: 'error', summary: 'Race Message',
                    detail: AppErrorHandler.REFRESH_PAGE_ON_TOAST_CLICK_MESSAGE});
          }
      } else {
        console.error('Error Inserting Race' + JSON.stringify(error));
        throw error;
      }
  }
}

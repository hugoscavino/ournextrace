import { Component, OnInit} from '@angular/core';

import { MessageService} from 'primeng/api';
import { ConfirmationService} from 'primeng/api';

import { RacesService} from '../../../service/races';
import { AddressService} from '../../../service/address';

import { Race, RaceType, RaceChange} from '../../../domain/race';
import { User} from '../../../domain/user';
import { Address} from '../../../domain/address';
import { AppErrorHandler} from '../../../service/error-handler/app-error-handler';
import { FormGroup, FormControl, Validators } from '@angular/forms';

@Component({
  selector: 'ijudy-races-manager',
  templateUrl: './races-manager.component.html',
  styleUrls: ['./races-manager.component.css']
})

export class RacesManagerComponent implements OnInit {

  display = false;

  cols: any[];
  public races: Race[];
  public selectedRace: Race;
  public raceTypes: RaceType[];
  loading: boolean;

  public users: User[];
  public locations: Address[];

  public race: Race;
  public raceForm: FormGroup;
  public selectedAddressId: number;

  constructor(private addressService: AddressService,
              private errorHandler: AppErrorHandler,
              private racesService: RacesService,
              private messageService: MessageService,
              private confirmationService: ConfirmationService) {}

  ngOnInit() {

    this.cols = [
      { field: 'name', header: 'Name' },                // 1
      { field: 'date', header: 'Date' },                // 2
      { field: 'raceTypes', header: 'Race Types' },     // 3
      { field: 'description', header: 'Description' },  // 4
      { field: 'url', header: 'url' },                  // 5
      { field: 'address', header: 'Address' },          // 6
      { field: 'public', header: 'Public' },            // 7
      { field: 'authorId', header: 'Author' },          // 8
      { field: 'actions', header: 'Actions' },          // 9
    ];

    this.refreshControls();

    this.raceForm = new FormGroup({
      raceId: new FormControl(null),
      selectedRaceTypes: new FormControl(),
      raceName: new FormControl('', Validators.required),
      raceDate: new FormControl('', Validators.required),
      raceDescription: new FormControl('', Validators.required),
      raceUrl: new FormControl('', Validators.required),
      locationId : new FormControl(''),
    });

  }


  private refreshControls() {
    this.loading = true;
    this.loadLocations();
    this.loadRaceTypes();
    this.getAllRaceTypes();

  }

  public refreshData() {

    this.refreshControls();

    this.messageService.add(
      {severity: 'success',
      summary: 'Refresh',
      detail: 'Refreshed Races'});
  }


  private loadRaceTypes() {
    this.racesService.getAllRaceTypes().subscribe(
      raceTypes => {
        this.raceTypes = raceTypes;
      },
      err => {
        console.error(err);
        this.errorHandler.handleError(err);
      }
    );
  }

  private loadLocations() {
    this.addressService.getAddresses().subscribe(
      locations => {
        this.locations = locations;
      },
      err => {
        console.error(err);
        this.errorHandler.handleError(err);
      }
    );
  }

  public onDropdownEditComplete(event: any, race: Race, field: string, rowIndex: number) {
      const raceChange = new RaceChange();
      raceChange.field    = field;
      raceChange.data     = race;
      raceChange.index    = rowIndex;

      // Update the static text
      if (field === 'address') {
        race.address = event.value;
        this.races[rowIndex].address.location = race.address.location;
      }

      this.onEditComplete(raceChange);
  }

  public onRaceDateComplete(race: Race, raceDate: Date,  rowIndex: number) {
    race.date = raceDate;
    this.racesService.updateRace(race).subscribe(
      () => {
        this.races[rowIndex].date = race.date;
        this.races[rowIndex].raceDateDesc = race.raceDateDesc;
        this.refreshData();
        this.messageService.add(
          {severity: 'success',
          summary: 'Race Date',
          detail: 'Updated Date' + race.name});
      },
      err => {
        console.error(err);
        this.messageService.add(
          {severity: 'error',
          summary: 'Race Date',
          detail: 'Race Date Not Updated ' + race.name});
      }
    );


  }

  public onEditComplete( raceChange: RaceChange) {

    let race = new Race();
    race = raceChange.data;

    this.racesService.updateRace(race).subscribe(
      () => {
        this.messageService.add(
          {severity: 'success',
          summary: 'Race',
          detail: 'Updated ' + race.name});
      },
      err => {
        console.error(err);
        this.messageService.add(
          {severity: 'error',
          summary: 'Race',
          detail: 'Not Updated ' + race.name});
      }
    );

  }

 public confirm(raceId: number) {
    this.confirmationService.confirm({
        message: 'Are you sure that you want to Delete Race?',
        header: 'Confirmation',
        icon: 'pi pi-exclamation-triangle',
        accept: () => {
          // console.log('Deleting race id : ' + raceId);
          this.racesService.deleteRace(raceId).subscribe(
            () => {
              this.refreshData();
              this.messageService.add(
                {severity: 'success',
                summary: 'Race',
                detail: 'Deleted ' + raceId});
            },
            err => {
              console.error(err);
              this.messageService.add(
                {severity: 'error',
                summary: 'Race',
                detail: 'Not Deleted ' + raceId});
            }
          );
        },
        reject: () => {
          this.messageService.add(
            {severity: 'rejected',
            summary: 'Race',
            detail: 'Not Deleted ' + raceId});
        }
    });
  }


  public clone(raceId: number) {
    this.confirmationService.confirm({
        message: 'Are you sure that you want to Clone Race for next year?',
        header: 'Confirmation',
        icon: 'pi pi pi-question',
        accept: () => {
          // console.log('Cloning race id : ' + raceId);
          this.racesService.clone(raceId).subscribe(
            () => {
              this.refreshData();
              this.messageService.add(
                {severity: 'success',
                summary: 'Race',
                detail: 'Cloned ' + raceId});
            },
            err => {
              console.error(err);
              this.messageService.add(
                {severity: 'error',
                summary: 'Race',
                detail: 'Not Cloned ' + raceId});
            }
          );
        },
        reject: () => {
          this.messageService.add(
            {severity: 'rejected',
            summary: 'Race',
            detail: 'Not Cloned ' + raceId});
        }
    });
  }

  public addNew(): void {
    this.display = true;
  }

  public cancel(event: any) {
    this.display = false;
  }

  onSubmit() {
    //  console.log('POST for raceForm');
     this.race.raceTypes      = this.raceForm.value['selectedRaceTypes'];
     this.race.name           = this.raceForm.value['raceName'];
     this.race.date           = this.raceForm.value['raceDate'];
     this.race.description    = this.raceForm.value['raceDescription'];
     this.race.address.id     = this.raceForm.value['locationId'];
     this.race.url            = this.raceForm.value['raceUrl'];

     this.racesService.saveRace(this.race).subscribe(
       data => {
        //  console.log('Inserted Race');
         this.race = data;
       },

       err => {
         this.errorHandler.handleError(err);
         console.error(err);
       },

       () =>  this.messageService.add(
        {severity: 'success',
        summary: 'Added Race to Inventory',
        detail: 'Added ' + this.race.name})
     );
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
}

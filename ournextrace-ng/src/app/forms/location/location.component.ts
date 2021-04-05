import { Component, OnInit} from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { MessageService, SelectItem} from 'primeng/api';
import { Router, ActivatedRoute } from '@angular/router';

import { Address} from '../../domain/address';
import { Race} from '../../domain/race';
import { RaceMode } from '../../domain/mode';

import { AppErrorHandler} from '../../service/error-handler/app-error-handler';
import { MetadataService } from '../../service/metadata';
import { AddressService} from '../../service/address';
import { RacesService} from '../../service/races';
import { IJudyConstants} from '../../util/constants';

@Component({
  selector: 'ijudy-location',
  templateUrl: './location.component.html',
  styleUrls: ['./location.component.css']
})
export class LocationComponent implements OnInit {

  public address: Address;
  public race: Race;
  public selectedRaceId: number;
  public states: SelectItem[];
  public countries: SelectItem[];
  public addressForm: FormGroup;
  public addresses: Address[];
  public addLocationOnly = false;

  public mode: RaceMode;
  public displaySteps: boolean;
  public readonly activeIndex = 2;

  constructor(
    private metadataService: MetadataService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private appErrorHandler: AppErrorHandler,
    private addressService: AddressService,
    private racesService: RacesService,
    private messageService: MessageService) {
     }

  ngOnInit() {
    this.address = new Address();
    this.selectedRaceId = this.activatedRoute.snapshot.params['raceId'] as number;
    const modeKey       = this.activatedRoute.snapshot.params[IJudyConstants.EDIT_MODE_KEY] as string;
    // console.log('value : ' + value);
    if (modeKey === RaceMode.STEP) {
      this.mode = RaceMode.STEP;
      this.displaySteps = true;
    } else {
      this.displaySteps = false;
      this.mode = RaceMode.EDIT;
    }
    
    if (this.selectedRaceId) {
      this.race = new Race();
      this.race.id = this.selectedRaceId;
    } else {
      this.addLocationOnly = true;
      this.selectedRaceId = -1;
    }
    // console.log('selectedRaceId: ' + this.selectedRaceId);

    this.addressForm = new FormGroup({
      addressId: new FormControl(null),
      location: new FormControl(null, Validators.required),
      street: new FormControl(null),
      city: new FormControl(null),
      state: new FormControl('FL', Validators.required),
      zip: new FormControl(null),
      country: new FormControl('United States'),
      phone: new FormControl(''),
      notes: new FormControl(''),
    });

    this.loadGeo();

    if (this.selectedRaceId > 0) {
      this.getAddresses();
    }

  }

  /**
   * Load list boxes
   */
  public loadGeo() {
    this.metadataService.getStates().subscribe(
        (data) => {
          this.states = data;
        },
        err => {
          console.error('Error Loading States : ' + err);
          this.appErrorHandler.handleError(err);
        },
    );

    this.metadataService.getCountries().subscribe(
        (data) => {
          this.countries = data;
        },
        err => {
          console.error('Error Loading Countries : ' + err);
          this.appErrorHandler.handleError(err);
        },
      );
  }

  public getAddresses() {

      this.addressService.getAddresses().subscribe(
        addresses => {
          this.addresses = addresses;
          const noAddress = new Address();
          noAddress.id = 0;
          noAddress.location = 'Choose Existing';
          this.addresses.splice(0, 0, noAddress);
        },
        err => {
          this.appErrorHandler.handleError(err);
        }
      );
  }

      /**
     * Go back to main page
     * @param event
     */
    cancel(event: any) {
      this.router.navigate([IJudyConstants.RACES_URI]);
    }

    onSubmit() {
      this.address.location = this.addressForm.value['location'];
      this.address.street   = this.addressForm.value['street'];
      this.address.city    = this.addressForm.value['city'];
      this.address.state   = this.addressForm.value['state'];
      this.address.zip     = this.addressForm.value['zip'];
      this.address.country = this.addressForm.value['country'];
      this.address.phone   = this.addressForm.value['phone'];
      this.address.notes   = this.addressForm.value['notes'];

      // console.log('Inserted Address ' + JSON.stringify(this.address));

        this.addressService.saveAddress(this.address).subscribe(
          savedAddress => {
            if (this.selectedRaceId > 0) {
              // This is part of the my event story
              const race = new Race();
              race.id = this.selectedRaceId;
              race.address = savedAddress;
              // console.log('Race Id: '    + race.id);
              // console.log('Address Id: ' + race.address.id);

              this.racesService.updateRaceAddress(race).subscribe(
                () => {
                  // console.error('Error Inserting Address' + err);
                  this.router.navigate([IJudyConstants.RACES_URI]);
                },
                err => {
                  // console.error('Error Inserting Address' + err);
                  this.appErrorHandler.handleError(err);
                },
                () =>  this.messageService.add(
                  {severity: 'success',
                  summary: 'Race Location Added',
                  detail: 'Added : ' + this.address.location})
                );
            } else {
              this.router.navigate([IJudyConstants.RACES_URI]);
            }

          },
          err => {
            // console.error('Error Inserting Address' + err);
            this.appErrorHandler.handleError(err);
          },
          () =>  this.messageService.add(
           {severity: 'success',
           summary: 'Race Location Added',
           detail: 'Added : ' + this.address.location})
        );

    }

    public chooseExistingLocation() {

      if (this.addressForm.value['addressId']) {
        this.race.address.id = this.addressForm.value['addressId'].id;
      }

      // console.log('Race Id: ' + this.race.id);
      // console.log('Updating Race with existing location  : ' + this.race.address.id);

      this.addressService.updateRaceLocation(this.race.id, this.race.address.id).subscribe(
        data => {
          this.race = data;
          this.router.navigate([IJudyConstants.RACES_URI]);
        },
        err => {
          // console.error('Error Inserting Address' + err);
          this.appErrorHandler.handleError(err);
        },
        () =>  this.messageService.add(
         {severity: 'success',
         summary: 'Race Address Updated',
         detail: 'Updated Race with : ' + this.address.location})
      );

    }

    public locationChosen(): boolean {
      return (this.addressForm.value['location'] && this.addressForm.value['location'].length > 0);
    }

}


import { Component, OnInit, Input} from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { MessageService} from 'primeng/api';

import { RacesService} from '../../../service/races';
import { AddressService} from '../../../service/address';
import { Race, RaceType } from '../../../domain/race';
import { Address} from '../../../domain/address';
import { IJudyConstants} from '../../../util/constants';

import { AppErrorHandler} from '../../../service/error-handler/app-error-handler';

@Component({
  selector: 'ijudy-update-race',
  templateUrl: './update-race.component.html',
  styleUrls: ['./update-race.component.css']
})
export class UpdateRaceComponent implements OnInit {

  @Input()
  get raceTypesFormControl() {
    return this.raceForm.controls['selectedRaceTypes'] as FormControl;
  }

  @Input()
  get addressFormControl() {
    return this.raceForm.controls['raceAddress'] as FormControl;
  }


  public race: Race;
  public addresses: Address[];
  public raceTypes: RaceType[];
  public selectedRaceTypes: RaceType[] = new Array();
  public selectedAddress: Address;

  public raceForm = new FormGroup({
    raceId:              new FormControl(null, Validators.required),
    selectedRaceTypes:   new FormControl(null, Validators.required),
    raceName:            new FormControl('', Validators.required),
    raceDate:            new FormControl('', Validators.required),
    raceDescription:     new FormControl(''),
    raceAddress:         new FormControl(null, Validators.required),
    raceUrl:             new FormControl(''),
    cancelled:            new FormControl(false, Validators.required),
  });

  constructor(
    private addressService: AddressService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private errorHandler: AppErrorHandler,
    private racesService: RacesService,
    private messageService: MessageService) { }

  ngOnInit() {
    this.race = new Race();
    this.race.id = this.activatedRoute.snapshot.params['raceId'] as number;
    // console.log('RaceId : ' + this.race.id);
    this.loadRace();
    this.getAddresses();
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
  public loadRace() {
    this.racesService.getRace(this.race.id).subscribe(
      race => {
        this.race = race;
        this.raceForm.controls['raceName'].setValue(this.race.name);
        this.raceForm.controls['raceDate'].setValue(this.race.date);
        this.raceForm.controls['raceDescription'].setValue(this.race.description);
        this.raceForm.controls['raceAddress'].setValue(this.race.address);
        this.raceForm.controls['raceUrl'].setValue(this.race.url);
        this.raceForm.controls['cancelled'].setValue(this.race.cancelled);

        const address = this.race.address;

        this.race.raceTypes.forEach(
          raceType => {
            this.selectedRaceTypes.push(raceType);
          }
        );

        this.raceTypesFormControl.setValue(this.selectedRaceTypes);
        this.addressFormControl.setValue(address);

       },
      err => {
        console.error(err);
      },
      () =>  this.messageService.add(
       {severity: 'success',
       summary: 'Race Loaded',
       detail: 'Loaded ' + this.race.name})
    );
  }

  public getAddresses() {
    this.addressService.getAddresses().subscribe(
      addresses => {
        this.addresses = addresses;
        this.addresses .sort((a, b) => (a.location > b.location) ? 1 : -1);
       },
      err => {
        console.error(err);
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
      this.race.name             = this.raceForm.value['raceName'];
      this.race.date             = this.raceForm.value['raceDate'];
      this.race.description      = this.raceForm.value['raceDescription'];
      this.race.address          = this.raceForm.value['raceAddress'];
      this.race.url              = this.raceForm.value['raceUrl'];
      this.race.cancelled        = this.raceForm.value['cancelled'];
      console.log('Updated ' + JSON.stringify(this.raceForm.value));
      this.racesService.updateRace(this.race).subscribe(
          race => {
              // console.log('updated ' + race.name);
              this.router.navigate([IJudyConstants.RACES_URI]);
          },
          errResponse => {
            this.errorHandler.handleError(errResponse);
          }

        );
    }
}

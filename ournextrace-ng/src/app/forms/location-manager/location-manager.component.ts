import { Component, OnInit} from '@angular/core';

import { MessageService} from 'primeng/api';
import { ConfirmationService} from 'primeng/api';

import { AuthService} from '../../service/auth';
import { AddressService} from '../../service/address';

import { Race, LocationChange} from '../../domain/race';
import { User} from '../../domain/user';
import { Address} from '../../domain/address';
import { AppErrorHandler} from '../../service/error-handler/app-error-handler';

@Component({
  selector: 'ijudy-location-manager',
  templateUrl: './location-manager.component.html',
  styleUrls: ['./location-manager.component.css']
})
export class LocationManagerComponent implements OnInit {
  cols: any[];
  public races: Race[];
  public locations: Address[];
  public users: User[];

  constructor(private addressService: AddressService,
              private authService: AuthService,
              private errorHandler: AppErrorHandler,
              private messageService: MessageService,
              private confirmationService: ConfirmationService) {}

  ngOnInit() {

    this.cols = [
      { field: 'location', header: 'Location' },  // 1
      { field: 'street', header: 'Street' },      // 2
      { field: 'city', header: 'City' },          // 3
      { field: 'state', header: 'State' },        // 4
      { field: 'zip', header: 'Zip' },            // 5
      { field: 'country', header: 'Country' },    // 6
      { field: 'phone', header: 'Phone' },        // 7
      { field: 'notes', header: 'Notes' },        // 8
      { field: 'authorId', header: 'Author' },    // 9
      { field: 'actions', header: 'Actions' },    // 10
    ];

    this.refreshControls();
  }

  private loadAuthorizedUsers() {
    this.authService.getAuthorizedUsers().subscribe(
      users => {
        this.users = users;
        const fakeUser: User = {
          id: -1,
          name: 'Select User',
          user: false
        };
      },
      err => {
        console.error(err);
        this.errorHandler.handleError(err);
      }
    );
  }

  private refreshControls() {
    this.loadAuthorizedUsers();
    this.loadLocations();
  }

  public refreshData() {

    this.refreshControls();

    this.messageService.add(
      {severity: 'success',
      summary: 'Refresh',
      detail: 'Refreshed Races'});
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

  public onComponentEditComplete(event: any, location: Address, field: string, rowIndex: number) {
      const change = new LocationChange();
      // console.log('field : ' + field);
      // console.log('$event : ' + JSON.stringify(event.value));
      // console.log('rowIndex : ' + rowIndex);
      change.field    = field;
      change.data     = location;
      change.index    = rowIndex;

      this.onEditComplete(change);
  }

  public onEditComplete(change: LocationChange) {

    // console.log('field : ' + change.field);
    // console.log('race event: ' + JSON.stringify(raceChange));
    let location = new Address();
    location = change.data;

    this.addressService.saveAddress(location).subscribe(
      () => {
        this.messageService.add(
          {severity: 'success',
          summary: 'Location',
          detail: 'Updated ' + location.location});
      },
      err => {
        console.error(err);
        this.messageService.add(
          {severity: 'error',
          summary: 'Location',
          detail: 'Not Updated ' + location.location});
      }
    );

  }

  confirm(addressId: number) {
    this.confirmationService.confirm({
        message: 'Are you sure that you want to Delete Location?',
        header: 'Confirmation',
        icon: 'pi pi-exclamation-triangle',
        accept: () => {
          // console.log('Deleting race id : ' + addressId);
          this.addressService.deleteAddress(addressId).subscribe(
            () => {
              this.refreshData();
              this.messageService.add(
                {severity: 'success',
                summary: 'Location',
                detail: 'Deleted ' + addressId});
            },
            err => {
              console.error(err);
              this.messageService.add(
                {severity: 'error',
                summary: 'Location',
                detail: 'Not Deleted ' + addressId});
            }
          );
        },
        reject: () => {
          this.messageService.add(
            {severity: 'rejected',
            summary: 'Location',
            detail: 'Not Deleted ' + addressId});
        }
    });
  }
}

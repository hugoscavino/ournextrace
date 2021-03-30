import { Component, OnInit} from '@angular/core';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MessageService, SelectItem} from 'primeng/api';
import { User} from '../../../domain/user';
import { AuthService} from '../../../service/auth';
import { IJudyConstants} from '../../../util/constants';
import { AppErrorHandler} from '../../../service/error-handler/app-error-handler';
import { MetadataService } from 'src/app/service/metadata';

@Component({
  selector: 'ijudy-update-user',
  templateUrl: './update-user.component.html',
  styleUrls: ['./update-user.component.css']
})
export class UpdateUserComponent implements OnInit {

  public user: User;
  public states: SelectItem[];
  public countries: SelectItem[];
  public updateUserForm: FormGroup;


  constructor(private authService: AuthService,
              private router: Router,
              private formBuilder: FormBuilder,
              private messageService: MessageService,
              private appErrorHandler: AppErrorHandler,
              private metadataService: MetadataService) { }

  ngOnInit() {
    this.updateUserForm = this.formBuilder.group({
      email: new FormControl({value: null, disabled: true}, Validators.required),
      name:  [''],
      city:  [''],
      state:  [''],
      zip:  [''],
      country :  [''],
      firstName:  [''],
      lastName:  ['']
    });

    this.loadUser();
    this.loadGeo();
  }

  public loadGeo() {
    this.metadataService.getStates().subscribe(
        (data) => {
          this.states = data;
        },
        err => {
          // console.error('Error Loading States : ' + err);
          this.appErrorHandler.handleError(err);
        },
    );

    this.metadataService.getCountries().subscribe(
      (data) => {
        this.countries = data;
      },
      err => {
        // console.error('Error Loading Countries : ' + err);
        this.appErrorHandler.handleError(err);
      },
  );
  }

  public loadUser() {
          this.authService.getUser().subscribe(
              (user) => {
                this.user = user;
                // console.log('Loading User : ' + this.user.email);
                this.updateUserForm.controls['email'].setValue(this.user.email);
                this.updateUserForm.controls['city'].setValue(this.user.city);
                this.updateUserForm.controls['state'].setValue(this.user.state);
                this.updateUserForm.controls['zip'].setValue(this.user.zip);
                this.updateUserForm.controls['country'].setValue(this.user.country);
                this.updateUserForm.controls['firstName'].setValue(this.user.firstName);
                this.updateUserForm.controls['lastName'].setValue(this.user.lastName);
              },
              (error) => {
                console.error('Loading User Error: ', error);
                this.appErrorHandler.handleError(error);
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
      const updateUser: User = {
          name: this.user.name,
          email: this.user.email,
          city: this.updateUserForm.controls['city'].value,
          state: this.updateUserForm.controls['state'].value,
          zip: this.updateUserForm.controls['zip'].value,
          country: this.updateUserForm.controls['country'].value,
          firstName: this.updateUserForm.controls['firstName'].value,
          lastName: this.updateUserForm.controls['lastName'].value,
          user: true
      };

      // console.log('onSubmit User : ' + JSON.stringify(updateUser));
      this.authService.updateUser(updateUser).subscribe(
        data => {
          // console.log('Updated User');
          this.user = data;
        },
        err => {
          // console.error('Error Inserting Address' + err);
          this.appErrorHandler.handleError(err);
        },
        () => {
           //  console.log('UPDATE completed');
           this.messageService.add({severity: 'success', summary: 'Updated', detail: this.user.name + ' was updated'});
           this.router.navigate([IJudyConstants.RACES_URI]);
         }
      );
    }

}

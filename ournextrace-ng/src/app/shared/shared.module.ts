import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import {TableModule} from 'primeng/table';

import {DataViewModule} from 'primeng/dataview';
import {PanelModule} from 'primeng/panel';
import {DialogModule} from 'primeng/dialog';
import {TabViewModule} from 'primeng/tabview';
import {DropdownModule} from 'primeng/dropdown';
import {ButtonModule} from 'primeng/button';
import {MessageModule} from 'primeng/message';

import {InputSwitchModule} from 'primeng/inputswitch';
import {InputTextModule} from 'primeng/inputtext';
import {CalendarModule} from 'primeng/calendar';

import {InputTextareaModule} from 'primeng/inputtextarea';
import {InputMaskModule} from 'primeng/inputmask';
import {PasswordModule} from 'primeng/password';
import {EditorModule} from 'primeng/editor';
import {CaptchaModule} from 'primeng/captcha';
import {MultiSelectModule} from 'primeng/multiselect';
import {ConfirmDialogModule} from 'primeng/confirmdialog';


@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    RouterModule,
    CaptchaModule,
    DataViewModule,
    PanelModule,
    DialogModule,
    TabViewModule,
    DropdownModule,
    ButtonModule,
    InputSwitchModule,
    InputTextModule,
    CalendarModule,
    InputTextareaModule,
    InputMaskModule,
    PasswordModule,
    MessageModule,
    EditorModule,
    TableModule,
    MultiSelectModule,
    ConfirmDialogModule
  ],
  declarations: [
  ],
  exports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    RouterModule,
    CaptchaModule,
    DataViewModule,
    PanelModule,
    DialogModule,
    TabViewModule,
    DropdownModule,
    ButtonModule,
    InputSwitchModule,
    InputTextModule,
    CalendarModule,
    InputTextareaModule,
    InputMaskModule,
    PasswordModule,
    MessageModule,
    EditorModule,
    TableModule,
    MultiSelectModule,
    ConfirmDialogModule
  ]
})
export class SharedModule {}

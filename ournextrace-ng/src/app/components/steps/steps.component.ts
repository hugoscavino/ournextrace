import { Component, OnInit, Output, Input, EventEmitter } from '@angular/core';
import {MenuItem} from 'primeng/api';

@Component({
  selector: 'ijudy-steps',
  templateUrl: './steps.component.html',
  styleUrls: ['./steps.component.css']
})
export class StepsComponent implements OnInit {

  items: MenuItem[];

  private activeIndexValue = 0;

  @Output() activeIndexChange = new EventEmitter();

  @Input()
  get activeIndex() {
    return this.activeIndexValue;
  }

  set activeIndex(val) {
    this.activeIndexValue = val;
    this.activeIndexChange.emit(this.activeIndexValue);
  }


  private displayVal = false;
  @Output() displayChange = new EventEmitter();

  @Input()
  get display() {
    return this.displayVal;
  }

  set display(val) {
    this.displayVal = val;
    this.displayChange.emit(this.displayVal);
  }

  constructor() { }

  ngOnInit() {

      this.items = [
        {
          label: 'New Race',
          command: (event: any) => {
              this.activeIndexValue = 0;
              // this.messageService.add({severity:'info', summary:'Seat Selection', detail: event.item.label});
          }
      },
          {
            label: 'My Race Details',
            command: (event: any) => {
                this.activeIndexValue = 1;
                // this.messageService.add({severity:'info', summary:'Seat Selection', detail: event.item.label});
            }
        },
        {
          label: 'Race Location',
          command: (event: any) => {
              this.activeIndexValue = 2;
              // this.messageService.add({severity:'info', summary:'First Step', detail: event.item.label});
          }
      },

      ];
  }
 }


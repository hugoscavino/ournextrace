import { Component, OnInit, Input, Output, EventEmitter, ViewChild } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { RaceStatus } from 'src/app/domain/race';
import { SplitButton } from 'primeng/splitbutton';

@Component({
  selector: 'ijudy-interested',
  templateUrl: './interested.component.html',
  styleUrls: ['./interested.component.css']
})
export class InterestedComponent implements OnInit {

  public readonly NotAssignedMenu: MenuItem = { label: 'Add to Plan',    icon: 'far fa-calendar-plus', style: 'ui-button-info'};
  public readonly Interested:      MenuItem = { label: 'Interested', icon: 'fas fa-star', style: 'ui-button-info'};
  public readonly GoingMenu:       MenuItem = { label: 'Going',      icon: 'fas fa-check', style: 'ui-button-success'};
  public readonly CantGoMenu:      MenuItem = { label: 'Can\'t Go',  icon: 'fas fa-times', style: 'ui-button-danger'};
  public readonly VolunteerMenu:   MenuItem = { label: 'Volunteer',  icon: 'fas fa-walking',  style: 'ui-button-warning'};
  public readonly DeleteMeMenu:    MenuItem = { label: 'Delete from Plan',     icon: 'fas fa-trash-alt',  style: 'ui-button-danger'};

  private raceStatusVal: RaceStatus = RaceStatus.NOT_ASSIGNED;

  @Input()
  public readonly: boolean;

  @Input()
  public showDelete: boolean;

  @Output() raceStatusChange = new EventEmitter<RaceStatus>();
  @Input()
  public get raceStatus() {
      return this.raceStatusVal;
  }
  public set raceStatus(val: RaceStatus) {
    this.setState(val);
  }

  @ViewChild(SplitButton) splitButtonComponent: SplitButton;

  public menuItems: MenuItem[];
  public menuLabel: string;
  public menuIcon: string;
  public menuStyle: string;

  constructor() {
    this.readonly = false;
    this.showDelete = true;
    this.menuLabel = this.NotAssignedMenu.label;
    this.menuStyle = this.NotAssignedMenu.style;
    this.menuIcon  = this.NotAssignedMenu.icon;
  }

  ngOnInit() {
    if (this.readonly	) {
      this.menuLabel = this.NotAssignedMenu.label;
      this.menuStyle = this.NotAssignedMenu.style;
      this.menuIcon  = this.NotAssignedMenu.icon;
    } else {
      this.menuItems = [
        /*
        {label: this.NotAssignedMenu.label, icon: this.NotAssignedMenu.icon,
                styleClass: this.NotAssignedMenu.style, command: (event: any) => {
            this.setState(RaceStatus.NOT_ASSIGNED);
            this.raceStatusChange.emit(RaceStatus.NOT_ASSIGNED);
        }},
        */
        {label: this.Interested.label, icon: this.Interested.icon, styleClass: this.Interested.style, command: (event: any) => {
          this.setState(RaceStatus.INTERESTED);
          this.raceStatusChange.emit(RaceStatus.INTERESTED);
        }},
        {label: this.GoingMenu.label, icon: this.GoingMenu.icon,  styleClass: this.GoingMenu.style, command: (event: any) => {
          this.setState(RaceStatus.GOING);
          this.raceStatusChange.emit(RaceStatus.GOING);
        }},
        {label: this.CantGoMenu.label, icon: this.CantGoMenu.icon, styleClass: this.CantGoMenu.style, command: (event: any) => {
          this.setState(RaceStatus.NOT_GOING);
          this.raceStatusChange.emit(RaceStatus.NOT_GOING);

        }},
        {label: this.VolunteerMenu.label, icon: this.VolunteerMenu.icon, styleClass: this.VolunteerMenu.style, command: (event: any) => {
          this.setState(RaceStatus.VOLUNTEERING);
          this.raceStatusChange.emit(RaceStatus.VOLUNTEERING);
        }},
      ];
    }

    if (this.menuItems && this.showDelete) {
      this.menuItems.push(
              {label: this.DeleteMeMenu.label, icon: this.DeleteMeMenu.icon, styleClass: this.DeleteMeMenu.style,
                command: (event: any) => {
                  this.setState(RaceStatus.DELETE_ME);
                  this.raceStatusChange.emit(RaceStatus.DELETE_ME);
              }});
    }

  }

  private setState(raceStatus: RaceStatus) {

    switch (raceStatus) {
      case RaceStatus.GOING:
          this.menuLabel = this.GoingMenu.label;
          this.menuStyle = this.GoingMenu.style;
          this.menuIcon  = this.GoingMenu.icon;
          break;
      case RaceStatus.NOT_GOING:
            this.menuLabel = this.CantGoMenu.label;
            this.menuStyle = this.CantGoMenu.style;
            this.menuIcon  = this.CantGoMenu.icon;
          break;
      case RaceStatus.INTERESTED:
            this.menuLabel = this.Interested.label;
            this.menuStyle = this.Interested.style;
            this.menuIcon  = this.Interested.icon;
          break;
      case RaceStatus.NOT_ASSIGNED:
          this.menuLabel = this.NotAssignedMenu.label;
          this.menuStyle = this.NotAssignedMenu.style;
          this.menuIcon  = this.NotAssignedMenu.icon;
          break;
      case RaceStatus.VOLUNTEERING:
          this.menuLabel = this.VolunteerMenu.label;
          this.menuStyle = this.VolunteerMenu.style;
          this.menuIcon  = this.VolunteerMenu.icon;
          break;
      case RaceStatus.DELETE_ME:
            this.menuLabel = this.DeleteMeMenu.label;
            this.menuStyle = this.DeleteMeMenu.style;
            this.menuIcon  = this.DeleteMeMenu.icon;
          break;
       default:
          break;
    }
    // Invoke the emit!
    this.raceStatusVal = raceStatus;

  }

  public addToCal(event: any) {
    // Invoke the emit!
    this.raceStatusVal = RaceStatus.INTERESTED;
  }

  public selectInterest(event: any) {
    this.splitButtonComponent.onDropdownButtonClick(event);
  }

}

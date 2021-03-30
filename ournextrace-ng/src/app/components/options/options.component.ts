import { Component, OnInit, Input, ViewChild } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { SplitButton } from 'primeng/splitbutton';

@Component({
  selector: 'ijudy-options',
  templateUrl: './options.component.html',
  styleUrls: ['./options.component.css']
})
export class OptionsComponent implements OnInit {

  @Input()
  public raceId: string;

  @Input()
  public showMyRace: boolean;

  @Input()
  public showOwner: boolean;

  @ViewChild(SplitButton) splitButtonComponent: SplitButton;

  public menuItems: MenuItem[] = [];

  constructor() { }

  ngOnInit() {
    const editMyRaceMenuItem: MenuItem = {label: 'My Race Plan', icon: 'fas fa-cog', routerLink: ['/edit-myrace', this.raceId]};
    const editRaceMenuItem: MenuItem = {label: 'Race Itself', icon: 'fas fa-cog', routerLink: ['/update-race', this.raceId]};
    if (this.showMyRace) {
      this.menuItems.push(editMyRaceMenuItem);
    }
    if (this.showOwner) {
      this.menuItems.push(editRaceMenuItem);
    }
  }

  public editMyRace(event: any) {
    this.splitButtonComponent.onDropdownButtonClick(event);
  }

}

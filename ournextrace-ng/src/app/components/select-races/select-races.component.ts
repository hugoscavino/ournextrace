import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { RaceType } from 'src/app/domain/race';
import { RacesService } from 'src/app/service/races';

@Component({
  selector: 'ijudy-select-races',
  templateUrl: './select-races.component.html',
  styleUrls: ['./select-races.component.css']
})
export class SelectRacesComponent implements OnInit {

  public raceTypes: RaceType[];
  public selectedRaceTypesVal: RaceType[];
  @Output() selectedRaceTypesChange = new EventEmitter<RaceType[]>();

  @Output()
  public raceTypesSelectedChange = new EventEmitter<RaceType[]>();

  @Input()
  public defaultLabel: string;

  @Input()
  public filterPlaceHolder: string;

  @Input()
  public get selectedRaceTypes(): RaceType[] {
      return this.selectedRaceTypesVal;
  }
  public set selectedRaceTypes(val: RaceType[]) {
    this.selectedRaceTypesVal = val;
    this.selectedRaceTypesChange.emit(val);
  }

  constructor(public racesService: RacesService) {
    this.defaultLabel = 'Select Races';
    this.filterPlaceHolder = 'Chose multiple';
   }

  ngOnInit() {

    this.racesService.getAllRaceTypes().subscribe(
        raceTypes => {
          this.raceTypes = raceTypes;
        }
    );
  }

  /**
   * event.originalEvent: browser event
   * event.value: Current selected values
   * event.itemValue: Toggled item value
   */
  raceTypesSelected($event: any) {
    console.log('SelectRacesComponent::raceTypesSelected event.itemValue' + JSON.stringify($event.itemValue));
    this.raceTypesSelectedChange.emit($event.itemValue);
  }

}

import { Component, OnInit} from '@angular/core';
import { AgRendererComponent, } from 'ag-grid-angular';
import { RaceType } from 'src/app/domain/race';
import { ICellRendererParams } from 'ag-grid-community';

@Component({
  selector: 'ijudy-app-race-type-renderer',
  templateUrl: './ag-grid-race-type-renderer.component.html',
  styleUrls: ['./ag-grid-race-type-renderer.component.css']
})
export class AgGridRaceTypeRendererComponent implements OnInit, AgRendererComponent {

    public selectRaceTypes: RaceType[];
    public params: ICellRendererParams;

    constructor() {
        // no op
    }

    ngOnInit() {
        // no init
     }

    agInit(params: ICellRendererParams): void {
        this.selectRaceTypes = params.value;
        // console.log('this.raceTypes : ' + this.selectRaceTypes.length);
    }

    refresh(params: ICellRendererParams): boolean {
       const raceTypes: RaceType[] = params.node.data.raceTypes;
       this.selectRaceTypes = raceTypes;
       return true;
    }


}

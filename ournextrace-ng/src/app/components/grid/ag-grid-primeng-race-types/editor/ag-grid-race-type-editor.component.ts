import { Component, OnInit} from '@angular/core';
import { AgEditorComponent, } from 'ag-grid-angular';
import { ICellEditorParams } from 'ag-grid-community';
import { RaceType } from 'src/app/domain/race';
import { ValuesICellEditorParams } from '../../ag-grid-types/ext-cell-editor-params';

@Component({
  selector: 'ijudy-app-race-type-editor',
  templateUrl: './ag-grid-race-type-editor.component.html',
  styleUrls: ['./ag-grid-race-type-editor.component.css']
})
export class AgGridRaceTypeEditorComponent implements OnInit, AgEditorComponent {

    public raceTypes: RaceType[];
    public params: ICellEditorParams;
    public selectRaceTypes: RaceType[];

    constructor() {
        // no op
    }

    ngOnInit() {
        // no init
     }

    isPopup(): boolean {
        return true;
    }

    isCancelBeforeStart(): boolean {
        return false;
    }

    isCancelAfterEnd(): boolean {
        return false;
    }

    agInit(params: ICellEditorParams): void {
        this.params      = params as ICellEditorParams;
        this.selectRaceTypes = this.params.value;
        const valuesParams  = params as unknown as ValuesICellEditorParams;
        this.raceTypes        = valuesParams.values;
    }

    public getValue() {
       console.log('returning : ' + JSON.stringify(this.selectRaceTypes));
       return this.selectRaceTypes;
    }

    /**
     * @param event
     * event.originalEvent Browser event
     * event.value         Current selected values
     * event.itemValue     Toggled item value
     */
    public onSelectChange(event: any): void {
        // console.log(event.originalEvent);   // browser event
        // console.log(event.value);           // Current selected values
        // console.log(updatedRaceTypes);       // Toggled item value

        setTimeout(function () {
            console.log('updating : ' + this.params.node.data.raceTypes);
            this.params.node.data.raceTypes = this.selectRaceTypes;
            console.log('to be instead : ' + this.params.node.data.raceTypes);
            this.params.api.refreshCells();
            this.params.api.stopEditing();
        }.bind(this));
    }

}

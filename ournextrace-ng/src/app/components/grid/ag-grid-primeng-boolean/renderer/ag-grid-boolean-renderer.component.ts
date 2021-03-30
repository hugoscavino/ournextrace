import {Component, OnInit} from '@angular/core';
import { ICellRendererParams} from 'ag-grid-community';
import { AgRendererComponent} from 'ag-grid-angular';

@Component({
    selector: 'ijudy-app-ag-grid-boolean-renderer',
    templateUrl: './ag-grid-boolean-renderer.component.html',
    styleUrls: ['./ag-grid-boolean-renderer.component.css']
  })
export class AgGridBooleanRendererComponent implements OnInit, AgRendererComponent {

    public params: ICellRendererParams;
    public checked: boolean;
    public field: string;

    ngOnInit() {
    }

    agInit(params: ICellRendererParams): void {
        this.params = params;
        this.field = params.colDef.field;
        this.checked = params.value;
    }

    refresh(params: ICellRendererParams): boolean {
        const newValue: boolean = params.node.data[this.field];
        // console.log('Changing to : ' + newValue);
        this.checked = newValue;
        return true;
     }

}

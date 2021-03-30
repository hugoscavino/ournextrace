import {Component, OnInit} from '@angular/core';
import {ICellEditorParams} from 'ag-grid-community';
import {AgEditorComponent} from 'ag-grid-angular';

@Component({
    selector: 'ijudy-app-ag-grid-datepicker-editor',
    templateUrl: './ag-grid-boolean-editor.component.html',
    styleUrls: ['./ag-grid-boolean-editor.component.css']
  })
export class AgGridPrimengBooleanEditorComponent implements OnInit, AgEditorComponent {

    public params: ICellEditorParams;
    public checked: boolean;
    public field: string;

    constructor() {
    }

    ngOnInit() {
    }

    isPopup(): boolean {
        return false;
    }

    isCancelBeforeStart(): boolean {
        return false;
    }

    isCancelAfterEnd(): boolean {
        return false;
    }

    agInit(params: any): void {
        this.params = params;
        this.checked = params.value;
        this.field = params.colDef.field;

    }

    getValue() {
        return this.checked;
    }

    onSelectChange(event: any): void {
        const opposite: boolean = !this.checked;
        // console.log('switching this.field' + this.field + ' to ' + this.checked + ' from ' + opposite);
        this.checked = opposite;
        setTimeout(function () {
            this.params.node.data[this.field] = this.checked;
            this.params.api.stopEditing();
        }.bind(this));
    }

}

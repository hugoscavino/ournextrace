import {Component, OnInit, ViewChild} from '@angular/core';
import {ICellEditorParams} from 'ag-grid-community';
import {AgEditorComponent} from 'ag-grid-angular';
import {Calendar} from 'primeng/calendar';

@Component({
    selector: 'ijudy-app-ag-grid-primeng-datepicker-editor',
    templateUrl: './ag-grid-primeng-datepicker-editor.component.html',
    styleUrls: ['./ag-grid-primeng-datepicker-editor.component.css']
  })
export class AgGridPrimengDatepickerEditorComponent implements OnInit, AgEditorComponent {

    public columnWidth: string;
    public params: ICellEditorParams;
    public value: string;

    @ViewChild('picker') picker: Calendar;

    constructor() {
    }

    ngOnInit() {
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

    agInit(params: any): void {
        this.params = params;
        this.value = params.value;
    }

    getValue(): string {
        return this.value;
    }

    onSelectChange(event: any): void {
        setTimeout(function () {
            this.params.stopEditing();
        }.bind(this));
    }

}

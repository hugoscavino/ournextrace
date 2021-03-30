import { Component, OnInit, ViewChild} from '@angular/core';
import { AgEditorComponent, } from 'ag-grid-angular';
import { Dropdown } from 'primeng/dropdown';
import { SelectItem } from 'primeng/api/selectitem';
import { ValuesICellEditorParams } from '../ag-grid-types/ext-cell-editor-params';
import { ICellEditorParams } from 'ag-grid-community';

@Component({
  selector: 'ijudy-app-ag-grid-primeng-select-editor',
  templateUrl: './ag-grid-primeng-select-editor.component.html',
  styleUrls: ['./ag-grid-primeng-select-editor.component.css']
})
export class AgGridPrimeNgSelectEditorComponent implements OnInit, AgEditorComponent {
    public options: SelectItem[];
    public selectItem: SelectItem;
    public paramsValues: ValuesICellEditorParams;
    @ViewChild('select') dropdownSelect: Dropdown;

    constructor() {
        // no op
    }

    ngOnInit() {
        // no init
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

    agInit(params: ICellEditorParams) {
        this.paramsValues = params as unknown as ValuesICellEditorParams;
        this.selectItem          = this.paramsValues.value;
        this.options         = this.paramsValues.values;
    }

    getValue(): string {
        return this.selectItem.value;
    }

    onSelectChange($event: any) {
        this.paramsValues.stopEditing();
    }

}

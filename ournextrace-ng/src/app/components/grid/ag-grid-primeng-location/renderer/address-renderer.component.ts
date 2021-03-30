import { Component, OnInit} from '@angular/core';
import { AgEditorComponent, } from 'ag-grid-angular';
import { ICellEditorParams } from 'ag-grid-community';
import { AuthService } from '../../../../service/auth';
import { Address } from 'src/app/domain/address';

@Component({
  selector: 'ijudy-address-renderer.component',
  templateUrl: './address-renderer.component.html',
  styleUrls: ['./address-renderer.component.css']
})
export class AgGridAddressRendererComponent implements OnInit, AgEditorComponent {

    public paramsValues: ICellEditorParams;
    public address: Address;

    constructor(public authService: AuthService) {
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
        this.paramsValues = params;
        this.address = this.paramsValues.value as Address;
    }

    getValue(): string {
        return this.address.location;
    }

    onSelectChange($event: any) {
        this.paramsValues.stopEditing();
    }

}

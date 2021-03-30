import { Component, OnInit} from '@angular/core';
import { AgEditorComponent, } from 'ag-grid-angular';
import { ValuesICellEditorParams } from '../../ag-grid-types/ext-cell-editor-params';
import { ICellEditorParams } from 'ag-grid-community';
import { AddressService } from '../../../../service/address';
import { Address } from '../../../../domain/address';


@Component({
  selector: 'ijudy-ag-grid-address-editor',
  templateUrl: './address-editor.component.html',
  styleUrls: ['./address-editor.component.css']
})
export class AgGridAddressEditorComponent implements OnInit, AgEditorComponent {
    public address: Address;
    public paramsValues: ValuesICellEditorParams;
    public results: Address[];
    public initialResults: Address[];

    constructor(public addressService: AddressService) {
        this.results = new Array<Address>();
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

    agInit(params: ICellEditorParams) {
        this.paramsValues = params as unknown as ValuesICellEditorParams;
        this.address      = this.paramsValues.value as Address;
    }

    search(event: any) {
        const criteria: string = event.query;

        if (!criteria) {
            return this.results;
        }

        this.addressService.getAddressesCache().subscribe(
            (addresses: Address[]) => {
                this.initialResults = addresses;
                this.filterResults(criteria);
        });

    }

    private filterResults(criteria: string) {
        this.results = this.initialResults.filter(
            (address: Address) => {
                const comp = address.location.toLowerCase();
                return comp.includes(criteria.toLowerCase());
            });
    }

    public getValue() {
        return this.address;
    }

    public onSelectChange($event: any) {
        this.paramsValues.stopEditing();
    }

    public onSelect(value: Address) {
        this.address = value;
    }
}


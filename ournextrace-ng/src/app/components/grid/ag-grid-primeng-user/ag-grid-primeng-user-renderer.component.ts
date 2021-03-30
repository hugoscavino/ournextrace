import { Component, OnInit} from '@angular/core';
import { AgEditorComponent, } from 'ag-grid-angular';
import { ValuesICellEditorParams } from '../ag-grid-types/ext-cell-editor-params';
import { ICellEditorParams } from 'ag-grid-community';
import { IUser, User } from 'src/app/domain/user';
import { AuthService } from '../../../service/auth';

@Component({
  selector: 'ijudy-app-ag-grid-primeng-user-renderer',
  templateUrl: './ag-grid-primeng-user-renderer.component.html',
  styleUrls: ['./ag-grid-primeng-user-renderer.component.css']
})
export class AgGridPrimeNgUserRendererComponent implements OnInit, AgEditorComponent {
    static readonly ADMIN_ID = 0;
    public user: IUser;
    public paramsValues: ValuesICellEditorParams;

    constructor(public authService: AuthService) {
        // no op
    }

    ngOnInit() {
        // no init
     }

    isPopup(): boolean {
        return false;
    }

    isAdmin(): boolean {
        return this.user && this.user.id === AgGridPrimeNgUserRendererComponent.ADMIN_ID;
    }

    isNormalUser(): boolean {
        return this.user && (this.user.id !== AgGridPrimeNgUserRendererComponent.ADMIN_ID);
    }

    isCancelBeforeStart(): boolean {
        return false;
    }

    isCancelAfterEnd(): boolean {
        return false;
    }

    agInit(params: ICellEditorParams) {
        this.paramsValues = params as unknown as ValuesICellEditorParams;
        this.user = this.paramsValues.value;
    }

    getValue(): string {
        return this.user.name;
    }

    onSelectChange($event: any) {
        this.paramsValues.stopEditing();
    }

}

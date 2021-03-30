import { Component, OnInit} from '@angular/core';
import { AgEditorComponent, } from 'ag-grid-angular';
import { ICellEditorParams, RefreshCellsParams } from 'ag-grid-community';
import { ConfirmationService, MessageService } from 'primeng/api';
import { AuthService } from '../../../service/auth';
import { RacesService } from '../../../service/races';
import { Race } from 'src/app/domain/race';

@Component({
  selector: 'ijudy-app-crud-renderer',
  templateUrl: './ag-grid-crud-renderer.component.html',
  styleUrls: ['./ag-grid-crud-renderer.component.css']
})
export class AgGridCrudRendererComponent implements OnInit, AgEditorComponent {
    public paramsValues: ICellEditorParams;

    constructor( public authService: AuthService,
                 public racesService: RacesService,
                 public messageService: MessageService,
                 public confirmationService: ConfirmationService) {
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
    }

    getValue() {
    }

    public onDeleteRace($event: any) {
        const raceId = this.paramsValues.node.data.id;
        const raceName = this.paramsValues.node.data.name;

        this.confirmationService.confirm({
            message: 'Are you sure that you want to Delete the Race' + raceName + '?',
            header: 'Confirmation',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
              console.log('Deleting race id : ' + raceId);
              this.racesService.deleteRace(raceId).subscribe(
                () => {
                  const rows = this.paramsValues.api.getSelectedRows();
                  const res = this.paramsValues.api.updateRowData({ remove: rows });
                  if (res.remove) {
                    this.refreshData();
                    this.messageService.add(
                      {severity: 'success',
                      summary: 'Race',
                      detail: 'Deleted ' + raceName});
                    }
                  },
                err => {
                  console.error(err);
                  this.messageService.add(
                    {severity: 'error',
                    summary: 'Race',
                    detail: 'Not Deleted ' + raceId + ' - ' + raceName});
                }
              );
            },
            reject: () => {
              this.messageService.add(
                {severity: 'rejected',
                summary: 'Race',
                detail: 'Not Deleted ' + raceId});
            }
        });
      }

    onCloneRace($event: any) {
        const raceId = this.paramsValues.node.data.id;
        const raceName = this.paramsValues.node.data.name;
        this.confirmationService.confirm({
                message: 'Are you sure that you want to Clone Race ' + raceName + ' for next year?',
                header: 'Confirmation',
                icon: 'pi pi pi-question',
                accept: () => {
                  this.racesService.clone(raceId).subscribe(
                    (newRace: Race) => {
                        const res = this.paramsValues.api.updateRowData({ add: [newRace] });
                        if (res.add) {
                            this.refreshData();
                            this.messageService.add({
                                severity: 'success',
                                summary:  'Race',
                                detail:   'Cloned ' + raceId + ' - ' + raceName
                            });
                        }
                    },
                    err => {
                      console.error(err);
                      this.messageService.add(
                        {severity: 'error',
                        summary: 'Race',
                        detail: 'Not Cloned ' + raceId});
                    }
                  );
                },
                reject: () => {
                  this.messageService.add(
                    {severity: 'rejected',
                    summary: 'Race',
                    detail: 'Not Cloned ' + raceId});
            }
        });
    }

    refreshData() {
        const params: RefreshCellsParams = {
            force: true
        };
        this.paramsValues.api.refreshCells(params);
    }
}

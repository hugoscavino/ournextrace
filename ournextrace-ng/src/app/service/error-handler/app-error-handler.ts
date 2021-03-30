import { Injectable} from '@angular/core';
import { ErrorHandler} from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { MessageService} from 'primeng/api';
import { HttpStatusCode} from './status-codes';

@Injectable()
export class AppErrorHandler implements ErrorHandler {

  static readonly REFRESH_PAGE_ON_TOAST_CLICK_MESSAGE: string = 'An error occurred: Please click this message to refresh';
  static readonly DEFAULT_ERROR_TITLE: string = 'Something went wrong';
  static readonly DEFAULT_AUTH_TITLE: string = 'Something went wrong with your authentication';
 
  constructor(
        private messageService: MessageService,
    ) {}

    public handleError(error: any): void {
        // console.error('handleError :' + JSON.stringify(error));
        if (error instanceof HttpErrorResponse) {
            const httpErrorCode = error.status;
            // console.error('httpErrorCode :' + httpErrorCode);
            switch (httpErrorCode) {
                 case HttpStatusCode.UNAUTHORIZED:
                    this.showError(AppErrorHandler.DEFAULT_AUTH_TITLE);
                    break;
                 case HttpStatusCode.FORBIDDEN:
                    this.showError(AppErrorHandler.DEFAULT_AUTH_TITLE);
                    break;
                case HttpStatusCode.BAD_REQUEST:
                    this.showError(error.message);
                    break;
                case HttpStatusCode.CONFLICT:
                    this.showDuplicateError(error.message);
                    break;
                default:
                    this.showError(AppErrorHandler.DEFAULT_ERROR_TITLE);
            }
        } else {
            this.showError('General Error');
        }
    }

    private showError(message: string) {
        this.messageService.add({severity: 'error', summary: 'Event Message',  detail: message});
    }

    private showDuplicateError(message: string) {
        this.messageService.add({severity: 'error', summary: 'MyEvent Error', detail: message});
    }
}

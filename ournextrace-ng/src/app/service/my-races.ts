import {Injectable} from '@angular/core';
import { HttpClient, HttpParams, HttpErrorResponse } from '@angular/common/http';
import {  MyRace, RaceStatus, IRace, IMyRace } from '../domain/race';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import {DatePipe} from '@angular/common';
import { HttpStatusCode } from './error-handler/status-codes';
import * as moment from 'moment';


const CACHE_SIZE = 1;

@Injectable()
export class MyRacesService {

    constructor(private datePipe: DatePipe,
                private http: HttpClient) {
    }


    /**
     * Get all my races
     *
     * @param beginDate Optional Date must be in ISO yyyy-MM-DD format
     * @param endDate Optional Date must be in ISO yyyy-MM-DD format
     */
    public getMyRaces(beginDate?: string, endDate?: string): Observable<MyRace[]> {
        const url = '/api/v2/myRaces';

        // Initialize Params Object
        let params = new HttpParams();

        // Begin assigning parameters
        if (beginDate && endDate) {
            params = params.append('beginDate', beginDate);
            params = params.append('endDate', endDate);
        }
        return this.http.get<MyRace[]>(url, {params: params}).pipe(
            map(myRaces => {
                    myRaces.map(
                        (oneMyRace: MyRace) => {
                            if (oneMyRace.race && oneMyRace.race.date) {
                                const dateStr = this.datePipe.transform(oneMyRace.race.date, 'EEEE MMMM d y');
                                oneMyRace.race.raceDateDesc = dateStr;
                                const momentDateObj = moment(oneMyRace.race.date);
                                oneMyRace.race.date = momentDateObj.toDate();
                            }
                        }
                    );
                    return myRaces;
                }
            )
          );
    }

    /**
     * Save My Race
     * @param myRace
     */
    public saveMyRace(myRace: IMyRace): Observable<IMyRace> {
        const url = '/api/v2/myRace';
        return this.http.post<IMyRace>(url, myRace);
    }
    
    public likeRace(myRace: IMyRace): Observable<IMyRace>  {
        // console.log('Updating : ' + JSON.stringify(raceView));
        const url = '/api/v2/myRace';
        return this.http.post<IMyRace>(url, myRace);
    }

    /**
     * Update Race Status for My Race
     * @param myRace
     */
    public updateRaceStatus(raceId: number, raceStatus: RaceStatus): Observable<IMyRace> {
        const url = '/api/v2/race-status';
        const race: IRace = {id: raceId}; // Part of the PK of UserId and RaceId
        const myRace:  IMyRace = {race: race, myRaceStatus: raceStatus};
        return this.http.patch<IMyRace>(url, myRace);
    }

    /**
     * Delete one MyRace
     * @param raceView
     */
    public deleteMyRace(raceId: number): Observable<IMyRace> {
        // console.log('Deleting : ' + raceId);
        const url = '/api/v2/myRace/' + raceId;
        return this.http.delete<IMyRace>(url);
    }

    /**
     * Update one MyRace
     * @param raceView
     */
    public updateMyRace(raceView: MyRace): Observable<MyRace>  {
        // console.log('Updating : ' + JSON.stringify(raceView));
        const url = '/api/myRace';
        return this.http.put<MyRace>(url, raceView);
    }



    /**
     * Get One MyRace given the Primary Key
     *
     * @param raceView
     */
    public getMyRace(raceId: number): Observable<MyRace>  {
        const url = '/api/v2/myRace/' + raceId;
        return this.http.get<MyRace>(url);
    }

    public async getMyRaceCheck(raceId: number): Promise<boolean> {
        const url = '/api/v2/myRace/' + raceId;
        const found =  await this.http.get<MyRace>(url).toPromise().then(
            (myRace: IMyRace) => {
                // console.log('Found My Race : ' + myRace.race.name);
                return true;
            },
            (error: any) => {
                if (error instanceof HttpErrorResponse) {
                  const httpErrorCode = error.status;
                  switch (httpErrorCode) {
                      case HttpStatusCode.NOT_FOUND:
                          console.log('Not Found My Race .. which is OK : ' + raceId);
                          return false;
                      default:
                        return false;
                  }
                }
            }
        );

        return found;
    }

     /**
     * Get One MyRace given email
     * @param myRace
     */
    public getJustMyRaces(): Observable<MyRace[]>  {
        const url = '/api/justmyraces';
        return this.http.get<MyRace[]>(url);
    }
}

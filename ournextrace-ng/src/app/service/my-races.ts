import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpParams} from '@angular/common/http';
import {IMyRace, IRace, MyRace, RaceStatus} from '../domain/race';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {DatePipe} from '@angular/common';
import * as moment from 'moment';


const CACHE_SIZE = 1;
const myRacesRootUrl = '/api/v2/myRaces';
const myRaceRootUrl  = '/api/v2/myRace';

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
        

        // Initialize Params Object
        let params = new HttpParams();

        // Begin assigning parameters
        if (beginDate && endDate) {
            params = params.append('beginDate', beginDate);
            params = params.append('endDate', endDate);
        }
        return this.http.get<MyRace[]>(myRacesRootUrl, {params: params}).pipe(
            map(myRaces => {
                    myRaces.map(
                        (oneMyRace: MyRace) => {
                            if (oneMyRace.race && oneMyRace.race.date) {
                                oneMyRace.race.raceDateDesc = this.datePipe.transform(oneMyRace.race.date, 'EEEE MMMM d y');
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
    public likeUnLikeRace(myRace: IMyRace): Observable<IMyRace>  {
        // console.log('Updating : ' + JSON.stringify(raceView));
        return this.http.post<IMyRace>(myRaceRootUrl, myRace);
    }
}

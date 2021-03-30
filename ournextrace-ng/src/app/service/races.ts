import {Injectable} from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Race, RaceType} from '../domain/race';
import { Observable } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';
import { DatePipe} from '@angular/common';
import { Address, IAddress } from '../domain/address';

const CACHE_SIZE = 1;

@Injectable()
export class RacesService {

    private cache$: Observable<RaceType[]>;

    constructor( private datePipe: DatePipe,
                 private http: HttpClient) {
        // no op
    }

    public getAllRaceTypes(): Observable<RaceType[]> {
        if ( !this.cache$ ) {
          this.cache$ = this.requestApi().pipe( shareReplay(CACHE_SIZE) );
        }
        return this.cache$;
      }

      private requestApi() {
        const url = '/api/v2/race-types';
        return this.http.get<RaceType[]>(url);
      }

      public resetCache() {
        this.cache$ = null;
      }

    /**
     * Insert One Race
     * @param event
     */
    public saveRace(race: Race): Observable<Race> {
        // console.log('Inserting : ' + JSON.stringify(race));
        const url = '/api/v2/race';
        return this.http.post<Race>(url, race);
    }

    /**
     * Insert One Race
     * @param event
     */
    public clone(raceId: number): Observable<Race> {
        const url = '/api/v2/clone/';
        const bodyValue: any = {
                        'raceId': raceId
                     };
        return this.http.post<any>(url, bodyValue);
    }

    /**
     * Update one event
     * @param event
     */
    public updateRace(race: Race): Observable<Race> {
        const url = '/api/v2/race';
        return this.http.patch<Race>(url, race);
    }

    /**
     * Update Address for on Race
     * @param event
     */
    public updateRaceAddress(race: Race): Observable<Race> {
        const url = '/api/v2/race-location';
        return this.http.patch<Race>(url, race);
    }

    /**
     * Get RaceView
     * @param raceId
     */
    public getRace(raceId: number): Observable<Race> {
        const url = '/api/v2/race/' + raceId ;
        return this.http.get<Race>(url);
    }

    public deleteRace(raceId: number): Observable<Race> {
        const url = '/api/v2/race/' + raceId ;
        return this.http.delete<Race>(url);
    }

    /**
     * ADMIN Function to manage all the events in repo
     *
     */
    public getAllRaces(): Observable<Race[]> {
        const url = '/api/v2/races';
        return this.http.get<Race[]>(url).pipe(
            map(data => {
                    data.map(
                        race => {
                            if (race.date) {
                                const dateStr = this.datePipe.transform(race.date, 'EEEE MMMM d y');
                                race.raceDateDesc = dateStr;
                                const dateObj = new Date(race.date);
                                race.date = dateObj;
                            }
                        }
                    );
                    return data;
                }
            )
          );
    }

    public getAllRacesForManager(): Observable<Race[]> {
        const url = '/api/v2/races';
        return this.http.get<Race[]>(url).pipe(
            map(races => {
                races.map(
                        (race: Race) => {
                            const address: IAddress = race.address;
                            if (address) {
                                race.addressAsSelectItem = Address.toSelectItem(address);
                            }

                            const raceTypes: RaceType[] = race.raceTypes;
                            if (raceTypes) {
                                race.raceTypesAsSelectItems = RaceType.toSelectItems(raceTypes);
                            }
                        }
                    );
                    return races;
                }
            )
          );
    }

}

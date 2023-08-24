import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Race, RaceType} from '../domain/race';
import {Observable} from 'rxjs';
import {shareReplay} from 'rxjs/operators';

const CACHE_SIZE = 1;

@Injectable()
export class RacesService {

    private cache$: Observable<RaceType[]>;

    constructor( private http: HttpClient) {
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

    /**
     * Update Address for on Race
     * @param race
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

}

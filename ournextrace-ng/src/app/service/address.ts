import {Injectable} from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Address} from '../domain/address';
import { Race} from '../domain/race';
import { Observable } from 'rxjs';
import { shareReplay } from 'rxjs/operators';

@Injectable()
export class AddressService {

    readonly CACHE_SIZE = 1;

    private cache$: Observable<Address[]>;

    constructor(private http: HttpClient) {}

    /**
     * Get all the Addresses
     */
    public getAddresses(): Observable<Address[]> {
        const url = '/api/v2/addresses';
        return this.http.get<Address[]>(url);
    }

    /**
     * Get the cached list of locations
     */
    public getAddressesCache(fresh?: boolean ): Observable<Address[]> {

        if (fresh) {
            this.resetCache();
        }

        if ( !this.cache$ ) {
          this.cache$ = this.requestApi().pipe( shareReplay(this.CACHE_SIZE) );
        }

        return this.cache$;
    }

    private requestApi() {
       const url = '/api/v2/addresses';
      return this.http.get<Address[]>(url);
    }

    public resetCache() {
        this.cache$ = null;
    }
    /**
     * Get all the Addresses
     */
    public getAddress(addressId: number): Observable<Address> {
        const url = '/api/v2/address/' + addressId;
        return this.http.get<Address>(url);
    }

    /**
     * We are updating the Address Id in on Race Entity
     */
    public updateRaceLocation(raceId: number, addressId: number): Observable<Race> {
        const url = '/api/v2/race/' + raceId + '/' + addressId;
        // There is no content body we are just updating the Address Id in on Race Entity
        return this.http.put<Race>(url, '');
    }

    /**
     * Insert Address
     */
    public saveAddress(address: Address): Observable<Address> {
        const url = '/api/v2/address';
        return this.http.post<Address>(url, address);
    }

    /**
     * Delete Address
     */
    public deleteAddress(addressId: number): Observable<Address> {
        const url = '/api/v2/address' + '/' + addressId;
        return this.http.delete<Address>(url);
    }
}

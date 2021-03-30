import { Injectable} from '@angular/core';
import { HttpClient} from '@angular/common/http';
import { Observable } from 'rxjs';
import { SelectItem } from 'primeng/api/selectitem';
import { environment} from '../../environments/environment';

export class Env {
  env: string;
}

@Injectable()
export class MetadataService {

  constructor(private httpClient: HttpClient) {}
  /**
   * Load the current Environment
   */
  public getEnvironment(): Observable<Env>  {
      const url = '/api/v2/env';
      return this.httpClient.get<Env>(url);
  }

  public getDocument(filename: string ): Observable<string> {
    return this.httpClient.get('assets/docs/' + filename, { responseType: 'text' });
  }

      /**
     * Get all the states
     */
  public getStates(): Observable<SelectItem[]> {
      return this.httpClient.get<SelectItem[]>('assets/data/' + environment.statesFilename);
  }

  /**
   * Get all the world countries
   */
  public getCountries(): Observable<SelectItem[]> {
      return this.httpClient.get<SelectItem[]>('assets/data/' + environment.countriesFilename);
  }
}

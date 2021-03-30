import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable} from 'rxjs';
import { User} from '../domain/user';
import { ContactUs} from '../domain/contact-us';
import { Store } from '@ngrx/store';
import { RaceStore } from '../ngrx/race.app.state';
import { map, share } from 'rxjs/operators';
import { SetUser, LogoutUser } from '../ngrx/race.actions';

@Injectable()
export class AuthService {

  constructor(public store: Store<RaceStore>,
              public httpClient: HttpClient
    ) {
      // no op
    }

 /**
  * Get One user for an ADMIN account
  *
  * @param userId
  */
  public getOneUser(userId: number): Observable<User> {
    const url = '/api/v2/user/' + userId;
    return this.httpClient.get<User>(url).pipe(
      share()
     );
    }

  public getUser(): Observable<User> {
    const url = '/api/v2/principal';
    const obsUser: Observable<User> = this.httpClient.get<User>(url);
    return obsUser;
  }

  public async getUserAsync(): Promise<User> {
    const url = '/api/v2/principal';
    return await this.httpClient.get<User>(url).toPromise();

  }

  /**
   * login user using application credentials
   * @param myEvent
   */
  public login(email: string, password: string): Observable<User>  {
      const url = '/api/v2/login';
      const formData = new FormData();
      formData.append('email', email);
      formData.append('password', password);

      const obsUser: Observable<User> = this.httpClient.post<User>(url, formData).pipe(
        map (
          (user) => {
              console.log('User logged in : ' + user.email);
              this.store.dispatch(SetUser({user: user}));
              return user;
            }
          )
        );
      return obsUser;
    }

    /**
     * Does user already exist?
     */
    public userExists(email: string): Observable<boolean>  {
      if (email) {
        const url = '/api/v2/user-exists?email=' + email;
        return this.httpClient.get<boolean>(url);
      }
    }

    /**
     * registration of user using application credentials
     * @param myEvent
     */
    public registration(user: User): Observable<User> {
      const url = '/api/v2/registration';
      return this.httpClient.post<User>(url, user);

    }

        /**
     * registration of user using application credentials
     * @param myEvent
     */
    public resetPassword(email: string, password: string, confirmPassword: string, token: string) {
      const url = '/api/v2/reset';
      const user: User = {
        email: email,
        password: password,
        confirmPassword: confirmPassword,
        token: token,
        user: true
      };

      return this.httpClient.post(url, user);

    }

    public contactUs(contactUs: ContactUs) {
      const url = '/api/v2/contact';
      return this.httpClient.post(url, contactUs);
    }

    /**
     * Forgot Password
     * @param myEvent
     */
    public forgotPassword(email: string) {
      const url = '/api/v2/forgot';
      const user: User = {
        email: email,
        user: true
      };
      return this.httpClient.post(url, user);
    }

     /**
      *  Uses http.patch() to update user
      *
      */
     public updateUser(user: User): Observable<User> {
        const url = '/api/v2/user';
        // console.log('Attempting to update : ' + JSON.stringify(user));
        const result = this.httpClient.patch<User>(url, user);
        return result;
      }

     /**
      *  Uses http.patch() to update user
      *
      */
     public getAuthorizedUsers(): Observable<User[]> {
      const url = '/api/v2/users';
      const results = this.httpClient.get<User[]>(url);
      return results;
    }

    /**
     * logout from session
     */
    public logout(): Observable<any> {
      this.store.dispatch(LogoutUser());
      const url = '/api/v2/logout';
      const headers = new HttpHeaders({
        'Content-Type': 'application/text'
      });
      return this.httpClient.get(url, {headers: headers, responseType: 'text'});
    }

 }

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) { }

  getAuthStatus(): Observable<any> {
    return this.http.get('http://localhost:8080/WorkHub/access');
  }
}

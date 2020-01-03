import {Injectable} from '@angular/core';

import {Router} from '@angular/router';
import {FormBuilder} from '@angular/forms';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ApplicationsService {


  constructor(private client: HttpClient, private router: Router, private builder: FormBuilder) {
  }

  submit(id: any) {
    return this.client.get(`http://localhost:8080/WorkHub/companies/accepting-member/` + id, {responseType: 'text'}).subscribe(res => {
    });
  }

  reject(id: any) {

    return this.client.get(`http://localhost:8080/WorkHub/companies/rejection-member/` + id, {responseType: 'text'}).subscribe(res => {
    });
  }

  allForCompany(page) {
    return this.client.get(`http://localhost:8080/WorkHub/companies/applications/` + page);
  }
}

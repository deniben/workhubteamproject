import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Company } from '../models/company.model';
import { Project } from '../models/project.model';
import { Router } from '@angular/router';
import { FormBuilder, Validators } from '@angular/forms';
import { URL } from '../../constants';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class CompanyService {

  companyType: string;
  companyURL: string;

  constructor(private client: HttpClient, private router: Router, private builder: FormBuilder) {
    this.companyURL = URL + '/companies';
  }

  public createCompany(company: Company) {
    console.log('[create company]');
    const body = {
      name: company.name,
      description: company.description,
      type: company.type,
      skills: company.skills,
      photoUrl: company.photoUrl,
      blocked: company.blocked
    };
    return this.client.post(this.companyURL, body);
  }

  public findCompanyById(id: Number) {
    console.log('[find company by id: ' + id + ']');
    return this.client.get<Company>(this.companyURL + '/' + id);
  }

  public findCompanyByIdForUpdate(id: Number) {
    console.log('[find company by id: ' + id + ']');
    return this.client.get<Company>(this.companyURL + "/" + id + "/check-owner");
  }

  public findMyCompany() {
    console.log("[find current loggined user company]");
    return this.client.get<Company>(this.companyURL + "/my");
  }

  public findAll() {
    console.log('[find all companies]');
    return this.client.get<Company[]>(this.companyURL);
  }

  public findAllForAdmin(page: number) {
    console.log('[find all admin companies]');
    console.log(page);
    return this.client.get<Company[]>(this.companyURL + '/allComp/' + page);
  }


  public adminUpdateComp(id, company) {
    console.log(id);
    console.log(company);
    return this.client.put(this.companyURL + '/' + id, company);
  }

  public findAllBlocked(page) {
    return this.client.get<Company[]>('http://localhost:8080/WorkHub/companies/admin/blockedCompanies/'+ page);
  }

  public blockCompany(id: any) {

    return this.client.get('http://localhost:8080/WorkHub/companies/admin/block/unblock-company/' + id + '/' + true, { responseType: 'text' });
  }

  public unblockCompany(id: any) {
    return this.client.get('http://localhost:8080/WorkHub/companies/admin/block/unblock-company/' + id + '/' + false, { responseType: 'text' });
  }

  public TopOfCompanyEmployer() {
    console.log('[sort companies]');
    return this.client.get<Company[]>(this.companyURL + '/top-employer');

  }

  public TopOfCompanyEmployee() {
    console.log('[sort companies]');
    return this.client.get<Company[]>(this.companyURL + '/top-employee');

  }

  public findAllProject() {
    console.log('[project]');
    return this.client.get<Project[]>(URL + '/project');
  }

  findProfilesByCompany(id: any) {
    return this.client.get('http://localhost:8080/WorkHub/profile/profiles-by_company/{id}' + id);
  }


  updateCompany(company: Company) {
    console.log('in updateCompany()');
    return this.client.put<Company>(this.companyURL, company);
  }
}

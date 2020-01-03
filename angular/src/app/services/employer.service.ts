import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Company } from "../models/company.model";
import { Project } from "../models/project.model";
import { Router } from "@angular/router";
import { FormBuilder, Validators } from "@angular/forms";
import { Profile } from "selenium-webdriver/firefox";
import { Observable } from 'rxjs';

@Injectable({
  providedIn: "root"
})
export class EmployerService {
  constructor(
    private client: HttpClient,
    private router: Router,
    private builder: FormBuilder
  ) {}

  public findByStatus(status: String, pageNumber: number) {
     
    return this.client.get<any>(
      "http://localhost:8080/WorkHub/employer/find-by-status/" +
      status +
      "/page/" +
      pageNumber 
    
    ); 
  }

  public findProjectById(id: number) {
    return this.client.get<Project>(
      "http://localhost:8080/WorkHub/employer/project/" + id
    );
  }

  public findAllCompanysForMyProject(id: number, page: number) {
    return this.client.get<any>(
      "http://localhost:8080/WorkHub/employer/companies-for-my-project/" +
      id +
      "/page/" +
      page
    );
  }

  public findProfilesByCompany(page: number) {
    return this.client.get<any>(
      "http://localhost:8080/WorkHub/employer/profiles/page/" + page
    );
  }

  public acceptCompanyToProject(companyId: number, projectId: number) {
    return this.client.put(
      "http://localhost:8080/WorkHub/employer/accept-company/" +
      companyId +
      "/to-project/" +
      projectId,
      { responseType: "text" }
    );
  }

  public rejectCompany(companyId: number, projectId: number) {
    return this.client.delete(
      "http://localhost:8080/WorkHub/employer/reject-company/" +
      companyId +
      "/from-project/" +
      projectId,
      { responseType: "text" }
    );
  }

  public finishProject(projectId: number) {
    return this.client.put(
      "http://localhost:8080/WorkHub/employer/finish-project/" +
      projectId,
      {},
      { responseType: "text" }
    );
  }

  public completeProject(projectId: number, rate: number, status : String) {
    let options: Object = { responseType: "text" };
    return this.client.post<any>(
      "http://localhost:8080/WorkHub/rates/projectId/" + projectId +"/rate/"+rate +"/status/" + status ,{},options
    );
  }

  public getPercentOfMatchedSkills(projectId: number, companyId : number){
    return this.client.get<number>(
      "http://localhost:8080/WorkHub/employer/get-percent-of-matched-skills/projectId/" + projectId +"/companyId/"+companyId);
  }
}

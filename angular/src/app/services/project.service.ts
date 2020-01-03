import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Company } from "../models/company.model";
import { ProjectType } from "../models/projectType.model";
import { Router } from "@angular/router";
import { FormBuilder, Validators } from '@angular/forms';
import { Project } from '../models/project.model';
import { URL } from '../../constants';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  constructor(private client: HttpClient, private router: Router, private builder: FormBuilder) { }


  public API = 'http://localhost:8080/WorkHub';
  public PROJECT_API = this.API + '/projects';

  public createProject(project: Project) {
    console.log(project.projectTypeId)
    const body = {
      name: project.name,
      description: project.description,
      projectTypeId: project.projectTypeId,
      budget: project.budget,
      expiryDate: project.expiryDate,
      skills: project.skills,
      photoUrl: project.photo
    };
    console.log('dfd')
    return this.client.post(URL + '/projects', body, {responseType: 'text'});

  }

  public createProjectType(typeName: string) {
    console.log('[create type]')
    const body = {
      name: typeName,
    };
    return this.client.post(URL + '/projectType', body);
  }

  public AllProjectType() {
    return this.client.get<ProjectType[]>(URL + '/projectType');
  }

  public getProjectsByCompany(companyId: string, companyType: string) {
    let params = new HttpParams().set("companyId", companyId).set("companyType", companyType);
    console.log(params)
    return this.client.get<Project[]>(URL + '/projects', { params });
  }

  public getProjectById(id: string) {
    console.log('[getProjectById]');
    return this.client.get<Project>(this.API + '/projects/' + id);
  }

  updateProject(project): Observable<Project> {
    console.log('in updateProject() Service');
    console.log(project);
    return this.client.put<Project>(this.API + '/projects/' + project.id, project);
  }

}

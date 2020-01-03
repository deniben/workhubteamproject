import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Company} from '../models/company.model';
import {Project} from '../models/project.model';
import {Skill} from '../models/skill.model';
import {Router} from '@angular/router';
import {FormBuilder, Validators} from '@angular/forms';
import {ProjectType} from '../models/projectType.model';

​
​
​
@Injectable({
  providedIn: 'root'
})
export class AdminService {
​
  companyType: string;
​

  constructor(private client: HttpClient, private router: Router, private builder: FormBuilder) {
  }

​
​

  public getAllProjectByCompany(id: any) {

    return this.client.get<Project[]>('http://localhost:8080/WorkHub/admin/projects/' + id);
​
  }

​
​
​

  public change_status(id, status) {
    console.log(id);
    let options: Object = {responseType: 'text'};

    return this.client.post<any>('http://localhost:8080/WorkHub/admin/projects/status?id=' + id + '&status=' + status, {}, options);
  }

​

  public change_typeInProject(id, projectType) {
    console.log(id);
    let options: Object = {responseType: 'text'};

    return this.client.post<any>('http://localhost:8080/WorkHub/admin/projects/projectType?id=' + id + '&projectType=' + projectType, {}, options);
  }

​

  public changetype(type: ProjectType) {
  ​

    let options: Object = {responseType: 'json'};

    return this.client.post<ProjectType>('http://localhost:8080/WorkHub/admin/projects/upProjectType?id=' + type.id + '&name=' + type.name, {}, options);
  }

​

  public changeskill(type: Skill) {
  ​

    let options: Object = {responseType: 'json'};

    return this.client.post<Skill>('http://localhost:8080/WorkHub/admin/projects/upSkill?id=' + type.id + '&name=' + type.name, {}, options);
  }

​

  public findAllStatus() {
  ​

    return this.client.get<any>('http://localhost:8080/WorkHub/admin/projects/status');
​
  }

​

  public findAllProjectType() {
  ​

    return this.client.get<any>('http://localhost:8080/WorkHub/projectType');
​
  }

​

  public findAllProjectTypes() {
  ​

    return this.client.get<any>('http://localhost:8080/WorkHub/admin/projects/projectType');
​
  }

​

  public findProjectTypeById(id) {
  ​
    console.log(id);
    return this.client.get<any>('http://localhost:8080/WorkHub/admin/projects/projectType/' + id);
​
  }

​

  public findSkillById(id) {
  ​
    console.log(id);
    return this.client.get<any>('http://localhost:8080/WorkHub/admin/projects/skill/' + id);
​
  }

​

  public findProjectById(id) {
  ​

    return this.client.get<any>('http://localhost:8080/WorkHub/admin/projects/id=' + id);
​
  }

​

  public deleteSkill(id) {
  ​
    console.log(id);
    return this.client.get<any>('http://localhost:8080/WorkHub/admin/projects/skillD/' + id);
​
  }

​

  public deleteType(id) {
  ​
    console.log(id);
    return this.client.put('http://localhost:8080/WorkHub/admin/projects/typeD/' + id, {}, {responseType: 'text'});
​
  }

​

  public unBlockedType(id) {
  ​
    console.log(id);
    return this.client.get('http://localhost:8080/WorkHub/admin/projects/typeD/' + id, {responseType: 'text'});
​
  }

​

  public createProjectType(typeName: string) {
    console.log('[create type]');
    const body = {
      name: typeName,
    };
    return this.client.post('http://localhost:8080/WorkHub/projectType', body, {responseType: 'text'});
  }

​

  public createSkill(skillName: string) {
    console.log('[create skill]');
    const body = {
      name: skillName
    };
    return this.client.post('http://localhost:8080/WorkHub/skills', body, {responseType: 'text'});
  }

​
​
​

  public findAllTypewithPagination(current_page) {
  ​

    return this.client.get<any>('http://localhost:8080/WorkHub/admin/projects/projectType?page=' + (Number(current_page) - 1));
​
  }

​

  public findTypeByName(current_page, filter_param) {
  ​

    return this.client.get<any>('http://localhost:8080/WorkHub/admin/projects/projectType?page=' + (Number(current_page) - 1) + '&name=' + filter_param);
​
  }

​

  public findAllSkillwithPagination(current_page) {
  ​

    return this.client.get<any>('http://localhost:8080/WorkHub/admin/projects/skills?page=' + (Number(current_page) - 1));
​
  }

​

  public findSkillByName(current_page, filter_param) {
  ​

    return this.client.get<any>('http://localhost:8080/WorkHub/admin/projects/skills?page=' + (Number(current_page) - 1) + '&name=' + filter_param);
​
  }

​

  public getAllProjectByCompanyWithPag(id: any, current_page) {

    return this.client.get<any>('http://localhost:8080/WorkHub/admin/projects?id=' + id + '&page=' + (Number(current_page) - 1));
​
  }

​

  public findProjectByName(id: any, current_page, filter_param) {

    return this.client.get<any>('http://localhost:8080/WorkHub/admin/projects?id=' + id + '&page=' + (Number(current_page) - 1) + '&name=' + filter_param);
​
  }

​
};

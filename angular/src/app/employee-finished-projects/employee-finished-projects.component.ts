import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { DomSanitizer, SafeResourceUrl, SafeUrl} from '@angular/platform-browser';
import { environment } from 'src/environments/environment';
import {Location} from '@angular/common';
import { Project } from '../models/project.model';
import {EmployerService} from '../services/employer.service'
import { ToastrService } from "ngx-toastr";

@Component({
  selector: 'app-employee-finished-projects',
  templateUrl: './employee-finished-projects.component.html',
  styleUrls: ['./employee-finished-projects.component.css']
})
export class EmployeeFinishedProjectsComponent implements OnInit {

  projects;
  type;
  type_segment;
  projectId : number;
  rate : number = 1;
  number_of_pages;
  page_number;
  page_size;
flag : number;
  constructor(private activatedRoute : ActivatedRoute, private client : HttpClient, private sanitizer : DomSanitizer,
              private router : Router, private location : Location, private employerService: EmployerService,  private toastr: ToastrService) { }

  ngOnInit() {

    this.projects = new Array<Project>();
    this.page_number = 1;
    this.page_size = 10;
    this.number_of_pages = 1;
    this.type = 0;

    this.activatedRoute.paramMap.subscribe(data => {

      this.activatedRoute.queryParams.subscribe(data => {
        if(data['page']) {
          this.page_number = data['page'];
        }
      });

      if (data.get('type') && data.get('type') === 'completed') {

        this.type_segment = '/completed';
        this.getProjects(this.type_segment);
        this.type = 1;
        return;

      } else if (data.get('type') && data.get('type') === 'failed') {

        this.type_segment = '/failed';
        this.getProjects(this.type_segment);
        this.type = 2;
        return;

      }

      this.getProjects(this.type_segment);

    });

  }

  change_page(page) {
    this.page_number = page;
    let uri = '/employee/my-projects/finished';

    if(this.type_segment) {
      uri += this.type_segment;
    }

    this.location.go(uri + '?page=' + this.page_number);
    this.getProjects(this.type_segment);
  }

  getProjects(type) {

    let url = environment.api_url + '/employee/projects/finished';

    if(type) {
      url += type;
    }

    this.client.get<any>(url + "/" + (Number(this.page_number) - 1)).subscribe(data => {

      this.number_of_pages = data.pagesCount;

      this.projects = [];
      console.log("all projects = " + data.items[0].flagForRate);

      for (let project of data.items) {

        let img = this.sanitizer.bypassSecurityTrustUrl(project.photo);

        let rate = [];

        for (let i = 0; i < (project.employeeMark); i++) {
          rate.push(1); 
        }

        this.projects.push({
            id : project.id,
            name : project.name,
            skills : project.skillSet,
            status : project.projectStatus,
            budget : project.budget,
            company : project.companyCreator,
            photo : img,
            mark : project.employeeMark,
            rate : rate,
            flagForRate : project.flagForRate
        });
        

      }

    },
      err => console.log(err.error.message));
  }


  setId(id : number){
    this.projectId = id;
  }

  completeProject() {
this.projects[this.flag].flagForRate = 0;
    this.employerService
      .completeProject(this.projectId, this.rate, 'COMPLETED')
      .subscribe(data => {
        this.showInfo("Rate","Company's rate is update");
      });
  }

  setRate(rate: number) {
    this.rate = rate;
  }


  showInfo( first : string , last : string) {
    this.toastr.info(last,first,
      { progressBar: true, timeOut: 5000 }
    );
  }

  setFlag(i: number){
    this.flag = i;
  }
}

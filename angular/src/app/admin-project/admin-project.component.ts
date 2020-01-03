import { Component, OnInit } from '@angular/core';
import { AdminService } from '../services/admin-project.sercive';
import { DomSanitizer, SafeResourceUrl, SafeUrl} from '@angular/platform-browser';
import { ProjectService } from '../services/project.service';
import {SuiModalService, TemplateModalConfig, ModalTemplate} from 'ng2-semantic-ui';
import { ViewChild, ElementRef } from '@angular/core'
import { Router, ActivatedRoute  } from '@angular/router';
import {Location} from '@angular/common';


export interface IContext {
  data:string;
}

@Component({
  selector: 'app-admin-project',
  templateUrl: './admin-project.component.html',
  styleUrls: ['./admin-project.component.css']
})
export class AdminProjectComponent implements OnInit {
 
 // @ts-ignore
  @ViewChild('modalTemplate', 'modalTemplate')

  public modalTemplate:ModalTemplate<IContext, string, string>
 
  @ViewChild("filter_field", {static : true}) filter_input : ElementRef;
  
  projectByCompany = [];
  project = [];
  projectType = [];
  skills = [];
  status = String;
  id: number;
  idCompany: number;
  id_project: number;
  filter_param;
  current_page;
  pages_count;
  page_size;
  projects;
  pageNumber: number = 1;

  index: boolean = false;
  pageSize: number = 6;

  public open(id) {

    this.id_project = id;

    const config = new TemplateModalConfig<IContext, string, string>(this.modalTemplate);
    config.closeResult = "closed!";

    this.adminService.findProjectById(id).subscribe(data => { this.project = data })
    console.log(this.project)

    this.modalService
      .open(config)
      .onApprove(result => { /* approve callback */ })
      .onDeny(result => { this.id=null });
  }
 

  constructor(private adminService: AdminService,  private sanitizer: DomSanitizer,
              private projectService: ProjectService, public modalService:SuiModalService, private route: ActivatedRoute, 
              private location : Location, private activatedRoute : ActivatedRoute, private activeRoute: ActivatedRoute ) 
              {
              
                this.page_size = 5;
               }

  ngOnInit() {
    this.adminService.findAllProjectType().subscribe( data => {this.projectType = data});
    this.adminService.findAllStatus().subscribe(data => { this.status = data; });

    this.getPage();

    this.route.params.subscribe(params => (this.idCompany = params["id"]));
  
    this.activatedRoute.paramMap.subscribe(params => {


      this.activatedRoute.queryParams.subscribe(params => {
        if(params['filter']) {
          this.filter_param = params['filter'];
          this.filter_input.nativeElement.value = this.filter_param;
          this.refresh_with_filter();
        } 
        
        else {
          this.load_projects();
        }
      });

    });

  }

checkprojectType(name)
{
  console.log(name);
  if(name === null)
  {
    return "Select type";
  }
  return name.name;

}

changestatus(id, name) 
{

  console.log(name)
   this.adminService.change_status(id, name).subscribe();

}

changetype(id, projectType) 
{
  console.log(projectType)
  if (projectType === null)
  {
    return null;
  }
  return this.adminService.change_typeInProject(id, projectType).subscribe();

}


filter(event) {
  this.filter_param = event.srcElement.value;
  this.refresh_with_filter();
}


refresh_with_filter() {

this.adminService.findProjectByName(this.idCompany, this.pageNumber, this.filter_param).subscribe(data => { this.projects = data.items });
console.log(this.projects)

}


load_projects() {
this.adminService.getAllProjectByCompanyWithPag(this.idCompany, this.pageNumber).subscribe(data => {

  if(data.items) {
    
  this.projects = data.items;
   
  }

  if(data.pagesCount) {
    this.pages_count = data.pagesCount;
    console.log(this.pages_count)
  }
  console.log(data);

})
}

change_page(number) {
this.pageNumber = number;

let url = '/admin/project/' + this.idCompany + "?page=" + this.pageNumber;

if(this.filter_param) {
  this.refresh_with_filter();
  url += '?filter=' + this.filter_param;
} else {
  this.load_projects();
}
this.location.go(url);
}

getPage() {
  this.activeRoute.queryParams.subscribe(params => {
    if (params["page"] && this.index === false) {
      if (params["page"] < 1 ) {
        console.log(" < 1");
        this.pageNumber = 1;
        this.location.go("/admin/project/" + this.idCompany + "?page=" + this.pageNumber);
      } 
        console.log("get page");
        this.pageNumber = <any>params["page"];
    }
  });
}

}

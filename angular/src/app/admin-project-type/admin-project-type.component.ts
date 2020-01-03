import { Component, OnInit } from '@angular/core';
import { AdminService } from '../services/admin-project.sercive';
import { SuiModalService, TemplateModalConfig, ModalTemplate } from 'ng2-semantic-ui';
import { ViewChild, ElementRef } from '@angular/core'
import { FormBuilder, Validators, FormGroup, FormControl  } from '@angular/forms';
import { ProjectType } from '../models/projectType.model';
import { Router, ActivatedRoute } from "@angular/router";
import { ProjectService } from '../services/project.service';
import {Location} from '@angular/common';

export interface IContext {
  // data: string;
  // id: string;
  
}


@Component({
  selector: 'app-project-type',
  templateUrl: './admin-project-type.component.html',
  styleUrls: ['./admin-project-type.component.css']
})


export class AdminProjectTypeComponent implements OnInit {
  // @ts-ignore
  @ViewChild('modalTemplate', 'modalTemplate')
  

  public modalTemplate:ModalTemplate<IContext, string, string>


  @ViewChild("filter_field", {static : true}) filter_input : ElementRef;
  
  projectType = [];
  projectTypeid: any;
  updateTypeForm: FormGroup;
  CreateTypeForm: FormGroup;
  type: ProjectType = new ProjectType;
  submitted: boolean;
  showMsg: boolean = false;
  showErrMsg: boolean = false;
  alreadyApplied;
  message;
  filter_param;
  skill: any;

  current_page;
  pages_count;
  page_size;
  types;

  public open(id) {
   
    const config = new TemplateModalConfig<IContext, string, string>(this.modalTemplate);
    config.closeResult = "closed!";
  
    this.adminService.findProjectTypeById(id).subscribe ( data => 
      {this.type = data
        console.log(data);
        console.log(this.type)
      })
      
    
    this.modalService
        .open(config)
        .onApprove(result => { /* approve callback */ })
        .onDeny(result => { /* deny callback */});
}



  constructor(private router: Router, private activatedRoute : ActivatedRoute, private adminService: AdminService, private projectService: ProjectService, 
              public modalService: SuiModalService, private formBuilder: FormBuilder, private location : Location) {
    this.current_page = 1;
    this.pages_count = 1;
    this.page_size = 9;

    this.submitted = false;
    this.updateTypeForm = this.formBuilder.group({

      name: ['', Validators.compose([Validators.required, Validators.minLength(1),
        Validators.maxLength(30)])],
   });

   this.CreateTypeForm = this.formBuilder.group({

    name: ['', Validators.compose([Validators.required, Validators.minLength(1),
      Validators.maxLength(30)])],
 });
  }

  ngOnInit() {

   // this.findAllByProjectType();
    this.alreadyApplied = false;

    this.activatedRoute.paramMap.subscribe(params => {

      if(params.get('page')) {
        this.current_page = params.get('page');
      }

      this.activatedRoute.queryParams.subscribe(params => {
        if(params['filter']) {
          this.filter_param = params['filter'];
          this.filter_input.nativeElement.value = this.filter_param;
          this.refresh_with_filter();
        } else {
          this.load_types();
        }
      });

    });
  };

  public updateType(projectData, id) {
    this.type.id = id;
    this.submitted = true;
    this.type.name = projectData.name;
    
    console.log(this.type.name)
      
  
    this.adminService.changetype(this.type).subscribe(
    data => {
      this.updateTypeForm.reset();
      this.showMsg = true;
      this.findAllByProjectType();
      this.router.navigate(['/admin/projects-type']);

     
    } ,
    err => {
      this.showErrMsg = true;
    } );

} 

public createType(projectData) {
  this.projectTypeid = projectData.name;


    

  this.adminService.createProjectType(this.projectTypeid).subscribe(
  data => {  
    this.alreadyApplied = false;
    this.message = '';
    this.CreateTypeForm.reset();
    this.load_types();
   
 


  } ,
  err => {
    this.message = JSON.parse(err.error).message;
    this.CreateTypeForm.reset();
  } );

} 

public deleteType(id) {
   
  this.adminService.deleteType(id).subscribe(data => {

    this.alreadyApplied = false;
    this.message = '';
    this.load_types();
  
  }
 );
}

public unBlockedType(id) {
   
  this.adminService.unBlockedType(id).subscribe(data => {

    this.alreadyApplied = false;
    this.message = '';
    this.load_types();
  
  },
  err => {
    this.message = JSON.parse(err.error).message;
  });
}

    
  findAllByProjectType() {
    
    this.adminService.findAllProjectTypes().subscribe( data => 
      {this.projectType = data
        console.log(data)
      }
    
      )
    
  }
  
  filter(event) {
    this.filter_param = event.srcElement.value;
    this.refresh_with_filter();
}

refresh_with_filter() {

  this.adminService.findTypeByName(this.current_page, this.filter_param).subscribe(data => { this.types = data.items });
  console.log(this.types)

}


load_types() {
  this.adminService.findAllTypewithPagination(this.current_page).subscribe(data => {

    if(data.items) {
      this.types = data.items;
     
    }
console.log(this.types)
    if(data.pagesCount) {
      this.pages_count = data.pagesCount;
    }

  }); 
 
}

change_page(number) {
  this.current_page = number;

  let url = '/admin/projects-type/' + this.current_page;

  if(this.filter_param) {
    this.refresh_with_filter();
    url += '?filter=' + this.filter_param;
  } else {
    this.load_types();
  }
  this.location.go(url);
}

}

import { Component, OnInit } from '@angular/core';
import { AdminService } from '../services/admin-project.sercive';
import { SkillService } from '../services/skill.service';
import { SuiModalService, TemplateModalConfig, ModalTemplate } from 'ng2-semantic-ui';
import { ViewChild, ElementRef } from '@angular/core'
import { FormBuilder, Validators, FormGroup, FormControl } from '@angular/forms';
import { Skill } from '../models/skill.model';
import { Router, ActivatedRoute } from "@angular/router";
import {Location} from '@angular/common';

export interface IContext {
  //  data: string;
  // id: string;

}


@Component({
  selector: 'app-admin-skills',
  templateUrl: './admin-skills.component.html',
  styleUrls: ['./admin-skills.component.css']
})


export class AdminSkillComponent implements OnInit {
  // @ts-ignore
  @ViewChild('modalTemplate', 'modalTemplate')
  // @ts-ignore
  @ViewChild('modalCreate', 'modalCreate')

  public modalTemplate: ModalTemplate<IContext, string, string>

  @ViewChild("filter_field", {static : true}) filter_input : ElementRef;

  skills = [];
  skill;
  updateSkillForm: FormGroup;
  CreateSkillForm: FormGroup;
  skil: Skill = new Skill;
  submitted: boolean;
  showMsg: boolean = false;
  showErrMsg: boolean = false;
  alreadyApplied;
  message;
  filter_param;
  current_page;
  pages_count;
  page_size;
  

  public open(id) {
    const config = new TemplateModalConfig<IContext, string, string>(this.modalTemplate);
    config.closeResult = "closed!";

    this.adminService.findSkillById(id).subscribe(data => { this.skill = data })
    console.log(this.skill)

    this.modalService
      .open(config)
      .onApprove(result => { /* approve callback */ })
      .onDeny(result => { /* deny callback */ });
  }

  public deleteSkill(id) {

    this.adminService.deleteSkill(id).subscribe(  
            
      );
    
    this.adminService.findAllSkillwithPagination(this.current_page).subscribe( data => 
      
    {this.skill = data
      this.load_skills();
    });
  }


  constructor(private router: Router, private adminService: AdminService, private activatedRoute : ActivatedRoute, private skillservice: SkillService, 
              public modalService: SuiModalService, private formBuilder: FormBuilder, private location : Location ) {
    this.submitted = false;

    this.current_page = 1;
    this.pages_count = 1;
    this.page_size = 9;
    this.updateSkillForm = this.formBuilder.group({

      name: ['', Validators.compose([Validators.required, Validators.minLength(1),
      Validators.maxLength(30)])],
    });

    this.CreateSkillForm = this.formBuilder.group({

      name: ['', Validators.compose([Validators.required, Validators.minLength(1),
      Validators.maxLength(30)])],
    });
  }

  ngOnInit() {

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
          this.load_skills();
        }
      });

    });


  };

  public updateSkill(projectData, id) {
    this.skil.id = id;
    this.submitted = true;
    this.skil.name = projectData.name;
    console.log(this.skil.name)


    this.adminService.changeskill(this.skil).subscribe(
      data => {
        this.updateSkillForm.reset();
        this.showMsg = true;
        this.load_skills();
        this.router.navigate(['/admin/skills']);
        console.log(this.skil);


      },
      err => {
        this.showErrMsg = true;
      });
  }

  public createSkill(projectData) {

    this.skill = projectData.name;
    this.load_skills();
    console.log(this.skill.name)


    this.adminService.createSkill(this.skill).subscribe(
      data => {

        this.alreadyApplied = false;
        this.message = '';
        this.load_skills();
        this.CreateSkillForm.reset();
        this.showMsg = true;

 
        console.log(this.skill);


      },
      err => {
        this.message = JSON.parse(err.error).message;
        this.CreateSkillForm.reset();
      } );
    
  }

  findAllSkill() {
    console.log("dfdsfd")
    this.skillservice.findAll().subscribe(data => { this.skills = data })
  }


  filter(event) {
    this.filter_param = event.srcElement.value;
    this.refresh_with_filter();
}


refresh_with_filter() {

  this.adminService.findSkillByName(this.current_page, this.filter_param).subscribe(data => { this.skill = data.items });
  console.log(this.skill)

}


load_skills() {
  this.adminService.findAllSkillwithPagination(this.current_page).subscribe(data => {

    if(data.items) {
      this.skill = data.items;
     
    }
    console.log(this.skill)
    if(data.pagesCount) {
      this.pages_count = data.pagesCount;
    }

  }); 
 
}

change_page(number) {
  this.current_page = number;

  let url = '/admin/skills/' + this.current_page;

  if(this.filter_param) {
    this.refresh_with_filter();
    url += '?filter=' + this.filter_param;
  } else {
    this.load_skills();
  }
  this.location.go(url);
}

}

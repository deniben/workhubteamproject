import {AfterViewInit, Component, Input, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ProjectService} from '../services/project.service';
import {ActivatedRoute, Router} from '@angular/router';
import {FileService} from '../services/file.service';
import {CustomValidators} from 'ng2-validation';
import {PrMultiselectComponent} from '../projecttypeselect/pt-multiselect.component';
import {SkillsMultiselectComponent} from '../skills-multiselect/skills-multiselect.component';
import {FileloadComponent} from '../fileload/fileload.component';
import {Project} from '../models/project.model';
import {PhotoTypes} from '../PhotoTypes';
import {Observable} from 'rxjs';


@Component({
  selector: 'app-project1',
  templateUrl: './project1.component.html',
  styleUrls: ['./project1.component.css']
})
export class Project1Component implements OnInit {

  projectForm: FormGroup;
  submitted: boolean;
  projectName: string;
  router: Router;
  project: Project = new Project;


  showMsg = false;
  showErrMsg = false;
  showSkills = false;
  showProjectType = false;

  projectType = [];
  skill = [];
  idUpdate = this.activatedRoute.snapshot.params.id;
  desctiption: string;

  id;
  name;
  description;
  expiryDate;
  budget;
  skills;
  selectedType = [];
  monthDate = '';
  dayDate = '';
  dateInForm;

  errorMsg;
  showErrors = false;

  dateTime = new Date();

  d = new Date();
  year1 = this.d.getFullYear();
  month = this.d.getMonth();
  day = this.d.getDate();
  c = new Date(this.year1 + 1, this.month, this.day);

  @ViewChild(PrMultiselectComponent, {static: false})
  private prMultiselectComponent: PrMultiselectComponent;

  @ViewChild(SkillsMultiselectComponent, {static: false})
  private skillMultiselectComponent: SkillsMultiselectComponent;

  @ViewChild(FileloadComponent, {static: false})
  private file: FileloadComponent;

  @Input() header: string;
  @Input() status: string;
  @Input() submit: string;


  constructor(private formBuilder: FormBuilder, private projectService: ProjectService, private activatedRoute: ActivatedRoute,
              private rout: Router, private fileService: FileService ) {


    this.submitted = false;
    this.router = rout;
    this.projectForm = this.formBuilder.group({

      projectName: ['', Validators.compose([Validators.required, Validators.minLength(5),
        Validators.maxLength(30)])],

      projectDescription: ['', Validators.compose([Validators.required, Validators.minLength(10),
        Validators.maxLength(1000)])],

      expiryDate: ['', CustomValidators.minDate(this.dateTime)],


      budget: ['', Validators.compose([Validators.required, Validators.min(0),
        Validators.max(1000000)])],

      photoUrl: ['', Validators.required]
    });
  }

  ngOnInit() {
    console.log(this.status);
    if (this.status === 'update') {
      console.log(this.status);
      this.projectService.getProjectById(this.idUpdate).subscribe((project: Project) => {
        this.fillFields(this.projectMapper(project));
        this.skillMultiselectComponent.selectedItems = this.project.skills.map(this.skillMultiselectComponent.parseSkillsListToUI);

// Show selected Type
        this.selectedType.push({
          // @ts-ignore
          id: this.project.projectType.id,
          // @ts-ignore
          itemName: this.project.projectType.name
        });
        this.prMultiselectComponent.selectedTypeItems = this.selectedType;

        // Show expiry date
        // @ts-ignore
        if (this.project.expiryDate.monthValue < 10) {
          // @ts-ignore
          this.monthDate = `0${this.project.expiryDate.monthValue}`;
        } else {
          // @ts-ignore
          this.monthDate = this.project.expiryDate.monthValue;
        }
        // @ts-ignore
        if (this.project.expiryDate.dayOfMonth < 10) {
          // @ts-ignore
          this.dayDate = `0${this.project.expiryDate.dayOfMonth}`;
        } else {
          // @ts-ignore
          this.dayDate = this.project.expiryDate.dayOfMonth;
        }
        // @ts-ignore
        this.dateInForm = `${this.project.expiryDate.year}-${this.monthDate}-${this.dayDate}`;
      }, error => { this.router.navigate(['/error-page']); });
    }
  }

  createOrEditProject(projectForm) {
    this.submitted = true;
    if (this.projectForm.invalid || this.file.invalidFile) {
      console.log('INVALID');
      return;
    }

    this.fileService.fileUpload(this.file.postFile(), PhotoTypes.PROJECT).subscribe((response: any) => {
      this.project.id = this.idUpdate;
      this.project.name = projectForm.projectName;
      this.project.description = projectForm.projectDescription;
      this.project.expiryDate = projectForm.expiryDate;
      this.project.budget = projectForm.budget;
      this.project.skills = this.skillMultiselectComponent.getSelectedItems();
      this.project.projectTypeId = this.prMultiselectComponent.getSelectedItems();
      this.project.photoUrl = response.name;
      this.project.photo = response.name;

      console.log(this.status);
      if (this.status === 'create') {
        this.projectService.createProject(this.project).subscribe(() => {
          this.router.navigate(['/employer/my-projects']);

        })
        console.log('create');
      }
      if (this.status === 'update') {
        this.projectService.updateProject(this.project).subscribe((updatedProject: Project) => {
          this.router.navigate(['/employer/my-projects/view-project/' + updatedProject.id]);

        });
        console.log('In the end update');
      }
    }, error => { this.errorMsg = error.error.message;
                  this.showErrors = true; });
  }

  projectMapper(response: Project): Project {
    this.project.id = response.id;
    this.project.name = response.name;
    this.project.description = response.description;
    this.project.budget = response.budget;
    this.project.expiryDate = response.expiryDate;
    this.project.companyCreator = response.companyCreator;
    this.project.employeeMark = response.employeeMark;
    this.project.employerMark = response.employerMark;
    this.project.projectType = response.projectType;
    this.project.projectStatus = response.projectStatus;
    this.project.projectType = response.projectType;
    // @ts-ignore
    this.project.skills = response.skillSet;
    this.project.photoUrl = response.photoUrl;
    this.file.imgURL = response.photo;
    return this.project;
  }

  fillFields(project: Project) {
    this.projectForm = this.formBuilder.group({
      projectName: project.name,
      projectDescription: project.description,
      expiryDate: project.expiryDate,
      budget: project.budget,
      photoUrl: project.photoUrl
    });
  }
}

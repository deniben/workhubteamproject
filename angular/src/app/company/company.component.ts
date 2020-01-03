import {AfterViewInit, Component, Input, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {Company} from '../models/company.model';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {FormBuilder, Validators} from '@angular/forms';
import {FileService} from '../services/file.service';
import {FileloadComponent} from '../fileload/fileload.component';
import {CompanyService} from '../services/company.service';
import {SkillsMultiselectComponent} from '../skills-multiselect/skills-multiselect.component';
import {PhotoTypes} from '../PhotoTypes';

@Component({
  selector: 'app-company',
  templateUrl: './company.component.html',
  styleUrls: ['./company.component.css']
})
export class CompanyComponent implements OnInit, AfterViewInit {
  private submitted: boolean;
  private errorType;

  constructor(private router: Router, private client: HttpClient, private companyService: CompanyService,
              private formBuilder: FormBuilder, private fileService: FileService, private route: ActivatedRoute) {
    this.companyForm = this.formBuilder.group({
      companyName: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(30)]],
      companyDescription: ['', [Validators.required, Validators.minLength(25), Validators.maxLength(2000)]],
      photoUrl: ['', [Validators.required]]
    });
  }

  @ViewChildren(SkillsMultiselectComponent)
  private skillMultiselectComponent1: QueryList<SkillsMultiselectComponent>;
  @ViewChild(SkillsMultiselectComponent, {static: false})
  private skillMultiselectComponent: SkillsMultiselectComponent;

  @ViewChild(FileloadComponent, {static: false})
  private file: FileloadComponent;

  @Input() header: string;
  @Input() status: string;
  @Input() submit: string;

  back = 'employer';
  companyForm;
  companyType = this.companyService.companyType;
  company: Company = new Company();
  idUpdate = this.route.snapshot.params.id;
  showSkills = false;
  id;
  companyName;
  description;
  type;
  skills;

  errorMsg;
  showErrors = false;

  ngAfterViewInit(): void {
    if (this.status === 'update') {
      this.skillMultiselectComponent1.changes.subscribe((multiselect: QueryList<SkillsMultiselectComponent>) => {
        if (this.company.type == 'EMPLOYEE') {
          multiselect.first.selectedItems = this.company.skills.map(multiselect.first.parseSkillsListToUI);
        }
      });
    }
  }

  ngOnInit() {
    this.companyService.companyType = this.companyType;
    if (this.status === 'update') {
      this.companyService.findCompanyByIdForUpdate(this.idUpdate).subscribe((company: Company) => {
        this.fillFields(this.companyMapper(company));
        if (this.company.type == 'EMPLOYEE') {
          this.showSkills = true;
          this.back = 'employee';
        }
      }, error => {
        this.router.navigate(['/error-page']);
      });
    }
    if (this.status === 'create') {
      if (this.companyType == 'EMPLOYEE') {
        this.showSkills = true;
        this.back = 'employee';
      }

    }


  }

  createOrEditCompany(companyForm) {
    this.submitted = true;
    if (this.companyForm.invalid || this.file.invalidFile) {
      console.log('INVALID');
      return;
    }
    this.fileService.fileUpload(this.file.postFile(), PhotoTypes.COMPANY).subscribe((response: any) => {
      this.company.name = companyForm.companyName;
      this.company.description = companyForm.companyDescription;
      this.company.blocked = false;
      if (this.company.type == 'EMPLOYEE') {
        this.company.skills = this.skillMultiselectComponent.getSelectedItems();
      } else {
        this.company.skills = [];
      }
      this.company.photoUrl = response.name;
      console.log(this.status);
      if (this.status === 'create') {
        this.company.type = this.companyType;
        if (this.company.type == 'EMPLOYEE') {
          this.company.skills = this.skillMultiselectComponent.getSelectedItems();
        } else {
          this.company.skills = [];
        }
        this.companyService.createCompany(this.company).subscribe((createdCompany: Company) => {
          console.log('created');
          if (this.company.type == 'EMPLOYEE') {
            this.router.navigate(['/employee']);
          } else {
            this.router.navigate(['/employer']);
          }
        }, error => {
          this.errorMsg = error.error.message;
          this.showErrors = true;
        });
        console.log('In the end create');
      }
      if (this.status === 'update') {
        this.company.id = this.idUpdate;
        this.companyService.updateCompany(this.company).subscribe((updatedCompany: Company) => {
          if (this.company.type == 'EMPLOYEE') {
            this.router.navigate(['/employee']);
          } else {
            this.router.navigate(['/employer']);
          }
        }, error => {
          this.errorMsg = error.error.message;
          this.showErrors = true;
        });
        console.log('In the end update');
      }
    });
  }

  companyMapper(response: Company): Company {
    this.company.id = response.id;
    this.company.name = response.name;
    this.company.description = response.description;
    this.company.avgMark = response.avgMark;
    this.company.type = response.type;
    this.company.skills = response.skills;
    this.company.isOwner = response.isOwner;
    this.company.photoUrl = response.photoUrl;
    this.file.imgURL = response.photoUrl;
    return this.company;
  }

  fillFields(company: Company) {
    this.companyForm = this.formBuilder.group({
      companyName: company.name,
      companyDescription: company.description,
      avgMark: company.avgMark,
      type: company.type,
      skills: company.skills,
      isOwner: company.isOwner,
      photoUrl: company.photoUrl
    });
  }


}

import {Component, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, Validators, FormGroup} from '@angular/forms';
import {Router} from '@angular/router';
import {Company} from '../models/company.model';
import {CompanyService} from '../services/company.service';
import {SkillsMultiselectComponent} from '../skills-multiselect/skills-multiselect.component';
import {FileloadComponent} from '../fileload/fileload.component';
import {FileService} from '../services/file.service';
import {PhotoTypes} from '../PhotoTypes';

@Component({
  selector: 'app-create-company',
  templateUrl: './create-company.component.html',
  styleUrls: ['./create-company.component.css']
})
export class CreateCompanyComponent implements OnInit {

  createCompanyForm: FormGroup;
  submitted: boolean;
  companyName: string;
  router: Router;
  company: Company = new Company;
  companyType: string;
  showMsg: boolean = false;
  showErrMsg: boolean = false;
  showSkills: boolean = false;

  @ViewChild(SkillsMultiselectComponent, {static: false})
  private skillMultiselectComponent: SkillsMultiselectComponent;

  @ViewChild(FileloadComponent, {static: false})
  private file: FileloadComponent;

  constructor(private formBuilder: FormBuilder, private companyService: CompanyService, private rout: Router, private fileService: FileService) {
    this.submitted = false;
    this.router = rout;
    this.createCompanyForm = this.formBuilder.group({
      companyName: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(30)]],
      companyDescription: ['', [Validators.required, Validators.minLength(25), Validators.maxLength(2000)]],
      photoUrl: ['', [Validators.required]]
    });
  }

  ngOnInit() {
    this.companyType = this.companyService.companyType;
    if (this.companyType == 'EMPLOYEE') {
      this.showSkills = true;
    }
  }

  createCompany(companyData) {
    console.log(companyData);
    this.submitted = true;
    this.company.name = companyData.companyName;
    this.company.description = companyData.companyDescription;
    this.company.type = this.companyType;
    this.company.blocked = false;
    if (this.companyType === 'EMPLOYEE') {
      console.log(companyData.skills);
      this.company.skills = this.skillMultiselectComponent.getSelectedItems();
    } else {
      this.company.skills = [];
    }
    this.fileService.fileUpload(this.file.postFile(), PhotoTypes.COMPANY).toPromise()
      .then((response: any) => {
        this.company.photoUrl = response.name;
        this.companyService.createCompany(this.company).subscribe(
          data => {
            this.createCompanyForm.reset();
            this.showMsg = true;
            this.router.navigate(['/employee']);
            console.log(data);
          },
          err => {
            this.showErrMsg = true;
          });
      });
  }

}

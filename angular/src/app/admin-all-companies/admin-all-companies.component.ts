import {Component, OnInit, ViewChild} from '@angular/core';
import {CompanyService} from '../services/company.service';
import {ModalTemplate, SuiModalService, TemplateModalConfig} from 'ng2-semantic-ui';
import {log} from 'util';
import {Company} from '../models/company.model';
import {SkillsMultiselectComponent} from '../skills-multiselect/skills-multiselect.component';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {DomSanitizer} from '@angular/platform-browser';
import {Router} from '@angular/router';
export interface IContext {
 data: string;
}
@Component({
 selector: 'app-admin-all-companies',
 templateUrl: './admin-all-companies.component.html',
 styleUrls: ['./admin-all-companies.component.css']
})
export class AdminAllCompaniesComponent implements OnInit {
 // @ts-ignore
 @ViewChild('modalTemplate', 'modalTemplate')
 public modalTemplate: ModalTemplate<IContext, string, string>;
 @ViewChild(SkillsMultiselectComponent, {static: false})
 private skillMultiselectComponent: SkillsMultiselectComponent;
 updateCompForm: FormGroup;
 companyById: any;
 descrip: string;
 companies = [];
 skills = [];
 ownerr = [];
 idmodal: any;
 submitted: boolean;
 company: Company = new Company;
 pageNumber = 1;
 pagesCount = 0;
 constructor(private compServ: CompanyService, public modalService: SuiModalService, private  formBuilder: FormBuilder, private sanitizer: DomSanitizer, private router: Router) {
   this.updateCompForm = this.formBuilder.group({
     companyName: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(30)]],
     companyDescription: ['', [Validators.required, Validators.minLength(25), Validators.maxLength(2000)]]
   });
 }
 ngOnInit() {
   this.allCompanies(this.pageNumber);
 }
 updateComp(companyData, id) {
   console.log(id);
   this.submitted = true;
   this.company.name = companyData.companyName;
   this.company.description = companyData.companyDescription;
   this.company.skills = this.skillMultiselectComponent.getSelectedItems();
   this.company.id = this.idmodal;
   console.log(this.company.id);
   this.compServ.updateCompany(this.company).subscribe(data =>{
     this.allCompanies(this.pageNumber);
   });
 }
 public open(id) {
   this.idmodal = id;
   const config = new TemplateModalConfig<IContext, string, string>(this.modalTemplate);
   const skillsComp = [];
   config.closeResult = 'closed!';
   this.compServ.findCompanyById(id).subscribe((data: Company) => {
     this.companyById = data;
     for (const skill of this.companyById.skills) {
       skillsComp.push({
         id: skill.id,
         name: skill.name
       });
       this.skills = skillsComp;
     }
   });
   this.modalService
     .open(config)
     .onApprove(result => { /* approve callback */
     })
     .onDeny(result => { /* deny callback */
     });
 }
 allCompanies(page: number) {
   console.log('method');
   this.compServ.findAllForAdmin(page - 1).subscribe((data: any) => {
     this.companies = data.items;
     this.pagesCount = data.pagesCount;
     console.log(data);
   });
 }
 blockCompany(id: any, i: number) {
   console.log(id);
   this.compServ.blockCompany(id).subscribe();
   this.companies.forEach((comp) => {
     if (comp.id === id) {
       this.companies.splice(i, 1);
     }
   });
 }
 more(id: any) {
   this.router.navigate(['/admin/projects/' + id]);
 }
 change_page(number) {
   this.pageNumber = number;
   this.allCompanies(this.pageNumber);
 }
}

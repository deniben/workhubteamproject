import { Component, OnInit, ViewChild } from "@angular/core";
import {
  HttpClient,
  HttpErrorResponse,
  HttpHeaders
} from "@angular/common/http";
import { Router } from "@angular/router";
import { Company } from "../models/company.model";
import { Project } from "../models/project.model";
import { EmployerService } from "../services/employer.service";
import { ActivatedRoute } from "@angular/router";
import { Location } from "@angular/common";
import { ToastrService } from "ngx-toastr";

@Component({
  selector: "app-view-project",
  templateUrl: "./view-project.component.html",
  styleUrls: ["./view-project.component.css"]
})
export class ViewProjectComponent implements OnInit {
  project: Project = new Project();
  id: number;
  rate: number = 1;
  companyId: number;
  percent: number;
  companysForMyProject: Array<Company> = new Array();
  percents: Array<number> = new Array();
  company: Company = new Company();
  i: number;
  pageNumber: number = 1;
  pagesCount: number = 0;
  index: boolean = false;
  pageSize: number = 6;
  status : String = "COMPLETED";

  alreadyApplied;

  constructor(
    private router: Router,
    private employerService: EmployerService,
    private route: ActivatedRoute,
    private location: Location,
    private activeRoute: ActivatedRoute,
    private toastr: ToastrService
  ) {}

  ngOnInit() {
    this.route.params.subscribe(params => (this.id = params["projectId"]));
    this.pageNumber = 1;
    this.findProjectById();
    this.findAllCompanysForMyProject();
  }

  findProjectById() {
    this.employerService.findProjectById(this.id).subscribe(data => {
      this.project = data;
      this.project.projectType = data.projectType;
      
    });
  }

  setRate(rate: number) {
    this.rate = rate;
  }

  findAllCompanysForMyProject() {
    console.log("do " + this.pageNumber);
    this.getPage();
    console.log("pisla " + this.pageNumber);

    this.employerService
      .findAllCompanysForMyProject(this.id, this.pageNumber-1)
      .subscribe(data => {
        this.companysForMyProject = data.items;
        this.pagesCount = data.pagesCount;
        this.findRate(this.companysForMyProject);
        this.setPercent();
      });
  }

  getPage() {
    this.activeRoute.queryParams.subscribe(params => {
      if (params["page"] && this.index === false) {
        if (params["page"] < 1) {
          console.log(" < 1");
          this.pageNumber = 1;
          console.log("ss " + this.pageNumber);
          this.location.go("employer/my-projects/view-project/"+this.id+"?page=" + this.pageNumber);
          this.showInfo("Warning","The page not exist");
        }
        else{
          //this.pageNumber = 1;
        console.log("get page " + this.pageNumber);
         this.pageNumber = <number>params["page"];
        console.log("page in get = " + this.pageNumber);
        }
      }
    });
  }

  change_page(number) {
    this.pageNumber = number;
    this.index = true;
    console.log("before find ");

    this.findAllCompanysForMyProject();
    this.location.go("employer/my-projects/view-project/"+this.id+"?page=" + this.pageNumber);
    console.log(" after find");
  }

  getPercentOfMatchedSkills(companyId: number) {
    this.employerService
      .getPercentOfMatchedSkills(this.id, companyId)
      .subscribe(data => {
        this.percent = data;
        this.percents.push(this.percent);
      });
  }

  setPercent() {
    for (let i = 0; i < this.companysForMyProject.length; i++) {
      this.getPercentOfMatchedSkills(Number(this.companysForMyProject[i].id));
    }
  }

  findRate(data: Array<Company>) {
    for (let a = 0; a < data.length; a++) {
      let proba = [];
      for (let i = 0; i < data[a].avgMark - 1; i++) {
        proba.push(1);
      }
      data[a]["rateArray"] = proba;
    }
  }

  acceptCompanyToProject(companyId: number, projectId: number) {
    this.employerService
      .acceptCompanyToProject(companyId, projectId)
      .subscribe();
    this.showSuccess("Accept","Company is succesful acceped");
   
  }

  rejectCompany(companyId: number, projectId: number) {
    console.log(companyId + "  " + projectId);
    this.employerService.rejectCompany(companyId, projectId).subscribe(data => {
      this.showSuccess("Reject","Company is successful rejected");
      // this.showInfo();
    });
  }

  finishProject(projectId: number) {
    console.log(projectId + "project");
    this.employerService.finishProject(projectId).subscribe(data => {
      this.showInfo("Delete","Project is deleted");
    });
  }

  showSuccess(first : string , last : string) {
    this.toastr.success(last,first, {
      progressBar: true,
      timeOut: 5000
    });
  }

  showInfo( first : string , last : string) {
    this.toastr.info(last,first,
      { progressBar: true, timeOut: 5000 }
    );
  }

  setCompanyId(id: number, i: number) {
    this.i = i;
    this.companyId = id;
  }

  rejectCompanyFromUI(i: number) {
    this.companysForMyProject.splice(i, 1);
  }

  completeProject(projectId: number, status: String) {
    console.log(status + "  status");
    this.employerService
      .completeProject(projectId, this.rate, status)
      .subscribe(data => {
        this.showSuccess("Project is finished","successful finish");
        this.showInfo("Rate","Company's rate is update");
      });
  }
}

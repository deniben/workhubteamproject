import { Component, OnInit, ViewChild, Input } from '@angular/core';
import { Router, ActivatedRoute } from "@angular/router";
import { CompanyService } from '../services/company.service';
import { ProjectService } from '../services/project.service';
import { Company } from "../models/company.model";
import { TokenService } from 'src/app/token.service';
import { Project } from '../models/project.model';

@Component({
  selector: 'app-view-company',
  templateUrl: './view-company.component.html',
  styleUrls: ['./view-company.component.css']
})
export class ViewCompanyComponent implements OnInit {

  company: Company;
  rate: number[];
  companyProjects: Project[];
  latestProjects: Project[];
  archiveProjects: Project[];
  homeLink: String;
  companyId: Number;
  isUserLoggedIn: boolean;

  constructor(private rout: Router, private companyService: CompanyService, private activeRoute: ActivatedRoute,
    private projectService: ProjectService, private tokenService: TokenService) {
    this.company = new Company();
    this.archiveProjects = new Array();
    this.latestProjects = new Array();
  }

  ngOnInit() {
    this.isUserLogIn();
    this.companyId = +this.activeRoute.snapshot.paramMap.get('id');
    this.companyService.findCompanyById(this.companyId).subscribe((data) => {
      this.company = data;
      this.projectService.getProjectsByCompany(this.company.id + '', this.company.type).subscribe((data) => {
        this.companyProjects = data;
        console.log(this.companyProjects);
        this.sortProjectsByStatus(this.companyProjects);
      })
    });
  }

  sortProjectsByStatus(projects: Project[]) {
    if (projects.length > 0) {
      var entry;
      for (entry in projects) {
        var item = projects[entry];
        if (item.projectStatus == "NEW" || item.projectStatus == "IN_PROGRESS") {
          this.latestProjects.push(item);
        } else {
          this.archiveProjects.push(item);
        }
      }
    }
  }

  isUserLogIn() {
    if (this.tokenService.getTokenIfExists() != null) {
      this.isUserLoggedIn = true;
    } else {
      this.isUserLoggedIn = false;
    }
  }

}

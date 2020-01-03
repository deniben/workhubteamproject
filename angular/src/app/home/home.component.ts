import { Component, OnInit } from '@angular/core';
import { Router } from "@angular/router";
import { CompanyService } from '../services/company.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  top_comp_employer = [];
  top_comp_employee = [];
  hotproject = [];

  constructor(private companyService: CompanyService, private router: Router) { }

  ngOnInit() {
    this.findAll();
  }

  findAll() {

    this.companyService.TopOfCompanyEmployer()
      .subscribe(data => {
        for (let company of data) {
          let description = company.description;
          if (description.length > 50) {
            description = description.substring(0, 50) + '...';
          }

          this.top_comp_employer.push({
            id: company.id,
            name: company.name,
            description: description,
            avgMark: company.avgMark,
          });
        }
      });

    this.companyService.TopOfCompanyEmployee()
      .subscribe(data => {
        for (let company of data) {
          let description = company.description;

          if (description.length > 50) {
            description = description.substring(0, 50) + '...';
          }

          this.top_comp_employee.push({
            id: company.id,
            name: company.name,
            description: description,
            avgMark: company.avgMark,
          });
        }
      });
  }

};



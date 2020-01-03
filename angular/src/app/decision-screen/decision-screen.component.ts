import { Component, OnInit } from "@angular/core";
import {
  HttpClient,
  HttpErrorResponse,
  HttpHeaders
} from "@angular/common/http";
import { Router } from "@angular/router";
import { FormBuilder, Validators } from "@angular/forms";
import { jqxComboBoxComponent } from "jqwidgets-ng/jqxcombobox";
import { CompanyService } from "../services/company.service";
import { environment } from "src/environments/environment";

@Component({
  selector: "app-decision-screen",
  templateUrl: "./decision-screen.component.html",
  styleUrls: ["./decision-screen.component.css"]
})
export class DecisionScreenComponent implements OnInit {
  httpClient;
  rout;
  message;
  companies;
  selectedOption;
  company;

  constructor(
    private client: HttpClient,
    private router: Router,
    private builder: FormBuilder,
    private companyService: CompanyService
  ) {
    this.httpClient = client;
    this.rout = router;
    this.message = "";

    this.companies = [];

    this.companyService.findAll().subscribe(data => {
      console.log(data);
      for (let company of data) {
        this.companies.push({ name: company.name });
      }
      console.log(this.companies);
    });
  }

  ngOnInit() {}

  selectedOptionChange(selectedOption: any) {
    if (selectedOption && selectedOption.name) {
      this.company = true;
    }
  }

  setCompanyTypeEmployee() {
    this.companyService.companyType = "EMPLOYEE";
    this.rout.navigate(["/create-company"]);
  }

  setCompanyTypeEmployer() {
    this.companyService.companyType = "EMPLOYER";
    this.rout.navigate(["/create-company"]);
  }

  apply() {
    console.log(this.selectedOption);
    if (!this.company) {
      return;
    }

    this.httpClient
      .get(environment.api_url + "/companies/join/" + this.selectedOption.name, {
        responseType: "text"
      })
      .subscribe(
        data => {
          this.rout.navigate(["/employee"]);
        },
        err => (this.message = JSON.parse(err.error).message)
      );
  }
}

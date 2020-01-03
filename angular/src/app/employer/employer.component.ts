import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { EmployerService } from "../services/employer.service";
import { Profile } from "../models/profile.models";
import { ToastrService } from "ngx-toastr";

@Component({
  selector: "app-employer",
  templateUrl: "./employer.component.html",
  styleUrls: ["./employer.component.css"]
})
export class EmployerComponent implements OnInit {
  profiles: Array<Profile>;
  pageNumber = 1;
  pagesCount = 0;

  constructor(
    private router: Router,
    private employerService: EmployerService,
    private toastr: ToastrService
  ) {
    this.findProfilesByCompany(this.pageNumber);
  }
  ngOnInit() {
    this.findProfilesByCompany(this.pageNumber);
  }

  findProfilesByCompany(page: number) {
    this.employerService.findProfilesByCompany(page - 1).subscribe(data => {
      console.log(data);
      this.profiles = data.items;
      console.log(this.profiles);
      this.pagesCount = data.pagesCount;

      console.log("pages count = " + data.pagesCount);
      console.log(this.profiles);
    });
  }

  change_page(number) {
    this.pageNumber = number;
    this.findProfilesByCompany(this.pageNumber);
  }
}

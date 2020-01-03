import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { EmployerService } from "../services/employer.service";
import { Location } from "@angular/common";
import { ActivatedRoute } from "@angular/router";
import { ToastrService } from "ngx-toastr";

@Component({
  selector: "app-my-projects",
  templateUrl: "./my-projects.component.html",
  styleUrls: ["./my-projects.component.css"]
})
export class MyProjectsComponent implements OnInit {
  Projects = [];
  status: String;
  flag: number = 1;
  pageNumber: number = 1;
  pagesCount: number = 0;
  index: boolean = false;
  pageSize: number = 6;
  dataOnPage: number = 0;
  checked: boolean;
  constructor(
    private router: Router,
    private employerService: EmployerService,
    private location: Location,
    private activeRoute: ActivatedRoute,
    private toastr: ToastrService
  ) {
    this.status = "NEW";
    this.checked = this.router.url.includes('profile');
    this.findByStatus(this.status);
  }

  ngOnInit() {

  }

  findByStatus(status: String) {
    console.log("page before get = " + this.pageNumber);
    this.getPage();
    console.log("page after get = " + this.pageNumber);
    this.employerService
      .findByStatus(status, this.pageNumber - 1)
      .subscribe(data => {
          console.log(data);
          this.Projects = data.items;
        this.pagesCount = data.pagesCount;
        console.log("page count = " + this.pagesCount);
        console.log(this.Projects);
      },

        err => ( this.showInfo(1),this.location.go("employer/my-projects?page=" + 1)));
  }

  getPage() {
    this.activeRoute.queryParams.subscribe(params => {
      if (params["page"] && this.index === false) {
        this.pageNumber = <any>params["page"];
      }
    });
  }

  showInfo(num: number) {
    this.toastr.warning(
      "Your assessment is taken into account ",
      "Number of page not exist",
      { progressBar: true, timeOut: 5000 }
    );
  }

  setStatus(status: String) {
    this.status = status;
    this.Projects = [];

    this.change_page(1);
    this.findByStatus(this.status);
  }

  setFlag(num: number): boolean {
    if (num == this.flag) {
      return true;
    } else {
      return false;
    }
  }

  change_page(number) {
    this.pageNumber = number;
    this.index = true;
    this.findByStatus(this.status);
    this.location.go("employer/my-projects?page=" + this.pageNumber);
  }
}

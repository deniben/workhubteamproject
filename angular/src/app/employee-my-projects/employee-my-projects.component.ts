import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { ActivatedRoute, Router } from '@angular/router';
import {Location} from '@angular/common';

@Component({
  selector: 'app-employee-my-projects',
  templateUrl: './employee-my-projects.component.html',
  styleUrls: ['./employee-my-projects.component.css']
})
export class EmployeeMyProjectsComponent implements OnInit {

  projects;

  number_of_pages;
  page_number;

  page_size;

  constructor(private client : HttpClient, private activatedRoute : ActivatedRoute,
              private router : Router, private location : Location) {}

  ngOnInit() {
    this.page_number = 1;
    this.number_of_pages = 1;
    this.page_size = 10;
    this.projects = [];

    this.activatedRoute.paramMap.subscribe(data => {
      if(data.get('page')) {
        this.page_number = data.get('page');
      }
      this.load();
    });

  }

  change_page(number) {
    this.page_number = number;
    this.load();
    this.location.go('/employee/my-projects?page=' + this.page_number);
  }

  load() {
    this.client.get<any>(environment.api_url + '/employee/projects/current/' + (Number(this.page_number) - 1)).subscribe(data => {
      this.number_of_pages = data.pagesCount;
      this.projects = data.items;
    },
    err => console.log(err.error.message));
  }


}

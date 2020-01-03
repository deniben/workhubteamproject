import {Component, OnInit, AfterViewInit, ElementRef, ViewChild} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {HttpClient, HttpErrorResponse, HttpHeaders} from '@angular/common/http';
import {DomSanitizer, SafeResourceUrl, SafeUrl} from '@angular/platform-browser';
import {Router} from '@angular/router';
import {ActivatedRoute} from '@angular/router';
import {environment} from 'src/environments/environment';
import {Location} from '@angular/common';
import * as $ from 'jquery';


@Component({
  selector: 'app-employee',
  templateUrl: './employee.component.html',
  styleUrls: ['./employee.component.css']
})
export class EmployeeComponent implements OnInit, AfterViewInit {

  searchForm;
  popular;
  all_projects;
  all_projects_left;
  all_projects_right;

  page_size;
  number_of_pages;
  page_number: number;

  @ViewChild('card', {static: false}) el: ElementRef;

  constructor(private formBuilder: FormBuilder, private client: HttpClient, private router: Router,
              private sanitizer: DomSanitizer, private activeRoute: ActivatedRoute, private location: Location) {

    this.page_size = 6;

    this.searchForm = formBuilder.group({
      name: ['', Validators.required]
    });

    this.popular = [];
    this.all_projects = [];
    this.all_projects_left = [];
    this.all_projects_right = [];
    this.number_of_pages = 1;
    this.page_number = 1;

    this.activeRoute.queryParams.subscribe(params => {

      if (params['page']) {
        console.log('page');
        this.page_number = <number> params['page'];
      }

    });

    this.load_data();

    this.client.get(environment.api_url + '/recommendations/by-history').subscribe((data: Array<any>) => {

        for (let project of data) {

          for (let ex_pr of this.popular) {

            if (ex_pr.id === project.id) {
              return;
            }

          }

          let img = this.sanitizer.bypassSecurityTrustUrl(project.photo);

          this.popular.push({
            id: project.id,
            name: project.projectType.name,
            project: project.name,
            companyName: project.companyCreator.name,
            budget: project.budget,
            photo: img
          });

        }

      },
      err => console.log('Server error'));

    this.client.get(environment.api_url + '/recommendations/by-skills').subscribe((data: any) => {

        for (let ex_pr of this.popular) {

          if (ex_pr.id === data.id) {
            return;
          }

        }
        let img = this.sanitizer.bypassSecurityTrustUrl(data.photo);

        this.popular.push({
          id: data.id,
          name: data.projectType.name,
          project: data.name,
          description: data.description,
          companyName: data.companyCreator.name,
          budget: data.budget,
          photo: img,
          by_skill: true
        });

      },
      err => console.log('Server error'));

  }

  load_data() {
    this.client.get<any>(environment.api_url + '/employee/projects?page=' + (Number(this.page_number) - 1)).subscribe(data => {
      this.all_projects = [];
      this.all_projects_left = [];
      this.all_projects_right = [];

      this.number_of_pages = data.pagesCount;

      for (let project of data.items) {

        let description = project.description;

        if (description.length > 40) {
          description = description.substring(0, 40) + '...';
        }

        let img = this.sanitizer.bypassSecurityTrustUrl(project.photo);

        this.all_projects.push({
          name: project.name,
          company: project.companyCreator,
          description: description,
          id: project.id,
          photo: img
        });
      }

      this.all_projects_left = this.all_projects.slice(0, (this.all_projects.length / 2));
      this.all_projects_right = this.all_projects.slice((this.all_projects.length / 2), this.all_projects.length);

    });
  }

  change_page(number) {
    this.page_number = number;
    this.load_data();
    this.location.go('/employee?page=' + this.page_number);
  }

  search(searchData) {
    this.router.navigate(['/search/' + searchData.name]);
  }

  ngOnInit() {
  }

  ngAfterViewInit() {
  }


}

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { ViewChild, ElementRef } from '@angular/core';
import {Location} from '@angular/common';

@Component({
  selector: 'app-manage-users-edit',
  templateUrl: './manage-users-edit.component.html',
  styleUrls: ['./manage-users-edit.component.css']
})
export class ManageUsersEditComponent implements OnInit {

  users;

  current_page;
  pages_count;

  page_size;

  roles;

  filter_param;

  @ViewChild("filter_field", {static : true}) filter_input : ElementRef;

  constructor(private client : HttpClient, private activatedRoute : ActivatedRoute,
     private router : Router, private location : Location) {
    this.current_page = 1;
    this.pages_count = 1;
    this.page_size = 20;
  }

  ngOnInit() {

    this.roles = [];

    this.client.get(environment.api_url + '/admin/roles').subscribe(data => {
      this.roles = data;
    });

    this.activatedRoute.paramMap.subscribe(params => {

      if(params.get('page')) {
        this.current_page = params.get('page');
      }

      this.activatedRoute.queryParams.subscribe(params => {
        if(params['filter']) {
          this.filter_param = params['filter'];
          this.filter_input.nativeElement.value = this.filter_param;
          this.refresh_with_filter();
        } else {
          this.load_users();
        }
      });

    });

  }

  load_users() {
    this.client.get<any>(environment.api_url + '/admin/users?page=' + (Number(this.current_page) - 1 )).subscribe(data => {

      if(data.items) {
        this.users = data.items;
      }

      if(data.pagesCount) {
        this.pages_count = data.pagesCount;
      }

    });
  }

  change_role(user_id, role) {
    let options : Object = { responseType : 'text' };
    this.client.post<any>(environment.api_url + '/admin/users/roles?id=' + user_id + "&role=" + role, {}, options).subscribe();
  }

  block(user) {
    let options : Object = { responseType : 'text' };
    this.client.post<any>(environment.api_url + '/admin/users/block/' + user.userId, {}, options).subscribe(data => {
      user.blocked = true;
    });
  }

  unblock(user) {
    let options : Object = { responseType : 'text' };
    this.client.post<any>(environment.api_url + '/admin/users/unblock/' + user.userId, {}, options).subscribe(data => {
      user.blocked = false;
    });
  }

  filter(event) {
      this.filter_param = event.srcElement.value;
      this.refresh_with_filter();
  }

  refresh_with_filter() {
    this.client.get<any>(environment.api_url + '/admin/users?page=' + (Number(this.current_page) - 1) + '&username=' + this.filter_param).subscribe(data => {

      if(data.items) {
        this.users = data.items;
      }

      if(data.pagesCount) {
        this.pages_count = data.pagesCount;
      }

    });
  }

  change_page(number) {
    this.current_page = number;

    let url = '/admin/users/' + this.current_page;

    if(this.filter_param) {
      this.refresh_with_filter();
      url += '?filter=' + this.filter_param;
    } else {
      this.load_users();
    }
    this.location.go(url);
  }

}

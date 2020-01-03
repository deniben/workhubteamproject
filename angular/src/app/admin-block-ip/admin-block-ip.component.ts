import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { environment } from 'src/environments/environment';
import {Location} from '@angular/common';

@Component({
  selector: 'app-admin-block-ip',
  templateUrl: './admin-block-ip.component.html',
  styleUrls: ['./admin-block-ip.component.css']
})
export class AdminBlockIpComponent implements OnInit {

  ipForm;
  submitted;

  addresses;
  pages_count;
  current_page;

  page_size;

  message;
  error_message;

  constructor(private fromBuilder : FormBuilder, private client : HttpClient,
  private activedRoute : ActivatedRoute, private router : Router, private location : Location) {

    this.submitted = false;
    this.pages_count = 1;
    this.current_page = 1;
    this.addresses = [];
    this.page_size = 20;
    this.ipForm = this.fromBuilder.group({
      address : ['', [Validators.required, Validators.pattern(new RegExp("^(\\d{1,3}\\.){3}\\d{1,3}$"))]],
      reason : ['', Validators.required]
    });

  }
  clearMsg() {
    this.message = '';
    this.error_message = '';
  }

  block_ip(address) {
    let options : Object = {responseType : 'text'};
    this.submitted = true;

    if(this.ipForm.invalid) {
      return;
    }

    this.client.post<any>(environment.api_url + '/admin/block/addresses', {
      address : address.address,
      reason : address.reason
    }, options).subscribe(data => {
      this.message = 'Successfully blocked';
      this.error_message = '';
      this.fetch_data();
    }, err =>  {
      this.error_message = JSON.parse(err.error).message;
      this.message = '';
    });

    this.submitted = false;
    this.ipForm.reset();

  }

  unblock(id) {
    let options : Object = {responseType : 'text'};
    this.client.delete<any>(environment.api_url + '/admin/block/addresses/' + id, options).subscribe(data => {
      this.message = 'Successfully unblocked';
      this.error_message = '';
      this.fetch_data();
    }, err =>  {
      this.error_message = err.error.message;
      this.message = '';
    });
  }

  fetch_data() {
    this.client.get<any>(environment.api_url + '/admin/block/addresses/' + (this.current_page - 1)).subscribe(data => {
      this.addresses = data.items;
      this.pages_count = data.pagesCount;
    });
  }

  change_page(number) {
    this.current_page = number;
    this.fetch_data();
    this.location.go('/admin/block-ip/' + this.current_page);
  }

  ngOnInit() {

    this.activedRoute.paramMap.subscribe(data => {

      if(data.get('page')) {
        this.current_page = data.get('page');
      }

      this.fetch_data();

    });
  }

}

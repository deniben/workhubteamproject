import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-manage-users-create',
  templateUrl: './manage-users-create.component.html',
  styleUrls: ['./manage-users-create.component.css']
})
export class ManageUsersCreateComponent implements OnInit {

  create_user_form;
  submitted;

  done;
  user_exists;

  constructor(private formBuilder : FormBuilder, private client : HttpClient) {
    this.done = false;
    this.user_exists = false;
  }

  ngOnInit() {
    this.create_user_form = this.formBuilder.group({
      email : ['', [Validators.required, Validators.email]]
    });
  }

  clear_err() {
    this.user_exists = false;
  }

  continue() {
    this.done = false;
  }

  create(data) {

    this.submitted = true;

    if(this.create_user_form.invalid || this.done) {
      return;
    }

    this.submitted = false;

    let options : Object = {responseType : 'text'};

    this.client.post<any>(environment.api_url + '/admin/users', {email : data.email},
     options).subscribe(data => {
       this.done = true;
       this.create_user_form.reset();
     }, err => this.user_exists = true);

  }

}

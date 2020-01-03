import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent implements OnInit {

  change_password_form;
  submitted;
  not_match_error;
  new_old_diff_error;
  bad_old_pass_error;

  checked;
  message;
  err_message;

  constructor(private formBuilder : FormBuilder, private client : HttpClient, private activatedRoute : ActivatedRoute) { }

  ngOnInit() {

    this.activatedRoute.queryParams.subscribe(params => {

      if(params['token']) {
        this.client.post(environment.api_url + '/change-password/email-verification/' + params['token'], {}, { responseType : 'text' }).subscribe(data => {
          this.doSuccess(data);
        }, err => this.err_message = JSON.parse(err.error).message);
      }

    });

    this.submitted = false;
    this.not_match_error = false;
    this.new_old_diff_error = false;
    this.bad_old_pass_error = false;
    this.message = '';
    this.err_message = '';

    this.change_password_form = this.formBuilder.group({
      old_password : ['', Validators.minLength(6)],
      new_password : ['', [Validators.required, Validators.minLength(6), Validators.pattern(new RegExp('^(?=.*[A-Z])(?=.*[a-z])(?=.*[1-9])(?=.{6,})'))]],
      repeated_password : ['', Validators.required]
    });

  }

  clearDiffError() {
    this.new_old_diff_error = false;
  }

  clearBadPassError() {
    this.bad_old_pass_error = false;
  }

  clearNotMatchError() {
    this.not_match_error = false;
  }

  doSuccess(message) {
      this.message = message;
      this.submitted = false;
      this.not_match_error = false;
      this.new_old_diff_error = false;
      this.bad_old_pass_error = false;
      this.change_password_form.reset();
  }

  switch_mode(event) {
    this.checked = event.srcElement.checked;
  }

  changePassword(data) {

    this.submitted = true;

    this.message = '';

    if (this.change_password_form.invalid) {
      return;
    }

    if(!this.checked && !data.old_password) {
      this.bad_old_pass_error = true;
      return;
    }

    this.not_match_error = false;
    this.new_old_diff_error = false;
    this.bad_old_pass_error = false;

    if (data.old_password === data.new_password) {
      this.new_old_diff_error = true;
      return;
    } else if (data.new_password !== data.repeated_password) {
      this.not_match_error = true;
      return;
    }

    if(this.checked) {

      this.client.post(environment.api_url + '/change-password/email-verification', {
        newPassword : data.new_password,
        repeatedPassword : data.repeated_password
      }, {responseType : 'text'}).subscribe(data => {
        this.doSuccess('Please, confirm password change via email. Thank you!');
      });

    } else {

      this.client.post(environment.api_url + '/change-password', {
        oldPassword : data.old_password,
        newPassword : data.new_password,
        repeatedPassword : data.repeated_password
      }, {responseType: 'text'}).subscribe(data => {
        this.doSuccess('Password was successfully changed! Thank you!');
      },
      err => this.bad_old_pass_error = true);

    }

  }

}

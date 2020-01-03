import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from '@angular/common/http';
import { TokenService } from 'src/app/token.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent implements OnInit {

  recoveryForm;
  change_password_form;

  submitted;
  message;
  mail_sent;
  token;

  change_success;

  password_change_submitted;

  change_password;

  constructor(private formBuilder : FormBuilder, private path : ActivatedRoute, private client : HttpClient, private tokenService : TokenService) { }

  ngOnInit() {

    this.message = '';
    this.mail_sent = false;
    this.change_password = false;
    this.password_change_submitted = false;
    this.token = '';
    this.change_success = false;

    this.path.queryParams.subscribe(data => {

      if (data.id != null && data.token != null) {

        this.token = data.token;

        this.client.post(environment.api_url + '/recover-password/check-token?id=' + data.id + '&token=' + data.token, {}, {responseType : 'text', observe : 'response'}).subscribe(data => {

            this.change_password = true;

            if (data.headers.get('Authorization')) {
              this.tokenService.setToken(data.headers.get('Authorization'));
            }

        }, err => this.message = JSON.parse(err.error).message);

      }

    });

    this.recoveryForm = this.formBuilder.group({
      email : ['', [Validators.email, Validators.required]]
    });

    this.change_password_form = this.formBuilder.group({
      password: ['', [Validators.required, Validators.minLength(6), Validators.pattern(new RegExp('^(?=.*[A-Z])(?=.*[a-z])(?=.*[1-9])(?=.{6,})'))]],
      repeated_password: ['', [Validators.required, Validators.minLength(6)]]
    });

  }

  changePasswordWithToken(data) {

    if (this.change_success) {
      return;
    }

    this.password_change_submitted = true;

    if(this.change_password_form.invalid || this.token === null || data.password != data.repeated_password) {
      return;
    }

    this.client.post(environment.api_url + '/change-password/' + this.token, {
      newPassword : data.password,
      repeatedPassword : data.repeated_password
    }, {responseType: 'text'}).subscribe(data => {
      this.change_success = true;
    }, err => this.message = JSON.parse(err.error).message);

  }

  recover(data) {

    this.submitted = true;

    if (this.recoveryForm.invalid || this.mail_sent) {
      return;
    }

    let params = new HttpParams();
    params = params.append('email', data.email);

    this.client.post(environment.api_url + '/recover-password?email=' + data.email, {}, {responseType : 'text'}).subscribe(data => {
      this.mail_sent = true;
      this.message = '';
    }, err => this.message = JSON.parse(err.error).message);

  }

}

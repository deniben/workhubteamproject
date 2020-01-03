import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Router } from "@angular/router";
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  registrationForm;
  submitted;
  username;
  client;
  message;
  activation;
  router;
  emailInUseError;

  sent;

  constructor(private formBuilder: FormBuilder, private httpClient: HttpClient, private rout: Router) {

    this.registrationForm = this.formBuilder.group({

        username : ['', [Validators.email, Validators.required]],
        password : ['', [Validators.required, Validators.minLength(6), Validators.pattern(new RegExp('^(?=.*[A-Z])(?=.*[a-z])(?=.*[1-9])(?=.{6,})'))]],
        repeatedPassword : ['', [Validators.required, Validators.minLength(6)]]

    });

    this.client = httpClient;

    this.submitted = false;
    this.activation = false;
    this.emailInUseError = false;
    this.router = rout;
    this.sent = false;

  }

  ngOnInit() {
  }

  passwordMatch() {
    // tslint:disable-next-line:triple-equals
    return this.registrationForm.value.password == this.registrationForm.value.repeatedPassword;
  }

  register(registrationData) {

    if(this.sent) {
      return;
    }

    this.sent = true;

    this.submitted = true;
    if (this.registrationForm.invalid) {
      return;
    }

    this.client.post(environment.api_url + '/registration', {

      username : registrationData.username,
      password : registrationData.password,
      repeatedPassword : registrationData.repeatedPassword

    }, { observe: 'response', responseType : 'text' }).subscribe((data) => {

        if (data.status === 200) {

          this.activation = true;

        } else if (data.status === 400) {

          this.emailInUseError = true;

        } else {

          this.message = 'data.body';

        }

    },
    err => {

        if (err.status === 400) {
          this.emailInUseError = true;
        } else {
          this.message = err.message;
        }

    });

  }

}

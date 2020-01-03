import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { ActivatedRoute } from '@angular/router';
import { TokenService } from 'src/app/token.service';
import { Router } from "@angular/router";
import { environment } from 'src/environments/environment';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  message = '';
  counter = 0;

  tokenService;

  userForm;
  client;

  submitted;

  github_login_url;
  google_login_url;

  constructor(private formBuilder: FormBuilder, private httpClient: HttpClient,
    private tokenServ: TokenService, private router: Router, private activatedRoute : ActivatedRoute) {

      this.activatedRoute.queryParams.subscribe(data => {
        if(data['blocked'] && data['blocked'] === 'true') {
          this.message = 'This account is blocked';
        }
      });


    let base_url = environment.api_url + '/authorization';

    this.github_login_url = base_url + '/github';
    this.google_login_url = base_url + '/google';

    this.userForm = this.formBuilder.group({

      username: ['', Validators.required],
      password: ['', Validators.required]

    });

    this.tokenService = this.tokenServ;
    this.client = this.httpClient;

  }

  login(credentials) {

    this.submitted = true;
    this.message = null;
    console.log(this.submitted);
    console.log(this.message);
    if (this.userForm.invalid) {
      return;
    }

    const httpOption = {
      observe: 'response',
      responseType: 'text',
      headers: new HttpHeaders({
        'Content-Type': 'application/x-www-form-urlencoded'
      })
    };

    let params = "username=" + credentials.username + "&password=" + credentials.password;
    this.client.post(environment.api_url + '/login', params, httpOption)
      .subscribe((data) => {

        if (data.body === "Success") {

          this.userForm.reset();

          let token = data.headers.get('Authorization');

          if (token) {
            this.tokenService.setToken(token);
          }
          this.router.navigate(['/employee']);
          return;

        } else {

          this.message = data.body;
          this.counter++;
          this.submitted = false;
          this.userForm.controls.password.reset();

        }

      },
        err => this.handleError(err));
  }

  handleError(err: HttpErrorResponse) {

    if (err.error && err.error.text) {
      this.message = err.error.text;
    } else {
      this.message = 'Server error. ' +
        'Please, try later';
      console.log(err);
    }

  }

}

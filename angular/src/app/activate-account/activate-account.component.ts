import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { TokenService } from 'src/app/token.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-activate-account',
  templateUrl: './activate-account.component.html',
  styleUrls: ['./activate-account.component.css']
})
export class ActivateAccountComponent implements OnInit {

  message;
  success;

  constructor(private activatedRoute : ActivatedRoute, private client : HttpClient, private tokenService : TokenService) {}

  ngOnInit() {

    this.message = '';
    this.success = false;

    this.activatedRoute.queryParams.subscribe(params => {

        let id = params['id'];
        let token = params['token'];

        if (id != null && token != null) {

          let urlParams = '?id=' + id + '&token=' + token;

          this.client.get(environment.api_url + '/registration/activate-account' + urlParams,
                      {responseType : 'text', observe : 'response'})
                  .subscribe((data) => {

                    if (data.status === 200) {

                      if (data.headers.get('Authorization')) {
                        this.tokenService.setToken(data.headers.get('Authorization'));
                      }

                      this.message = 'Your account was successfully activated. Thank you!';
                      this.success = true;
                    }

                  }, err => {
                    this.message = 'Sorry, wrong activation data';
                  });

        } else {
          this.message = 'Invalid input data';
        }


    });

  }

}

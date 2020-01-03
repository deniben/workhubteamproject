import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {TokenService} from 'src/app/token.service';
import {Router} from '@angular/router';
import {ProfileModel} from '../models/profile.model';
import {ProfileService} from '../services/profile.service';
import {ToastrService} from 'ngx-toastr';
import {AuthService} from '../services/auth.service';
import {AuthGuardService} from '../auth-guard.service';

@Component({
  selector: 'app-oauth2',
  templateUrl: './oauth2.component.html',
  styleUrls: ['./oauth2.component.css']
})
export class Oauth2Component implements OnInit {

  constructor(private activeRoute: ActivatedRoute,
              private tokenService: TokenService,
              private router: Router,
              private profileService: ProfileService,
              private auth: AuthGuardService) {
  }

  ngOnInit() {

    this.activeRoute.queryParams.subscribe(params => {

      if (params['token'] && params['token'].length > 10) {
        this.tokenService.setToken('Bearer ' + params['token']);
      }
      this.auth.canActivate();
    });

  }

}

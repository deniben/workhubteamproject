import {Component, OnInit} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders} from '@angular/common/http';
import {TokenService} from 'src/app/token.service';
import {DomSanitizer, SafeResourceUrl, SafeUrl} from '@angular/platform-browser';
import {environment} from 'src/environments/environment';

@Component({
  selector: 'app-profile-view',
  templateUrl: './profile-view.component.html',
  styleUrls: ['./profile-view.component.css']
})
export class ProfileViewComponent implements OnInit {

  id;
  name;
  nickname;
  photoUrl;

  constructor(private tokenService: TokenService, private client: HttpClient, private sanitizer: DomSanitizer) {
    this.client.get<any>(environment.api_url + '/profiles/data').subscribe((data) => {

        if (data.id) {
          this.id = data.id;
        }
        if (data.firstName && data.lastName) {
          this.name = data.firstName + ' ' + data.lastName;
        }
        if (data.nickname) {
          this.nickname = data.nickname;
        }
        if (data.photoUrl) {

          const img = this.sanitizer.bypassSecurityTrustUrl(data.photoUrl);
          this.photoUrl = img;
        }

      },
      err => console.log(err));
  }

  ngOnInit() {
  }

  logout() {
    this.tokenService.logout();
  }

}

import {Component, OnInit, ViewChild} from '@angular/core';
import {ProfileComponent} from '../profile/profile.component';
import {ProfileModel} from '../models/profile.model';
import {ActivatedRoute} from '@angular/router';
import {ProfileService} from '../services/profile.service';
import {ToastrService} from 'ngx-toastr';
import {FormGroup} from '@angular/forms';
import {Profile} from '../models/profile.models';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-profile-represent',
  templateUrl: './profile-represent.component.html',
  styleUrls: ['./profile-represent.component.css']
})
export class ProfileRepresentComponent implements OnInit {

  profileModel: ProfileModel = new ProfileModel();
  applications = [];
  sizeApp: any;
  projects: Array<Profile> = new Array<Profile>();
  pageNumber = 1;
  pagesCount = 0;
  representProfileForm = new FormGroup({});


  constructor(private route: ActivatedRoute,
              private toastr: ToastrService,
              private profileService: ProfileService,
              private httpClient: HttpClient) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => (this.profileModel.id = params['id']));
    console.log(this.profileModel);
    this.profileService.findById(this.profileModel.id).subscribe((profile: ProfileModel) => {

        this.profileModel.firstName = profile.firstName;
        this.profileModel.lastName = profile.lastName;
        this.profileModel.nickname = profile.nickname;
        this.profileModel.photoUrl = profile.photoUrl;
        this.profileModel.company = profile.company;
        this.profileModel.user = profile.user;
        console.log(this.profileModel.company);
      },
      error => {
        this.showInfo(error.error.details);
        console.log(error);
      });
  }

  showInfo(massage: string) {
    this.toastr.error(
      massage,
      'Error',
      {progressBar: true, timeOut: 700000}
    );
  }

}

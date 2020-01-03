import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {AbstractControl, FormBuilder, Validators} from '@angular/forms';
import {ProfileService} from '../services/profile.service';
import {FileloadComponent} from '../fileload/fileload.component';
import {FileService} from '../services/file.service';
import {ProfileModel} from '../models/profile.model';
import {PhotoTypes} from '../PhotoTypes';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css'],
  providers: [FileloadComponent]
})
export class ProfileComponent implements OnInit {
  profileForm;
  profileModel: ProfileModel = new ProfileModel();
  submitted = false;
  id;
  firstName;
  lastName;
  nickname;
  @ViewChild(FileloadComponent, {static: true})
  private file: FileloadComponent;

  @Input() header: string;
  @Input() status: string;
  @Input() submit: string;

  constructor(private router: Router, private client: HttpClient, private profileService: ProfileService,
              private formBuilder: FormBuilder, private fileService: FileService) {
    console.log(this.submit);
    this.fillFields(new ProfileModel());
  }

  createOrEditProfile(createProfileForm) {
    this.submitted = true;
    if (this.profileForm.invalid || this.file.invalidFile) {
      return;
    }
    this.fileService
      .fileUpload(this.file.postFile(), PhotoTypes.PROFILE)
      .subscribe((response: any) => {
        this.profileModel.firstName = createProfileForm.firstName;
        this.profileModel.lastName = createProfileForm.lastName;
        this.profileModel.nickname = createProfileForm.nickname;
        this.profileModel.photoUrl = response.name;
        if (this.status === 'create') {
          console.log(this.status);
          this.profileService.createProfile(this.profileModel).subscribe();
          this.router.navigate(['/decision']);
        }
        if (this.status === 'update') {
          console.log(this.profileModel);
          console.log(this.id);
          this.profileService.editProfile(this.profileModel).subscribe((updatedProfile: ProfileModel) => {
            console.log(updatedProfile.id);
            this.router.navigate(['/profile/' + updatedProfile.id]);
          });
        }
      });
  }

  profileMapper(response: ProfileModel): ProfileModel {
    this.profileModel.id = response.id;
    this.profileModel.firstName = response.firstName;
    this.profileModel.lastName = response.lastName;
    this.profileModel.nickname = response.nickname;
    this.profileModel.photoUrl = response.photoUrl;
    this.file.imgURL = response.photoUrl;
    return this.profileModel;
  }

  fillFields(profile: ProfileModel) {
    this.profileForm = this.formBuilder.group({
      firstName: [profile.firstName, Validators.compose([Validators.required, Validators.minLength(3),
        Validators.maxLength(25), startOfNameValidation, holeNameValidation, endOfNameValidation, specialSymbolsValidation])],
      lastName: [profile.lastName, Validators.compose([Validators.required, Validators.minLength(3),
        Validators.maxLength(25), startOfNameValidation, holeNameValidation, endOfNameValidation, specialSymbolsValidation])],
      nickname: [profile.nickname, Validators.compose([Validators.required, Validators.minLength(3),
        Validators.maxLength(25)])],
      photoUrl: [profile.photoUrl, Validators.compose([Validators.required])]
    });
  }

  ngOnInit(): void {
    if (this.status === 'update') {
      this.profileService.findCurrentUsersProfile().subscribe((profile: ProfileModel) => {
        console.log(profile);
        this.fillFields(this.profileMapper(profile));
      });
    }
  }
}

export function startOfNameValidation(
  control: AbstractControl
): { [key: string]: boolean } | null {
  if (!control.value.toString().match('^[a-zA-Z]') && control.value !== '') {
    console.log('start');
    return {startOfName: true};
  }
  return;
}

export function holeNameValidation(
  control: AbstractControl
): { [key: string]: boolean } | null {
  if (
    !control.value.toString().match('^[a-zA-Z-\']*$') &&
    control.value !== ''
  ) {
    return {holeName: true};
  }
  return;
}

export function endOfNameValidation(control: AbstractControl): { [key: string]: boolean } | null {
  if (!control.value.toString().match('[a-zA-Z]$') && control.value !== '') {
    return {endOfName: true};
  }
  return;
}

export function specialSymbolsValidation(control: AbstractControl): { [key: string]: boolean } | null {
  const doubleHhyphen = !control.value.toString().match('^((?!.*--.*).)*$');
  const hyphenAndApostrophe = !control.value.toString().match('^((?!.*-\'.*).)*$');
  const apostropheAndHyphen = !control.value.toString().match('^((?!.*\'-.*).)*$');
  const doubleApostrophe = !control.value.toString().match('^((?!.*\'\'.*).)*$');
  if (((doubleApostrophe) || (hyphenAndApostrophe) || (apostropheAndHyphen) || (doubleHhyphen)) && control.value !== '') {
    return {symbols: true};
  }
  return;
}

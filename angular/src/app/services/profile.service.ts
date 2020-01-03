import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {URL, PROFILE} from '../../constants';
import {ProfileModel} from '../models/profile.model';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {

  constructor(private client: HttpClient) {
  }

  createProfile(profile: ProfileModel) {
    const url = URL + PROFILE;
    console.log(profile);
    return this.client.post(url, profile);
  }

  editProfile(profile: ProfileModel) {
    console.log(profile);
    const url = URL + PROFILE;
    return this.client.put(url, profile);
  }

  findById(id: number) {
    const url = URL + PROFILE + '/' + id;
    return this.client.get(url);
  }

  findCurrentUsersProfile() {
    const url = URL + PROFILE + '/data';
    return this.client.get<any>(url);
  }

  isCurrentUserCompanyOwner() {
    const url = URL + PROFILE + '/isOwner';
    return this.client.get(url);
  }

}

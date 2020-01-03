import {UserModel} from './user.model';
import {Company} from './company.model';

export class ProfileModel {

  id: number;

  firstName: string;

  lastName: string;

  nickname: string;

  photoUrl: string;

  user: UserModel;

  company: Company;

  constructor() {
    this.firstName = '';
    this.lastName = '';
    this.nickname = '';
    this.photoUrl = '';
    this.user = new UserModel();
    this.company = new Company();
  }
}

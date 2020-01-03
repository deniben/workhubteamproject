import {Injectable} from '@angular/core';
import {CanActivate, Router} from '@angular/router';
import {AuthService} from './services/auth.service';
import {ToastrService} from 'ngx-toastr';

​
const NOT_AUTHORIZED = 'auth_fail';
const PROFILE_NOT_FILLED = 'prof_is_empty';
const COMPANY_NEEDED = 'comp_is_empty';
const COMPANY_IS_NOT_ACCEPTED = 'comp_is_not_accepted';
const ADMIN = 'ADMIN';
const BLOCKED_COMPANY = 'company_is_blocked';
const USER_IS_BLOCKED = 'user_blocked';
const EMPLOYEE = 'EMPLOYEE';
const EMPLOYER = 'EMPLOYER';
​
@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate {
  constructor(public auth: AuthService, public router: Router, public toastr: ToastrService) {
  }

​

  canActivate() {
    this.auth.getAuthStatus().subscribe((res: any) => {
      console.log(res);
    }, (err) => {
      console.log(err);
      if ((err.status === 406) && ((err.error !== COMPANY_NEEDED) && (err.error !== COMPANY_IS_NOT_ACCEPTED) && (err.error !== PROFILE_NOT_FILLED))) {
        this.router.navigate(['/ip-blocked'], {queryParams: {reason: err.error}});
      }
​
      if (!err.error.text) {
        if (err.error.trim() === USER_IS_BLOCKED.trim()) {
          let params = {queryParams: {blocked: true}};
          this.router.navigate(['/login'], params);
          return false;
        }
        if (err.error === PROFILE_NOT_FILLED) {
          if (this.router.url.includes('authorization/oauth2')) {
            console.log('awdawdawd');
            this.showInfo();
          }
          this.router.navigate(['/create-profile']);
          return false;
        }
        if (err.error === COMPANY_NEEDED) {
          if (!this.router.url.includes('create-company')) {
            this.router.navigate(['/decision']);
          }
          return false;
        } else if (err.error === COMPANY_IS_NOT_ACCEPTED) {
          this.router.navigate(['/notAcceptedCompany']);
          return false;
        } else if (err.error === BLOCKED_COMPANY) {
          this.router.navigate(['/blocked']);
          return false;
        } else if (
          err.error.text === EMPLOYEE &&
          (this.router.url.includes('employer') ||
            this.router.url.includes('admin') ||
            this.router.url.includes('dec') ||
            this.router.url.includes('create'))
        ) {
          this.router.navigate(['/employee']);
          // return false;
        } else if (
          err.error.text === EMPLOYER &&
          (this.router.url.includes('employee') ||
            this.router.url.includes('create') ||
            this.router.url.includes('admin') ||
            this.router.url.includes('dec'))
        ) {
          this.router.navigate(['/employer']);
          // return false;
        } else if (
          err.error.text === ADMIN &&
          (this.router.url.includes('employee') ||
            this.router.url.includes('authorization/oauth2') ||
            this.router.url.includes('employer') ||
            this.router.url.includes('dec') ||
            this.router.url.includes('create'))
        ) {
          this.router.navigate(['/admin/allCompanies']);
          // return false;
        }
      }
      if (err.error === NOT_AUTHORIZED) {
        this.router.navigate(['/login']);
        return false;
      }
      if (err.error === PROFILE_NOT_FILLED) {
        if (this.router.url.includes('authorization/oauth2')) {
          console.log('awdawdawd');
          this.showInfo();
        }
        this.router.navigate(['/create-profile']);
        return false;
      }
      if (err.error === COMPANY_NEEDED) {
        if (!this.router.url.includes('create-company')) {
          this.router.navigate(['/decision']);
        }
        return false;
      } else if (err.error === COMPANY_IS_NOT_ACCEPTED) {
        this.router.navigate(['/notAcceptedCompany']);
        return false;
      } else if (err.error === BLOCKED_COMPANY) {
        this.router.navigate(['/blocked']);
        return false;
      } else if (err.error.text === EMPLOYEE && (this.router.url.includes('employer') || this.router.url.includes('admin') || this.router.url.includes('dec') || this.router.url.includes('create'))) {
        this.router.navigate(['/employee']);
        // return false;
      } else if (err.error.text === EMPLOYER && (this.router.url.includes('employee') || this.router.url.includes('create') || this.router.url.includes('admin') || this.router.url.includes('dec'))) {
        this.router.navigate(['/employer']);
        // return false;
      } else if (err.error.text === ADMIN && (this.router.url.includes('employee') || this.router.url.includes('authorization/oauth2') || this.router.url.includes('employer') || this.router.url.includes('dec') || this.router.url.includes('create'))) {
        this.router.navigate(['/admin/allCompanies']);
        // return false;
      }

    });
    return true;
  }

  showInfo() {
    this.toastr.warning(
      'Name policy in your google profile doesn\'t, suit to our profile. Please create profile with your own=)',
      'Name policy',
      {progressBar: true, timeOut: 700000}
    );
  }
}

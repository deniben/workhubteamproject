import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from './login/login.component';
import {RegistrationComponent} from './registration/registration.component';
import {DecisionScreenComponent} from './decision-screen/decision-screen.component';
import {ActivateAccountComponent} from './activate-account/activate-account.component';
import {EmployeeComponent} from './employee/employee.component';
import {SearchComponent} from './search/search.component';
import {HomeComponent} from './home/home.component';
import {AuthGuardService} from './auth-guard.service';
import {ErrorPageComponent} from './error-page/error-page.component';
import {EmployerComponent} from './employer/employer.component';
import {ViewProjectComponent} from './view-project/view-project.component';
import {CreateCompanyComponent} from './company/create-company/create-company.component';
import {EmployeeProjectComponent} from './employee-project/employee-project.component';
import {EmployeeProjectRequestsComponent} from './employee-project-requests/employee-project-requests.component';
import {EmployeeMyProjectsComponent} from './employee-my-projects/employee-my-projects.component';
import {EmployeeFinishedProjectsComponent} from './employee-finished-projects/employee-finished-projects.component';
import {ChangePasswordComponent} from './change-password/change-password.component';
import {ForgotPasswordComponent} from './forgot-password/forgot-password.component';
import {Oauth2Component} from './oauth2/oauth2.component';
import {AdminProjectTypeComponent} from './admin-project-type/admin-project-type.component';
import {AdminSkillComponent} from './admin-skills/admin-skills.component';
import {AdminBlockIpComponent} from './admin-block-ip/admin-block-ip.component';
import {IpBlockedComponent} from './ip-blocked/ip-blocked.component';
import {ManageUsersEditComponent} from './manage-users-edit/manage-users-edit.component';
import {ManageUsersCreateComponent} from './manage-users-create/manage-users-create.component';
import {ViewCompanyComponent} from './view-company/view-company.component';
import {ProfileEditComponent} from './profile-edit/profile-edit.component';
import {ProfileCreateComponent} from './profile-create/profile-create.component';
import {ProfileRepresentComponent} from './profile-represent/profile-represent.component';
import {AdminProjectComponent} from './admin-project/admin-project.component';
import {MyProjectsComponent} from './my-projects/my-projects.component';
import {MembershipRequestsComponent} from './membership-requests/membership-requests.component';
import {EmployeeWorkspaceComponent} from './employee-workspace/employee-workspace.component';
import {AcceptionComponent} from './acception/acception.component';
import {AdminAllCompaniesComponent} from './admin-all-companies/admin-all-companies.component';
import {BlockedCompaniesComponent} from './blocked-companies/blocked-companies.component';
import {NotAcceptedCompanyComponent} from './not-accepted-company/not-accepted-company.component';
import {BlockedUsersCompComponent} from './blocked-users-comp/blocked-users-comp.component';
import {UpdateCompanyComponent} from './company/update-company/update-company.component';
import {CreateProjectComponent} from './project1/create-project/create-project.component';
import {UpdateProjectComponent} from './project1/update-project/update-project.component';
import { MyCompanyEmployerComponent } from './my-company-employer/my-company-employer.component';
import { MyCompanyEmployeeComponent } from './my-company-employee/my-company-employee.component';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full',
    canActivate: [AuthGuardService]

  },

  {
    path: 'employer/my-projects/view-project/:projectId',
    component: ViewProjectComponent,
    pathMatch: 'full'
  },
  {
    path: 'employer/my-projects',
    component: MyProjectsComponent,
    pathMatch: 'full'
  },
  {
    path: 'employer/my-company',
    component: MyCompanyEmployerComponent,
    pathMatch: 'full'
  },
  {
    path: 'login',
    component: LoginComponent,
    pathMatch: 'full'
  },
  {
    path: 'employer',
    component: EmployerComponent,
    pathMatch: 'full',
    canActivate: [AuthGuardService]
  },
  {
    path: 'registration',
    component: RegistrationComponent,
    pathMatch: 'full'
  },
  {
    path: 'change-password',
    component: ChangePasswordComponent,
    pathMatch: 'full'
  },
  {
    path: 'decision',
    component: DecisionScreenComponent,
    pathMatch: 'full',
    canActivate: [AuthGuardService]
  },
  {
    path: 'activate',
    component: ActivateAccountComponent,
    pathMatch: 'full'
  },
  {
    path: 'home',
    component: HomeComponent,
    pathMatch: 'prefix'
  },
  {
    path: 'employee',
    component: EmployeeComponent,
    pathMatch: 'full',
    canActivate: [AuthGuardService]
  },
  {
    path: 'search',
    component: SearchComponent,
    pathMatch: 'full'
  },
  {
    path: 'search/:name',
    component: SearchComponent,
    pathMatch: 'full'
  },
  {
    path: 'createCompany',
    component: CreateCompanyComponent,
    pathMatch: 'full'
  },
  {
    path: 'forgot-password',
    component: ForgotPasswordComponent,
    pathMatch: 'full'
  },
  {
    path: 'employee/project/:id',
    component: EmployeeProjectComponent,
    pathMatch: 'full'
  },
  {
    path: 'employee/requests',
    component: EmployeeProjectRequestsComponent,
    pathMatch: 'full'
  },
  {
    path: 'employee/my-projects',
    component: EmployeeMyProjectsComponent,
    pathMatch: 'full'
  },
  {
    path: 'employee/my-company',
    component: MyCompanyEmployeeComponent,
    pathMatch: 'full'
  },
  {
    path: 'employee/my-projects/finished',
    component: EmployeeFinishedProjectsComponent,
    pathMatch: 'full'
  },
  {
    path: 'employee/my-projects/finished/:type',
    component: EmployeeFinishedProjectsComponent,
    pathMatch: 'full'
  },
  {
    path: 'authorization/oauth2',
    component: Oauth2Component,
    pathMatch: 'full'
  },
  {
    path: 'errorP',
    component: ErrorPageComponent,
    pathMatch: 'full'
  },
  {
    path: 'accepting',
    component: AcceptionComponent,
    pathMatch: 'full'
  },
  {
    path: 'notAcceptedCompany',
    component: NotAcceptedCompanyComponent,
    pathMatch: 'full'
  },
  {
    path: 'profile/:id',
    component: ProfileRepresentComponent,
    pathMatch: 'full'
  },
  {
    path: 'blocked',
    component: BlockedUsersCompComponent,
    pathMatch: 'full'
  },
  {
    path: 'create-profile',
    component: ProfileCreateComponent,
    pathMatch: 'full',
    canActivate: [AuthGuardService]
  },
  {
    path: 'admin/allCompanies',
    component: AdminAllCompaniesComponent,
    pathMatch: 'full',
    canActivate: [AuthGuardService]
  },
  {
    path: 'employee/company/requests',
    component: MembershipRequestsComponent,
    pathMatch: 'full'
  },
  {
    path: 'admin/blockedCompanies',
    component: BlockedCompaniesComponent,
    pathMatch: 'full',
    canActivate: [AuthGuardService]
  },
  {
    path: 'admin/block-ip',
    component: AdminBlockIpComponent,
    pathMatch: 'full'
  },
  {
    path: 'employee/my-projects/:page',
    component: EmployeeMyProjectsComponent,
    pathMatch: 'full'
  },
  {
    path: 'admin/block-ip/:page',
    component: AdminBlockIpComponent,
    pathMatch: 'full'
  },
  {
    path: 'ip-blocked',
    component: IpBlockedComponent,
    pathMatch: 'full'
  },
  {
    path: 'admin/users/create',
    component: ManageUsersCreateComponent,
    pathMatch: 'full'
  },
  {
    path: 'admin/users/:page',
    component: ManageUsersEditComponent,
    pathMatch: 'full'
  },
  {
    path: 'admin/users',
    component: ManageUsersEditComponent,
    pathMatch: 'full'
  },
  {
    path: 'admin/projects/:id',
    component: AdminProjectComponent,
    pathMatch: 'full'
  },
  {
    path: 'admin/projects-type',
    component: AdminProjectTypeComponent,
    pathMatch: 'full'
  },
  {
    path: 'admin/skills',
    component: AdminSkillComponent,
    pathMatch: 'full'
  },
  {
    path: 'view/company/:id',
    component: ViewCompanyComponent,
    pathMatch: 'full'
  },
  {
    path: 'edit-profile',
    component: ProfileEditComponent,
    pathMatch: 'full',
    canActivate: [AuthGuardService]
  },
  {
    path: 'create-company',
    component: CreateCompanyComponent,
    pathMatch: 'full'
  },
  {
    path: 'update-company/:id',
    component: UpdateCompanyComponent,
    pathMatch: 'full'
  },
  {
    path: 'create-project',
    component: CreateProjectComponent,
    pathMatch: 'full'
  },
  {
    path: 'update-project/:id',
    component: UpdateProjectComponent,
    pathMatch: 'full'
  },
  {
    path: 'employee/workspace/:id',
    component: EmployeeWorkspaceComponent,
    pathMatch: 'full'
  },
  {
    path: '**',
    component: ErrorPageComponent
  }
];

@NgModule({
  declarations: [],
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

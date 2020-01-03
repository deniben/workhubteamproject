import {AcceptionComponent} from './acception/acception.component';
import {AdminAllCompaniesComponent} from './admin-all-companies/admin-all-companies.component';
import {BlockedCompaniesComponent} from './blocked-companies/blocked-companies.component';
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {NgModule} from '@angular/core';
import {ReactiveFormsModule, FormsModule} from '@angular/forms';
import {HttpClientModule, HTTP_INTERCEPTORS} from '@angular/common/http';
import {RouterModule} from '@angular/router';
import {AppComponent} from './app.component';
import {TokenInterceptor} from './api.interceptor';
import {TokenService} from './token.service';
import {StorageServiceModule} from 'ngx-webstorage-service';
import {RegistrationComponent} from './registration/registration.component';
import {AppRoutingModule} from './app-routing.module';
import {LoginComponent} from './login/login.component';
import {DecisionScreenComponent} from './decision-screen/decision-screen.component';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {ActivateAccountComponent} from './activate-account/activate-account.component';
import {EmployeeComponent} from './employee/employee.component';
import {SearchComponent} from './search/search.component';
import {jqxComboBoxModule} from 'jqwidgets-ng/jqxcombobox';
import {CommonModule} from '@angular/common';
import {HomeComponent} from './home/home.component';
import {ProjectService} from './services/project.service';
import {CompanyService} from './services/company.service';
import {AuthGuardService} from './auth-guard.service';
import {ErrorPageComponent} from './error-page/error-page.component';
import {FileloadComponent} from './fileload/fileload.component';
import {ProfileComponent} from './profile/profile.component';
import {EmployerComponent} from './employer/employer.component';
import {EmployerService} from './services/employer.service';
import {CreateCompanyComponent} from './company/create-company/create-company.component';
import {ProfileViewComponent} from './profile-view/profile-view.component';
import {SuiModule} from 'ng2-semantic-ui';
import { EmployeeProjectRequestsComponent } from './employee-project-requests/employee-project-requests.component';
import { EmployeeMyProjectsComponent } from './employee-my-projects/employee-my-projects.component';
import { EmployeeFinishedProjectsComponent } from './employee-finished-projects/employee-finished-projects.component';
import { ChangePasswordComponent } from './change-password/change-password.component';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';
import { AngularMultiSelectModule } from 'angular2-multiselect-dropdown';
import { SkillsMultiselectComponent } from './skills-multiselect/skills-multiselect.component';
import { PrMultiselectComponent } from './projecttypeselect/pt-multiselect.component';
import { CustomFormsModule } from 'ng2-validation';
import { Oauth2Component } from './oauth2/oauth2.component';
import { NgxPageScrollCoreModule } from 'ngx-page-scroll-core';
import {MatSelectModule} from '@angular/material/select';
import { AdminProjectTypeComponent } from './admin-project-type/admin-project-type.component';
import { AdminSkillComponent } from './admin-skills/admin-skills.component';
import { AdminBlockIpComponent } from './admin-block-ip/admin-block-ip.component';
import { IpBlockedComponent } from './ip-blocked/ip-blocked.component';
import { ManageUsersCreateComponent } from './manage-users-create/manage-users-create.component';
import { ManageUsersEditComponent } from './manage-users-edit/manage-users-edit.component';
import { CommentsComponent } from './comments/comments.component';
import {ViewProjectComponent} from './view-project/view-project.component';
import {EmployeeProjectComponent} from './employee-project/employee-project.component';
import {MyProjectsComponent} from './my-projects/my-projects.component';
import {ProfileEditComponent} from './profile-edit/profile-edit.component';
import {ProfileCreateComponent} from './profile-create/profile-create.component';
import {ProfileRepresentComponent} from './profile-represent/profile-represent.component';
import {AdminProjectComponent} from './admin-project/admin-project.component';
import { ViewCompanyComponent } from './view-company/view-company.component';
import {NotAcceptedCompanyComponent} from './not-accepted-company/not-accepted-company.component';
import { BlockedUsersCompComponent } from './blocked-users-comp/blocked-users-comp.component';
import { MembershipRequestsComponent } from './membership-requests/membership-requests.component';
import { NewProjectsNotificationsComponent } from './new-projects-notifications/new-projects-notifications.component';
import { EmployeeWorkspaceComponent } from './employee-workspace/employee-workspace.component';
import { WorkspaceComponent } from './workspace/workspace.component';
import {NgxPaginationModule} from 'ngx-pagination';
import { SchedulerModule } from '@progress/kendo-angular-scheduler';
import {ImageCropperModule} from 'ngx-image-cropper';
import { CompanyComponent } from './company/company.component';
import { UpdateCompanyComponent } from './company/update-company/update-company.component';
import { Project1Component } from './project1/project1.component';
import { UpdateProjectComponent } from './project1/update-project/update-project.component';
import { CreateProjectComponent } from './project1/create-project/create-project.component';
import { ToastrModule } from 'ngx-toastr';
import { MyCompanyComponent } from './my-company/my-company.component';
import { HeaderComponent } from './headerForAdmin/header.component';
import { HeaderForNonAuthComponent } from './header-for-non-auth/header-for-non-auth.component';
import { MyCompanyEmployeeComponent } from './my-company-employee/my-company-employee.component';
import { MyCompanyEmployerComponent } from './my-company-employer/my-company-employer.component';

@NgModule({
  declarations: [
    AppComponent,
    RegistrationComponent,
    LoginComponent,
    DecisionScreenComponent,
    ActivateAccountComponent,
    EmployeeComponent,
    SearchComponent,
    ProfileComponent,
    ProfileEditComponent,
    ProfileCreateComponent,
    ProfileRepresentComponent,
    HomeComponent,
    ErrorPageComponent,
    FileloadComponent,
    EmployerComponent,
    CreateCompanyComponent,
    ProfileViewComponent,
    EmployeeProjectComponent,
    EmployeeProjectRequestsComponent,
    EmployeeMyProjectsComponent,
    EmployeeFinishedProjectsComponent,
    ChangePasswordComponent,
    ForgotPasswordComponent,
    SkillsMultiselectComponent,
    Oauth2Component,
    AdminBlockIpComponent,
    IpBlockedComponent,
    ManageUsersCreateComponent,
    ManageUsersEditComponent,
    CommentsComponent,
    AcceptionComponent,
    AdminAllCompaniesComponent,
    BlockedCompaniesComponent,
    EmployerComponent,
    CreateCompanyComponent,
    ViewProjectComponent,
    ProfileViewComponent,
    EmployeeProjectComponent,
    MyProjectsComponent,
    EmployeeProjectRequestsComponent,
    EmployeeMyProjectsComponent,
    EmployeeFinishedProjectsComponent,
    ChangePasswordComponent,
    CreateCompanyComponent,
    ForgotPasswordComponent,
    SkillsMultiselectComponent,
    ProfileCreateComponent,
    ProfileRepresentComponent,
    PrMultiselectComponent,
    Oauth2Component,
    AdminProjectComponent,
    ViewCompanyComponent,
    AdminProjectTypeComponent,
    AdminSkillComponent,
    BlockedUsersCompComponent,
    NotAcceptedCompanyComponent,
    MembershipRequestsComponent,
    NewProjectsNotificationsComponent,
    EmployeeWorkspaceComponent,
    WorkspaceComponent,
    CompanyComponent,
    UpdateCompanyComponent,
    Project1Component,
    UpdateProjectComponent,
    CreateProjectComponent,
    MyCompanyComponent,
    HeaderComponent,
    HeaderForNonAuthComponent,
    MyCompanyEmployeeComponent,
    MyCompanyEmployerComponent
  ],
  imports: [
    BrowserModule,
    CommonModule,
    ReactiveFormsModule,
    HttpClientModule,
    StorageServiceModule,
    AppRoutingModule,
    SuiModule,
    ImageCropperModule,
    BrowserAnimationsModule,
    jqxComboBoxModule,
    CustomFormsModule,
    NgxPageScrollCoreModule,
    MatSelectModule,
    FormsModule,
    MatSelectModule,
    AngularMultiSelectModule,
    NgxPageScrollCoreModule,
    jqxComboBoxModule,
    CommonModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot(),
    NgxPaginationModule,
    SchedulerModule,
    RouterModule.forRoot([
      {path: '', component: MyProjectsComponent},
      {path: 'view-project/:projectId', component: MyProjectsComponent},
      {path: 'admin/project/:id', component: AdminAllCompaniesComponent},
      {path: 'admin/project/:id', component: BlockedCompaniesComponent},

    ]),

],

  providers: [
    AuthGuardService,
    CompanyService,
    ProjectService,
    TokenService,
    EmployerService,

    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    },
    // {
    // provide: ErrorHandler,
    // useClass: GlobalErrService
    // },
  ],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]

})
export class AppModule {
}

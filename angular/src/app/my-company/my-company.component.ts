import {Component, OnInit} from '@angular/core';
import {ProfileService} from '../services/profile.service';
import {CompanyService} from '../services/company.service';
import {Company} from '../models/company.model';
import {Router} from '@angular/router';

@Component({
  selector: 'app-my-company',
  templateUrl: './my-company.component.html',
  styleUrls: ['./my-company.component.css']
})
export class MyCompanyComponent implements OnInit {

  isOwner: boolean;
  company: Company;

  constructor(private profileService: ProfileService, private companyService: CompanyService, private router: Router) {
    this.company = new Company();
  }

  ngOnInit() {
    this.companyService.findMyCompany().subscribe((data) => {
      this.company = data;
    });


    this.profileService.isCurrentUserCompanyOwner().subscribe((data) => {
      this.isOwner = JSON.parse(data.toString());
    });
  }

  goToEditPage() {
    this.router.navigate(['/update-company/' + this.company.id]);
  }

}

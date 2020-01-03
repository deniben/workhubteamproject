import {Component, OnInit} from '@angular/core';
import {CompanyService} from '../services/company.service';
import {Router} from '@angular/router';
import {DomSanitizer} from '@angular/platform-browser';

@Component({
  selector: 'app-blocked-companies',
  templateUrl: './blocked-companies.component.html',
  styleUrls: ['./blocked-companies.component.css']
})
export class BlockedCompaniesComponent implements OnInit {
  descrip: string;
  companies = [];
  ownerr = [];
  pageNumber = 1;
  pagesCount = 0;

  constructor(private compServ: CompanyService, private router: Router, private sanitizer: DomSanitizer) {
  }

  ngOnInit() {
    this.allCompanies(this.pageNumber);
  }

  allCompanies(page: number) {
    console.log('method');
    this.compServ.findAllBlocked(page - 1).subscribe((data: any) => {
      this.companies = data.items;
      this.pagesCount = data.pagesCount;

    });

  }

  more(id: any) {
    this.router.navigate(['/admin/projects/' + id]);
  }

  unblockCompany(id: any, i: any) {
    console.log(id);
    this.compServ.unblockCompany(id).subscribe();
    this.companies.forEach((comp) => {
      if (comp.id === id) {
        this.companies.splice(i, 1);
      }
    });
  }
  change_page(number) {
    this.pageNumber = number;
    this.allCompanies(this.pageNumber);
  }
}

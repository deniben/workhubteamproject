import {Component, OnInit, ViewChild} from '@angular/core';
import {CompanyComponent} from '../company.component';

@Component({
  selector: 'app-update-company',
  templateUrl: './update-company.component.html',
  styleUrls: ['./update-company.component.css']
})
export class UpdateCompanyComponent implements OnInit {

  @ViewChild(CompanyComponent, {static: false})
  public company: CompanyComponent;

  constructor() { }

  ngOnInit() {

  }

}

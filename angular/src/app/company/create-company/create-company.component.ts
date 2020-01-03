import { Component, OnInit, ViewChild } from '@angular/core';
import {CompanyComponent} from '../company.component';

@Component({
  selector: 'app-create-company',
  templateUrl: './create-company.component.html',
  styleUrls: ['./create-company.component.css']
})
export class CreateCompanyComponent implements OnInit {

  @ViewChild(CompanyComponent, {static: false})
  public company: CompanyComponent;

  constructor() { }

  ngOnInit() {

  }
}

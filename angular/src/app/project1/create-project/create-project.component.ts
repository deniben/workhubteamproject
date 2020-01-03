import {Component, OnInit, ViewChild} from '@angular/core';
import {Project1Component} from '../project1.component';

@Component({
  selector: 'app-create-project',
  templateUrl: './create-project.component.html',
  styleUrls: ['./create-project.component.css']
})
export class CreateProjectComponent implements OnInit {

  @ViewChild(Project1Component, {static: false})
  public project1: Project1Component;

  constructor() { }

  ngOnInit() {
  }

}

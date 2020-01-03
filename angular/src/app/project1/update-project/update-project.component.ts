import {Component, OnInit, ViewChild} from '@angular/core';
import {Project1Component} from '../project1.component';

@Component({
  selector: 'app-update-project',
  templateUrl: './update-project.component.html',
  styleUrls: ['./update-project.component.css']
})
export class UpdateProjectComponent implements OnInit {

  @ViewChild(Project1Component, {static: false})
  public project1: Project1Component;

  constructor() { }

  ngOnInit() {
  }

}

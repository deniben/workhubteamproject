import { Component, OnInit } from '@angular/core';
import { WorkspaceComponent } from 'src/app/workspace/workspace.component'
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-employee-workspace',
  templateUrl: './employee-workspace.component.html',
  styleUrls: ['./employee-workspace.component.css']
})
export class EmployeeWorkspaceComponent implements OnInit {

  project_id;
  project_name;

  constructor(private activatedRoute : ActivatedRoute, private router : Router, private client : HttpClient) { }

  ngOnInit() {
    this.project_name = '';
    this.activatedRoute.paramMap.subscribe(data => {
      if(data.get('id')) {
        this.project_id = data.get('id');
        this.client.get<any>(environment.api_url + '/projects/' + this.project_id).subscribe(data => {
          this.project_name = data.name;
        });
      } else {
        this.router.navigate(['employee/my-projects']);
      }
    });
  }

}

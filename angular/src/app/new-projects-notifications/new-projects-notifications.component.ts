import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-new-projects-notifications',
  templateUrl: './new-projects-notifications.component.html',
  styleUrls: ['./new-projects-notifications.component.css']
})
export class NewProjectsNotificationsComponent implements OnInit {

  new_projects;

  constructor(private client: HttpClient) {
    this.client.get(environment.api_url + '/notifications/new-projects').subscribe(data => {
      this.new_projects = Number(data);
    });
  }

  ngOnInit() {
  }

}

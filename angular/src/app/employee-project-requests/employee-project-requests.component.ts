import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { ToastrService } from "ngx-toastr";

@Component({
  selector: 'app-employee-project-requests',
  templateUrl: './employee-project-requests.component.html',
  styleUrls: ['./employee-project-requests.component.css']
})
export class EmployeeProjectRequestsComponent implements OnInit {

  applications;
  successMessage;
  errorMessage;

  constructor(private client : HttpClient,    private toastr: ToastrService) {
    this.successMessage = '';
    this.errorMessage = '';
  }

  ngOnInit() {
    this.client.get<any>(environment.api_url + '/projects/current/requests/all').subscribe(data => {
      this.applications = data;
      console.log(data)
    });
  }

  deleteApplication(element) {
    this.client.delete(environment.api_url + '/projects/' + element.projectId + '/requests', {responseType : 'text'}).subscribe(data => {
      this.successMessage = 'Application deleted';
      this.errorMessage = '';

      // this.toastr.info(
      //   "Application deleted ",
      //   "INFO",
      //   { progressBar: true, timeOut: 5000 }
      // );
      let index: number = this.applications.indexOf(element, 0);

      if (index >= 0) {
        this.applications.splice(index, 1);
      }

      this.applications


    },
    err => {
      this.errorMessage = JSON.parse(err.error).message;
      this.successMessage = '';
    });
  }

}

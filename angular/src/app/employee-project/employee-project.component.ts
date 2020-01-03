import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { DomSanitizer, SafeResourceUrl, SafeUrl} from '@angular/platform-browser';
import { Router } from "@angular/router";
import { environment } from 'src/environments/environment';
import { CommentsComponent } from 'src/app/comments/comments.component';

@Component({
  selector: 'app-employee-project',
  templateUrl: './employee-project.component.html',
  styleUrls: ['./employee-project.component.css']
})
export class EmployeeProjectComponent implements OnInit {

  project;
  colorClass;
  alreadyApplied;
  message;
  applySent;
  cancelSent;

  constructor(private activatedRoute : ActivatedRoute, private router : Router,
     private client : HttpClient, private sanitizer : DomSanitizer) { }

  ngOnInit() {

    this.project = {};
    this.colorClass = 'ui black statistic';
    this.alreadyApplied = false;

    this.activatedRoute.paramMap.subscribe(data => {

      if (data.get('id')) {

        let project_id = data.get('id');

        this.client.get<any>(environment.api_url + '/projects/' + project_id).subscribe((data) => {

              let img = this.sanitizer.bypassSecurityTrustUrl(data.photo);

              let rate = []

              if (data.companyCreator.avgMark > 0) {
                for (let i  = 0; i < (data.companyCreator.avgMark - 1); i++) {
                  rate.push(i);
                }
              }

              this.project = {
                id : data.id,
                projectType : data.projectType.name,
                name : data.name,
                description : data.description,
                companyName : data.companyCreator.name,
                budget : data.budget,
                photo : img,
                percentage : data.skillsMatch,
                companyRate : rate,
                skillSet : data.skillSet,
                expiryDate : data.expiryDate
              };

              if (data.skillsMatch < 20) {
                this.colorClass = 'ui red statistic';
              } else if (data.skillsMatch >= 20 && data.skillsMatch < 70) {
                this.colorClass = 'ui yellow statistic';
              } else {
                this.colorClass = 'ui green statistic';
              }

              this.client.get(environment.api_url + '/projects/' + this.project.id + '/requests').subscribe(data => {
                this.alreadyApplied = data;
              });

        });

      } else {
        this.router.navigate(['employee']);
      }


    });

  }

  applyForProject(id) {

    this.client.post(environment.api_url + '/projects/' + id + '/requests', {}, {responseType : 'text'}).subscribe((data) => {
      this.alreadyApplied = true;
      this.message = '';
    },
    err => {
      this.message = JSON.parse(err.error).message;
    });

  }

  cancelApplication(id) {

    this.client.delete(environment.api_url + '/projects/' + id + '/requests', {responseType : 'text'}).subscribe(data => {
      this.alreadyApplied = false;
      this.message = '';
    },
    err => {
      this.message = JSON.parse(err.error).message;
    });

  }

}

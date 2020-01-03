import {Component, OnInit} from '@angular/core';
import {ApplicationsService} from '../services/applications.service';
import {HttpClient, HttpParams} from '@angular/common/http';
import {DomSanitizer} from '@angular/platform-browser';
import {Profile} from '../models/profile.models';
@Component({
 selector: 'app-acception',
 templateUrl: './acception.component.html',
 styleUrls: ['./acception.component.css']
})
export class AcceptionComponent implements OnInit {
 applications = [];
 sizeApp: any;
 profiles: Array<Profile> = new Array<Profile>();
 pageNumber = 1;
 pagesCount = 0;
 constructor(private appService: ApplicationsService, private httpClient: HttpClient, private sanitizer: DomSanitizer) {
 }
 ngOnInit() {
   this.showApps(this.pageNumber);
 }
 rejectMembers(id: any,i : number) {
   this.appService.reject(id);
   this.profiles.splice(i  , 1);
   this.pagesCount--;
   (this.pageNumber > 1) ?   this.change_page(this.pageNumber - 1) : this.change_page(this.pageNumber);
 }
 submitMembers(id: any,i : number) {
   this.appService.submit(id);
   this.profiles.splice(i  , 1);
   this.pagesCount--;
   (this.pageNumber > 1) ?  this.change_page(this.pageNumber - 1) : this.change_page(this.pageNumber);

 }
 showApps(pageNum: any) {
   this.httpClient.get<any>('http://localhost:8080/WorkHub/companies/applications/' + (pageNum - 1)).subscribe(data => {
     console.log(data);
     this.profiles = data.items;
     this.pagesCount = data.pagesCount;
   });
 }
 change_page(number) {
   this.pageNumber = number;
   this.showApps(this.pageNumber);
 }
}

import { Component, Input, OnInit, Inject, ElementRef, ViewChild } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { environment } from 'src/environments/environment';
import { Router, ActivatedRoute } from "@angular/router";
import {Location} from '@angular/common';
import { SchedulerEvent, SchedulerComponent } from '@progress/kendo-angular-scheduler';
import * as fileSaver from 'file-saver';

@Component({
  selector: 'app-workspace',
  templateUrl: './workspace.component.html',
  styleUrls: ['./workspace.component.css']
})
export class WorkspaceComponent implements OnInit {

  files;

  file_form;

  err_message;
  message;

  event_message;
  event_err_message;

  page_number;
  pages_count;

  page_size;

  selectedDate;
  events;

  event_form;

  event_details;

  min_date;
  max_date;

  current_month;
  cached_dates;

  @Input() project_id : Number;

  constructor(private client : HttpClient, private router : Router,
              private location : Location, private activatedRoute : ActivatedRoute,
              private formBuilder : FormBuilder) {
    this.page_size = 5;
    this.page_number = 1;
    this.files = [];
    this.events = [];
    this.cached_dates = [];

    let date = new Date();

    let current_year = new Date().getFullYear();

    this.min_date = new Date(current_year + "-01-01");
    this.max_date = new Date(current_year + "-12-31");

    this.err_message = '';
    this.message = '';
    this.selectedDate = date;
    this.event_form = this.formBuilder.group({
      id: [''],
      start: [date, Validators.required],
      end: [date, Validators.required],
      isAllDay: [false],
      title: ['', Validators.required],
      description: ['', Validators.required],
      recurrenceId: [''],
      recurrenceException: [''],
      roomId: [0],
      attendees: [0]
    });
  }

  ngOnInit() {
    this.activatedRoute.queryParams.subscribe(data => {
        if(data['page']) {
          this.page_number = data['page'];
        }
        this.current_month = new Date().getMonth() + 1;
        this.cached_dates.push(this.current_month);
        this.refresh_documents_list();
        this.refresh_events();
    });

  }

  close_dialog(event) {
    let scheduler = <SchedulerComponent>event.sender;
    scheduler.closeEvent();
  }

  add_event(event) {
    let scheduler = <SchedulerComponent>event.sender;
    this.event_form.patchValue({
      start : this.parseAdjust(String(event.start).split('(')[0]),
      end : this.parseAdjust(String(event.end).split('(')[0])
    });
    scheduler.addEvent(this.event_form);
  }

  show_details(event) {
    this.event_details = {
      ownerName : event.event.dataItem.owner_name,
      title : event.event.dataItem.title,
      description : event.event.dataItem.description
    };
  }

  date_changed(event) {

    let selected_month = event.selectedDate.getMonth() + 1;

    if(this.cached_dates.indexOf(selected_month) > -1) {
      return;
    }

    this.current_month = selected_month;
    this.cached_dates.push(this.current_month);

    this.refresh_events();
  }

  save_event(event) {

    let data = event.formGroup.value;

    if(!data.title || data.title.length === 0) {
      return;
    }

    this.client.post(environment.api_url + '/workspace/events', {
      title : data.title,
      description : data.description,
      startTime : this.formatDate(data.start),
      endTime :  this.formatDate(data.end),
      allDay : data.allDay,
      projectId : this.project_id
    }, { responseType : 'text' }).subscribe(data => {
      this.close_dialog(event);
      this.refresh_events();
    }, err => {
      console.log(JSON.parse(err.error).message);
    });
  }

  remove_event(event) {
    this.client.delete(environment.api_url + '/workspace/events/' + event.dataItem.id, {responseType : 'text'}).subscribe(data => {
      this.event_message = 'Successfully deleted';
      this.event_err_message = '';
      this.events = this.events.splice(this.events.indexOf(event), 0);
      this.refresh_events();
    }, err => {
      this.event_message = '';
      this.event_err_message = JSON.parse(err.error).message;
    });
  }

  event_clear_messages() {
    this.event_message = '';
    this.event_err_message = '';
  }

  formatDate(date) {
    return date.getFullYear() + '-' + this.formatDateUnit(date.getMonth() + 1) + '-' + this.formatDateUnit(date.getDate()) +
     'T' + this.formatDateUnit(date.getHours()) + ':' + this.formatDateUnit(date.getMinutes()) + ':' + this.formatDateUnit(date.getSeconds());
  }

  formatDateUnit(unit) {
    if(unit < 10) {
      return '0' + unit;
    }
    return unit;
  }

  parseAdjust(date_str) {
    let date = new Date(date_str);
    date.setFullYear(this.selectedDate.getFullYear());
    return date;
  }

  name_preprocess(name) {
    if(name.length > 29) {
      let parts = name.split('.');
      let left_size = 29 - parts[1].length;
      let left_part : String = parts[0];
      name = left_part.slice(0, left_size) + '...' + parts[1];
    }
    return name;
  }

  refresh_documents_list() {
    this.client.get<any>(environment.api_url + '/workspace/documents/' + this.project_id + '?page=' + (Number(this.page_number) - 1)).subscribe(data => {
      this.files = data.items;
      this.pages_count = data.pagesCount;
    }, err => {
      this.message = '';
      this.err_message = JSON.parse(err.error).message
    });

  }

  refresh_events() {
    this.client.get<any>(environment.api_url + '/workspace/events/' + this.project_id + '?month=' + this.current_month).subscribe(data => {

      let data_list = data.map(dataItem => (
      <SchedulerEvent> {
          id: dataItem.id,
          owner_name : dataItem.ownerName,
          start: this.parseAdjust(dataItem.startTime),
          startTimezone: null,
          end: this.parseAdjust(dataItem.endTime),
          endTimezone: null,
          isAllDay: false,
          title: dataItem.title,
          description: dataItem.description,
          recurrenceRule: "",
          recurrenceId: null,
          recurrenceException: null,
          roomId: 0,
          attendees: [0]
          }
      ));

      this.events = this.events.concat(data_list);

    });
  }

  change_page_docs(event) {
    this.page_number = event;
    this.location.go('/employee/workspace/' + this.project_id + '?page=' + this.page_number);
    this.refresh_documents_list();
  }

  download(name) {

    let options : Object = { observe: 'response', responseType : 'arraybuffer' };

    this.client.get<any>(environment.api_url + '/workspace/download/documents?name=' + name, options).subscribe(data => {

      let file_name = data.headers.get('filename');

      const blob = new Blob([data.body], {type : 'application/octet-stream'});

      fileSaver.saveAs(blob, file_name);

    }, err => this.err_message = JSON.parse(err.error).message);
  }

  delete(name) {
    this.client.delete(environment.api_url + '/workspace/documents?fileName=' + name, { responseType : 'text' }).subscribe(data => {
      this.message = data;
      this.err_message = '';
      this.refresh_documents_list();
    }, err => {
      this.message = '';
      this.err_message = JSON.parse(err.error).message;
    });
  }

  load_document(event) {

    if(event.target.files && event.target.files.length > 0) {

      let file = event.target.files[0];

      if(Number(file.size) > 10485760) {
        this.message = '';
        this.err_message = 'Maximum allowed file size is 10 megabytes';
        return;
      }

      let formData : FormData = new FormData();

      formData.append('multipartFile', file, file.name);
      formData.append('projectId', this.project_id + '');

      this.client.post(environment.api_url + '/workspace/documents', formData, { responseType : 'text' }).subscribe(data => {
        this.refresh_documents_list();
        this.message = data;
        this.err_message = '';
      }, err => {
        this.message = '';
        this.err_message = JSON.parse(err.error).message;
      });

    }

  }

}

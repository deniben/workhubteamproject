import { Component, OnInit, Input } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {  Inject } from '@angular/core';
import { DOCUMENT } from '@angular/common';
import { PageScrollService } from 'ngx-page-scroll-core';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-comments',
  templateUrl: './comments.component.html',
  styleUrls: ['./comments.component.css']
})
export class CommentsComponent implements OnInit {


    applySent;
    cancelSent;
    comments;
    comment_text;
    current_date;

    page_number;
    pages_count;

    reply_id;

    do_refresh;
    is_active;

    my_id;
    my_role;

    request;

    @Input() project_id : Number;

  constructor(private client : HttpClient, private pageScrollService : PageScrollService,
      @Inject(DOCUMENT) private document : any) { }

  ngOnInit() {

    this.comments = [];
    this.current_date = Date.now();
    this.reply_id = '';
    this.page_number = 0;
    this.pages_count = 0;
    this.is_active = false;

    this.client.get<any>(environment.api_url + '/profiles/data').subscribe(data => {
      this.my_id = data.id;
      this.my_role = data.admin;
    });

    this.do_refresh = setInterval(() => {
      this.refresh(this.project_id);
    }, 500);

  }

  delete(comment) {
    this.comments.splice(this.comments.indexOf(comment), 1);
    this.request.unsubscribe();
    this.delete_query(comment.id);
  }

  delete_inner(outer_comment, inner_comment) {
    let outer_index = this.comments.indexOf(outer_comment);
    outer_comment.replies.splice(outer_comment.replies.indexOf(inner_comment), 1);
    this.comments.splice(outer_index, 1, outer_comment);
    this.request.unsubscribe();
    this.delete_query(outer_comment.id + ':' + inner_comment.id);
  }

  delete_query(id) {
    this.client.delete(environment.api_url + '/comments/' + id, {responseType : 'text'}).subscribe();
  }

  ngOnDestroy() {
    if (this.do_refresh) {
      clearInterval(this.do_refresh);
    }
  }

  next() {

    if(this.page_number < (this.pages_count - 1)) {
      this.page_number = this.page_number + 1;
      this.refresh(this.project_id);
    }

  }

  previous() {
    if(this.page_number > 0) {
      this.page_number = this.page_number - 1;
      this.refresh(this.project_id);
    }
  }

  convert_date(date) {
    return new Date(date.year, date.monthValue - 1, date.dayOfMonth, date.hour, date.minute);
  }

  time_diff(date_a, date_b) {
    let millis = date_a - date_b;
    return Math.abs((((millis / 1000) / 60 ) / 60 ) / 24);
  }

  send_comment() {

    if(!this.comment_text || this.comment_text.length === 0 || this.comment_text.length > 256 || this.project_id === null) {
      return;
    }

    let params = {
      text : this.comment_text,
      projectId : this.project_id
    };

    if(this.reply_id && this.reply_id.length > 5) {
        params['replyId'] = this.reply_id;
    }

    const option : Object = {
      responseType : 'text'
    };

    this.client.post<any>(environment.api_url + '/comments/', params, option).subscribe(data => {
      this.refresh(this.project_id);
    });

    this.comment_text = '';
    this.reply_id = '';

  }

  refresh(id) {

    if(id && !this.is_active) {
      this.is_active = true;
      clearInterval(this.do_refresh);
      this.do_refresh = setInterval(() => {
        this.refresh(this.project_id);
      }, 10000);
    }

    if(!id) {
      return;
    }

    this.request = this.client.get<any>(environment.api_url + '/comments/' + id + '/pages/' + this.page_number).subscribe(data => {
      this.comments = data.items;
      this.pages_count = data.pagesCount;
    });
  }

  reply(id) {
    this.reply_id = id;
    this.pageScrollService.scroll({
      document : this.document,
      scrollTarget: '#commentsField'
    });
  }

  cancel_reply() {
    this.reply_id = '';
  }

}

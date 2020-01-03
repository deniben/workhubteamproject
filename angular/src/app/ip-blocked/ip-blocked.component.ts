import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-ip-blocked',
  templateUrl: './ip-blocked.component.html',
  styleUrls: ['./ip-blocked.component.css']
})
export class IpBlockedComponent implements OnInit {

  reason;

  constructor(private activatedRoute : ActivatedRoute) {
    this.reason = '';
  }

  ngOnInit() {
    this.activatedRoute.queryParams.subscribe(data => {
      if(data['reason']) {
        this.reason = data['reason'];
      }
    });
  }

}

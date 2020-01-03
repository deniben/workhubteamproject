import { Component } from '@angular/core';
import { Router } from '@angular/router';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],

})
export class AppComponent {
check: boolean;
checkActive: boolean;

  constructor(public router: Router) 
  {
    this.check = false;
  

    if((this.router.url.includes('home')) || (this.router.url.includes('login')) || (this.router.url.includes('registration') ))
    {
    this.check = false;

    }
    else if ((this.router.url.includes('admin')))
    { 
      this.check = true;
    }
  }
}

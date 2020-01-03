import { Component, OnInit } from '@angular/core';
import { TokenService } from '../token.service';

@Component({
  selector: 'app-header-for-non-auth',
  templateUrl: './header-for-non-auth.component.html',
  styleUrls: ['./header-for-non-auth.component.css']
})
export class HeaderForNonAuthComponent implements OnInit {
  check: boolean;
  constructor(private tokenService : TokenService) { }

  ngOnInit() {
    this.check = false;

  if (this.tokenService.getTokenIfExists()  == null)
  {
      this.check = true;
  }
   
  }

}

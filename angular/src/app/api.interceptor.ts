import { Injectable } from '@angular/core';
import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest } from '@angular/common/http';
import { TokenService } from './token.service';
import { Observable } from 'rxjs';


@Injectable({providedIn: 'root'})
export class TokenInterceptor implements HttpInterceptor {

  tokenSevice;

  constructor(private tokenSeviceP : TokenService) {
    this.tokenSevice = tokenSeviceP;
  }

  intercept(req : HttpRequest<any>, next : HttpHandler) : Observable<HttpEvent<any>> {
    var token = this.tokenSevice.getTokenIfExists();
    let tokenReq = req;

    if (token) {
      tokenReq = req.clone({ headers : req.headers.set('Authorization', token)});
    }

    return next.handle(tokenReq);
  }

}

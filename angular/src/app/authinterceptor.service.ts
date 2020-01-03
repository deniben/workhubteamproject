import { Injectable } from '@angular/core';
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpRequest} from "@angular/common/http";
import {Observable, pipe, throwError, UnaryFunction} from "rxjs";
import {Router} from "@angular/router";
import {catchError} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class AuthinterceptorService {

  constructor(private router: Router) {
  }

  intercept(req : HttpRequest<any>, next : HttpHandler) : UnaryFunction<unknown, unknown> {

    return pipe(
        catchError((error: HttpErrorResponse) => {

          if (error.status === 404 || error.status === 405 || error.status === 500) {
            let errorMessage = '';
            if (error.error instanceof ErrorEvent) {

              errorMessage = `Error: ${error.error.message}`;
            } else {
              errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
            }
            this.router.navigate(['/errorP']);
            return throwError(errorMessage);
          }
          return ;
        })
      );

  }
}

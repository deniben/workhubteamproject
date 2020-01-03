import { ErrorHandler ,Injectable, Injector} from '@angular/core';
import { Router } from '@angular/router';

@Injectable()
export class GlobalErrService implements ErrorHandler{

   constructor(private injector: Injector) {   
    }  
  handleError(error) { 
    console.error('An error occurred:', error.message);  
    console.error(error); 
    const router = this.injector.get(Router);
  router.navigate(['/errorP']);
 }  
 
}

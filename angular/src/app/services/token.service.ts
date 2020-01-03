import { Injectable, Inject } from '@angular/core';
import { LOCAL_STORAGE, StorageService } from 'ngx-webstorage-service';

@Injectable({providedIn: 'root'})

export class TokenService {

  STORAGE_KEY = 'jwt_token';

  constructor(@Inject(LOCAL_STORAGE) private storage : StorageService) {

  }

  getTokenIfExists() : string {
    var token = this.storage.get(this.STORAGE_KEY);
    if (token && token.length > 3) {
      return token;
    }
    return null;
  }

  setToken(tokenStr) {
    this.storage.set(this.STORAGE_KEY, tokenStr);
  }

}

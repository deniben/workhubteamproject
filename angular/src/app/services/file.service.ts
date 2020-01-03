import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {URL} from '../../constants';
import {PhotoTypes} from '../PhotoTypes';
import 'rxjs-compat/add/operator/map';

@Injectable({
  providedIn: 'root'
})
export class FileService {

  constructor(private http: HttpClient) {
  }

  fileUpload(file: FormData, typeOfPhoto: PhotoTypes) {
    const url = URL + '/download-file/type/' + typeOfPhoto.enumName;
    return this.http.post(url, file);
  }

}

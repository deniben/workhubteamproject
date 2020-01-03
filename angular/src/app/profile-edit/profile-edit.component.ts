import {Component, OnInit} from '@angular/core';
import {Actions} from '../Actions';

@Component({
  selector: 'app-profile-edit',
  templateUrl: './profile-edit.component.html',
  styleUrls: ['./profile-edit.component.css']
})
export class ProfileEditComponent implements OnInit {
  Actions: Actions;

  constructor() {
  }

  ngOnInit() {
  }

}

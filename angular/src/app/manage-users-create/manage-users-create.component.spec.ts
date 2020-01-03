import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageUsersCreateComponent } from './manage-users-create.component';

describe('ManageUsersCreateComponent', () => {
  let component: ManageUsersCreateComponent;
  let fixture: ComponentFixture<ManageUsersCreateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ManageUsersCreateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ManageUsersCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

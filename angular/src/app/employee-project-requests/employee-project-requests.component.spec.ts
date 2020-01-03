import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeeProjectRequestsComponent } from './employee-project-requests.component';

describe('EmployeeProjectRequestsComponent', () => {
  let component: EmployeeProjectRequestsComponent;
  let fixture: ComponentFixture<EmployeeProjectRequestsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EmployeeProjectRequestsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EmployeeProjectRequestsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

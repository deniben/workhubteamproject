import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeeFinishedProjectsComponent } from './employee-finished-projects.component';

describe('EmployeeFinishedProjectsComponent', () => {
  let component: EmployeeFinishedProjectsComponent;
  let fixture: ComponentFixture<EmployeeFinishedProjectsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EmployeeFinishedProjectsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EmployeeFinishedProjectsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

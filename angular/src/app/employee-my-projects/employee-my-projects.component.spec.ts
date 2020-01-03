import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeeMyProjectsComponent } from './employee-my-projects.component';

describe('EmployeeMyProjectsComponent', () => {
  let component: EmployeeMyProjectsComponent;
  let fixture: ComponentFixture<EmployeeMyProjectsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EmployeeMyProjectsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EmployeeMyProjectsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

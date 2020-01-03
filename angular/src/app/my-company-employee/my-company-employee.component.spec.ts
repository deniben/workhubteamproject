import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MyCompanyEmployeeComponent } from './my-company-employee.component';

describe('MyCompanyEmployeeComponent', () => {
  let component: MyCompanyEmployeeComponent;
  let fixture: ComponentFixture<MyCompanyEmployeeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MyCompanyEmployeeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MyCompanyEmployeeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

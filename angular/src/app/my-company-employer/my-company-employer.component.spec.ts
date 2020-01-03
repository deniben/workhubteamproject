import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MyCompanyEmployerComponent } from './my-company-employer.component';

describe('MyCompanyEmployerComponent', () => {
  let component: MyCompanyEmployerComponent;
  let fixture: ComponentFixture<MyCompanyEmployerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MyCompanyEmployerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MyCompanyEmployerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NotAcceptedCompanyComponent } from './not-accepted-company.component';

describe('NotAcceptedCompanyComponent', () => {
  let component: NotAcceptedCompanyComponent;
  let fixture: ComponentFixture<NotAcceptedCompanyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NotAcceptedCompanyComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NotAcceptedCompanyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

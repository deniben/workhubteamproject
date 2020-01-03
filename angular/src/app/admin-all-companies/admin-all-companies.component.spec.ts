import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminAllCompaniesComponent } from './admin-all-companies.component';

describe('AdminAllCompaniesComponent', () => {
  let component: AdminAllCompaniesComponent;
  let fixture: ComponentFixture<AdminAllCompaniesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdminAllCompaniesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminAllCompaniesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

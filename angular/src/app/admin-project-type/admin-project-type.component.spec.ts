import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminProjectTypeComponent } from './admin-project-type.component';

describe('AdminProjectTypeComponent', () => {
  let component: AdminProjectTypeComponent;
  let fixture: ComponentFixture<AdminProjectTypeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdminProjectTypeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminProjectTypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

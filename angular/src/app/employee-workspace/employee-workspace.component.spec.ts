import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeeWorkspaceComponent } from './employee-workspace.component';

describe('EmployeeWorkspaceComponent', () => {
  let component: EmployeeWorkspaceComponent;
  let fixture: ComponentFixture<EmployeeWorkspaceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EmployeeWorkspaceComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EmployeeWorkspaceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

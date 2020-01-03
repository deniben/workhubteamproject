import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewProjectsNotificationsComponent } from './new-projects-notifications.component';

describe('NewProjectsNotificationsComponent', () => {
  let component: NewProjectsNotificationsComponent;
  let fixture: ComponentFixture<NewProjectsNotificationsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NewProjectsNotificationsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewProjectsNotificationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfileRepresentComponent } from './profile-represent.component';

describe('ProfileRepresentComponent', () => {
  let component: ProfileRepresentComponent;
  let fixture: ComponentFixture<ProfileRepresentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProfileRepresentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProfileRepresentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

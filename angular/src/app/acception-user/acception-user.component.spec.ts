import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AcceptionUserComponent } from './acception-user.component';

describe('AcceptionUserComponent', () => {
  let component: AcceptionUserComponent;
  let fixture: ComponentFixture<AcceptionUserComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AcceptionUserComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AcceptionUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

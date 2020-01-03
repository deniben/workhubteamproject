import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IpBlockedComponent } from './ip-blocked.component';

describe('IpBlockedComponent', () => {
  let component: IpBlockedComponent;
  let fixture: ComponentFixture<IpBlockedComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IpBlockedComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IpBlockedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

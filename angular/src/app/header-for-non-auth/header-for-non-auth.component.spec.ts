import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HeaderForNonAuthComponent } from './header-for-non-auth.component';

describe('HeaderForNonAuthComponent', () => {
  let component: HeaderForNonAuthComponent;
  let fixture: ComponentFixture<HeaderForNonAuthComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HeaderForNonAuthComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HeaderForNonAuthComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

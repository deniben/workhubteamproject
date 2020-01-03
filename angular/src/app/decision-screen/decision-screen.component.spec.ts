import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DecisionScreenComponent } from './decision-screen.component';

describe('DecisionScreenComponent', () => {
  let component: DecisionScreenComponent;
  let fixture: ComponentFixture<DecisionScreenComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DecisionScreenComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DecisionScreenComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

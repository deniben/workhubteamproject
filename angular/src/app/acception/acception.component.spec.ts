import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AcceptionComponent } from './acception.component';

describe('AcceptionComponent', () => {
  let component: AcceptionComponent;
  let fixture: ComponentFixture<AcceptionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AcceptionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AcceptionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

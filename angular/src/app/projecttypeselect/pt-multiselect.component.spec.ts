import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PrMultiselectComponent } from './pt-multiselect.component';

describe('PrMultiselectComponent', () => {
  let component: PrMultiselectComponent;
  let fixture: ComponentFixture<PrMultiselectComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PrMultiselectComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PrMultiselectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

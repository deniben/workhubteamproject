import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BlockedCompaniesComponent } from './blocked-companies.component';

describe('BlockedCompaniesComponent', () => {
  let component: BlockedCompaniesComponent;
  let fixture: ComponentFixture<BlockedCompaniesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BlockedCompaniesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BlockedCompaniesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

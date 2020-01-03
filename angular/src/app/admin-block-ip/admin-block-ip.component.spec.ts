import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminBlockIpComponent } from './admin-block-ip.component';

describe('AdminBlockIpComponent', () => {
  let component: AdminBlockIpComponent;
  let fixture: ComponentFixture<AdminBlockIpComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdminBlockIpComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminBlockIpComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BlockedUsersCompComponent } from './blocked-users-comp.component';

describe('BlockedUsersCompComponent', () => {
  let component: BlockedUsersCompComponent;
  let fixture: ComponentFixture<BlockedUsersCompComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BlockedUsersCompComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BlockedUsersCompComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

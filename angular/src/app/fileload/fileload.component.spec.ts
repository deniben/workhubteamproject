import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FileloadComponent } from './fileload.component';

describe('FileloadComponent', () => {
  let component: FileloadComponent;
  let fixture: ComponentFixture<FileloadComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FileloadComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FileloadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

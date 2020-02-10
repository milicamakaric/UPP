import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RadPdfComponent } from './rad-pdf.component';

describe('RadPdfComponent', () => {
  let component: RadPdfComponent;
  let fixture: ComponentFixture<RadPdfComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RadPdfComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RadPdfComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

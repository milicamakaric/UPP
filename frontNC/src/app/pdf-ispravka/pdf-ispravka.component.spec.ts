import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PdfIspravkaComponent } from './pdf-ispravka.component';

describe('PdfIspravkaComponent', () => {
  let component: PdfIspravkaComponent;
  let fixture: ComponentFixture<PdfIspravkaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PdfIspravkaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PdfIspravkaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

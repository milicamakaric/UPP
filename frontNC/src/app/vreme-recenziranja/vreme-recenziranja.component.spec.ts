import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VremeRecenziranjaComponent } from './vreme-recenziranja.component';

describe('VremeRecenziranjaComponent', () => {
  let component: VremeRecenziranjaComponent;
  let fixture: ComponentFixture<VremeRecenziranjaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VremeRecenziranjaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VremeRecenziranjaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

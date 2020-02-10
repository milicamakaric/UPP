import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RadPregledComponent } from './rad-pregled.component';

describe('RadPregledComponent', () => {
  let component: RadPregledComponent;
  let fixture: ComponentFixture<RadPregledComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RadPregledComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RadPregledComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

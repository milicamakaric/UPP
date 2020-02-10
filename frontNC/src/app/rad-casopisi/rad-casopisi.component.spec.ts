import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RadCasopisiComponent } from './rad-casopisi.component';

describe('RadCasopisiComponent', () => {
  let component: RadCasopisiComponent;
  let fixture: ComponentFixture<RadCasopisiComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RadCasopisiComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RadCasopisiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NoviRecenzentiComponent } from './novi-recenzenti.component';

describe('NoviRecenzentiComponent', () => {
  let component: NoviRecenzentiComponent;
  let fixture: ComponentFixture<NoviRecenzentiComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NoviRecenzentiComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NoviRecenzentiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

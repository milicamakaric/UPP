import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RecenziranjeComponent } from './recenziranje.component';

describe('RecenziranjeComponent', () => {
  let component: RecenziranjeComponent;
  let fixture: ComponentFixture<RecenziranjeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RecenziranjeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RecenziranjeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

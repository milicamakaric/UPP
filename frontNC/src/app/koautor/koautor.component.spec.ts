import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { KoautorComponent } from './koautor.component';

describe('KoautorComponent', () => {
  let component: KoautorComponent;
  let fixture: ComponentFixture<KoautorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ KoautorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(KoautorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IspravkaRadComponent } from './ispravka-rad.component';

describe('IspravkaRadComponent', () => {
  let component: IspravkaRadComponent;
  let fixture: ComponentFixture<IspravkaRadComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IspravkaRadComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IspravkaRadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

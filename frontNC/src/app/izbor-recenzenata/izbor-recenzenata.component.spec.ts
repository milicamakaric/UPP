import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IzborRecenzenataComponent } from './izbor-recenzenata.component';

describe('IzborRecenzenataComponent', () => {
  let component: IzborRecenzenataComponent;
  let fixture: ComponentFixture<IzborRecenzenataComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IzborRecenzenataComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IzborRecenzenataComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

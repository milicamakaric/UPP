import { TestBed } from '@angular/core/testing';

import { RadService } from './rad.service';

describe('RadService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: RadService = TestBed.get(RadService);
    expect(service).toBeTruthy();
  });
});

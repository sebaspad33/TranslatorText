import { TestBed } from '@angular/core/testing';

import { TranslatorTextService } from './translator-text.service';

describe('TranslatorTextService', () => {
  let service: TranslatorTextService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TranslatorTextService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

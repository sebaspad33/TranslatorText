import { TestBed } from '@angular/core/testing';

import { TranslatorLenguajesService } from './translator-lenguajes.service';

describe('TranslatorLenguajesService', () => {
  let service: TranslatorLenguajesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TranslatorLenguajesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

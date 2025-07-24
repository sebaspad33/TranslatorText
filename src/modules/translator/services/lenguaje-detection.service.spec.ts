import { TestBed } from '@angular/core/testing';

import { LanguageDetectionService } from './lenguaje-detection.service';

describe('LanguajeDetectionService', () => {
  let service: LanguageDetectionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LanguageDetectionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

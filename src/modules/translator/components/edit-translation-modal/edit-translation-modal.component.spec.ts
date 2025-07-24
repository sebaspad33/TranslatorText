import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditTranslationModalComponent } from './edit-translation-modal.component';

describe('EditTranslationModalComponent', () => {
  let component: EditTranslationModalComponent;
  let fixture: ComponentFixture<EditTranslationModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [EditTranslationModalComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EditTranslationModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

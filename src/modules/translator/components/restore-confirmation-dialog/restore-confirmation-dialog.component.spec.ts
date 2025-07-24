import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RestoreConfirmationDialogComponent } from './restore-confirmation-dialog.component';

describe('RestoreConfirmationDialogComponent', () => {
  let component: RestoreConfirmationDialogComponent;
  let fixture: ComponentFixture<RestoreConfirmationDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RestoreConfirmationDialogComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RestoreConfirmationDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

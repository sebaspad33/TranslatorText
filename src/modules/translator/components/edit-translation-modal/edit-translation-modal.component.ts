import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-edit-translation-modal',
  templateUrl: './edit-translation-modal.component.html',
})
export class EditTranslationModalComponent {
  editForm: FormGroup;
  supportedLanguages: { key: string; value: string }[];

  constructor(
    public dialogRef: MatDialogRef<EditTranslationModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private fb: FormBuilder
  ) {
    // Inicia el formulario con los datos proporcionados al abrir el modal
    this.editForm = this.fb.group({
      request_text: [data.request_text],
      from_lang: [data.from_lang],
      to_lang: [data.to_lang],
    });

    this.supportedLanguages = data.supportedLanguages;
  }

  save(): void {
    this.dialogRef.close(this.editForm.value); // Devuelve los datos al cerrar
  }

  close(): void {
    this.dialogRef.close();
  }
}

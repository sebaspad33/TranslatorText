import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-delete-confirmation-dialog',
  template: `
    <h2 mat-dialog-title class="dialog-title">Confirmar Eliminación</h2>
    <div mat-dialog-content class="delete-dialog-content">
      <p class="dialog-message">
        ¿Estás seguro de que deseas eliminar la traducción de "<strong>{{ data.request_text }}</strong>"?
      </p>
    </div>
    <div mat-dialog-actions class="delete-dialog-actions">
      <button mat-button (click)="onCancel()">Cancelar</button>
      <button mat-raised-button color="warn" (click)="onConfirm()">Eliminar</button>
    </div>
  `,
  styles: [`
    :host {
      display: block;
      border-radius: 28px; 
      overflow: hidden;
    }
    .dialog-title {
      font-size: 24px;
      font-weight: bold;
      color: #d32f2f; /* Color de advertencia */
      text-align: center;
      margin-bottom: 10px;
    }

    .delete-dialog-content {
      display: flex;
      align-items: center;
      justify-content: center;
      text-align: center;
      border-radius: 8px;
      padding: 16px;
      margin: 10px;
    }

    .dialog-message {
      font-size: 16px;
      line-height: 1.5;
      margin: 0;
      color: #333;
    }

    .delete-dialog-actions {
      display: flex;
      justify-content: flex-end;
      gap: 10px;
      margin-top: 16px;
    }

    button[mat-button] {
      font-weight: bold;
      color: #757575; /* Color gris para el botón cancelar */
    }

    button[mat-raised-button] {
      margin-left: 8px;
      background-color: #d32f2f; /* Color rojo para el botón eliminar */
      color: #fff;
    }

    button[mat-raised-button]:hover {
      background-color: #b71c1c; /* Color rojo más oscuro al pasar el mouse */
    }
  `]
})
export class DeleteConfirmationDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<DeleteConfirmationDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { id: number, request_text: string }
  ) {}

  onCancel(): void {
    this.dialogRef.close(false);
  }

  onConfirm(): void {
    this.dialogRef.close(true);
  }
}

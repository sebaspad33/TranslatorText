import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-restore-confirmation-dialog',
  template: `
    <h2 mat-dialog-title class="dialog-title">Restaurar Traducción</h2>
    <div mat-dialog-content class="restore-dialog-content">
      <p class="dialog-message">
        ¿Está seguro de que desea restaurar esta traducción?
      </p>
    </div>
    <div mat-dialog-actions class="restore-dialog-actions">
      <button mat-button (click)="onCancel()">Cancelar</button>
      <button mat-raised-button color="primary" (click)="onConfirm()">Restaurar</button>
    </div>
  `,
  styles: [`
    .dialog-title {
      font-size: 24px;
      font-weight: bold;
      color: #1976d2; /* Color primario */
      text-align: center;
      margin-bottom: 10px;
    }

    .restore-dialog-content {
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

    .restore-dialog-actions {
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
      background-color: #1976d2; /* Color azul primario para el botón restaurar */
      color: #fff;
    }

    button[mat-raised-button]:hover {
      background-color: #1565c0; /* Color azul más oscuro al pasar el mouse */
    }
  `]
})
export class RestoreConfirmationDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<RestoreConfirmationDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {}

  onCancel(): void {
    this.dialogRef.close(false);
  }

  onConfirm(): void {
    this.dialogRef.close(true);
  }
}

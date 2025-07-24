import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { TranslatorLenguajesService } from '../../services/translator-lenguajes.service';
import { TranslatorTextService } from '../../services/translator-text.service';
import { LanguageDetectionService } from '../../services/lenguaje-detection.service';
import { EditTranslationModalComponent } from '../edit-translation-modal/edit-translation-modal.component';
import { DeleteConfirmationDialogComponent } from '../delete-confirmation-dialog/delete-confirmation-dialog.component';
import { RestoreConfirmationDialogComponent } from '../restore-confirmation-dialog/restore-confirmation-dialog.component';

@Component({
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.scss']
})
export class LayoutComponent implements OnInit {
  isLoading: boolean = false;
  lastTranslation: any = null; // Cambia esto para almacenar más detalles de la traducción
  supportedLanguages: Map<string, string> = new Map();
  textToTranslate: string = ''; // Texto a traducir
  sourceLanguage: string = '';  // Idioma de origen
  targetLanguage: string = '';  // Idioma de destino
  activeTranslations: any[] = [];    // Traducciones activas
  inactiveTranslations: any[] = [];  // Traducciones inactivas
  editForm: FormGroup;
  selectedTranslation: any = null;
  detectedLanguage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private translatorLenguajesService: TranslatorLenguajesService,
    private translatorTextService: TranslatorTextService,
    private languageDetectionService: LanguageDetectionService,
    private dialog: MatDialog

  ) {
    this.editForm = this.fb.group({
      request_text: [''],
      from_lang: [''],
      to_lang: ['']
    });
  }

  ngOnInit(): void {
    this.loadSupportedLanguages(); // Cargar los idiomas al inicializar
    this.loadLastTranslation();    // Cargar la última traducción al inicializar
    this.loadActiveTranslations();    // Cargar traducciones activas e inactivas
    this.loadInactiveTranslations();
  }

  loadSupportedLanguages(): void {
    this.translatorLenguajesService.getLanguages().subscribe(
      (languages) => {
        this.supportedLanguages = languages; // Asignar correctamente los idiomas soportados
      },
      (error) => {
        console.error('Error al cargar los idiomas', error); // Manejo de errores
      }
    );
  }

  loadLastTranslation(): void {
    this.isLoading = true;
    this.translatorTextService.findLast().subscribe({
      next: (data) => {
        if (data) {
          this.lastTranslation = data; // Almacena todos los detalles
        }
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error al cargar la última traducción:', error);
        this.isLoading = false;
      }
    });
  }

  loadActiveTranslations(): void {
    this.isLoading = true;
    this.translatorTextService.findAllActives().subscribe({
      next: (data) => {
        this.activeTranslations = data;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error al cargar traducciones activas:', error);
        this.isLoading = false;
      }
    });
  }

  loadInactiveTranslations(): void {
    this.isLoading = true;
    this.translatorTextService.findAllInactives().subscribe({
      next: (data) => {
        this.inactiveTranslations = data;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error al cargar traducciones inactivas:', error);
        this.isLoading = false;
      }
    });
  }

  traducir(): void {
    if (!this.sourceLanguage || !this.targetLanguage || !this.textToTranslate) {
      console.error('Debe seleccionar idiomas y escribir texto a traducir');
      return;
    }

    this.isLoading = true;
    this.translatorTextService.translate(this.textToTranslate, this.sourceLanguage, this.targetLanguage).subscribe({
      next: (data) => {
        this.lastTranslation = {
          request_text: this.textToTranslate,
          translated_text: data.translated_text,
          from_lang: this.sourceLanguage,
          to_lang: this.targetLanguage,
          status: 'Success'
        };
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error en la traducción:', error);
        this.isLoading = false;
      }
    });
  }


  openEditModal(translation: any): void {
    const dialogRef = this.dialog.open(EditTranslationModalComponent, {
      width: '600px',
      data: {
        request_text: translation.request_text,
        from_lang: translation.from_lang,
        to_lang: translation.to_lang,
        supportedLanguages: Array.from(this.supportedLanguages.entries()).map(([key, value]) => ({ key, value })), // Convertir Map a Array de objetos
      },
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.saveChanges(result, translation.id);
      }
    });
  }


  saveChanges(updatedData: any, id: number): void {
    this.translatorTextService.editTranslation(id, updatedData.request_text, updatedData.from_lang, updatedData.to_lang).subscribe({
      next: (updatedTranslation) => {
        const index = this.activeTranslations.findIndex(t => t.id === id);
        if (index > -1) {
          this.activeTranslations[index] = updatedTranslation;
        }
      },
      error: (error) => {
        console.error('Error al editar la traducción:', error);
      }
    });
  }

  openDeleteModal(translation: any): void {
    const dialogRef = this.dialog.open(DeleteConfirmationDialogComponent, {
      width: '300px',
      data: { id: translation.id, request_text: translation.request_text }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.deleteTranslation(translation.id);
      }
    });
  }

  deleteTranslation(id: number): void {
    this.translatorTextService.deleteTranslation(id).subscribe({
      next: (response) => {
        console.log('Respuesta del servidor:', response); // Log para verificar la respuesta
        this.activeTranslations = this.activeTranslations.filter(t => t.id !== id);
        this.loadInactiveTranslations();
      },
      error: (error) => {
        console.error('Error al eliminar la traducción:', error.status, error.message);
      }
    });
  }

  openRestoreModal(translation: any): void {
    const dialogRef = this.dialog.open(RestoreConfirmationDialogComponent, {
      width: '300px',
      data: { id: translation.id, request_text: translation.request_text }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.restoreTranslation(translation.id);
      }
    });
  }

  restoreTranslation(id: number): void {
    this.translatorTextService.activateTranslation(id).subscribe({
      next: (response) => {
        console.log('Traducción restaurada:', response);
        this.loadActiveTranslations();
        this.loadInactiveTranslations();
      },
      error: (error) => {
        console.error('Error al restaurar la traducción:', error);
      }
    });
  }

  detectLanguage(text: string): void {
    // Intentamos detectar el idioma siempre
    this.languageDetectionService.detectLanguage(text).subscribe({
      next: (response: string) => {
        this.detectedLanguage = response;
        this.sourceLanguage = response;  // Asignar el idioma detectado al selector
        console.log('Idioma detectado:', this.detectedLanguage);
      },
      error: (error) => {
        console.error('Error al detectar el idioma:', error);
      }
    });
  
    // Si el texto está vacío, limpiamos el selector de idioma
    if (!text) {
      this.sourceLanguage = '';  
      this.detectedLanguage = null; 
    }
  }

  onTextChange(text: string): void {
    if (!text) {
      this.sourceLanguage = '';  // Limpiar el selector de idioma cuando el texto esté vacío
      this.detectedLanguage = null;
    } else {
      this.detectLanguage(text);
    }
  }

}

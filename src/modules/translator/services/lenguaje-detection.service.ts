import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LanguageDetectionService {

  private apiUrl = 'https://fictional-zebra-56w9g6xp9xrcv5w6-8080.app.github.dev/detect';

  constructor(private http: HttpClient) {}

  detectLanguage(text: string): Observable<string> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    });

    const body = { text };

    // Usamos .text() para obtener la respuesta como texto plano
    return this.http.post(this.apiUrl, body, { headers, responseType: 'text' });
  }
}

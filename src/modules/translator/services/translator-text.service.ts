import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TranslateRequestBody } from '../services/models/translate';


@Injectable({
  providedIn: 'root'
})
export class TranslatorTextService {

  private apiUrl = 'https://fictional-zebra-56w9g6xp9xrcv5w6-8080.app.github.dev/translate';

  constructor(private http: HttpClient) {}

  findLast(): Observable<any> {
    return this.http.get(`${this.apiUrl}/last`);
  }

  findAllActives(): Observable<any> {
    return this.http.get(`${this.apiUrl}/actives`);
  }

  findAllInactives(): Observable<any> {
    return this.http.get(`${this.apiUrl}/inactives`);
  }

  translate(text: string, from: string, to: string): Observable<any> {
    const requestBody: TranslateRequestBody = { text, from, to };
    return this.http.post<any>(`${this.apiUrl}`, requestBody);
  }

  editTranslation(id: number, request_text: string, from_lang: string, to_lang: string): Observable<any> {
    const requestBody = { request_text, from_lang, to_lang };
    return this.http.put<any>(`${this.apiUrl}/edit/${id}`, requestBody);
  }

  deleteTranslation(id: number): Observable<string> {
    return this.http.delete<string>(`${this.apiUrl}/delete-log/${id}`, { responseType: 'text' as 'json' });
  }

  activateTranslation(id: number): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/activate/${id}`, {});
  }

}

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TranslatorLenguajesService {

  private apiUrl = 'https://fictional-zebra-56w9g6xp9xrcv5w6-8080.app.github.dev/lenguajes';

  constructor(private http: HttpClient) {}

  getLanguages(): Observable<Map<string, string>> {
    return this.http.get<Record<string, string>>(this.apiUrl + '/lenguajes').pipe(
      map(languages => new Map(Object.entries(languages))) 
    );
  }
}

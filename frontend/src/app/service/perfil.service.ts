import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class PerfilService {

  private baseUrl = 'http://localhost:8080/api/perfil';

  private perfilSubject = new BehaviorSubject<any>(null);
  public perfil$ = this.perfilSubject.asObservable();

  constructor(private http: HttpClient) { }

  
  getPerfil(): Observable<any> {
    return this.http.get(this.baseUrl).pipe(
      tap(perfil => {
        this.perfilSubject.next(perfil);
      })
    );
  }

  
  uploadFoto(file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file, file.name);
    const url = `${this.baseUrl}/upload-foto`;

    
    
    return this.http.post(url, formData, { responseType: 'text' });
  }

  
  updateAluno(payload: any): Observable<any> {
    
    return this.http.put(`${this.baseUrl}/aluno`, payload, { responseType: 'text' });
  }

  
  updateProfessor(payload: any): Observable<any> {
    
    return this.http.put(`${this.baseUrl}/professor`, payload, { responseType: 'text' });
  }

  removerFoto(): Observable<any> {
    const url = `${this.baseUrl}/foto`;

    
    return this.http.delete(url, { responseType: 'text' });
  }

  desativarPerfil(): Observable<any> {
    

    
    return this.http.delete(this.baseUrl, { responseType: 'text' });
  }
}

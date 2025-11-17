import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class AuthService {


  private baseUrl = 'http://localhost:8080/api';

  private authenticated = false;

  private getToken(): string | null {
    return localStorage.getItem('token');
  }

  public getRole(): string {
    const token = this.getToken();
    if (token) {
      try {
        const decodedToken: any = jwtDecode(token);

        console.log('Token Descodificado (Payload):', decodedToken);

        if (decodedToken.authorities && decodedToken.authorities.length > 0) {
          console.log('Encontrado o Role em "authorities"');
          return decodedToken.authorities[0];
        }

        if (decodedToken.role) {
          console.log('Encontrado o Role em "role"');
          return decodedToken.role;
        }
        if (decodedToken.roles && decodedToken.roles.length > 0) {
          console.log('Encontrado o Role em "roles"');
          return decodedToken.roles[0];
        }

      } catch (error) {
        console.error("Erro ao descodificar o token:", error);
        return 'DESCONHECIDO';
      }
    }
    return 'DESCONHECIDO';
  }

  public getUsername(): string {
    const token = this.getToken();
    if (token) {
      try {
        const decodedToken: any = jwtDecode(token);
        return decodedToken.sub; 
      } catch (error) {
        return 'Usuário';
      }
    }
    return 'Usuário';
  }

  constructor(private http: HttpClient) { }

  login(user: any) {
    const url = `${this.baseUrl}/login`;
    return this.http.post(url, user);
  }

  logout() {
    localStorage.removeItem('token');
    this.setAuthenticated(false);
  }

  isLoggedIn() {
    return !!localStorage.getItem('token');
  }

  setAuthenticated(value: boolean) {
    this.authenticated = value;
  }
}

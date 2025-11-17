

import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/service/auth-service.service';
import { HeaderService } from 'src/app/service/header.service';

import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'], 
})
export class LoginComponent implements OnInit {

  
  public loginForm: FormGroup;
  public hide = true;
  public isLoading = false;
  public mensagemErro: string | null = null;

  
  

  constructor(
    private headerService: HeaderService,
    private authService: AuthService,
    private router: Router,
    private fb: FormBuilder 
  ) {
    
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      senha: ['', [Validators.required]]
    });
  }

  ngOnInit() {
    this.headerService.showSearchBar = false;
  }

  enviar() {
    // 6. Verifique a validade do formulário
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.isLoading = true;
    this.mensagemErro = null;

    // 7. Use o valor do formulário
    this.authService.login(this.loginForm.value).subscribe(
      (response: any) => {
        // (Sua lógica de sucesso está correta)
        localStorage.setItem('token', response.token);
        this.authService.setAuthenticated(true);
        this.isLoading = false;
        this.router.navigate(['painel']);
      },
      (error) => {
        console.error('Erro no login:', error);
        this.isLoading = false;
        this.mensagemErro = 'Email ou senha incorretos.';
      }
    );
  }
}

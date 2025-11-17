

import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ValidationErrors } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router'; 
import { SenhaService } from 'src/app/service/senha.service';


function passwordsMatch(group: FormGroup): ValidationErrors | null {
  const password = group.get('novaSenha')?.value;
  const confirmPassword = group.get('confirmarSenha')?.value;
  
  return password === confirmPassword ? null : { passwordMismatch: true };
}

@Component({
  selector: 'app-resetar-senha',
  templateUrl: './resetar-senha.component.html',
  styleUrls: ['./resetar-senha.component.scss']
})
export class ResetarSenhaComponent implements OnInit {

  resetForm: FormGroup;
  isLoading = false;
  token: string | null = null;
  mensagemSucesso: string | null = null;
  mensagemErro: string | null = null;
  hidePassword = true;

  constructor(
    private fb: FormBuilder,
    private senhaService: SenhaService,
    private route: ActivatedRoute, 
    private router: Router 
  ) {
    this.resetForm = this.fb.group({
      novaSenha: ['', [Validators.required, Validators.minLength(6)]],
      confirmarSenha: ['', Validators.required]
    }, {
      validators: passwordsMatch // Aplica o nosso validador customizado
    });
  }

  ngOnInit(): void {
    // 1. "Ouve" os parâmetros da URL para apanhar o token
    this.route.queryParamMap.subscribe(params => {
      this.token = params.get('token');
      if (!this.token) {
        this.mensagemErro = 'Token de redefinição inválido ou em falta.';
        this.isLoading = false;
        this.resetForm.disable();
      }
    });
  }

  
  enviarNovaSenha(): void {
    if (this.resetForm.invalid || !this.token) {
      this.resetForm.markAllAsTouched();
      return;
    }

    this.isLoading = true;
    this.mensagemSucesso = null;
    this.mensagemErro = null;

    
    const dto = {
      token: this.token,
      novaSenha: this.resetForm.value.novaSenha
    };

    
    this.senhaService.resetarSenha(dto).subscribe(
      (response: string) => {
        
        console.log('Senha redefinida:', response);
        this.mensagemSucesso = response;
        this.isLoading = false;
        this.resetForm.disable();

        
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 3000);
      },
      (error) => {
        
        console.error('Erro ao resetar senha:', error);
        this.mensagemErro = error.error || 'Não foi possível redefinir a sua senha.';
        this.isLoading = false;
      }
    );
  }

  
  get novaSenha() { return this.resetForm.get('novaSenha'); }
  get confirmarSenha() { return this.resetForm.get('confirmarSenha'); }
}

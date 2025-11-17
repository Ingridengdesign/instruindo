

import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { SenhaService } from 'src/app/service/senha.service'; 

@Component({
  selector: 'app-solicitar-reset',
  templateUrl: './solicitar-reset.component.html',
  styleUrls: ['./solicitar-reset.component.scss']
})
export class SolicitarResetComponent implements OnInit {

  resetForm: FormGroup;
  isLoading = false;
  mensagemSucesso: string | null = null;
  mensagemErro: string | null = null;

  constructor(
    private fb: FormBuilder,
    private senhaService: SenhaService
  ) {
    
    this.resetForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });
  }

  ngOnInit(): void {
    // (Podemos carregar algo aqui se necessário, mas por agora está OK)
  }

  /**
   * Chamado pelo botão "Enviar"
   */
  enviarSolicitacao(): void {
    if (this.resetForm.invalid) {
      this.resetForm.markAllAsTouched();
      return;
    }

    // 1. Ativa o loading e limpa as mensagens
    this.isLoading = true;
    this.mensagemSucesso = null;
    this.mensagemErro = null;

    const email = this.resetForm.value.email;

    // 2. Chama o serviço (POST /api/public/senha/solicitar-reset)
    this.senhaService.solicitarReset(email).subscribe(
      (response: string) => {
        // 3. SUCESSO! (A API devolve texto, como "Email enviado...")
        console.log('Solicitação de reset enviada:', response);
        this.mensagemSucesso = response; // Mostra a mensagem de sucesso
        this.isLoading = false;
        this.resetForm.disable(); // Desativa o formulário
      },
      (error) => {
        // 4. ERRO! (Ex: "Usuário não encontrado")
        console.error('Erro ao solicitar reset:', error);
        // O 'error.error' contém a string de texto do backend
        this.mensagemErro = error.error || 'Não foi possível processar a sua solicitação.';
        this.isLoading = false;
      }
    );
  }

  
  get email() {
    return this.resetForm.get('email');
  }
}

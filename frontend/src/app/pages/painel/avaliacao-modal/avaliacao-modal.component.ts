

import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AvaliacaoService } from 'src/app/service/avaliacao.service';
import { Observable } from 'rxjs';


@Component({
  selector: 'app-avaliacao-modal',
  templateUrl: './avaliacao-modal.component.html',
  styleUrls: ['./avaliacao-modal.component.scss']
})
export class AvaliacaoModalComponent implements OnInit {

  avaliacaoForm: FormGroup;
  isLoading = false;

  
  estrelas = [1, 2, 3, 4, 5];
  notaSelecionada = 0;

  constructor(
    private fb: FormBuilder,
    private avaliacaoService: AvaliacaoService,
    
    @Inject(MAT_DIALOG_DATA) public data: { idAula: number, role: string },
    
    public dialogRef: MatDialogRef<AvaliacaoModalComponent>
  ) {
    
    this.avaliacaoForm = this.fb.group({
      nota: [null, [Validators.required, Validators.min(1)]],
      comentario: ['']
    });
  }

  ngOnInit(): void {
    console.log('Modal de Avaliação aberto para:', this.data);
  }

  // Chamado pelo botão "Enviar"
  enviarAvaliacao(): void {
    if (this.avaliacaoForm.invalid) {
      return;
    }

    this.isLoading = true;
    const formValues = this.avaliacaoForm.value;
    const idAula = this.data.idAula;

    let obsAvaliacao: Observable<any>;

    // Decide qual *endpoint* chamar (Aluno ou Professor)
    if (this.data.role === 'ROLE_ALUNO') {
      console.log('Enviando avaliação de ALUNO...', formValues);
      obsAvaliacao = this.avaliacaoService.avaliarAulaAluno(idAula, formValues);

    } else if (this.data.role === 'ROLE_PROFESSOR') {
      console.log('Enviando avaliação de PROFESSOR...', formValues);
      obsAvaliacao = this.avaliacaoService.avaliarAulaProfessor(idAula, formValues);

    } else {
      console.error('Role desconhecido, não é possível avaliar.');
      this.isLoading = false;
      return;
    }

    
    obsAvaliacao.subscribe(
      (response) => {
        console.log('Avaliação enviada com sucesso!', response);
        this.isLoading = false;
        
        this.dialogRef.close({
          sucesso: true,
          idAula: this.data.idAula,
          role: this.data.role,
          avaliacao: this.avaliacaoForm.value 
        });
      },
      (error) => {
        console.error('Erro ao enviar avaliação:', error);
        this.isLoading = false;
        
        this.dialogRef.close(false); 
      }
    );
  }

  

  
  selecionarNota(nota: number): void {
    this.notaSelecionada = nota;
    this.avaliacaoForm.get('nota')?.setValue(nota);
  }

  
  setHoverNota(nota: number): void {
    this.notaSelecionada = nota;
  }

  
  limparHover(): void {
    
    this.notaSelecionada = this.avaliacaoForm.get('nota')?.value || 0;
  }

  
  fechar(): void {
    this.dialogRef.close();
  }
}

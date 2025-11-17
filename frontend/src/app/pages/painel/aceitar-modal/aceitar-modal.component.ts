import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { SolicitacaoService } from 'src/app/service/solicitacao.service';

@Component({
  selector: 'app-aceitar-modal',
  templateUrl: './aceitar-modal.component.html',
  styleUrls: ['./aceitar-modal.component.scss']
})
export class AceitarModalComponent {

  public form: FormGroup;
  public isLoading: boolean = false;
  public mensagemErro: string | null = null;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<AceitarModalComponent>,
    private solicitacaoService: SolicitacaoService,
    @Inject(MAT_DIALOG_DATA) public data: { idSolicitacao: number }
  ) {
    this.form = this.fb.group({
      local: [''] // Campo opcional
    });
  }

  /**
   * Chamado pelo botão "Confirmar e Aceitar"
   */
  onConfirm(): void {
    this.isLoading = true;
    this.mensagemErro = null;
    const local = this.form.value.local || null;

    this.solicitacaoService.responderSolicitacao(this.data.idSolicitacao, 'ACEITAR', local)
      .subscribe(
        (response) => {
          this.isLoading = false;
          this.dialogRef.close(true); 
        },
        (error) => {
          console.error('Erro ao aceitar solicitação:', error);
          this.isLoading = false;
          this.mensagemErro = error.error?.message || 'Ocorreu um erro.';
        }
      );
  }

  
  onCancel(): void {
    this.dialogRef.close(false); 
  }
}

import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RelatorioService } from 'src/app/service/relatorio.service';

@Component({
  selector: 'app-relatorios-tab',
  templateUrl: './relatorios-tab.component.html',
  styleUrls: ['./relatorios-tab.component.scss']
})
export class RelatoriosTabComponent{

  public relatorioForm: FormGroup;
  public relatorioData: any = null;
  public isLoadingRelatorio: boolean = false;
  public relatorioPesquisado: boolean = false;

  constructor(
    private fb: FormBuilder,
    private relatorioService: RelatorioService
  ) {

    this.relatorioForm = this.fb.group({
      dataInicio: ['', Validators.required],
      dataFim: ['', Validators.required],
    });
  }




  gerarRelatorio(): void {
    if (this.relatorioForm.invalid) {
      this.relatorioForm.markAllAsTouched();
      return;
    }

    this.isLoadingRelatorio = true;
    this.relatorioData = null;
    this.relatorioPesquisado = false;

    const { dataInicio, dataFim } = this.relatorioForm.value;

    this.relatorioService.getRelatorioAgendamentos(dataInicio, dataFim).subscribe(
      (response: any) => {
        this.relatorioData = response;
        this.isLoadingRelatorio = false;
        this.relatorioPesquisado = true;
        console.log('Relatório Gerado:', response);
      },
      (error) => {
        console.error('Erro ao gerar relatório:', error);
        this.isLoadingRelatorio = false;
        this.relatorioPesquisado = true;
      }
    );
  }

  limparRelatorio(): void {
    this.relatorioForm.reset();
    this.relatorioData = null;
    this.relatorioPesquisado = false;
  }


  formatarDataArray(dataArray: number[]): Date {
    if (!dataArray || dataArray.length < 3) return new Date();

    return new Date(dataArray[0], dataArray[1] - 1, dataArray[2]);
  }
}



import { Component, OnInit } from '@angular/core';
import { FormGroup, FormArray, FormBuilder, Validators } from '@angular/forms';
import { AgendaService } from 'src/app/service/agenda.service';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-agenda-gestao-tab',
  templateUrl: './agenda-gestao-tab.component.html',
  styleUrls: ['./agenda-gestao-tab.component.scss']
})
export class AgendaGestaoTabComponent implements OnInit {

  public disponibilidadeForm: FormGroup;
  public bloqueioForm: FormGroup;
  public bloqueios: any[] = [];
  public isLoadingAgenda: boolean = true;

  public diasDaSemana = [
    { key: 'SUNDAY', nome: 'Domingo' }, { key: 'MONDAY', nome: 'Segunda-feira' },
    { key: 'TUESDAY', nome: 'Terça-feira' }, { key: 'WEDNESDAY', nome: 'Quarta-feira' },
    { key: 'THURSDAY', nome: 'Quinta-feira' }, { key: 'FRIDAY', nome: 'Sexta-feira' },
    { key: 'SATURDAY', nome: 'Sábado' },
  ];

  constructor(
    private agendaService: AgendaService,
    private fb: FormBuilder
  ) {
    this.disponibilidadeForm = this.fb.group({
      dias: this.fb.array(
        this.diasDaSemana.map(dia => this.fb.group({
          idDisponibilidade: [null], diaDaSemana: [dia.key], nomeDia: [dia.nome],
          ativo: [false], horaInicio: [''], horaFim: ['']
        }))
      )
    });

    this.bloqueioForm = this.fb.group({
      dataBloqueio: ['', Validators.required], horaInicioBloqueio: ['', Validators.required],
      horaFimBloqueio: ['', Validators.required], motivo: ['']
    });
  }

  ngOnInit(): void {
    this.carregarDadosAgenda();
  }

  carregarDadosAgenda(): void {
    this.isLoadingAgenda = true;
    const obsDisponibilidade = this.agendaService.getDisponibilidade();
    const obsBloqueios = this.agendaService.getBloqueios();

    forkJoin([obsDisponibilidade, obsBloqueios]).subscribe(
      ([disponibilidadeResponse, bloqueiosResponse]: [any[], any[]]) => {
        this.preencherFormularioDisponibilidade(disponibilidadeResponse);

        this.bloqueios = bloqueiosResponse.map((b: any) =>
          this.formatarDatasArray(b.dataHoraInicio, b, 'dataHoraInicio', b.dataHoraFim, 'dataHoraFim')
        );

        this.isLoadingAgenda = false;
      },
      (error: any) => {
        console.error('Erro ao carregar dados da agenda:', error);
        this.isLoadingAgenda = false;
      }
    );
  }

  get diasFormArray() {
    return this.disponibilidadeForm.get('dias') as FormArray;
  }

  onSaveDisponibilidade(): void {
    if (this.disponibilidadeForm.invalid) return;

    const formValues = this.diasFormArray.value;
    const payload = formValues
      .filter((dia: any) => dia.ativo === true)
      .map((diaAtivo: any) => ({
        idDisponibilidade: diaAtivo.idDisponibilidade,
        diaDaSemana: diaAtivo.diaDaSemana,
        horaInicio: this.htmlTimeToJavaTime(diaAtivo.horaInicio),
        horaFim: this.htmlTimeToJavaTime(diaAtivo.horaFim)
      }));

    this.agendaService.atualizarDisponibilidade(payload).subscribe(
      (response: any) => {
        console.log('Disponibilidade atualizada!', response);
        this.carregarDadosAgenda();
      },
      (error: any) => { console.error('Erro ao salvar disponibilidade:', error); }
    );
  }

  onCreateBloqueio(): void {
    if (this.bloqueioForm.invalid) return this.bloqueioForm.markAllAsTouched();

    const form = this.bloqueioForm.value;
    const dataHoraInicioISO = `${form.dataBloqueio}T${form.horaInicioBloqueio}:00`;
    const dataHoraFimISO = `${form.dataBloqueio}T${form.horaFimBloqueio}:00`;

    const payload = {
      dataHoraInicio: dataHoraInicioISO,
      dataHoraFim: dataHoraFimISO,
      motivo: form.motivo
    };

    this.agendaService.criarBloqueio(payload).subscribe(
      (response: any) => {
        console.log('Bloqueio criado:', response);
        this.bloqueioForm.reset();
        this.carregarDadosAgenda();
      },
      (error: any) => { console.error('Erro ao criar bloqueio:', error); }
    );
  }

  onDeleteBloqueio(id: number): void {
    this.agendaService.apagarBloqueio(id).subscribe(
      (response: any) => {
        console.log('Bloqueio apagado:', response);
        this.carregarDadosAgenda();
      },
      (error: any) => { console.error('Erro ao apagar bloqueio:', error); }
    );
  }

  

  private preencherFormularioDisponibilidade(disponibilidades: any[]): void {
    const diasControls = this.diasFormArray.controls as FormGroup[];
    diasControls.forEach(control => control.patchValue({ ativo: false, horaInicio: '', horaFim: '' }));

    for (const dispo of disponibilidades) {
      const diaControl = diasControls.find(c => c.value.diaDaSemana === dispo.diaDaSemana);
      if (diaControl) {
        diaControl.patchValue({
          idDisponibilidade: dispo.idDisponibilidade,
          ativo: true,
          horaInicio: this.javaTimeToHtmlTime(dispo.horaInicio),
          horaFim: this.javaTimeToHtmlTime(dispo.horaFim)
        });
      }
    }
  }

  // CORRIGIDO: Removido o 'S' e adicionada a lógica
  private javaTimeToHtmlTime(javaTime: number[]): string {
    if (!javaTime || javaTime.length < 2) return '';
    const hora = javaTime[0].toString().padStart(2, '0');
    const minuto = javaTime[1].toString().padStart(2, '0');
    return `${hora}:${minuto}`;
  }

  
  private htmlTimeToJavaTime(htmlTime: string): number[] {
    if (!htmlTime) return [0, 0];
    const [hora, minuto] = htmlTime.split(':').map(Number);
    return [hora, minuto];
  }

  
  private formatarDatasArray(
    dataValor: any, objetoInteiro: any, campoData: string,
    dataValorFim: any = null, campoDataFim: string = 'dataHoraFim'
  ): any {
    let dataLimpa = null;
    let dataFimLimpa = null;
    if (Array.isArray(dataValor) && dataValor.length >= 5) {
      const d = new Date(dataValor[0], dataValor[1] - 1, dataValor[2], dataValor[3], dataValor[4], (dataValor[5] || 0));
      if (!isNaN(d.getTime())) { dataLimpa = d.toISOString(); }
    } else if (typeof dataValor === 'string') { dataLimpa = dataValor; }

    if (dataValorFim) {
      if (Array.isArray(dataValorFim) && dataValorFim.length >= 5) {
        const d = new Date(dataValorFim[0], dataValorFim[1] - 1, dataValorFim[2], dataValorFim[3], dataValorFim[4], (dataValorFim[5] || 0));
        if (!isNaN(d.getTime())) { dataFimLimpa = d.toISOString(); }
      } else if (typeof dataValorFim === 'string') { dataFimLimpa = dataValorFim; }
    }
    return {
      ...objetoInteiro,
      [campoData]: dataLimpa,
      [campoDataFim]: dataFimLimpa ? dataFimLimpa : objetoInteiro[campoDataFim]
    };
  }
}



import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatCalendarCellCssClasses } from '@angular/material/datepicker';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ProfessorService } from 'src/app/service/professor.service';
import { SolicitacaoService } from 'src/app/service/solicitacao.service';
import { CategoriaService } from 'src/app/service/categoria.service';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.scss'] 
})
export class ModalComponent implements OnInit {

  public professor: any;
  public isLoading: boolean = true;
  public isLoadingHorarios: boolean = false; 
  public agendaDisponivel: string[] = []; 
  public solicitacaoForm: FormGroup;


  public isLoadingAgenda: boolean = true; 
  public diasComAgenda: Set<string> = new Set<string>();

  public professorCategoriasParaSelect: any[] = [];

  constructor(
    private professorService: ProfessorService,
    private solicitacaoService: SolicitacaoService,
    private categoriaService: CategoriaService,
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<ModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { id: number }
  ) {
    
    this.solicitacaoForm = this.fb.group({
      idCategoria:[null, Validators.required],
      dataSelecionada: [null, Validators.required],
      horarioSelecionado: [null, Validators.required],
      detalhes: [''], // (A "Mensagem")
      termos: [false, Validators.requiredTrue] // (O Checkbox)
    });
  }

  ngOnInit(): void {
    // 4. USAR FORKJOIN PARA CARREGAR TUDO
    this.isLoading = true;
    const hoje = new Date();

    const obsPerfil = this.professorService.getProfessorById(this.data.id).pipe(
      catchError(err => {
        console.error('FALHA AO CARREGAR PERFIL', err);
        return of(null); 
      })
    );

    const obsCategoriasGlobais = this.categoriaService.getCategorias().pipe(
      catchError(err => {
        console.error('FALHA AO CARREGAR CATEGORIAS GLOBAIS', err);
        return of([]); 
      })
    );

    const obsDiasDisponiveis = this.professorService.getDiasDisponiveis(this.data.id, hoje.getMonth() + 1, hoje.getFullYear()).pipe(
      catchError(err => {
        console.error('FALHA AO CARREGAR DIAS DISPONÍVEIS', err);
        return of([]); 
      })
    );

    
    forkJoin([obsPerfil, obsCategoriasGlobais, obsDiasDisponiveis]).subscribe(
      ([perfil, categoriasGlobais, dias]) => {

        
        console.log('[DEBUG MODAL] Perfil carregado:', perfil);
        console.log('[DEBUG MODAL] Categorias Globais carregadas:', categoriasGlobais);
        console.log('[DEBUG MODAL] Dias Disponíveis carregados:', dias);
        

        if (!perfil) {
          console.error("FALHA CRÍTICA: O Perfil não pôde ser carregado. Fechando modal.");
          this.isLoading = false;
          this.dialogRef.close(); 
          return;
        }

        
        this.professor = perfil;

        
        this.diasComAgenda = new Set(dias as string[]);

        
        const nomesCategoriasProf = new Set(this.professor.categorias || []);
        console.log('[DEBUG MODAL] Nomes que o professor ensina:', nomesCategoriasProf);

        if (categoriasGlobais.length === 0) {
          console.warn("[DEBUG MODAL] A lista de Categorias Globais está vazia. O select não será populado.");
        }

        this.professorCategoriasParaSelect = (categoriasGlobais as any[]).filter(cat =>
          nomesCategoriasProf.has(cat.nome)
        );

        console.log('[DEBUG MODAL] Lista FINAL para o Select:', this.professorCategoriasParaSelect);
        

        if (this.professorCategoriasParaSelect.length === 1) {
          this.solicitacaoForm.patchValue({
            idCategoria: this.professorCategoriasParaSelect[0].idCategoria
          });
        }

        this.isLoading = false;
        this.isLoadingAgenda = false;
      });
  }

  carregarDiasDisponiveis(mes: number, ano: number): void {
    this.isLoadingAgenda = true;
    this.diasComAgenda.clear();

    this.professorService.getDiasDisponiveis(this.data.id, mes, ano).subscribe(
      (dias: string[]) => {
        this.diasComAgenda = new Set(dias); 
        this.isLoadingAgenda = false;
        console.log('Dias para "pintar":', this.diasComAgenda);
      },
      (error) => {
        console.error('Erro ao carregar dias disponíveis:', error);
        this.isLoadingAgenda = false;
      }
    );
  }

  dateClass = (date: Date): MatCalendarCellCssClasses => {
    const dataString = this.formatarData(date); 

    if (this.diasComAgenda.has(dataString)) {
      return 'dia-disponivel'; 
    }
    return '';
  }

  carregarPerfilDoProfessor(): void {
    this.isLoading = true;
    this.professorService.getProfessorById(this.data.id).subscribe(
      (response) => {
        this.professor = response;
        this.isLoading = false;
        console.log('Dados do professor carregados:', this.professor);
      },
      (error) => {
        console.error('Erro ao carregar perfil:', error);
        this.isLoading = false;
      }
    );
  }

  /**
   * (MÉTODO ATUALIZADO)
   * Chamado quando o usuário ESCOLHE UMA DATA no datepicker.
   */
  public onDataSelecionada(event: any): void {
    const data: Date = event.value; // O datepicker retorna um evento
    if (!data) return;

    this.isLoadingHorarios = true;
    this.agendaDisponivel = []; // Limpa horários antigos
    this.solicitacaoForm.get('horarioSelecionado')?.reset(); 

    const dataFormatada = this.formatarData(data);
    console.log('Buscando horários para:', dataFormatada);

    
    this.professorService.getAgendaPublica(this.professor.idProfessor, dataFormatada).subscribe(
      (horarios: string[]) => {
        this.agendaDisponivel = horarios;
        this.isLoadingHorarios = false;
        console.log('Horários disponíveis:', horarios);
      },
      (error) => {
        console.error('Erro ao buscar horários:', error);
        this.isLoadingHorarios = false;
      }
    );
  }


  enviarSolicitacao(): void {
    if (this.solicitacaoForm.invalid) {
      this.solicitacaoForm.markAllAsTouched();
      return;
    }

    const form = this.solicitacaoForm.value;

    

    
    const dataSelecionada: Date = form.dataSelecionada;

    
    const [hora, minuto] = form.horarioSelecionado.split(':');
    dataSelecionada.setHours(parseInt(hora), parseInt(minuto));
    

    
    const dataFormatadaParaBackend = this.formatarDataParaBackend(dataSelecionada);

    const dto = {
      idProfessor: this.professor.idProfessor,
      idCategoria: form.idCategoria,

      
      dataSolicitacao: dataFormatadaParaBackend, 

      detalhes: form.detalhes
    };

    

    console.log('Enviando DTO de solicitação (FINAL):', dto);
    this.isLoading = true;

    this.solicitacaoService.criarSolicitacao(dto).subscribe(
      (response) => {
        this.isLoading = false;
        console.log('Solicitação criada!', response);
        this.dialogRef.close(true); 
      },
      (error) => {
        this.isLoading = false;
        console.error('Erro ao criar solicitação:', error);
      }
    );
  }

  private formatarDataParaBackend(date: Date): string {
    const y = date.getFullYear();
    const m = (date.getMonth() + 1).toString().padStart(2, '0');
    const d = date.getDate().toString().padStart(2, '0');
    const h = date.getHours().toString().padStart(2, '0');
    const min = date.getMinutes().toString().padStart(2, '0');
    const sec = date.getSeconds().toString().padStart(2, '0'); 

    
    return `${y}-${m}-${d}T${h}:${min}:${sec}`;
  }

  
  private formatarData(date: Date): string {
    const y = date.getFullYear();
    const m = (date.getMonth() + 1).toString().padStart(2, '0');
    const d = date.getDate().toString().padStart(2, '0');
    return `${y}-${m}-${d}`;
  }

  private readonly fileBaseUrl = 'http://localhost:8080/api/files/';

  public getProfessorImageUrl(professor: any): string {
    const nomeFicheiro = professor?.nomeFicheiro; 

    if (nomeFicheiro) {
      return this.fileBaseUrl + nomeFicheiro;
    }
    return 'https://material.angular.io/assets/img/examples/shiba2.jpg'; 
  }
}

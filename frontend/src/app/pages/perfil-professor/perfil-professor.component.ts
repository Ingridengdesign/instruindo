

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProfessorService } from 'src/app/service/professor.service';
import { HeaderService } from 'src/app/service/header.service';
import { MatDialog } from '@angular/material/dialog';
import { ModalComponent } from '../search/modal/modal.component';
import { AuthService } from 'src/app/service/auth-service.service';


import { MatCalendarCellCssClasses } from '@angular/material/datepicker';

@Component({
  selector: 'app-perfil-professor',
  templateUrl: './perfil-professor.component.html',
  styleUrls: ['./perfil-professor.component.scss']
})
export class PerfilProfessorComponent implements OnInit {

  public professor: any;
  public isLoading: boolean = true;
  private professorId: number = 0;


  public isLoadingAgenda: boolean = true;
  public diasComAgenda: Set<string> = new Set<string>();

  constructor(
    private route: ActivatedRoute,
    private professorService: ProfessorService,
    private headerService: HeaderService,
    public dialog: MatDialog,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.headerService.showSearchBar = false;

    const idParam = this.route.snapshot.paramMap.get('id');
    if (!idParam) {

      return;
    }

    this.professorId = Number(idParam);


    this.professorService.getProfessorById(this.professorId).subscribe(
      (data) => {
        this.professor = data;
        this.isLoading = false;
        console.log('Perfil carregado:', this.professor);
      },
      (error) => {

      }
    );


    const hoje = new Date();
    this.carregarDiasDisponiveis(hoje.getMonth() + 1, hoje.getFullYear());
  }


  carregarDiasDisponiveis(mes: number, ano: number): void {
    if (!this.professorId) return;

    this.isLoadingAgenda = true;
    this.diasComAgenda.clear();

    this.professorService.getDiasDisponiveis(this.professorId, mes, ano).subscribe(
      (dias: string[]) => {
        this.diasComAgenda = new Set(dias);
        this.isLoadingAgenda = false;
        console.log('[Perfil] Dias para "pintar":', this.diasComAgenda);
      },
      (error) => {
        console.error('[Perfil] Erro ao carregar dias disponíveis:', error);
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
  
  private formatarData(date: Date): string {
    const y = date.getFullYear();
    const m = (date.getMonth() + 1).toString().padStart(2, '0');
    const d = date.getDate().toString().padStart(2, '0');
    return `${y}-${m}-${d}`;
  }


  public abrirModalSolicitacao(): void {
    if (!this.professor) return;

    if (this.authService.isLoggedIn()) {
      const dialogRef = this.dialog.open(ModalComponent, {
        data: { id: this.professorId },
        width: '800px',
        autoFocus: false
      });

    } else {
      this.router.navigate(['/login']);
    }
  }

  public getInitials(name: string): string {
    if (!name) return '?';

    const names = name.split(' ');
    const firstInitial = names[0] ? names[0][0] : '';
    const lastInitial = names.length > 1 ? names[names.length - 1][0] : '';

    return `${firstInitial}${lastInitial}`.toUpperCase();
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

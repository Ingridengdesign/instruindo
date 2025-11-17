import { Component, OnInit } from '@angular/core';
import { Observable, of } from 'rxjs';
import { SolicitacaoService } from 'src/app/service/solicitacao.service';
import { AuthService } from 'src/app/service/auth-service.service';
import { MatDialog } from '@angular/material/dialog';
import { AceitarModalComponent } from '../aceitar-modal/aceitar-modal.component';

@Component({
  selector: 'app-solicitacoes-pendentes',
  templateUrl: './solicitacoes-pendentes.component.html',
  styleUrls: ['./solicitacoes-pendentes.component.scss']
})
export class SolicitacoesPendentesComponent implements OnInit {

  public solicitacoes: any[] = [];
  public isLoadingSolicitacoes: boolean = true;
  public userRole: string = 'DESCONHECIDO';

  constructor(
    private solicitacaoService: SolicitacaoService,
    private authService: AuthService,
    public dialog: MatDialog
  ) { }

  ngOnInit(): void {
    this.userRole = this.authService.getRole();
    this.carregarSolicitacoes();
  }


  carregarSolicitacoes(): void {
    let obsSolicitacoes: Observable<any>;
    if (this.userRole === 'ROLE_PROFESSOR') {
      obsSolicitacoes = this.solicitacaoService.getSolicitacoesProfessor();
    } else {
      obsSolicitacoes = this.solicitacaoService.getSolicitacoesAluno();
    }

    this.isLoadingSolicitacoes = true;
    obsSolicitacoes.subscribe(
      (response: any[]) => {
        const solicitacoesFormatadas = response.map(sol => this.formatarDatasArray(sol.dataSolicitacao, sol));

        if (this.userRole === 'ROLE_PROFESSOR') {

          this.solicitacoes = solicitacoesFormatadas.filter(sol => sol.status === 'PENDENTE');
        } else {

          this.solicitacoes = solicitacoesFormatadas;
        }
        this.isLoadingSolicitacoes = false;
      },
      (error) => {
        console.error('Erro ao buscar solicitações:', error);
        this.solicitacoes = [];
        this.isLoadingSolicitacoes = false;
      }
    );
  }


  onResponder(id: number, acao: 'ACEITAR' | 'RECUSAR'): void {

    if (acao === 'RECUSAR') {
      
      this.solicitacaoService.responderSolicitacao(id, 'RECUSAR').subscribe(
        (response) => {
          console.log('Solicitação recusada:', response);
          this.carregarSolicitacoes(); 
        },
        (error) => {
          console.error('Erro ao recusar solicitação:', error);
        }
      );
    }
    else {
      
      const dialogRef = this.dialog.open(AceitarModalComponent, {
        width: '450px',
        data: { idSolicitacao: id }
      });

      
      dialogRef.afterClosed().subscribe(result => {
        
        if (result === true) {
          this.carregarSolicitacoes(); 
        }
      });
    }
  }

  public getChipClass(status: string): string {
    const baseClass = 'status-chip'; 
    switch (status) {
      case 'ACEITA': return `${baseClass} status-aceita`;
      case 'PENDENTE': return `${baseClass} status-pendente`;
      case 'RECUSADA': return `${baseClass} status-recusada`;
      default: return baseClass;
    }
  }

  private formatarDatasArray(
    dataValor: any,
    objetoInteiro: any,
    campoData: string = 'dataSolicitacao',

  ): any {
    let dataLimpa = null;
    if (Array.isArray(dataValor) && dataValor.length >= 5) {
      const d = new Date(dataValor[0], dataValor[1] - 1, dataValor[2], dataValor[3], dataValor[4], (dataValor[5] || 0));
      if (!isNaN(d.getTime())) { dataLimpa = d.toISOString(); }
    } else if (typeof dataValor === 'string') {
      dataLimpa = dataValor;
    }
    return {
      ...objetoInteiro,
      [campoData]: dataLimpa
    };
  }

  public getInitials(name: string): string {
    if (!name) return '?';
    const names = name.split(' ');
    const firstInitial = names[0] ? names[0][0] : '';
    const lastInitial = names.length > 1 ? names[names.length - 1][0] : '';
    return `${firstInitial}${lastInitial}`.toUpperCase();
  }
}



import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/service/auth-service.service'; 
import { RelatorioService } from 'src/app/service/relatorio.service';

@Component({
  selector: 'app-inicio-dashboard',
  templateUrl: './inicio-dashboard.component.html',
  styleUrls: ['./inicio-dashboard.component.scss'] 
})
export class InicioDashboardComponent implements OnInit {

  public userName: string = '';
  public userRole: string = 'DESCONHECIDO'; 
  public isLoading: boolean = true;

  
  public stats = {
    solicitadas: 0,
    concluidas: 0,
    avaliacoes: 0,
    novosPedidos: 0
  };

  constructor(
    private authService: AuthService,
    private relatorioService: RelatorioService
  ) {

  }

  ngOnInit(): void {

    this.userName = this.authService.getUsername() || 'Usuário';
    this.userRole = this.authService.getRole();


    if (this.userRole === 'ROLE_PROFESSOR') {
      this.isLoading = true; 
      this.relatorioService.getDashboardStats().subscribe(
        (data: any) => {
          this.stats = data;
          this.isLoading = false;
        },
        (error) => {
          console.error('Erro ao buscar estatísticas do dashboard:', error);
          this.isLoading = false;
        }
      );
    } else {

      this.isLoading = false;
    }
  }
}

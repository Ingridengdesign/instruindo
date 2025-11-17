

import { Component, OnInit } from '@angular/core';

import { PerfilService } from 'src/app/service/perfil.service';

@Component({
  selector: 'app-avaliacoes',
  templateUrl: './avaliacoes.component.html',
  styleUrls: ['./avaliacoes.component.scss']
})
export class AvaliacoesComponent implements OnInit {

  public avaliacoes: any[] = [];
  public isLoading: boolean = true;

  constructor(private perfilService: PerfilService) { }

  ngOnInit(): void {
    this.isLoading = true;
    
    this.perfilService.getPerfil().subscribe(
      (perfil: any) => {
        
        this.avaliacoes = perfil.avaliacoes || [];
        this.isLoading = false;
      },
      (error) => {
        console.error('Erro ao carregar avaliações:', error);
        this.isLoading = false;
      }
    );
  }

  
  public getInitials(name: string): string {
    if (!name) return '?';
    const names = name.split(' ');
    const firstInitial = names[0] ? names[0][0] : '';
    const lastInitial = names.length > 1 ? names[names.length - 1][0] : '';
    return `${firstInitial}${lastInitial}`.toUpperCase();
  }
}

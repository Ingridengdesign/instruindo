

import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { AuthService } from 'src/app/service/auth-service.service';
import { HeaderService } from 'src/app/service/header.service';
import { PerfilService } from 'src/app/service/perfil.service';
import { Subscription, Observable } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout'; 
import { MatSidenav } from '@angular/material/sidenav'; 

@Component({
  selector: 'app-painel',
  templateUrl: './painel.component.html',
  styleUrls: ['./painel.component.scss']
})
export class PainelComponent implements OnInit, OnDestroy {

  @ViewChild('sidenav') sidenav!: MatSidenav; 

  public userRole: string = 'DESCONHECIDO';
  public userName: string = 'Usuário';
  public userInitials: string = '?';
  public perfilData: any = null;
  private readonly fileBaseUrl = 'http://localhost:8080/api/files/';
  private perfilSub: Subscription | undefined;
  private mobileSub: Subscription | undefined;

  isMobile$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches),
      shareReplay()
  );

  private isMobile: boolean = false;

  constructor(
    private authService: AuthService,
    private headerService: HeaderService,
    private perfilService: PerfilService,
    private breakpointObserver: BreakpointObserver 
  ) { }

  ngOnInit(): void {
    
    this.headerService.showSearchBar = false;

    this.userRole = this.authService.getRole();

    
    this.perfilSub = this.perfilService.perfil$.subscribe(
      (data) => {
        if (data) {
          this.perfilData = data;
          this.userName = data.nome || 'Usuário';
          this.userInitials = this.getInitials(this.userName);
        }
      }
    );

    
    this.perfilService.getPerfil().subscribe({
      error: (err) => {
        console.error('Erro ao carregar perfil para o painel:', err);
        this.userName = this.authService.getUsername();
        this.userInitials = this.getInitials(this.userName);
      }
    });

    this.mobileSub = this.isMobile$.subscribe(isMobile => {
      this.isMobile = isMobile; 
    });
  }

  ngOnDestroy(): void {
    this.perfilSub?.unsubscribe();
    this.mobileSub?.unsubscribe(); 
    this.headerService.showSearchBar = true;
  }

  public onNavLinkClick(): void {
    
    if (this.isMobile) {
      this.sidenav.close();
    }
    
  }

  
  private getInitials(name: string): string {
    if (!name) return '?';
    const names = name.split(' ');
    const firstInitial = names[0] ? names[0][0] : '';
    const lastInitial = names.length > 1 ? names[names.length - 1][0] : '';
    return `${firstInitial}${lastInitial}`.toUpperCase();
  }

  // (Helper de Imagem - Inalterado, com o cache buster)
  public getProfessorImageUrl(): string {
    const urlDaFoto = this.perfilData?.fotoUrl;
    if (urlDaFoto) {
      // (Adicionando o cache buster que fizemos)
      return `${urlDaFoto}?v=${new Date().getTime()}`;
    }
    return '';
  }
}

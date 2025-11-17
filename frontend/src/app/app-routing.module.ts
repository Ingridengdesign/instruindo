import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AuthGuard } from './service/auth.guard';

import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './pages/login/login.component';
import { SignupComponent } from './pages/signup/signup.component';
import { SearchComponent } from './pages/search/search.component';
import { ModalComponent } from './pages/search/modal/modal.component';
import { PainelComponent } from './pages/painel/painel.component';
import { SolicitarResetComponent } from './pages/solicitar-reset/solicitar-reset.component';
import { ResetarSenhaComponent } from './pages/resetar-senha/resetar-senha.component';
import { PerfilProfessorComponent } from './pages/perfil-professor/perfil-professor.component';
import { InicioDashboardComponent } from './pages/painel/inicio-dashboard/inicio-dashboard.component';
import { PerfilSidenavComponent } from './pages/painel/perfil-sidenav/perfil-sidenav.component';
import { SolicitacoesPendentesComponent } from './pages/painel/solicitacoes-pendentes/solicitacoes-pendentes.component';
import { AgendaGestaoTabComponent } from './pages/painel/agenda-gestao-tab/agenda-gestao-tab.component';
import { CalendarioAulasTabComponent } from './pages/painel/calendario-aulas-tab/calendario-aulas-tab.component';
import { RelatoriosTabComponent } from './pages/painel/relatorios-tab/relatorios-tab.component';
import { HistoricoTabComponent } from './pages/painel/historico-tab/historico-tab.component';
import { AvaliacoesComponent } from './pages/painel/avaliacoes/avaliacoes.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignupComponent },
  { path: 'solicitar-reset', component: SolicitarResetComponent },
  { path: 'resetar-senha', component: ResetarSenhaComponent },
  { path: 'search', component: SearchComponent },
  {
    path: 'painel',
    component: PainelComponent,
    canActivate: [AuthGuard],
    children: [
      { path: '', redirectTo: 'inicio', pathMatch: 'full' },
      { path: 'inicio', component: InicioDashboardComponent },
      { path: 'informacoes', component: PerfilSidenavComponent },

      { path: 'pedidos', component: SolicitacoesPendentesComponent },
      { path: 'gestao-agenda', component: AgendaGestaoTabComponent },
      { path: 'calendario', component: CalendarioAulasTabComponent },
      { path: 'relatorios', component: RelatoriosTabComponent },

      { path: 'solicitacoes', component: SolicitacoesPendentesComponent },
      { path: 'historico', component: HistoricoTabComponent },
      { path: 'avaliacoes', component: AvaliacoesComponent }

    ]
  },

  { path: 'modal', component: ModalComponent },
  { path: 'perfil-professor/:id', component: PerfilProfessorComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

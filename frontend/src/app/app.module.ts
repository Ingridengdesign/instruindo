import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { registerLocaleData } from '@angular/common';
import localePt from '@angular/common/locales/pt';
import { MAT_DATE_LOCALE } from '@angular/material/core';

import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptorInterceptor } from './service/auth-interceptor.interceptor';

registerLocaleData(localePt);


import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './components/header/header.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';


import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatCardModule } from '@angular/material/card';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatRadioModule } from '@angular/material/radio';
import { MatSelectModule } from '@angular/material/select';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatTabsModule } from '@angular/material/tabs';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatDialogModule } from '@angular/material/dialog';
import { MatStepperModule } from '@angular/material/stepper';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatChipsModule } from '@angular/material/chips';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatListModule } from '@angular/material/list';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatButtonToggleModule } from '@angular/material/button-toggle';


import { FooterComponent } from './components/footer/footer.component';
import { HomeComponent } from './pages/home/home.component';
import { CardComponent } from './components/components/card/card.component';
import { CardDisplayComponent } from './components/components/card-display/card-display.component';
import { CalendarComponent } from './components/components/calendar/calendar.component';
import { ScheduleComponent } from './components/components/schedule/schedule.component';
import { LoginComponent } from './pages/login/login.component';
import { SignupComponent } from './pages/signup/signup.component';
import { SearchComponent } from './pages/search/search.component';
import { PainelComponent } from './pages/painel/painel.component';
import { ModalComponent } from './pages/search/modal/modal.component';
import { MessageComponent } from './components/components/message/message.component';
import { SignupService } from './service/signup.service';
import { AvaliacaoModalComponent } from './pages/painel/avaliacao-modal/avaliacao-modal.component';
import { SolicitarResetComponent } from './pages/solicitar-reset/solicitar-reset.component';
import { ResetarSenhaComponent } from './pages/resetar-senha/resetar-senha.component';
import { PerfilSidenavComponent } from "./pages/painel/perfil-sidenav/perfil-sidenav.component";
import { SolicitacoesPendentesComponent } from './pages/painel/solicitacoes-pendentes/solicitacoes-pendentes.component';
import { AgendaGestaoTabComponent } from './pages/painel/agenda-gestao-tab/agenda-gestao-tab.component';
import { CalendarioAulasTabComponent } from './pages/painel/calendario-aulas-tab/calendario-aulas-tab.component';
import { RelatoriosTabComponent } from './pages/painel/relatorios-tab/relatorios-tab.component';
import { HistoricoTabComponent } from './pages/painel/historico-tab/historico-tab.component';
import { PerfilProfessorComponent } from './pages/perfil-professor/perfil-professor.component';
import { InicioDashboardComponent } from './pages/painel/inicio-dashboard/inicio-dashboard.component';
import { AvaliacoesComponent } from './pages/painel/avaliacoes/avaliacoes.component';
import { AceitarModalComponent } from './pages/painel/aceitar-modal/aceitar-modal.component';


@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    HomeComponent,
    CardComponent,
    CardDisplayComponent,
    CalendarComponent,
    ScheduleComponent,
    LoginComponent,
    SignupComponent,
    SearchComponent,
    PainelComponent,
    ModalComponent,
    MessageComponent,
    AvaliacaoModalComponent,
    SolicitarResetComponent,
    ResetarSenhaComponent,
    PerfilSidenavComponent,
    SolicitacoesPendentesComponent,
    AgendaGestaoTabComponent,
    CalendarioAulasTabComponent,
    RelatoriosTabComponent,
    HistoricoTabComponent,
    PerfilProfessorComponent,
    InicioDashboardComponent,
    AvaliacoesComponent,
    AceitarModalComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    MatToolbarModule,
    MatIconModule,
    MatButtonModule,
    MatMenuModule,
    MatCardModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatFormFieldModule,
    MatInputModule,
    MatRadioModule,
    MatSelectModule,
    MatSidenavModule,
    MatTabsModule,
    MatPaginatorModule,
    MatDialogModule,
    MatStepperModule,
    MatProgressSpinnerModule,
    MatChipsModule,
    MatSlideToggleModule,
    MatListModule,
    MatGridListModule,
    MatCheckboxModule,
    MatButtonToggleModule

],
  providers: [{ provide: MAT_DATE_LOCALE, useValue: 'pt-BR' }, SignupService,
  {
    provide: HTTP_INTERCEPTORS,
    useClass: AuthInterceptorInterceptor,
    multi: true
  }],
  bootstrap: [AppComponent]
})
export class AppModule { }

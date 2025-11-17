import { Component, OnInit, OnDestroy, ViewChild, ElementRef } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Observable, forkJoin, of, Subscription } from 'rxjs';
import { AuthService } from 'src/app/service/auth-service.service';
import { PerfilService } from 'src/app/service/perfil.service';
import { CategoriaService } from 'src/app/service/categoria.service';
import { Router } from '@angular/router';


import { NotificationService } from 'src/app/service/notificacao.service';


@Component({
  selector: 'app-perfil-sidenav',
  templateUrl: './perfil-sidenav.component.html',
  styleUrls: ['./perfil-sidenav.component.scss']
})
export class PerfilSidenavComponent implements OnInit, OnDestroy {

  @ViewChild('fileInput') fileInput!: ElementRef;

  public perfilForm: FormGroup;
  public userRole: string = 'DESCONHECIDO';
  public listaCategorias: any[] = [];
  public isEditing = false;
  public hidePassword = true;
  public perfilData: any = null; 

  private cacheBuster = new Date().getTime();
  private perfilSub: Subscription | undefined;
  private categoriasSub: Subscription | undefined;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private perfilService: PerfilService,
    private categoriaService: CategoriaService,
    private router: Router,
    private notificationService: NotificationService 
  ) {
    
    this.perfilForm = this.fb.group({
      nome: ['', Validators.required],
      email: [{ value: '', disabled: true }],
      novaSenha: [''],
      idCategorias: [[]],
      precoPorHora: [null],
      profileStatement: ['']
    });
    this.perfilForm.disable();
  }

  ngOnInit(): void {
    this.userRole = this.authService.getRole();
    this.carregarDadosIniciais();
  }

  ngOnDestroy(): void {
    this.perfilSub?.unsubscribe();
    this.categoriasSub?.unsubscribe();
  }

  carregarDadosIniciais(): void {
    // Ouve o stream 'perfil$'
    this.perfilSub = this.perfilService.perfil$.subscribe(perfil => {
      if (perfil) {
        this.perfilData = perfil;
        this.preencherFormularioPerfil(perfil);
      }
    });

    // Se for professor, carrega as categorias (lógica separada)
    if (this.userRole === 'ROLE_PROFESSOR') {
      this.categoriasSub = this.categoriaService.getCategorias().subscribe(
        (categorias: any[]) => {
          this.listaCategorias = categorias;
        },
        (error: any) => {
          console.error('Erro ao carregar Categorias:', error);
          
          this.notificationService.showError('Não foi possível carregar as categorias.');
        }
      );
    }

    
    this.perfilService.getPerfil().subscribe({
      error: (err) => {
        console.error('Erro ao carregar Perfil:', err);
        this.perfilForm.patchValue({ email: this.authService.getUsername() });
      }
    });
  }

  public onFileSelected(event: any): void {
    const file: File = event.target.files[0];
    if (file) {
      this.perfilService.uploadFoto(file).subscribe(
        (response) => {
          console.log('Foto enviada com sucesso!', response);

          
          this.notificationService.showSuccess('Foto atualizada com sucesso!');

          this.perfilService.getPerfil().subscribe(() => {
            this.cacheBuster = new Date().getTime();
          });
        },
        (error) => {
          console.error('Erro ao enviar foto:', error);
          
          this.notificationService.showError('Erro ao enviar foto. Tente novamente.');
        }
      );
    }
  }

  public onRemoveFoto(): void {
    if (!confirm('Tem certeza que deseja remover sua foto de perfil?')) {
      return;
    }

    this.perfilService.removerFoto().subscribe(
      (response) => {
        console.log('Foto removida!', response);
        
        this.notificationService.showSuccess('Foto removida com sucesso.');

        this.perfilService.getPerfil().subscribe(() => {
          this.cacheBuster = new Date().getTime();
        });
      },
      (error) => {
        console.error('Erro ao remover foto:', error);
        
        this.notificationService.showError('Erro ao remover foto.');
      }
    );
  }

  public onSalvar(): void {
    if (this.perfilForm.invalid) {
      this.perfilForm.markAllAsTouched();
      return;
    }

    
    let updateSubscription: Observable<any>;
    const formValues = this.perfilForm.getRawValue();

    if (this.userRole === 'ROLE_ALUNO') {
      const payloadAluno = {  };
      updateSubscription = this.perfilService.updateAluno(payloadAluno);
    } else if (this.userRole === 'ROLE_PROFESSOR') {
      const payloadProfessor = {  };
      updateSubscription = this.perfilService.updateProfessor(payloadProfessor);
    } else {
      return this.notificationService.showError('Role desconhecido, não é possível salvar.');
    }
    

    updateSubscription.subscribe(
      (response) => {
        console.log('Perfil atualizado com sucesso!', response);
        this.perfilForm.disable();
        this.isEditing = false;

        
        this.notificationService.showSuccess('Perfil salvo com sucesso!');

        
        this.perfilService.getPerfil().subscribe();
      },
      (error) => {
        console.error('Erro ao salvar perfil:', error);
        
        this.notificationService.showError('Erro ao salvar perfil. Verifique os campos.');
      }
    );
  }

  public onDesativarPerfil(): void {
    const confirmacao = window.confirm(
      'TEM CERTEZA?\n\nEsta ação irá desativar sua conta. ' +
      'Você perderá o acesso ao painel. Esta ação não pode ser desfeita.'
    );
    if (!confirmacao) return;

    this.perfilService.desativarPerfil().subscribe(
      (response) => {
        
        this.notificationService.showSuccess('Conta desativada. Sentiremos sua falta!');

        this.authService.logout();
        this.router.navigate(['/']);
      },
      (error) => {
        console.error('Erro ao desativar perfil:', error);
        
        this.notificationService.showError('Erro ao desativar conta.');
      }
    );
  }

  public onUploadClick(): void {
    this.fileInput.nativeElement.click();
  }

  public onToggleEdit(): void {
    if (this.perfilForm.disabled) {
      this.perfilForm.enable();
      this.perfilForm.get('email')?.disable();
      this.isEditing = true;
    } else {
      this.onSalvar();
    }
  }

  public onCancelEdit(): void {
    this.perfilForm.disable();
    this.isEditing = false;
    this.carregarDadosIniciais(); 
  }

  public getProfessorImageUrl(): string {
    const urlDaFoto = this.perfilData?.fotoUrl;
    if (urlDaFoto) {
      return `${urlDaFoto}?v=${this.cacheBuster}`;
    }
    return 'https://material.angular.io/assets/img/examples/shiba2.jpg';
  }

  private preencherFormularioPerfil(perfil: any): void {
    console.log('Perfil carregado:', perfil);
    const dadosParaFormulario = { ...perfil };
    if (perfil.categorias && Array.isArray(perfil.categorias)) {
      dadosParaFormulario.idCategorias = this.traduzirCategoriasParaIds(perfil.categorias);
    }
    this.perfilForm.patchValue(dadosParaFormulario);
  }

  private traduzirCategoriasParaIds(nomes: string[]): number[] {
    if (!nomes || !this.listaCategorias) return [];
    return nomes.map((nomeCategoria: string) => {
      const categoriaEncontrada = this.listaCategorias.find(
        (cat: any) => cat.nome === nomeCategoria
      );
      return categoriaEncontrada ? categoriaEncontrada.idCategoria : null;
    }).filter((id: number | null) => id !== null);
  }
}

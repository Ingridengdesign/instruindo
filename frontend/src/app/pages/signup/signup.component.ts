

import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ValidationErrors } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/service/auth-service.service';
import { CategoriaService } from 'src/app/service/categoria.service';
import { HeaderService } from 'src/app/service/header.service';
import { SignupService } from 'src/app/service/signup.service';


function passwordsMatch(group: FormGroup): ValidationErrors | null {
  const password = group.get('senha')?.value;
  const confirmPassword = group.get('confirmarSenha')?.value;
  return password === confirmPassword ? null : { passwordMismatch: true };
}

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss'] 
})
export class SignupComponent implements OnInit {

  public signupForm: FormGroup; 
  public hide = true;
  public isLoading = false;
  public mensagemErro: string | null = null;
  public listaCategorias: any[] = []; 

  constructor(
    private headerService: HeaderService,
    private signupService: SignupService,
    private categoriaService: CategoriaService,
    private authService: AuthService,
    private fb: FormBuilder, 
    private router: Router
  ) {
    
    this.signupForm = this.fb.group({
      
      role: ['ALUNO', Validators.required],

      
      nome: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      senha: ['', [Validators.required, Validators.minLength(6)]],
      confirmarSenha: ['', Validators.required],

      // Passo 2 (Opcionais, só para Professor)
      precoPorHora: [null],
      idCategorias: [[]] // Default como array vazio
    }, {
      validators: passwordsMatch // Validador de senha
    });
  }

  ngOnInit() {
    this.headerService.showSearchBar = false;
    this.carregarCategorias();

    // 2. A "MÁGICA": OUVIR MUDANÇAS NO TOGGLE 'ROLE'
    this.signupForm.get('role')?.valueChanges.subscribe(role => {
      this.updateProfessorValidators(role);
    });
  }

  
  private updateProfessorValidators(role: string): void {
    const precoControl = this.signupForm.get('precoPorHora');
    const categoriasControl = this.signupForm.get('idCategorias');

    if (role === 'PROFESSOR') {
      
      precoControl?.setValidators([Validators.required, Validators.min(1)]);
      categoriasControl?.setValidators([Validators.required]);
    } else {
      
      precoControl?.clearValidators();
      categoriasControl?.clearValidators();
      precoControl?.reset();
      categoriasControl?.reset();
    }
    
    precoControl?.updateValueAndValidity();
    categoriasControl?.updateValueAndValidity();
  }

  
  onSubmit() {
    if (this.signupForm.invalid) {
      this.signupForm.markAllAsTouched();
      return;
    }

    this.isLoading = true;
    this.mensagemErro = null;

    const formValues = this.signupForm.value;
    const role = formValues.role;

    
    let payload: any;
    let cadastroSubscription: any;

    
    const payloadBase = {
      nome: formValues.nome,
      email: formValues.email,
      senha: formValues.senha
    };

    if (role === 'ALUNO') {
      payload = payloadBase;
      console.log('Payload Aluno:', payload);
      cadastroSubscription = this.signupService.cadastrarAluno(payload);

    } else if (role === 'PROFESSOR') {
      payload = {
        ...payloadBase,
        precoPorHora: formValues.precoPorHora,
        idCategorias: formValues.idCategorias
      };
      console.log('Payload Professor:', payload);
      cadastroSubscription = this.signupService.cadastrarProfessor(payload);

    } else {
      this.isLoading = false;
      this.mensagemErro = 'Tipo de usuário inválido.';
      return;
    }

    
    cadastroSubscription.subscribe({
      next: (response: any) => {
        console.log('Cadastro bem-sucedido:', response);
        
        this.authService.login({ email: payload.email, senha: payload.senha })
          .subscribe({
            next: (loginResponse: any) => {
              localStorage.setItem('token', loginResponse.token);
              this.authService.setAuthenticated(true);
              this.isLoading = false;
              this.router.navigate(['painel']);
            },
            error: (loginErr: any) => {
              this.isLoading = false;
              this.router.navigate(['/login']); 
            }
          });
      },
      error: (cadastroErr: any) => {
        console.error('Erro no cadastro:', cadastroErr);
        this.isLoading = false;
        this.mensagemErro = cadastroErr.error?.message || 'Erro ao criar conta. O email já pode estar em uso.';
      }
    });
  }

  
  carregarCategorias(): void {
    this.categoriaService.getCategorias().subscribe(
      (response) => { this.listaCategorias = response; },
      (error) => { console.error('Erro ao carregar categorias:', error); }
    );
  }

  getErrorMessage() {
    const emailControl = this.signupForm.get('email');
    if (emailControl?.hasError('required')) { return 'Você deve digitar seu email'; }
    return emailControl?.hasError('email') ? 'Esse email não é válido' : '';
  }
}

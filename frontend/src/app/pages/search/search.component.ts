import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { PageEvent } from '@angular/material/paginator';
import { ActivatedRoute } from '@angular/router';
import { ModalComponent } from './modal/modal.component'; 
import { ProfessorService } from 'src/app/service/professor.service';
import { CategoriaService } from 'src/app/service/categoria.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit {

  filtroForm: FormGroup;
  todosOsProfessores: any[] = [];
  professoresPaginados: any[] = [];
  categorias: any[] = [];
  length = 0;
  pageSize = 9;
  pageIndex = 0;
  pageSizeOptions = [9, 12, 15, 24, 36];
  showFirstLastButtons = true;
  pageEvent!: PageEvent;
  options: FormGroup;

  constructor(
    private fb: FormBuilder,
    public dialog: MatDialog,
    private professorService: ProfessorService,
    private categoriaService: CategoriaService,
    private route: ActivatedRoute
  ) {
    this.options = this.fb.group({
      bottom: 0,
      fixed: false,
      top: 0,
    });

    this.filtroForm = this.fb.group({
      idCategoria: [null],
      precoMax: [null],
      notaMin: [null]
    });
  }

  ngOnInit(): void {
    this.categoriaService.getCategorias().subscribe(
      (cats: any[]) => {
        this.categorias = cats;

        const termoBusca = this.route.snapshot.queryParamMap.get('q');

        if (termoBusca) {
          const termoNormalizado = this.normalizeString(termoBusca);

          const categoriaEncontrada = this.categorias.find(
            cat => this.normalizeString(cat.nome) === termoNormalizado
          );

          if (categoriaEncontrada) {
            console.log('Categoria encontrada!', categoriaEncontrada);

            
            
            
            this.filtroForm.patchValue({ idCategoria: categoriaEncontrada.idCategoria });

          } else {
            console.log(`Termo "${termoBusca}" não encontrado.`);
          }
        }

        
        this.buscar();
      },
      (error) => {
        console.error('Erro ao carregar categorias', error);
        this.buscar(); 
      }
    );
  }

  
  private normalizeString(str: string): string {
    if (!str) return '';
    return str
      .toLowerCase()
      .normalize("NFD")
      .replace(/[\u0300-\u036f]/g, "");
  }


  buscar(): void {
    const filtros = this.filtroForm.value;
    console.log('Buscando com filtros:', filtros); // Este log agora deve mostrar o ID

    this.professorService.buscarProfessores(filtros).subscribe(
      (response: any[]) => {
        this.todosOsProfessores = response;
        this.length = this.todosOsProfessores.length;
        this.pageIndex = 0;
        this.atualizarPagina();
      },
      (error) => {
        this.todosOsProfessores = [];
        this.professoresPaginados = [];
        this.length = 0;
      }
    );
  }

  handlePageEvent(e: PageEvent) {
    this.pageEvent = e;
    this.length = e.length;
    this.pageSize = e.pageSize;
    this.pageIndex = e.pageIndex;
    this.atualizarPagina();
  }

  atualizarPagina(): void {
    const startIndex = this.pageIndex * this.pageSize;
    const endIndex = startIndex + this.pageSize;
    this.professoresPaginados = this.todosOsProfessores.slice(startIndex, endIndex);
  }

  openDialog(professorId: number): void {
    const dialogRef = this.dialog.open(ModalComponent, {
      data: { id: professorId },
      width: '800px',
      autoFocus: false
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);
    });
  }
}

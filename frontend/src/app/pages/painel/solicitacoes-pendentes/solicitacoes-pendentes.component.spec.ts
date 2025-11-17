import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SolicitacoesPendentesComponent } from './solicitacoes-pendentes.component';

describe('SolicitacoesPendentesComponent', () => {
  let component: SolicitacoesPendentesComponent;
  let fixture: ComponentFixture<SolicitacoesPendentesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SolicitacoesPendentesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SolicitacoesPendentesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

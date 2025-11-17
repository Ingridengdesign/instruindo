import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AvaliacaoModalComponent } from './avaliacao-modal.component';

describe('AvaliacaoModalComponent', () => {
  let component: AvaliacaoModalComponent;
  let fixture: ComponentFixture<AvaliacaoModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AvaliacaoModalComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AvaliacaoModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HistoricoTabComponent } from './historico-tab.component';

describe('HistoricoTabComponent', () => {
  let component: HistoricoTabComponent;
  let fixture: ComponentFixture<HistoricoTabComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HistoricoTabComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(HistoricoTabComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

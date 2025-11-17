import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AgendaGestaoTabComponent } from './agenda-gestao-tab.component';

describe('AgendaGestaoTabComponent', () => {
  let component: AgendaGestaoTabComponent;
  let fixture: ComponentFixture<AgendaGestaoTabComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AgendaGestaoTabComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AgendaGestaoTabComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

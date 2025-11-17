import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SolicitarResetComponent } from './solicitar-reset.component';

describe('SolicitarResetComponent', () => {
  let component: SolicitarResetComponent;
  let fixture: ComponentFixture<SolicitarResetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SolicitarResetComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SolicitarResetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

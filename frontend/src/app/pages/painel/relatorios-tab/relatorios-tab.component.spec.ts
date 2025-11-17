import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RelatoriosTabComponent } from './relatorios-tab.component';

describe('RelatoriosTabComponent', () => {
  let component: RelatoriosTabComponent;
  let fixture: ComponentFixture<RelatoriosTabComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RelatoriosTabComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RelatoriosTabComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

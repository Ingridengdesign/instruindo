import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CalendarioAulasTabComponent } from './calendario-aulas-tab.component';

describe('CalendarioAulasTabComponent', () => {
  let component: CalendarioAulasTabComponent;
  let fixture: ComponentFixture<CalendarioAulasTabComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CalendarioAulasTabComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CalendarioAulasTabComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

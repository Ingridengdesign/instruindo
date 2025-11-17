import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AceitarModalComponent } from './aceitar-modal.component';

describe('AceitarModalComponent', () => {
  let component: AceitarModalComponent;
  let fixture: ComponentFixture<AceitarModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AceitarModalComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AceitarModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

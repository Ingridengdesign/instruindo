
import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/service/auth-service.service';

@Component({
  selector: 'app-card-display',
  templateUrl: './card-display.component.html',
  styleUrls: ['./card-display.component.scss']
})
export class CardDisplayComponent {

  @Input() professor: any;

  @Input() hideActions: boolean = false;

  
  @Output() solicitarAula = new EventEmitter<number>();

  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  
  getProfessorImage(): string {
    
    

    
    return 'https://material.angular.io/assets/img/examples/shiba2.jpg';
  }


  onSolicitarAulaClick(): void {
    
    if (this.authService.isLoggedIn()) {
      
      this.solicitarAula.emit(this.professor.idProfessor);
    } else {
      
      this.router.navigate(['/login']);
    }
  }

  private readonly fileBaseUrl = 'http://localhost:8080/api/files/';

  public getProfessorImageUrl(professor: any): string {
    const nomeFicheiro = professor?.nomeFicheiro; 

    if (nomeFicheiro) {
      return this.fileBaseUrl + nomeFicheiro;
    }
    return 'https://material.angular.io/assets/img/examples/shiba2.jpg'; 
  }
}

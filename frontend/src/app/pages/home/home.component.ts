import { Component } from '@angular/core';
import { HeaderService } from 'src/app/service/header.service';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {

  
  public materias = ['Matemática', 'Português', 'Física', 'Redação'];

  constructor(private headerService: HeaderService) { }

  
  public getIconForMateria(materia: string): string {

    
    switch (materia.toLowerCase()) {
      case 'matemática':
        return 'calculate'; 
      case 'português':
        return 'translate'; 
      case 'redação':
        return 'edit'; 
      case 'física':
        return 'science'; 
      case 'química':
        return 'science';
      case 'história':
        return 'public'; 
      case 'geografia':
        return 'map'; 

      
      default:
        return 'school'; 
    }
  }
}

import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  constructor(private snackBar: MatSnackBar) { }

  
  public showSuccess(message: string): void {
    this.snackBar.open(message, 'Fechar', {
      duration: 3000, 
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass: ['success-snackbar'] 
    });
  }

  
  public showError(message: string): void {
    this.snackBar.open(message, 'Fechar', {
      duration: 5000, 
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass: ['error-snackbar'] 
    });
  }
}

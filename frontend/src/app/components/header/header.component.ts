

import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'src/app/service/auth-service.service';
import { HeaderService } from 'src/app/service/header.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {

  
  public searchQuery: string = '';

  // 2. Não precisamos mais do @ViewChild
  // @ViewChild(MatMenuTrigger) ... (REMOVIDO)

  constructor(
    private headerService: HeaderService,
    private authService: AuthService,
    private router: Router
  ) {
    // 3. Removemos a lógica de 'isLoggedIn' do construtor
  }

  
  
  
  get isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }

  get showSearchBar(): boolean {
    return this.headerService.showSearchBar;
  }


  public onSearch(): void {
    if (!this.searchQuery || this.searchQuery.trim() === '') {
      return;
    }

    this.router.navigate(['/search'], {
      queryParams: { q: this.searchQuery }
    });
  }

  public logout(): void {
    this.authService.logout();
    this.router.navigate(['/']);
  }
}

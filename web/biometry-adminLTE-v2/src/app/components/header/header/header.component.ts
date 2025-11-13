// src/app/components/header/header.component.ts

import { Component, ElementRef, HostListener, Input, OnInit, Renderer2 } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { AuthService } from '../../../auth/auth.service';
import { User } from '../../../models/user';




@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterModule,TranslateModule],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
     @Input() user: User | null = null;
 userName: string = 'FRANK JIATOU';
  userRole: string = 'Super admin';
  consultationCount: number = 1;
  ordonnanceCount: number = 3;
  examenCount: number = 2;

  dropdowns: { [key: string]: boolean } = {
    consultations: false,
    ordonnances: false,
    examens: false,
    user: false
  };

  constructor(
    private renderer: Renderer2,
    private router: Router,
    private elementRef: ElementRef,
    private authservice:AuthService
  ) {}

  ngOnInit() {
    this.loadUserData();
    this.loadNotificationCounts();
  }

  toggleSidebar(event: Event) {
    event.preventDefault();
    event.stopPropagation();
    
    const body = document.body;
    const isMobile = window.innerWidth < 768;
    
    if (isMobile) {
      if (body.classList.contains('sidebar-open')) {
        this.renderer.removeClass(body, 'sidebar-open');
      } else {
        this.renderer.addClass(body, 'sidebar-open');
      }
    } else {
      if (body.classList.contains('sidebar-collapse')) {
        this.renderer.removeClass(body, 'sidebar-collapse');
      } else {
        this.renderer.addClass(body, 'sidebar-collapse');
      }
    }
  }

  toggleDropdown(dropdown: string, event: Event) {
    event.preventDefault();
    event.stopPropagation();
    
    Object.keys(this.dropdowns).forEach(key => {
      if (key !== dropdown) {
        this.dropdowns[key] = false;
      }
    });
    
    this.dropdowns[dropdown] = !this.dropdowns[dropdown];
  }

  closeAllDropdowns() {
    Object.keys(this.dropdowns).forEach(key => {
      this.dropdowns[key] = false;
    });
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent) {
    const target = event.target as HTMLElement;
    const clickedInside = this.elementRef.nativeElement.contains(target);
    
    if (!clickedInside) {
      this.closeAllDropdowns();
    }
  }

  loadUserData() {
    this.userName = 'John Doe';
    this.userRole = 'Administrateur';
  }

  loadNotificationCounts() {
    this.consultationCount = 5;
    this.ordonnanceCount = 3;
    this.examenCount = 2;
  }

  /**logout() {
    console.log('Déconnexion...');
    this
    this.router.navigate(['/login']);
  }**/
  logout(): void {
    this.authservice.logout().subscribe({
      next: () => console.log('Logout complete'),
      error: err => console.error('Logout failed', err)
    });
  }

  goToConsultations() {
    this.closeAllDropdowns();
    this.router.navigate(['/admin/consultation']);
  }

  goToOrdonnances() {
    this.closeAllDropdowns();
    this.router.navigate(['/admin/ordonnance']);
  }

  goToExamens() {
    this.closeAllDropdowns();
    this.router.navigate(['/admin/examen']);
  }

  toggleControlSidebar() {
  const body = document.body;
  if (body.classList.contains('control-sidebar-open')) {
    this.renderer.removeClass(body, 'control-sidebar-open');
  } else {
    this.renderer.addClass(body, 'control-sidebar-open');
  }
}
}
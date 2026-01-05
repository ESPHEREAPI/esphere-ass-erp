 import { Component, OnInit, HostListener, Renderer2, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';
import { TranslateModule } from '@ngx-translate/core';
import { User } from '../../../models/user';

 interface MenuItem {
  title: string;
  icon: string;
  route?: string;
  children?: MenuItem[];
  notificationCount?: number;
  isOpen?: boolean;
}

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterModule, TranslateModule],
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {
 
  currentRoute: string = '';
  isMobile: boolean = false;
  
  menuItems: MenuItem[] = [
    {
      title: 'accueil',
      icon: 'fa fa-home',
      route: '/admin/accueil'
    },
    {
      title: 'administration',
      icon: 'fa fa-users',
      isOpen: false,
      children: [
        { title: 'employe', icon: 'fa fa-circle-o', route: '/admin/employe' },
        { title: 'profil', icon: 'fa fa-circle-o', route: '/admin/profil' },
        { title: 'permission', icon: 'fa fa-circle-o', route: '/admin/permission' },
        { title: 'prestataire', icon: 'fa fa-circle-o', route: '/admin/prestataire' }
      ]
    },
    {
      title: 'internaute',
      icon: 'fa fa-user',
      route: '/admin/internaute'
    },
    {
      title: 'menu',
      icon: 'fa fa-gears',
      route: '/admin/menu'
    },
    {
      title: 'parametre',
      icon: 'fa fa-gears',
      route: '/admin/parametre'
    },
    {
      title: 'regionalisation',
      icon: 'fa fa-circle-o',
      isOpen: false,
      children: [
        { title: 'ville', icon: 'fa fa-circle-o', route: '/admin/ville' }
      ]
    },
    {
      title: 'consultation',
      icon: 'fa fa-circle-o',
      route: '/admin/consultation'
    },
    {
      title: 'ordonnance',
      icon: 'fa fa-circle-o',
      route: '/admin/ordonnance',
      notificationCount: 1
    },
    {
      title: 'examen',
      icon: 'fa fa-circle-o',
      route: '/admin/examen',
      notificationCount: 1
    }
  ];

  constructor(
    private router: Router,
    private renderer: Renderer2
  ) {}

  ngOnInit(): void {
    this.checkScreenSize();
    
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: any) => {
      this.currentRoute = event.url;
      this.openParentOfActiveRoute();
      
      if (this.isMobile) {
        this.closeSidebarOnMobile();
      }
    });
  }

  @HostListener('window:resize')
  onResize() {
    this.checkScreenSize();
  }

  checkScreenSize() {
    this.isMobile = window.innerWidth < 768;
  }

  isActive(route: string): boolean {
    return this.currentRoute === route;
  }

  hasActiveChild(item: MenuItem): boolean {
    if (!item.children) return false;
    return item.children.some(child => child.route === this.currentRoute);
  }

  openParentOfActiveRoute() {
    this.menuItems.forEach(item => {
      if (item.children) {
        const hasActive = item.children.some(child => child.route === this.currentRoute);
        if (hasActive) {
          item.isOpen = true;
        }
      }
    });
  }

  toggleSubmenu(item: MenuItem, event: Event) {
    event.preventDefault();
    event.stopPropagation();
    
    if (item.children) {
      item.isOpen = !item.isOpen;
    }
  }

  closeSidebarOnMobile() {
    if (this.isMobile) {
      const body = document.body;
      this.renderer.removeClass(body, 'sidebar-open');
    }
  }
}

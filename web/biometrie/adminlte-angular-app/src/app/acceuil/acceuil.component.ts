import { Component, OnInit } from '@angular/core';
import { Module } from '../model/module';
import { Router } from '@angular/router';
import { AuthService } from '../auth/auth.service';
import { ModulePermissionService } from '../services/module-permission.service';
import { ToastrService } from 'ngx-toastr';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-acceuil',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './acceuil.component.html',
  styleUrl: './acceuil.component.css'
})
export class AcceuilComponent implements OnInit{

  modules: Module[] = [
    {
      id: 1,
      code: 'auto_insurance',
      title: 'Assurance Automobile',
      icon: 'fas fa-car',
      iconDisabled: 'fas fa-car text-muted',
      enabled: false
    },
      {
      id: 11,
      code: 'A_TIRD',
      title: 'Assurance TIRD',
      icon: 'fas fa-sync-alt',
      iconDisabled: 'fas fa-sync-alt text-muted',
      enabled: false
    },
    {
      id: 6,
      code: 'adp_insurance',
      title: 'Assurance ADP - Non Vie',
      icon: 'fas fa-shield-alt',
      iconDisabled: 'fas fa-shield-alt text-muted',
      enabled: false
    },
     {
      id: 7,
      code: 'insured_management',
      title: 'Gestion Assurés',
      icon: 'fas fa-users',
      iconDisabled: 'fas fa-users text-muted',
      enabled: false
    },
    {
      id: 101,
      code: 'ref_auto_insurance',
      title: 'Referentiel Auto',
      icon: 'fas fa-car',
      iconDisabled: 'fas fa-car text-muted',
      enabled: false
    },
      {
      id: 1100,
      code: 'ref_A_TIRD',
      title: 'Referentiel TIRD',
      icon: 'fas fa-sync-alt',
      iconDisabled: 'fas fa-sync-alt text-muted',
      enabled: false
    },
    {
      id: 600,
      code: 'ref_adp_insurance',
      title: 'Referentiel ADP - Non Vie',
      icon: 'fas fa-shield-alt',
      iconDisabled: 'fas fa-shield-alt text-muted',
      enabled: false
    },
     
     {
      id: 8,
      code: 'general_accounting',
      title: 'Comptabilité Générale',
      icon: 'fas fa-calculator',
      iconDisabled: 'fas fa-calculator text-muted',
      enabled: false
    },
    {
      id: 9,
      code: 'admin',
      title: 'Administration',
      icon: 'fas fa-cogs',
      iconDisabled: 'fas fa-cogs text-muted',
      enabled: false
    },
    
    
    {
      id: 2,
      code: 'ird_insurance',
      title: 'Assurance IRD',
      icon: 'fas fa-hospital',
      iconDisabled: 'fas fa-hospital text-muted',
      enabled: false
    },
    {
      id: 3,
      code: 'transport',
      title: 'Transport',
      icon: 'fas fa-truck',
      iconDisabled: 'fas fa-truck text-muted',
      enabled: false
    },
    {
      id: 4,
      code: 'credit_caution',
      title: 'Caution Crédit',
      icon: 'fas fa-credit-card',
      iconDisabled: 'fas fa-credit-card text-muted',
      enabled: false
    },
    {
      id: 5,
      code: 'agriculture',
      title: 'Agriculture',
      icon: 'fas fa-seedling',
      iconDisabled: 'fas fa-seedling text-muted',
      enabled: false
    },
    {
      id: 13,
      code: 'settings',
      title: 'Paramétrage',
      icon: 'fas fa-tools',
      iconDisabled: 'fas fa-tools text-muted',
      enabled: false
    },
   
   
    {
      id: 10,
      code: 'agency_accounting',
      title: 'Comptabilité Agence',
      icon: 'fas fa-balance-scale',
      iconDisabled: 'fas fa-balance-scale text-muted',
      enabled: false
    },
  
    {
      id: 12,
      code: 'reporting',
      title: 'Gestion États',
      icon: 'fas fa-chart-bar',
      iconDisabled: 'fas fa-chart-bar text-muted',
      enabled: false
    }
  ];

  currentUser: any = null;
  showPasswordChangeModal = false;
  passwordForm = {
    oldPassword: '',
    newPassword: '',
    confirmPassword: '',
    securityKey: ''
  };

  constructor(
    private router: Router,
    private authService: AuthService,
    private modulePermissionService: ModulePermissionService,
    private toastr: ToastrService,
    private modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.loadUserData();
    this.checkModulePermissions();
  }

  private loadUserData(): void {
    this.currentUser = this.authService.currentUserValue?.userDTO;
    if (!this.currentUser) {
      this.router.navigate(['/login']);
    }
  }

  private checkModulePermissions(): void {
    this.modules.forEach(module => {
      module.enabled = this.modulePermissionService.hasModuleAccess(module.code);
    });
  }

  navigateToModule(module: Module): void {
    if (!module.enabled) {
      this.toastr.warning('Vous n\'avez pas accès à ce module', 'Accès refusé');
      return;
    }

    // Check if first connection requires password change
    //if (this.authService.isFirstConnection()) {
     // this.openPasswordChangeModal();
      //return;
   // }

    this.router.navigate([`/modules/${module.code}`]);
  }

  openPasswordChangeModal(): void {
    this.showPasswordChangeModal = true;
    this.resetPasswordForm();
  }

  closePasswordChangeModal(): void {
    this.showPasswordChangeModal = false;
    this.resetPasswordForm();
  }

  private resetPasswordForm(): void {
    this.passwordForm = {
      oldPassword: '',
      newPassword: '',
      confirmPassword: '',
      securityKey: ''
    };
  }

  updatePassword(): void {
    if (!this.validatePasswordForm()) {
      return;
    }

    /**this.authService.updatePassword(this.passwordForm).subscribe({
      next: (response) => {
        this.toastr.success('Mot de passe mis à jour avec succès', 'Succès');
        this.closePasswordChangeModal();
        this.authService.logout();
        this.router.navigate(['/login']);
      },
      error: (error) => {
        this.toastr.error(error.message || 'Erreur lors de la mise à jour', 'Erreur');
      }
    });**/
  }

  private validatePasswordForm(): boolean {
    if (!this.passwordForm.oldPassword || !this.passwordForm.newPassword || 
        !this.passwordForm.confirmPassword || !this.passwordForm.securityKey) {
      this.toastr.error('Tous les champs sont obligatoires', 'Validation');
      return false;
    }

    if (this.passwordForm.newPassword !== this.passwordForm.confirmPassword) {
      this.toastr.error('Les mots de passe ne correspondent pas', 'Validation');
      return false;
    }

    if (this.passwordForm.newPassword.length < 6) {
      this.toastr.error('Le mot de passe doit contenir au moins 6 caractères', 'Validation');
      return false;
    }

    return true;
  }

  logout(): void {
    this.authService.clearAuthData();
    this.router.navigate(['/login']);
  }
}

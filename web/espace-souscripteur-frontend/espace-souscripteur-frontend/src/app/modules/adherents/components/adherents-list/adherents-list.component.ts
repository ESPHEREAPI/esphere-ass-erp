import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { AdherentService } from '@core/services/adherent.service';
import {
  Adherent,
  AdherentFilters,
  AdherentPageResponse
} from '@core/models/adherent.model';
import Swal from 'sweetalert2';

/**
 * Composant de la liste des adhérents
 */
@Component({
  selector: 'app-adherents-list',
  templateUrl: './adherents-list.component.html',
  styleUrls: ['./adherents-list.component.scss']
})
export class AdherentsListComponent implements OnInit {
  adherents: Adherent[] = [];
  loading = true;
  
  // Pagination
  currentPage = 0;
  pageSize = 10;
  totalElements = 0;
  totalPages = 0;
  pageSizeOptions = [5, 10, 20, 50, 100];

  // Filtres
  filters: AdherentFilters = {};
  showFilters = false;
  
  // Recherche
  searchTerm = '';
  searchTimeout: any;

  // Sélection
  selectedAdherents: Set<string> = new Set();
  selectAll = false;

  constructor(
    private adherentService: AdherentService,
    private router: Router,
    private translate: TranslateService
  ) {}

  ngOnInit(): void {
    this.loadAdherents();
  }

  /**
   * Charge la liste des adhérents
   */
  loadAdherents(): void {
    this.loading = true;
    
    this.adherentService.getAdherents(this.currentPage, this.pageSize, this.filters)
      .subscribe({
        next: (response: AdherentPageResponse) => {
          this.adherents = response.content;
          this.totalElements = response.totalElements;
          this.totalPages = response.totalPages;
          this.loading = false;
        },
        error: (error) => {
          console.error('Erreur lors du chargement des adhérents:', error);
          this.loading = false;
        }
      });
  }

  /**
   * Recherche d'adhérents (debounce)
   */
  onSearch(): void {
    clearTimeout(this.searchTimeout);
    this.searchTimeout = setTimeout(() => {
      this.filters.searchTerm = this.searchTerm;
      this.currentPage = 0;
      this.loadAdherents();
    }, 500);
  }

  /**
   * Applique les filtres
   */
  applyFilters(): void {
    this.currentPage = 0;
    this.loadAdherents();
    this.showFilters = false;
  }

  /**
   * Réinitialise les filtres
   */
  resetFilters(): void {
    this.filters = {};
    this.searchTerm = '';
    this.currentPage = 0;
    this.loadAdherents();
  }

  /**
   * Change de page
   */
  changePage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.loadAdherents();
    }
  }

  /**
   * Change la taille de page
   */
  changePageSize(size: number): void {
    this.pageSize = size;
    this.currentPage = 0;
    this.loadAdherents();
  }

  /**
   * Navigue vers le profil d'un adhérent
   */
  viewProfile(codeAdherent: string): void {
    this.router.navigate(['/adherents/profile', codeAdherent]);
  }

  /**
   * Édite un adhérent
   */
  editAdherent(codeAdherent: string): void {
    this.router.navigate(['/adherents/edit', codeAdherent]);
  }

  /**
   * Supprime un adhérent
   */
  deleteAdherent(adherent: Adherent): void {
    Swal.fire({
      title: this.translate.instant('messages.confirm.delete'),
      text: `${adherent.assurePrincipal} (${adherent.codeAdherent})`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#dc3545',
      cancelButtonColor: '#6c757d',
      confirmButtonText: this.translate.instant('common.delete'),
      cancelButtonText: this.translate.instant('common.cancel')
    }).then((result) => {
      if (result.isConfirmed) {
        this.adherentService.deleteAdherent(adherent.codeAdherent).subscribe({
          next: () => {
            Swal.fire(
              this.translate.instant('common.deleted'),
              this.translate.instant('messages.success.deleted'),
              'success'
            );
            this.loadAdherents();
          },
          error: (error) => {
            console.error('Erreur lors de la suppression:', error);
          }
        });
      }
    });
  }

  /**
   * Sélectionne/désélectionne un adhérent
   */
  toggleSelection(codeAdherent: string): void {
    if (this.selectedAdherents.has(codeAdherent)) {
      this.selectedAdherents.delete(codeAdherent);
    } else {
      this.selectedAdherents.add(codeAdherent);
    }
    this.updateSelectAll();
  }

  /**
   * Sélectionne/désélectionne tous les adhérents
   */
  toggleSelectAll(): void {
    if (this.selectAll) {
      this.selectedAdherents.clear();
    } else {
      this.adherents.forEach(a => this.selectedAdherents.add(a.codeAdherent));
    }
    this.selectAll = !this.selectAll;
  }

  /**
   * Met à jour l'état de sélection globale
   */
  updateSelectAll(): void {
    this.selectAll = this.adherents.length > 0 &&
      this.adherents.every(a => this.selectedAdherents.has(a.codeAdherent));
  }

  /**
   * Vérifie si un adhérent est sélectionné
   */
  isSelected(codeAdherent: string): boolean {
    return this.selectedAdherents.has(codeAdherent);
  }

  /**
   * Exporte les adhérents
   */
  exportAdherents(format: 'pdf' | 'excel'): void {
    this.adherentService.exportAdherents(format, this.filters).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = `adherents.${format === 'pdf' ? 'pdf' : 'xlsx'}`;
        link.click();
        window.URL.revokeObjectURL(url);
      },
      error: (error) => {
        console.error('Erreur lors de l\'export:', error);
      }
    });
  }

  /**
   * Retourne la classe CSS pour le badge de statut
   */
  getStatusClass(statut: string): string {
    switch (statut?.toLowerCase()) {
      case 'actif':
        return 'badge-success';
      case 'suspendu':
        return 'badge-warning';
      case 'resilie':
        return 'badge-danger';
      default:
        return 'badge-secondary';
    }
  }

  /**
   * Formatte une date
   */
  formatDate(date: Date | string): string {
    if (!date) return '';
    const d = new Date(date);
    return d.toLocaleDateString('fr-FR');
  }

  /**
   * Calcule l'âge à partir de la date de naissance
   */
  calculateAge(birthDate: Date | string): number {
    if (!birthDate) return 0;
    const today = new Date();
    const birth = new Date(birthDate);
    let age = today.getFullYear() - birth.getFullYear();
    const m = today.getMonth() - birth.getMonth();
    if (m < 0 || (m === 0 && today.getDate() < birth.getDate())) {
      age--;
    }
    return age;
  }

  /**
   * Rafraîchit la liste
   */
  refresh(): void {
    this.loadAdherents();
  }
}

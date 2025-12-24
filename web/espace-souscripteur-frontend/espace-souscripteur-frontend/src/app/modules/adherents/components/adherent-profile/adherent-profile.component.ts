import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { AdherentService } from '@core/services/adherent.service';
import { Adherent, AyantDroit } from '@core/models/adherent.model';
import { HistoriqueConsommation } from '@core/models/prestation.model';

/**
 * Composant du profil détaillé d'un adhérent
 */
@Component({
  selector: 'app-adherent-profile',
  templateUrl: './adherent-profile.component.html',
  styleUrls: ['./adherent-profile.component.scss']
})
export class AdherentProfileComponent implements OnInit {
  adherent: Adherent | null = null;
  ayantsDroit: AyantDroit[] = [];
  historique: HistoriqueConsommation[] = [];
  plafonds: any = null;
  
  loading = true;
  activeTab = 'info';
  
  // Pagination pour l'historique
  currentPage = 0;
  pageSize = 10;

  constructor(
    private adherentService: AdherentService,
    private route: ActivatedRoute,
    private router: Router,
    private translate: TranslateService
  ) {}

  ngOnInit(): void {
    const codeAdherent = this.route.snapshot.paramMap.get('code');
    if (codeAdherent) {
      this.loadAdherentProfile(codeAdherent);
      this.loadAyantsDroit(codeAdherent);
      this.loadHistoriqueConsommation(codeAdherent);
      this.loadPlafonds(codeAdherent);
    }
  }

  /**
   * Charge le profil de l'adhérent
   */
  loadAdherentProfile(codeAdherent: string): void {
    this.loading = true;
    
    this.adherentService.getAdherentProfile(codeAdherent).subscribe({
      next: (adherent) => {
        this.adherent = adherent;
        this.loading = false;
      },
      error: (error) => {
        console.error('Erreur lors du chargement du profil:', error);
        this.loading = false;
        this.router.navigate(['/adherents/list']);
      }
    });
  }

  /**
   * Charge les ayants droit de l'adhérent
   */
  loadAyantsDroit(codeAdherent: string): void {
    this.adherentService.getAyantsDroit(codeAdherent).subscribe({
      next: (ayantsDroit) => {
        this.ayantsDroit = ayantsDroit;
      },
      error: (error) => {
        console.error('Erreur lors du chargement des ayants droit:', error);
      }
    });
  }

  /**
   * Charge l'historique de consommation
   */
  loadHistoriqueConsommation(codeAdherent: string): void {
    this.adherentService.getHistoriqueConsommation(codeAdherent).subscribe({
      next: (historique) => {
        this.historique = historique;
      },
      error: (error) => {
        console.error('Erreur lors du chargement de l\'historique:', error);
      }
    });
  }

  /**
   * Charge les plafonds et taux
   */
  loadPlafonds(codeAdherent: string): void {
    this.adherentService.getPlafonds(codeAdherent).subscribe({
      next: (plafonds) => {
        this.plafonds = plafonds;
      },
      error: (error) => {
        console.error('Erreur lors du chargement des plafonds:', error);
      }
    });
  }

  /**
   * Change d'onglet actif
   */
  setActiveTab(tab: string): void {
    this.activeTab = tab;
  }

  /**
   * Édite l'adhérent
   */
  editAdherent(): void {
    if (this.adherent) {
      this.router.navigate(['/adherents/edit', this.adherent.codeAdherent]);
    }
  }

  /**
   * Upload une photo
   */
  onPhotoUpload(event: any): void {
    const file = event.target.files[0];
    if (file && this.adherent) {
      this.adherentService.uploadPhoto(this.adherent.codeAdherent, file).subscribe({
        next: (response) => {
          if (this.adherent) {
            this.adherent.photo = response.url;
          }
        },
        error: (error) => {
          console.error('Erreur lors de l\'upload de la photo:', error);
        }
      });
    }
  }

  /**
   * Formatte une date
   */
  formatDate(date: Date | string): string {
    if (!date) return '-';
    return new Date(date).toLocaleDateString('fr-FR');
  }

  /**
   * Formatte un montant
   */
  formatCurrency(amount: number): string {
    return new Intl.NumberFormat('fr-FR', {
      style: 'currency',
      currency: 'XAF',
      minimumFractionDigits: 0
    }).format(amount);
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
   * Calcule le pourcentage de plafond utilisé
   */
  getPlafondPercentage(): number {
    if (this.adherent && this.adherent.plafondUtilise && this.plafonds?.plafondTotal) {
      return (this.adherent.plafondUtilise / this.plafonds.plafondTotal) * 100;
    }
    return 0;
  }

  /**
   * Retourne la classe de la barre de progression selon le pourcentage
   */
  getProgressBarClass(): string {
    const percentage = this.getPlafondPercentage();
    if (percentage >= 90) return 'bg-danger';
    if (percentage >= 70) return 'bg-warning';
    return 'bg-success';
  }

  /**
   * Retourne vers la liste
   */
  goBack(): void {
    this.router.navigate(['/adherents/list']);
  }

  /**
   * Imprime la carte d'adhérent
   */
  printCard(): void {
    window.print();
  }

  /**
   * Ajoute un ayant droit
   */
  addAyantDroit(): void {
    if (this.adherent) {
      this.router.navigate(['/adherents', this.adherent.codeAdherent, 'ayants-droit', 'add']);
    }
  }
}

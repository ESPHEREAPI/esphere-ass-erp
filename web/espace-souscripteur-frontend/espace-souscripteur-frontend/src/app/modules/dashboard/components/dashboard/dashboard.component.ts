import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { DashboardService } from '@core/services/dashboard.service';
import {
  DashboardStatistics,
  ConsommationGlobale,
  StatistiquePeriode,
  Alerte,
  PeriodFilter
} from '@core/models/dashboard.model';
import { ChartConfiguration, ChartType } from 'chart.js';

/**
 * Composant du dashboard principal
 */
@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  loading = true;
  dashboardStats: DashboardStatistics | null = null;
  consommationGlobale: ConsommationGlobale | null = null;
  alertes: Alerte[] = [];
  
  // Filtre de période
  selectedPeriod: 'jour' | 'semaine' | 'mois' | 'annee' = 'mois';
  customDateRange = false;
  dateDebut: Date | null = null;
  dateFin: Date | null = null;

  // Configuration des graphiques Chart.js
  periodChartData: ChartConfiguration['data'] | null = null;
  periodChartOptions: ChartConfiguration['options'];
  periodChartType: ChartType = 'line';

  prestationChartData: ChartConfiguration['data'] | null = null;
  prestationChartOptions: ChartConfiguration['options'];
  prestationChartType: ChartType = 'doughnut';

  constructor(
    private dashboardService: DashboardService,
    private translate: TranslateService
  ) {
    this.periodChartOptions = this.getLineChartOptions();
    this.prestationChartOptions = this.getDoughnutChartOptions();
  }

  ngOnInit(): void {
    this.loadDashboardData();
  }

  /**
   * Charge les données du dashboard
   */
  loadDashboardData(): void {
    this.loading = true;

    const periodFilter: PeriodFilter = {
      type: this.selectedPeriod,
      dateDebut: this.dateDebut || undefined,
      dateFin: this.dateFin || undefined
    };

    // Charger les statistiques globales
    this.dashboardService.getDashboardStatistics(periodFilter).subscribe({
      next: (stats) => {
        this.dashboardStats = stats;
        this.consommationGlobale = stats.consommationGlobale;
        this.alertes = stats.alertes;
        
        // Préparer les données pour les graphiques
        this.prepareChartData(stats);
        
        this.loading = false;
      },
      error: (error) => {
        console.error('Erreur lors du chargement du dashboard:', error);
        this.loading = false;
      }
    });
  }

  /**
   * Prépare les données pour les graphiques
   */
  prepareChartData(stats: DashboardStatistics): void {
    // Graphique par période
    if (stats.statistiquesParPeriode && stats.statistiquesParPeriode.length > 0) {
      this.periodChartData = {
        labels: stats.statistiquesParPeriode.map(s => s.periode),
        datasets: [
          {
            label: this.translate.instant('dashboard.total_consumption'),
            data: stats.statistiquesParPeriode.map(s => s.montantTotal),
            borderColor: '#007bff',
            backgroundColor: 'rgba(0, 123, 255, 0.1)',
            tension: 0.4
          },
          {
            label: this.translate.instant('dashboard.total_reimbursed'),
            data: stats.statistiquesParPeriode.map(s => s.montantRembourse),
            borderColor: '#28a745',
            backgroundColor: 'rgba(40, 167, 69, 0.1)',
            tension: 0.4
          }
        ]
      };
    }

    // Graphique par prestation
    if (stats.statistiquesParPrestation && stats.statistiquesParPrestation.length > 0) {
      const topPrestations = stats.statistiquesParPrestation.slice(0, 5);
      this.prestationChartData = {
        labels: topPrestations.map(s => s.typePrestation),
        datasets: [{
          data: topPrestations.map(s => s.montantTotal),
          backgroundColor: [
            '#007bff',
            '#28a745',
            '#ffc107',
            '#dc3545',
            '#17a2b8'
          ]
        }]
      };
    }
  }

  /**
   * Configuration des options pour le graphique en ligne
   */
  getLineChartOptions(): ChartConfiguration['options'] {
    return {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          display: true,
          position: 'top'
        }
      },
      scales: {
        y: {
          beginAtZero: true
        }
      }
    };
  }

  /**
   * Configuration des options pour le graphique en donut
   */
  getDoughnutChartOptions(): ChartConfiguration['options'] {
    return {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          display: true,
          position: 'right'
        }
      }
    };
  }

  /**
   * Change la période de filtrage
   */
  changePeriod(period: 'jour' | 'semaine' | 'mois' | 'annee'): void {
    this.selectedPeriod = period;
    this.customDateRange = false;
    this.dateDebut = null;
    this.dateFin = null;
    this.loadDashboardData();
  }

  /**
   * Active le filtre de période personnalisée
   */
  toggleCustomDateRange(): void {
    this.customDateRange = !this.customDateRange;
    if (!this.customDateRange) {
      this.dateDebut = null;
      this.dateFin = null;
    }
  }

  /**
   * Applique le filtre de dates personnalisées
   */
  applyCustomDateFilter(): void {
    if (this.dateDebut && this.dateFin) {
      this.loadDashboardData();
    }
  }

  /**
   * Marque une alerte comme vue
   */
  markAlerteAsViewed(alerte: Alerte): void {
    this.dashboardService.markAlerteAsViewed(alerte.id).subscribe({
      next: () => {
        alerte.statut = 'vue';
      },
      error: (error) => {
        console.error('Erreur lors du marquage de l\'alerte:', error);
      }
    });
  }

  /**
   * Retourne la classe CSS selon la sévérité de l'alerte
   */
  getAlertClass(severite: string): string {
    switch (severite) {
      case 'critical':
        return 'alert-danger';
      case 'warning':
        return 'alert-warning';
      case 'info':
        return 'alert-info';
      default:
        return 'alert-secondary';
    }
  }

  /**
   * Retourne l'icône selon le type d'alerte
   */
  getAlertIcon(type: string): string {
    switch (type) {
      case 'depassement_plafond':
        return 'fas fa-exclamation-triangle';
      case 'anomalie':
        return 'fas fa-bug';
      case 'visite_repetee':
        return 'fas fa-redo';
      default:
        return 'fas fa-info-circle';
    }
  }

  /**
   * Formatte un montant en devise
   */
  formatCurrency(amount: number): string {
    return new Intl.NumberFormat('fr-FR', {
      style: 'currency',
      currency: 'XAF',
      minimumFractionDigits: 0
    }).format(amount);
  }

  /**
   * Rafraîchit les données du dashboard
   */
  refresh(): void {
    this.loadDashboardData();
  }
}

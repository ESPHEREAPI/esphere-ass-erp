import { Component, OnInit } from '@angular/core';
import { AdminLteService } from '../services/admin-lte-service';
import { ContentHeaderComponent } from '../content-header/content-header.component';
import { Alerte } from '../model/Alerte';
import { DashboardStatistics } from '../model/DashboardStatistics';
import { ConsommationGlobale } from '../model/ConsommationGlobale';
import { Subject, takeUntil } from 'rxjs';
import { DashboardService } from '../services/dashboard.service';
import { Chart, ChartConfiguration, registerables } from 'chart.js';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

// Enregistrer tous les composants de Chart.js
Chart.register(...registerables);
@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [ CommonModule,
    FormsModule,
    ReactiveFormsModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {
  pageTitle: string = 'Dashboard';
  breadcrumbItems = [
    { label: 'Home', route: '/dashboard' },
  { label: 'Dashboard', active: true }
  ];
  constructor(private adminLteService: AdminLteService,public dashboardService: DashboardService)
{

}  
 // Données du tableau de bord
  dashboardData: DashboardStatistics | null = null;
  consommationGlobale: ConsommationGlobale | null = null;
  alertes: Alerte[] = [];
  
  // États de chargement
  loading = false;
  error: string | null = null;
  
  // Filtres
  codeSouscripteur = 'CAMTEL'; // À récupérer du contexte utilisateurFEICOM
  selectedPeriod = 'current-month';
  customDateDebut: string | null = null;
  customDateFin: string | null = null;
  
  // Graphiques
  chartConsommationPeriode: Chart | null = null;
  chartTypePrestation: Chart | null = null;
  chartTopPrestations: Chart | null = null;
  
  // Options de période
  periodOptions = [
    { value: 'last-week', label: '7 derniers jours' },
    { value: 'current-month', label: 'Mois en cours' },
    { value: 'current-year', label: 'Année en cours' },
    { value: 'custom', label: 'Période personnalisée' }
  ];
  
  private destroy$ = new Subject<void>();



  ngOnInit(): void {
    this.loadDashboardData();
    
    // Écoute des changements de données
    this.dashboardService.dashboardData$
      .pipe(takeUntil(this.destroy$))
      .subscribe(data => {
        if (data) {
          this.dashboardData = data;
          this.consommationGlobale = data.consommationGlobale;
          this.alertes = data.alertes || [];
          this.initializeCharts();
        }
      });
    
    // Écoute de l'état de chargement
    this.dashboardService.loading$
      .pipe(takeUntil(this.destroy$))
      .subscribe(loading => this.loading = loading);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
    this.destroyCharts();
  }

  /**
   * Charge les données du tableau de bord
   */
  loadDashboardData(): void {
    this.error = null;
    
    if (this.selectedPeriod === 'custom') {
      if (this.customDateDebut && this.customDateFin) {
        this.dashboardService.getStatistics({
          codeSouscripteur: this.codeSouscripteur,
          dateDebut: this.customDateDebut,
          dateFin: this.customDateFin
        }).subscribe({
          error: (err) => this.handleError(err)
        });
      }
    } else {
      this.dashboardService.loadStatisticsByPeriod(
        this.codeSouscripteur,
        this.selectedPeriod
      ).subscribe({
        error: (err) => this.handleError(err)
      });
    }
  }

  /**
   * Gère le changement de période
   */
  onPeriodChange(): void {
    if (this.selectedPeriod !== 'custom') {
      this.loadDashboardData();
    }
  }

  /**
   * Applique les filtres personnalisés
   */
  applyCustomFilters(): void {
    if (this.customDateDebut && this.customDateFin) {
      this.loadDashboardData();
    }
  }

  /**
   * Initialise tous les graphiques
   */
  initializeCharts(): void {
    setTimeout(() => {
      this.createConsommationPeriodeChart();
      this.createTypePrestationChart();
      this.createTopPrestationsChart();
    }, 100);
  }

  /**
   * Graphique de consommation par période
   */
  createConsommationPeriodeChart(): void {
    const canvas = document.getElementById('chartConsommationPeriode') as HTMLCanvasElement;
    if (!canvas || !this.dashboardData?.statistiquesParPeriode) return;

    this.destroyChart(this.chartConsommationPeriode);

    const ctx = canvas.getContext('2d');
    if (!ctx) return;

    const data = this.dashboardData.statistiquesParPeriode;
    
    this.chartConsommationPeriode = new Chart(ctx, {
      type: 'line',
      data: {
        labels: data.map(s => s.libelle),
        datasets: [
          {
            label: 'Montant Total Ticket Moderateur',
            data: data.map(s => s.montantTotal),
            borderColor: '#007bff',
            backgroundColor: 'rgba(0, 123, 255, 0.1)',
            tension: 0.4,
            fill: true
          },
          {
            label: 'Montant Total Prise En Charge',
            data: data.map(s => s.montantRembourse),
            borderColor: '#28a745',
            backgroundColor: 'rgba(40, 167, 69, 0.1)',
            tension: 0.4,
            fill: true
          }
        ]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            display: true,
            position: 'top'
          },
          tooltip: {
            callbacks: {
              label: (context) => {
                const value = context.parsed.y ?? 0;
                return `${context.dataset.label}: ${this.dashboardService.formatCurrency(value)}`;
              }
            }
          }
        },
        scales: {
          y: {
            beginAtZero: true,
            ticks: {
              callback: (value) => {
                const numValue = typeof value === 'number' ? value : 0;
                return this.dashboardService.formatCurrency(numValue);
              }
            }
          }
        }
      }
    });
  }

  /**
   * Graphique par type de prestation (Donut)
   */
  createTypePrestationChart(): void {
    const canvas = document.getElementById('chartTypePrestation') as HTMLCanvasElement;
    if (!canvas || !this.dashboardData?.statistiquesParTypePrestation) return;

    this.destroyChart(this.chartTypePrestation);

    const ctx = canvas.getContext('2d');
    if (!ctx) return;

    const data = this.dashboardData.statistiquesParTypePrestation;
    
    this.chartTypePrestation = new Chart(ctx, {
      type: 'doughnut',
      data: {
        labels: data.map(s => s.typeNom),
        datasets: [{
          data: data.map(s => s.montantRembourse),
          backgroundColor: [
            '#007bff',
            '#28a745',
            '#ffc107',
            '#dc3545',
            '#17a2b8',
            '#6610f2',
            '#e83e8c'
          ]
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            display: true,
            position: 'right'
          },
          tooltip: {
            callbacks: {
              label: (context) => {
                const value = context.parsed ?? 0;
                const label = context.label || '';
                const percentage = data[context.dataIndex].pourcentageTotal.toFixed(1);
                return `${label}: ${this.dashboardService.formatCurrency(value)} (${percentage}%)`;
              }
            }
          }
        }
      }
    });
  }

  /**
   * Graphique des top prestations (Horizontal Bar)
   */
  createTopPrestationsChart(): void {
    const canvas = document.getElementById('chartTopPrestations') as HTMLCanvasElement;
    if (!canvas || !this.dashboardData?.topPrestations) return;

    this.destroyChart(this.chartTopPrestations);

    const ctx = canvas.getContext('2d');
    if (!ctx) return;

    const data = this.dashboardData.topPrestations;
    
    this.chartTopPrestations = new Chart(ctx, {
      type: 'bar',
      data: {
        labels: data.map(p => p.typenom),
        datasets: [{
          label: 'Nombre d\'utilisations',
          data: data.map(p => p.nombreUtilisations),
          backgroundColor: '#17a2b8'
        }]
      },
      options: {
        indexAxis: 'y',
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            display: false
          },
          tooltip: {
            callbacks: {
              label: (context) => {
                const montant = data[context.dataIndex].montantTotal;
                return [
                  `Utilisations: ${context.parsed.x}`,
                  `Montant: ${this.dashboardService.formatCurrency(montant)}`
                ];
              }
            }
          }
        },
        scales: {
          x: {
            beginAtZero: true
          }
        }
      }
    });
  }

  /**
   * Détruit un graphique
   */
  destroyChart(chart: Chart | null): void {
    if (chart) {
      chart.destroy();
    }
  }

  /**
   * Détruit tous les graphiques
   */
  destroyCharts(): void {
    this.destroyChart(this.chartConsommationPeriode);
    this.destroyChart(this.chartTypePrestation);
    this.destroyChart(this.chartTopPrestations);
  }

  /**
   * Calcule le pourcentage d'utilisation du plafond
   */
  getPlafondPercentage(): number {
    return this.dashboardData?.tauxUtilisationPlafond?.pourcentageUtilisation || 0;
  }

  /**
   * Détermine la classe de couleur pour la barre de progression
   */
  getProgressBarClass(): string {
    const percentage = this.getPlafondPercentage();
    if (percentage >= 90) return 'bg-danger';
    if (percentage >= 70) return 'bg-warning';
    return 'bg-success';
  }

  /**
   * Gère les erreurs
   */
  handleError(error: any): void {
    console.error('Erreur lors du chargement des données:', error);
    this.error = 'Une erreur est survenue lors du chargement des données.';
  }

  /**
   * Rafraîchit les données
   */
  refresh(): void {
    this.loadDashboardData();
  }

  /**
   * Obtient la classe CSS pour une alerte
   */
  getAlertClass(alerte: Alerte): string {
    return this.dashboardService.getAlertClass(alerte.niveau);
  }

  /**
   * Obtient l'icône pour une alerte
   */
  getAlertIcon(alerte: Alerte): string {
    return this.dashboardService.getAlertIcon(alerte.type);
  }

}

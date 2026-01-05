import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { Subject, interval, switchMap, takeUntil, catchError, of, forkJoin } from 'rxjs';
import { CanvasJSAngularChartsModule } from '@canvasjs/angular-charts';
import { StatistiquesMensuelles } from '../../../models/statistiques-mensuelles';
import { EtatPrestation } from '../../../enums/etat-prestation';
import { DashboardService } from '../../../services/dashboard.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, TranslateModule, CanvasJSAngularChartsModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit, OnDestroy {
  
  // Subject pour gérer la désinscription des observables
  private destroy$ = new Subject<void>();
  
  // Statistiques des cards (prestations en attente)
  nbreConsultations: number = 0;
  nbreOrdonnances: number = 0;
  nbreExamens: number = 0;
  
  // Année sélectionnée pour les statistiques
  anneeSelectionnee: number = new Date().getFullYear();
  
  // Liste des années disponibles
  anneesDisponibles: number[] = [];
  
  // Indicateurs de chargement
  isLoadingStats: boolean = false;
  isLoadingChart: boolean = false;
  
  // Messages d'erreur
  errorStats: string = '';
  errorChart: string = '';
  
  // Configuration du graphique CanvasJS
  chartOptions: any = {};

  // Intervalle de rafraîchissement automatique (en millisecondes)
  private readonly REFRESH_INTERVAL = 30000; // 30 secondes

  constructor(private dashboardService: DashboardService) {
    this.initAnneesDisponibles();
  }

  ngOnInit(): void {
    // Chargement initial des données
    this.chargerStatistiquesCards();
    this.chargerStatistiquesGraphique(this.anneeSelectionnee);
    
    // Démarrer le rafraîchissement automatique des cards uniquement
    this.demarrerRafraichissementAutomatique();
  }

  ngOnDestroy(): void {
    // Nettoyage des subscriptions
    this.destroy$.next();
    this.destroy$.complete();
  }

  /**
   * Démarre le rafraîchissement automatique des statistiques des cards
   * Utilise interval() pour actualiser les données toutes les 30 secondes
   * Sans recharger le graphique pour optimiser les performances
   */
  private demarrerRafraichissementAutomatique(): void {
    interval(this.REFRESH_INTERVAL)
      .pipe(
        takeUntil(this.destroy$),
        switchMap(() => this.chargerStatistiquesCardsSilencieux())
      )
      .subscribe({
        next: () => {
          console.log('Statistiques des cards rafraîchies automatiquement');
        },
        error: (error) => {
          console.error('Erreur lors du rafraîchissement automatique:', error);
        }
      });
  }

  /**
   * Initialise la liste des années disponibles (2018-2034)
   */
  private initAnneesDisponibles(): void {
    const anneeDebut = 2018;
    const anneeFin = 2034;
    
    for (let annee = anneeDebut; annee <= anneeFin; annee++) {
      this.anneesDisponibles.push(annee);
    }
  }

  /**
   * Charge les statistiques pour les cards (prestations en attente)
   * Affiche le nombre de consultations, ordonnances et examens en attente de validation
   * Version avec spinner de chargement
   */
  chargerStatistiquesCards(): void {
    this.isLoadingStats = true;
    this.errorStats = '';

    // Utilisation de forkJoin pour charger toutes les statistiques en parallèle
    forkJoin({
      consultations: this.dashboardService.getConsultationsByEtat(EtatPrestation.EN_ATTENTE)
        .pipe(catchError(() => of(0))),
      ordonnances: this.dashboardService.getOrdonnancesByEtat(EtatPrestation.EN_ATTENTE)
        .pipe(catchError(() => of(0))),
      examens: this.dashboardService.getExamensByEtat(EtatPrestation.EN_ATTENTE)
        .pipe(catchError(() => of(0)))
    })
    .pipe(takeUntil(this.destroy$))
    .subscribe({
      next: (results) => {
        this.nbreConsultations = results.consultations;
        this.nbreOrdonnances = results.ordonnances;
        this.nbreExamens = results.examens;
        this.isLoadingStats = false;
      },
      error: (error) => {
        console.error('Erreur chargement statistiques:', error);
        this.errorStats = 'Erreur lors du chargement des statistiques';
        this.isLoadingStats = false;
      }
    });
  }

  /**
   * Charge les statistiques pour les cards de manière silencieuse
   * Sans afficher le spinner de chargement pour ne pas perturber l'utilisateur
   * Utilisé pour le rafraîchissement automatique
   */
  private chargerStatistiquesCardsSilencieux(): any {
    return forkJoin({
      consultations: this.dashboardService.getConsultationsByEtat(EtatPrestation.EN_ATTENTE)
        .pipe(catchError(() => of(this.nbreConsultations))),
      ordonnances: this.dashboardService.getOrdonnancesByEtat(EtatPrestation.EN_ATTENTE)
        .pipe(catchError(() => of(this.nbreOrdonnances))),
      examens: this.dashboardService.getExamensByEtat(EtatPrestation.EN_ATTENTE)
        .pipe(catchError(() => of(this.nbreExamens)))
    })
    .pipe(
      takeUntil(this.destroy$)
    )
    .subscribe({
      next: (results) => {
        // Mise à jour silencieuse des valeurs
        this.nbreConsultations = results.consultations;
        this.nbreOrdonnances = results.ordonnances;
        this.nbreExamens = results.examens;
      },
      error: (error) => {
        console.error('Erreur rafraîchissement silencieux:', error);
        // Ne pas afficher d'erreur à l'utilisateur pour un rafraîchissement automatique
      }
    });
  }

  /**
   * Charge les statistiques mensuelles pour le graphique
   * Affiche uniquement les prestations encaissées
   * @param annee - Année pour laquelle charger les statistiques
   */
  chargerStatistiquesGraphique(annee: number): void {
    this.isLoadingChart = true;
    this.errorChart = '';

    this.dashboardService.getStatistiquesMensuelles(annee)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (data: StatistiquesMensuelles[]) => {
          this.construireGraphique(data);
          this.isLoadingChart = false;
        },
        error: (error) => {
          console.error('Erreur chargement graphique:', error);
          this.errorChart = error.message || 'Erreur lors du chargement du graphique';
          this.isLoadingChart = false;
        }
      });
  }

  /**
   * Construit la configuration du graphique CanvasJS
   * avec les données mensuelles récupérées
   * @param data - Données statistiques mensuelles
   */
  private construireGraphique(data: StatistiquesMensuelles[]): void {
    // Préparation des points de données pour chaque type de prestation
    const dataPointsConsultations = data.map(item => ({
      label: item.mois,
      y: item.consultations
    }));

    const dataPointsOrdonnances = data.map(item => ({
      label: item.mois,
      y: item.ordonnances
    }));

    const dataPointsExamens = data.map(item => ({
      label: item.mois,
      y: item.examens
    }));

    // Configuration du graphique
    this.chartOptions = {
      animationEnabled: true,
      theme: "light2",
      title: {
        text: `Statistiques des Prestations Encaissées - ${this.anneeSelectionnee}`,
        fontSize: 20,
        fontFamily: "Arial",
        fontWeight: "bold"
      },
      axisX: {
        title: "Mois",
        labelAngle: -45,
        labelFontSize: 12
      },
      axisY: {
        title: "Nombre de Prestations",
        includeZero: true,
        gridThickness: 1,
        gridColor: "#E8E8E8"
      },
      toolTip: {
        shared: true,
        contentFormatter: function(e: any) {
          let content = `<strong>${e.entries[0].dataPoint.label}</strong><br/>`;
          e.entries.forEach((entry: any) => {
            content += `${entry.dataSeries.name}: <strong>${entry.dataPoint.y}</strong><br/>`;
          });
          return content;
        }
      },
      legend: {
        cursor: "pointer",
        fontSize: 14,
        itemclick: function(e: any) {
          // Toggle de la visibilité des séries
          if (typeof e.dataSeries.visible === "undefined" || e.dataSeries.visible) {
            e.dataSeries.visible = false;
          } else {
            e.dataSeries.visible = true;
          }
          e.chart.render();
        }
      },
      data: [
        {
          type: "column",
          name: "Consultations",
          legendText: "Consultations",
          showInLegend: true,
          color: "#00C0EF", // Bleu AdminLTE
          dataPoints: dataPointsConsultations
        },
        {
          type: "column",
          name: "Ordonnances",
          legendText: "Ordonnances",
          showInLegend: true,
          color: "#00A65A", // Vert AdminLTE
          dataPoints: dataPointsOrdonnances
        },
        {
          type: "column",
          name: "Examens",
          legendText: "Examens",
          showInLegend: true,
          color: "#F39C12", // Orange AdminLTE
          dataPoints: dataPointsExamens
        }
      ]
    };
  }

  /**
   * Gère le changement d'année dans le select
   * Recharge les statistiques pour la nouvelle année
   * @param event - Événement du select
   */
  onAnneeChange(event: Event): void {
    const selectElement = event.target as HTMLSelectElement;
    const annee = parseInt(selectElement.value, 10);
    
    if (annee && annee !== this.anneeSelectionnee) {
      this.anneeSelectionnee = annee;
      this.chargerStatistiquesGraphique(annee);
    }
  }

  /**
   * Rafraîchit toutes les données du tableau de bord
   * Utilisé par le bouton de rafraîchissement manuel
   */
  rafraichirDonnees(): void {
    this.chargerStatistiquesCards();
    this.chargerStatistiquesGraphique(this.anneeSelectionnee);
  }
}
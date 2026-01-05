import { Injectable } from '@angular/core';
import { DashboardStatistics } from '../model/DashboardStatistics';
import { BehaviorSubject, catchError, Observable, tap } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { DashboardFilters } from '../model/DashboardFilters';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  private apiUrl = `${environment.apiUrl}/gateway-proxy/api/service-biometrie-partenaire/dashboard`;
  
  // Subject pour le partage des données entre composants
  private dashboardDataSubject = new BehaviorSubject<DashboardStatistics | null>(null);
  public dashboardData$ = this.dashboardDataSubject.asObservable();
  
  private loadingSubject = new BehaviorSubject<boolean>(false);
  public loading$ = this.loadingSubject.asObservable();

  constructor(private http: HttpClient) {}

  /**
   * Récupère les statistiques du tableau de bord avec filtres personnalisés
   */
  getStatistics(filters: DashboardFilters): Observable<DashboardStatistics> {
    this.loadingSubject.next(true);
    
    let params = new HttpParams()
      .set('codeSouscripteur', filters.codeSouscripteur);
    
    if (filters.dateDebut) {
      params = params.set('dateDebut', filters.dateDebut);
    }
    
    if (filters.dateFin) {
      params = params.set('dateFin', filters.dateFin);
    }
    
    return this.http.get<DashboardStatistics>(`${this.apiUrl}/statistics`, { params })
      .pipe(
        tap(data => {
          this.dashboardDataSubject.next(data);
          this.loadingSubject.next(false);
        }),
        catchError(error => {
          this.loadingSubject.next(false);
          throw error;
        })
      );
  }

  /**
   * Récupère les statistiques du mois en cours
   */
  getCurrentMonthStatistics(codeSouscripteur: string): Observable<DashboardStatistics> {
    this.loadingSubject.next(true);
    
    const params = new HttpParams()
      .set('codeSouscripteur', codeSouscripteur);
    
    return this.http.get<DashboardStatistics>(`${this.apiUrl}/statistics/current-month`, { params })
      .pipe(
        tap(data => {
          this.dashboardDataSubject.next(data);
          this.loadingSubject.next(false);
        }),
        catchError(error => {
          this.loadingSubject.next(false);
          throw error;
        })
      );
  }

  /**
   * Récupère les statistiques de l'année en cours
   */
  getCurrentYearStatistics(codeSouscripteur: string): Observable<DashboardStatistics> {
    this.loadingSubject.next(true);
    
    const params = new HttpParams()
      .set('codeSouscripteur', codeSouscripteur);
    
    return this.http.get<DashboardStatistics>(`${this.apiUrl}/statistics/current-year`, { params })
      .pipe(
        tap(data => {
          this.dashboardDataSubject.next(data);
          this.loadingSubject.next(false);
        }),
        catchError(error => {
          this.loadingSubject.next(false);
          throw error;
        })
      );
  }

  /**
   * Récupère les statistiques des 7 derniers jours
   */
  getLastWeekStatistics(codeSouscripteur: string): Observable<DashboardStatistics> {
    this.loadingSubject.next(true);
    
    const params = new HttpParams()
      .set('codeSouscripteur', codeSouscripteur);
    
    return this.http.get<DashboardStatistics>(`${this.apiUrl}/statistics/last-week`, { params })
      .pipe(
        tap(data => {
          this.dashboardDataSubject.next(data);
          this.loadingSubject.next(false);
        }),
        catchError(error => {
          this.loadingSubject.next(false);
          throw error;
        })
      );
  }

  /**
   * Charge les statistiques selon le type de période
   */
  loadStatisticsByPeriod(codeSouscripteur: string, periodeType: string): Observable<DashboardStatistics> {
    switch (periodeType) {
      case 'current-month':
        return this.getCurrentMonthStatistics(codeSouscripteur);
      case 'current-year':
        return this.getCurrentYearStatistics(codeSouscripteur);
      case 'last-week':
        return this.getLastWeekStatistics(codeSouscripteur);
      default:
        return this.getCurrentMonthStatistics(codeSouscripteur);
    }
  }

  /**
   * Formate un nombre avec séparateurs de milliers
   */
  formatNumber(value: number): string {
    return new Intl.NumberFormat('fr-FR', {
      minimumFractionDigits: 0,
      maximumFractionDigits: 0
    }).format(value);
  }

  /**
   * Formate un montant en devise
   */
  formatCurrency(value: number): string {
    return new Intl.NumberFormat('fr-FR', {
      style: 'currency',
      currency: 'XAF',
      minimumFractionDigits: 0,
      maximumFractionDigits: 0
    }).format(value);
  }

  /**
   * Formate un pourcentage
   */
  formatPercentage(value: number): string {
    return `${value.toFixed(2)} %`;
  }

  /**
   * Détermine la classe de couleur selon le niveau d'alerte
   */
  getAlertClass(niveau: string): string {
    switch (niveau) {
      case 'CRITICAL':
        return 'danger';
      case 'WARNING':
        return 'warning';
      case 'INFO':
      default:
        return 'info';
    }
  }

  /**
   * Détermine l'icône selon le type d'alerte
   */
  getAlertIcon(type: string): string {
    switch (type) {
      case 'DEPASSEMENT_PLAFOND':
        return 'fas fa-exclamation-triangle';
      case 'CONSOMMATION_ANORMALE':
        return 'fas fa-chart-line';
      case 'VISITE_REPETEE':
        return 'fas fa-redo';
      default:
        return 'fas fa-info-circle';
    }
  }
}

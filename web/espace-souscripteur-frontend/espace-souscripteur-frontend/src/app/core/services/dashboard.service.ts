import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import {
  DashboardStatistics,
  ConsommationGlobale,
  StatistiquePeriode,
  StatistiquePrestation,
  StatistiqueAssure,
  TauxUtilisationPlafond,
  Alerte,
  TopPrestataire,
  TopPrestation,
  PeriodFilter
} from '@core/models/dashboard.model';

/**
 * Service de gestion du dashboard
 */
@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  private apiUrl = `${environment.apiUrl}/dashboard`;

  constructor(private http: HttpClient) {}

  /**
   * Récupère toutes les statistiques du dashboard
   */
  getDashboardStatistics(periodFilter?: PeriodFilter): Observable<DashboardStatistics> {
    let params = new HttpParams();
    
    if (periodFilter) {
      params = params.set('type', periodFilter.type);
      if (periodFilter.dateDebut) {
        params = params.set('dateDebut', periodFilter.dateDebut.toString());
      }
      if (periodFilter.dateFin) {
        params = params.set('dateFin', periodFilter.dateFin.toString());
      }
    }

    return this.http.get<DashboardStatistics>(this.apiUrl, { params });
  }

  /**
   * Récupère la consommation globale
   */
  getConsommationGlobale(periodFilter?: PeriodFilter): Observable<ConsommationGlobale> {
    let params = new HttpParams();
    
    if (periodFilter) {
      params = params.set('type', periodFilter.type);
      if (periodFilter.dateDebut) {
        params = params.set('dateDebut', periodFilter.dateDebut.toString());
      }
      if (periodFilter.dateFin) {
        params = params.set('dateFin', periodFilter.dateFin.toString());
      }
    }

    return this.http.get<ConsommationGlobale>(`${this.apiUrl}/consommation-globale`, { params });
  }

  /**
   * Récupère les statistiques par période
   */
  getStatistiquesParPeriode(
    type: 'jour' | 'semaine' | 'mois' | 'annee',
    dateDebut?: Date,
    dateFin?: Date
  ): Observable<StatistiquePeriode[]> {
    let params = new HttpParams().set('type', type);
    
    if (dateDebut) {
      params = params.set('dateDebut', dateDebut.toISOString());
    }
    if (dateFin) {
      params = params.set('dateFin', dateFin.toISOString());
    }

    return this.http.get<StatistiquePeriode[]>(`${this.apiUrl}/statistiques-periode`, { params });
  }

  /**
   * Récupère les statistiques par type de prestation
   */
  getStatistiquesParPrestation(periodFilter?: PeriodFilter): Observable<StatistiquePrestation[]> {
    let params = new HttpParams();
    
    if (periodFilter) {
      params = params.set('type', periodFilter.type);
      if (periodFilter.dateDebut) {
        params = params.set('dateDebut', periodFilter.dateDebut.toString());
      }
      if (periodFilter.dateFin) {
        params = params.set('dateFin', periodFilter.dateFin.toString());
      }
    }

    return this.http.get<StatistiquePrestation[]>(
      `${this.apiUrl}/statistiques-prestation`,
      { params }
    );
  }

  /**
   * Récupère les statistiques par assuré
   */
  getStatistiquesParAssure(
    limit: number = 10,
    periodFilter?: PeriodFilter
  ): Observable<StatistiqueAssure[]> {
    let params = new HttpParams().set('limit', limit.toString());
    
    if (periodFilter) {
      params = params.set('type', periodFilter.type);
      if (periodFilter.dateDebut) {
        params = params.set('dateDebut', periodFilter.dateDebut.toString());
      }
      if (periodFilter.dateFin) {
        params = params.set('dateFin', periodFilter.dateFin.toString());
      }
    }

    return this.http.get<StatistiqueAssure[]>(`${this.apiUrl}/statistiques-assure`, { params });
  }

  /**
   * Récupère les taux d'utilisation des plafonds
   */
  getTauxUtilisationPlafonds(): Observable<TauxUtilisationPlafond[]> {
    return this.http.get<TauxUtilisationPlafond[]>(`${this.apiUrl}/taux-utilisation-plafonds`);
  }

  /**
   * Récupère les alertes actives
   */
  getAlertes(
    statut?: 'nouvelle' | 'vue' | 'traitee',
    type?: string
  ): Observable<Alerte[]> {
    let params = new HttpParams();
    
    if (statut) {
      params = params.set('statut', statut);
    }
    if (type) {
      params = params.set('type', type);
    }

    return this.http.get<Alerte[]>(`${this.apiUrl}/alertes`, { params });
  }

  /**
   * Marque une alerte comme vue
   */
  markAlerteAsViewed(alerteId: number): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/alertes/${alerteId}/vue`, {});
  }

  /**
   * Marque une alerte comme traitée
   */
  markAlerteAsTreated(alerteId: number): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/alertes/${alerteId}/traitee`, {});
  }

  /**
   * Récupère le top des prestataires
   */
  getTopPrestataires(
    limit: number = 5,
    periodFilter?: PeriodFilter
  ): Observable<TopPrestataire[]> {
    let params = new HttpParams().set('limit', limit.toString());
    
    if (periodFilter) {
      params = params.set('type', periodFilter.type);
      if (periodFilter.dateDebut) {
        params = params.set('dateDebut', periodFilter.dateDebut.toString());
      }
      if (periodFilter.dateFin) {
        params = params.set('dateFin', periodFilter.dateFin.toString());
      }
    }

    return this.http.get<TopPrestataire[]>(`${this.apiUrl}/top-prestataires`, { params });
  }

  /**
   * Récupère le top des prestations
   */
  getTopPrestations(
    limit: number = 5,
    periodFilter?: PeriodFilter
  ): Observable<TopPrestation[]> {
    let params = new HttpParams().set('limit', limit.toString());
    
    if (periodFilter) {
      params = params.set('type', periodFilter.type);
      if (periodFilter.dateDebut) {
        params = params.set('dateDebut', periodFilter.dateDebut.toString());
      }
      if (periodFilter.dateFin) {
        params = params.set('dateFin', periodFilter.dateFin.toString());
      }
    }

    return this.http.get<TopPrestation[]>(`${this.apiUrl}/top-prestations`, { params });
  }

  /**
   * Récupère les données pour les graphiques
   */
  getChartData(
    chartType: 'periode' | 'prestation' | 'prestataire',
    periodFilter?: PeriodFilter
  ): Observable<any> {
    let params = new HttpParams().set('chartType', chartType);
    
    if (periodFilter) {
      params = params.set('type', periodFilter.type);
      if (periodFilter.dateDebut) {
        params = params.set('dateDebut', periodFilter.dateDebut.toString());
      }
      if (periodFilter.dateFin) {
        params = params.set('dateFin', periodFilter.dateFin.toString());
      }
    }

    return this.http.get(`${this.apiUrl}/chart-data`, { params });
  }
}

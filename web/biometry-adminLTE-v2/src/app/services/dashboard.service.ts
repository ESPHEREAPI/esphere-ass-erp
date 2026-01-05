import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { DashboardStats } from '../models/dashboard-stats';
import { catchError, map, Observable, throwError } from 'rxjs';
import { ApiResponse } from '../models/api-response';
import { StatistiquesMensuelles } from '../models/statistiques-mensuelles';
import { EtatPrestation } from '../enums/etat-prestation';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  private readonly API_URL = environment.apiUrl; // https://localhost:8080/gateway-proxy/api/service-biometrie
  private readonly PRESTATION_ENDPOINT = 'ligne-prestations';
  private readonly CONSULTATION_ENDPOINT = 'consultations';
  private readonly DASHBOARD_ENDPOINT = 'dashboard';
// En-têtes HTTP pour les requêtes
  private httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    })
  };

  constructor(private http: HttpClient) { }

  /**
   * Récupère les statistiques générales du tableau de bord
   * Compte les prestations en attente de validation
   * @returns Observable<DashboardStats>
   */
  getDashboardStats(): Observable<DashboardStats> {
    const url = `${this.API_URL}/${this.DASHBOARD_ENDPOINT}/stats`;
    
    return this.http.get<ApiResponse<DashboardStats>>(url, this.httpOptions)
      .pipe(
        map(response => {
          if (response.success && response.data) {
            return response.data;
          }
          throw new Error(response.message || 'Erreur lors de la récupération des statistiques');
        }),
        catchError(this.handleError)
      );
  }

  /**
   * Récupère les statistiques mensuelles pour une année donnée
   * Filtre uniquement les prestations encaissées
   * @param annee - Année pour laquelle récupérer les statistiques
   * @returns Observable<StatistiquesMensuelles[]>
   */
  getStatistiquesMensuelles(annee: number): Observable<StatistiquesMensuelles[]> {
    const url = `${this.API_URL}/${this.DASHBOARD_ENDPOINT}/statistiques-mensuelles/${annee}`;
    
    return this.http.get<ApiResponse<StatistiquesMensuelles[]>>(url, this.httpOptions)
      .pipe(
        map(response => {
          if (response.success && response.data) {
            return response.data;
          }
          throw new Error(response.message || 'Erreur lors de la récupération des statistiques mensuelles');
        }),
        catchError(this.handleError)
      );
  }

  /**
   * Récupère le nombre de consultations par état
   * @param etat - État de la consultation (EN_ATTENTE, ENCAISSE, etc.)
   * @returns Observable<number>
   */
  getConsultationsByEtat(etat: EtatPrestation): Observable<number> {
    const url = `${this.API_URL}/${this.CONSULTATION_ENDPOINT}/count-by-etat/${etat}`;
    
    return this.http.get<ApiResponse<number>>(url, this.httpOptions)
      .pipe(
        map(response => {
          if (response.success && response.data !== undefined) {
            return response.data;
          }
          throw new Error(response.message || 'Erreur lors du comptage des consultations');
        }),
        catchError(this.handleError)
      );
  }

  /**
   * Récupère le nombre d'ordonnances par état
   * Les ordonnances sont filtrées depuis la table LignePrestation avec medicamentId non null
   * @param etat - État de l'ordonnance
   * @returns Observable<number>
   */
  getOrdonnancesByEtat(etat: EtatPrestation): Observable<number> {
    const url = `${this.API_URL}/${this.PRESTATION_ENDPOINT}/ordonnances/count-by-etat/${etat}`;
    
    return this.http.get<ApiResponse<number>>(url, this.httpOptions)
      .pipe(
        map(response => {
          if (response.success && response.data !== undefined) {
            return response.data;
          }
          throw new Error(response.message || 'Erreur lors du comptage des ordonnances');
        }),
        catchError(this.handleError)
      );
  }

  /**
   * Récupère le nombre d'examens par état
   * Les examens sont filtrés depuis la table LignePrestation avec examenId non null
   * @param etat - État de l'examen
   * @returns Observable<number>
   */
  getExamensByEtat(etat: EtatPrestation): Observable<number> {
    const url = `${this.API_URL}/${this.PRESTATION_ENDPOINT}/examens/count-by-etat/${etat}`;
    
    return this.http.get<ApiResponse<number>>(url, this.httpOptions)
      .pipe(
        map(response => {
          if (response.success && response.data !== undefined) {
            return response.data;
          }
          throw new Error(response.message || 'Erreur lors du comptage des examens');
        }),
        catchError(this.handleError)
      );
  }

  /**
   * Récupère les statistiques détaillées pour les graphiques
   * Inclut les montants et volumes par type de prestation
   * @param annee - Année de référence
   * @param mois - Mois optionnel pour filtrer
   * @returns Observable<StatistiquesMensuelles[]>
   */
  getStatistiquesDetaillees(annee: number, mois?: number): Observable<StatistiquesMensuelles[]> {
    let url = `${this.API_URL}/${this.DASHBOARD_ENDPOINT}/statistiques-detaillees?annee=${annee}`;
    
    if (mois) {
      url += `&mois=${mois}`;
    }
    
    return this.http.get<ApiResponse<StatistiquesMensuelles[]>>(url, this.httpOptions)
      .pipe(
        map(response => {
          if (response.success && response.data) {
            return response.data;
          }
          throw new Error(response.message || 'Erreur lors de la récupération des statistiques détaillées');
        }),
        catchError(this.handleError)
      );
  }

  /**
   * Gestion centralisée des erreurs HTTP
   * @param error - Erreur HTTP
   * @returns Observable qui émet une erreur
   */
  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'Une erreur est survenue';

    if (error.error instanceof ErrorEvent) {
      // Erreur côté client
      errorMessage = `Erreur: ${error.error.message}`;
    } else {
      // Erreur côté serveur
      if (error.status === 0) {
        errorMessage = 'Impossible de se connecter au serveur. Vérifiez votre connexion.';
      } else if (error.status === 401) {
        errorMessage = 'Non autorisé. Veuillez vous reconnecter.';
      } else if (error.status === 403) {
        errorMessage = 'Accès interdit.';
      } else if (error.status === 404) {
        errorMessage = 'Ressource non trouvée.';
      } else if (error.status === 500) {
        errorMessage = 'Erreur interne du serveur.';
      } else {
        errorMessage = error.error?.message || `Erreur ${error.status}: ${error.statusText}`;
      }
    }

    console.error('Erreur HTTP:', errorMessage, error);
    return throwError(() => new Error(errorMessage));
}
}

import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import {
  Adherent,
  AyantDroit,
  AdherentFilters,
  AdherentPageResponse
} from '@core/models/adherent.model';
import { HistoriqueConsommation } from '@core/models/prestation.model';

/**
 * Service de gestion des adhérents
 */
@Injectable({
  providedIn: 'root'
})
export class AdherentService {
  private apiUrl = `${environment.apiUrl}/adherents`;

  constructor(private http: HttpClient) {}

  /**
   * Récupère la liste paginée des adhérents
   */
  getAdherents(
    page: number = 0,
    size: number = 10,
    filters?: AdherentFilters
  ): Observable<AdherentPageResponse> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (filters) {
      if (filters.searchTerm) {
        params = params.set('search', filters.searchTerm);
      }
      if (filters.statut) {
        params = params.set('statut', filters.statut);
      }
      if (filters.groupe) {
        params = params.set('groupe', filters.groupe.toString());
      }
      if (filters.police) {
        params = params.set('police', filters.police);
      }
      if (filters.dateDebut) {
        params = params.set('dateDebut', filters.dateDebut.toString());
      }
      if (filters.dateFin) {
        params = params.set('dateFin', filters.dateFin.toString());
      }
    }

    return this.http.get<AdherentPageResponse>(this.apiUrl, { params });
  }

  /**
   * Récupère un adhérent par son code
   */
  getAdherentByCode(codeAdherent: string): Observable<Adherent> {
    return this.http.get<Adherent>(`${this.apiUrl}/${codeAdherent}`);
  }

  /**
   * Récupère le profil détaillé d'un adhérent
   */
  getAdherentProfile(codeAdherent: string): Observable<Adherent> {
    return this.http.get<Adherent>(`${this.apiUrl}/${codeAdherent}/profile`);
  }

  /**
   * Crée un nouvel adhérent
   */
  createAdherent(adherent: Adherent): Observable<Adherent> {
    return this.http.post<Adherent>(this.apiUrl, adherent);
  }

  /**
   * Met à jour un adhérent
   */
  updateAdherent(codeAdherent: string, adherent: Adherent): Observable<Adherent> {
    return this.http.put<Adherent>(`${this.apiUrl}/${codeAdherent}`, adherent);
  }

  /**
   * Supprime un adhérent (soft delete)
   */
  deleteAdherent(codeAdherent: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${codeAdherent}`);
  }

  /**
   * Récupère les ayants droit d'un adhérent
   */
  getAyantsDroit(codeAdherent: string): Observable<AyantDroit[]> {
    return this.http.get<AyantDroit[]>(`${this.apiUrl}/${codeAdherent}/ayants-droit`);
  }

  /**
   * Ajoute un ayant droit à un adhérent
   */
  addAyantDroit(codeAdherent: string, ayantDroit: AyantDroit): Observable<AyantDroit> {
    return this.http.post<AyantDroit>(
      `${this.apiUrl}/${codeAdherent}/ayants-droit`,
      ayantDroit
    );
  }

  /**
   * Met à jour un ayant droit
   */
  updateAyantDroit(
    codeAdherent: string,
    codeAyantDroit: string,
    ayantDroit: AyantDroit
  ): Observable<AyantDroit> {
    return this.http.put<AyantDroit>(
      `${this.apiUrl}/${codeAdherent}/ayants-droit/${codeAyantDroit}`,
      ayantDroit
    );
  }

  /**
   * Supprime un ayant droit
   */
  deleteAyantDroit(codeAdherent: string, codeAyantDroit: string): Observable<void> {
    return this.http.delete<void>(
      `${this.apiUrl}/${codeAdherent}/ayants-droit/${codeAyantDroit}`
    );
  }

  /**
   * Récupère l'historique de consommation d'un adhérent
   */
  getHistoriqueConsommation(
    codeAdherent: string,
    dateDebut?: Date,
    dateFin?: Date
  ): Observable<HistoriqueConsommation[]> {
    let params = new HttpParams();
    if (dateDebut) {
      params = params.set('dateDebut', dateDebut.toISOString());
    }
    if (dateFin) {
      params = params.set('dateFin', dateFin.toISOString());
    }

    return this.http.get<HistoriqueConsommation[]>(
      `${this.apiUrl}/${codeAdherent}/historique-consommation`,
      { params }
    );
  }

  /**
   * Récupère les plafonds et taux de couverture d'un adhérent
   */
  getPlafonds(codeAdherent: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/${codeAdherent}/plafonds`);
  }

  /**
   * Upload la photo d'un adhérent
   */
  uploadPhoto(codeAdherent: string, photo: File): Observable<any> {
    const formData = new FormData();
    formData.append('photo', photo);
    return this.http.post(
      `${this.apiUrl}/${codeAdherent}/photo`,
      formData
    );
  }

  /**
   * Upload une pièce justificative pour un ayant droit
   */
  uploadPieceJustificative(
    codeAdherent: string,
    codeAyantDroit: string,
    type: string,
    file: File
  ): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('type', type);
    return this.http.post(
      `${this.apiUrl}/${codeAdherent}/ayants-droit/${codeAyantDroit}/piece-justificative`,
      formData
    );
  }

  /**
   * Exporte la liste des adhérents
   */
  exportAdherents(format: 'pdf' | 'excel', filters?: AdherentFilters): Observable<Blob> {
    let params = new HttpParams().set('format', format);

    if (filters) {
      if (filters.searchTerm) params = params.set('search', filters.searchTerm);
      if (filters.statut) params = params.set('statut', filters.statut);
      if (filters.groupe) params = params.set('groupe', filters.groupe.toString());
      if (filters.police) params = params.set('police', filters.police);
    }

    return this.http.get(`${this.apiUrl}/export`, {
      params,
      responseType: 'blob'
    });
  }

  /**
   * Recherche d'adhérents avec autocomplete
   */
  searchAdherents(term: string): Observable<Adherent[]> {
    return this.http.get<Adherent[]>(`${this.apiUrl}/search`, {
      params: new HttpParams().set('q', term)
    });
  }
}

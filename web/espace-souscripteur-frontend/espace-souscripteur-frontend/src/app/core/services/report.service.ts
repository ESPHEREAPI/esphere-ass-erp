import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import {
  Report,
  ReportFilters,
  ReportExport,
  ScheduledReport,
  ConsommationVsPlafond
} from '@core/models/notification.model';

/**
 * Service de gestion des rapports
 */
@Injectable({
  providedIn: 'root'
})
export class ReportService {
  private apiUrl = `${environment.apiUrl}/reports`;

  constructor(private http: HttpClient) {}

  /**
   * Génère un rapport
   */
  generateReport(filters: ReportFilters): Observable<Report> {
    return this.http.post<Report>(`${this.apiUrl}/generate`, filters);
  }

  /**
   * Récupère un rapport par son ID
   */
  getReport(reportId: string): Observable<Report> {
    return this.http.get<Report>(`${this.apiUrl}/${reportId}`);
  }

  /**
   * Récupère la liste des rapports générés
   */
  getReports(
    page: number = 0,
    size: number = 10,
    typeRapport?: string
  ): Observable<any> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (typeRapport) {
      params = params.set('typeRapport', typeRapport);
    }

    return this.http.get(`${this.apiUrl}`, { params });
  }

  /**
   * Exporte un rapport en PDF
   */
  exportToPDF(reportId: string, options?: any): Observable<Blob> {
    return this.http.post(`${this.apiUrl}/${reportId}/export/pdf`, options, {
      responseType: 'blob'
    });
  }

  /**
   * Exporte un rapport en Excel
   */
  exportToExcel(reportId: string, options?: any): Observable<Blob> {
    return this.http.post(`${this.apiUrl}/${reportId}/export/excel`, options, {
      responseType: 'blob'
    });
  }

  /**
   * Exporte un rapport en CSV
   */
  exportToCSV(reportId: string): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/${reportId}/export/csv`, {
      responseType: 'blob'
    });
  }

  /**
   * Génère un rapport de consommation par période
   */
  generateConsommationReport(filters: ReportFilters): Observable<Report> {
    return this.http.post<Report>(`${this.apiUrl}/consommation`, filters);
  }

  /**
   * Génère un rapport par type de prestation
   */
  generatePrestationReport(filters: ReportFilters): Observable<Report> {
    return this.http.post<Report>(`${this.apiUrl}/prestation`, filters);
  }

  /**
   * Génère un rapport de consommation vs plafond
   */
  generatePlafondReport(filters: ReportFilters): Observable<ConsommationVsPlafond[]> {
    return this.http.post<ConsommationVsPlafond[]>(`${this.apiUrl}/plafond`, filters);
  }

  /**
   * Récupère les rapports programmés
   */
  getScheduledReports(): Observable<ScheduledReport[]> {
    return this.http.get<ScheduledReport[]>(`${this.apiUrl}/scheduled`);
  }

  /**
   * Crée un rapport programmé
   */
  createScheduledReport(scheduledReport: ScheduledReport): Observable<ScheduledReport> {
    return this.http.post<ScheduledReport>(`${this.apiUrl}/scheduled`, scheduledReport);
  }

  /**
   * Met à jour un rapport programmé
   */
  updateScheduledReport(id: number, scheduledReport: ScheduledReport): Observable<ScheduledReport> {
    return this.http.put<ScheduledReport>(`${this.apiUrl}/scheduled/${id}`, scheduledReport);
  }

  /**
   * Supprime un rapport programmé
   */
  deleteScheduledReport(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/scheduled/${id}`);
  }

  /**
   * Active/désactive un rapport programmé
   */
  toggleScheduledReport(id: number, actif: boolean): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/scheduled/${id}/toggle`, { actif });
  }

  /**
   * Télécharge un fichier exporté
   */
  downloadFile(blob: Blob, filename: string): void {
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = filename;
    link.click();
    window.URL.revokeObjectURL(url);
  }
}

import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { environment } from '@environments/environment';
import { Notification } from '@core/models/notification.model';

/**
 * Service de gestion des notifications
 */
@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private apiUrl = `${environment.apiUrl}/notifications`;
  private unreadCountSubject = new BehaviorSubject<number>(0);
  public unreadCount$ = this.unreadCountSubject.asObservable();

  constructor(private http: HttpClient) {
    this.loadUnreadCount();
  }

  /**
   * Récupère toutes les notifications
   */
  getNotifications(
    page: number = 0,
    size: number = 10,
    statut?: string,
    categorie?: string
  ): Observable<any> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (statut) {
      params = params.set('statut', statut);
    }
    if (categorie) {
      params = params.set('categorie', categorie);
    }

    return this.http.get(`${this.apiUrl}`, { params });
  }

  /**
   * Récupère une notification par son ID
   */
  getNotification(id: number): Observable<Notification> {
    return this.http.get<Notification>(`${this.apiUrl}/${id}`).pipe(
      tap(() => this.loadUnreadCount())
    );
  }

  /**
   * Récupère les notifications non lues
   */
  getUnreadNotifications(): Observable<Notification[]> {
    return this.http.get<Notification[]>(`${this.apiUrl}/unread`);
  }

  /**
   * Récupère le nombre de notifications non lues
   */
  getUnreadCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/unread/count`);
  }

  /**
   * Charge le nombre de notifications non lues
   */
  private loadUnreadCount(): void {
    this.getUnreadCount().subscribe(count => {
      this.unreadCountSubject.next(count);
    });
  }

  /**
   * Marque une notification comme lue
   */
  markAsRead(id: number): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/${id}/read`, {}).pipe(
      tap(() => this.loadUnreadCount())
    );
  }

  /**
   * Marque plusieurs notifications comme lues
   */
  markMultipleAsRead(ids: number[]): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/mark-read`, { ids }).pipe(
      tap(() => this.loadUnreadCount())
    );
  }

  /**
   * Marque toutes les notifications comme lues
   */
  markAllAsRead(): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/mark-all-read`, {}).pipe(
      tap(() => this.loadUnreadCount())
    );
  }

  /**
   * Archive une notification
   */
  archiveNotification(id: number): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/${id}/archive`, {}).pipe(
      tap(() => this.loadUnreadCount())
    );
  }

  /**
   * Supprime une notification
   */
  deleteNotification(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      tap(() => this.loadUnreadCount())
    );
  }

  /**
   * Récupère les notifications par catégorie
   */
  getNotificationsByCategorie(categorie: string): Observable<Notification[]> {
    return this.http.get<Notification[]>(`${this.apiUrl}/categorie/${categorie}`);
  }

  /**
   * Récupère les notifications par type
   */
  getNotificationsByType(type: string): Observable<Notification[]> {
    return this.http.get<Notification[]>(`${this.apiUrl}/type/${type}`);
  }

  /**
   * Récupère les notifications prioritaires
   */
  getPriorityNotifications(): Observable<Notification[]> {
    return this.http.get<Notification[]>(`${this.apiUrl}/priority`);
  }

  /**
   * Crée une nouvelle notification (admin)
   */
  createNotification(notification: Notification): Observable<Notification> {
    return this.http.post<Notification>(this.apiUrl, notification);
  }

  /**
   * Envoie une notification à un utilisateur spécifique
   */
  sendNotificationToUser(userId: string, notification: Notification): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/send/${userId}`, notification);
  }

  /**
   * Envoie une notification à tous les utilisateurs
   */
  broadcastNotification(notification: Notification): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/broadcast`, notification);
  }
}

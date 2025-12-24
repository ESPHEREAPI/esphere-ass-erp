/**
 * Interface pour les notifications
 */
export interface Notification {
  id: number;
  titre: string;
  message: string;
  type: 'info' | 'warning' | 'error' | 'success';
  categorie: 'systeme' | 'plafond' | 'anomalie' | 'nouveaute' | 'incident';
  dateCreation: Date | string;
  dateExpiration?: Date | string;
  statut: 'non_lue' | 'lue' | 'archivee';
  priorite: 'basse' | 'normale' | 'haute' | 'critique';
  destinataireId: string;
  expediteurId?: string;
  metadata?: NotificationMetadata;
  actions?: NotificationAction[];
}

/**
 * Interface pour les métadonnées de notification
 */
export interface NotificationMetadata {
  codeAdherent?: string;
  referenceExterne?: string;
  url?: string;
  icone?: string;
  couleur?: string;
  [key: string]: any;
}

/**
 * Interface pour les actions de notification
 */
export interface NotificationAction {
  libelle: string;
  url: string;
  type: 'lien' | 'bouton' | 'modal';
}

/**
 * Interface pour les filtres de reporting
 */
export interface ReportFilters {
  dateDebut: Date | string;
  dateFin: Date | string;
  typePrestation?: string[];
  police?: string[];
  groupe?: number[];
  statut?: string[];
  codeAdherent?: string;
  prestataireId?: string;
  typeRapport: 'consommation' | 'prestation' | 'plafond' | 'custom';
}

/**
 * Interface pour un rapport généré
 */
export interface Report {
  id: string;
  titre: string;
  typeRapport: string;
  dateGeneration: Date | string;
  filtresAppliques: ReportFilters;
  donnees: ReportData;
  graphiques?: ChartData[];
  resume: ReportSummary;
}

/**
 * Interface pour les données d'un rapport
 */
export interface ReportData {
  lignes: any[];
  colonnes: ReportColumn[];
  totaux?: { [key: string]: number };
  sousTotaux?: { [key: string]: number };
}

/**
 * Interface pour les colonnes d'un rapport
 */
export interface ReportColumn {
  field: string;
  header: string;
  type: 'text' | 'number' | 'date' | 'currency' | 'percent';
  width?: string;
  align?: 'left' | 'center' | 'right';
  sortable?: boolean;
}

/**
 * Interface pour le résumé d'un rapport
 */
export interface ReportSummary {
  totalDepenses: number;
  totalRembourse: number;
  totalACharge: number;
  nombreLignes: number;
  periode: string;
  indicateurs?: { [key: string]: number | string };
}

/**
 * Interface pour l'export de rapport
 */
export interface ReportExport {
  format: 'pdf' | 'excel' | 'csv';
  nom: string;
  rapport: Report;
  options?: ExportOptions;
}

/**
 * Interface pour les options d'export
 */
export interface ExportOptions {
  orientation?: 'portrait' | 'landscape';
  includeGraphiques?: boolean;
  includeResume?: boolean;
  logoEntreprise?: boolean;
  [key: string]: any;
}

/**
 * Interface pour les rapports programmés
 */
export interface ScheduledReport {
  id: number;
  nom: string;
  typeRapport: string;
  filtres: ReportFilters;
  frequence: 'quotidien' | 'hebdomadaire' | 'mensuel' | 'trimestriel' | 'annuel';
  jourEnvoi?: number;
  heureEnvoi: string;
  destinataires: string[];
  formatExport: 'pdf' | 'excel' | 'csv';
  actif: boolean;
  derniereExecution?: Date | string;
  prochaineExecution?: Date | string;
}

/**
 * Interface pour les statistiques de consommation vs plafond
 */
export interface ConsommationVsPlafond {
  police: string;
  groupe: number;
  typePrestation: string;
  plafondDefini: number;
  consommationActuelle: number;
  plafondRestant: number;
  tauxUtilisation: number;
  nombreAdherents: number;
  nombreDepassements: number;
}

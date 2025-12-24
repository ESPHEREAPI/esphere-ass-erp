/**
 * Interface pour les statistiques du dashboard
 */
export interface DashboardStatistics {
  consommationGlobale: ConsommationGlobale;
  statistiquesParPeriode: StatistiquePeriode[];
  statistiquesParPrestation: StatistiquePrestation[];
  statistiquesParAssure: StatistiqueAssure[];
  tauxUtilisationPlafonds: TauxUtilisationPlafond[];
  alertes: Alerte[];
  topPrestataires: TopPrestataire[];
  topPrestations: TopPrestation[];
}

/**
 * Interface pour la consommation globale
 */
export interface ConsommationGlobale {
  totalDepenses: number;
  totalRembourse: number;
  totalACharge: number;
  nombreConsultations: number;
  nombrePrestations: number;
  tauxRemboursementMoyen: number;
  evolution: {
    pourcentage: number;
    sens: 'hausse' | 'baisse' | 'stable';
  };
}

/**
 * Interface pour les statistiques par période
 */
export interface StatistiquePeriode {
  periode: string;
  type: 'jour' | 'semaine' | 'mois' | 'annee';
  montantTotal: number;
  montantRembourse: number;
  nombreActes: number;
  dateDebut: Date | string;
  dateFin: Date | string;
}

/**
 * Interface pour les statistiques par prestation
 */
export interface StatistiquePrestation {
  typePrestation: string;
  nombreActes: number;
  montantTotal: number;
  montantRembourse: number;
  pourcentage: number;
}

/**
 * Interface pour les statistiques par assuré
 */
export interface StatistiqueAssure {
  codeAdherent: string;
  nomComplet: string;
  nombreConsultations: number;
  montantTotal: number;
  montantRembourse: number;
  plafondUtilise: number;
  tauxUtilisation: number;
}

/**
 * Interface pour le taux d'utilisation des plafonds
 */
export interface TauxUtilisationPlafond {
  police: string;
  groupe: number;
  plafondTotal: number;
  plafondUtilise: number;
  plafondRestant: number;
  tauxUtilisation: number;
  nombreAdherents: number;
}

/**
 * Interface pour les alertes
 */
export interface Alerte {
  id: number;
  type: 'depassement_plafond' | 'anomalie' | 'visite_repetee' | 'info';
  severite: 'critical' | 'warning' | 'info';
  titre: string;
  message: string;
  codeAdherent?: string;
  nomAdherent?: string;
  dateDetection: Date | string;
  statut: 'nouvelle' | 'vue' | 'traitee';
  details?: any;
}

/**
 * Interface pour le top prestataires
 */
export interface TopPrestataire {
  prestataireId: string;
  nomPrestataire: string;
  categoriePrestataire: string;
  nombreActes: number;
  montantTotal: number;
  rang: number;
}

/**
 * Interface pour le top prestations
 */
export interface TopPrestation {
  typePrestationId: string;
  nomPrestation: string;
  nombreActes: number;
  montantTotal: number;
  montantMoyen: number;
  rang: number;
}

/**
 * Interface pour les filtres de période
 */
export interface PeriodFilter {
  type: 'jour' | 'semaine' | 'mois' | 'annee' | 'personnalise';
  dateDebut?: Date | string;
  dateFin?: Date | string;
}

/**
 * Interface pour les données de graphique
 */
export interface ChartData {
  labels: string[];
  datasets: ChartDataset[];
}

export interface ChartDataset {
  label: string;
  data: number[];
  backgroundColor?: string | string[];
  borderColor?: string | string[];
  borderWidth?: number;
}

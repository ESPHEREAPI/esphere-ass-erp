/**
 * Interface représentant un adhérent (assuré principal)
 */
export interface Adherent {
  numero: number;
  codeAdherent: string;
  assurePrincipal: string;
  naissance: Date | string;
  sexe: string;
  matricule: string;
  telephone: string;
  taux: number;
  souscripteur: string;
  police: string;
  effetPolice: Date | string;
  echeancePolice: Date | string;
  groupe: number;
  enrole: string;
  dateEnrole: Date | string;
  imprime: string;
  statut: string;
  ayantsDroit?: AyantDroit[];
  // Champs calculés pour le frontend
  photo?: string;
  consommationTotale?: number;
  plafondUtilise?: number;
  plafondRestant?: number;
}

/**
 * Interface représentant un ayant droit
 */
export interface AyantDroit {
  codeAyantDroit: string;
  nom: string;
  sexe: string;
  naissance: Date | string;
  lienpare: string;
  telephone: string;
  police: string;
  enrole: string;
  dateEnrole: Date | string;
  statut: string;
  codeAdherent: string;
  // Champs calculés
  photo?: string;
  age?: number;
}

/**
 * Interface pour les filtres de recherche d'adhérents
 */
export interface AdherentFilters {
  searchTerm?: string;
  statut?: string;
  groupe?: number;
  police?: string;
  dateDebut?: Date | string;
  dateFin?: Date | string;
}

/**
 * Interface pour la réponse paginée d'adhérents
 */
export interface AdherentPageResponse {
  content: Adherent[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

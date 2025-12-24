/**
 * Interface représentant une consultation médicale
 */
export interface Consultation {
  id: number;
  taux: number;
  natureConsultation: string;
  natureAffection: string;
  montant: number;
  montantModif: number;
  date: Date | string;
  dateValideRejete: Date | string;
  observations: string;
  etatConsultation: string;
  supprime: string;
  visiteId: number;
  employeValideRejeteId: number;
  typeConsultation: TypePrestation;
  // Champs calculés
  montantRembourse?: number;
  montantACharge?: number;
}

/**
 * Interface représentant une prestation
 */
export interface Prestation {
  id: number;
  naturePrestation: string;
  date: Date | string;
  supprime: string;
  visiteId: number;
  prestataireId: string;
  lignesPrestations?: LignePrestation[];
  // Champs calculés
  montantTotal?: number;
  montantRembourse?: number;
}

/**
 * Interface représentant une ligne de prestation
 */
export interface LignePrestation {
  id: number;
  taux: number;
  dentsConcernees: string;
  codification: string;
  nom: string;
  valeur: number;
  nbre: number;
  actePrelevement: number;
  valeurModif: number;
  nbreModif: number;
  actePrelevementModif: number;
  posologie: string;
  observations: string;
  observationsActePrelevement: string;
  date: Date | string;
  dateValideRejete: Date | string;
  dateEncaisse: Date | string;
  etat: string;
  supprime: string;
  prestationId: number;
  prestataireId: string;
  typeExamen: TypePrestation;
  descriptionSoins: TypePrestation;
  // Champs calculés
  montantTotal?: number;
  montantRembourse?: number;
}

/**
 * Interface représentant un type de prestation
 */
export interface TypePrestation {
  id: string;
  nom: string;
  affiche: number;
  categorie: string;
}

/**
 * Interface représentant un taux de prestation
 */
export interface TauxPrestation {
  id: number;
  typePrestationId: string;
  police: string;
  groupe: number;
  taux: number;
  plafond: number;
}

/**
 * Interface représentant un prestataire
 */
export interface Prestataire {
  id: string;
  nom: string;
  adresse: string;
  email: string;
  telephone: string;
  registre: string;
  logo: string;
  statut: string;
  supprime: string;
  categorieId: string;
  villeId: string;
}

/**
 * Interface pour l'historique de consommation
 */
export interface HistoriqueConsommation {
  date: Date | string;
  typePrestation: string;
  prestataire: string;
  montant: number;
  montantRembourse: number;
  montantACharge: number;
  statut: string;
}

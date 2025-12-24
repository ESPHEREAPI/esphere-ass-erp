/**
 * Interface pour l'utilisateur authentifié
 */
export interface User {
  id: string;
  username: string;
  email: string;
  nom: string;
  prenom: string;
  role: UserRole;
  souscripteurId?: string;
  permissions: string[];
  photo?: string;
  telephone?: string;
  actif: boolean;
  derniereConnexion?: Date | string;
}

/**
 * Énumération des rôles utilisateur
 */
export enum UserRole {
  ADMIN = 'ADMIN',
  SOUSCRIPTEUR = 'SOUSCRIPTEUR',
  ASSURE = 'ASSURE',
  COURTIER = 'COURTIER',
  PRESTATAIRE = 'PRESTATAIRE'
}

/**
 * Interface pour les credentials de connexion
 */
export interface LoginCredentials {
  username: string;
  password: string;
  rememberMe?: boolean;
}

/**
 * Interface pour la réponse d'authentification
 */
export interface AuthResponse {
  token: string;
  refreshToken?: string;
  tokenType: string;
  expiresIn: number;
  user: User;
}

/**
 * Interface pour le changement de mot de passe
 */
export interface ChangePassword {
  ancienMotDePasse: string;
  nouveauMotDePasse: string;
  confirmationMotDePasse: string;
}

/**
 * Interface pour la réinitialisation de mot de passe
 */
export interface ResetPassword {
  email: string;
  token?: string;
  nouveauMotDePasse?: string;
}

/**
 * Interface pour le profil utilisateur
 */
export interface UserProfile {
  id: string;
  username: string;
  email: string;
  nom: string;
  prenom: string;
  telephone?: string;
  adresse?: string;
  photo?: string;
  preferences?: UserPreferences;
}

/**
 * Interface pour les préférences utilisateur
 */
export interface UserPreferences {
  langue: 'fr' | 'en';
  theme: 'light' | 'dark';
  notificationsEmail: boolean;
  notificationsSMS: boolean;
  affichageParPage: number;
  formatDate: string;
  formatMontant: string;
  dashboardWidgets?: string[];
}

/**
 * Interface pour les permissions
 */
export interface Permission {
  code: string;
  module: string;
  action: 'read' | 'create' | 'update' | 'delete';
  description: string;
}

/**
 * Interface pour le souscripteur
 */
export interface Souscripteur {
  id: string;
  nom: string;
  raisonSociale: string;
  email: string;
  telephone: string;
  adresse: string;
  ville: string;
  pays: string;
  logo?: string;
  numeroContrat: string;
  dateAdhesion: Date | string;
  statut: 'actif' | 'suspendu' | 'resilie';
  nombreAdherents: number;
  polices: string[];
}

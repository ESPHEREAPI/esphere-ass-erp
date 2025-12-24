# Espace Souscripteur - Frontend Angular 18

Application frontend pour la gestion de la couverture santé avec Angular 18, AdminLTE v3 et internationalisation FR/EN.

## 📋 Prérequis

- Node.js 18.x ou supérieur
- npm 9.x ou supérieur
- Angular CLI 18.x

## 🚀 Installation

### 1. Installer les dépendances

```bash
cd espace-souscripteur-frontend
npm install
```

### 2. Installer Angular CLI (si nécessaire)

```bash
npm install -g @angular/cli@18
```

### 3. Configuration de l'environnement

Modifier `src/environments/environment.ts` pour pointer vers votre API backend:

```typescript
apiUrl: 'http://localhost:8080/api/v1'
```

## 🏃 Démarrage

### Mode développement

```bash
npm start
# ou
ng serve
```

L'application sera accessible sur `http://localhost:4200`

### Build de production

```bash
npm run build
# ou
ng build --configuration production
```

Les fichiers compilés seront dans le dossier `dist/`

## 📁 Structure du projet

```
src/
├── app/
│   ├── core/                          # Services et fonctionnalités core
│   │   ├── guards/                    # Guards de navigation
│   │   │   └── auth.guard.ts
│   │   ├── interceptors/              # Intercepteurs HTTP
│   │   │   ├── jwt.interceptor.ts
│   │   │   └── error.interceptor.ts
│   │   ├── models/                    # Interfaces TypeScript
│   │   │   ├── adherent.model.ts
│   │   │   ├── prestation.model.ts
│   │   │   ├── dashboard.model.ts
│   │   │   ├── notification.model.ts
│   │   │   └── auth.model.ts
│   │   └── services/                  # Services métier
│   │       ├── auth.service.ts
│   │       ├── adherent.service.ts
│   │       ├── dashboard.service.ts
│   │       ├── report.service.ts
│   │       └── notification.service.ts
│   │
│   ├── modules/                       # Modules fonctionnels
│   │   ├── dashboard/                 # Module Dashboard
│   │   │   ├── components/
│   │   │   │   └── dashboard/
│   │   │   │       ├── dashboard.component.ts
│   │   │   │       ├── dashboard.component.html
│   │   │   │       └── dashboard.component.scss
│   │   │   └── dashboard.module.ts
│   │   │
│   │   ├── adherents/                 # Module Adhérents
│   │   │   ├── components/
│   │   │   │   ├── adherents-list/
│   │   │   │   ├── adherent-profile/
│   │   │   │   ├── adherent-form/
│   │   │   │   └── ayant-droit-form/
│   │   │   └── adherents.module.ts
│   │   │
│   │   ├── reporting/                 # Module Reporting
│   │   │   └── reporting.module.ts
│   │   │
│   │   └── notifications/             # Module Notifications
│   │       └── notifications.module.ts
│   │
│   ├── shared/                        # Composants partagés
│   │   ├── components/
│   │   │   ├── layout/
│   │   │   │   ├── main-layout/
│   │   │   │   │   ├── main-layout.component.ts
│   │   │   │   │   ├── main-layout.component.html
│   │   │   │   │   └── main-layout.component.scss
│   │   │   │   └── login-layout/
│   │   │   ├── breadcrumb/
│   │   │   ├── loading-spinner/
│   │   │   └── confirmation-dialog/
│   │   ├── directives/
│   │   ├── pipes/
│   │   └── shared.module.ts
│   │
│   ├── app-routing.module.ts          # Configuration des routes
│   ├── app.component.ts
│   └── app.module.ts
│
├── assets/
│   ├── i18n/                          # Fichiers de traduction
│   │   ├── fr.json
│   │   └── en.json
│   ├── img/                           # Images
│   └── css/                           # Styles globaux
│
├── environments/                      # Configuration des environnements
│   ├── environment.ts
│   └── environment.prod.ts
│
└── styles.scss                        # Styles globaux
```

## 🎨 AdminLTE - Composants utilisés

### Widgets du Dashboard

- **Small Box**: Cartes de statistiques avec icône
- **Info Box**: Boîtes d'information compactes
- **Card**: Conteneurs de contenu avec header/body/footer
- **Chart.js Integration**: Graphiques interactifs

### Composants de formulaire

- **Input Groups**: Groupes de champs avec icônes
- **Select2 Integration**: Sélecteurs avancés
- **Date Pickers**: Sélecteurs de dates
- **File Upload**: Upload de fichiers avec preview

### Tables

- **DataTables**: Tables avec tri, recherche, pagination
- **Responsive Tables**: Tables adaptatives
- **Hover Effects**: Effets au survol

### Navigation

- **Sidebar Menu**: Menu latéral avec sous-menus
- **Navbar**: Barre de navigation supérieure
- **Breadcrumbs**: Fil d'Ariane

## 🌍 Internationalisation

### Langues supportées

- **Français (fr)** - Par défaut
- **Anglais (en)**

### Ajouter une nouvelle langue

1. Créer le fichier de traduction: `src/assets/i18n/xx.json`
2. Ajouter la langue dans `environment.ts`:

```typescript
supportedLanguages: ['fr', 'en', 'xx']
```

3. Utiliser dans les templates:

```html
{{ 'key.translation' | translate }}
```

## 🔒 Authentification & Sécurité

### JWT Token

Les tokens JWT sont stockés dans localStorage et automatiquement ajoutés aux requêtes HTTP via l'intercepteur `JwtInterceptor`.

### Guards de navigation

```typescript
// Route protégée par authentification
{
  path: 'dashboard',
  component: DashboardComponent,
  canActivate: [AuthGuard]
}

// Route avec vérification de rôle
{
  path: 'admin',
  component: AdminComponent,
  canActivate: [AuthGuard],
  data: { roles: ['ADMIN', 'SOUSCRIPTEUR'] }
}

// Route avec vérification de permission
{
  path: 'adherents',
  component: AdherentsComponent,
  canActivate: [AuthGuard],
  data: { permissions: ['adherents.view'] }
}
```

### Gestion des erreurs

L'intercepteur `ErrorInterceptor` gère automatiquement:
- Erreurs HTTP 4xx et 5xx
- Affichage de messages d'erreur traduits
- Logging en mode développement

## 📊 Modules principaux

### 1. Dashboard

**Fonctionnalités:**
- Consommation globale
- Statistiques par période (jour, semaine, mois, année)
- Statistiques par prestation
- Alertes en temps réel
- Top prestataires et prestations

**Composants:**
- `DashboardComponent`: Vue principale avec widgets
- Charts: Line chart, Doughnut chart

### 2. Gestion des Adhérents

**Fonctionnalités:**
- Liste paginée avec filtres
- Recherche avec debounce
- Profil détaillé
- Gestion des ayants droit
- Historique de consommation
- Export PDF/Excel

**Composants:**
- `AdherentsListComponent`: Liste avec table
- `AdherentProfileComponent`: Profil détaillé
- `AdherentFormComponent`: Formulaire création/édition
- `AyantDroitFormComponent`: Formulaire ayant droit

### 3. Reporting

**Fonctionnalités:**
- Génération de rapports dynamiques
- Filtres multiples
- Export PDF, Excel, CSV
- Rapports programmés
- Graphiques comparatifs

### 4. Notifications

**Fonctionnalités:**
- Liste des notifications
- Badge de compteur dans la navbar
- Marquage lu/non lu
- Archivage
- Catégories et types

## 🎯 Services API

### Exemple d'utilisation

```typescript
import { AdherentService } from '@core/services/adherent.service';

constructor(private adherentService: AdherentService) {}

loadAdherents(): void {
  const filters = {
    statut: 'actif',
    groupe: 1
  };
  
  this.adherentService.getAdherents(0, 10, filters)
    .subscribe({
      next: (response) => {
        this.adherents = response.content;
        this.totalElements = response.totalElements;
      },
      error: (error) => {
        console.error('Error:', error);
      }
    });
}
```

## 🧪 Tests

### Tests unitaires

```bash
npm test
# ou
ng test
```

### Tests end-to-end

```bash
npm run e2e
# ou
ng e2e
```

## 📦 Build et déploiement

### Build de production

```bash
ng build --configuration production
```

### Optimisations appliquées

- Minification du code
- Tree shaking
- Optimisation des images
- Lazy loading des modules
- AOT compilation

### Déploiement

Les fichiers compilés dans `dist/` peuvent être déployés sur:
- Serveur web (Apache, Nginx)
- Services cloud (AWS S3, Azure, Google Cloud)
- Plateformes de déploiement (Netlify, Vercel)

## 🔧 Configuration recommandée Nginx

```nginx
server {
    listen 80;
    server_name votre-domaine.com;
    root /var/www/espace-souscripteur-frontend/dist;
    index index.html;

    location / {
        try_files $uri $uri/ /index.html;
    }

    # Cache des assets statiques
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg)$ {
        expires 1y;
        add_header Cache-Control "public, immutable";
    }
}
```

## 📝 Conventions de code

### Naming

- **Composants**: PascalCase (ex: `AdherentProfileComponent`)
- **Services**: PascalCase avec suffix Service (ex: `AdherentService`)
- **Variables**: camelCase (ex: `currentUser`)
- **Constants**: UPPER_SNAKE_CASE (ex: `API_URL`)

### Structure des fichiers

```
component-name/
├── component-name.component.ts
├── component-name.component.html
├── component-name.component.scss
└── component-name.component.spec.ts
```

## 🐛 Debugging

### Logs en développement

```typescript
if (!environment.production) {
  console.log('Debug info:', data);
}
```

### Angular DevTools

Installer l'extension Chrome Angular DevTools pour:
- Inspection des composants
- Performance profiling
- Debug du state management

## 🤝 Contribution

1. Créer une branche feature: `git checkout -b feature/nom-feature`
2. Commiter les changements: `git commit -m 'Add feature'`
3. Pusher la branche: `git push origin feature/nom-feature`
4. Créer une Pull Request

## 📄 Licence

Copyright © 2025 - Tous droits réservés

## 👥 Support

Pour toute question ou problème:
- Email: support@espaceclient.com
- Documentation: https://docs.espaceclient.com

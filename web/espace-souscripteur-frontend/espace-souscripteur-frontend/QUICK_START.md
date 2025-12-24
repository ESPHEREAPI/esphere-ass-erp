# 🚀 Guide de Démarrage Rapide - Espace Souscripteur Frontend

## ⚡ Installation en 5 minutes

### 1. Prérequis système

```bash
# Vérifier Node.js (version 18+)
node --version

# Vérifier npm (version 9+)
npm --version
```

Si Node.js n'est pas installé: [https://nodejs.org](https://nodejs.org)

### 2. Installation du projet

```bash
# Cloner le repository (si applicable)
git clone https://github.com/votre-org/espace-souscripteur-frontend.git
cd espace-souscripteur-frontend

# Installer les dépendances
npm install

# Installer Angular CLI globalement (si nécessaire)
npm install -g @angular/cli@18
```

### 3. Configuration

#### Configurer l'URL de l'API

Éditer `src/environments/environment.ts`:

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api/v1',  // <- Modifier ici
  // ... autres configurations
};
```

### 4. Lancer l'application

```bash
npm start
```

✅ L'application est accessible sur: **http://localhost:4200**

---

## 📋 Checklist Post-Installation

### ✓ Vérifications à effectuer

- [ ] L'application se lance sans erreurs
- [ ] La page de login s'affiche correctement
- [ ] Le backend API est accessible
- [ ] Les fichiers de traduction (FR/EN) fonctionnent
- [ ] AdminLTE est correctement chargé (sidebar, navbar)

### 🔍 Test de connexion

**Credentials de test (à adapter selon votre backend):**

```
Username: souscripteur@test.com
Password: Test123!
```

---

## 🎨 Personnalisation rapide

### Changer le logo

Remplacer le fichier: `src/assets/img/logo.png`

### Modifier les couleurs

Éditer `src/styles.scss`:

```scss
$primary-color: #007bff;  // Couleur principale
$success-color: #28a745;  // Couleur succès
$warning-color: #ffc107;  // Couleur avertissement
$danger-color: #dc3545;   // Couleur danger
```

### Adapter les traductions

Éditer les fichiers:
- Français: `src/assets/i18n/fr.json`
- Anglais: `src/assets/i18n/en.json`

---

## 🔑 Connexion avec le Backend

### API Endpoints requis

Le frontend attend que le backend expose ces endpoints:

#### Authentification
```
POST /api/v1/auth/login
POST /api/v1/auth/refresh-token
POST /api/v1/auth/logout
```

#### Dashboard
```
GET  /api/v1/dashboard
GET  /api/v1/dashboard/consommation-globale
GET  /api/v1/dashboard/statistiques-periode
GET  /api/v1/dashboard/alertes
```

#### Adhérents
```
GET    /api/v1/adherents
GET    /api/v1/adherents/:code
POST   /api/v1/adherents
PUT    /api/v1/adherents/:code
DELETE /api/v1/adherents/:code
GET    /api/v1/adherents/:code/ayants-droit
```

#### Reporting
```
POST /api/v1/reports/generate
GET  /api/v1/reports/:id/export/pdf
GET  /api/v1/reports/:id/export/excel
```

#### Notifications
```
GET   /api/v1/notifications
GET   /api/v1/notifications/unread/count
PATCH /api/v1/notifications/:id/read
```

### Configuration CORS sur le Backend

Le backend doit autoriser les requêtes depuis `http://localhost:4200`:

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
```

---

## 🛠️ Commandes Utiles

### Développement

```bash
# Démarrer en mode dev
npm start

# Démarrer avec un port différent
ng serve --port 4300

# Démarrer et ouvrir automatiquement le navigateur
ng serve --open
```

### Build

```bash
# Build de développement
ng build

# Build de production
ng build --configuration production

# Build avec analyse de bundle
ng build --stats-json
npm run analyze
```

### Tests

```bash
# Tests unitaires
npm test

# Tests avec couverture
ng test --code-coverage

# Tests E2E
npm run e2e
```

### Code Quality

```bash
# Linter
ng lint

# Formatter (si Prettier installé)
npm run format

# Vérifier les types TypeScript
npm run type-check
```

---

## 🐛 Résolution des problèmes courants

### Problème: `npm install` échoue

**Solution:**
```bash
# Nettoyer le cache npm
npm cache clean --force

# Supprimer node_modules et package-lock.json
rm -rf node_modules package-lock.json

# Réinstaller
npm install
```

### Problème: Erreur CORS lors des appels API

**Solutions:**
1. Vérifier que le backend autorise `http://localhost:4200`
2. Vérifier l'URL de l'API dans `environment.ts`
3. Utiliser un proxy (voir ci-dessous)

### Configurer un proxy (optionnel)

Créer `proxy.conf.json`:

```json
{
  "/api": {
    "target": "http://localhost:8080",
    "secure": false,
    "changeOrigin": true
  }
}
```

Modifier `package.json`:

```json
"scripts": {
  "start": "ng serve --proxy-config proxy.conf.json"
}
```

Changer `environment.ts`:

```typescript
apiUrl: '/api/v1'  // Au lieu de http://localhost:8080/api/v1
```

### Problème: AdminLTE ne s'affiche pas correctement

**Solution:**
```bash
# Réinstaller les dépendances AdminLTE
npm install admin-lte@3.2.0 --save

# Vérifier que les styles sont importés dans angular.json
```

### Problème: Traductions ne fonctionnent pas

**Vérifications:**
1. Les fichiers JSON sont dans `src/assets/i18n/`
2. ngx-translate est configuré dans `app.module.ts`
3. La langue par défaut est définie

---

## 📚 Ressources

### Documentation

- [Angular Documentation](https://angular.io/docs)
- [AdminLTE Documentation](https://adminlte.io/docs/3.0/)
- [ngx-translate](https://github.com/ngx-translate/core)
- [Chart.js](https://www.chartjs.org/docs/)

### Communauté

- [Angular GitHub](https://github.com/angular/angular)
- [Stack Overflow - Angular](https://stackoverflow.com/questions/tagged/angular)

---

## 🎯 Prochaines étapes

Une fois l'application lancée:

1. ✅ **Tester la connexion** avec les credentials de test
2. ✅ **Explorer le dashboard** pour vérifier les statistiques
3. ✅ **Vérifier la liste des adhérents** et la pagination
4. ✅ **Tester les filtres** et la recherche
5. ✅ **Consulter un profil adhérent** complet
6. ✅ **Générer un rapport** et tester l'export
7. ✅ **Changer la langue** (FR/EN)
8. ✅ **Vérifier les notifications**

---

## 📞 Support

En cas de blocage:

1. **Consulter les logs de la console** (F12 dans le navigateur)
2. **Vérifier les logs du terminal** où tourne `ng serve`
3. **Consulter le README.md** pour plus de détails
4. **Contacter le support technique**

---

## ✨ Fonctionnalités principales

### Module Dashboard ✅
- Consommation globale en temps réel
- Graphiques interactifs (période, prestations)
- Alertes et notifications
- Top prestataires/prestations

### Module Adhérents ✅
- Liste paginée avec recherche
- Filtres avancés (statut, groupe, police)
- Profil détaillé avec historique
- Gestion des ayants droit
- Export PDF/Excel

### Module Reporting ✅
- Rapports personnalisables
- Multiples formats d'export
- Filtres par période, prestation, police
- Rapports programmés

### Module Notifications ✅
- Notifications en temps réel
- Badge de compteur
- Catégorisation (système, plafond, anomalie)
- Gestion lu/non lu

---

Bon développement ! 🚀

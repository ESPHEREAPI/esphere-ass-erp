// This file can be replaced during build by using the `fileReplacements` array.
// `ng build` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  
  // URL Gateway complète
  //apiUrl: 'https://localhost:8081/gateway-proxy/api/service-biometrie',
  //apiUrl:'https://77.68.94.193/gateway-proxy/api/service-biometrie',
      apiUrl:'http://127.0.0.1:8080/gateway-proxy/api/service-biometrie',
  // Token Gateway (clé d'authentification)
  token_key: '3cfa76ef890d4aed2d3981a7c93bd1a13c8796dafcb4f94fa578234a0df56b321',
  
  // Configuration HTTPS
  enableHttps: true,
  strictSSL: false,  // ⚠️ false en dev pour certificat auto-signé
  
  // Timeouts
  httpTimeout: 30000,  // 30 secondes
  
  // Logs
  enableDebugLogs: true,   // Afficher logs détaillés
  enableErrorLogs: true,
  
  // Sécurité
  tokenExpiration: 3600,  // 1 heure
  
  // Features
  enableRememberMe: true,
  enableAutoRefresh: true
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/plugins/zone-error';  // Included with Angular CLI.

export const environment = {
  production: true,
  
  // URL Gateway production
 /// apiUrl: 'https://api.zenithe-insurance.com/gateway-proxy/api/service-biometrie',
    //apiUrl:'https://77.68.94.193/gateway-proxy/api/service-biometrie',
    apiUrl:'http://127.0.0.1:8080/gateway-proxy/api/service-biometrie',
  
  // Token Gateway
  token_key: '3cfa76ef890d4aed2d3981a7c93bd1a13c8796dafcb4f94fa578234a0df56b321',
  
  // Configuration HTTPS
  enableHttps: true,
  strictSSL: true,  // ✅ true en production pour sécurité maximale
  
  // Timeouts
  httpTimeout: 20000,  // 20 secondes
  
  // Logs
  enableDebugLogs: false,  // ❌ Désactivé en production
  enableErrorLogs: true,
  
  // Sécurité
  tokenExpiration: 1800,  // 30 minutes
  
  // Features
  enableRememberMe: true,
  enableAutoRefresh: true
};
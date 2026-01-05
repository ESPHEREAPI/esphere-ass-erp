import { ApplicationConfig, importProvidersFrom, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { providePrimeNG } from 'primeng/config';
import Aura from '@primeng/themes/aura';

import { routes } from './app.routes';
import { FilterMatchMode, MessageService } from 'primeng/api';
import { provideHttpClient, withFetch } from '@angular/common/http';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { provideToastr } from 'ngx-toastr';

export const appConfig: ApplicationConfig = {
  providers: [provideZoneChangeDetection({ eventCoalescing: true }), provideRouter(routes),MessageService,
    provideHttpClient(withFetch()),

    provideAnimationsAsync(),
    
  //  provideAnimations(),  // 👈 nécessaire pour les animations
    provideToastr({       // 👈 config globale (optionnel)
      timeOut: 3000,
      positionClass: 'toast-top-right',
      preventDuplicates: true,
    }),
        providePrimeNG({
            theme: {
                preset: Aura,
                options: {
                  prefix: 'p',
                  darkModeSelector: 'system',
                  cssLayer: false
              }
            },
            ripple: true,
            //inputVariant: 'filled',
         
        }),
       importProvidersFrom(NgbModule)   // 👈 ajout ici
  ]
};

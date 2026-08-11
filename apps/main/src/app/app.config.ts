import {
    ApplicationConfig,
    inject, provideAppInitializer,
    provideBrowserGlobalErrorListeners,
} from '@angular/core';
import { provideRouter, withInMemoryScrolling } from '@angular/router';

import { routes } from './app.routes';
import { provideClientHydration } from '@angular/platform-browser';
import { provideAnimationsAsync } from "@angular/platform-browser/animations/async";
import { provideHttpClient, withFetch } from '@angular/common/http';
import { ApolloConfigService } from "./apollo/apollo.config.service";
import { provideApollo } from "apollo-angular";

function initializeApp(apolloConfigService: ApolloConfigService) {

}

export const appConfig: ApplicationConfig = {
    providers: [
        provideHttpClient(withFetch()),
        provideApollo(() => {
            const apolloConfig = inject(ApolloConfigService);
            return apolloConfig.init();
        }),
        provideBrowserGlobalErrorListeners(),
        provideAppInitializer(() => initializeApp(inject(ApolloConfigService))),
        provideClientHydration(),
        provideAnimationsAsync(),
        provideRouter(
            routes,
            withInMemoryScrolling({
                anchorScrolling: 'enabled',
                scrollPositionRestoration: 'enabled',
            }),
        ),
    ],
};

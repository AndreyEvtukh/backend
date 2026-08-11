import { Injectable, PLATFORM_ID, inject, signal } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';

const STORAGE_KEY = 'accessToken';

@Injectable({ providedIn: 'root' })
export class AuthTokenService {
    private readonly isBrowser = isPlatformBrowser(inject(PLATFORM_ID));

    readonly token = signal<string | null>(
        this.isBrowser ? localStorage.getItem(STORAGE_KEY) : null,
    );

    setToken(token: string): void {
        this.token.set(token);
        if (this.isBrowser) {
            localStorage.setItem(STORAGE_KEY, token);
        }
    }

    clear(): void {
        this.token.set(null);
        if (this.isBrowser) {
            localStorage.removeItem(STORAGE_KEY);
        }
    }
}

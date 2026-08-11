import { Injectable, inject, PLATFORM_ID, signal } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { MatDialog } from '@angular/material/dialog';
import { ROUTE_PATH } from '../models/app.model';

@Injectable({ providedIn: 'root' })
export class ScrollService {
    private dialog = inject(MatDialog);
    private isBrowser = isPlatformBrowser(inject(PLATFORM_ID));

    readonly currentFragment = signal<string>('');
    readonly isAboutBelow = signal(false);

    private started = false;

    start(): void {
        if (!this.isBrowser || this.started) return;
        this.started = true;

        window.addEventListener('scroll', this.onScroll, { passive: true });

        const hash = window.location.hash?.substring(1);
        if (hash) {
            this.navigateToFragment(hash)
        } else {
            this.currentFragment.set(hash || ROUTE_PATH.HOME);
        }

        this.updateAboutBelow();
    }

    stop(): void {
        if (!this.isBrowser || !this.started) return;
        window.removeEventListener('scroll', this.onScroll);
        this.started = false;
    }

    private onScroll = () => {
        if (this.dialog.openDialogs.length > 0) return this.updateAboutBelow();

        this.updateAboutBelow();

        if (window.scrollY === 0) {
            this.setFragment(ROUTE_PATH.HOME);
            return;
        }
    };

    private updateAboutBelow(): void {
        const about = document.getElementById('about');
        if (!about) {
            this.isAboutBelow.set(false);
            return;
        }
        const rect = about.getBoundingClientRect();
        this.isAboutBelow.set(Math.round(rect.top) <= 0);
    }

    setFragment(id: string): void {
        if (this.currentFragment() === id) return;

        this.currentFragment.set(id);

        if (!this.isBrowser) return;
        if (this.dialog.openDialogs.length > 0) return;

        const url = new URL(window.location.href);
        url.hash = id;
        history.replaceState(null, '', url.pathname + url.search + (url.hash ? url.hash : ''));
    }

    navigateToFragment(fragment: string): void {
        if (this.dialog.openDialogs.length > 0) return;
        document.getElementById(fragment)?.scrollIntoView({ behavior: 'smooth', block: 'start' });
        this.currentFragment.set(fragment);
    }
}

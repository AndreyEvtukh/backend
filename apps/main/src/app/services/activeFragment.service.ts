import { DestroyRef, inject, Injectable, signal, Signal } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';

@Injectable({ providedIn: 'root' })
export class ActiveFragmentService {
    private readonly router = inject(Router);
    private readonly destroyRef = inject(DestroyRef);

    private readonly _currentFragment = signal<string>(
        this.router.parseUrl(this.router.url).fragment ?? ''
    );

    public currentFragment = this._currentFragment;
    public isAboutBelow = signal<boolean>(false);

    constructor() {
        const subscription = this.router.events.subscribe((event) => {
            if (event instanceof NavigationEnd) {
                this.currentFragment.set(
                    this.router.parseUrl(this.router.url).fragment ?? ''
                );
            }
        });

        this.destroyRef.onDestroy(() => subscription.unsubscribe());
    }
}

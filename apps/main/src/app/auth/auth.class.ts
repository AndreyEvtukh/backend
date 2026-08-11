import { Directive, inject, OnDestroy, OnInit, PLATFORM_ID } from '@angular/core';
import { ScrollService } from "../services/scroll.service";
import { MatDialog, MatDialogRef } from "@angular/material/dialog";
import { AuthService } from "./auth.service";
import { Router } from "@angular/router";
import LoginDialogComponent from "./login/login-dialog";
import { isPlatformBrowser } from "@angular/common";
import RegisterDialogComponent from "./register/register-dialog";

@Directive()
export abstract class AuthClass implements OnInit, OnDestroy {
    protected dialog = inject(MatDialog);
    protected router = inject(Router);
    protected authService = inject(AuthService);
    protected scrollService = inject(ScrollService);
    protected ref: MatDialogRef<LoginDialogComponent | RegisterDialogComponent> | null = null;
    protected isBrowser = isPlatformBrowser(inject(PLATFORM_ID));
    protected returnScrollY = 0;
    protected abstract component: any;

    ngOnInit() {
        this.returnScrollY = this.authService.returnScrollY ?? 0;

        this.ref = this.dialog.open(this.component, {
            width: '420px',
            maxWidth: '95vw',
            panelClass: 'auth-dialog-panel',
            closeOnNavigation: false
        });

        this.ref.afterClosed().subscribe((result) => {
            if (result === 'switch') return;

            this.isBrowser && history.replaceState(null, '', `/#${this.scrollService.currentFragment()}`)
            this.router.navigate([''], { fragment: this.scrollService.currentFragment() });
            this.isBrowser && window.scrollTo({ top: this.authService.returnScrollY });
        });
    }

    ngOnDestroy() {
        this.ref?.close('destroyed');
        this.ref = null;
    }
}

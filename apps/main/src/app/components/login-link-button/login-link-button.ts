import { Component, computed, inject, input, InputSignal, Signal } from '@angular/core';
import { ICONS, NAV_LOCATION, ROUTE_PATH } from "../../models/app.model";
import { MatIcon } from "@angular/material/icon";
import { Router } from "@angular/router";
import { NgClass } from "@angular/common";
import { AuthService } from "../../auth/auth.service";

@Component({
    selector: 'app-login-link-button',
    imports: [MatIcon, NgClass],
    templateUrl: `login-link-button.html`,
    styleUrl: 'login-link-button.css',
})
export class LoginLinkButtonComponent {
    protected readonly ICONS = ICONS;

    private router = inject(Router);
    private authService = inject(AuthService);

    public location: InputSignal<string> = input<string>(NAV_LOCATION.HEADER);
    public isSide: Signal<boolean> = computed(() => this.location() === NAV_LOCATION.SIDE);

    protected login() {
        this.authService.returnScrollY = window.scrollY;
        this.router.navigate([ROUTE_PATH.LOGIN]);
    }
}

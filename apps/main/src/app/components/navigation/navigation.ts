import {
    Component,
    computed,
    inject,
    input,
    OnInit, PLATFORM_ID,
    Signal,
    signal
} from '@angular/core';
import { ICONS, NAV_LOCATION, ROUTE_PATH } from "../../models/app.model";
import { Router } from "@angular/router";
import { MatIcon } from "@angular/material/icon";
import { isPlatformBrowser, NgClass } from "@angular/common";
import {
    animate,
    style,
    transition,
    trigger,
} from '@angular/animations';
import { LoginLinkButtonComponent } from "../login-link-button/login-link-button";
import { ScrollService } from "../../services/scroll.service";

@Component({
    selector: 'app-navigation',
    imports: [MatIcon, NgClass, LoginLinkButtonComponent],
    templateUrl: `navigation.html`,
    styleUrl: 'navigation.css',
    animations: [
        trigger('sideNavAnimation', [
            transition(':enter', [
                style({ opacity: 0, transform: 'translate(-8px, 0) scale(0.95)' }),
                animate('200ms ease-out', style({ opacity: 1, transform: 'translate(0, 0%) scale(1)' })),
            ]),
            transition(':leave', [
                animate('200ms ease-in', style({ opacity: 0, transform: 'translate(-8px, 0) scale(0.95)' })),
            ]),
        ]),
    ],
})
export class NavigationComponent implements OnInit {
    public location = input<string>(NAV_LOCATION.HEADER);

    private router: Router = inject(Router);
    private scrollService = inject(ScrollService);

    protected isHeader: Signal<boolean> = computed(() => this.location() === NAV_LOCATION.HEADER);
    protected isSide: Signal<boolean> = computed(() => this.location() === NAV_LOCATION.SIDE);
    protected isFooter: Signal<boolean> = computed(() => this.location() === NAV_LOCATION.FOOTER);

    protected showSideNav: Signal<boolean> = computed(() => {
        const fragment = this.scrollService.currentFragment();
        const isAboutBelow = this.scrollService.isAboutBelow();
        return this.isSide() && (fragment !== '' && fragment !== ROUTE_PATH.HOME) && isAboutBelow;
    });

    routesList: any = signal(
        this.router.config
            .map(route => route.data && {...route.data, url: route.path})
            .filter(Boolean)
    );

    isRouteActive = (item: any): boolean => {
        const fragment = this.scrollService.currentFragment();
        if (item.fragment === ROUTE_PATH.HOME) {
            return fragment === '' || fragment === ROUTE_PATH.HOME;
        }
        return fragment === item.fragment;
    };

    ngOnInit(): void {
        this.scrollService.start();
    }

    gotoView(item: { url: string, fragment?: string }) {
        if (!item.fragment) return;
        this.router.navigate([''], { fragment: item.fragment });
    }

    protected readonly ROUTE_PATH = ROUTE_PATH;
    protected readonly ICONS = ICONS;
    protected readonly NAV_LOCATION = NAV_LOCATION;
}

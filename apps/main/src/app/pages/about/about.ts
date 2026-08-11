import {Component} from '@angular/core';
import {MainBannerComponent} from "../../components/main-banner/main-banner";
import {PortfolioLinksComponent} from "../../components/page-blocks/portfolio-links/portfolio-links";
import {WhoIAmComponent} from "../../components/page-blocks/whoiam/whoiam";
import {TestimonialsComponent} from "../../components/page-blocks/testimonials/testimonials";
import {ICONS} from "../../models/app.model";
import {MatIcon} from "@angular/material/icon";
import { ScrollSpyDirective } from "../../directives/scroll‑spy.directive";

@Component({
    selector: 'app-about-page',
    imports: [
        MainBannerComponent,
        PortfolioLinksComponent,
        WhoIAmComponent,
        TestimonialsComponent,
        MatIcon,
        ScrollSpyDirective
    ],
    templateUrl: `about.html`,
    styleUrl: 'about.css',
})
export default class AboutPageComponent {
    protected readonly ICONS = ICONS;
}

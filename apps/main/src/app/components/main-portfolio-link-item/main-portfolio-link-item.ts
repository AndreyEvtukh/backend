import {Component, input, InputSignal} from '@angular/core';
import {ICONS, MainPortfolioLink} from "../../models/app.model";
import {MatIcon} from "@angular/material/icon";

@Component({
    selector: 'app-main-portfolio-link-item',
    imports: [
        MatIcon
    ],
    templateUrl: `main-portfolio-link-item.html`,
    styleUrl: 'main-portfolio-link-item.css',
})
export class MainPortfolioLinkItemComponent {
    public data: InputSignal<MainPortfolioLink> = input.required<MainPortfolioLink>();

    protected readonly ICONS = ICONS;
}

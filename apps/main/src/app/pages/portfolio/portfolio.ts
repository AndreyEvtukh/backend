import { Component } from '@angular/core';
import { ICONS } from "../../models/app.model";

@Component({
    selector: 'app-portfolio-page',
    imports: [],
    templateUrl: `portfolio.html`,
    styleUrl: 'portfolio.css',
})
export default class portfolioPageComponent {
    protected readonly ICONS = ICONS;
}

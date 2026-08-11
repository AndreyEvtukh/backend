import {Component} from '@angular/core';
import {ICONS} from "../../models/app.model";

@Component({
    selector: 'app-home-page',
    imports: [    ],
    templateUrl: `home.html`,
    styleUrl: 'home.css',
})
export default class HomePageComponent {
    protected readonly ICONS = ICONS;
}

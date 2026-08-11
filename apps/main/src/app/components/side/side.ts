import { Component } from '@angular/core';
import { NAV_LOCATION } from "../../models/app.model";
import { NavigationComponent } from "../navigation/navigation";

@Component({
    selector: 'app-side',
    imports: [
        NavigationComponent
    ],
    templateUrl: `side.html`,
    styleUrl: 'side.css',
})
export class SideController {
    public readonly NAV_LOCATION = NAV_LOCATION;
}

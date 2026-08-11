import { Component } from '@angular/core';
import { ICONS } from "../../models/app.model";
import { NavigationComponent } from "../navigation/navigation";
import { LangComponent } from "../lang/lang";
import { LoginLinkButtonComponent } from "../login-link-button/login-link-button";

@Component({
    selector: 'app-header',
    imports: [NavigationComponent, LangComponent, LoginLinkButtonComponent],
    templateUrl: `header.html`,
    styleUrl: 'header.css',
})
export class HeaderComponent {
    protected readonly ICONS = ICONS;
}

import {Component} from '@angular/core';
import {UIButtonComponent} from "../ui/button/button";
import {ICONS, UIButton} from "../../models/app.model";
import {MatIcon} from "@angular/material/icon";

@Component({
    selector: 'app-main-banner',
    imports: [
        UIButtonComponent,
        MatIcon
    ],
    templateUrl: `main-banner.html`,
    styleUrl: 'main-banner.css',
})
export class MainBannerComponent {

    CVLinkButtonConfig: UIButton = {
        label: "Download CV",
        link: "Download CV",
        borderClass: "border-bronze",
        hoverClass: "hover bg-hover-dark-2",
        bgClass: "bg-dark-3",
        icon: ICONS.DOWNLOAD,
        iconPosition: 'right'
    }

    ContactsLinkButtonConfig: UIButton = {
        label: "Let's Chat!",
        link: "Download CV",
        labelClass: "text-bronze"
    }

    retro = [
        {
            id: 0,
            value: "11+",
            text: "years\nexperience"
        }, {
            id: 1,
            value: "3",
            text: "enterprise\nprojects"
        }, {
            id: 1,
            value: "18",
            text: "projects completed\non 8 countries"
        }
    ]

    protected readonly ICONS = ICONS;
}

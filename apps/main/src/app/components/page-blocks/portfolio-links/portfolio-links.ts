import {Component} from '@angular/core';
import {MainPortfolioLinkItemComponent} from "../../main-portfolio-link-item/main-portfolio-link-item";
import {ICONS, MainPortfolioLink} from "../../../models/app.model";

@Component({
    selector: 'app-portfolio-links',
    imports: [
        MainPortfolioLinkItemComponent
    ],
    templateUrl: `portfolio-links.html`,
    styleUrl: 'portfolio-links.css',
})
export class PortfolioLinksComponent {
    links: MainPortfolioLink[] = [
        {
            id: 0,
            title: "Design",
            content: "Turning ideas into interfaces people actually enjoy using.",
            link: "/porfolio",
            icon: ICONS.Design
        },
        {
            id: 1,
            title: "Front-End",
            content: "Building fast, polished interfaces with Angular and TypeScript.",
            link: "/porfolio",
            icon: ICONS.Frontend
        },
        {
            id: 2,
            title: "Back-End",
            content: "Designing scalable APIs and backend architecture with Spring Boot.",
            link: "/porfolio",
            icon: ICONS.Backend
        }
    ];
}

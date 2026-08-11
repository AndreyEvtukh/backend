import { Component } from '@angular/core';
import { Lang } from "../../models/app.model";
import { NgClass } from "@angular/common";

@Component({
    selector: 'app-lang',
    imports: [
        NgClass
    ],
    templateUrl: `lang.html`,
    styleUrl: 'lang.css',
})
export class LangComponent {
    protected langs: Lang[] = [
        {
            id: "en",
            title: "En",
            active: true,
            order: 1
        }, {
            id: "pl",
            title: "Pl",
            order: 2
        },
        {
            id: "ru",
            title: "Ru",
            order: 3
        },
    ];
    protected readonly isActive = (item: Lang) => {

    };
}

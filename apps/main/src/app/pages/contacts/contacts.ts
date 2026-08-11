import {Component} from '@angular/core';
import {ICONS} from "../../models/app.model";

@Component({
    selector: 'app-contacts-page',
    imports: [],
    templateUrl: `contacts.html`,
    styleUrl: 'contacts.css',
})
export default class ContactsPageComponent {
    protected readonly ICONS = ICONS;
}

import {Component, input, InputSignal} from '@angular/core';
import {UIButton} from "../../../models/app.model";
import { MatIcon } from "@angular/material/icon";
import { NgClass } from "@angular/common";

@Component({
    selector: 'app-button',
    imports: [MatIcon, NgClass],
    templateUrl: `button.html`,
    styleUrl: 'button.css',
})
export class UIButtonComponent {
    public data: InputSignal<UIButton> = input.required<UIButton>();
}

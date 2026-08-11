import {Component} from '@angular/core';
import {ICONS} from "../../models/app.model";

@Component({
    selector: 'app-resume-page',
    imports: [
    ],
    templateUrl: `resume.html`,
    styleUrl: 'resume.css',
})
export default class ResumePageComponent {
    protected readonly ICONS = ICONS;
}

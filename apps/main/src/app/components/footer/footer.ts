import {Component} from '@angular/core';
import {NavigationComponent} from "../navigation/navigation";
import { LangComponent } from "../lang/lang";

@Component({
    selector: 'app-footer',
    imports: [
        NavigationComponent, LangComponent
    ],
    templateUrl: `footer.html`,
    styleUrl: 'footer.css',
})
export class FooterController {

}

import {Component, inject, OnInit, signal} from '@angular/core';
import {IconService} from "./services/icon.service";
import { RouterOutlet } from "@angular/router";
import AboutPageComponent from "./pages/about/about";
import { AuthTestComponent } from "./components/test/authTest";
import { FooterController } from "./components/footer/footer";
import { HeaderComponent } from "./components/header/header";
import { ScrollSpyDirective } from "./directives/scroll‑spy.directive";
import { SideController } from "./components/side/side";

@Component({
    selector: 'app-root',
    imports: [
        RouterOutlet,
        AboutPageComponent,
        AuthTestComponent,
        FooterController,
        HeaderComponent,
        ScrollSpyDirective,
        SideController,
    ],
    templateUrl: `app.html`,
    styleUrl: './app.css',
})
export default class App implements OnInit {
    protected readonly title = signal('main');
    protected iconService: IconService = inject(IconService);

    async ngOnInit() {
        this.iconService.registerImageIcons();
    }
}

import { inject, Injectable } from "@angular/core";
import { MatIconRegistry } from "@angular/material/icon";
import { DomSanitizer, SafeResourceUrl } from "@angular/platform-browser";
import {ICONS} from "../models/app.model";

@Injectable({
    providedIn: "root",
})
export class IconService {
    path = "/assets/images";

    private iconRegistry: MatIconRegistry = inject(MatIconRegistry);
    private sanitizer: DomSanitizer = inject(DomSanitizer);

    public registerImageIcons() {
        Object.values(ICONS).forEach((iconName) => {
            this.iconRegistry.addSvgIcon(iconName, this.setPath(`/assets/icons/${iconName}.svg`));
        });
    }

    private setPath(url: string): SafeResourceUrl {
        return this.sanitizer.bypassSecurityTrustResourceUrl(url);
    }
}

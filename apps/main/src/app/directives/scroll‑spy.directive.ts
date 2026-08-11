import {
    Directive,
    ElementRef,
    inject,
    OnDestroy,
    OnInit,
    PLATFORM_ID,
} from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { MatDialog } from "@angular/material/dialog";
import { ScrollService } from "../services/scroll.service";

@Directive({
    selector: '[appScrollSpy]',
})
export class ScrollSpyDirective implements OnInit, OnDestroy {
    private el = inject(ElementRef);
    private scrollService: ScrollService = inject(ScrollService);
    private isBrowser: boolean = isPlatformBrowser(inject(PLATFORM_ID));
    private dialog = inject(MatDialog);
    private observer?: IntersectionObserver;

    ngOnInit() {
        if (!this.isBrowser) return;

        this.observer = new IntersectionObserver(
            entries => {
                if (this.dialog.openDialogs.length > 0) return;

                entries.forEach(entry => {
                    if (entry.isIntersecting) {
                        this.scrollService.setFragment(entry.target.id);
                    }
                });
            },
            {
                root: null,
                rootMargin: '-0% 0px -100% 0px',
                threshold: [0, 0.01],
            }
        );

        this.observer.observe(this.el.nativeElement);
    }

    ngOnDestroy() {
        if (!this.isBrowser) return;
        this.observer?.disconnect();
    }
}

import {
    afterNextRender,
    Component,
    computed,
    ElementRef,
    signal,
    viewChild,
    WritableSignal
} from '@angular/core';
import {ICONS, Testimonial} from "../../../models/app.model";
import {MatIcon} from "@angular/material/icon";

@Component({
    selector: 'app-testimonials',
    imports: [
        MatIcon
    ],
    templateUrl: `testimonials.html`,
    styleUrl: 'testimonials.css',
})
export class TestimonialsComponent {
    scrollContainer = viewChild.required<ElementRef<HTMLDivElement>>('scrollContainer');
    container = signal<HTMLDivElement | null>(null);

    pLeft = signal(0);
    pRight = signal(0);
    left = signal(0);
    right = signal(0);
    doAnimate = signal(false);

    testimonials: WritableSignal<Testimonial[]> = signal<Testimonial[]>([
        {
            "authorName": "Dave Texidor",
            "authorPosition": "Director of Engineering | PayPal, Procore Alum",
            "date": "March 7, 2025",
            "text": "Andrey is a highly skilled web developer with deep expertise in Angular. As a tech lead, he not only delivered well-structured, scalable solutions but also fostered great collaboration and best practices within the team. His ability to break down complex problems and guide the development of critical features helped us deliver significant value to our customers.",
            "id": 1
        },
        {
            "authorName": "Sergey Yakubovsky",
            "authorPosition": "Staff Software Engineer at Arlo Technologies, Inc.",
            "date": "March 3, 2025",
            "text": "Andrey has a background of strong technical skills, including proficiency in programming languages, understanding of software development methodologies, and knowledge of software design principles.\n\nAndrey developed efficient solutions in different parts of web application. Those solutions characterized by clean, maintainable, and scalable code. His attention to details helped to improve app performance.\n\nAndrey effectively passes his knowledge to teammates, newcomers and existing.\n\nIt is worth to mention Andrey's strong commitment to continuous learning and professional development, staying up to date with the latest trends, technologies, and best practices in software engineering.\n\nIt is a great pleasure to have Andrey in the team.",
            "id": 2
        },
        {
            "authorName": "Artem Kuzhovnik",
            "authorPosition": "Web developer",
            "date": "March 2, 2025",
            "text": "I worked with Andrey on a project in the home security domain that involved a large codebase and high demands for quality software development, where he was responsible for frontend work. Throughout the project, Andrey demonstrated attention to detail, a responsible approach to task completion, and always strived for high-quality results. I recommend him as a professional capable of solving complex technical challenges and delivering excellent results.",
            "id": 3
        },
        {
            "authorName": "Denis Dmitriev",
            "authorPosition": "CTO at ITS Partner",
            "date": "March 17, 2025",
            "text": "Andrey was working on one of the largest projects for ITS for many years. Since his start, Andrey quickly became a go-to man for almost every technical bit of questions on this project. His dedication and technical mastery remained key assets throughout the project. Also, I'd like to note Andrey's attitude to help solve any problem in an adjacent area, those which are not his direct responsibilities but solution for which helps the team to move forward.",
            "id": 4
        }
    ])
    firstItem = computed(() => {
        const items = this.scrollContainer().nativeElement.children;
        return items[0];
    })

    lastItem = computed(() => {
        const items = this.scrollContainer().nativeElement.children;
        return items[items.length - 1];
    })
    isLeftDisabled: WritableSignal<boolean> = signal<boolean>(false);
    isRightDisabled: WritableSignal<boolean> = signal<boolean>(false);
    sliderIndex: WritableSignal<number> = signal<number>(0);

    protected readonly ICONS = ICONS;

    constructor() {
        afterNextRender(() => {
            this.updateBounds();
        });
    }

    updateBounds = () => {
        const el = this.scrollContainer().nativeElement;
        const parent = el.parentElement;
        if (!parent) return;

        const pRect = parent.getBoundingClientRect();

        this.pLeft.set(Math.round(pRect.left));
        this.pRight.set(Math.round(pRect.right));

        const items = el.children;
        this.left.set(Math.round(items[0].getBoundingClientRect().left));
        this.right.set(Math.round(items[items.length - 1].getBoundingClientRect().right));

        const isLeftDisabled = this.right() <= this.pRight();
        const isRightDisabled = this.left() >= this.pLeft();
        this.isLeftDisabled.set(isLeftDisabled);
        this.isRightDisabled.set(isRightDisabled);

        setTimeout(() => this.doAnimate.set(false), 10);
    };

    toLeft = (): undefined => {
        if (this.isLeftDisabled() || this.doAnimate()) return;

        this.sliderIndex.update(val => val - 1);

        const isLastItemPartiallyVisible = Math.round(this.lastItem().getBoundingClientRect().left) < this.pRight()
            && Math.round(this.lastItem().getBoundingClientRect().right) > this.pRight();
        const isSliderItemsPartiallyVisible = Math.round(this.firstItem().getBoundingClientRect().left) < this.pLeft()
            && Math.round(this.lastItem().getBoundingClientRect().right) > this.pRight();

        if (isLastItemPartiallyVisible || isSliderItemsPartiallyVisible) {
            this.scrollContainer().nativeElement.style.transform = "translateX(calc(-60% - 3.75rem))";
        } else {
            const transform = `translateX(calc(${this.sliderIndex()} * (33%)))`;
            this.updatePosition(transform);
        }
        this.doAnimate.set(true)
        setTimeout(() => this.updateBounds(), 210);
    };

    toRight = (): undefined => {
        if (this.isRightDisabled() || this.doAnimate()) return;

        const isFirstItemPartiallyVisible = Math.round(this.firstItem().getBoundingClientRect().left) < this.pLeft()
            && Math.round(this.firstItem().getBoundingClientRect().right) > this.pLeft();
        const isSliderItemsPartiallyVisible = Math.round(this.firstItem().getBoundingClientRect().left) < this.pLeft()
            && Math.round(this.lastItem().getBoundingClientRect().right) > this.pRight();

        this.sliderIndex.update(val => val + 1);

        if (isFirstItemPartiallyVisible || isSliderItemsPartiallyVisible) {
            this.scrollContainer().nativeElement.style.transform = "translateX(calc(0%))";
        } else {
            const transform = `translateX(calc(${this.sliderIndex()} * (33%)))`;
            this.updatePosition(transform);
        }
        this.doAnimate.set(true);
        setTimeout(() => this.updateBounds(), 210);
    };

    private updatePosition(transform: string) {
        this.scrollContainer().nativeElement.style.transform = transform;
    }
}

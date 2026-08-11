import { Component, OnInit } from "@angular/core";
import { RouterOutlet } from "@angular/router";

@Component({
    selector: 'app-auth-modal',
    imports: [
        RouterOutlet
    ],
    templateUrl: './auth-modal.html'
})
export default class AuthModalComponent implements OnInit {
    ngOnInit() {

    }
}

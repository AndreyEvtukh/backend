import { Component, OnDestroy, OnInit } from '@angular/core';
import RegisterDialogComponent from "./register-dialog";
import { AuthClass } from "../auth.class";

@Component({
    selector: 'app-login-route',
    standalone: true,
    template: '',
})
export default class RegisterRouteComponent extends AuthClass implements OnInit, OnDestroy {
    protected component: typeof RegisterDialogComponent = RegisterDialogComponent;
}

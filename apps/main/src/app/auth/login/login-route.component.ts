import { Component, OnDestroy, OnInit } from '@angular/core';
import LoginDialogComponent from "./login-dialog";
import { AuthClass } from "../auth.class";

@Component({
    selector: 'app-login-route',
    standalone: true,
    template: '',
})
export default class LoginRouteComponent extends AuthClass implements OnInit, OnDestroy {
    protected component: typeof LoginDialogComponent = LoginDialogComponent;
}

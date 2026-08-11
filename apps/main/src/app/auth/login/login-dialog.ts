import { Component, inject, OnInit, Signal, signal } from '@angular/core';
import { ICONS, ROUTE_PATH } from "../../models/app.model";
import { MatIcon } from "@angular/material/icon";
import { MatDialogActions, MatDialogRef } from "@angular/material/dialog";
import { MatFormField, MatInputModule, MatLabel } from "@angular/material/input";
import {
    AbstractControl,
    FormControl,
    FormGroup,
    ReactiveFormsModule,
    ValidationErrors,
    ValidatorFn,
    Validators
} from "@angular/forms";
import { MatFormFieldModule } from "@angular/material/form-field";
import { Router } from "@angular/router";

@Component({
    selector: 'app-login-dialog',
    imports: [MatIcon, MatDialogActions, MatLabel, MatFormField, MatInputModule, MatFormFieldModule, ReactiveFormsModule],
    templateUrl: `login-dialog.html`,
    styleUrl: 'login-dialog.css',
})
export default class LoginDialogComponent implements OnInit {
    private dialogRef: MatDialogRef<any> | null = inject(MatDialogRef<LoginDialogComponent>, { optional: true });
    private router = inject(Router);

    waiting: Signal<boolean> = signal<boolean>(false);
    form!: FormGroup;

    private emailRegExp = new RegExp("^[\\w-]+(\\.[\\w-]+)*@([a-z0-9-]+(\\.[a-z0-9-]+)*?\\.[a-z]{2,6}|(\\d{1,3}\\.){3}\\d{1,3})(:\\d{4})?$");
    private passwordRegExp = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d!@#$%^&*()_+\-=[\]{};':"\\|,.<>/?`~]{8,}$/;

    get emailIsInvalid() {
        const { errors } = this.form.controls["email"];
        return errors?.["email"] || errors?.["pattern"];
    }

    protected close() {
        if (this.dialogRef) {
            this.dialogRef.close();
        } else {
            this.router.navigate(['/']);
        }
    }

    protected signIn() {
        // this.dialogRef.close();
    }

    protected async goRegister() {
        await this.router.navigate([ROUTE_PATH.REGISTER]);
        this.dialogRef!.close('switch');
    }

    private noWhitespace: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
        const value = control.value as string;
        if ((value != null && !value.trim().length) || /\s/.test(value)) {
            return { whitespace: true };
        }
        return null;
    };

    public ngOnInit(): void {
        this.form = new FormGroup({
            password: new FormControl("", [
                Validators.required,
                Validators.minLength(1),
                this.noWhitespace,
            ]),
            email: new FormControl("", [
                Validators.pattern(this.emailRegExp),
                Validators.nullValidator,
                Validators.required,
                Validators.email,
            ])
        });
    }

    protected doBlurredInput(input: string) {
        this.form.controls[input].markAsTouched();
        this.form.controls[input].markAsDirty();
    }

    protected readonly ICONS = ICONS;
}

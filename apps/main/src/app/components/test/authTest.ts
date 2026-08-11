import { Component, inject, signal } from '@angular/core';
import { AuthService } from '../../auth/auth.service';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../../../../environments/environment";
import { Apollo } from "apollo-angular";
import gql from 'graphql-tag';

@Component({
    selector: 'app-auth-test',
    standalone: true,
    template: `
        <div
            class="p-5"
            style="font-family: monospace;"
        >
            <button class="cursor-pointer" (click)="testRegister()">
                Register test user
            </button>
            <button class="cursor-pointer" (click)="testLogin()">
                Login test user
            </button>
            <button class="cursor-pointer" (click)="testMe()">Call me</button>
            <pre>{{ result() }}</pre>
        </div>
    `,
})
export class AuthTestComponent {
    private readonly apiUrl = `http://${environment.api_host}:${environment.api_port}/api`;

    private readonly auth = inject(AuthService);
    private readonly http = inject(HttpClient);
    private readonly apollo = inject(Apollo);
    readonly result = signal('');

    async testRegister() {
        try {
            const user = await this.auth.register(
                'john',
                'john@example.com',
                'SuperSecret1',
            );
            this.result.set(JSON.stringify(user, null, 2));
        } catch (e: any) {
            this.result.set('Error: ' + e.message);
        }
    }

    async testLogin() {
        try {
            const user = await this.auth.login('john', 'SuperSecret1');
            this.result.set(JSON.stringify(user, null, 2));
        } catch (e: any) {
            this.result.set('Error: ' + e.message);
        }
    }

    async testMe() {
        this.apollo.query({
            query: gql`
                query {
                    users {
                        id
                        username
                        email
                        created_at
                        enabled
                        role
                        password_hash
                    }
                }
            `
        }).subscribe({
            next: (result: any) => {
                console.error(result.data.users);
            },
            error: (err) => {
                console.error('Ошибка GraphQL запроса', err);
            }
        });
    }
}

import { Injectable, inject } from '@angular/core';
import { Apollo, gql } from 'apollo-angular';
import { firstValueFrom } from 'rxjs';
import { AuthTokenService } from '../services/authToken.service';

const REGISTER_MUTATION = gql`
    mutation Register($input: RegisterInput!) {
        register(input: $input) {
            accessToken
            user {
                id
                username
                email
                role
            }
        }
    }
`;

const LOGIN_MUTATION = gql`
    mutation Login($input: LoginInput!) {
        login(input: $input) {
            accessToken
            user {
                id
                username
                email
                role
            }
        }
    }
`;

const ME_QUERY = gql`
    query Me {
        me {
            id
            username
            email
            role
        }
    }
`;

@Injectable({ providedIn: 'root' })
export class AuthService {
    private readonly apollo = inject(Apollo);
    private readonly tokenService = inject(AuthTokenService);
    public returnScrollY: number = 0;

    async register(username: string, email: string, password: string) {
        const result = await firstValueFrom(
            this.apollo.mutate<any>({
                mutation: REGISTER_MUTATION,
                variables: { input: { username, email, password } },
            }),
        );
        this.tokenService.setToken(result.data.register.accessToken);
        return result.data.register.user;
    }

    async login(username: string, password: string) {
        const result = await firstValueFrom(
            this.apollo.mutate<any>({
                mutation: LOGIN_MUTATION,
                variables: { input: { username, password } },
            }),
        );
        this.tokenService.setToken(result.data.login.accessToken);
        return result.data.login.user;
    }

    async me() {
        const result = await firstValueFrom(
            this.apollo.query<any>({
                query: ME_QUERY,
                fetchPolicy: 'network-only',
            }),
        );
        return result.data.me;
    }
}

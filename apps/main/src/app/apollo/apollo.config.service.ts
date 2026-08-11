import { inject, Injectable } from '@angular/core';
import { HttpLink } from 'apollo-angular/http';
import {
    ApolloClient,
    ApolloLink,
    CombinedGraphQLErrors,
    CombinedProtocolErrors,
    InMemoryCache
} from '@apollo/client/core';
import { environment } from "../../../../../environments/environment";
import { BFF } from "../models/constatnts";
import { ErrorLink } from "@apollo/client/link/error";

@Injectable({
    providedIn: 'root',
})
export class ApolloConfigService {
    private readonly httpLink = inject(HttpLink);

    public init(): ApolloClient.Options {
        const terminatingLink: ApolloLink = this.httpLink.create({
            uri: `http://${environment.api_host}:${environment.api_port}${BFF.API_URL}`,
        });

        const errorLink = new ErrorLink(({ error }) => {
            if (CombinedGraphQLErrors.is(error)) {
                error.errors.forEach(({ message, locations, path }) =>
                    console.log(
                        `[GraphQL error]: Message: ${message}, Location: ${locations}, Path: ${path}`
                    )
                );
            } else if (CombinedProtocolErrors.is(error)) {
                error.errors.forEach(({ message, extensions }) =>
                    console.log(
                        `[Protocol error]: Message: ${message}, Extensions: ${JSON.stringify(
                            extensions
                        )}`
                    )
                );
            } else {
                console.error(`[Network error]: ${error}`);
            }
        });

        return {
            link: ApolloLink.from([
                // authLink
                errorLink,
                // loggerLink
                terminatingLink,
            ]),
            cache: new InMemoryCache(),
            defaultOptions: {
                watchQuery: {
                    fetchPolicy: 'network-only',
                },
                query: {
                    fetchPolicy: 'network-only',
                },
            },
        };
    }
}

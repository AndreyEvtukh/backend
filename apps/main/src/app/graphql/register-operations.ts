import { gql } from "apollo-angular";
import { MutationOptions } from "@apollo/client";

export namespace Register {

    export const requestRegistration = ( form: any ): MutationOptions<any> => {
        return {
            mutation: gql`
                mutation requestRegistration($input: RegisterRequestInput!) {
                    requestRegistration(input: $input) {
                        success
                        message
                    }
                }
            `,
            variables: {
                input: {
                    email: form.email,
                    username: form.username,
                    password_hash: form.password_hash
                }
            },
        };
    };
}

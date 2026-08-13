package com.portfolio.backend.config;

import com.portfolio.backend.exceptions.EmailAlreadyRegisteredException;
import com.portfolio.backend.exceptions.InputEmailErrorException;
import com.portfolio.backend.exceptions.InputPasswordErrorException;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.jspecify.annotations.NonNull;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GraphQlExceptionHandler extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(@NonNull Throwable ex, @NonNull DataFetchingEnvironment env) {
        return switch (ex) {
            case EmailAlreadyRegisteredException ignored -> GraphqlErrorBuilder.newError()
                    .errorType(ErrorType.FORBIDDEN)
                    .message(ex.getMessage())
                    .path(env.getExecutionStepInfo().getPath())
                    .extensions(Map.of(
                            "code", 1401,
                            "reason", "EMAIL_ALREADY_REGISTERED"
                    ))
                    .build();
            case InputPasswordErrorException ignored -> GraphqlErrorBuilder.newError()
                    .errorType(ErrorType.BAD_REQUEST)
                    .message(ex.getMessage())
                    .path(env.getExecutionStepInfo().getPath())
                    .extensions(Map.of(
                            "code", 1402,
                            "reason", "PASSWORD_INCORRECT"
                    ))
                    .build();
            case InputEmailErrorException ignored -> GraphqlErrorBuilder.newError()
                    .errorType(ErrorType.BAD_REQUEST)
                    .message(ex.getMessage())
                    .path(env.getExecutionStepInfo().getPath())
                    .extensions(Map.of(
                            "code", 1403,
                            "reason", "EMAIL_INCORRECT"
                    ))
                    .build();
            default -> null;
        };


    }
}
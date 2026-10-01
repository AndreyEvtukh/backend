package com.portfolio.backend.exceptions;

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
    protected GraphQLError resolveToSingleError(
            @NonNull Throwable ex,
            @NonNull DataFetchingEnvironment env
    ) {

        return switch (ex) {

            case InputEmailAlreadyRegisteredException ignored ->
                    GraphqlErrorBuilder.newError()
                            .errorType(ErrorType.FORBIDDEN)
                            .message(ex.getMessage())
                            .path(env.getExecutionStepInfo().getPath())
                            .extensions(Map.of(
                                    "code", 1401,
                                    "reason", "EMAIL_ALREADY_REGISTERED"
                            ))
                            .build();

            case InputPasswordErrorException ignored ->
                    GraphqlErrorBuilder.newError()
                            .errorType(ErrorType.BAD_REQUEST)
                            .message(ex.getMessage())
                            .path(env.getExecutionStepInfo().getPath())
                            .extensions(Map.of(
                                    "code", 1402,
                                    "reason", "PASSWORD_INCORRECT"
                            ))
                            .build();

            case InputEmailErrorException ignored ->
                    GraphqlErrorBuilder.newError()
                            .errorType(ErrorType.BAD_REQUEST)
                            .message(ex.getMessage())
                            .path(env.getExecutionStepInfo().getPath())
                            .extensions(Map.of(
                                    "code", 1403,
                                    "reason", "EMAIL_INCORRECT"
                            ))
                            .build();

            case InputSendMailErrorException ignored ->
                    GraphqlErrorBuilder.newError()
                            .errorType(ErrorType.BAD_REQUEST)
                            .message(ex.getMessage())
                            .path(env.getExecutionStepInfo().getPath())
                            .extensions(Map.of(
                                    "code", 1404,
                                    "reason", "EMAIL_IS_NOT_EXISTS"
                            ))
                            .build();

            case InputVerifyCodeErrorException ignored ->
                    GraphqlErrorBuilder.newError()
                            .errorType(ErrorType.BAD_REQUEST)
                            .message(ex.getMessage())
                            .path(env.getExecutionStepInfo().getPath())
                            .extensions(Map.of(
                                    "code", 1405,
                                    "reason", "VERIFY_CODE_IS_INVALID"
                            ))
                            .build();

            case InputVerifyCodeExpiredException ignored ->
                    GraphqlErrorBuilder.newError()
                            .errorType(ErrorType.BAD_REQUEST)
                            .message(ex.getMessage())
                            .path(env.getExecutionStepInfo().getPath())
                            .extensions(Map.of(
                                    "code", 1406,
                                    "reason", "VERIFY_CODE_IS_EXPIRED"
                            ))
                            .build();

            case InputVerifyCodeUsedException ignored ->
                    GraphqlErrorBuilder.newError()
                            .errorType(ErrorType.BAD_REQUEST)
                            .message(ex.getMessage())
                            .path(env.getExecutionStepInfo().getPath())
                            .extensions(Map.of(
                                    "code", 1407,
                                    "reason", "VERIFY_CODE_IS_ALREADY_USED"
                            ))
                            .build();

            default -> null;
        };
    }
}
package com.urutare.stockservice.exception;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.execution.ResultPath;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

@Component
public class CustomExceptionResolver extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        if (ex instanceof ForbiddenException || ex instanceof NotFoundException) {
            return createGraphQLError(ex, env);
        }
        return null;
    }

    private GraphQLError createGraphQLError(Throwable ex, DataFetchingEnvironment env) {
        return GraphqlErrorBuilder.newError()
                .errorType(getErrorType(ex))
                .message(ex.getMessage())
                .path(getPath(env))
                .location(env.getField().getSourceLocation())
                .build();
    }

    private ErrorType getErrorType(Throwable ex) {
        if (ex instanceof ForbiddenException) {
            return ErrorType.FORBIDDEN;
        } else if (ex instanceof NotFoundException) {
            return ErrorType.NOT_FOUND;
        } else if (ex instanceof ConflictException) {
            return ErrorType.BAD_REQUEST;
        }
        // Add other custom exception types here if needed
        return ErrorType.INTERNAL_ERROR;
    }

    private ResultPath getPath(DataFetchingEnvironment env) {
        return env.getExecutionStepInfo().getPath();
    }
}

//package service.organization.impl;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.ResponseStatus;
//
///**
// * Exception thrown when a client sends an invalid request.
// * This exception corresponds to HTTP status code 400 (Bad Request).
// */
//@ResponseStatus(HttpStatus.BAD_REQUEST)
//public class BadRequestException extends RuntimeException {
//
//    private static final long serialVersionUID = 1L;
//
//    /**
//     * Constructs a new bad request exception with the specified detail message.
//     *
//     * @param message the detail message
//     */
//    public BadRequestException(String message) {
//        super(message);
//    }
//
//    /**
//     * Constructs a new bad request exception with the specified detail message and cause.
//     *
//     * @param message the detail message
//     * @param cause the cause of the exception
//     */
//    public BadRequestException(String message, Throwable cause) {
//        super(message, cause);
//    }
//
//    /**
//     * Creates a new exception instance for a validation error on a specific field.
//     *
//     * @param fieldName the name of the field that failed validation
//     * @param errorMessage the validation error message
//     * @return the exception instance
//     */
//    public static BadRequestException invalidField(String fieldName, String errorMessage) {
//        return new BadRequestException("Invalid " + fieldName + ": " + errorMessage);
//    }
//
//    /**
//     * Creates a new exception instance for a missing required field.
//     *
//     * @param fieldName the name of the required field
//     * @return the exception instance
//     */
//    public static BadRequestException missingField(String fieldName) {
//        return new BadRequestException("Missing required field: " + fieldName);
//    }
//
//    /**
//     * Creates a new exception instance for an operation that cannot be performed
//     * due to the current state of a resource.
//     *
//     * @param resourceName the name of the resource
//     * @param currentState the current state of the resource
//     * @param operation the operation that was attempted
//     * @return the exception instance
//     */
//    public static BadRequestException invalidOperation(String resourceName, String currentState, String operation) {
//        return new BadRequestException("Cannot " + operation + " " + resourceName + " in " + currentState + " state");
//    }
//
//    /**
//     * Creates a new exception instance for an invalid state transition.
//     *
//     * @param resourceName the name of the resource
//     * @param fromState the current state
//     * @param toState the target state
//     * @return the exception instance
//     */
//    public static BadRequestException invalidStateTransition(String resourceName, String fromState, String toState) {
//        return new BadRequestException("Cannot transition " + resourceName + " from " + fromState + " to " + toState);
//    }
//
//    /**
//     * Creates a new exception instance for a resource ownership mismatch.
//     *
//     * @param resourceName the name of the resource
//     * @param ownerId the ID of the expected owner
//     * @return the exception instance
//     */
//    public static BadRequestException ownershipMismatch(String resourceName, Long ownerId) {
//        return new BadRequestException(resourceName + " does not belong to entity with ID: " + ownerId);
//    }
//}
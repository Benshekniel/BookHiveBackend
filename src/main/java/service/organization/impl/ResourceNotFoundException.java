//package service.organization.impl;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.ResponseStatus;
//
///**
// * Exception thrown when a requested resource cannot be found.
// * Note: This exception class is typically placed in an exception package,
// * but is defined here as requested.
// */
//@ResponseStatus(HttpStatus.NOT_FOUND)
//public class ResourceNotFoundException extends RuntimeException {
//
//    private static final long serialVersionUID = 1L;
//
//    /**
//     * Constructs a new resource not found exception with the specified detail message.
//     *
//     * @param message the detail message
//     */
//    public ResourceNotFoundException(String message) {
//        super(message);
//    }
//
//    /**
//     * Constructs a new resource not found exception with the specified detail message and cause.
//     *
//     * @param message the detail message
//     * @param cause the cause of the exception
//     */
//    public ResourceNotFoundException(String message, Throwable cause) {
//        super(message, cause);
//    }
//
//    /**
//     * Creates a new exception instance for a resource identified by its ID.
//     *
//     * @param resourceName the name of the resource type
//     * @param id the ID of the resource
//     * @return the exception instance
//     */
//    public static ResourceNotFoundException forId(String resourceName, Long id) {
//        return new ResourceNotFoundException(resourceName + " not found with id: " + id);
//    }
//
//    /**
//     * Creates a new exception instance for a resource identified by a field value.
//     *
//     * @param resourceName the name of the resource type
//     * @param fieldName the name of the field
//     * @param fieldValue the value of the field
//     * @return the exception instance
//     */
//    public static ResourceNotFoundException forField(String resourceName, String fieldName, String fieldValue) {
//        return new ResourceNotFoundException(resourceName + " not found with " + fieldName + ": " + fieldValue);
//    }
//}
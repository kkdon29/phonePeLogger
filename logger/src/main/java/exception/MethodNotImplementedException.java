package exception;

/**
 * {@link RuntimeException} indicating method throwing this is not implemented.
 * Useful when writing new code with tdd
 *
 * @author Kaustubh Khasnis
 */
public class MethodNotImplementedException extends RuntimeException {
    public MethodNotImplementedException(String methodName) {
        super("Method " + methodName + " is not implemented");
    }
}

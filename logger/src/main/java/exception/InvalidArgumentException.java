package exception;

public class InvalidArgumentException extends RuntimeException {
    private String argName;
    private Object argValue;

    public InvalidArgumentException(String argName, Object value) {
        this.argName = argName;
        this.argValue = value;
    }

    @Override
    public String toString() {
        return "Invalid value " + this.argValue + " passed for argument"
                        + this.argName;
    }
}

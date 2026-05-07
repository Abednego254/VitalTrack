package app.utility.validation;

public interface Validate<T> {

    void printValidation();

    boolean process(T entity);
}

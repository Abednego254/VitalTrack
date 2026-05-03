package app.utility.validation;

/**
 * [CONCEPT: Generic Validation Interface]
 * This is the rulebook for all our "Bouncers".
 * Any specific Bouncer (like ValidateEquipment) must follow this rulebook!
 */
public interface Validate<T> {
    
    // Prints out what the bouncer is doing (optional, but good for debugging)
    void printValidation();

    // This is the actual check! Returns true if the data is good, false if it's bad.
    boolean process(T entity);
}

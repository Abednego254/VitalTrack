package app.utility.bootstrap;

/**
 * [CONCEPT: Strategy Pattern / Interface]
 * This is an item on our "Morning Checklist".
 * Any class that implements this interface represents a task 
 * the hospital must complete before it opens its doors.
 */
public interface Bootstrap {
    void process();
}

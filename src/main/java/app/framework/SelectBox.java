package app.framework;

public class SelectBox {

    private String value;
    private String name;

    private SelectBox() {
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    // Step 1: value is required first
    public static ValueStep builder() {
        return new Builder();
    }

    public interface ValueStep {
        NameStep value(String value);
    }

    // Step 2: name is required next
    public interface NameStep {
        BuildStep name(String name);
    }

    // Step 3: build becomes available only after both are set
    public interface BuildStep {
        SelectBox build();
    }

    private static class Builder implements ValueStep, NameStep, BuildStep {

        private final SelectBox selectBox = new SelectBox();

        @Override
        public NameStep value(String value) {
            selectBox.value = value;
            return this;
        }

        @Override
        public BuildStep name(String name) {
            selectBox.name = name;
            return this;
        }

        @Override
        public SelectBox build() {
            return selectBox;
        }
    }
}
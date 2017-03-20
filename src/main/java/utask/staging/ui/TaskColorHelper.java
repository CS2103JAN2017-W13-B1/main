package utask.staging.ui;

import java.util.Random;

public class TaskColorHelper {

    public static String getARandomColor() {
        ColorType color = randomEnum(ColorType.class);
        return color.toString();
    }

    private static <T extends Enum<?>> T randomEnum(Class<T> c) {
        Random random = new Random();
        int x = random.nextInt(c.getEnumConstants().length);
        return c.getEnumConstants()[x];
    }

    public enum ColorType {
        RED("#F44336"),
        PINK("#E91E63"),
        PURPLE("#9C27B0"),
        DEEPPURPLE("#673AB7"),
        INDIGO("#3F51B5"),
        BLUE("#2196F3"),
        CYAN("#00BCD4"),
        TEAL("#009688"),
        GREEN("#4CAF50"),
        DEEPORANGE("FF5722"),
        ORANGE("#FF9800"),
        AMBER("#FFC107");

        private final String text;

        private ColorType(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }
}

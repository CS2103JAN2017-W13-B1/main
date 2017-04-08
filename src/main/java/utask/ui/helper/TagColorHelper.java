//@@author A0139996A
package utask.ui.helper;

import java.util.Random;;

public class TagColorHelper {

    public static int getSize() {
        return ColorType.values().length;
    }

    public static String getARandomColor() {
        ColorType color = randomEnum(ColorType.class);
        return color.toString();
    }

    public static String getColorValueFromIndex (int colorIndex) {
        return getColorTypeFromIndex(colorIndex).toString();
    }

    private static ColorType getColorTypeFromIndex (int colorIndex) {
        assert colorIndex >= 0 : "Color index must be equals or greater than 0";
        assert colorIndex < getSize() : "Color index out of bounds of ColorType";
        return ColorType.values()[colorIndex];
    }

    public static String getColorValueFromName (String name) {
        return getColorTypeFromName(name.toUpperCase()).toString();
    }

    public static ColorType getColorTypeFromName (String name) {
        assert name != null : "Provided color name is null";
        return ColorType.valueOf(name);
    }

    private static <T extends Enum<?>> T randomEnum(Class<T> c) {
        Random random = new Random();
        int r = random.nextInt(c.getEnumConstants().length);
        return c.getEnumConstants()[r];
    }

    public static String getListOfSupportedColor() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < ColorType.values().length; i++) {
            sb.append(ColorType.values()[i].name() + " ");
        }

        return sb.toString();
    }

    public enum ColorType {
        BLACK("#000"),
        BLUE("#2196F3"),
        CYAN("#00BCD4"),
        GREEN("#009688"),
        ORANGE("#FF5722"),
        PINK("#F06292"),
        PURPLE("#673AB7"),
        RED("#B71C1C"),
        YELLOW("#FFA000");

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

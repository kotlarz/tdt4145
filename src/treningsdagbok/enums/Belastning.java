package treningsdagbok.enums;

import java.util.Arrays;

public enum Belastning {
    LAV, MIDDELS, HØY;

    public static String getOptions() {
        return Arrays.toString(Belastning.values());
    }
}

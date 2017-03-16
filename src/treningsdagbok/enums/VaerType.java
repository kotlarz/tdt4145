package treningsdagbok.enums;

import java.util.Arrays;

public enum VaerType {
    SOL, REGN;

    public static String getOptions() {
        return Arrays.toString(VaerType.values());
    }
}

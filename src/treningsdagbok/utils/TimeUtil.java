package treningsdagbok.utils;

import java.util.Arrays;
import java.util.List;

public class TimeUtil {
    private final TimeUnit unit;
    private final int amount;

    public enum TimeUnit {
        MILLISECOND(new String[]{"milisekunder", "ms"}, 1, 0.02),
        SECOND(new String[]{"sekunder", "s"}, 1000, 20),
        MINUTE(new String[]{"minutter", "m"}, 60000, 1200),
        HOUR(new String[]{"timer", "t"}, 3600000, 72000),
        DAY(new String[]{"dager", "d"}, 86400000, 1728000);

        private String[] aliases;
        private int milliseconds;
        private double ticks;

        TimeUnit(String[] aliases, int milliseconds, double ticks) {
            this.aliases = aliases.clone();
            this.milliseconds = milliseconds;
            this.ticks = ticks;
        }

        public List<String> getAliases() {
            return Arrays.asList(this.aliases);
        }

        public String getName() {
            return this.name().toLowerCase();
        }

        public String getPlural() {
            return getAliases().get(0).toLowerCase();
        }

        public int getMilliseconds() {
            return this.milliseconds;
        }

        public int getSeconds() {
            return this.milliseconds / 1000;
        }

        public int getTicks() {
            return (int) this.ticks;
        }

        public static TimeUnit getUnit(String name) {
            for (TimeUnit unit : TimeUnit.values()) {
                if (unit.getName().equalsIgnoreCase(name) || unit.getAliases().contains(name.toLowerCase())) {
                    return unit;
                }
            }
            return null;
        }
    }

    public TimeUtil(int seconds) {
        this.unit = TimeUnit.SECOND;
        this.amount = seconds;
    }

    public TimeUtil(long seconds) {
        this((int) seconds);
    }

    public TimeUtil(String timeString) {
        String[] split = timeString.split(" ");
        this.amount = Integer.parseInt(split[0]);
        this.unit = TimeUnit.getUnit(split[1].toLowerCase());
    }

    public TimeUtil(int amount, TimeUnit unit) {
        this.amount = amount;
        this.unit = unit;
    }

    public TimeUtil(int amount, String unit) {
        this.amount = amount;
        this.unit = TimeUnit.getUnit(unit.toLowerCase());
    }

    public TimeUnit getUnit() {
        return this.unit;
    }

    public int getAmount() {
        return this.amount;
    }

    public int getTicks() {
        return getUnit().getTicks() * getAmount();
    }

    public int getSeconds() {
        return getUnit().getSeconds() * getAmount();
    }

    @Override
    public String toString() {
        if (getUnit().equals(TimeUnit.SECOND) && getAmount() > 60) {
            /*TODO: Create a seconds to string converter*/
            return getAmount() + " " + getUnit().getPlural();
        } else if (getAmount() > 1) {
            return getAmount() + " " + getUnit().getPlural();
        } else {
            return getAmount() + " " + getUnit().getName();
        }
    }
}

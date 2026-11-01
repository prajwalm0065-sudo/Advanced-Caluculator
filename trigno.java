package operations;

public class Trig {

    private static final double PI = 3.14159;

    public static double sin(double angle) {
        return Math.sin(angle * PI / 180);
    }

    public static double cos(double angle) {
        return Math.cos(angle * PI / 180);
    }

    public static double tan(double angle) {
        return Math.tan(angle * PI / 180);
    }
}

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class SquareRoot {

    public static void main(String[] args) {
        final var t0 = System.currentTimeMillis();

        final var reps = 100*100*100*100;
        final var step = reps/10;
        final var eMax = 1.0E-10;
        allSqrts(reps, step, eMax);

        final var t1 = System.currentTimeMillis();
        final var dt = (t1 - t0)/1000;
        System.out.println("Time/s: " + dt);
    }

    public static void allSqrts(int reps, int step, double eMax) {
        var dev = new Result(0, 0, 0);
        for (int rep = 1; rep <= reps; rep++) {
            final var s = pow(pow(PI, PI), PI) + PI*rep;
        
            final var r1 = sqrtByIteration(s, eMax);
            final var r2 = sqrtByRecursion(s, eMax);

            dev = new Result(dev.n + abs(r1.n - r2.n), dev.x + abs(r1.x - r2.x), dev.e + abs(r1.e - r2.e));

            if (rep % step == 0) {
                System.out.println(r1.n + ": " + sqrt(s) + " = " + r1.x + " +/- " + r1.e);
                System.out.println(r2.n + ": " + sqrt(s) + " = " + r2.x + " +/- " + r2.e);
            }
        }
        System.out.println("Deviation: " + dev.n + ": " + dev.x + ", " + dev.e);
    }

    private static Result sqrtByIteration(double s, double eMax) {
        final var i = initial(s, eMax);
        var n = i.n;
        var x = i.x;
        var e = i.e;
        while(abs(e) > eMax) {
            n++;
            x = improve(x, e);
            e = error(s, x);
        }
        return new Result(n, x, abs(e));
    }

    private static Result sqrtByRecursion(double s, double eMax) {
        final var r = sqrtByRecursion(s, eMax, initial(s, eMax));
        return new Result(r.n, r.x, abs(r.e));
    }

    private static Result sqrtByRecursion(double s, double eMax, Result r) {
        if (abs(r.e) <= eMax) return r;

        final var x = improve(r.x, r.e);
        final var e = error(s, x);
        return sqrtByRecursion(s, eMax, new Result(r.n + 1, x, e));
    }

    private static Result initial(double s, double eMax) {
        return new Result(
            0, 
            eMax,          // terrible choice
            error(s, eMax) // for x = eMax
        );
    }

    private static double error(double s, double x) {
        return (s - x*x)/(2*x);
    }

    private static double improve(double x, double e) {
        return x + e;
    }

    private static class Result {
        final int n;
        final double x;
        final double e;
        Result(int n, double x, double e) {
            this.n = n;
            this.x = x;
            this.e = e;
        }
    }
}
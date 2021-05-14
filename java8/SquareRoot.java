import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class SquareRoot {

    public static void main(String[] args) {
        final long t0 = System.currentTimeMillis();

        final int reps = 100*100*100*100;
        final int step = reps/10;
        final double eMax = 1.0E-10;
        allSqrts(reps, step, eMax);

        final long t1 = System.currentTimeMillis();
        final long dt = (t1 - t0)/1000;
        System.out.println("Time/s: " + dt);
    }

    public static void allSqrts(int reps, int step, double eMax) {
        Result dev = new Result(0, 0, 0);
        for (int rep = 1; rep <= reps; rep++) {
            final double s = pow(pow(PI, PI), PI) + PI*rep;
        
            final Result r1 = sqrtByIteration(s, eMax);
            final Result r2 = sqrtByRecursion(s, eMax);

            assertCorrectness(r1.x, s, eMax);
		    assertCorrectness(r2.x, s, eMax);

            dev = new Result(dev.n + abs(r1.n - r2.n), dev.x + abs(r1.x - r2.x), dev.e + abs(r1.e - r2.e));

            if (rep % step == 0) {
                System.out.println(r1.n + ": " + sqrt(s) + " = " + r1.x + " +/- " + r1.e);
                System.out.println(r2.n + ": " + sqrt(s) + " = " + r2.x + " +/- " + r2.e);
            }
        }
        System.out.println("Accumulated Deviations: " + dev.n + ": " + dev.x + ", " + dev.e);
        assertAccumDevs(dev, eMax);
    }

    private static void assertCorrectness(double x, double s, double eMax) {
        final double c = sqrt(s);
        final double e = abs(c - x);
        final double eMaxSafe = eMax * 1.01; // add 1% safety margin because error used during calculation is an approximation
        assert e <= eMaxSafe : "sqrt(" + s + "): deviation " + e + " between correct result " + c + " and approximation " + x + " exceeds maximally allowed error " + eMax;
    }
    
    private static void assertAccumDevs(Result d, double eMax) {
        assert d.n == 0 && d.x <= eMax && d.e <= eMax : "Accumulated deviations exceed acceptable thresholds";
    }

    private static Result sqrtByIteration(double s, double eMax) {
        final Result i = initial(s, eMax);
        int n = i.n;
        double x = i.x;
        double e = i.e;
        while(abs(e) > eMax) {
            n++;
            x = improve(x, e);
            e = error(s, x);
        }
        return new Result(n, x, abs(e));
    }

    private static Result sqrtByRecursion(double s, double eMax) {
        final Result r = sqrtByRecursion(s, eMax, initial(s, eMax));
        return new Result(r.n, r.x, abs(r.e));
    }

    private static Result sqrtByRecursion(double s, double eMax, Result r) {
        if (abs(r.e) <= eMax) return r;

        final double x = improve(r.x, r.e);
        final double e = error(s, x);
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
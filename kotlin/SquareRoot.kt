import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

fun main() {
    val t0 = System.currentTimeMillis()
    
    val reps = 100*100*100*100
    val step = reps/10
    val eMax = 1.0E-10
    allSqrts(reps, step, eMax)

    val t1 = System.currentTimeMillis()
    val dt = (t1 - t0)/1000
    println("Time/s: $dt")
}

fun allSqrts(reps: Int, step: Int, eMax: Double) {
    var dev = Result(0, 0.0, 0.0)
    for (rep in 1..reps) {
        val s = PI.pow(PI).pow(PI) + PI*rep
    
        val r1 = sqrtByIteration(s, eMax)
        val r2 = sqrtByRecursion(s, eMax)

        assertCorrectness(r1.x, s, eMax)
		assertCorrectness(r2.x, s, eMax)

        dev = Result(dev.n + abs(r1.n - r2.n), dev.x + abs(r1.x - r2.x), dev.e + abs(r1.e - r2.e))

        if (rep % step == 0) {
            println("${r1.n}: ${sqrt(s)} = ${r1.x} +/- ${r1.e}")
            println("${r2.n}: ${sqrt(s)} = ${r2.x} +/- ${r2.e}")
        }
    }
    println("Accumulated Deviations: ${dev.n}: ${dev.x}, ${dev.e}")
    assertAccumDevs(dev, eMax)
}

private fun assertCorrectness(x: Double, s: Double, eMax: Double) {
	val c = sqrt(s)
	val e = abs(c - x)
	val eMaxSafe = eMax * 1.01 // add 1% safety margin because error used during calculation is an approximation
    assert(e <= eMaxSafe) {"sqrt($s): deviation $e between correct result $c and approximation $x exceeds maximally allowed error $eMax"}
}

private fun assertAccumDevs(d: Result, eMax: Double) {
    assert(d.n == 0 && d.x <= eMax && d.e <= eMax) {"Accumulated deviations exceed acceptable thresholds"}
}

private fun sqrtByIteration(s: Double, eMax: Double): Result {
    val i = initial(s, eMax)
    var n = i.n
    var x = i.x
    var e = i.e
    while(abs(e) > eMax) {
        n++
        x = improve(x, e)
        e = error(s, x)
    }
    return Result(n, x, abs(e))
}

private fun sqrtByRecursion(s: Double, eMax: Double) = 
    sqrtByRecursion(s, eMax, initial(s, eMax)).run {
        Result(n, x, abs(e))
    }

private tailrec fun sqrtByRecursion(s: Double, eMax: Double, r: Result): Result =
    if (abs(r.e) <= eMax) r
    else {
        val x = improve(r.x, r.e)
        val e = error(s, x)
        sqrtByRecursion(s, eMax, Result(r.n + 1, x, e))
    }

private fun initial(s: Double, eMax: Double) = Result(
    0, 
    eMax,          // terrible choice
    error(s, eMax) // for x = eMax
)

private inline fun error(s: Double, x: Double) = (s - x*x)/(2*x)

private inline fun improve(x: Double, e: Double) = x + e

data class Result(val n: Int, val x: Double, val e: Double)

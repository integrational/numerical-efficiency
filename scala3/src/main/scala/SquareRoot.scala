import java.lang.Math.{PI, abs, pow, sqrt}
import java.lang.System.currentTimeMillis
import scala.annotation.tailrec

@main def main(): Unit =
  val t0 = currentTimeMillis()

  val reps = 100 * 100 * 100 * 100
  val step = reps / 10
  val eMax = 1.0E-10
  allSqrts(reps, step, eMax)

  val t1 = currentTimeMillis()
  val dt = (t1 - t0) / 1000
  println(s"Time/s: $dt")

def allSqrts(reps: Int, step: Int, eMax: Double): Unit =
  var dev = Result(0, 0.0, 0.0)
  for rep <- 1 to reps do
    val s = pow(pow(PI, PI), PI) + PI * rep
    val r1 = sqrtByIteration(s, eMax)
    val r2 = sqrtByRecursion(s, eMax)
    assertCorrectness(r1.x, s, eMax)
    assertCorrectness(r2.x, s, eMax)
    dev = Result(dev.n + abs(r1.n - r2.n), dev.x + abs(r1.x - r2.x), dev.e + abs(r1.e - r2.e))
    if rep % step == 0 then
      println(s"${r1.n}: ${sqrt(s)} = ${r1.x} +/- ${r1.e}")
      println(s"${r2.n}: ${sqrt(s)} = ${r2.x} +/- ${r2.e}")

  println(s"Accumulated Deviations: ${dev.n}: ${dev.x}, ${dev.e}")
  assertAccumDevs(dev, eMax)

private def assertCorrectness(x: Double, s: Double, eMax: Double) =
  val c = sqrt(s)
  val e = abs(c - x)
  val eMaxSafe = eMax * 1.01 // add 1% safety margin because error used during calculation is an approximation
  assert(e <= eMaxSafe, s"sqrt($s): deviation $e between correct result $c and approximation $x exceeds maximally allowed error $eMax")

private def assertAccumDevs(d: Result, eMax: Double) =
  assert(d.n == 0 && d.x <= eMax && d.e <= eMax, "Accumulated deviations exceed acceptable thresholds")

private def sqrtByIteration(s: Double, eMax: Double): Result =
  val i = initial(s, eMax)
  var (n, x, e) = (i.n, i.x, i.e)
  while abs(e) > eMax do
    n += 1
    x = improve(x, e)
    e = error(s, x)
  Result(n, x, abs(e))

private def sqrtByRecursion(s: Double, eMax: Double): Result =
  val r = sqrtByRecursion(s, eMax, initial(s, eMax))
  Result(r.n, r.x, abs(r.e))

@tailrec
private def sqrtByRecursion(s: Double, eMax: Double, r: Result): Result =
  if abs(r.e) <= eMax then r
  else
    val x = improve(r.x, r.e)
    val e = error(s, x)
    sqrtByRecursion(s, eMax, Result(r.n + 1, x, e))

private def initial(s: Double, eMax: Double) = Result(
  0,
  eMax, // terrible choice
  error(s, eMax) // for x = eMax
)

private inline def error(s: Double, x: Double) = (s - x * x) / (2 * x)
private inline def improve(x: Double, e: Double) = x + e

case class Result(val n: Int, val x: Double, val e: Double)

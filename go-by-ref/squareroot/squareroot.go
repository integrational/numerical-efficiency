package main

import (
	"fmt"
	"math"
	"time"
)

func main() {
	t0 := time.Now()

	reps := 100 * 100 * 100 * 100
	step := reps / 10
	eMax := 1.0e-10
	allSqrts(reps, step, eMax)

	t1 := time.Now()
	dt := t1.Sub(t0).Seconds()
	fmt.Println("Time/s:", int(dt))
}

func allSqrts(reps, step int, eMax float64) {
	dev := Result{0, 0.0, 0.0}
	for rep := 1; rep <= reps; rep++ {
		s := math.Pow(math.Pow(math.Pi, math.Pi), math.Pi) + math.Pi*float64(rep)

		r1 := sqrtByIteration(s, eMax)
		r2 := sqrtByRecursion(s, eMax)

		assertCorrectness(r1.x, s, eMax)
		assertCorrectness(r2.x, s, eMax)

		dev.n += int(math.Abs(float64(r1.n - r2.n)))
		dev.x += math.Abs(r1.x - r2.x)
		dev.e += math.Abs(r1.e - r2.e)

		if rep%step == 0 {
			fmt.Println(r1.n, ":", math.Sqrt(s), "=", r1.x, "+/-", r1.e)
			fmt.Println(r2.n, ":", math.Sqrt(s), "=", r2.x, "+/-", r2.e)
		}
	}
	fmt.Println("Accumulated Deviations:", dev.n, ":", dev.x, ",", dev.e)
	assertAccumDevs(dev, eMax)
}

func assertCorrectness(x, s, eMax float64) {
	c := math.Sqrt(s)
	e := math.Abs(c - x)
	eMaxSafe := eMax * 1.01 // add 1% safety margin because error used during calculation is an approximation
	if e > eMaxSafe {
		panic(fmt.Sprint("sqrt(", s, "): deviation ", e, " between correct result ", c,
			" and approximation ", x, " exceeds maximally allowed error ", eMax))
	}
}

func assertAccumDevs(d Result, eMax float64) {
	if d.n > 0 || d.x > eMax || d.e > eMax {
		panic("Accumulated deviations exceed acceptable thresholds")
	}
}

func sqrtByIteration(s, eMax float64) *Result {
	i := initial(s, eMax)
	n := i.n
	x := i.x
	e := i.e
	for math.Abs(e) > eMax {
		n++
		x = improve(x, e)
		e = error(s, x)
	}
	return &Result{n, x, math.Abs(e)}
}

func sqrtByRecursion(s, eMax float64) *Result {
	r := initial(s, eMax)
	sqrtByRecursionInternal(s, eMax, r)
	r.e = math.Abs(r.e)
	return r
}

func sqrtByRecursionInternal(s, eMax float64, r *Result) {
	if math.Abs(r.e) <= eMax {
		return
	} else {
		r.n += 1
		r.x = improve(r.x, r.e)
		r.e = error(s, r.x)
		sqrtByRecursionInternal(s, eMax, r)
	}
}

func initial(s, eMax float64) *Result {
	return &Result{
		0,
		eMax,           // terrible choice
		error(s, eMax), // for x = eMax
	}
}

func error(s, x float64) float64 {
	return (s - x*x) / (2 * x)
}

func improve(x, e float64) float64 {
	return x + e
}

type Result struct {
	n    int
	x, e float64
}

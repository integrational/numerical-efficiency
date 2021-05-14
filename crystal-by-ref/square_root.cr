t0 = Time.monotonic

reps  = 100*100*100*100
step  = (reps/10).to_i
e_max = 1.0E-10
all_sqrts reps, step, e_max

t1 = Time.monotonic
dt = (t1 - t0).seconds
puts "Time/s: #{dt}"

def all_sqrts(reps : Int32, step : Int32, e_max : Float64)
  dev = Result.new 0, 0.0, 0.0
  rep = 1
  while rep <= reps
    s = ((Math::PI ** Math::PI) ** Math::PI) + Math::PI*rep

    r1 = sqrt_by_iteration s, e_max
    r2 = sqrt_by_recursion s, e_max

    assert_correctness r1.x, s, e_max
		assert_correctness r2.x, s, e_max

    dev = Result.new dev.n + (r1.n - r2.n).abs, dev.x + (r1.x - r2.x).abs, dev.e + (r1.e - r2.e).abs

    if rep % step == 0
        puts "#{r1.n}: #{Math.sqrt(s)} = #{r1.x} +/- #{r1.e}"
        puts "#{r2.n}: #{Math.sqrt(s)} = #{r2.x} +/- #{r2.e}"
    end
    rep += 1
  end
  puts "Accumulated Deviations: #{dev.n}: #{dev.x}, #{dev.e}"
  assert_accum_devs dev, e_max
end

def assert_correctness(x : Float64, s : Float64, e_max : Float64)
	c = Math.sqrt(s)
	e = (c - x).abs
	e_max_safe = e_max * 1.01 # add 1% safety margin because error used during calculation is an approximation
  raise "sqrt(#{s}): deviation #{e} between correct result #{c} and approximation #{x} exceeds maximally allowed error #{e_max}" unless e <= e_max_safe
end

def assert_accum_devs(d : Result, e_max : Float64)
  raise "Accumulated deviations exceed acceptable thresholds" unless d.n == 0 && d.x <= e_max && d.e <= e_max
end

def sqrt_by_iteration(s : Float64, e_max : Float64) : Result
  i       = initial s, e_max
  n, x, e = i.n, i.x, i.e
  while e.abs > e_max
    n += 1
    x  = improve x, e
    e  = error s, x
  end
  Result.new n, x, e.abs
end

def sqrt_by_recursion(s : Float64, e_max : Float64) : Result
  r = sqrt_by_recursion s, e_max, initial(s, e_max)
  Result.new r.n, r.x, r.e.abs
end

def sqrt_by_recursion(s : Float64, e_max : Float64, r : Result) : Result
  if r.e.abs <= e_max
    r
  else
    x = improve r.x, r.e
    e = error s, x
    sqrt_by_recursion s, e_max, Result.new(r.n + 1, x, e)
  end
end

def initial(s : Float64, e_max : Float64) : Result
  Result.new 0, 
    e_max,          # terrible choice
    error(s, e_max) # for x = e_max
end

def error(s : Float64, x : Float64) : Float64
  (s - x*x)/(2*x)
end

def improve(x : Float64, e : Float64) : Float64
  x + e
end

class Result
  getter n : Int32
  getter x : Float64
  getter e : Float64

  def initialize(@n, @x, @e) 
  end
end
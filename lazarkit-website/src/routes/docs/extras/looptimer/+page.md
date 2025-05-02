# LoopTimer  
Measures time intervals and calculates smoothed frequency (Hz).  

## Constructor  
`LoopTimer(smootherWindow: Int = 5)`  
- `smootherWindow`: Window size for smoothing Hz values (default: 5).  

## Properties  
| Name       | Type   | Description                |  
|------------|--------|----------------------------|  
| `ms`       | `Long` | Last measured interval (ms) |  
| `hz`       | `Double` | Smoothed frequency (Hz)     |  
| `startTime`| `Long` | Timestamp of `start()` call |  
| `endTime`  | `Long` | Timestamp of `end()` call   |  

## Methods  
- `start()` – Start timing.  
- `end()` – End timing, updates `ms` and `hz`.  

## Usage Example  
```kotlin
val timer = LoopTimer()
timer.start()
// Simulate work
Thread.sleep(100)
timer.end()
println("Time: ${timer.ms}ms, Freq: ${timer.hz}Hz")
```
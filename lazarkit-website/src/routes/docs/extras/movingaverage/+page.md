# MovingAverageSmoother  
Calculates a sliding-window average for smoothing data streams.

## Constructor  
**MovingAverageSmoother(windowSize: Int)**  
- `windowSize`: Maximum number of values to track in the moving window.

## Methods  
- `addValue(value: Double): Double` – Adds a value to the window (evicts oldest if full). Returns the current average.  
- `reset()` – Clears all values and resets the internal sum. 

## Example  
```kotlin
val smoother = MovingAverageSmoother(3)
smoother.addValue(10.0) // 10.0
smoother.addValue(20.0) // 15.0
smoother.addValue(30.0) // 20.0
smoother.addValue(40.0) // 30.0 (oldest value 10.0 removed)
```
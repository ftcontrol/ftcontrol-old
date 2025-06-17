package com.bylazar.ftcontrol.panels.json

class CanvasPresets {
    val PANELS = CanvasPreset { /* no-op */ }

    val PEDRO_PATHING = CanvasPreset {
        it.offsetX = -24.0 * 3
        it.offsetY = 24.0 * 3
        it.rotation = CanvasRotation.DEG_270
    }

    val ROAD_RUNNER = CanvasPreset { /* no-op */ }
}
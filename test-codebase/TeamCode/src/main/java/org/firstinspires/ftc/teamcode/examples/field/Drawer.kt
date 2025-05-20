package org.firstinspires.ftc.teamcode.examples.field

import com.bylazar.ftcontrol.panels.Panels
import com.bylazar.ftcontrol.panels.json.Canvas
import com.bylazar.ftcontrol.panels.json.CanvasRotation
import com.bylazar.ftcontrol.panels.json.Circle
import com.bylazar.ftcontrol.panels.json.Look
import com.bylazar.ftcontrol.panels.json.Point

class Drawer {
    fun draw() {
        val canvas = Canvas().withOffsetX(0.0).withOffsetY(0.0).withRotation(CanvasRotation.DEG_0)

        canvas.add(
            Circle(
                center = Point(0.0, 0.0),
                radius = 10.0,
            ).withLook(
                Look(
                    outlineColor = "red",
                    outlineWidth = 1.0,
                    fillColor = "",
                    opacity = 1.0
                )
            )
        )

        Panels.getTelemetry().sendCanvas(canvas)
    }
}
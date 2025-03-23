package org.lazarkit.panels

import org.firstinspires.ftc.robotcore.external.Func
import org.firstinspires.ftc.robotcore.external.Telemetry

class TelemetryManager: Telemetry {
    override fun addData(caption: String?, format: String?, vararg args: Any?): Telemetry.Item {
        TODO("Not yet implemented")
    }

    override fun addData(caption: String?, value: Any?): Telemetry.Item {
        TODO("Not yet implemented")
    }

    override fun <T : Any?> addData(caption: String?, valueProducer: Func<T>?): Telemetry.Item {
        TODO("Not yet implemented")
    }

    override fun <T : Any?> addData(
        caption: String?,
        format: String?,
        valueProducer: Func<T>?
    ): Telemetry.Item {
        TODO("Not yet implemented")
    }

    override fun removeItem(item: Telemetry.Item?): Boolean {
        TODO("Not yet implemented")
    }

    override fun clear() {
        TODO("Not yet implemented")
    }

    override fun clearAll() {
        TODO("Not yet implemented")
    }

    override fun addAction(action: Runnable?): Any {
        TODO("Not yet implemented")
    }

    override fun removeAction(token: Any?): Boolean {
        TODO("Not yet implemented")
    }

    override fun speak(text: String?) {
        TODO("Not yet implemented")
    }

    override fun speak(text: String?, languageCode: String?, countryCode: String?) {
        TODO("Not yet implemented")
    }

    override fun update(): Boolean {
        TODO("Not yet implemented")
    }

    override fun addLine(): Telemetry.Line {
        TODO("Not yet implemented")
    }

    override fun addLine(lineCaption: String?): Telemetry.Line {
        TODO("Not yet implemented")
    }

    override fun removeLine(line: Telemetry.Line?): Boolean {
        TODO("Not yet implemented")
    }

    override fun isAutoClear(): Boolean {
        TODO("Not yet implemented")
    }

    override fun setAutoClear(autoClear: Boolean) {
        TODO("Not yet implemented")
    }

    override fun getMsTransmissionInterval(): Int {
        TODO("Not yet implemented")
    }

    override fun setMsTransmissionInterval(msTransmissionInterval: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemSeparator(): String {
        TODO("Not yet implemented")
    }

    override fun setItemSeparator(itemSeparator: String?) {
        TODO("Not yet implemented")
    }

    override fun getCaptionValueSeparator(): String {
        TODO("Not yet implemented")
    }

    override fun setCaptionValueSeparator(captionValueSeparator: String?) {
        TODO("Not yet implemented")
    }

    override fun setDisplayFormat(displayFormat: Telemetry.DisplayFormat?) {
        TODO("Not yet implemented")
    }

    override fun log(): Telemetry.Log {
        TODO("Not yet implemented")
    }
}
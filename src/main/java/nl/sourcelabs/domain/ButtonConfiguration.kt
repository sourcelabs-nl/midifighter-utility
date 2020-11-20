package nl.sourcelabs.domain

import nl.sourcelabs.domain.LedColor.BRIGHT_BLUE
import nl.sourcelabs.domain.LedColor.BRIGHT_LIME
import nl.sourcelabs.domain.LedColor.BRIGHT_RED
import nl.sourcelabs.domain.LedColor.OFF
import nl.sourcelabs.domain.LedColor.WHITE

data class ButtonConfiguration(
    val button: Button,
    val activeState: ButtonState = ButtonState(color = WHITE),
    val processingState: ButtonState = ButtonState(color = BRIGHT_BLUE, pulse = true),
    val errorState: ButtonState = ButtonState(color = BRIGHT_RED, pulse = true),
    val successState: ButtonState = ButtonState(color = BRIGHT_LIME),
    val defaultState: ButtonState = ButtonState(color = OFF),
    val listener: ButtonListener? = null,
    val action: ButtonAction? = null
)

class ButtonListener(private val press: (() -> Unit)? = null, private val release: (() -> Unit)? = null) {

    fun onPress() = press?.invoke()

    fun onRelease() = release?.invoke()
}

class ButtonAction(val interval: Long = 5000, private val action: () -> Boolean) {

    fun executeAction(): Boolean {
        return try {
            action()
        } catch(throwable: Throwable) {
            false
        }
    }
}
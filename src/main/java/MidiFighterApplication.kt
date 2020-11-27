import nl.sourcelabs.device.MidiFighter
import nl.sourcelabs.domain.*
import java.awt.Desktop
import java.net.URI

fun main() {
    MidiFighter().apply {
        Button.values().forEach {
            addButtonConfiguration(ButtonConfiguration(
                button = it,
                activeState = ButtonState(color = LedColor.OFF),
                processingState = ButtonState(color = LedColor.BLUE, pulse = true),
                defaultState = ButtonState(color = LedColor.OFF),
                successState = ButtonState(color = LedColor.BLUE),
                errorState = ButtonState(color = LedColor.BRIGHT_RED, pulse = true),
                action = ButtonAction(20000) {
                    Thread.sleep((1000L..4000L).random())
                    (0..100).random() % 10 != 0
                },
                listener = ButtonListener(
                    release = {
                        Desktop.getDesktop().browse(URI("https://www.google.com/appsstatus#hl=en&v=status"))
                    }
                )
            ))
        }
    }
}
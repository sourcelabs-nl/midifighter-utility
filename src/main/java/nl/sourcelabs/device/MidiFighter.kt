package nl.sourcelabs.device

import nl.sourcelabs.domain.Button
import nl.sourcelabs.domain.ButtonConfiguration
import nl.sourcelabs.domain.ButtonState
import nl.sourcelabs.domain.LedColor
import nl.sourcelabs.domain.LedColor.OFF
import java.util.*
import java.util.concurrent.Executors
import javax.sound.midi.*

private const val MIDI_FIGHTER_DEVICE_NAME = "Midi Fighter Spectra"
private val timer = Timer()
private val executor = Executors.newCachedThreadPool()

class MidiFighter {

    init {
       val midiFighter = this
        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun run() {
                midiFighter.allOff()
                midiFighter.close()
                println("Closed channels.")
            }
        })
    }

    private val receiver = getReceiver(MIDI_FIGHTER_DEVICE_NAME)

    private val transmitter = getTransmitter(MIDI_FIGHTER_DEVICE_NAME).apply {
        receiver = object : Receiver {
            override fun close() {
            }

            override fun send(message: MidiMessage, timeStamp: Long) {
                if (message is ShortMessage) {
                    val button = Button.getByNote(message.data1)
                    buttonConfigurations.firstOrNull { it.button == button }?.let {
                        if (message.command == ShortMessage.NOTE_ON) {
                            it.listener?.onPress()
                            updateState(it.button, it.activeState)
                        } else if (message.command == ShortMessage.NOTE_OFF) {
                            it.listener?.onRelease()
                            updateState(it.button, it.defaultState)
                        }
                    }
                }
            }
        }
    }

    private fun updateState(button: Button, buttonState: ButtonState) {
        setColor(button, buttonState.color)
        pulse(button, buttonState.pulse)
    }

    private val buttonConfigurations = mutableListOf<ButtonConfiguration>()

    fun addButtonConfiguration(buttonConfiguration: ButtonConfiguration) {
        if (buttonConfigurations.count { it.button == buttonConfiguration.button } > 0) {
            throw IllegalArgumentException("Button configuration already present.")
        }
        buttonConfigurations.add(buttonConfiguration)
        if (buttonConfiguration.action != null) {
            timer.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    executor.execute {
                        updateState(buttonConfiguration.button, buttonConfiguration.processingState)
                        if (buttonConfiguration.action.executeAction()) {
                            updateState(buttonConfiguration.button, buttonConfiguration.successState)
                        } else {
                            updateState(buttonConfiguration.button, buttonConfiguration.errorState)
                        }
                    }
                }
            }, Date(), buttonConfiguration.action.interval)
        }
        updateState(buttonConfiguration.button, buttonConfiguration.defaultState)
    }

    private fun setColor(button: Button, color: LedColor) {
        receiver.send(ShortMessage(ShortMessage.NOTE_ON, 2, button.note, color.velocity), -1)
    }

    private fun pulse(button: Button, pulse: Boolean = true) {
        receiver.send(ShortMessage(ShortMessage.NOTE_ON, 3, button.note, if (pulse) 47 else 0), -1)
    }

    fun allOff() {
        Button.values().forEach {
            setColor(it, OFF)
        }
    }

    fun close() {
        receiver.close()
        transmitter.close()
    }
}

private fun getReceiver(deviceName: String) = MidiSystem.getMidiDevice(MidiSystem.getMidiDeviceInfo().first {
    it.name == deviceName && MidiSystem.getMidiDevice(it).maxReceivers == -1
}).also {
    it.open()
}.receiver

private fun getTransmitter(deviceName: String) = MidiSystem.getMidiDevice(MidiSystem.getMidiDeviceInfo().first {
    it.name == deviceName && MidiSystem.getMidiDevice(it).maxTransmitters == -1
}).also {
    it.open()
}.transmitter
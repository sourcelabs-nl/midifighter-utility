package nl.sourcelabs.domain

import nl.sourcelabs.domain.LedColor.OFF

data class ButtonState(
    val color: LedColor = OFF,
    val pulse: Boolean = false
)
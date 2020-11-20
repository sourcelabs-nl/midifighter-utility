package nl.sourcelabs.domain

enum class LedColor(val velocity: Int) {
    BRIGHT_RED(14),
    RED(20),
    BRIGHT_ORANGE(26),
    ORANGE(32),
    BRIGHT_YELLOW(38),
    YELLOW(44),
    BRIGHT_LIME(50),
    LIME(56),
    BRIGHT_GREEN(62),
    GREEN(68),
    BRIGHT_CYAN(74),
    CYAN(80),
    BRIGHT_BLUE(86),
    BLUE(92),
    BRIGHT_PURPLE(98),
    PURPLE(104),
    BRIGHT_PINK(110),
    PINK(116),
    WHITE(123),
    OFF(5)
}
package nl.sourcelabs.domain

enum class Button(val note: Int) {
    BUTTON1(36),
    BUTTON2(37),
    BUTTON3(38),
    BUTTON4(39),
    BUTTON5(40),
    BUTTON6(41),
    BUTTON7(42),
    BUTTON8(43),
    BUTTON9(44),
    BUTTON10(45),
    BUTTON11(46),
    BUTTON12(47),
    BUTTON13(48),
    BUTTON14(49),
    BUTTON15(50),
    BUTTON16(51);

    companion object {
        fun getByNote(note: Int) = Button.values().first { it.note == note }
    }
}
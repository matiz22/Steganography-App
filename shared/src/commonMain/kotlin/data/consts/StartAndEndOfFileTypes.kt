package data.consts

sealed class StartAndEndOfFileTypes(val start: ByteArray, val end: ByteArray) {
    data object PNGFileStartEnd : StartAndEndOfFileTypes(
        start = byteArrayOf(
            0x89.toByte(),
            0x50.toByte(),
            0x4E.toByte(),
            0x47.toByte(),
            0x0D.toByte(),
            0x0A.toByte(),
            0x1A.toByte(),
            0x0A.toByte()
        ),
        end = byteArrayOf(
            0x49.toByte(),
            0x45.toByte(),
            0x4E.toByte(),
            0x44.toByte(),
            0xAE.toByte(),
            0x42.toByte(),
            0x60.toByte(),
            0x82.toByte()
        )
    )

    data object JPGFileStartEnd : StartAndEndOfFileTypes(
        start = byteArrayOf(0xFF.toByte(), 0xD8.toByte()),
        end = byteArrayOf(0xFF.toByte(), 0xD9.toByte())
    )
}
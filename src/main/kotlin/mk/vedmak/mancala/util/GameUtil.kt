package mk.vedmak.mancala.util

import java.util.*

const val NR_OF_STONES = 6
const val P1_LOWER_LIMIT = 0
const val P1_UPPER_LIMIT = 6
const val P2_LOWER_LIMIT = 8
const val P2_UPPER_LIMIT = 13
const val P1_MAIN_POCKET = 7
const val P2_MAIN_POCKET = 14
const val P2_LOWER_BOUNDERY = 7


fun getRandomString(): String? {
    val leftLimit = 48 // numeral '0'
    val rightLimit = 122 // letter 'z'
    return Random().ints(leftLimit, rightLimit + 1)
            .filter { i: Int -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97) }
            .limit(10)
            .collect({ StringBuilder() }, { obj: StringBuilder, codePoint: Int -> obj.appendCodePoint(codePoint) }) { obj: StringBuilder, s: StringBuilder? -> obj.append(s) }
            .toString()
}
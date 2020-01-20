class LL1Parser(private val input: List<String>) {

    private var currentIndex = 0
    private val firstMap = mapOf(
        NTS_S to DIGITS + TS_OPENING_BRACKET,
        NTS_Z to DIGITS + TS_OPENING_BRACKET + TS_EPSILON,
        NTS_W to DIGITS + TS_OPENING_BRACKET,
        NTS_W_PRIM to OPERATORS + TS_EPSILON,
        NTS_P to DIGITS + TS_OPENING_BRACKET,
        NTS_R to DIGITS,
        NTS_R_PRIM to listOf(TS_DOT, TS_EPSILON),
        NTS_L to DIGITS,
        NTS_L_PRIM to DIGITS + TS_EPSILON,
        NTS_C to DIGITS,
        NTS_O to OPERATORS
    )

    fun parse() = s()

    private fun isCurrentSymbolEqualTo(symbol: String) =
        input[currentIndex] == symbol

    private fun isLastSymbolEqualToEpsilon() =
        isLastSymbol() && isCurrentSymbolEqualTo(TS_EPSILON)

    private fun isLastSymbol() = currentIndex == input.lastIndex

    private fun verifyProduction(
        productionSymbol: String,
        productionVerifier: () -> Boolean
    ): Boolean {
        val currentSymbol = input[currentIndex]
        val first = firstMap[productionSymbol]
        return when {
            first.isNullOrEmpty() -> false
            currentSymbol in first -> productionVerifier()
            TS_EPSILON in first -> {
                currentIndex -= 1
                true
            }
            else -> false
        }
    }

    private fun s() = verifyProduction(NTS_S, ::verifyS)

    // S -> W ; Z
    private fun verifyS(): Boolean {
        return if (w()) {
            currentIndex += 1
            if (isCurrentSymbolEqualTo(TS_SEMICOLON)) {
                currentIndex += 1
                z()
            } else {
                false
            }
        } else {
            false
        }
    }

    private fun z(): Boolean {
        if (isLastSymbolEqualToEpsilon()) return true
        return verifyProduction(NTS_Z, ::verifyZ)
    }

    // Z -> W ; Z
    // Z -> ''
    private fun verifyZ(): Boolean {
        return if (w()) {
            currentIndex += 1
            if (isCurrentSymbolEqualTo(TS_SEMICOLON)) {
                currentIndex += 1
                z()
            } else {
                false
            }
        } else {
            false
        }
    }

    private fun w() = verifyProduction(NTS_W, ::verifyW)

    //W -> P W'
    private fun verifyW(): Boolean {
        return if (p()) {
            currentIndex += 1
            wPrim()
        } else {
            false
        }
    }

    private fun wPrim(): Boolean {
        if (isLastSymbolEqualToEpsilon()) return true
        return verifyProduction(NTS_W_PRIM, ::verifyWPrim)
    }

    // W' -> O W
    // W' -> ''
    private fun verifyWPrim(): Boolean {
        return if (o()) {
            currentIndex += 1
            w()
        } else {
            false
        }
    }

    private fun p() = verifyProduction(NTS_P, ::verifyP)

    // P -> R
    // P -> ( W )
    private fun verifyP(): Boolean {
        return if (r()) {
            true
        } else if (isCurrentSymbolEqualTo(TS_OPENING_BRACKET)) {
            currentIndex += 1
            if (w()) {
                currentIndex += 1
                isCurrentSymbolEqualTo(TS_CLOSING_BRACKET)
            } else {
                false
            }
        } else {
            false
        }
    }

    private fun r() = verifyProduction(NTS_R, ::verifyR)

    // R -> L R'
    private fun verifyR(): Boolean {
        return if (l()) {
            currentIndex += 1
            rPrim()
        } else {
            false
        }
    }

    private fun rPrim(): Boolean {
        if (isLastSymbolEqualToEpsilon()) return true
        return verifyProduction(NTS_R_PRIM, ::verifyRPrim)
    }

    // R' -> . L
    // R' -> ''
    private fun verifyRPrim(): Boolean {
        return if (isCurrentSymbolEqualTo(TS_DOT)) {
            currentIndex += 1
            l()
        } else {
            false
        }
    }

    private fun l() = verifyProduction(NTS_L, ::verifyL)

    // L -> C L'
    private fun verifyL(): Boolean {
        return if (c()) {
            currentIndex += 1
            lPrim()
        } else {
            false
        }
    }

    private fun lPrim(): Boolean {
        if (isLastSymbolEqualToEpsilon()) return true
        return verifyProduction(NTS_L_PRIM, ::verifyLPrim)
    }

    // L' -> L
    // L' -> ''
    private fun verifyLPrim(): Boolean {
        return l()
    }

    private fun c() = verifyProduction(NTS_C, ::verifyC)

    // C -> 0
    // C -> 1
    // C -> 2
    // C -> 3
    // C -> 4
    // C -> 5
    // C -> 6
    // C -> 7
    // C -> 8
    // C -> 9
    private fun verifyC(): Boolean {
        val symbol = input[currentIndex]
        return isCurrentSymbolEqualTo(symbol)
    }

    private fun o() = verifyProduction(NTS_O, ::verifyO)

    // O -> *
    // O -> :
    // O -> +
    // O -> -
    // O -> ^
    private fun verifyO(): Boolean {
        val symbol = input[currentIndex]
        return isCurrentSymbolEqualTo(symbol)
    }

    private companion object {
        const val TS_SEMICOLON = ";"
        const val TS_OPENING_BRACKET = "("
        const val TS_CLOSING_BRACKET = ")"
        const val TS_DOT = "."
        const val TS_0 = "0"
        const val TS_1 = "1"
        const val TS_2 = "2"
        const val TS_3 = "3"
        const val TS_4 = "4"
        const val TS_5 = "5"
        const val TS_6 = "6"
        const val TS_7 = "7"
        const val TS_8 = "8"
        const val TS_9 = "9"
        const val TS_TIMES = "*"
        const val TS_COLON = ":"
        const val TS_PLUS = "+"
        const val TS_MINUS = "-"
        const val TS_CARET = "^"
        const val TS_EPSILON = "E"
        const val NTS_S = "S"
        const val NTS_Z = "Z"
        const val NTS_W = "W"
        const val NTS_W_PRIM = "W'"
        const val NTS_P = "P"
        const val NTS_R = "R"
        const val NTS_R_PRIM = "R'"
        const val NTS_L = "L"
        const val NTS_L_PRIM = "L'"
        const val NTS_C = "C"
        const val NTS_O = "O"
        val DIGITS = listOf(
            TS_0, TS_1, TS_2, TS_3, TS_4, TS_5, TS_6, TS_7, TS_8, TS_9
        )
        val OPERATORS = listOf(TS_TIMES, TS_COLON, TS_PLUS, TS_MINUS, TS_CARET)
    }
}
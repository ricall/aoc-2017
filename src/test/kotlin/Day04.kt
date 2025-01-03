package org.ricall.dayxx

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private fun parseLine(line: String) = line.split("\\s+".toRegex())

private fun isValidPassphrase(phrase: String) = parseLine(phrase).let {
    it.size == it.toSet().size
}

private fun solvePartOne(input: String) = input.lines().count(::isValidPassphrase)

private fun isValidPassphraseWithRearrangement(phrase: String) = parseLine(phrase).let { parts ->
    val sortedPartsAsSet = parts.map { it.split("").sorted().joinToString("") }.toSet()
    parts.size == sortedPartsAsSet.size
}

private fun solvePartTwo(input: String) = input.lines().count(::isValidPassphraseWithRearrangement)

class Day04 {
    @Test
    fun `part 1 test data`() {
        assertEquals(true, isValidPassphrase("aa bb cc dd ee"))
        assertEquals(false, isValidPassphrase("aa bb cc dd aa"))
        assertEquals(true, isValidPassphrase("aa bb cc dd aaa"))
    }

    @Test
    fun `part 1`() {
        val result = solvePartOne(File("./inputs/day04.txt").readText())

        assertEquals(383, result)
    }

    @Test
    fun `part 2 test data`() {
        assertEquals(true, isValidPassphraseWithRearrangement("abcde fghij"))
        assertEquals(false, isValidPassphraseWithRearrangement("abcde xyz ecdab"))
        assertEquals(true, isValidPassphraseWithRearrangement("a ab abc abd abf abj"))
        assertEquals(true, isValidPassphraseWithRearrangement("iiii oiii ooii oooi oooo"))
        assertEquals(false, isValidPassphraseWithRearrangement("oiii ioii iioi iiio"))
    }

    @Test
    fun `part 2`() {
        val result = solvePartTwo(File("./inputs/day04.txt").readText())

        assertEquals(265, result)
    }
}
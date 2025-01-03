package org.ricall.dayxx

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private fun parseLines(text: String) = text.lines().map { line -> line.split("\\s+".toRegex()).map { it.toInt() } }

private fun lineDifference(values: List<Int>) = values.max() - values.min()

private fun lineEvenlyDivisible(values: List<Int>): Int = values.sortedDescending().run {
    forEachIndexed { index, numerator ->
        (index + 1..<size).forEach { index2 ->
            val denominator = get(index2)
            if (numerator % denominator == 0) {
                return numerator / denominator
            }
        }
    }
    return 0
}

private fun solvePartOne(input: String) = parseLines(input).sumOf(::lineDifference)
private fun solvePartTwo(input: String) = parseLines(input).sumOf(::lineEvenlyDivisible)

class Day02 {

    @Test
    fun `part 1 test data`() {
        val result = solvePartOne(
            """
            |5 1 9 5
            |7 5 3
            |2 4 6 8""".trimMargin()
        )

        assertEquals(18, result)
    }

    @Test
    fun `part 1`() {
        assertEquals(50376, solvePartOne(File("./inputs/day02.txt").readText()))
    }

    @Test
    fun `part 2 test data`() {
        val result = solvePartTwo(
            """
            |5 9 2 8
            |9 4 7 3
            |3 8 6 5""".trimMargin()
        )

        assertEquals(9, result)
    }

    @Test
    fun `part 2`() {
        assertEquals(267, solvePartTwo(File("./inputs/day02.txt").readText()))
    }
}
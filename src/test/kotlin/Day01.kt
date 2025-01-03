package org.ricall.day01

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private fun solve(digits: String, offset: Int) = digits.mapIndexed { index, current ->
    if (current == digits[(index + offset) % digits.length]) "$current".toInt() else 0
}.sum()

private fun solvePartOne(digits: String) = solve(digits, 1)

private fun solvePartTwo(digits: String) = solve(digits, digits.length / 2)

class Day01 {
    @Test
    fun `part 1 test data`() {
        assertEquals(3, solvePartOne("1122"))
        assertEquals(4, solvePartOne("1111"))
        assertEquals(0, solvePartOne("1234"))
        assertEquals(9, solvePartOne("91212129"))
    }

    @Test
    fun `part 1`() {
        assertEquals(1390, solvePartOne(File("./inputs/Day01.txt").readText()))
    }

    @Test
    fun `part 2 test data`() {
        assertEquals(6, solvePartTwo("1212"))
        assertEquals(0, solvePartTwo("1221"))
        assertEquals(4, solvePartTwo("123425"))
        assertEquals(12, solvePartTwo("123123"))
        assertEquals(4, solvePartTwo("12131415"))
    }

    @Test
    fun `part 2`() {
        assertEquals(1232, solvePartTwo(File("./inputs/Day01.txt").readText()))
    }
}
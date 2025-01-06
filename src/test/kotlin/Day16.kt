package org.ricall.day16

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private fun CharArray.swap(a: Int, b: Int) {
    val temp = this[a]
    this[a] = this[b]
    this[b] = temp
}

private fun dance(programs: CharArray, danceMoves: List<String>) {
    for (step in danceMoves) {
        when(step[0]) {
            's' -> {
                val position = step.substring(1).toInt()
                val index = programs.size - position
                val temp = programs.copyOf()
                programs.indices.forEach { i -> programs[i] = temp[(i + index).mod(temp.size)] }
            }
            'x' -> {
                val (a, b) = step.substring(1).split('/').map(String::toInt)
                programs.swap(a, b)
            }
            'p' -> {
                val (a, b) = step.substring(1).split('/')
                programs.swap(programs.indexOf(a[0]), programs.indexOf(b[0]))
            }
            else -> error("Unknown dance move: $step")
        }
    }
}

private fun solve(input: String, programCount: Int = 16, iterations: Int = 1): String {
    val danceMoves = input.split(",")
    val programs = (0..<programCount).map { 'a' + it }.toCharArray()
    val initialState = programs.joinToString("")

    var iteration = 1
    while (iteration <= iterations) {
        dance(programs, danceMoves)
        if (programs.joinToString("") == initialState) {
            iteration *= (iterations / iteration)       // jump to last cycle before iterations complete
        }
        iteration++
    }
    return programs.joinToString("")
}

private fun solvePartOne(input: String, programCount: Int = 16) = solve(input, programCount)
private fun solvePartTwo(input: String, programCount: Int = 16) = solve(input, programCount, 1_000_000_000)

class Day16 {
    @Test
    fun `part 1 test data`() {
        val result = solvePartOne("s1,x3/4,pe/b", 5)

        assertEquals("baedc", result)
    }

    @Test
    fun `part 1`() {
        val result = solvePartOne(File("./inputs/day16.txt").readText())

        assertEquals("kpfonjglcibaedhm", result)
    }

    @Test
    fun `part 2 test data`() {
        val result = solvePartTwo("s1,x3/4,pe/b", 5)

        assertEquals("abcde", result)
    }

    @Test
    fun `part 2`() {
        val result = solvePartTwo(File("./inputs/day16.txt").readText())

        assertEquals("odiabmplhfgjcekn", result)
    }
}
package org.ricall.day23

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private class Computer(input: String) {
    private val registers = mutableMapOf<String, Long>()
    private val instructions = input.lines()
    private var programCounter = 0

    private fun toValue(field: String): Long = field.toLongOrNull() ?: registers.getOrDefault(field, 0L)

    private fun execute(instruction: String): Unit = instruction.split(" ").iterator().run {
        when (next()) {
            "set" -> registers[next()] = toValue(next())
            "sub" -> next().let { registers[it] = toValue(it) - toValue(next()) }
            "mul" -> next().let { registers[it] = toValue(it) * toValue(next()) }
            "jnz" -> if (toValue(next()) != 0L) programCounter += toValue(next()).toInt() - 1
        }
        programCounter++
    }

    fun solvePartOne(): Int {
        var mulCount = 0
        programCounter = 0
        while (programCounter in instructions.indices) {
            instructions[programCounter].run {
                if (startsWith("mul")) { mulCount++ }
                execute(this)
            }
        }
        return mulCount
    }
}

private fun solvePartTwo(): Int {
    var b = 67 * 100 + 100_000
    val c = b + 17000
    var h = 0
    var g = 1

    while (g != 0) {
        var f = 1
        var d = 2
        while (d < b) {
            if (b % d == 0) {
                f = 0
                break
            }
            d++
        }
        if (f == 0) {
            h++
        }
        g = b - c
        b += 17
    }
    return h
}

class Day23 {
    @Test
    fun `part 1`() {
        val result = Computer(File("./inputs/day23.txt").readText()).solvePartOne()

        assertEquals(4225, result)
    }

    @Test
    fun `part 2`() {
        val result = solvePartTwo()

        assertEquals(905, result)
    }
}
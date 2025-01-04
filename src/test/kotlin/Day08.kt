package org.ricall.day08

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private data class Instruction(
    val register: String,
    val operation: String,
    val amount: Int,
    val conditionRegister: String,
    val conditionOperation: String,
    val conditionAmount: Int
) {
    private fun isConditionTrue(registers: Map<String, Int>) = registers.getOrDefault(conditionRegister, 0).let {
        when (conditionOperation) {
            ">" -> it > conditionAmount
            "<" -> it < conditionAmount
            ">=" -> it >= conditionAmount
            "<=" -> it <= conditionAmount
            "==" -> it == conditionAmount
            "!=" -> it != conditionAmount
            else -> error("unsupported condition operation $conditionOperation")
        }
    }

    fun process(registers: MutableMap<String, Int>): Int {
        if (!isConditionTrue(registers)) {
            return 0
        }
        when (operation) {
            "inc" -> registers[register] = registers.getOrDefault(register, 0) + amount
            "dec" -> registers[register] = registers.getOrDefault(register, 0) - amount
            else -> error("unsupported operation $operation")
        }
        return registers[register]!!
    }
}

private val INSTRUCTION_REGEX = """^(\w+) (\w+) (-?\d+) if (\w+) ([!<=>]+) (-?\d+)$""".toRegex()
private fun parseInstruction(line: String) = INSTRUCTION_REGEX.find(line)?.let {
    val (register, operation, amount, conditionRegister, conditionOperation, conditionAmount) = it.destructured
    Instruction(register, operation, amount.toInt(), conditionRegister, conditionOperation, conditionAmount.toInt())
} ?: error("Could not parse line $line")

private class Computer(input: String) {
    val registers = mutableMapOf<String, Int>()
    val highestRegisterValue = input.lines().map(::parseInstruction).maxOf { it.process(registers) }
}

private fun solvePartOne(input: String) = Computer(input).registers.values.max()

private fun solvePartTwo(input: String) = Computer(input).highestRegisterValue

class Day08 {
    private val TEST_DATA = """
        |b inc 5 if a > 1
        |a inc 1 if b < 5
        |c dec -10 if a >= 1
        |c inc -20 if c == 10""".trimMargin()

    @Test
    fun `part 1 test data`() {
        val result = solvePartOne(TEST_DATA)

        assertEquals(1, result)
    }

    @Test
    fun `part 1`() {
        val result = solvePartOne(File("./inputs/day08.txt").readText())

        assertEquals(4902, result)
    }

    @Test
    fun `part 2 test data`() {
        val result = solvePartTwo(TEST_DATA)

        assertEquals(10, result)
    }

    @Test
    fun `part 2`() {
        val result = solvePartTwo(File("./inputs/day08.txt").readText())

        assertEquals(7037, result)
    }
}
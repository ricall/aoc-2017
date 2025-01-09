package org.ricall.dayxx

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private data class Action(val write: Int, val delta: Int, val nextState: String)
private data class State(val state: String, val zeroAction: Action, val oneAction: Action)

private val STATE_REGEX = """In state (\w+)""".toRegex()
private val WRITE_REGEX = """Write the value (\d+).""".toRegex()
private val MOVE_REGEX = """Move one slot to the (\w+).""".toRegex()
private val NEXT_STATE_REGEX = """Continue with state (\w+).""".toRegex()
private fun parseState(text: String): State {
    val lines = text.lines()

    val state = STATE_REGEX.find(lines[0])!!.groupValues[1]
    val actions = (0..1).map { index ->
        val write = WRITE_REGEX.find(lines[index * 4 + 2])!!.groupValues[1].toInt()
        val delta = when (MOVE_REGEX.find(lines[index * 4 + 3])!!.groupValues[1]) {
            "left" -> -1
            "right" -> 1
            else -> error("Invalid input: $text")
        }
        val nextState = NEXT_STATE_REGEX.find(lines[index * 4 + 4])!!.groupValues[1]
        Action(write, delta, nextState)
    }
    return State(state, actions[0], actions[1])
}

private fun machineFor(input: String): TurningMachine {
    val parts = input.split("\n\n")
    val steps = parts[0].split(" ").let { it[it.size - 2].toInt() }
    val states = (1..<parts.size).map { parts[it] }.map(::parseState)
    return TurningMachine("A", steps, states.associateBy { it.state })
}

private class TurningMachine(val initialState: String, val steps: Int, val states: Map<String, State>) {
    val tape = mutableMapOf<Int, Int>()
    var position = 0

    fun execute(): Int {
        var state = states[initialState] ?: error("Invalid state: $initialState")
        repeat(steps) {
            val currentValue = tape[position] ?: 0
            val action = when (currentValue) {
                0 -> state.zeroAction
                else -> state.oneAction
            }
            when (action.write) {
                0 -> tape.remove(position)
                else -> tape.put(position, 1)
            }
            position += action.delta
            state = states[action.nextState] ?: error("Invalid state: $action")
        }
        return tape.size
    }
}

private fun solvePartOne(input: String) = machineFor(input).execute()

class Day25 {
    private val TEST_DATA = """
        |Begin in state A.
        |Perform a diagnostic checksum after 6 steps.
        |
        |In state A:
        |  If the current value is 0:
        |    - Write the value 1.
        |    - Move one slot to the right.
        |    - Continue with state B.
        |  If the current value is 1:
        |    - Write the value 0.
        |    - Move one slot to the left.
        |    - Continue with state B.
        |
        |In state B:
        |  If the current value is 0:
        |    - Write the value 1.
        |    - Move one slot to the left.
        |    - Continue with state A.
        |  If the current value is 1:
        |    - Write the value 1.
        |    - Move one slot to the right.
        |    - Continue with state A.""".trimMargin()

    @Test
    fun `part 1 test data`() {
        val result = solvePartOne(TEST_DATA)

        assertEquals(3, result)
    }

    @Test
    fun `part 1`() {
        val result = solvePartOne(File("./inputs/day25.txt").readText())

        assertEquals(3362, result)
    }
}
package org.ricall.day09

import org.junit.jupiter.api.Test
import org.ricall.day09.State.*
import java.io.File
import kotlin.test.assertEquals

private enum class State {
    OTHER, OPEN, CLOSE, GARBAGE, IGNORE;

    fun transition(ch: Char) = transitions[this]!!.next(ch)

    companion object {
        fun interface NextState { fun next(ch: Char): State }
        private fun State.transition(vararg transitions: Pair<Char, State>): Pair<State, NextState> {
            val state = this
            val states = transitions.toMap()
            return state to NextState { ch -> states.getOrDefault(ch, state) }
        }
        val transitions = mapOf(
            OTHER.transition('{' to OPEN, '}' to CLOSE, '<' to GARBAGE),
            OPEN.transition('}' to CLOSE, '<' to GARBAGE),
            CLOSE.transition('{' to OPEN, '<' to GARBAGE, ',' to OTHER),
            GARBAGE.transition('!' to IGNORE, '>' to OTHER),
            IGNORE to NextState { GARBAGE }
        )
    }
}

private fun solve(input: String): Pair<Int, Int> {
    var (level, score, garbageCount) = Triple(0, 0, 0)
    input.fold(OTHER) { state, ch ->
        state.transition(ch).also {
            when (it) {
                OPEN -> level++
                CLOSE -> score += level--
                GARBAGE -> if (state == GARBAGE) {
                    garbageCount++
                }
                else -> {}
            }
        }
    }
    return score to garbageCount
}

private fun solvePartOne(input: String) = solve(input).first
private fun solvePartTwo(input: String) = solve(input).second

class Day09 {
    @Test
    fun `part 1 test data`() {
        assertEquals(1, solvePartOne("{}"))
        assertEquals(6, solvePartOne("{{{}}}"))
        assertEquals(5, solvePartOne("{{},{}}"))
        assertEquals(16, solvePartOne("{{{},{},{{}}}}"))
        assertEquals(1, solvePartOne("{<a>,<a>,<a>,<a>}"))
        assertEquals(9, solvePartOne("{{<ab>},{<ab>},{<ab>},{<ab>}}"))
        assertEquals(9, solvePartOne("{{<!!>},{<!!>},{<!!>},{<!!>}}"))
        assertEquals(3, solvePartOne("{{<a!>},{<a!>},{<a!>},{<ab>}}"))
    }

    @Test
    fun `part 1`() {
        val result = solvePartOne(File("./inputs/day09.txt").readText())

        assertEquals(14212, result)
    }

    @Test
    fun `part 2 test data`() {
        assertEquals(0, solvePartTwo("<>"))
        assertEquals(17, solvePartTwo("<random characters>"))
        assertEquals(3, solvePartTwo("<<<<>"))
        assertEquals(2, solvePartTwo("<{!>}>"))
        assertEquals(0, solvePartTwo("<!!>"))
        assertEquals(0, solvePartTwo("<!!!>>"))
        assertEquals(10, solvePartTwo("<{o\"i!a,<{i<a>"))
    }

    @Test
    fun `part 2`() {
        val result = solvePartTwo(File("./inputs/day09.txt").readText())

        assertEquals(6569, result)
    }
}
package org.ricall.day22

import org.junit.jupiter.api.Test
import org.ricall.day22.Direction.*
import org.ricall.day22.NodeStatus.*
import java.io.File
import kotlin.test.assertEquals

private data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Direction) = Point(x + other.delta.x, y + other.delta.y)
}
private enum class Direction(val delta: Point) {
    UP(Point(0, -1)),
    RIGHT(Point(1, 0)),
    DOWN(Point(0, 1)),
    LEFT(Point(-1, 0));

    private fun move(rotate: Int) = entries[(entries.indexOf(this) + rotate).mod(entries.size)]
    fun turnLeft() = move(-1)
    fun turnRight() = move(1)
}

private fun parseInput(input: String) = input.lines().let { lines ->
    Point(lines[0].length / 2, lines.size / 2) to buildList {
        lines.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                if (c == '#') {
                    add(Point(x, y))
                }
            }
        }
    }
}

private fun solvePartOne(input: String): Int {
    val (start, initial) = parseInput(input)
    var (current, direction) = start to UP
    val infections = initial.toMutableSet()

    var infectionCount = 0
    repeat(10_000) {
        if (infections.remove(current)) {
            direction = direction.turnRight()
        } else {
            infections.add(current)
            infectionCount++
            direction = direction.turnLeft()
        }
        current += direction
    }
    return infectionCount
}

private enum class NodeStatus { CLEAN, WEAKENED, INFECTED, FLAGGED }

private fun solvePartTwo(input: String): Int {
    val (start, infections) = parseInput(input)
    var (current, direction) = start to UP
    val nodeStatus = infections.associateWith { INFECTED }.toMutableMap()

    var infectionCount = 0
    repeat(10_000_000) {
        val status = nodeStatus.getOrDefault(current, CLEAN)
        when (status) {
            CLEAN -> {
                direction = direction.turnLeft()
                nodeStatus[current] = WEAKENED
            }
            WEAKENED -> {
                nodeStatus[current] = INFECTED
                infectionCount++
            }
            INFECTED -> {
                direction = direction.turnRight()
                nodeStatus[current] = FLAGGED
            }
            FLAGGED -> {
                direction = direction.turnLeft().turnLeft()
                nodeStatus.remove(current)
            }
        }
        current += direction
    }
    return infectionCount
}

class Day22 {
    private val TEST_DATA = """
        |..#
        |#..
        |...""".trimMargin()

    @Test
    fun `part 1 test data`() {
        val result = solvePartOne(TEST_DATA)

        assertEquals(5587, result)
    }

    @Test
    fun `part 1`() {
        val result = solvePartOne(File("./inputs/day22.txt").readText())

        assertEquals(5330, result)
    }

    @Test
    fun `part 2 test data`() {
        val result = solvePartTwo(TEST_DATA)

        assertEquals(2511944, result)
    }

    @Test
    fun `part 2`() {
        val result = solvePartTwo(File("./inputs/day22.txt").readText())

        assertEquals(2512103, result)
    }
}
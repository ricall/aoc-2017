package org.ricall.day14

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.streams.toList
import kotlin.test.assertEquals

private data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point) = Point(x + other.x, y + other.y)
}

private val DIRECTIONS = listOf(Point(-1, 0), Point(0, -1), Point(1, 0), Point(0, 1))

private fun MutableList<Int>.swap(index1: Int, index2: Int) {
    val temp = this[index2]
    this[index2] = this[index1]
    this[index1] = temp
}

private fun MutableList<Int>.reverse(start: Int, length: Int): Int {
    var front = start
    var end = (start + length - 1).mod(size)
    val nextPosition = (end + 1).mod(size)
    repeat((length + 1) / 2) {
        swap(front, end)
        front = (front + 1).mod(size)
        end = (end - 1).mod(size)
    }
    return nextPosition
}

private fun knotHash(lengths: List<Int>): List<Int> {
    var (start, skip) = 0 to 0
    val hash = (0..<256).toMutableList()
    repeat(64) { lengths.forEach { start = (hash.reverse(start, it) + skip++).mod(hash.size) } }
    return hash
}

private fun parseToLengthList(input: String) = input.chars().toList() + listOf(17, 31, 73, 47, 23)

private fun String.toKnotHash() = knotHash(parseToLengthList(this))
    .chunked(16).map { it.fold(0) { acc, value -> acc xor value } }
    .joinToString("") { it.toString(2).padStart(8, '0') }

private fun toDiskImage(input: String) = (0..127).map { "$input-$it".toKnotHash() }

private fun solvePartOne(input: String) = toDiskImage(input).sumOf { knot -> knot.count { it == '1' } }

private fun solvePartTwo(input: String): Int = toDiskImage(input).let { disk ->
    val visited = mutableSetOf<Point>()
    fun isNewGroup(point: Point) = !visited.contains(point)
            && (point.x in disk[0].indices)
            && (point.y in disk.indices)
            && disk[point.y][point.x] == '1'

    fun visitDiskLocation(point: Point): Int = when (isNewGroup(point)) {
        true -> {
            visited.add(point)
            DIRECTIONS.forEach { visitDiskLocation(point + it) }
            1
        }

        false -> 0
    }
    return disk.indices.sumOf { y -> disk[0].indices.sumOf { x -> visitDiskLocation(Point(x, y)) } }
}

class Day14 {
    @Test
    fun `part 1 test data`() {
        assertEquals("11010100", "flqrgnkx-0".toKnotHash().take(8))
        assertEquals("01010101", "flqrgnkx-1".toKnotHash().take(8))
        assertEquals(8108, solvePartOne("flqrgnkx"))
    }

    @Test
    fun `part 1`() {
        val result = solvePartOne(File("./inputs/day14.txt").readText())

        assertEquals(8214, result)
    }

    @Test
    fun `part 2 test data`() {
        assertEquals(1242, solvePartTwo("flqrgnkx"))
    }

    @Test
    fun `part 2`() {
        val result = solvePartTwo(File("./inputs/day14.txt").readText())

        assertEquals(1093, result)
    }
}
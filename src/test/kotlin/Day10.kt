package org.ricall.day10

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.streams.toList
import kotlin.test.assertEquals

private fun knotHash(size: Int, lengths: List<Int>, rounds: Int = 1): List<Int> {
    val list = (0..<size).toMutableList()
    var currentPosition = 0
    var skip = 0
    repeat(rounds) {
        lengths.forEach { length ->
            var end = (currentPosition + length - 1) % list.size
            val nextPosition = (end + 1 + skip++) % list.size
            repeat((length + 1) / 2) {
                val temp = list[end]
                list[end] = list[currentPosition]
                list[currentPosition] = temp

                currentPosition = (currentPosition + 1) % list.size
                end = when (end) {
                    0 -> list.size - 1
                    else -> end - 1
                }
            }
            currentPosition = nextPosition
        }
    }
    return list
}

private fun solvePartOne(size: Int, input: String) = knotHash(size, input.split(",").map(String::toInt))
    .take(2)
    .fold(1) { acc, value -> acc * value }

private fun parseToLengthList(input: String) = input.chars().toList() + listOf(17, 31, 73, 47, 23)

private fun solvePartTwo(input: String) = knotHash(256, parseToLengthList(input), 64)
    .chunked(16).map { it.fold(0) { acc, value -> acc xor value } }
    .joinToString("") { it.toString(16).padStart(2, '0') }

class Day10 {
    @Test
    fun `part 1 test data`() {
        assertEquals(12, solvePartOne(5, "3,4,1,5"))
    }

    @Test
    fun `part 1`() {
        val result = solvePartOne(256, File("./inputs/day10.txt").readText())

        assertEquals(11413, result)
    }

    @Test
    fun `part 2 test data`() {
        assertEquals(listOf(49, 44, 50, 44, 51, 17, 31, 73, 47, 23), parseToLengthList("1,2,3"))
        assertEquals("a2582a3a0e66e6e86e3812dcb672a272", solvePartTwo(""))
        assertEquals("33efeb34ea91902bb2f59c9920caa6cd", solvePartTwo("AoC 2017"))
        assertEquals("3efbe78a8d82f29979031a4aa0b16a9d", solvePartTwo("1,2,3"))
        assertEquals("63960835bcdc130f0b66d7ff4f6a5a8e", solvePartTwo("1,2,4"))
    }

    @Test
    fun `part 2`() {
        val result = solvePartTwo(File("./inputs/day10.txt").readText())

        assertEquals("7adfd64c2a03a4968cf708d1b7fd418d", result)
    }
}
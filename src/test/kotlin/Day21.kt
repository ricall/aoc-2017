package org.ricall.day21

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.math.sqrt
import kotlin.test.assertEquals

private fun List<Canvas>.joinToSingle() = this.chunked(sqrt(size.toFloat()).toInt())
    .map { it.reduce { a, b -> a.mergeHorizontal(b) } }
    .reduce { a, b -> a.mergeVertical(b) }

private data class Canvas(val grid: List<List<Char>>) {
    constructor(input: String) : this(input.split("/").map { it.toList() })

    private fun splitHorizontal(size: Int) = this.grid.chunked(size).map { Canvas(it) }
    private fun splitVertical(size: Int): List<Canvas> {
        val rows = this.grid.map { row -> row.chunked(size) }
        return (0..<(grid[0].size / size)).map { x ->
            (0..<size).map { y -> rows[y][x] }
        }.map { Canvas(it) }
    }

    fun partition(): List<Canvas> {
        val splitSize = if (grid.size % 2 == 0) 2 else 3
        val splits = grid.size / splitSize
        if (splits == 1) {
            return listOf(this)
        }
        return splitHorizontal(splitSize).flatMap { it.splitVertical(splitSize) }
    }

    fun mergeHorizontal(other: Canvas) = Canvas(grid.mapIndexed { index, row -> row + other.grid[index] })
    fun mergeVertical(other: Canvas) = Canvas(grid + other.grid)

    private fun rotate() = Canvas(grid.indices.map { y -> grid.indices.map { x -> grid[x][grid.size - 1 - y] } })
    private fun flip() = Canvas(grid.map { it.reversed() })
    fun combinations(): List<Canvas> {
        val rotations = (0..3).fold(listOf(this)) { rots, _ -> rots + rots.last().rotate() }
        val flips = rotations.map { it.flip() }
        return rotations + flips
    }

    override fun toString() = grid.joinToString("") { it.joinToString("") }
}

private fun parseTransform(line: String) = line.split(""" => """.toRegex()).map { Canvas(it) }.let { it[0] to it[1] }

private fun parseTransforms(input: String) = input.lines()
    .map(::parseTransform)
    .flatMap { (from, dest) -> from.combinations().map { it to dest } }
    .toMap()

private fun solve(input: String, iterations: Int): Int {
    val transforms = parseTransforms(input)
    var grid = Canvas(".#./..#/###")
    repeat(iterations) {
        val partitions = grid.partition()
        grid = partitions.map { transforms.getValue(it) }.joinToSingle()
    }
    return grid.toString().count { it == '#' }
}

private fun solvePartOne(input: String) = solve(input, 5)
private fun solvePartTwo(input: String) = solve(input, 18)

class Day21 {
    private val TEST_DATA = """
        |../.# => ##./#../...
        |.#./..#/### => #..#/..../..../#..#""".trimMargin()

    @Test
    fun `part 1 test data`() {
        val result = solve(TEST_DATA, 2)

        assertEquals(12, result)
    }

    @Test
    fun `part 1`() {
        val result = solvePartOne(File("./inputs/day21.txt").readText())

        assertEquals(203, result)
    }

    @Test
    fun `part 2`() {
        val result = solvePartTwo(File("./inputs/day21.txt").readText())

        assertEquals(3342470, result)
    }
}
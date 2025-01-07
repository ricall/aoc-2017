package org.ricall.day18

import org.junit.jupiter.api.Test
import org.ricall.day18.State.*
import java.io.File
import kotlin.test.assertEquals

private enum class State { RUNNING, HALTED }
private fun interface SendListener {
    fun onSend(value: Long)
}

private class Computer(input: String, programId: Int) {
    private val registers = mutableMapOf("p" to programId.toLong())
    private val instructions = input.lines()
    private var programCounter = 0
    var state = RUNNING

    private var listener: SendListener? = null
    var receiveQueue = mutableListOf<Long>()

    fun onSend(listener: SendListener): Computer {
        this.listener = listener
        return this
    }

    fun sendToReceiveQueue(value: Long) = receiveQueue.addLast(value)

    private fun valueFor(field: String): Long = field.toLongOrNull() ?: registers.getOrDefault(field, 0L)

    private fun execute(instruction: String): Unit = instruction.split(" ").iterator().run {
        when (next()) {
            "snd" -> listener?.onSend(valueFor(next()))
            "rcv" -> when (receiveQueue.isNotEmpty()) {
                true -> registers[next()] = receiveQueue.removeFirst()
                else -> {
                    state = HALTED; return
                }
            }

            "set" -> registers[next()] = valueFor(next())
            "add" -> next().let { registers[it] = valueFor(it) + valueFor(next()) }
            "mul" -> next().let { registers[it] = valueFor(it) * valueFor(next()) }
            "mod" -> next().let { registers[it] = valueFor(it).mod(valueFor(next())) }
            "jgz" -> if (valueFor(next()) > 0) programCounter += valueFor(next()).toInt() - 1
        }
        state = RUNNING
        programCounter++
    }

    fun step() = execute(instructions[programCounter])
}

private fun solvePartOne(input: String): Long {
    var sound = 0L
    Computer(input, 0).run {
        onSend({ sound = it })
        while (state == RUNNING) { step() }
    }
    return sound
}

private fun solvePartTwo(input: String): Long {
    val computers = listOf(Computer(input, 0), Computer(input, 1))
    var counter = 0L
    computers[0].onSend({ computers[1].sendToReceiveQueue(it) })
    computers[1].onSend({ counter++; computers[0].sendToReceiveQueue(it) })

    while (computers.any { it.state == RUNNING }) {
        computers.forEach(Computer::step)
    }
    return counter
}

class Day18 {
    @Test
    fun `part 1 test data`() {
        val result = solvePartOne(
            """
            |set a 1
            |add a 2
            |mul a a
            |mod a 5
            |snd a
            |set a 0
            |rcv a
            |jgz a -1
            |set a 1
            |jgz a -2""".trimMargin()
        )

        assertEquals(4, result)
    }

    @Test
    fun `part 1`() {
        val result = solvePartOne(File("./inputs/day18.txt").readText())

        assertEquals(1187, result)
    }

    @Test
    fun `part 2 test data`() {
        val result = solvePartTwo(
            """
            |snd 1
            |snd 2
            |snd p
            |rcv a
            |rcv b
            |rcv c
            |rcv d""".trimMargin()
        )

        assertEquals(3, result)
    }

    @Test
    fun `part 2`() {
        val result = solvePartTwo(File("./inputs/day18.txt").readText())

        assertEquals(5969, result)
    }
}
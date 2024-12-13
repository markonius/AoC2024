package markonius.aoc2024

import kotlin.math.sin
import kotlin.math.sqrt

class Machine(rules: List<String>) {
	val a: Vector2
	val b: Vector2
	val prize: Vector2

	init {
		val numberRegex = """\d+""".toRegex()

		numberRegex.findAll(rules[0]).toList()
			.let { a = Vector2(it[0].value.toInt(), it[1].value.toInt()) }

		numberRegex.findAll(rules[1]).toList()
			.let { b = Vector2(it[0].value.toInt(), it[1].value.toInt()) }

		numberRegex.findAll(rules[2]).toList()
			.let { prize = Vector2(it[0].value.toInt(), it[1].value.toInt()) }
	}

	override fun toString(): String {
		return "a = $a\nb = $b\nprize = $prize"
	}
}

class Machine2(rules: List<String>) {
	val a: Vector2L
	val b: Vector2L
	val prize: Vector2L

	init {
		val numberRegex = """\d+""".toRegex()

		numberRegex.findAll(rules[0]).toList()
			.let { a = Vector2L(it[0].value.toLong(), it[1].value.toLong()) }

		numberRegex.findAll(rules[1]).toList()
			.let { b = Vector2L(it[0].value.toLong(), it[1].value.toLong()) }

		numberRegex.findAll(rules[2]).toList()
			.let { prize = Vector2L(it[0].value.toLong() + 10000000000000L, it[1].value.toLong() + 10000000000000L) }
	}

	override fun toString(): String {
		return "a = $a\nb = $b\nprize = $prize"
	}
}

object Day13 {
	val name = "Claw Contraption"

	fun one(): String {
		val lines = getInput(13).lines()
		val machinesRules = lines
			.filter { it.isNotEmpty() }
			.withIndex()
			.groupBy(
				{ (i, _) -> i / 3 },
				{ (_, v) -> v }
			).values
		val machines = machinesRules.map { Machine(it) }

		val result = machines
			.map { m ->
				val aAngle = m.prize.angle - m.a.angle
				val bAngle = m.prize.angle - m.b.angle

				if (aAngle * bAngle < 0.0) {
					val leftVector: Vector2
					val leftCost: Int
					val rightVector: Vector2
					val rightCost: Int

					if (aAngle < bAngle) {
						leftVector = m.a
						leftCost = 3
						rightVector = m.b
						rightCost = 1
					} else {
						leftVector = m.b
						leftCost = 1
						rightVector = m.a
						rightCost = 3
					}

					var path = Vector2(0, 0)
					var cost = 0
					val prizeAngle = m.prize.angle
					while (path.x < m.prize.x && path.y < m.prize.y) {
						if (path.angle < prizeAngle) {
							path += leftVector
							cost += leftCost
						} else {
							path += rightVector
							cost += rightCost
						}
					}

					if (path == m.prize)
						cost
					else
						0
				} else {
					0
				}
			}.sum()

		return result.toString()
	}

	fun two(): String {
		val lines = getInput(13).lines()
//		val lines = testInput.lines()
		val machinesRules = lines
			.filter { it.isNotEmpty() }
			.withIndex()
			.groupBy(
				{ (i, _) -> i / 3 },
				{ (_, v) -> v }
			).values
		val machines = machinesRules.map { Machine2(it) }

		var successCount = 0
		var missCount = 0
		var badAngleCount = 0

		val result = machines
			.map { m ->
				val diagonalAngle = Math.PI / 4
				val aAngle = diagonalAngle - m.a.angle
				val bAngle = diagonalAngle - m.b.angle

				if (aAngle * bAngle < 0.0) {
					val leftVector: Vector2L
					val leftCost: Long
					val rightVector: Vector2L
					val rightCost: Long

					if (m.a.angle > m.b.angle) {
						leftVector = m.a
						leftCost = 3
						rightVector = m.b
						rightCost = 1
					} else {
						leftVector = m.b
						leftCost = 1
						rightVector = m.a
						rightCost = 3
					}

					// Sneaky bug! I needed to reduce this distance by about 100k.
					// That way, we don't reach all the way to the farlands.
					// This means that some of the prizes that weren't reachable in one are now reachable!
					val farlandsDistance = 14142135523730L

					val angleA = diagonalAngle - rightVector.angle
					val angleB = leftVector.angle - diagonalAngle
					val angleC = Math.PI - angleA - angleB
					val ratioLeft = sin(angleA) / sin(angleC)
					val ratioRight = sin(angleB) / sin(angleC)

					val rightToFarlands = (ratioRight * farlandsDistance / rightVector.length).toLong()
					val leftToFarlands = (ratioLeft * farlandsDistance / leftVector.length).toLong()

					val farlandsPath = rightVector * rightToFarlands + leftVector * leftToFarlands
					val farlandsCost = rightCost * rightToFarlands + leftCost * leftToFarlands

					val remainingPrizeVector = m.prize - farlandsPath
					val prizeAngle = remainingPrizeVector.angle

					var path = Vector2L(0L, 0L)
					var cost = 0L
					while (path.x < remainingPrizeVector.x && path.y < remainingPrizeVector.y) {
						if (path.angle < prizeAngle) {
							path += leftVector
							cost += leftCost
						} else {
							path += rightVector
							cost += rightCost
						}
					}

					if (path == remainingPrizeVector) {
						successCount++
						farlandsCost + cost
					} else {
						missCount++
						0
					}
				} else {
					badAngleCount++
					0
				}
			}.sum()

		println("Success: $successCount, Miss: $missCount, Bad angle: $badAngleCount")

		return result.toString()
	}

	val testInput =
		"""
		Button A: X+94, Y+34
		Button B: X+22, Y+67
		Prize: X=8400, Y=5400

		Button A: X+26, Y+66
		Button B: X+67, Y+21
		Prize: X=12748, Y=12176

		Button A: X+17, Y+86
		Button B: X+84, Y+37
		Prize: X=7870, Y=6450

		Button A: X+69, Y+23
		Button B: X+27, Y+71
		Prize: X=18641, Y=10279
		""".trimIndent()
}

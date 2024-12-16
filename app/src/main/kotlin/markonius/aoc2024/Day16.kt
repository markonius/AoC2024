package markonius.aoc2024

import kotlin.math.min

object Day16 {
	val name = "Reindeer Maze"

	fun one(): Long {
		val board = Board(16)
//		val board = Board(testInput)
		val start = Vector2(1, 1)
		val end = Vector2(board.width - 2, board.height - 2)

		val tileScores = HashMap<Vector2, Long>()

		fun explore(from: Vector2, forward: Vector2, currentScore: Long): Long {
			if (from == end)
				return currentScore

			if (board[from] == '#')
				return Long.MAX_VALUE

			val tileScore = tileScores.computeIfAbsent(from) { _ -> Long.MAX_VALUE }
			if (tileScore < currentScore)
				return Long.MAX_VALUE

			tileScores[from] = currentScore

			val right = forward.rotateClockwise()
			val left = forward.rotateCounterClockwise()

			val forwardScore = explore(from + forward, forward, currentScore + 1)
			val rightScore = explore(from + right, right, currentScore + 1001)
			val leftScore = explore(from + left, left, currentScore + 1001)

			return minOf(forwardScore, rightScore, leftScore)
		}

		val score = explore(start, Vector2.right, 0L)
//		println(board)
		return score
	}

	class Step(val position: Vector2, val previous: Step?)

	fun two(): Long {
		val board = Board(16)
//		val board = Board(testInput)
		val start = Vector2(1, 1)
		val end = Vector2(board.width - 2, board.height - 2)

		val tileScores = HashMap<Vector2, Long>()

		fun explore(from: Step, forward: Vector2, currentScore: Long): Pair<List<Step>, Long> {
			if (from.position == end)
				return listOf(from) to currentScore

			if (board[from.position] == '#')
				return listOf(from) to Long.MAX_VALUE

			val tileScore = tileScores.computeIfAbsent(from.position) { _ -> Long.MAX_VALUE }
			if (tileScore < currentScore)
				return listOf(from) to Long.MAX_VALUE

			tileScores[from.position] = currentScore

			val right = forward.rotateClockwise()
			val left = forward.rotateCounterClockwise()

			val forwardScore = explore(Step(from.position + forward, from), forward, currentScore + 1)
			val rightScore = explore(Step(from.position + right, from), right, currentScore + 1001)
			val leftScore = explore(Step(from.position + left, from), left, currentScore + 1001)

			val nextScores = listOf(
				forwardScore,
				rightScore,
				leftScore
			)

			val minScore = nextScores.minOf { it.second }

			// Stupid hack that seems to work >:(
			if (rightScore.second == minScore || leftScore.second == minScore)
				tileScores[from.position] = currentScore + 1000

			val minSteps = nextScores.filter { it.second == minScore }.flatMap { it.first }
			return minSteps to minScore
		}


		var score: Pair<List<Step>, Long>? = null
		// We need a bigger stack :D
		val thread = Thread(
			null as ThreadGroup?,
			{ score = explore(Step(start, null), Vector2.right, 0L) },
			"bla",
			1024L * 1024L * 1024L
		)
		thread.start()
		thread.join()

		val goodPositions = score!!.first
			.flatMap { step ->
				sequence {
					var current: Step? = step
					while (current != null) {
						yield(current.position)
						current = current.previous
					}
				}
			}.toSet()
//		println(board)
		return goodPositions.size.toLong()
	}

	val testInput =
		"""
			#################
			#...#...#...#..E#
			#.#.#.#.#.#.#.#.#
			#.#.#.#...#...#.#
			#.#.#.#.###.#.#.#
			#...#.#.#.....#.#
			#.#.#.#.#.#####.#
			#.#...#.#.#.....#
			#.#.#####.#.###.#
			#.#.#.......#...#
			#.#.###.#####.###
			#.#.#...#.....#.#
			#.#.#.#####.###.#
			#.#.#.........#.#
			#.#.#.#########.#
			#S#.............#
			#################
		""".trimIndent()

}

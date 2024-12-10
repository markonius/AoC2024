object Day10 {
	val name = "Hoof It"

	fun one(): String {
		val board = Board(10)

		val trailHeads = board
			.filter { (_, c) -> c == '0' }
			.map { (v, _) -> v }

		fun reachablePeaks(from: Vector2, atHeight: Int): Set<Vector2> {
			val char = board[from]
			if (char == board.defaultChar)
				return emptySet()

			val height = char.digitToInt()
			if (height != atHeight + 1)
				return emptySet()

			if (height == 9)
				return setOf(from)

			return reachablePeaks(Vector2(from.x, from.y + 1), height) union
				reachablePeaks(Vector2(from.x + 1, from.y), height) union
				reachablePeaks(Vector2(from.x, from.y - 1), height) union
				reachablePeaks(Vector2(from.x - 1, from.y), height)
		}

		val result = trailHeads
			.map { h ->
				val peaks = reachablePeaks(h, -1)
				peaks.size
			}.sum()

		return result.toString()
	}

	fun two(): String {
		val board = Board(10)

		val trailHeads = board
			.filter { (_, c) -> c == '0' }
			.map { (v, _) -> v }

		fun paths(from: Vector2, atHeight: Int): Long {
			val char = board[from]
			if (char == board.defaultChar)
				return 0

			val height = char.digitToInt()
			if (height != atHeight + 1)
				return 0

			if (height == 9)
				return 1

			return paths(Vector2(from.x, from.y + 1), height) +
				paths(Vector2(from.x + 1, from.y), height) +
				paths(Vector2(from.x, from.y - 1), height) +
				paths(Vector2(from.x - 1, from.y), height)
		}

		val result = trailHeads
			.map { h -> paths(h, -1) }
			.sum()

		return result.toString()
	}
}

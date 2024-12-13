package markonius.aoc2024

object Day6 {
	val name = "Guard Gallivant"

	fun one(): String {
		val board = Board(6)
		var position = Vector2(0, 0)
		var direction = Vector2.up
		var steps = 1

		outer@for (j in 0..<board.height) {
			for (i in 0..<board.width) {
				if (board[i, j] == '^') {
					position = Vector2(i, j)
					break@outer
				}
			}
		}

		board[position] = 'X'

		fun step() {
			if (board[position + direction] != '#') {
				position += direction
				if (board[position] == '.') {
					board[position] = 'X'
					steps++
				}
			} else {
				direction = direction.rotateClockwise()
			}
		}

		while (board.isInBounds(position)) {
			step()
		}

		return steps.toString()
	}

	fun two(): String {
		fun traverse(board: Board): Boolean {
			var position = Vector2(0, 0)
			var direction = Vector2.up
			var steps = 1

			for (j in 0 ..< board.height) {
				for (i in 0 ..< board.width) {
					if (board[i, j] == '^') position = Vector2(i, j)
				}
			}

			board[position] = '0'

			fun directionToChar(direction: Vector2) =
					when (direction) {
						Vector2.up -> '^'
						Vector2.right -> '>'
						Vector2.down -> 'v'
						Vector2.left -> '<'
						else -> throw Exception()
					}

			fun charToDirection(char: Char) =
					when (char) {
						'^' -> Vector2.up
						'>' -> Vector2.right
						'v' -> Vector2.down
						'<' -> Vector2.left
						else -> throw Exception()
					}

			fun step(): Boolean {
				if (board[position + direction] != '#') {
					position += direction
					if (board[position] == directionToChar(direction)) {
						return false
					}
					if (board[position] == '.') {
						board[position] = directionToChar(direction)
						steps++
					}
				} else {
					direction = direction.rotateClockwise()
				}
				return true
			}

			var isLoop = false
			while (board.isInBounds(position)) {
				if (!step()) {
					isLoop = true
					break
				}
			}

			return isLoop
		}

		fun tryObstacle(position: Vector2): Boolean {
			val board = Board(6)
			board[position] = '#'
			return traverse(board)
		}

		val initialBoard = Board(6)
		traverse(initialBoard)

		var loops = 0

		val directions = listOf('^', '>', 'v', '<')
		for (j in 0 ..< initialBoard.height) {
			for (i in 0 ..< initialBoard.width) {
				if (directions.contains(initialBoard[i, j])) {
					if (tryObstacle(Vector2(i, j)))
						loops++
				}
			}
		}

		return loops.toString()
	}
}

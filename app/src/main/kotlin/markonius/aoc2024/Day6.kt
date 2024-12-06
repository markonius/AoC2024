import java.io.File

object Day6 {
	val name = "Guard Gallivant"

	fun one(): String {
		val board = Board(6)
		var position = Vector2(0, 0)
		var direction = Vector2.up
		var steps = 1

		for (j in 0..<board.height) {
			for (i in 0..<board.width) {
				if (board[i, j] == '^')
					position = Vector2(i, j)
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
}

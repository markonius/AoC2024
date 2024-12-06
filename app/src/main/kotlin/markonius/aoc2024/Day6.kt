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

		board[position] = '^'

		fun directionToChar(direction: Vector2) = when(direction) {
			Vector2.up -> '^'
			Vector2.right -> '>'
			Vector2.down -> 'v'
			Vector2.left -> '<'
			else -> throw Exception()
		}

		fun charToDirection(char: Char) = when(char) {
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

		while (
			board.isInBounds(position)
			&& step()
		) {
		}

		val outputFile = File("output.txt")
		outputFile.writeText("")
		for (j in 0 .. board.height) {
			for (i in 0 .. board.width) {
				outputFile.appendText(board[i, board.height - j].toString())
			}
			outputFile.appendText("\n")
		}

		return steps.toString()
	}
}

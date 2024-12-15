package markonius.aoc2024

object Day15 {
	val name = "Warehouse Woes"

	fun parseInstructions(string: String) =
		string
			.filter { it != '\n' }
			.map {
				when (it) {
					'^' -> Vector2.up
					'>' -> Vector2.right
					'v' -> Vector2.down
					'<' -> Vector2.left
					else -> PANIC
				}
			}

	fun one(): String {
		val input = getInput(15)
//		val input = testInput
		val inputParts = input.split("\n\n")
		val board = Board(inputParts[0])
		val instructions = parseInstructions(inputParts[1])

		var robotPosition = board.first { (_, c) -> c == '@' }.first

		fun tryMove(position: Vector2, direction: Vector2): Vector2 {
			var next = board[position + direction]

			if (next == '#')
				return Vector2.zero

			if (next == 'O')
				tryMove(position + direction, direction)

			next = board[position + direction]
			if (next == '.') {
				board[position + direction] = board[position]
				board[position] = '.'
				return direction
			}

			return Vector2.zero
		}

		for (instruction in instructions) {
			robotPosition += tryMove(robotPosition, instruction)
		}

		val result = board
			.sumOf { (pos, c) ->
				if (c == 'O')
					pos.x + (100 * (board.height - 1 - pos.y))
				else
					0
			}

		return result.toString()
	}

	fun two(): String {
		fun enthiccenBoard(input: String): String =
			input
				.map {
					when (it) {
						'.' -> ".."
						'#' -> "##"
						'O' -> "[]"
						'@' -> "@."
						else -> it.toString()
					}
				}.joinToString("") { it }

		val input = getInput(15)
//		val input = testInput
		val inputParts = input.split("\n\n")
		val board = Board(enthiccenBoard(inputParts[0]))
		val instructions = parseInstructions(inputParts[1])

		var robotPosition = board.first { (_, c) -> c == '@' }.first

		fun canMove(position: Vector2, direction: Vector2): Boolean {
			val next = board[position + direction]
			return when (next) {
				'#' -> false
				'.' -> true
				'[' -> {
					canMove(position + direction, direction)
						&& (direction == Vector2.left || canMove(position + direction + Vector2.right, direction))
				}
				']' -> {
					canMove(position + direction, direction)
						&& (direction == Vector2.right || canMove(position + direction + Vector2.left, direction))
				}
				else -> PANIC
			}
		}

		fun move(position: Vector2, direction: Vector2) {
			val next = board[position + direction]
			when (next) {
				'#' -> return
				'.' -> { }
				'[' -> {
					if (direction.y != 0)
						move(position + direction + Vector2.right, direction)
					move(position + direction, direction)
				}
				']' -> {
					if (direction.y != 0)
						move(position + direction + Vector2.left, direction)
					move(position + direction, direction)
				}
				else -> PANIC
			}
			board[position + direction] = board[position]
			board[position] = '.'
		}

		for (instruction in instructions) {
			if (canMove(robotPosition, instruction)) {
				move(robotPosition, instruction)
				robotPosition += instruction
			}
		}

		val result = board
			.sumOf { (pos, c) ->
				if (c == '[')
					pos.x + (100 * (board.height - 1 - pos.y))
				else
					0
			}

		return result.toString()
	}

	val testInput =
		"""
			##########
			#..O..O.O#
			#......O.#
			#.OO..O.O#
			#..O@..O.#
			#O#..O...#
			#O..O..O.#
			#.OO.O.OO#
			#....O...#
			##########

			<vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
			vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
			><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
			<<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
			^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
			^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
			>^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
			<><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
			^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
			v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
		""".trimIndent()
}

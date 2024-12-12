object Day12 {
	val name = "Garden Groups"

	fun one(): String {
		val board = Board(12)

		fun calculateCost(position: Vector2): Int {
			val plots = board.floodFill(position).toList()
			val char = board[position]

			var area = 0
			var perimeter = 0
			for (plot in plots) {
				area++
				if (board[plot + Vector2.up] != char)
					perimeter++
				if (board[plot + Vector2.right] != char)
					perimeter++
				if (board[plot + Vector2.down] != char)
					perimeter++
				if (board[plot + Vector2.left] != char)
					perimeter++
			}

			for (plot in plots) {
				board[plot] = ' '
			}

			// println("char: $char, area: $area, perimeter: $perimeter, position: $position")

			return area * perimeter
		}

		val result = board.map { (position, _) ->
			val realChar = board[position]
			if (realChar != ' ') {
				calculateCost(position)
			} else {
				0
			}
		}.sum()

		return result.toString()
	}

	fun two(): String {
		val board = Board(12)
		// val board = Board(testInput)

		fun calculateCost(position: Vector2): Int {
			val plots = board.floodFill(position).toList()
			val char = board[position]

			val fenceParts = HashSet<Pair<Vector2, Vector2>>()
			var fenceCount = 0

			fun addFencePart(position: Vector2, direction: Vector2) {
				if (board[position + direction] != char) {
					// println(fenceParts)
					if (direction == Vector2.up || direction == Vector2.down) {
						if (!fenceParts.contains(Pair(position + Vector2.left, direction))
							&& !fenceParts.contains(Pair(position + Vector2.right, direction)))
							fenceCount++
						else if (fenceParts.contains(Pair(position + Vector2.left, direction))
							&& fenceParts.contains(Pair(position + Vector2.right, direction)))
							fenceCount--
					} else {
						if (!fenceParts.contains(Pair(position + Vector2.down, direction))
							&& !fenceParts.contains(Pair(position + Vector2.up, direction)))
							fenceCount++
						if (fenceParts.contains(Pair(position + Vector2.down, direction))
							&& fenceParts.contains(Pair(position + Vector2.up, direction)))
							fenceCount--
					}
					fenceParts.add(position to direction)
				}
			}

			var area = 0
			for (plot in plots) {
				area++
				addFencePart(plot, Vector2.up)
				addFencePart(plot, Vector2.right)
				addFencePart(plot, Vector2.down)
				addFencePart(plot, Vector2.left)
			}

			for (plot in plots) {
				board[plot] = ' '
			}

			// println(fenceParts.joinToString("\n") { it.toString() })
			// println(board)
			// println("char: $char, area: $area, fences: $fenceCount, position: $position")

			return area * fenceCount
		}

		val result = board.map { (position, _) ->
			val realChar = board[position]
			if (realChar != ' ') {
				calculateCost(position)
			} else {
				0
			}
		}.sum()

		return result.toString()
	}

	val testInput =
"""
RRRRIICCFF
RRRRIICCCF
VVRRRCCFFF
VVRCCCJFFF
VVVVCJJCFE
VVIVCCJJEE
VVIIICJJEE
MIIIIIJJEE
MIIISIJEEE
MMMISSJEEE
""".trim()
}

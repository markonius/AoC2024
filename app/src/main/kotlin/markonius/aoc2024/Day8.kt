package markonius.aoc2024

object Day8 {
	val name = "Resonant Collinearity"

	fun one(): String {
		val board = Board(8)
		val antinodes = HashSet<Vector2>()
		val antennasByFrequency = HashMap<Char, ArrayList<Vector2>>()

		for (y in 0 ..< board.height) {
			for (x in 0 ..< board.width) {
				val value = board[x, y]
				if (value != '.') {
					val antennas = antennasByFrequency.computeIfAbsent(value, { _ -> ArrayList() })
					antennas.add(Vector2(x, y))
				}
			}
		}

		for (entry in antennasByFrequency.entries) {
			for (antenna in entry.value) {
				for (other in entry.value) {
					if (antenna != other) {
						val node1 = other + other - antenna
						if (node1.x >= 0 && node1.x < board.width && node1.y >= 0 && node1.y < board.height)
							antinodes.add(node1)
						val node2 = antenna + antenna - other
						if (node2.x >= 0 && node2.x < board.width && node2.y >= 0 && node2.y < board.height)
							antinodes.add(node2)
					}
				}
			}
		}

		return antinodes.size.toString()
	}

	fun two(): String {
		val board = Board(8)
		val antinodes = HashSet<Vector2>()
		val antennasByFrequency = HashMap<Char, ArrayList<Vector2>>()

		for (y in 0 ..< board.height) {
			for (x in 0 ..< board.width) {
				val value = board[x, y]
				if (value != '.') {
					val antennas = antennasByFrequency.computeIfAbsent(value, { _ -> ArrayList() })
					antennas.add(Vector2(x, y))
				}
			}
		}

		fun traverse(start: Vector2, direction: Vector2) {
			var position = start
			while (
				position.x >= 0 && position.x < board.width
				&& position.y >= 0 && position.y < board.height
			) {
				antinodes.add(position)
				position += direction
			}
		}

		for (entry in antennasByFrequency.entries) {
			for (antenna in entry.value) {
				for (other in entry.value) {
					if (antenna != other) {
						var direction = other - antenna
						val gcd = greatestCommonDivisor(direction.x, direction.y)
						direction /= gcd
						traverse(antenna, direction)
						traverse(antenna, -direction)
					}
				}
			}
		}

		return antinodes.size.toString()
	}

	val testInput =
"""
............
........0...
.....0......
.......0....
....0.......
......A.....
............
............
........A...
.........A..
............
............
""".trim()
}

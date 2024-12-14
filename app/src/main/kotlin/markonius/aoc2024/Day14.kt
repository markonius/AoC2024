package markonius.aoc2024

class Robot(var position: Vector2, val velocity: Vector2)

fun parseRobot(description: String): Robot {
	val numbers = readAllInts(description)
	return Robot(Vector2(numbers[0], numbers[1]), Vector2(numbers[2], numbers[3]))
}

object Day14 {
	val name = "Restroom Redoubt"

	fun one(): String {
		val lines = getInput(14).lines()
//		val lines = testInput.lines()
		val robots = lines.map { parseRobot(it) }
		val width = 101
//		val width = 11
		val height = 103
//		val height = 7

		for (robot in robots) {
			robot.position += robot.velocity * 100
			robot.position = Vector2(robot.position.x.mod(width), robot.position.y.mod(height))
		}

		val robotsPerQuadrant = robots
			.map { r ->
				var quadrantId = 0
				if (r.position.x == width / 2
					|| r.position.y == height / 2
				) {
					quadrantId = -1
				} else {
					if (r.position.x > width / 2)
						quadrantId += 0b1
					if (r.position.y > height / 2)
						quadrantId += 0b10
				}
				quadrantId
			}.groupBy { it }

		val result = robotsPerQuadrant.entries
			.filter { it.key >= 0 }
			.map { it.value.size }
			.reduce { a, b -> a * b }

		return result.toString()
	}

	fun two(): String {
		val lines = getInput(14).lines()
		val robots = lines.map { parseRobot(it) }
		val width = 101
		val height = 103

		// I hate this problem. Very under-defined.
		fun detectRectangle(): Boolean {
			val positionSet = robots.map { it.position }.toSet()
			for (robot in robots) {
				var positionsInRect = 0
				var currentPos = robot.position
				var step = 0
				var direction = Vector2.right
				var nextDirection = direction.rotateClockwise()
				while (step < 4) {
					if (currentPos + direction in positionSet) {
						currentPos = currentPos + direction
						positionsInRect++
						if (currentPos == robot.position && positionsInRect > 50) {
							return true
						}
					} else if (currentPos + nextDirection in positionSet) {
						direction = nextDirection
						nextDirection = direction.rotateClockwise()
						step++
						positionsInRect++
					} else {
						break
					}
				}
			}
			return false
		}

		var step = 0
		while (true) {
			for (robot in robots) {
				robot.position += robot.velocity
				robot.position = Vector2(robot.position.x.mod(width), robot.position.y.mod(height))
			}
			step++
			if (detectRectangle())
				break
		}

		return step.toString()
	}

	val testInput =
		"""
		p=0,4 v=3,-3
		p=6,3 v=-1,-3
		p=10,3 v=-1,2
		p=2,0 v=2,-1
		p=0,0 v=1,3
		p=3,0 v=-2,-2
		p=7,6 v=-1,-3
		p=3,0 v=-1,-2
		p=9,3 v=2,3
		p=7,3 v=-1,2
		p=2,4 v=2,-3
		p=9,5 v=-3,-3
		""".trimIndent()
}

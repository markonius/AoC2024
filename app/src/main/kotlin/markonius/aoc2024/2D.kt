class Board(day: Int, defaultChar: Char = ' ') {
	val lines: List<MutableList<Char>>
	val defaultChar = defaultChar
	val width: Int
	val height: Int

	init {
		lines = getInput(day).lines().map { l -> l.toMutableList() }.reversed()
		width = lines[0].size
		height = lines.size
	}

	operator fun get(x: Int, y: Int): Char =
		if (isInBounds(x, y))
			lines[y][x]
		else
			defaultChar

	operator fun get(position: Vector2): Char = get(position.x, position.y)

	operator fun set(x: Int, y: Int, value: Char): Boolean =
		if (isInBounds(x, y)) {
			lines[y][x] = value
			true
		} else {
			false
		}

	operator fun set(position: Vector2, value: Char): Boolean = set(position.x, position.y, value)

	fun isInBounds(x: Int, y: Int): Boolean = x >= 0 && x < width && y >= 0 && y < height

	fun isInBounds(position: Vector2): Boolean = isInBounds(position.x, position.y)
}

class Vector2(val x: Int, val y: Int) {
	operator fun plus(other: Vector2): Vector2 =
		Vector2(x + other.x, y + other.y)

	override fun equals(other: Any?): Boolean =
		other is Vector2 && other.x == x && other.y == y

	fun rotateClockwise(): Vector2 = Vector2(y, -x)

	companion object {
		val up = Vector2(0, 1)
		val right = Vector2(1, 0)
		val down = Vector2(0, -1)
		val left = Vector2(-1, 0)
	}
}

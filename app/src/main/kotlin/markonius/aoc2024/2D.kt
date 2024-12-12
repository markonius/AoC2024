class Board(input: String, defaultChar: Char = ' ')
	: Iterable<Pair<Vector2, Char>> {

	val lines: List<MutableList<Char>>
	val defaultChar = defaultChar
	val width: Int
	val height: Int

	constructor(day: Int, defaultChar: Char = ' ') : this(getInput(day), defaultChar)

	init {
		lines = input.lines().map { l -> l.toMutableList() }.reversed()
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

	fun floodFill(position: Vector2): Sequence<Vector2> = floodFillStep(position, HashSet())

	private fun floodFillStep(position: Vector2, visited: HashSet<Vector2>): Sequence<Vector2> {
		if (visited.contains(position))
			return emptySequence()

		val self = this
		visited.add(position)
		return sequence {
			yield(position)
			val char = self[position]
			if (self[position + Vector2.up] == char)
				yieldAll(floodFillStep(position + Vector2.up, visited))
			if (self[position + Vector2.right] == char)
				yieldAll(floodFillStep(position + Vector2.right, visited))
			if (self[position + Vector2.down] == char)
				yieldAll(floodFillStep(position + Vector2.down, visited))
			if (self[position + Vector2.left] == char)
				yieldAll(floodFillStep(position + Vector2.left, visited))
		}
	}

	override fun toString(): String {
		return lines.reversed().map { l ->
			String(l.toCharArray())
		}.joinToString("\n")
	}

 	override fun iterator(): Iterator<Pair<Vector2, Char>> {
		val self = this
		return sequence {
			for (y in 0..<height) {
				for (x in 0..<width) {
					yield(Vector2(x, y) to self[x, y])
				}
			}
		}.iterator()
	}
}

data class Vector2(val x: Int, val y: Int) {
	operator fun plus(other: Vector2): Vector2 =
		Vector2(x + other.x, y + other.y)

	operator fun minus(other: Vector2): Vector2 =
		Vector2(x - other.x, y - other.y)

	operator fun times(n: Int) =
		Vector2(x * n, y * n)

	operator fun div(n: Int) =
		Vector2(x / n, y / n)

	operator fun unaryMinus() =
		Vector2(-x, -y)

	fun rotateClockwise(): Vector2 = Vector2(y, -x)

	companion object {
		val up = Vector2(0, 1)
		val right = Vector2(1, 0)
		val down = Vector2(0, -1)
		val left = Vector2(-1, 0)
	}
}

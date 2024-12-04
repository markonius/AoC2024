object Day4 {
   val name = "Ceres Search"

   fun one(): String {
      val lines = getInput(4).lines()
      // val lines = testInput2.lines()
		val width = lines[0].length
		val height = lines.size

		fun isChar(char: Char, x: Int, y: Int): Boolean =
			x >= 0 && x < width && y >= 0 && y < height && lines[y][x] == char

      fun countXmas(x: Int, y: Int): Int {
			var count = 0
			if (isChar('X', x, y)) {
				for (j in -1..1) {
					for (i in -1..1) {
						if ((i != 0 || j != 0)
							&& isChar('M', x + i, y + j)
							&& isChar('A', x + i * 2, y + j * 2)
							&& isChar('S', x + i * 3, y + j * 3)
						) {
							// println("$x, $y, $i, $j")
							count++
						}
					}
				}
			}
			return count
		}

      var count = 0
      for (y in 0 ..< height) {
         for (x in 0 ..< width) {
				count += countXmas(x, y)
         }
      }

		return count.toString()
   }

	fun two(): String {
      val lines = getInput(4).lines()
      // val lines = testInput.lines()
		val width = lines[0].length
		val height = lines.size

		fun isChar(char: Char, x: Int, y: Int): Boolean =
			x >= 0 && x < width && y >= 0 && y < height && lines[y][x] == char

      fun isXmas(x: Int, y: Int): Boolean {
			var count = 0
			if (isChar('A', x, y)) {
				for (j in arrayOf(-1, 1)) {
					for (i in arrayOf(-1, 1)) {
							if(isChar('M', x + i, y + j)
							&& isChar('S', x - i, y - j)
						) {
							// println("$x, $y, $i, $j")
							count++
						}
					}
				}
			}
			return count == 2
		}

      var count = 0
      for (y in 0 ..< height) {
         for (x in 0 ..< width) {
				if (isXmas(x, y))
					count++
         }
      }

		return count.toString()
	}

	val testInput = """
MMMSXXMASM
MSAMXMSMSA
AMXSXMAAMM
MSAMASMSMX
XMASAMXAMM
XXAMMXXAMA
SMSMSASXSS
SAXAMASAAA
MAMMMXMMMM
MXMXAXMASX
""".trim()

val testInput2 = """
....XXMAS.
.SAMXMS...
...S..A...
..A.A.MS.X
XMASAMX.MM
X.....XA.A
S.S.S.S.SS
.A.A.A.A.A
..M.M.M.MM
.X.X.XMASX
""".trim()
}

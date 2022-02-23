@file:Suppress("UNUSED_PARAMETER")

package lesson2.task2

import lesson1.task1.sqr
import kotlin.math.abs
import kotlin.math.pow

/**
 * Пример
 *
 * Лежит ли точка (x, y) внутри окружности с центром в (x0, y0) и радиусом r?
 */
fun pointInsideCircle(x: Double, y: Double, x0: Double, y0: Double, r: Double) =
    sqr(x - x0) + sqr(y - y0) <= sqr(r)

/**
 * Простая (2 балла)
 *
 * Четырехзначное число назовем счастливым, если сумма первых двух ее цифр равна сумме двух последних.
 * Определить, счастливое ли заданное число, вернуть true, если это так.
 */
fun isNumberHappy(number: Int): Boolean =
    number / 1000 + (number / 100) % 10 == (number / 10) % 10 + number % 10

/**
 * Простая (2 балла)
 *
 * На шахматной доске стоят два ферзя (ферзь бьет по вертикали, горизонтали и диагоналям).
 * Определить, угрожают ли они друг другу. Вернуть true, если угрожают.
 * Считать, что ферзи не могут загораживать друг друга.
 */
fun queenThreatens(x1: Int, y1: Int, x2: Int, y2: Int): Boolean = 
    x1 == x2 || y1 == y2 || abs(x2 - x1) == abs(y2 - y1)


/**
 * Простая (2 балла)
 *
 * Дан номер месяца (от 1 до 12 включительно) и год (положительный).
 * Вернуть число дней в этом месяце этого года по григорианскому календарю.
 */
fun daysInMonth(month: Int, year: Int): Int {
    val leapYear = year % 400 == 0 || year % 100 != 0 && year % 4 == 0
    val daysInFeb = if (leapYear) 29 else 28
    return when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        else -> daysInFeb
    }
}

/**
 * Простая (2 балла)
 *
 * Проверить, лежит ли окружность с центром в (x1, y1) и радиусом r1 целиком внутри
 * окружности с центром в (x2, y2) и радиусом r2.
 * Вернуть true, если утверждение верно
 */
fun circleInside(
    x1: Double, y1: Double, r1: Double,
    x2: Double, y2: Double, r2: Double
): Boolean = sqrt((x1 - x2).pow(2.0) + (y1 - y2).pow(2.0)) <= (r2 - r1)

/**
 * Функция avgOf принимает 3 параметра типа Т, который должен быть сравнимым. Параметр Т указывается явно в <...>
 * Пример: запись avgOf<Int>(a, b, c) аналог "обычной" функции вида:
 * fun avgOf(a: Int, b: Int, c: Int): Int = ...
 */
fun <T : Comparable<T>> avgOf(a: T, b: T, c: T): T =
    when (minOf(a, b, c)) {
        a -> minOf(b, c)
        b -> minOf(a, c)
        else -> minOf(a, b)
    }

/**
 * Средняя (3 балла)
 *
 * Определить, пройдет ли кирпич со сторонами а, b, c сквозь прямоугольное отверстие в стене со сторонами r и s.
 * Стороны отверстия должны быть параллельны граням кирпича.
 * Считать, что совпадения длин сторон достаточно для прохождения кирпича, т.е., например,
 * кирпич 4 х 4 х 4 пройдёт через отверстие 4 х 4.
 * Вернуть true, если кирпич пройдёт
 */
fun brickPasses(a: Int, b: Int, c: Int, r: Int, s: Int): Boolean =
    a <= r && b <= s || a <= s && b <= r ||
    b <= r && c <= s || b <= s && c <= r ||
    a <= r && c <= s || a <= s && c <= r
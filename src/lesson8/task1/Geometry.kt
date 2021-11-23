@file:Suppress("UNUSED_PARAMETER")

package lesson8.task1

import lesson1.task1.sqr
import kotlin.math.*

// Урок 8: простые классы
// Максимальное количество баллов = 40 (без очень трудных задач = 11)

/**
 * Точка на плоскости
 */
data class Point(val x: Double, val y: Double) {
    /**
     * Пример
     *
     * Рассчитать (по известной формуле) расстояние между двумя точками
     */
    private fun avgAxis(a1: Double, a2: Double): Double = abs(a1 - a2) / 2 + min(a1, a2)
    fun distance(other: Point): Double = sqrt(sqr(x - other.x) + sqr(y - other.y))
    fun avgPoint(other: Point): Point = Point(avgAxis(x, other.x), avgAxis(y, other.y))
    fun subtract(p: Point): Point = Point(x - p.x, y - p.y)
    fun cross(p: Point): Double = x * p.y - y * p.x
}

/**
 * Треугольник, заданный тремя точками (a, b, c, см. constructor ниже).
 * Эти три точки хранятся в множестве points, их порядок не имеет значения.
 */
@Suppress("MemberVisibilityCanBePrivate")
class Triangle private constructor(private val points: Set<Point>) {

    private val pointList = points.toList()

    val a: Point get() = pointList[0]

    val b: Point get() = pointList[1]

    val c: Point get() = pointList[2]

    constructor(a: Point, b: Point, c: Point) : this(linkedSetOf(a, b, c))

    /**
     * Пример: полупериметр
     */
    fun halfPerimeter() = (a.distance(b) + b.distance(c) + c.distance(a)) / 2.0

    /**
     * Пример: площадь
     */
    fun area(): Double {
        val p = halfPerimeter()
        return sqrt(p * (p - a.distance(b)) * (p - b.distance(c)) * (p - c.distance(a)))
    }

    /**
     * Пример: треугольник содержит точку
     */
    fun contains(p: Point): Boolean {
        val abp = Triangle(a, b, p)
        val bcp = Triangle(b, c, p)
        val cap = Triangle(c, a, p)
        return abp.area() + bcp.area() + cap.area() <= area()
    }

    override fun equals(other: Any?) = other is Triangle && points == other.points

    override fun hashCode() = points.hashCode()

    override fun toString() = "Triangle(a = $a, b = $b, c = $c)"
}

/**
 * Окружность с заданным центром и радиусом
 */
data class Circle(val center: Point, val radius: Double) {
    /**
     * Простая (2 балла)
     *
     * Рассчитать расстояние между двумя окружностями.
     * Расстояние между непересекающимися окружностями рассчитывается как
     * расстояние между их центрами минус сумма их радиусов.
     * Расстояние между пересекающимися окружностями считать равным 0.0.
     */
    fun distance(other: Circle): Double = max(center.distance(other.center) - (radius + other.radius), 0.0)

    /**
     * Тривиальная (1 балл)
     *
     * Вернуть true, если и только если окружность содержит данную точку НА себе или ВНУТРИ себя
     */
    fun contains(p: Point): Boolean =
        radius.pow(2) - ((p.x - center.x).pow(2) + (p.y - center.y).pow(2)) >= 0
}

/**
 * Отрезок между двумя точками
 */
data class Segment(val begin: Point, val end: Point) {
    override fun equals(other: Any?) =
        other is Segment && (begin == other.begin && end == other.end || end == other.begin && begin == other.end)

    override fun hashCode() =
        begin.hashCode() + end.hashCode()
}

/**
 * Средняя (3 балла)
 *
 * Дано множество точек. Вернуть отрезок, соединяющий две наиболее удалённые из них.
 * Если в множестве менее двух точек, бросить IllegalArgumentException
 */
fun diameter(vararg points: Point): Segment {
    if (points.size < 2) throw IllegalArgumentException("Error")
    var maxi = Double.MIN_VALUE
    var out = listOf<Int>(-1, -1)
    for (i in (points.size - 1) downTo 0)
        for (j in (i - 1) downTo 0)
            if (maxi <= points[i].distance(points[j])) {
                maxi = points[i].distance(points[j])
                out = listOf(i, j)
            }
    return Segment(points[out[1]], points[out[0]])
}

/**
 * Простая (2 балла)
 *
 * Построить окружность по её диаметру, заданному двумя точками
 * Центр её должен находиться посередине между точками, а радиус составлять половину расстояния между ними
 */

fun circleByDiameter(diameter: Segment): Circle {
    val c = Point((diameter.begin.x + diameter.end.x) / 2, (diameter.begin.y + diameter.end.y) / 2)
    return Circle(c, max(c.distance(diameter.begin), c.distance(diameter.end)))
}

/**
 * Прямая, заданная точкой point и углом наклона angle (в радианах) по отношению к оси X.
 * Уравнение прямой: (y - point.y) * cos(angle) = (x - point.x) * sin(angle)
 * или: y * cos(angle) = x * sin(angle) + b, где b = point.y * cos(angle) - point.x * sin(angle).
 * Угол наклона обязан находиться в диапазоне от 0 (включительно) до PI (исключительно).
 */
class Line private constructor(val b: Double, val angle: Double) {
    init {
        require(angle >= 0 && angle < PI) { "Incorrect line angle: $angle" }
    }

    constructor(point: Point, angle: Double) : this(point.y * cos(angle) - point.x * sin(angle), angle)

    /**
     * Средняя (3 балла)
     *
     * Найти точку пересечения с другой линией.
     * Для этого необходимо составить и решить систему из двух уравнений (каждое для своей прямой)
     */

    fun crossPoint(other: Line): Point =
        Point(
            (b * cos(other.angle) - other.b * cos(angle)) / sin(other.angle - angle),
            (b * sin(other.angle) - other.b * sin(angle)) / sin(other.angle - angle)
        )


    override fun equals(other: Any?) = other is Line && angle == other.angle && b == other.b

    override fun hashCode(): Int {
        var result = b.hashCode()
        result = 31 * result + angle.hashCode()
        return result
    }

    override fun toString() = "Line(${cos(angle)} * y = ${sin(angle)} * x + $b)"
}

/**
 * Средняя (3 балла)
 *
 * Построить прямую по отрезку
 */
//Угол наклона обязан находиться в диапазоне от 0 (включительно) до PI (исключительно).
private fun calAngle(begin: Point, end: Point): Double = when {
    begin.x - end.x == 0.0 -> PI / 2
    atan((begin.y - end.y) / (begin.x - end.x)) + PI < PI -> atan((begin.y - end.y) / (begin.x - end.x))
    else -> atan((begin.y - end.y) / (begin.x - end.x)) + PI
}

fun lineBySegment(s: Segment): Line =
    Line(
        min(s.begin, s.end),
        calAngle(s.begin, s.end)
    )

fun min(begin: Point, end: Point): Point = when {
    begin.y > end.y -> end
    begin.y < end.y -> begin
    else -> if (begin.x < end.x) begin else end
}

/**
 * Средняя (3 балла)
 *
 * Построить прямую по двум точкам
 */
fun lineByPoints(a: Point, b: Point): Line = lineBySegment(Segment(a, b))

/**
 * Сложная (5 баллов)
 *
 * Построить серединный перпендикуляр по отрезку или по двум точкам
 */
fun bisectorByPoints(a: Point, b: Point): Line = TODO()

/**
 * Средняя (3 балла)
 *
 * Задан список из n окружностей на плоскости.
 * Найти пару наименее удалённых из них; расстояние между окружностями
 * рассчитывать так, как указано в Circle.distance.
 *
 * При наличии нескольких наименее удалённых пар,
 * вернуть первую из них по порядку в списке circles.
 *
 * Если в списке менее двух окружностей, бросить IllegalArgumentException
 */
fun findNearestCirclePair(vararg circles: Circle): Pair<Circle, Circle> {
    if (circles.size < 2) throw IllegalArgumentException("Error")
    var maxi = Double.MAX_VALUE
    var out = listOf<Int>(-1, -1)
    for (i in circles.size - 1 downTo 0)
        for (j in i - 1 downTo 0)
            if (maxi >= circles[i].distance(circles[j])) {
                maxi = circles[i].distance(circles[j])
                out = listOf(i, j)
            }
    return Pair(circles[out[1]], circles[out[0]])
}

/**
 * Сложная (5 баллов)
 *
 * Дано три различные точки. Построить окружность, проходящую через них
 * (все три точки должны лежать НА, а не ВНУТРИ, окружности).
 * Описание алгоритмов см. в Интернете
 * (построить окружность по трём точкам, или
 * построить окружность, описанную вокруг треугольника - эквивалентная задача).
 */
fun circleByThreePoints(a: Point, b: Point, c: Point): Circle {
    val ox = (min(min(a.x, b.x), c.x) + max(max(a.x, b.x), c.x)) / 2
    val oy = (min(min(a.y, b.y), c.y) + max(max(a.y, b.y), c.y)) / 2
    val ax = a.x - ox
    val ay = a.y - oy
    val bx = b.x - ox
    val by = b.y - oy
    val cx = c.x - ox
    val cy = c.y - oy
    val d = (ax * (by - cy) + bx * (cy - ay) + cx * (ay - by)) * 2
    val x = ((sqr(ax) + sqr(ay)) * (by - cy) + (sqr(bx) + sqr(by)) * (cy - ay) + (sqr(cx) + sqr(cy)) * (ay - by)) / d
    val y = ((sqr(ax) + sqr(ay)) * (cx - bx) + (sqr(bx) + sqr(by)) * (ax - cx) + (sqr(cx) + sqr(cy)) * (bx - ax)) / d
    val p = Point(ox + x, oy + y)
    val r = max(max(p.distance(a), p.distance(b)), p.distance(c))
    return Circle(p, r)
}

/**
 * Очень сложная (10 баллов)
 *
 * Дано множество точек на плоскости. Найти круг минимального радиуса,
 * содержащий все эти точки. Если множество пустое, бросить IllegalArgumentException.
 * Если множество содержит одну точку, вернуть круг нулевого радиуса с центром в данной точке.
 *
 * Примечание: в зависимости от ситуации, такая окружность может либо проходить через какие-либо
 * три точки данного множества, либо иметь своим диаметром отрезок,
 * соединяющий две самые удалённые точки в данном множестве.
 */
fun minCircle(points: List<Point>): Circle {
    var out = circleByThreePoints(points[0], points[1], points[2])
    for (i in 3 until points.size) {
        val elem1 = points[i]
        if (out.contains(elem1)) continue
        var preCircle = Circle(elem1, -1.0)
        for (j in 0..i) {
            val elem2 = points[j]
            if (out.contains(elem2)) continue
            if (preCircle.radius == -1.0) {
                preCircle = circleByDiameter(Segment(elem1, elem2))
            } else {
                val pq = elem2.subtract(elem1)
                var left = Circle(Point(0.0, 0.0), -1.0)
                var right = Circle(Point(0.0, 0.0), -1.0)
                val circ = circleByDiameter(Segment(elem1, elem2))
                for (k in 0..j) {
                    val elem3 = points[k]
                    if (preCircle.contains(elem3)) continue
                    val cross = pq.cross(elem3.subtract(elem1))
                    val probableCircle = circleByThreePoints(elem1, elem2, elem3)
                    if (probableCircle.radius == 0.0) continue
                    else if (cross > 0 && (left.radius == -1.0 || pq.cross(probableCircle.center.subtract(elem1)) > pq.cross(
                            left.center.subtract(
                                elem1
                            )
                        ))
                    )
                        left = probableCircle
                    else if (cross < 0 && (right.radius == -1.0 || pq.cross(probableCircle.center.subtract(elem1)) < pq.cross(
                            right.center.subtract(
                                elem1
                            )
                        ))
                    )
                        right = probableCircle
                }
                out = when {
                    left.radius == -1.0 && right.radius == -1.0 -> circ
                    left.radius == -1.0 -> right
                    right.radius == -1.0 -> left
                    else -> if (left.radius <= right.radius) left
                    else right
                }
            }
        }
    }
    return out
}

fun minContainingCircle(vararg points: Point): Circle = when (points.size) {
    0 -> throw IllegalArgumentException()
    1 -> Circle(points[0], 0.0)
    2 -> circleByDiameter(Segment(points[0], points[1]))
    3 -> circleByThreePoints(points[0], points[1], points[2])
    else -> minCircle(points.toList())
}

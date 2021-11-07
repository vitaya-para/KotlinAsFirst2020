@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")

package lesson4.task1

import lesson1.task1.discriminant
import kotlin.math.pow
import kotlin.math.sqrt

// Урок 4: списки
// Максимальное количество баллов = 12
// Рекомендуемое количество баллов = 8
// Вместе с предыдущими уроками = 24/33

/**
 * Пример
 *
 * Найти все корни уравнения x^2 = y
 */
fun sqRoots(y: Double) =
    when {
        y < 0 -> listOf()
        y == 0.0 -> listOf(0.0)
        else -> {
            val root = sqrt(y)
            // Результат!
            listOf(-root, root)
        }
    }

/**
 * Пример
 *
 * Найти все корни биквадратного уравнения ax^4 + bx^2 + c = 0.
 * Вернуть список корней (пустой, если корней нет)
 */
fun biRoots(a: Double, b: Double, c: Double): List<Double> {
    if (a == 0.0) {
        return if (b == 0.0) listOf()
        else sqRoots(-c / b)
    }
    val d = discriminant(a, b, c)
    if (d < 0.0) return listOf()
    if (d == 0.0) return sqRoots(-b / (2 * a))
    val y1 = (-b + sqrt(d)) / (2 * a)
    val y2 = (-b - sqrt(d)) / (2 * a)
    return sqRoots(y1) + sqRoots(y2)
}

/**
 * Пример
 *
 * Выделить в список отрицательные элементы из заданного списка
 */
fun negativeList(list: List<Int>): List<Int> {
    val result = mutableListOf<Int>()
    for (element in list) {
        if (element < 0) {
            result.add(element)
        }
    }
    return result
}

/**
 * Пример
 *
 * Изменить знак для всех положительных элементов списка
 */
fun invertPositives(list: MutableList<Int>) {
    for (i in 0 until list.size) {
        val element = list[i]
        if (element > 0) {
            list[i] = -element
        }
    }
}

/**
 * Пример
 *
 * Из имеющегося списка целых чисел, сформировать список их квадратов
 */
fun squares(list: List<Int>) = list.map { it * it }

/**
 * Пример
 *
 * Из имеющихся целых чисел, заданного через vararg-параметр, сформировать массив их квадратов
 */
fun squares(vararg array: Int) = squares(array.toList()).toTypedArray()

/**
 * Пример
 *
 * По заданной строке str определить, является ли она палиндромом.
 * В палиндроме первый символ должен быть равен последнему, второй предпоследнему и т.д.
 * Одни и те же буквы в разном регистре следует считать равными с точки зрения данной задачи.
 * Пробелы не следует принимать во внимание при сравнении символов, например, строка
 * "А роза упала на лапу Азора" является палиндромом.
 */
fun isPalindrome(str: String): Boolean {
    val lowerCase = str.toLowerCase().filter { it != ' ' }
    for (i in 0..lowerCase.length / 2) {
        if (lowerCase[i] != lowerCase[lowerCase.length - i - 1]) return false
    }
    return true
}

/**
 * Пример
 *
 * По имеющемуся списку целых чисел, например [3, 6, 5, 4, 9], построить строку с примером их суммирования:
 * 3 + 6 + 5 + 4 + 9 = 27 в данном случае.
 */
fun buildSumExample(list: List<Int>) = list.joinToString(separator = " + ", postfix = " = ${list.sum()}")

/**
 * Простая (2 балла)
 *
 * Найти модуль заданного вектора, представленного в виде списка v,
 * по формуле abs = sqrt(a1^2 + a2^2 + ... + aN^2).
 * Модуль пустого вектора считать равным 0.0.
 */
fun abs(v: List<Double>): Double =
    v.fold(0.0) { previousResult, element -> sqrt(previousResult.pow(2.0) + element.pow(2)) }

/**
 * Простая (2 балла)
 *
 * Рассчитать среднее арифметическое элементов списка list. Вернуть 0.0, если список пуст
 */
fun mean(list: List<Double>): Double = if (list.isEmpty()) 0.0 else list.sum() / list.size

/**
 * Средняя (3 балла)
 *
 * Центрировать заданный список list, уменьшив каждый элемент на среднее арифметическое всех элементов.
 * Если список пуст, не делать ничего. Вернуть изменённый список.
 *
 * Обратите внимание, что данная функция должна изменять содержание списка list, а не его копии.
 */
fun center(list: MutableList<Double>): MutableList<Double> {
    val avg = mean(list)
    for (i in 0 until list.size)
        list[i] -= avg
    return list
}

/**
 * Средняя (3 балла)
 *
 * Найти скалярное произведение двух векторов равной размерности,
 * представленные в виде списков a и b. Скалярное произведение считать по формуле:
 * C = a1b1 + a2b2 + ... + aNbN. Произведение пустых векторов считать равным 0.
 */
fun times(a: List<Int>, b: List<Int>): Int {
    var res = 0
    for (i in a.indices)
        res += a[i] * b[i]
    return res
}

/**
 * Средняя (3 балла)
 *
 * Рассчитать значение многочлена при заданном x:
 * p(x) = p0 + p1*x + p2*x^2 + p3*x^3 + ... + pN*x^N.
 * Коэффициенты многочлена заданы списком p: (p0, p1, p2, p3, ..., pN).
 * Значение пустого многочлена равно 0 при любом x.
 */

fun polynom(p: List<Int>, x: Int): Int {
    if (p.isEmpty()) return 0
    var res = 0
    var myPow = 1
    for (i in 0 until p.size) {
        res += p[i] * myPow
        myPow *= x
    }
    return res
}

fun Int.pow(i: Int): Int = this.toDouble().pow(i).toInt()

/**
 * Средняя (3 балла)
 *
 * В заданном списке list каждый элемент, кроме первого, заменить
 * суммой данного элемента и всех предыдущих.
 * Например: 1, 2, 3, 4 -> 1, 3, 6, 10.
 * Пустой список не следует изменять. Вернуть изменённый список.
 *
 * Обратите внимание, что данная функция должна изменять содержание списка list, а не его копии.
 */

fun accumulate(list: MutableList<Int>): MutableList<Int> {
    for (i in 1 until list.size)
        list[i] += list[i - 1]
    return list
}

/**
 * Средняя (3 балла)
 *
 * Разложить заданное натуральное число n > 1 на простые множители.
 * Результат разложения вернуть в виде списка множителей, например 75 -> (3, 5, 5).
 * Множители в списке должны располагаться по возрастанию.
 */
fun factorize(n: Int): List<Int> {
    val thisNumberIsPrime = MutableList(sqrt(n.toDouble()).toInt() + 1) { true }
    val res = mutableListOf<Int>()
    var cp = n
    thisNumberIsPrime[0] = false
    thisNumberIsPrime[1] = false
    for (i in 2 until thisNumberIsPrime.size) {
        if (thisNumberIsPrime[i]) {
            for (j in 2 * i until thisNumberIsPrime.size step i)
                thisNumberIsPrime[j] = false
            while (cp % i == 0) {
                cp /= i
                res.add(i)
            }
        }
    }
    if (cp != 1)
        res.add(cp)
    return res
}

/**
 * Сложная (4 балла)
 *
 * Разложить заданное натуральное число n > 1 на простые множители.
 * Результат разложения вернуть в виде строки, например 75 -> 3*5*5
 * Множители в результирующей строке должны располагаться по возрастанию.
 */
fun factorizeToString(n: Int): String = factorize(n).joinToString(separator = "*")

/**
 * Средняя (3 балла)
 *
 * Перевести заданное целое число n >= 0 в систему счисления с основанием base > 1.
 * Результат перевода вернуть в виде списка цифр в base-ичной системе от старшей к младшей,
 * например: n = 100, base = 4 -> (1, 2, 1, 0) или n = 250, base = 14 -> (1, 3, 12)
 */
fun convert(n: Int, base: Int): List<Int> {
    val res = mutableListOf<Int>()
    var cp = n
    do {
        res.add(cp % base)
        cp /= base
    } while (cp > 0)
    return res.reversed()
}

/**
 * Сложная (4 балла)
 *
 * Перевести заданное целое число n >= 0 в систему счисления с основанием 1 < base < 37.
 * Результат перевода вернуть в виде строки, цифры более 9 представлять латинскими
 * строчными буквами: 10 -> a, 11 -> b, 12 -> c и так далее.
 * Например: n = 100, base = 4 -> 1210, n = 250, base = 14 -> 13c
 *
 * Использовать функции стандартной библиотеки, напрямую и полностью решающие данную задачу
 * (например, n.toString(base) и подобные), запрещается.
 */
fun convertToString(n: Int, base: Int): String = convert(n, base).fold("")
{ previousResult, element -> if (element < 10) previousResult + element else previousResult + ('a' - 10 + element) }

/**
 * Средняя (3 балла)
 *
 * Перевести число, представленное списком цифр digits от старшей к младшей,
 * из системы счисления с основанием base в десятичную.
 * Например: digits = (1, 3, 12), base = 14 -> 250
 */
fun decimal(digits: List<Int>, base: Int): Int {
    var res = 0
    var j = 0
    for (i in digits.size - 1 downTo 0) {
        res += digits[j] * base.pow(i)
        j++
    }
    return res
}

/**
 * Сложная (4 балла)
 *
 * Перевести число, представленное цифровой строкой str,
 * из системы счисления с основанием base в десятичную.
 * Цифры более 9 представляются латинскими строчными буквами:
 * 10 -> a, 11 -> b, 12 -> c и так далее.
 * Например: str = "13c", base = 14 -> 250
 *
 * Использовать функции стандартной библиотеки, напрямую и полностью решающие данную задачу
 * (например, str.toInt(base)), запрещается.
 */
fun decimalFromString(str: String, base: Int): Int = decimal(
    str.toList().map { if (it.code < 'a'.code) it.code - '0'.code else it - 'a' + 10 },
    base
)

/**
 * Сложная (5 баллов)
 *
 * Перевести натуральное число n > 0 в римскую систему.
 * Римские цифры: 1 = I, 4 = IV, 5 = V, 9 = IX, 10 = X, 40 = XL, 50 = L,
 * 90 = XC, 100 = C, 400 = CD, 500 = D, 900 = CM, 1000 = M.
 * Например: 23 = XXIII, 44 = XLIV, 100 = C
 */

fun roman(n: Int): String {
    val nums = listOf(1000, 500, 100, 50, 10, 5, 1)
    val assoc = mapOf<Int, String>(
        1 to "I",
        5 to "V",
        10 to "X",
        50 to "L",
        100 to "C",
        500 to "D",
        1000 to "M"
    )
    var cp = n
    return buildString {
        append(padEnd(cp / 1000, 'M'))
        cp %= 1000
        for (i in 2 until nums.size) {
            val occurrence = cp / nums[i - 1]
            val occurrenceNext = cp / nums[i]
            when {
                occurrenceNext == 9 -> {
                    append(assoc[nums[i]] + assoc[nums[i - 2]])
                    cp -= (nums[i - 2] - nums[i])
                }
                // в случае если occurrence будет 2, нам будет более "выгодно" заменить на символы, связанные с ним, нежели
                // с occurrenceNext (предполагается, что в последовательности MDCLXVI occurrence отвечает за более ранний символ,
                // а occurrenceNext за последующий (если occurrence ~ M, то occurrenceNext ~ D))
                occurrenceNext == 4 && occurrence != 2 -> {
                    append(assoc[nums[i]] + assoc[nums[i - 1]])
                    cp -= (nums[i - 1] - nums[i])
                }
                else -> when (occurrence) {
                    4 -> {
                        append(assoc[nums[i - 1]] + assoc[nums[i - 2]])
                        cp -= (nums[i - 2] - nums[i - 1])
                    }
                    else -> {
                        for (j in 0 until occurrence)
                            append(assoc[nums[i - 1]])
                        cp %= nums[i - 1]
                    }
                }
            }
        }
        for (i in 1..cp) {
            append(assoc[nums[6]])
        }
    }
}

/**
 * Очень сложная (7 баллов)
 *
 * Записать заданное натуральное число 1..999999 прописью по-русски.
 * Например, 375 = "триста семьдесят пять",
 * 23964 = "двадцать три тысячи девятьсот шестьдесят четыре"
 */
private fun hundreds(n: Int): String {
    val units = listOf<String>(
        "",
        "один",
        "два",
        "три",
        "четыре",
        "пять",
        "шесть",
        "семь",
        "восемь",
        "девять",
    )
    var res = when (n / 100) {
        0 -> ""
        1 -> "сто"
        2 -> "двести"
        in 3..4 -> units[n / 100] + "ста"
        else -> units[n / 100] + "сот"
    }
    res += " "
    if (n % 100 in 11..19) {
        res += when (n % 10) {
            2 -> "двенадцать"
            4 -> "четырнадцать"
            else -> units[n % 10].replace("ь", "") + "надцать"
        }
    } else {
        res += when ((n % 100) / 10) {
            0 -> ""
            1 -> "десять "
            2, 3 -> units[(n % 100) / 10] + "дцать "
            4 -> "сорок "
            in 5..8 -> units[(n % 100) / 10] + "десят "
            else -> "девяносто "
        }
        res += units[n % 10]
    }
    return res.trim(' ')
}

private fun hundredsForThousand(n: Int): String {
    if (n == 0) return ""
    if (n == 1) return "тысяча"
    val preRes = hundreds(n)
    return if (n % 100 in 11..19)
        "$preRes тысяч"
    else when (n % 10) {
        1 -> preRes.replace("один", "одна тысяча")
        2 -> ("$preRes ").replace("два ", "две тысячи")
        in 2..4 -> "$preRes тысячи"
        else -> "$preRes тысяч"
    }
}

fun russian(n: Int): String = when (n) {
    0 -> "ноль"
    else -> (hundredsForThousand(n / 1000) + " " + hundreds(n % 1000)).trim()
}
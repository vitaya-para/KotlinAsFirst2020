@file:Suppress("UNUSED_PARAMETER")

package lesson3.task1

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

// Урок 3: циклы
// Максимальное количество баллов = 9
// Рекомендуемое количество баллов = 7
// Вместе с предыдущими уроками = 16/21

/**
 * Пример
 *
 * Вычисление факториала
 */
fun factorial(n: Int): Double {
    var result = 1.0
    for (i in 1..n) {
        result = result * i // Please do not fix in master
    }
    return result
}

/**
 * Пример
 *
 * Проверка числа на простоту -- результат true, если число простое
 */
fun isPrime(n: Int): Boolean {
    if (n < 2) return false
    if (n == 2) return true
    if (n % 2 == 0) return false
    for (m in 3..sqrt(n.toDouble()).toInt() step 2) {
        if (n % m == 0) return false
    }
    return true
}

/**
 * Пример
 *
 * Проверка числа на совершенность -- результат true, если число совершенное
 */
fun isPerfect(n: Int): Boolean {
    var sum = 1
    for (m in 2..n / 2) {
        if (n % m > 0) continue
        sum += m
        if (sum > n) break
    }
    return sum == n
}

/**
 * Пример
 *
 * Найти число вхождений цифры m в число n
 */
fun digitCountInNumber(n: Int, m: Int): Int =
    when {
        n == m -> 1
        n < 10 -> 0
        else -> digitCountInNumber(n / 10, m) + digitCountInNumber(n % 10, m)
    }

/**
 * Простая (2 балла)
 *
 * Найти количество цифр в заданном числе n.
 * Например, число 1 содержит 1 цифру, 456 -- 3 цифры, 65536 -- 5 цифр.
 *
 * Использовать операции со строками в этой задаче запрещается.
 */
fun digitNumber(n: Int): Int {

    var curN = abs(n)

    if (curN < 10)
        return 1

    var count = 0

    while (curN > 0) {
        count++
        curN /= 10
    }
    return count
}

/**
 * Простая (2 балла)
 *
 * Найти число Фибоначчи из ряда 1, 1, 2, 3, 5, 8, 13, 21, ... с номером n.
 * Ряд Фибоначчи определён следующим образом: fib(1) = 1, fib(2) = 1, fib(n+2) = fib(n) + fib(n+1)
 */
fun fib(n: Int): Int {
    var prev = 0
    var beforePrev = 1
    var cur = 1

    while (cur != n) {
        val t = prev
        prev += beforePrev
        beforePrev = t
        cur++
    }
    return prev + beforePrev
}

/**
 * Простая (2 балла)
 *
 * Для заданного числа n > 1 найти минимальный делитель, превышающий 1
 */
fun minDivisor(n: Int): Int {

    for (divisor in 2..sqrt(n.toDouble()).toInt()) {
        if (n % divisor == 0)
            return divisor
    }
    return n
}

/**
 * Простая (2 балла)
 *
 * Для заданного числа n > 1 найти максимальный делитель, меньший n
 */
fun maxDivisor(n: Int) = n / minDivisor(n)


/**
 * Простая (2 балла)
 *
 * Гипотеза Коллатца. Рекуррентная последовательность чисел задана следующим образом:
 *
 *   ЕСЛИ (X четное)
 *     Xслед = X /2
 *   ИНАЧЕ
 *     Xслед = 3 * X + 1
 *
 * например
 *   15 46 23 70 35 106 53 160 80 40 20 10 5 16 8 4 2 1 4 2 1 4 2 1 ...
 * Данная последовательность рано или поздно встречает X == 1.
 * Написать функцию, которая находит, сколько шагов требуется для
 * этого для какого-либо начального X > 0.
 */
fun collatzSteps(x: Int): Int {
    var steps = 0
    var curX = x
    while (curX != 1) {
        if (curX % 2 == 0)
            curX /= 2
        else
            curX = curX * 3 + 1
        steps++
    }
    return steps
}

/**
 * Средняя (3 балла)
 *
 * Для заданных чисел m и n найти наименьшее общее кратное, то есть,
 * минимальное число k, которое делится и на m и на n без остатка
 */
fun lcm(m: Int, n: Int): Int {

    val gcd = countGCD(m, n)

    return (m * n) / gcd

}

fun countGCD(a: Int, b: Int): Int {

    var curA = a
    var curB = b

    while (curA != 0 && curB != 0) {
        if (curA > curB)
            curA %= curB
        else
            curB %= curA
    }
    return curA + curB
}

/**
 * Средняя (3 балла)
 *
 * Определить, являются ли два заданных числа m и n взаимно простыми.
 * Взаимно простые числа не имеют общих делителей, кроме 1.
 * Например, 25 и 49 взаимно простые, а 6 и 8 -- нет.
 */
fun isCoPrime(m: Int, n: Int): Boolean = countGCD(m, n) == 1


/**
 * Средняя (3 балла)
 *
 * Для заданных чисел m и n, m <= n, определить, имеется ли хотя бы один точный квадрат между m и n,
 * то есть, существует ли такое целое k, что m <= k*k <= n.
 * Например, для интервала 21..28 21 <= 5*5 <= 28, а для интервала 51..61 квадрата не существует.
 */
fun squareBetweenExists(m: Int, n: Int): Boolean = TODO()

/**
 * Средняя (3 балла)
 *
 * Поменять порядок цифр заданного числа n на обратный: 13478 -> 87431.
 *
 * Использовать операции со строками в этой задаче запрещается.
 */
fun revert(n: Int): Int {

    var curN = abs(n)
    var reversed = 0

    while (curN > 0) {
        reversed = reversed * 10 + curN % 10
        curN /= 10
    }
    return reversed
}

/**
 * Средняя (3 балла)
 *
 * Проверить, является ли заданное число n палиндромом:
 * первая цифра равна последней, вторая -- предпоследней и так далее.
 * 15751 -- палиндром, 3653 -- нет.
 *
 * Использовать операции со строками в этой задаче запрещается.
 */
fun isPalindrome(n: Int): Boolean = n == revert(n)

fun isPalindrome(n: Int): Boolean {
    val len = getLen(n)
    val res = revert((n % 10.0.pow(len / 2)).toInt())
    return (n / 10.0.pow(len - len / 2).toInt()) == res * 10.0.pow(len / 2 - getLen(res)).toInt()
}

/*
 * Средняя (3 балла)
 *
 * Для заданного числа n определить, содержит ли оно различающиеся цифры.
 * Например, 54 и 323 состоят из разных цифр, а 111 и 0 из одинаковых.
 *
 * Использовать операции со строками в этой задаче запрещается.
 */
fun hasDifferentDigits(n: Int): Boolean {

    var curN = abs(n)
    if (curN < 10)
        return false

    val dig = curN % 10
    curN /= 10

    while (curN > 0) {
        if (dig != curN % 10)
            return true
        curN /= 10
    }
    return false

}

/**
 * Средняя (4 балла)
 *
 * Для заданного x рассчитать с заданной точностью eps
 *  * cos(x) = 1 - x^2 / 2! + x^4 / 4! - x^6 / 6! + ...
 * sin(x) = x - x^3 / 3! + x^5 / 5! - x^7 / 7! + ...
 * Нужную точность считать достигнутой, если очередной член ряда меньше eps по модулю.
 * Подумайте, как добиться более быстрой сходимости ряда при больших значениях x.
 * Использовать kotlin.math.sin и другие стандартные реализации функции синуса в этой задаче запрещается.
 */
fun sin(x: Double, eps: Double): Double {
    val curX = x % (2 * PI)
    var ans = curX
    var count = 2
    var n = 3
    do {
        val sammand = (-1.0).pow(if (count % 2 == 0) 1 else 0) * curX.pow(n) / factorial(n)
        ans += sammand
        count++
        n += 2
    } while (abs(sammand) >= eps)
    return ans
}

/**
 * Средняя (4 балла)
 *
 * Для заданного x рассчитать с заданной точностью eps
 * cos(x) = 1 - x^2 / 2! + x^4 / 4! - x^6 / 6! + ...
 * Нужную точность считать достигнутой, если очередной член ряда меньше eps по модулю
 * Подумайте, как добиться более быстрой сходимости ряда при больших значениях x.
 * Использовать kotlin.math.cos и другие стандартные реализации функции косинуса в этой задаче запрещается.
 */
fun cos(x: Double, eps: Double): Double {
    val curX = x % (2 * PI)
    var ans = 1.0
    var count = 2
    var n = 2
    do {
        val sammand = (-1.0).pow(if (count % 2 == 0) 1 else 0) * curX.pow(n) / factorial(n)
        ans += sammand
        count++
        n += 2
    } while (abs(sammand) >= eps)
    return ans
}

/**
 * Сложная (4 балла)
 *
 * Найти n-ю цифру последовательности из квадратов целых чисел:
 * 149162536496481100121144...
 * Например, 2-я цифра равна 4, 7-я 5, 12-я 6.
 *
 * Использовать операции со строками в этой задаче запрещается.
 */
fun squareSequenceDigit(n: Int): Int {

    var curN = 0
    var a = 1
    var num = a * a

    while (curN + digitNumber(num) < n) {
        a++
        curN += digitNumber(num)
        num = a * a
    }

    curN += digitNumber(num)


    while (curN != n) {
        curN--
        num /= 10
    }
    return num % 10

}



/**
 * Сложная (5 баллов)
 *
 * Найти n-ю цифру последовательности из чисел Фибоначчи (см. функцию fib выше):
 * 1123581321345589144...
 * Например, 2-я цифра равна 1, 9-я 2, 14-я 5.
 *
 * Использовать операции со строками в этой задаче запрещается.
 */


fun fibSequenceDigit(n: Int): Int {

    var curN = 0
    var b = 1

    var fibNum = fib(b)
    while (curN + digitNumber(fibNum) < n) {

        curN += digitNumber(fibNum)
        b++
        fibNum = fib(b)

    }

    // var fibNum = fib(b + 1)
    curN += digitNumber(fibNum)

    while (curN != n) {
        curN--
        fibNum /= 10
    }

    return fibNum % 10
}
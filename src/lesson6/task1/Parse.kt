@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")

package lesson6.task1

import lesson2.task2.daysInMonth
import lesson4.task1.roman
import kotlin.math.max

// Урок 6: разбор строк, исключения
// Максимальное количество баллов = 13
// Рекомендуемое количество баллов = 11
// Вместе с предыдущими уроками (пять лучших, 2-6) = 40/54

/**
 * Пример
 *
 * Время представлено строкой вида "11:34:45", содержащей часы, минуты и секунды, разделённые двоеточием.
 * Разобрать эту строку и рассчитать количество секунд, прошедшее с начала дня.
 */
fun timeStrToSeconds(str: String): Int {
    val parts = str.split(":")
    var result = 0
    for (part in parts) {
        val number = part.toInt()
        result = result * 60 + number
    }
    return result
}

/**
 * Пример
 *
 * Дано число n от 0 до 99.
 * Вернуть его же в виде двухсимвольной строки, от "00" до "99"
 */
fun twoDigitStr(n: Int) = if (n in 0..9) "0$n" else "$n"

/**
 * Пример
 *
 * Дано seconds -- время в секундах, прошедшее с начала дня.
 * Вернуть текущее время в виде строки в формате "ЧЧ:ММ:СС".
 */
fun timeSecondsToStr(seconds: Int): String {
    val hour = seconds / 3600
    val minute = (seconds % 3600) / 60
    val second = seconds % 60
    return String.format("%02d:%02d:%02d", hour, minute, second)
}

/**
 * Пример: консольный ввод
 */
fun main() {
    println("Введите время в формате ЧЧ:ММ:СС")
    val line = readLine()
    if (line != null) {
        val seconds = timeStrToSeconds(line)
        if (seconds == -1) {
            println("Введённая строка $line не соответствует формату ЧЧ:ММ:СС")
        } else {
            println("Прошло секунд с начала суток: $seconds")
        }
    } else {
        println("Достигнут <конец файла> в процессе чтения строки. Программа прервана")
    }
}


/**
 * Средняя (4 балла)
 *
 * Дата представлена строкой вида "15 июля 2016".
 * Перевести её в цифровой формат "15.07.2016".
 * День и месяц всегда представлять двумя цифрами, например: 03.04.2011.
 * При неверном формате входной строки вернуть пустую строку.
 *
 * Обратите внимание: некорректная с точки зрения календаря дата (например, 30.02.2009) считается неверными
 * входными данными.
 */

fun dateStrToDigit(str: String): String {
    val date = str.split(' ')
    if (date.size != 3) return ""
    val month = when (date[1]) {
        "января" -> 1
        "февраля" -> 2
        "марта" -> 3
        "апреля" -> 4
        "мая" -> 5
        "июня" -> 6
        "июля" -> 7
        "августа" -> 8
        "сентября" -> 9
        "октября" -> 10
        "ноября" -> 11
        "декабря" -> 12
        else -> null
    } ?: return ""
    try {
        if (date[0].toInt() < 1 ||
            date[0].toInt() > daysInMonth(month, date[2].toInt())
        ) return ""
        return String.format("%02d.%02d.%s", date[0].toInt(), month, date[2])
    } catch (e: NumberFormatException) {
        return ""
    }
}

/**
 * Средняя (4 балла)
 *
 * Дата представлена строкой вида "15.07.2016".
 * Перевести её в строковый формат вида "15 июля 2016".
 * При неверном формате входной строки вернуть пустую строку
 *
 * Обратите внимание: некорректная с точки зрения календаря дата (например, 30 февраля 2009) считается неверными
 * входными данными.
 */
fun dateDigitToStr(digital: String): String = TODO()

/**
 * Средняя (4 балла)
 *
 * Номер телефона задан строкой вида "+7 (921) 123-45-67".
 * Префикс (+7) может отсутствовать, код города (в скобках) также может отсутствовать.
 * Может присутствовать неограниченное количество пробелов и чёрточек,
 * например, номер 12 --  34- 5 -- 67 -89 тоже следует считать легальным.
 * Перевести номер в формат без скобок, пробелов и чёрточек (но с +), например,
 * "+79211234567" или "123456789" для приведённых примеров.
 * Все символы в номере, кроме цифр, пробелов и +-(), считать недопустимыми.
 * При неверном формате вернуть пустую строку.
 *
 * PS: Дополнительные примеры работы функции можно посмотреть в соответствующих тестах.
 */
fun flattenPhoneNumber(phone: String): String {
    //проверка на наличие () и символов отличных от 0-9, ' ', + и минуса
    if (Regex("""\(\)|[^0-9- ()+]""").containsMatchIn(phone)) return ""
    var str = ""
    Regex("""[+]?[0-9]+""").findAll(phone).forEach { str += it.value }
    return str
}

/**
 * Средняя (5 баллов)
 *
 * Результаты спортсмена на соревнованиях в прыжках в длину представлены строкой вида
 * "706 - % 717 % 703".
 * В строке могут присутствовать числа, черточки - и знаки процента %, разделённые пробелами;
 * число соответствует удачному прыжку, - пропущенной попытке, % заступу.
 * Прочитать строку и вернуть максимальное присутствующее в ней число (717 в примере).
 * При нарушении формата входной строки или при отсутствии в ней чисел, вернуть -1.
 */
fun bestLongJump(jumps: String): Int {
    if (Regex("""[^0-9- %]""").containsMatchIn(jumps)) return -1
    var myMax = -1
    Regex("[0-9]+").findAll(jumps).forEach { myMax = max(myMax, it.value.toIntOrNull()!!) }
    return myMax
}

/**
 * Сложная (6 баллов)
 *
 * Результаты спортсмена на соревнованиях в прыжках в высоту представлены строкой вида
 * "220 + 224 %+ 228 %- 230 + 232 %%- 234 %".
 * Здесь + соответствует удачной попытке, % неудачной, - пропущенной.
 * Высота и соответствующие ей попытки разделяются пробелом.
 * Прочитать строку и вернуть максимальную взятую высоту (230 в примере).
 * При нарушении формата входной строки, а также в случае отсутствия удачных попыток,
 * вернуть -1.
 */
fun bestHighJump(jumps: String): Int {
    if (Regex("""[^0-9- %+]""").containsMatchIn(jumps)) return -1
    var myMax = -1
    //получаем все успешные результаты и сравниваем с максимумом подстроку до символа пробел
    Regex("[0-9]+ [+]").findAll(jumps).forEach { myMax = max(myMax, it.value.substringBefore(' ').toIntOrNull()!!) }
    return myMax
}

/**
 * Сложная (6 баллов)
 *
 * В строке представлено выражение вида "2 + 31 - 40 + 13",
 * использующее целые положительные числа, плюсы и минусы, разделённые пробелами.
 * Наличие двух знаков подряд "13 + + 10" или двух чисел подряд "1 2" не допускается.
 * Вернуть значение выражения (6 для примера).
 * Про нарушении формата входной строки бросить исключение IllegalArgumentException
 */
fun plusMinus(expression: String): Int {
    if (Regex("""[^0-9 +-]""").containsMatchIn(expression))
        throw IllegalArgumentException("Error")

    var isNum = true
    var plus = true
    var res = 0

    val numsPlusMinus = Regex("""[0-9]+|[+-]""").findAll(expression)
    for (i in numsPlusMinus) {
        val tmp = i.value

        if (((tmp != "+") && (tmp != "-") && !isNum) || ((tmp == "-") && isNum) || ((tmp == "+") && isNum))
            throw IllegalArgumentException("Error")

        if (isNum)
            when (plus) {
                true -> res += tmp.toIntOrNull()!!
                else -> res -= tmp.toIntOrNull()!!
            }
        else
            when (tmp) {
                "+" -> plus = true
                else -> plus = false
            }
        isNum = !isNum
    }
    //проверка на отсутствие в конце строки знака +/-
    if (isNum)
        throw IllegalArgumentException("Error")
    return res
}



/**
 * Сложная (6 баллов)
 *
 * Строка состоит из набора слов, отделённых друг от друга одним пробелом.
 * Определить, имеются ли в строке повторяющиеся слова, идущие друг за другом.
 * Слова, отличающиеся только регистром, считать совпадающими.
 * Вернуть индекс начала первого повторяющегося слова, или -1, если повторов нет.
 * Пример: "Он пошёл в в школу" => результат 9 (индекс первого 'в')
 */
fun firstDuplicateIndex(str: String): Int {
    val data = str.uppercase().split(' ')
    var pos = 0
    var lastWord = ""

    for (i in data.indices) {
        if (data[i] == lastWord)
            return (pos - lastWord.length - 1)
        pos += data[i].length + 1
        lastWord = data[i]
    }
    return -1
}



/**
 * Сложная (6 баллов)
 *
 * Строка содержит названия товаров и цены на них в формате вида
 * "Хлеб 39.9; Молоко 62; Курица 184.0; Конфеты 89.9".
 * То есть, название товара отделено от цены пробелом,
 * а цена отделена от названия следующего товара точкой с запятой и пробелом.
 * Вернуть название самого дорогого товара в списке (в примере это Курица),
 * или пустую строку при нарушении формата строки.
 * Все цены должны быть больше нуля либо равны нулю.
 */
fun mostExpensive(description: String): String {
    val pairs = Regex("""[^ ]+ ([0-9]+(([.]|[,])[0-9]+)?)(([;][ ]|$))""").findAll(description)

    var sz = 0
    pairs.forEach { sz += it.value.length }
    if (sz != description.length) return ""

    var max = Pair("", -1.0)
    for (i in pairs) {
        val myPair = i.value.trim(' ', ';').split(' ')
        if (max.second < myPair[1].toFloat())
            max = Pair(myPair[0], myPair[1].toDouble())
    }

    return max.first
}



/**
 * Сложная (6 баллов)
 *
 * Перевести число roman, заданное в римской системе счисления,
 * в десятичную систему и вернуть как результат.
 * Римские цифры: 1 = I, 4 = IV, 5 = V, 9 = IX, 10 = X, 40 = XL, 50 = L,
 * 90 = XC, 100 = C, 400 = CD, 500 = D, 900 = CM, 1000 = M.
 * Например: XXIII = 23, XLIV = 44, C = 100
 *
 * Вернуть -1, если roman не является корректным римским числом
 */
fun fromRoman(roman: String): Int {
    if (Regex("""[^IVXLCDM]""").containsMatchIn(roman) || roman == "") return -1
    val assoc = mapOf<Char, Int>(
        'I' to 1,
        'V' to 5,
        'X' to 10,
        'L' to 50,
        'C' to 100,
        'D' to 500,
        'M' to 1000
    )
    if (roman.length == 1) return assoc[roman[0]]!!
    if (roman.isEmpty()) return 0
    var res = assoc[roman[1]]!!
    //проверяем наличие п.2 из цикла до его начала (XL)
    res += if (res > assoc[roman[0]]!!) -assoc[roman[0]]!! else assoc[roman[0]]!!
    for (i in 2 until roman.length) {
        //обработка двух цифр, идущих подряд, сумма которых меньше i-го символа (IIV)
        if (roman[i - 1] == roman[i - 2] && 2 * assoc[roman[i - 1]]!! < assoc[roman[i]]!!)
            res = res - 4 * assoc[roman[i - 1]]!! + assoc[roman[i]]!!
        else if (assoc[roman[i - 1]]!! < assoc[roman[i]]!!) //обработка одного числа, которое меньше i-го (IV) (п.2)
            res = res - 2 * assoc[roman[i - 1]]!! + assoc[roman[i]]!!
        else
            res += assoc[roman[i]]!! //это база
    }
    return if (roman(res) == roman) res else -1
}



/**
 * Очень сложная (7 баллов)
 *
 * Имеется специальное устройство, представляющее собой
 * конвейер из cells ячеек (нумеруются от 0 до cells - 1 слева направо) и датчик, двигающийся над этим конвейером.
 * Строка commands содержит последовательность команд, выполняемых данным устройством, например +>+>+>+>+
 * Каждая команда кодируется одним специальным символом:
 *	> - сдвиг датчика вправо на 1 ячейку;
 *  < - сдвиг датчика влево на 1 ячейку;
 *	+ - увеличение значения в ячейке под датчиком на 1 ед.;
 *	- - уменьшение значения в ячейке под датчиком на 1 ед.;
 *	[ - если значение под датчиком равно 0, в качестве следующей команды следует воспринимать
 *  	не следующую по порядку, а идущую за соответствующей следующей командой ']' (с учётом вложенности);
 *	] - если значение под датчиком не равно 0, в качестве следующей команды следует воспринимать
 *  	не следующую по порядку, а идущую за соответствующей предыдущей командой '[' (с учётом вложенности);
 *      (комбинация [] имитирует цикл)
 *  пробел - пустая команда
 *
 * Изначально все ячейки заполнены значением 0 и датчик стоит на ячейке с номером N/2 (округлять вниз)
 *
 * После выполнения limit команд или всех команд из commands следует прекратить выполнение последовательности команд.
 * Учитываются все команды, в том числе несостоявшиеся переходы ("[" при значении под датчиком не равном 0 и "]" при
 * значении под датчиком равном 0) и пробелы.
 *
 * Вернуть список размера cells, содержащий элементы ячеек устройства после завершения выполнения последовательности.
 * Например, для 10 ячеек и командной строки +>+>+>+>+ результат должен быть 0,0,0,0,0,1,1,1,1,1
 *
 * Все прочие символы следует считать ошибочными и формировать исключение IllegalArgumentException.
 * То же исключение формируется, если у символов [ ] не оказывается пары.
 * Выход за границу конвейера также следует считать ошибкой и формировать исключение IllegalStateException.
 * Считать, что ошибочные символы и непарные скобки являются более приоритетной ошибкой чем выход за границу ленты,
 * то есть если в программе присутствует некорректный символ или непарная скобка, то должно быть выброшено
 * IllegalArgumentException.
 * IllegalArgumentException должен бросаться даже если ошибочная команда не была достигнута в ходе выполнения.
 *
 */
fun computeDeviceCells(cells: Int, commands: String, limit: Int): List<Int> {
    val bracketOpenMap = mutableMapOf<Int, Int>()
    val needIndex = ArrayDeque<Int>()
    //проверка корректности данных
    var steps = 0
    for (i in commands.indices) {
        when (commands[i]) {
            '[' -> {
                steps++
                needIndex.addLast(i)
            }
            ']' -> {
                steps--
                if (steps >= 0) {
                    bracketOpenMap[needIndex.last()] = i
                    needIndex.removeLast()
                } else
                    throw IllegalArgumentException("Error")
            }
            '+', '-', ' ', '<', '>' -> continue
            else -> throw IllegalArgumentException("Error")
        }
    }
    if (steps != 0) throw IllegalArgumentException("Error")


    val bracketCloseMap = bracketOpenMap.entries.associateBy({ it.value }) { it.key }
    val table = MutableList(cells) { 0 }
    steps = limit
    var pos = cells / 2
    var i = 0
    while (i < commands.length) {
        when (commands[i]) {
            '+' -> table[pos]++
            '-' -> table[pos]--
            '>' -> {
                pos++
                if (pos >= cells)
                    throw IllegalStateException("Error")
            }
            '<' -> {
                pos--
                if (pos < 0)
                    throw IllegalStateException("Error")
            }
            '[' -> {
                if (table[pos] == 0)
                    i = bracketOpenMap[i]!!
            }
            ']' -> {
                if (table[pos] != 0)
                    i = bracketCloseMap[i]!!
            }
        }
        if (steps > 1)
            steps--
        else
            return table
        ++i
    }
    return table
}

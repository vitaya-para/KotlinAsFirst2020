@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")
// Правки к замечаниям будут присланы в ближайшее время
package lesson7.task1

import ru.spbstu.wheels.NullableMonad.map
// import sun.jvm.hotspot.ui.tree.SimpleTreeNode
import java.io.File
import java.lang.StringBuilder
import java.util.*
import javax.swing.text.ParagraphView
import kotlin.math.max

// Урок 7: работа с файлами
// Урок интегральный, поэтому его задачи имеют сильно увеличенную стоимость
// Максимальное количество баллов = 55
// Рекомендуемое количество баллов = 20
// Вместе с предыдущими уроками (пять лучших, 3-7) = 55/103

/**
 * Пример
 *
 * Во входном файле с именем inputName содержится некоторый текст.
 * Вывести его в выходной файл с именем outputName, выровняв по левому краю,
 * чтобы длина каждой строки не превосходила lineLength.
 * Слова в слишком длинных строках следует переносить на следующую строку.
 * Слишком короткие строки следует дополнять словами из следующей строки.
 * Пустые строки во входном файле обозначают конец абзаца,
 * их следует сохранить и в выходном файле
 */
fun alignFile(inputName: String, lineLength: Int, outputName: String) {
    val writer = File(outputName).bufferedWriter()
    var currentLineLength = 0
    fun append(word: String) {
        if (currentLineLength > 0) {
            if (word.length + currentLineLength >= lineLength) {
                writer.newLine()
                currentLineLength = 0
            } else {
                writer.write(" ")
                currentLineLength++
            }
        }
        writer.write(word)
        currentLineLength += word.length
    }
    for (line in File(inputName).readLines()) {
        if (line.isEmpty()) {
            writer.newLine()
            if (currentLineLength > 0) {
                writer.newLine()
                currentLineLength = 0
            }
            continue
        }
        for (word in line.split(Regex("\\s+"))) {
            append(word)
        }
    }
    writer.close()
}

/**
 * Простая (8 баллов)
 *
 * Во входном файле с именем inputName содержится некоторый текст.
 * Некоторые его строки помечены на удаление первым символом _ (подчёркивание).
 * Перенести в выходной файл с именем outputName все строки входного файла, убрав при этом помеченные на удаление.
 * Все остальные строки должны быть перенесены без изменений, включая пустые строки.
 * Подчёркивание в середине и/или в конце строк значения не имеет.
 */
fun deleteMarked(inputName: String, outputName: String) {
    val writer = File(outputName).bufferedWriter()
    for (line in File(inputName).readLines())
        try {
            if (line[0] != '_')
                writer.write("$line\n")
        } catch (e: Throwable) {
            writer.write("\n")
        }
    writer.close()
}

/**
 * Средняя (14 баллов)
 *
 * Во входном файле с именем inputName содержится некоторый текст.
 * На вход подаётся список строк substrings.
 * Вернуть ассоциативный массив с числом вхождений каждой из строк в текст.
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 *
 */
private fun String.myIndexOf(line: String, from: Int): Int {
    val a = this.substring(from).indexOf(line)
    return if (a == -1) a else a + from
}

fun countSubstrings(inputName: String, substrings: List<String>): Map<String, Int> {
    val statistic = substrings.map { it to 0 }.toMutableMap()
    val input = File(inputName).readLines().toString().lowercase()
    for (i in substrings.toSet()) {
        val lCase = i.lowercase()
        var pos = input.indexOf(lCase)
        while (pos != -1) {
            statistic[i] = statistic[i]!! + 1
            pos = input.myIndexOf(lCase, pos + 1)
        }
    }
    return statistic
}

/**
 * Средняя (12 баллов)
 *
 * В русском языке, как правило, после букв Ж, Ч, Ш, Щ пишется И, А, У, а не Ы, Я, Ю.
 * Во входном файле с именем inputName содержится некоторый текст на русском языке.
 * Проверить текст во входном файле на соблюдение данного правила и вывести в выходной
 * файл outputName текст с исправленными ошибками.
 *
 * Регистр заменённых букв следует сохранять.
 *
 * Исключения (жюри, брошюра, парашют) в рамках данного задания обрабатывать не нужно
 *
 */
private fun String.syncCase(oldLine: String): String {
    val outLine = this.toMutableList()
    for (i in this.indices)
        if (oldLine[i].isUpperCase() && this[i].isLowerCase() || oldLine[i].isLowerCase() && this[i].isUpperCase())
            if (oldLine[i].isUpperCase())
                outLine[i] = outLine[i].uppercase()[0]
            else
                outLine[i] = outLine[i].lowercase()[0]
    return outLine.joinToString("")
}

fun sibilants(inputName: String, outputName: String) {

    val writer = File(outputName).bufferedWriter()
    val letters = mapOf('ы' to 'и', 'Ы' to 'И', 'я' to 'а', 'Я' to 'А', 'ю' to 'у', 'Ю' to 'У')
    val hissing = setOf('ж', 'ч', 'ш', 'щ', 'Ж', 'Ч', 'Ш', 'Щ')

    writer.use {
        for (line in File(inputName).readLines()) {
            val chars = line.toCharArray()
            for (i in 1 until chars.size) {
                if (letters.containsKey(chars[i]) && chars[i - 1] in hissing)
                    chars[i] = letters[chars[i]]!!
            }

            it.write(chars.joinToString(separator = ""))
            it.newLine()
        }
    }
}

/**
 * Средняя (15 баллов)
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 * Вывести его в выходной файл с именем outputName, выровняв по центру
 * относительно самой длинной строки.
 *
 * Выравнивание следует производить путём добавления пробелов в начало строки.
 *
 *
 * Следующие правила должны быть выполнены:
 * 1) Пробелы в начале и в конце всех строк не следует сохранять.
 * 2) В случае невозможности выравнивания строго по центру, строка должна быть сдвинута в ЛЕВУЮ сторону
 * 3) Пустые строки не являются особым случаем, их тоже следует выравнивать
 * 4) Число строк в выходном файле должно быть равно числу строк во входном (в т. ч. пустых)
 *
 */
private fun maxSize(inputName: String): Int {
    var myMax = Int.MIN_VALUE
    for (line in File(inputName).readLines())
        myMax = max(myMax, line.trim().length)
    return myMax
}

fun centerFile(inputName: String, outputName: String) {
    val writer = File(outputName).bufferedWriter()
    val myMax = maxSize(inputName)
    for (line in File(inputName).readLines())
        writer.write(buildString {
            append(padEnd((myMax - line.trim().length) / 2, ' '))
            append("${line.trim()}\n")
        })
    writer.close()
}

/**
 * Сложная (20 баллов)
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 * Вывести его в выходной файл с именем outputName, выровняв по левому и правому краю относительно
 * самой длинной строки.
 * Выравнивание производить, вставляя дополнительные пробелы между словами: равномерно по всей строке
 *
 * Слова внутри строки отделяются друг от друга одним или более пробелом.
 *
 * Следующие правила должны быть выполнены:
 * 1) Каждая строка входного и выходного файла не должна начинаться или заканчиваться пробелом.
 * 2) Пустые строки или строки из пробелов трансформируются в пустые строки без пробелов.
 * 3) Строки из одного слова выводятся без пробелов.
 * 4) Число строк в выходном файле должно быть равно числу строк во входном (в т. ч. пустых).
 *
 * Равномерность определяется следующими формальными правилами:
 * 5) Число пробелов между каждыми двумя парами соседних слов не должно отличаться более, чем на 1.
 * 6) Число пробелов между более левой парой соседних слов должно быть больше или равно числу пробелов
 *    между более правой парой соседних слов.
 *
 * Следует учесть, что входной файл может содержать последовательности из нескольких пробелов  между слвоами. Такие
 * последовательности следует учитывать при выравнивании и при необходимости избавляться от лишних пробелов.
 * Из этого следуют следующие правила:
 * 7) В самой длинной строке каждая пара соседних слов должна быть отделена В ТОЧНОСТИ одним пробелом
 * 8) Если входной файл удовлетворяет требованиям 1-7, то он должен быть в точности идентичен выходному файлу
 */
private fun getSpace(need: Int): String = "".padEnd(need, ' ')
fun alignFileByWidth(inputName: String, outputName: String) {
    val writer = File(outputName).bufferedWriter()
    val myMax = maxSize(inputName)
    for (line in File(inputName).readLines()) {
        val words = Regex("""[^ ]+""").findAll(line).map { it.value }.toList()
        var needSpace = myMax - Regex("""[^ ]""").findAll(line.trim()).count()
        var countWords = words.count() - 1
        val addLine = buildString {
            for (word in words)
                if (countWords < 1)
                    append(word)
                else {
                    append(word)
                    val logic = needSpace.toFloat() / countWords > (needSpace / countWords).toFloat()
                    append(getSpace(needSpace / countWords))
                    needSpace -= (needSpace / countWords)
                    if (logic) {
                        append(' ')
                        needSpace--
                    }
                    countWords--
                }
            append("\n")
        }
        writer.write(addLine)
    }
    writer.close()
}

/**
 * Средняя (14 баллов)
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 *
 * Вернуть ассоциативный массив, содержащий 20 наиболее часто встречающихся слов с их количеством.
 * Если в тексте менее 20 различных слов, вернуть все слова.
 * Вернуть ассоциативный массив с числом слов больше 20, если 20-е, 21-е, ..., последнее слова
 * имеют одинаковое количество вхождений (см. также тест файла input/onegin.txt).
 *
 * Словом считается непрерывная последовательность из букв (кириллических,
 * либо латинских, без знаков препинания и цифр).
 * Цифры, пробелы, знаки препинания считаются разделителями слов:
 * Привет, привет42, привет!!! -привет?!
 * ^ В этой строчке слово привет встречается 4 раза.
 *
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 * Ключи в ассоциативном массиве должны быть в нижнем регистре.
 *
 */
fun top20Words(inputName: String): Map<String, Int> = TODO()

/**
 * Средняя (14 баллов)
 *
 * Реализовать транслитерацию текста из входного файла в выходной файл посредством динамически задаваемых правил.

 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 *
 * В ассоциативном массиве dictionary содержится словарь, в котором некоторым символам
 * ставится в соответствие строчка из символов, например
 * mapOf('з' to "zz", 'р' to "r", 'д' to "d", 'й' to "y", 'М' to "m", 'и' to "yy", '!' to "!!!")
 *
 * Необходимо вывести в итоговый файл с именем outputName
 * содержимое текста с заменой всех символов из словаря на соответствующие им строки.
 *
 * При этом регистр символов в словаре должен игнорироваться,
 * но при выводе символ в верхнем регистре отображается в строку, начинающуюся с символа в верхнем регистре.
 *
 * Пример.
 * Входной текст: Здравствуй, мир!
 *
 * заменяется на
 *
 * Выходной текст: Zzdrавствуy, mир!!!
 *
 * Пример 2.
 *
 * Входной текст: Здравствуй, мир!
 * Словарь: mapOf('з' to "zZ", 'р' to "r", 'д' to "d", 'й' to "y", 'М' to "m", 'и' to "YY", '!' to "!!!")
 *
 * заменяется на
 *
 * Выходной текст: Zzdrавствуy, mир!!!
 *
 * Обратите внимание: данная функция не имеет возвращаемого значения
 */
fun transliterate(inputName: String, dictionary: Map<Char, String>, outputName: String) {
    TODO()
}

/**
 * Средняя (12 баллов)
 *
 * Во входном файле с именем inputName имеется словарь с одним словом в каждой строчке.
 * Выбрать из данного словаря наиболее длинное слово,
 * в котором все буквы разные, например: Неряшливость, Четырёхдюймовка.
 * Вывести его в выходной файл с именем outputName.
 * Если во входном файле имеется несколько слов с одинаковой длиной, в которых все буквы разные,
 * в выходной файл следует вывести их все через запятую.
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 *
 * Пример входного файла:
 * Карминовый
 * Боязливый
 * Некрасивый
 * Остроумный
 * БелогЛазый
 * ФиолетОвый

 * Соответствующий выходной файл:
 * Карминовый, Некрасивый
 *
 * Обратите внимание: данная функция не имеет возвращаемого значения
 */
fun chooseLongestChaoticWord(inputName: String, outputName: String) {

    val words = mutableMapOf<String, Int>()
    var max = 0


    for (line in File(inputName).readLines()) {
        val letters = line.lowercase().toSet()

        if (letters.size == line.length) {
            words[line] = letters.size
            max = max(max, letters.size)
        }
    }

    File(outputName).bufferedWriter().use { writer ->
        writer.write(words.filter { it.value == max }.keys.joinToString(separator = ", "))
    }

    /*
 val result = StringBuilder("")
 for ((word, count) in words) {
     if (count == max)
         result.append(if (result.isEmpty()) word else ", $word")
 }

 */
}

/**
 * Сложная (22 балла)
 *
 * Реализовать транслитерацию текста в заданном формате разметки в формат разметки HTML.
 *
 * Во входном файле с именем inputName содержится текст, содержащий в себе элементы текстовой разметки следующих типов:
 * - *текст в курсивном начертании* -- курсив
 * - **текст в полужирном начертании** -- полужирный
 * - ~~зачёркнутый текст~~ -- зачёркивание
 *
 * Следует вывести в выходной файл этот же текст в формате HTML:
 * - <i>текст в курсивном начертании</i>
 * - <b>текст в полужирном начертании</b>
 * - <s>зачёркнутый текст</s>
 *
 * Кроме того, все абзацы исходного текста, отделённые друг от друга пустыми строками, следует обернуть в теги <p>...</p>,
 * а весь текст целиком в теги <html><body>...</body></html>.
 *
 * Все остальные части исходного текста должны остаться неизменными с точностью до наборов пробелов и переносов строк.
 * Отдельно следует заметить, что открывающая последовательность из трёх звёздочек (***) должна трактоваться как "<b><i>"
 * и никак иначе.
 *
 * При решении этой и двух следующих задач полезно прочитать статью Википедии "Стек".
 *
 * Пример входного файла:
Lorem ipsum *dolor sit amet*, consectetur **adipiscing** elit.
Vestibulum lobortis, ~~Est vehicula rutrum *suscipit*~~, ipsum ~~lib~~ero *placerat **tortor***,

Suspendisse ~~et elit in enim tempus iaculis~~.
 *
 * Соответствующий выходной файл:
<html>
<body>
<p>
Lorem ipsum <i>dolor sit amet</i>, consectetur <b>adipiscing</b> elit.
Vestibulum lobortis. <s>Est vehicula rutrum <i>suscipit</i></s>, ipsum <s>lib</s>ero <i>placerat <b>tortor</b></i>.
</p>
<p>
Suspendisse <s>et elit in enim tempus iaculis</s>.
</p>
</body>
</html>
 *
 * (Отступы и переносы строк в примере добавлены для наглядности, при решении задачи их реализовывать не обязательно)
 */


fun markdownToHtmlSimple(inputName: String, outputName: String) {

    var tags = mutableListOf("<html>", "<body>", "<p>")
    val stackOfTags = ArrayDeque<String>()
    val allLines = File(inputName).readLines()
    var needEnd = true



    File(outputName).bufferedWriter().use {

        fun writeBoundaryTags() {
            for (tag in tags) {
                it.write(tag)
                it.newLine()
            }
        }

        writeBoundaryTags()

        for ((index, line) in allLines.withIndex()) {

            if (line.isBlank())
                continue


            if (index != 0 && allLines[index - 1].isBlank() && !needEnd) {
                it.write("<p>")
                it.newLine()
                needEnd = true
            }


            val symbols = line.toCharArray()
            val newLine = StringBuilder("")
            var i = 0

            fun addToNewLine(tag: String) {

                if (stackOfTags.isNotEmpty() && stackOfTags.peek() == tag) {
                    newLine.append(stackOfTags.pop().replace("<", "</"))
                } else {
                    stackOfTags.push(tag)
                    newLine.append(tag)
                }
            }

            while (i < symbols.size) {

                when (symbols[i]) {
                    '*' -> {
                        if (i + 1 < symbols.size && symbols[i + 1] == '*') {
                            addToNewLine("<b>")
                            i++
                        } else
                            addToNewLine("<i>")
                        i++
                    }

                    '~' -> {
                        if (i + 1 < symbols.size && symbols[i + 1] == '~') {
                            addToNewLine("<s>")
                            i += 2
                        }
                    }

                    else -> {
                        newLine.append(symbols[i])
                        i++
                    }
                }
            }

            it.write(newLine.toString())
            it.newLine()

            if (index != allLines.size - 1 && allLines[index + 1].isBlank() && needEnd) {
                it.write("</p>")
                it.newLine()
                needEnd = false
            }
        }


        if (needEnd) {
            it.write("</p>")
            it.newLine()
        }

        tags.replaceAll { elem -> elem.replace("<", "</") }
        tags = tags.reversed().toMutableList().subList(1, 3)

        writeBoundaryTags()
    }

}

/**
 * Сложная (23 балла)
 *
 * Реализовать транслитерацию текста в заданном формате разметки в формат разметки HTML.
 *
 * Во входном файле с именем inputName содержится текст, содержащий в себе набор вложенных друг в друга списков.
 * Списки бывают двух типов: нумерованные и ненумерованные.
 *
 * Каждый элемент ненумерованного списка начинается с новой строки и символа '*', каждый элемент нумерованного списка --
 * с новой строки, числа и точки. Каждый элемент вложенного списка начинается с отступа из пробелов, на 4 пробела большего,
 * чем список-родитель. Максимально глубина вложенности списков может достигать 6. "Верхние" списки файла начинются
 * прямо с начала строки.
 *
 * Следует вывести этот же текст в выходной файл в формате HTML:
 * Нумерованный список:
 * <ol>
 *     <li>Раз</li>
 *     <li>Два</li>
 *     <li>Три</li>
 * </ol>
 *
 * Ненумерованный список:
 * <ul>
 *     <li>Раз</li>
 *     <li>Два</li>
 *     <li>Три</li>
 * </ul>
 *
 * Кроме того, весь текст целиком следует обернуть в теги <html><body><p>...</p></body></html>
 *
 * Все остальные части исходного текста должны остаться неизменными с точностью до наборов пробелов и переносов строк.
 *
 * Пример входного файла:
///////////////////////////////начало файла/////////////////////////////////////////////////////////////////////////////
 * Утка по-пекински
 * Утка
 * Соус
 * Салат Оливье
1. Мясо
 * Или колбаса
2. Майонез
3. Картофель
4. Что-то там ещё
 * Помидоры
 * Фрукты
1. Бананы
23. Яблоки
1. Красные
2. Зелёные
///////////////////////////////конец файла//////////////////////////////////////////////////////////////////////////////
 *
 *
 * Соответствующий выходной файл:
///////////////////////////////начало файла/////////////////////////////////////////////////////////////////////////////
<html>
<body>
<p>
<ul>
<li>
Утка по-пекински
<ul>
<li>Утка</li>
<li>Соус</li>                  li - пункт
</ul>                          ol - нумерованный
</li>                          Ul - ненумерованный
<li>
Салат Оливье
<ol>
<li>Мясо
<ul>
<li>Или колбаса</li>
</ul>
</li>
<li>Майонез</li>
<li>Картофель</li>
<li>Что-то там ещё</li>
</ol>
</li>
<li>Помидоры</li>
<li>Фрукты
<ol>
<li>Бананы</li>
<li>Яблоки
<ol>
<li>Красные</li>
<li>Зелёные</li>
</ol>
</li>
</ol>
</li>
</ul>
</p>
</body>
</html>
///////////////////////////////конец файла//////////////////////////////////////////////////////////////////////////////
 * (Отступы и переносы строк в примере добавлены для наглядности, при решении задачи их реализовывать не обязательно)
 */
private fun getLevel(str: String): Int {
    val a = Regex("""^[ ]*""").find(str) ?: return -1
    return a.value.length / 4 // 4 - кол-во пробелов перед символом остановки(* или число) в уровне
}

fun markdownToHtmlLists(inputName: String, outputName: String) {
    var boundaryTags = mutableListOf("<html>", "<body>", "<p>")
    val numberedListTag = "<ol>"
    val notNumberedListTag = "<ul>"
    val tableItemTag = "<li>"
    val stack = ArrayDeque<Pair<String, Int>>()                // тег, его глубина
    val writer = File(outputName).bufferedWriter()
    val allLines = File(inputName).readLines()
    val tablePointRegex = Regex("""[\s]*([\d]+. )|([\s]*[\*][ ])""")


    fun writeBoundaryTags() {
        for (tag in boundaryTags) {
            writer.write(tag)
            writer.newLine()
        }
    }

    writeBoundaryTags()

    fun writeTypeListTag(tag: String, depth: Int) {
        writer.write(tag)
        stack.push(Pair(tag.replace("<", "</"), depth))
    }


    if (allLines[0].isNotBlank() && allLines[0].first() == '*')
        writeTypeListTag(notNumberedListTag, -1)
    else
        writeTypeListTag(numberedListTag, -1)

    writer.newLine()

    for ((index, line) in allLines.withIndex()) {

        if (index == allLines.size - 1) {

            writer.write(
                "$tableItemTag${
                    line.replace(tablePointRegex, "")
                }${tableItemTag.replace("<", "</")}"
            )
            writer.newLine()
            while (stack.isNotEmpty()) {
                writer.write(stack.pop().first)
                writer.newLine()
            }
            break
        }

        fun countDepth(line: String) =
            ((Regex("""[\s]+[\S]?""").find(line).map { it.groupValues[0] }?.length ?: 0) - 1) / 4

        val curDepth = countDepth(line)
        val nextDepth = countDepth(allLines[index + 1])



        writer.write("$tableItemTag${line.replace(tablePointRegex, "")}")


        if (nextDepth > curDepth) {
            stack.push(Pair(tableItemTag.replace("<", "</"), curDepth))
            writer.newLine()

            if (allLines[index + 1].contains(Regex("""([\d]+. )""")))
                writeTypeListTag(numberedListTag, curDepth)
            else
                writeTypeListTag(notNumberedListTag, curDepth)
            writer.newLine()
        } else {
            writer.write(tableItemTag.replace("<", "</"))
            writer.newLine()
            if (nextDepth < curDepth) {
                while (stack.isNotEmpty() && stack.peek().second >= nextDepth) {
                    writer.write(stack.pop().first)
                    writer.newLine()
                }
            }
        }

    }

    boundaryTags.replaceAll { elem -> elem.replace("<", "</") }
    boundaryTags = boundaryTags.reversed().toMutableList()
    writeBoundaryTags()


    writer.close()

}


/**
 * Очень сложная (30 баллов)
 *
 * Реализовать преобразования из двух предыдущих задач одновременно над одним и тем же файлом.
 * Следует помнить, что:
 * - Списки, отделённые друг от друга пустой строкой, являются разными и должны оказаться в разных параграфах выходного файла.
 *
 */
fun markdownToHtml(inputName: String, outputName: String) {

    // <editor-fold desc = "Переменные">
    val stackOfTextTags = ArrayDeque<String>()
    var needEnd = true
    var boundaryTags = mutableListOf("<html>", "<body>", "<p>")
    val numberedListTag = "<ol>"
    val notNumberedListTag = "<ul>"
    val tableItemTag = "<li>"
    val stackOfListTags = ArrayDeque<Pair<String, Int>>()                // тег, его глубина
    val writer = File(outputName).bufferedWriter()
    val allLines = File(inputName).readLines()
    val tablePointRegex = Regex("""[\s]*([\d]+. )""")
    // </editor-fold>

    // <editor-fold desc = "Начальные теги">
    fun writeBoundaryTags() {
        for (tag in boundaryTags) {
            writer.write(tag)
            writer.newLine()
        }
    }

    writeBoundaryTags()
    // </editor-fold>

    fun writeTypeListTag(tag: String, depth: Int) {
        writer.write(tag)
        writer.newLine()
        stackOfListTags.push(Pair(tag.replace("<", "</"), depth))
    }


    for ((index, line) in allLines.withIndex()) {

        if (line.isBlank())
            continue


        if (index != 0 && allLines[index - 1].isBlank() && !needEnd) {
            writer.write("<p>")
            writer.newLine()
            needEnd = true
        }

        val oldLine = StringBuilder()

        fun countDepth(line: String) =
            ((Regex("""[\s]+[\S]?""").find(line).map { it.groupValues[0] }?.length ?: 0) - 1) / 4

        val curDepth = countDepth(line)
        val nextDepth = countDepth(allLines[if (index < allLines.size - 1) index + 1 else index])


        if (line.contains(tablePointRegex))
            oldLine.append(line.replace(tablePointRegex, tableItemTag))
        else if (line.trim().startsWith("* "))
            oldLine.append(line.replaceFirst("* ", tableItemTag))
        else
            oldLine.append(line)

        if (line.contains(tablePointRegex) && (stackOfListTags.isNotEmpty()
                    && stackOfListTags.peek() != Pair(
                numberedListTag.replace("<", "</"),
                curDepth
            ) || stackOfListTags.isEmpty())
        )
            writeTypeListTag(numberedListTag, curDepth)

        else if (line.trim().startsWith("* ") && (stackOfListTags.isNotEmpty()
                    && stackOfListTags.peek() != Pair(
                notNumberedListTag.replace("<", "</"),
                curDepth
            ) || stackOfListTags.isEmpty())
        )
            writeTypeListTag(notNumberedListTag, curDepth)




        if (nextDepth <= curDepth) {
            if (line.contains(tablePointRegex) || line.trim().startsWith("* "))
                oldLine.append(tableItemTag.replace("<", "</"))
        }


        // <editor-fold desc = "Обрабокта тегов строки">
        val symbols = oldLine.toString().toCharArray()
        val newLine = StringBuilder("")
        var i = 0

        fun addToNewLine(tag: String) {

            if (stackOfTextTags.isNotEmpty() && stackOfTextTags.peek() == tag) {
                newLine.append(stackOfTextTags.pop().replace("<", "</"))
            } else {
                stackOfTextTags.push(tag)
                newLine.append(tag)
            }
        }

        while (i < symbols.size) {

            when (symbols[i]) {
                '*' -> {
                    if (i + 1 < symbols.size && symbols[i + 1] == '*') {
                        addToNewLine("<b>")
                        i++
                    } else
                        addToNewLine("<i>")
                    i++
                }

                '~' -> {
                    if (i + 1 < symbols.size && symbols[i + 1] == '~') {
                        addToNewLine("<s>")
                        i += 2
                    }
                }

                else -> {
                    newLine.append(symbols[i])
                    i++
                }
            }
        }

        writer.write(newLine.toString())
        writer.newLine()
        // </editor-fold>


        if (nextDepth > curDepth)
            stackOfListTags.push(Pair(tableItemTag.replace("<", "</"), nextDepth))

        if (nextDepth < curDepth) {
            while (stackOfListTags.isNotEmpty() && stackOfListTags.peek().second >= curDepth) /*nextDepth*/ {
                writer.write(stackOfListTags.pop().first)
                writer.newLine()
            }
        }


        if (index != allLines.size - 1) {
            if (allLines[index + 1].isBlank() && needEnd) {
                writer.write("</p>")
                writer.newLine()
                needEnd = false
            }
        }
    }
    while (stackOfListTags.isNotEmpty()) {
        writer.write(stackOfListTags.pop().first)
        writer.newLine()
    }


    // <editor-fold desc = "Конечные теги">
    if (needEnd) {
        writer.write("</p>")
        writer.newLine()
    }

    boundaryTags.replaceAll { elem -> elem.replace("<", "</") }
    boundaryTags = boundaryTags.reversed().toMutableList().subList(1, 3)
    writeBoundaryTags()
    // </editor-fold>

    writer.close()

}


/*
 *
 * import ru.spbstu.wheels.NullableMonad.map
import java.io.File
import java.util.ArrayDeque
import kotlin.text.StringBuilder

fun main() {

    markdownToHtml("input/IN.txt", "input/result.txt")

}

fun markdownToHtml(inputName: String, outputName: String) {


    // <editor-fold desc = "Переменные">
    val stackOfTextTags = ArrayDeque<String>()
    var needEnd = true
    var boundaryTags = mutableListOf("<html>", "<body>", "<p>")
    val numberedListTag = "<ol>"
    val notNumberedListTag = "<ul>"
    val tableItemTag = "<li>"
    val stackOfListTags = ArrayDeque<Pair<String, Int>>()                // тег, его глубина
    val writer = File(outputName).bufferedWriter()
    val allLines = File(inputName).readLines()
    val tablePointRegex = Regex("""[\s]*([\d]+. )|([\s]*[\*][ ])""")
    // </editor-fold>

    // <editor-fold desc = "Начальные теги">
    fun writeBoundaryTags() {
        for (tag in boundaryTags) {
            writer.write(tag)
            writer.newLine()
        }
    }

    writeBoundaryTags()
    // </editor-fold>

    fun writeTypeListTag(tag: String, depth: Int) {
        writer.write(tag)
        writer.newLine()
        stackOfListTags.push(Pair(tag.replace("<", "</"), depth))
    }


/*    if (allLines[0].isNotBlank() && allLines[0].first() == '*') {
//        writeTypeListTag(notNumberedListTag, -1)
//    } else if (allLines[0].isNotBlank() && allLines[0].first() in '1'..'9') {
//        writeTypeListTag(numberedListTag, -1)
//    }*/


    for ((index, line) in allLines.withIndex()) {

        if (line.isBlank())
            continue


        if (index != 0 && allLines[index - 1].isBlank() && !needEnd) {
            writer.write("<p>")
            writer.newLine()
            needEnd = true
        }

        val oldLine = StringBuilder()

        fun countDepth(line: String) =
            ((Regex("""[\s]+[\S]?""").find(line).map { it.groupValues[0] }?.length ?: 0) - 1) / 4

        val curDepth = countDepth(line)
        val nextDepth = countDepth(allLines[if (index < allLines.size - 1) index + 1 else index])


        if (line.contains(tablePointRegex))
            oldLine.append(line.replace(tablePointRegex, tableItemTag))
        else
            oldLine.append(line)


        if (nextDepth <= curDepth) {

            if (line.contains(tablePointRegex))
                oldLine.append(tableItemTag.replace("<", "</"))
        }

        if (nextDepth == curDepth) {

            if (allLines[index].contains(Regex("""([\d]+. )""")) && (stackOfListTags.isNotEmpty()
                        && stackOfListTags.peek().second != curDepth || stackOfListTags.isEmpty())
            )                                                    // в обоих - Было next
                writeTypeListTag(numberedListTag, curDepth)
            else if (allLines[index].contains(Regex("""([\s]*[\*][ ])""")) && (stackOfListTags.isNotEmpty()
                        && stackOfListTags.peek().second != curDepth || stackOfListTags.isEmpty())
            )
                writeTypeListTag(notNumberedListTag, curDepth)
        }


        // <editor-fold desc = "Обрабокта тегов строки">
        val symbols = oldLine.toString().toCharArray()
        val newLine = StringBuilder("")
        var i = 0

        fun addToNewLine(tag: String) {

            if (stackOfTextTags.isNotEmpty() && stackOfTextTags.peek() == tag) {
                newLine.append(stackOfTextTags.pop().replace("<", "</"))
            } else {
                stackOfTextTags.push(tag)
                newLine.append(tag)
            }
        }

        while (i < symbols.size) {

            when (symbols[i]) {
                '*' -> {
                    if (i + 1 < symbols.size && symbols[i + 1] == '*') {
                        addToNewLine("<b>")
                        i++
                    } else
                        addToNewLine("<i>")
                    i++
                }

                '~' -> {
                    if (i + 1 < symbols.size && symbols[i + 1] == '~') {
                        addToNewLine("<s>")
                        i += 2
                    }
                }

                else -> {
                    newLine.append(symbols[i])
                    i++
                }
            }
        }

        writer.write(newLine.toString())
        writer.newLine()
        // </editor-fold>


        if (nextDepth > curDepth) {

            stackOfListTags.push(Pair(tableItemTag.replace("<", "</"), nextDepth))

// delete this!
            if (line.contains("Kchau"))
                print(stackOfListTags)

            if (allLines[index].contains(Regex("""([\d]+. )""")) && (stackOfListTags.isNotEmpty()
                        && stackOfListTags.peek().second != curDepth || stackOfListTags.isEmpty())
            )                                                    // в обоих - Было next
                writeTypeListTag(numberedListTag, curDepth)
            else if (allLines[index].contains(Regex("""([\s]*[\*][ ])""")) && (stackOfListTags.isNotEmpty()
                        && stackOfListTags.peek().second != curDepth || stackOfListTags.isEmpty())
            )
                writeTypeListTag(notNumberedListTag, curDepth)
// delete this!

            if (index < allLines.size - 1) {
                if (allLines[index + 1].contains(Regex("""([\d]+. )""")))
                    writeTypeListTag(numberedListTag, nextDepth)
                else if (allLines[index + 1].contains(Regex("""([\s]*[\*][ ])""")))
                    writeTypeListTag(notNumberedListTag, nextDepth)
            }

        }

        if (nextDepth < curDepth) {
            while (stackOfListTags.isNotEmpty() && stackOfListTags.peek().second >= curDepth) /*nextDepth*/ {
                writer.write(stackOfListTags.pop().first)
                writer.newLine()
            }
        }


        if (index != allLines.size - 1) {
            if (allLines[index + 1].isBlank() || !line.contains(tablePointRegex)) {
                while (stackOfListTags.isNotEmpty() && stackOfListTags.peek().second >= curDepth) {
                    writer.write(stackOfListTags.pop().first)
                    writer.newLine()
                }
            }
            if (allLines[index + 1].isBlank() && needEnd) {
                writer.write("</p>")
                writer.newLine()
                needEnd = false
            }
        }
    }


    // <editor-fold desc = "Конечные теги">
    if (needEnd) {
        writer.write("</p>")
        writer.newLine()
    }

    boundaryTags.replaceAll { elem -> elem.replace("<", "</") }
    boundaryTags = boundaryTags.reversed().toMutableList().subList(1, 3)
    writeBoundaryTags()
    // </editor-fold>

    writer.close()
}
 *
 *
 *
 *
 * */
/**
 * Средняя (12 баллов)
 *
 * Вывести в выходной файл процесс умножения столбиком числа lhv (> 0) на число rhv (> 0).
 *
 * Пример (для lhv == 19935, rhv == 111):
19935
 *    111
--------
19935
+ 19935
+19935
--------
2212785
 * Используемые пробелы, отступы и дефисы должны в точности соответствовать примеру.
 * Нули в множителе обрабатывать так же, как и остальные цифры:
235
 *  10
-----
0
+235
-----
2350
 *
 */
fun printMultiplicationProcess(lhv: Int, rhv: Int, outputName: String) {
    TODO()
}


/**
 * Сложная (25 баллов)
 *
 * Вывести в выходной файл процесс деления столбиком числа lhv (> 0) на число rhv (> 0).
 *
 * Пример (для lhv == 19935, rhv == 22):
19935 | 22
-198     906
----
13
-0
--
135
-132
----
3

 * Используемые пробелы, отступы и дефисы должны в точности соответствовать примеру.
 *
 */
private fun Int.length(): Int {
    var out = 0
    var cp = this
    do {
        cp /= 10
        out++
    } while (cp > 0)
    return out
}

fun printDivisionProcess(lhv: Int, rhv: Int, outputName: String) {
    val writer = File(outputName).bufferedWriter()
    var space = 0
    val lhvLight = lhv.length()
    var minuend = 0 //уменьшаемое
    var subtrahend = 0 //вычитаемое
    var difference = 0 //разность
    for (i in 0..lhvLight) {
        if (lhv.toFloat() / 10.pow(lhvLight - i) / rhv >= 1.0) {
            minuend = lhv / 10.pow(lhvLight - i)
            subtrahend = (minuend / rhv) * rhv
            difference = minuend - subtrahend
            space = i
            break
        }
        minuend = lhv
        subtrahend = (minuend / rhv) * rhv
        difference = minuend - subtrahend
        space = lhvLight
    }
    if (subtrahend == 0 && lhv.length() > 1) {
        writer.appendLine(
            "$lhv | $rhv\n${getSpace(minuend.length() - 2)}-$subtrahend   ${lhv / rhv}\n" +
                    "".padEnd(max(minuend.length(), subtrahend.length() + 1), '-')
        )// -2 из-за строки "-0"
    } else {
        var prespace = ""
        if (minuend.length() < (subtrahend.length() + 1)) {
            prespace = " "
            space++
        }
        //val prespace = if (lhv.length() == (subtrahend + 1)) " " else ""

        writer.appendLine(
            "$prespace$lhv | $rhv\n-${subtrahend}${getSpace(lhvLight + 2 + prespace.length - subtrahend.length())}${lhv / rhv}\n" +
                    "".padEnd(max(minuend.length(), subtrahend.length() + 1), '-')
        )
    }
    for (i in lhv.toString().substring(minuend.length(), lhvLight)) {
        space++
        writer.appendLine(getSpace(space - difference.length() - 1) + "$difference$i")//-1 тк новый символ
        minuend = difference * 10 + i.toString().toInt()
        subtrahend = (minuend / rhv) * rhv
        difference = minuend - subtrahend
        writer.appendLine(
            getSpace(space - subtrahend.length() - 1) + "-" + subtrahend + "\n" +
                    getSpace(space - max(minuend.length(), subtrahend.length() + 1)) +
                    "".padEnd(max(minuend.length(), subtrahend.length() + 1), '-')
        ) //-1 из-за символа "-"
    }
    writer.appendLine(
        getSpace(space - difference.length()) + difference
    )
    writer.close()
}


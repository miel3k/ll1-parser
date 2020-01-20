import java.io.File

fun main() {
    runSingle()
}

fun runSingle() {
    val input = readTextFromFile("data/input.txt")
    val inputList = input.chunked(1).toMutableList()
    inputList.add("E")
    val result = LL1Parser(inputList).parse()
    println("Input: $input")
    println("Result: $result")
}

fun readTextFromFile(path: String) =
    File(path).inputStream().bufferedReader().use {
        it.readText()
    }
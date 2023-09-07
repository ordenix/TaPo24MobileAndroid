package pl.tapo24.utils

class Regex {


    fun code(input: String): Boolean {
        val regex = Regex(pattern = "[A-Za-z]\\d\\d|[A-Za-z]+-\\d\\d", options = setOf(RegexOption.IGNORE_CASE))
        return regex.matches(input)
    }
}
package pl.tapo24.twa.utils

class RegexTariff {

//https://regex-generator.olafneumann.org/?sampleText=b02&flags=i&selection=1%7CNumber,0%7CMultiple%20characters
//https://regexr.com/
    fun code(input: String): Boolean {
        val regex = Regex(pattern = "[A-Za-z]\\d\\d", options = setOf(RegexOption.IGNORE_CASE))
        return regex.matches(input)
    }

    // code patern B01 (xxx)
    fun sign(input: String): Boolean {
        val regex = Regex(pattern = "[A-Za-z]+-[0-9]+[0-9]|[A-Za-z]+-\\d", options = setOf(RegexOption.IGNORE_CASE))
        return regex.matches(input)
    }

// sign patern b-1 (x-x)
}
import Conversion.*

typealias DoubleConversion = (Double) -> Double

enum class Conversion {
    CentigradeToFahrenheit,
    KgsToPounds,
    PoundsToUsTons
}

fun convert(x: Double, converter: DoubleConversion): Double {
    val result = converter(x)
    println("$x is converted to $result")

    return result
}

fun getConversionLambda(conversion: Conversion): DoubleConversion {
    when (conversion) {
        CentigradeToFahrenheit -> return { it * 1.8 + 32 }
        KgsToPounds -> return { it * 2.204623 }
        PoundsToUsTons -> return { it / 2000 }
        else -> return { it }
    }
}

fun combine(lambda1: DoubleConversion, lambda2: DoubleConversion): DoubleConversion =
    { x: Double -> lambda2(lambda1(x)) }

fun main() {
    println("2.5 Kgs to pounds = ${getConversionLambda(KgsToPounds)(2.5)}")

    val kgsToPoundsLambda = getConversionLambda(KgsToPounds)
    val poundsToUsTonsLambda = getConversionLambda(PoundsToUsTons)

    val kgsToUsTonsLambdas = combine(kgsToPoundsLambda, poundsToUsTonsLambda)

    val v = 17.4

    println("$v Kgs to US Tons = ${convert(v, kgsToUsTonsLambdas)}")
}
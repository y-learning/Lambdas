import Conversion.*

typealias DoubleConversion = (Double) -> Double

fun <E> rest(): (Collection<E>) -> List<E> = { it.drop(1) }

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

val square = { n: Int -> n * n }

val triple = { n: Int -> n * 3 }

fun <T, U, V> compose(f: (U) -> V, g: (T) -> U): (T) -> V = { T -> f(g(T)) }

val squareOfTriple = compose(square, triple)

val add: (Int) -> IntToInt = { a -> { b -> a + b } }

typealias IntToInt = (Int) -> Int

fun <T, U, V> composeV3(): ((U) -> V) -> (((T) -> U) -> ((T) -> V)) =
    { x ->
        { y ->
            { z ->
                x(y(z))
            }
        }
    }

val composeV2: (IntToInt) -> (IntToInt) -> IntToInt =
    { x -> { y -> { z -> x(y(z)) } } }

fun main() {
    println("2.5 Kgs to pounds = ${getConversionLambda(KgsToPounds)(2.5)}")

    val kgsToPoundsLambda = getConversionLambda(KgsToPounds)
    val poundsToUsTonsLambda = getConversionLambda(PoundsToUsTons)

    val kgsToUsTonsLambdas = combine(kgsToPoundsLambda, poundsToUsTonsLambda)

    val v = 17.4

    println("$v Kgs to US Tons = ${convert(v, kgsToUsTonsLambdas)}")

    println(rest<Int>()(listOf(1, 5, 6, 47, 2, 6, 3, 4)))

    println("squareOfTriple = ${squareOfTriple(3)}")
}
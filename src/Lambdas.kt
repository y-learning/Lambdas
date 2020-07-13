import Conversion.*
import java.lang.IllegalArgumentException

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
    { f ->
        { g ->
            { x ->
                f(g(x))
            }
        }
    }

fun <T, U, V> higherAndThen(): ((T) -> U) -> (((U) -> V) -> ((T) -> V)) =
    { f ->
        { g ->
            { x ->
                g(f(x))
            }
        }
    }

val composeV2: (IntToInt) -> (IntToInt) -> IntToInt =
    { x -> { y -> { z -> x(y(z)) } } }

val addTax = { tax: Double ->
    { price: Double ->
        price + price * tax
    }
}

val tc5 = addTax(5.0)

fun <A, B, C> partialA(a: A, f: (A) -> (B) -> C): (B) -> C = f(a)

fun <A, B, C> partialB(b: B, f: (A) -> (B) -> C): (A) -> C = { a: A -> f(a)(b) }

fun <A, B, C, D> func(a: A, b: B, c: C, d: D): String = "$a $b $c $d"

fun <A, B, C, D> funCurried(): (A) -> (B) -> (C) -> (D) -> String =
    { a ->
        { b ->
            { c ->
                { d ->
                    func(a, b, c, d)
                }
            }
        }
    }

fun <A, B, C> curry(f: (A, B) -> C): (A) -> (B) -> C =
    { a: A ->
        { b: B ->
            f(a, b)
        }
    }

fun <T, U, V> taxOnly(f: (T) -> (U) -> V): (U) -> (T) -> V =
    { u: U ->
        { t: T ->
            f(t)(u)
        }
    }

data class Price private constructor(private val value: Double) {
    override fun toString(): String = value.toString()

    operator fun plus(price: Price) = Price(this.value + price.value)

    operator fun times(n: Int) = Price(this.value * n)

    companion object {
        val identity = Price(0.0)

        operator fun invoke(value: Double) =
            if (value > 0)
                Price(value)
            else
                throw IllegalArgumentException("Price must be Positive")

    }
}

data class Weight(val value: Double) {
    operator fun plus(weight: Weight) = Weight(this.value + weight.value)
}

val priceAddition = { x: Price, y: Price -> x + y }
val weightAddition = { x: Weight, y: Weight -> x + y }

val zeroPrice = Price.identity
val zeroWeight = Weight(0.0)

fun sumByPrice(prices: List<Price>) =
    prices.fold(zeroPrice, priceAddition)

fun sumByWeight(weights: List<Weight>) =
    weights.fold(zeroWeight, weightAddition)

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

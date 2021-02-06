package uz.star.testforanymobile.utils

/**
 * Created by Farhod Tohirov on 04-Feb-21
 **/

typealias SingleBlock <T> = (T) -> Unit
typealias DoubleBlock <T, E> = (T, E) -> Unit
typealias TrialBlock <T, E, F> = (T, E, F) -> Unit
typealias EmptyBlock = () -> Unit

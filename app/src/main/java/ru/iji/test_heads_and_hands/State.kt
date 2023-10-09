package ru.iji.test_heads_and_hands

sealed class State {

    object GameIsActive : State()

    object GameIsPaused : State()

    object GameOver : State()
}

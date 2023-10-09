package ru.iji.test_heads_and_hands.domain

interface Creature {
    val name: String
    val health: Int
    val attack: Int
    val damage: Int
    val protection: Int
    val recovery: Int
}
package ru.iji.test_heads_and_hands.domain.models

import ru.iji.test_heads_and_hands.domain.Creature

data class MonsterModel(
    override val name: String = "Волан-Де-Морт",
    override val health: Int = 100,
    override val attack: Int = (1..30).random(),
    override val damage: Int = (1..6).random(),
    override val protection: Int = (1..30).random(),
    override val recovery: Int = 4
) : Creature
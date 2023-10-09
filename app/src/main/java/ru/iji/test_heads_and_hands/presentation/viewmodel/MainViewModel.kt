package ru.iji.test_heads_and_hands.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.iji.test_heads_and_hands.State
import ru.iji.test_heads_and_hands.domain.Creature
import ru.iji.test_heads_and_hands.domain.models.MonsterModel
import ru.iji.test_heads_and_hands.domain.models.PlayerModel

class MainViewModel : ViewModel() {

    private val numbers = MutableStateFlow<List<Int>>(emptyList())
    private val attempts = MutableStateFlow(0)

    private val _player = MutableStateFlow<Creature>(PlayerModel())
    val player = _player.asStateFlow()

    private val _monster = MutableStateFlow<Creature>(MonsterModel())
    val monster = _monster.asStateFlow()

    private val _state = MutableStateFlow<State>(State.GameIsActive)
    val state = _state.asStateFlow()

    fun newGame() {
        _player.value = PlayerModel()
        _monster.value = MonsterModel()
        _state.value = State.GameIsActive
    }

    fun calculateAttackModifier(creature: Creature = _monster.value) {
        when (creature) {
            is PlayerModel -> {
                _state.value = State.GameIsPaused
                attempts.value = (_monster.value.protection - creature.attack) + 1
                getNumbers()

                if (numbers.value.any { it == 5 || it == 6 })
                    makeAttack(creature)
                else
                    calculateAttackModifier()
            }

            is MonsterModel -> {
                viewModelScope.launch {
                    delay(1000)
                    healYourself()
                    attempts.value = (_player.value.protection - creature.attack) + 1
                    getNumbers()

                    if (numbers.value.any { it == 5 || it == 6 })
                        makeAttack()
                    else
                        _state.value = State.GameIsActive
                }
            }
        }
    }

    private fun healYourself(creature: Creature = _monster.value) {
        if (creature.recovery > 0 && creature.health + MAX_HEAL <= MAX_HEALTH)
            when (creature) {
                is PlayerModel -> {
                    _player.value = (_player.value as PlayerModel).copy(
                        health = creature.health + MAX_HEAL,
                        recovery = creature.recovery - 1
                    )
                }

                is MonsterModel -> {
                    if (CHANCE.random())
                        _monster.value = (_monster.value as MonsterModel).copy(
                            health = creature.health + MAX_HEAL,
                            recovery = creature.recovery - 1
                        )
                }
            }
    }

    private fun makeAttack(creature: Creature = _monster.value) {
        when (creature) {
            is PlayerModel -> {
                _monster.value = (_monster.value as MonsterModel).copy(
                    health = _monster.value.health - creature.damage
                )
                if (_monster.value.health <= 0)
                    _state.value = State.GameOver
                else
                    calculateAttackModifier()
            }

            is MonsterModel -> {
                _player.value = (_player.value as PlayerModel).copy(
                    health = _player.value.health - creature.damage
                )
                if (_player.value.health <= 0)
                    _state.value = State.GameOver
                else
                    _state.value = State.GameIsActive
            }
        }
    }

    private fun getNumbers() {
        numbers.value = emptyList()
        do {
            attempts.value = attempts.value - ATTEMPT
            numbers.value = numbers.value.plus(RANGE.random())
        } while (attempts.value > ATTEMPTS_ENDED)
    }

    private companion object {
        const val ATTEMPTS_ENDED = 0
        const val ATTEMPT = 1
        const val MAX_HEAL = 30
        const val MAX_HEALTH = 100
        val RANGE = (1..6)
        val CHANCE = listOf(true, false)
    }
}
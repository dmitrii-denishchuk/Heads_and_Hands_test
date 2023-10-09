package ru.iji.test_heads_and_hands.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.iji.test_heads_and_hands.State
import ru.iji.test_heads_and_hands.domain.Creature
import ru.iji.test_heads_and_hands.presentation.viewmodel.MainViewModel

@Composable
fun MainScreen() {

    // ViewModel
    val mainViewModel: MainViewModel = viewModel()

    // Creatures
    val player by mainViewModel.player.collectAsState(null)
    val monster by mainViewModel.monster.collectAsState(null)

    // Observable state
    val state by mainViewModel.state.collectAsState(State.GameIsActive)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            monster?.let {
                if (it.health <= 0)
                    DeadCreature(it)
                else
                    LivingCreature(
                        creature = it,
                        icon = Icons.Rounded.Face,
                        recover = {}
                    )
            }

            player?.let {
                if (it.health <= 0)
                    DeadCreature(it)
                else
                    LivingCreature(
                        creature = it,
                        icon = Icons.Rounded.Person,
                        recover = {}
                    )
            }
        }
    }

    PlayGameButton(
        state = state,
        action = {
            if (state == State.GameOver) mainViewModel.newGame()
            else player?.let { mainViewModel.calculateAttackModifier(it) }
        }
    )
}

@Composable
fun LivingCreature(
    creature: Creature,
    icon: ImageVector,
    recover: () -> Unit
) {
    Column(
        modifier = Modifier
            .height(150.dp)
            .width(320.dp)
            .background(Color.LightGray),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "",
                tint = Color.Black
            )

            Text(
                modifier = Modifier.padding(10.dp),
                fontWeight = FontWeight.Bold,
                text = creature.name
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = Icons.Rounded.Favorite,
                        contentDescription = "",
                        tint = Color.Red
                    )
                }

                Text(
                    fontWeight = FontWeight.Bold,
                    text = "health ${creature.health}"
                )
            }

            Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = Icons.Rounded.ThumbUp,
                        contentDescription = "",
                        tint = Color.Yellow
                    )
                }

                Text(
                    fontWeight = FontWeight.Bold,
                    text = "damage ${creature.damage}"
                )
            }

            Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = { recover() }
                ) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = Icons.Rounded.AddCircle,
                        contentDescription = "",
                        tint = if (creature.recovery > 0) Color.Green else Color.DarkGray
                    )
                }

                Text(
                    fontWeight = FontWeight.Bold,
                    text = "recoveries ${creature.recovery}"
                )
            }
        }
    }
}

@Composable
fun DeadCreature(creature: Creature) {
    Column(
        modifier = Modifier
            .height(150.dp)
            .width(320.dp)
            .background(Color.Red),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(100.dp),
                imageVector = Icons.Rounded.Close,
                contentDescription = "",
                tint = Color.Black
            )
        }

        Text(
            modifier = Modifier.padding(10.dp),
            fontWeight = FontWeight.Bold,
            text = creature.name
        )
    }
}

@Composable
fun PlayGameButton(
    state: State?,
    action: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        state?.let {
            Button(
                onClick = { action() },
                enabled = it != State.GameIsPaused,
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = when (it) {
                        State.GameIsActive -> "Бросить кубики"
                        State.GameIsPaused -> "Ход противника"
                        State.GameOver -> "Начать новую игру"
                    },
                    modifier = Modifier.padding(6.dp)
                )
            }
        }
    }
}
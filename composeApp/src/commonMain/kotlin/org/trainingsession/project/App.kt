package org.trainingsession.project

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.trainingsession.project.actual.AppLogger
import org.trainingsession.project.mainScreen.AppRoutes
import org.trainingsession.project.mainScreen.composables.ExercisePresentation
import org.trainingsession.project.mainScreen.composables.WorkoutProgramPresentation
import org.trainingsession.project.mainScreen.screens.ProgramSelectionScreen
import org.trainingsession.project.mainScreen.screens.WorkoutPlayerScreen
import org.trainingsession.project.mainScreen.theme.AppTheme
import org.trainingsession.project.mainScreen.viewModels.ProgramsScreenViewModel

@Composable
@Preview
fun App() {
    AppLogger.d("Logger-Android", "Started")

    val theme = remember { mutableStateOf(false) }
    val navController = rememberNavController()

    AppTheme(
        darkTheme = theme.value
    ) {
        Surface(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            contentColor = Color.Transparent
        ) {
            var selectedProgram by remember {
                mutableStateOf<WorkoutProgramPresentation?>(
                    WorkoutProgramPresentation(
                        3,
                        "Дыхательные практики",
                        "Техники для снятия стресса и концентрации",
                        listOf(
                            ExercisePresentation(
                                "Диафрагмальное дыхание",
                                60,
                                "Глубокий вдох животом"
                            ),
                            ExercisePresentation(
                                "Квадратное дыхание",
                                90,
                                "Вдох-задержка-выдох-задержка"
                            ),
                            ExercisePresentation(
                                "Расслабляющее дыхание",
                                120,
                                "Медленный выдох через рот"
                            )
                        )
                    )
                )
            }

            LaunchedEffect(Unit) {
                while (true) {
                    delay(5000)
                    theme.value = !theme.value
                }
            }

            NavHost(
                modifier = Modifier.safeContentPadding().fillMaxSize(),
                navController = navController,
                startDestination = AppRoutes.ProgramListScreen
            ) {

                composable<AppRoutes.ProgramListScreen> {
                    ProgramSelectionScreen(
                        onProgramSelected = { navController.navigate(AppRoutes.ChosenProgramScreen) }
                    )
                }

                composable<AppRoutes.ChosenProgramScreen> {
                    WorkoutPlayerScreen(
                        program = selectedProgram!!,
                        onBackClick = {
                            navController.navigate(AppRoutes.ProgramListScreen)
                        }
                    )
                }
            }
        }
    }
}

fun initKoin() = startKoin {
    modules(appModule)
}

val appModule = module {
    viewModel { ProgramsScreenViewModel() }
}
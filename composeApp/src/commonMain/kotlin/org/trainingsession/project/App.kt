package org.trainingsession.project

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.trainingsession.project.presentation.AppRoutes
import org.trainingsession.project.presentation.screens.ProgramSelectionScreen
import org.trainingsession.project.presentation.screens.WorkoutPlayerScreen
import org.trainingsession.project.presentation.theme.AppTheme
import org.trainingsession.project.presentation.viewModels.WorkoutScreenViewModel
import org.trainingsession.project.utils.AppLogger

@Composable
@Preview
fun App() {
    val platform = koinInject<Platform>()
    AppLogger.d(platform.name, "Was initialized")

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
                        onProgramSelected = {
                            navController.navigate(
                                AppRoutes.ChosenProgramScreen(
                                    it.id
                                )
                            )
                        }
                    )
                }

                composable<AppRoutes.ChosenProgramScreen> { backstack ->
                    val route: AppRoutes.ChosenProgramScreen = backstack.toRoute()
                    val viewModel: WorkoutScreenViewModel = koinViewModel(
                        parameters = { parametersOf(1) }
                    )
                    val state = viewModel.state.collectAsStateWithLifecycle()

                    WorkoutPlayerScreen(
                        onBackClick = {
                            navController.navigate(AppRoutes.ProgramListScreen)
                        },
                        screenState = state,
                        event = viewModel::event
                    )
                }
            }
        }
    }
}

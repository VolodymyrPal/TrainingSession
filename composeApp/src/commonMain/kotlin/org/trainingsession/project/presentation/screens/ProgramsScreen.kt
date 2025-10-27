package org.trainingsession.project.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.trainingsession.project.presentation.composables.ProgramCard
import org.trainingsession.project.presentation.models.ExercisePresentation
import org.trainingsession.project.presentation.models.WorkoutProgramPresentation
import org.trainingsession.project.presentation.viewModels.ProgramsScreenViewModel
import org.trainingsession.project.utils.AppLogger
import trainingsession.composeapp.generated.resources.Res
import trainingsession.composeapp.generated.resources.select_program

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgramSelectionScreen(
    onProgramSelected: (WorkoutProgramPresentation) -> Unit,
    viewModel: ProgramsScreenViewModel = koinViewModel<ProgramsScreenViewModel>()
) {
    val programs = remember { programs }

    LaunchedEffect(Unit) {
        AppLogger.d("Programs from viewModel", "${viewModel.getPrograms()}")
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.select_program),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )
        },
        containerColor = Color.Transparent,
        contentColor = Color.Transparent
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(programs) { program ->
                ProgramCard(
                    program = program,
                    onStartClick = { onProgramSelected(program) }
                )
            }
        }
    }
}

val programs = listOf(
    WorkoutProgramPresentation(
        id = 1,
        name = "Йога для начинающих",
        description = "Мягкие асаны для гибкости и расслабления",
        exercisePresentations = listOf(
            ExercisePresentation("Поза горы", 30, "Встаньте прямо, стопы вместе"),
            ExercisePresentation("Поза дерева", 45, "Баланс на одной ноге"),
            ExercisePresentation("Поза ребёнка", 60, "Расслабление и отдых")
        )
    ),
    WorkoutProgramPresentation(
        id = 2,
        name = "Растяжка всего тела",
        description = "15-минутная программа для улучшения гибкости",
        exercisePresentations = listOf(
            ExercisePresentation("Растяжка шеи", 30, "Медленные наклоны головы"),
            ExercisePresentation("Растяжка плеч", 45, "Круговые движения руками"),
            ExercisePresentation("Наклоны вперёд", 60, "Тянитесь к носкам")
        )
    ),
    WorkoutProgramPresentation(
        id = 3,
        name = "Дыхательные практики",
        description = "Техники для снятия стресса и концентрации",
        exercisePresentations = listOf(
            ExercisePresentation("Диафрагмальное дыхание", 60, "Глубокий вдох животом"),
            ExercisePresentation("Квадратное дыхание", 90, "Вдох-задержка-выдох-задержка"),
            ExercisePresentation("Расслабляющее дыхание", 120, "Медленный выдох через рот")
        )
    ),
    WorkoutProgramPresentation(
        id = 4,
        name = "Утренняя зарядка",
        description = "Энергичные упражнения для бодрого начала дня",
        exercisePresentations = listOf(
            ExercisePresentation("Прыжки со звездой", 30, "Активные прыжки"),
            ExercisePresentation("Приседания", 45, "20 повторений"),
            ExercisePresentation("Планка", 30, "Держите корпус прямо")
        )
    )
)
package org.trainingsession.project.mainScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import trainingsession.composeapp.generated.resources.Res
import trainingsession.composeapp.generated.resources.arrow_back

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgramSelectionScreen(
    onProgramSelected: (WorkoutProgramPresentation) -> Unit
) {
    val programs = remember {
        listOf(
            WorkoutProgramPresentation(
                1,
                "Йога для начинающих",
                "Мягкие асаны для гибкости и расслабления",
                Res.drawable.arrow_back,
                listOf(
                    ExercisePresentation("Поза горы", 30, "Встаньте прямо, стопы вместе"),
                    ExercisePresentation("Поза дерева", 45, "Баланс на одной ноге"),
                    ExercisePresentation("Поза ребёнка", 60, "Расслабление и отдых")
                )
            ),
            WorkoutProgramPresentation(
                2,
                "Растяжка всего тела",
                "15-минутная программа для улучшения гибкости",
                Res.drawable.arrow_back,
                listOf(
                    ExercisePresentation("Растяжка шеи", 30, "Медленные наклоны головы"),
                    ExercisePresentation("Растяжка плеч", 45, "Круговые движения руками"),
                    ExercisePresentation("Наклоны вперёд", 60, "Тянитесь к носкам")
                )
            ),
            WorkoutProgramPresentation(
                3,
                "Дыхательные практики",
                "Техники для снятия стресса и концентрации",
                Res.drawable.arrow_back,
                listOf(
                    ExercisePresentation("Диафрагмальное дыхание", 60, "Глубокий вдох животом"),
                    ExercisePresentation("Квадратное дыхание", 90, "Вдох-задержка-выдох-задержка"),
                    ExercisePresentation("Расслабляющее дыхание", 120, "Медленный выдох через рот")
                )
            ),
            WorkoutProgramPresentation(
                4,
                "Утренняя зарядка",
                "Энергичные упражнения для бодрого начала дня",
                Res.drawable.arrow_back,
                listOf(
                    ExercisePresentation("Прыжки со звездой", 30, "Активные прыжки"),
                    ExercisePresentation("Приседания", 45, "20 повторений"),
                    ExercisePresentation("Планка", 30, "Держите корпус прямо")
                )
            )
        )
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Выбери тренировку",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
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
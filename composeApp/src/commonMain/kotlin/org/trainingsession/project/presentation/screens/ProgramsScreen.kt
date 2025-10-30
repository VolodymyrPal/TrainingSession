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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.trainingsession.project.presentation.composables.ProgramCard
import org.trainingsession.project.presentation.models.WorkoutProgramPresentation
import org.trainingsession.project.presentation.viewModels.ProgramsScreenViewModel
import trainingsession.composeapp.generated.resources.Res
import trainingsession.composeapp.generated.resources.select_program

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgramSelectionScreen(
    onProgramSelected: (WorkoutProgramPresentation) -> Unit,
    viewModel: ProgramsScreenViewModel = koinViewModel<ProgramsScreenViewModel>()
) {
    val screenState = viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(), topBar = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.select_program),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )
        }, containerColor = Color.Transparent, contentColor = Color.Transparent
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(screenState.value.programs) { program ->
                ProgramCard(
                    program = program, onStartClick = { onProgramSelected(program) })
            }
        }
    }
}
package org.trainingsession.project.domain.repository

import org.trainingsession.project.domain.models.ProgramExercise
import org.trainingsession.project.domain.models.Program

actual interface ProgramRepository {
    actual fun getPrograms(): List<Program>
    actual fun getProgram(id: Int): Program
    actual fun getExercises(program: Program): ProgramExercise
}
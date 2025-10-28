package org.trainingsession.project.domain.repository

import org.trainingsession.project.domain.models.Exercise
import org.trainingsession.project.domain.models.Program

actual interface ProgramRepository {
    actual fun getPrograms(): List<Program>
    actual fun getExercises(program: Program): Exercise
}
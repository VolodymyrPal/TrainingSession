package org.trainingsession.project.domain.repository

import org.trainingsession.project.domain.models.Exercise
import org.trainingsession.project.domain.models.Program

interface ProgramRepository {
    fun getPrograms(): List<Program>
    fun getExercises(program: Program) : Exercise
}
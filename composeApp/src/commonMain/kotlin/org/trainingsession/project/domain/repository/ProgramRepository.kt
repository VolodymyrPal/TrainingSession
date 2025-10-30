package org.trainingsession.project.domain.repository

import org.trainingsession.project.domain.models.ProgramExercise
import org.trainingsession.project.domain.models.Program

expect interface ProgramRepository {
    fun getPrograms(): List<Program>
    fun getProgram(id: Int): Program
    fun getExercises(program: Program) : ProgramExercise
}
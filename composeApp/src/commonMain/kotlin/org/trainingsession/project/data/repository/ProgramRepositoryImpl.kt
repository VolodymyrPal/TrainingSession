package org.trainingsession.project.data.repository

import org.koin.core.annotation.Single
import org.trainingsession.project.domain.models.Exercise
import org.trainingsession.project.domain.models.Program
import org.trainingsession.project.domain.repository.ProgramRepository

@Single(binds = [ProgramRepository::class])
class ProgramRepositoryImpl (

) : ProgramRepository {
    override fun getPrograms(): List<Program> {
        return emptyList()
    }

    override fun getExercises(program: Program): Exercise {
        return Exercise("Test", 20, "Description")
    }

}
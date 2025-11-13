package org.trainingsession.project.data.repository

import org.koin.core.annotation.Single
import org.trainingsession.project.domain.models.Program
import org.trainingsession.project.domain.models.Exercise
import org.trainingsession.project.domain.models.provideProgramExercise
import org.trainingsession.project.domain.models.providePrograms
import org.trainingsession.project.domain.repository.ProgramRepository

@Single(binds = [ProgramRepository::class])
class ProgramRepositoryImpl(

) : ProgramRepository {
    override fun getPrograms(): List<Program> {
        return providePrograms()
    }

    override fun getProgram(id: Int): Program {
        return providePrograms()[id.coerceIn(0, providePrograms().size)] //TODO
    }

    override fun getExercises(program: Program): Exercise {
        return provideProgramExercise()
    }

}
package org.trainingsession.project.domain.models.muscles

sealed interface AccessoryMuscleAndStabilizer : MuscleGroup {
    object ROTATOR_CUFF : AccessoryMuscleAndStabilizer
    object NECK : AccessoryMuscleAndStabilizer
    object FOREARMS_FLEXORS_EXTENSORS : AccessoryMuscleAndStabilizer
    object GRIP_MUSCLES : AccessoryMuscleAndStabilizer
}
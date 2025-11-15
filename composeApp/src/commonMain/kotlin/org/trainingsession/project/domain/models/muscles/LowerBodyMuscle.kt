package org.trainingsession.project.domain.models.muscles

sealed interface LowerBodyMuscle : MuscleGroup {
    object QUADRICEPS : LowerBodyMuscle
    object GLUTES : LowerBodyMuscle
    object HAMSTRINGS : LowerBodyMuscle
    object ADDUCTORS : LowerBodyMuscle
    object GASTROCNEMIUS : LowerBodyMuscle
    object SOLEUS : LowerBodyMuscle
    object TIBIALIS_ANTERIOR : LowerBodyMuscle
    object HIP_FLEXORS : LowerBodyMuscle
}
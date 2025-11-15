package org.trainingsession.project.domain.models.muscles

sealed interface CoreMuscle : MuscleGroup {
    object ABS_RECTUS : CoreMuscle
    object ABS_OBLIQUES : CoreMuscle
    object ABS_TRANSVERSE : CoreMuscle
    object QUADRATUS_LUMBORUM : CoreMuscle
    object PELVIC_FLOOR : CoreMuscle
    object DIAPHRAGM : CoreMuscle
    object MULTIFIDUS : CoreMuscle
}
package org.trainingsession.project.domain.models

import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

data class Program(
    val id: Int,
    val name: String,
    val description: String,
    val exercises: List<Exercise>,

    val exerciseType: String = "Base program",
    val uxRecommendation: String = "Mate it slow",
    val programGoal: String = "",
    val durationTotal: Duration = 9.minutes,
    val restInterval: Duration = 15.seconds,
)

fun providePrograms(): List<Program> {
    return listOf(program1, program2, program3)
}

fun provideRandomProgram(): Program {
    return providePrograms().random()
}

fun provideProgramExercise(): Exercise {
    return provideRandomProgram().exercises.random()
}


val program1 = Program(
    id = 1,
    name = "Мобильность и Динамический Разогрев",
    description = "Программа для активации суставов и подготовки тела к движению. Фокус на динамической растяжке и подвижности позвоночника.",
    programGoal = "Улучшение подвижности и общей гибкости тела.",
    exerciseType = "Мобильность (Dynamic Warmup)",
    uxRecommendation = "Акцент на плавности движений. Голосовое оповещение о следующем упражнении за 3-5 секунд.",
    durationTotal = 9.minutes,
    restInterval = 10.seconds,
    exercises = listOf(
        Exercise(
            id = 101, // Уникальный ID
            name = "Марш на месте с подъемом коленей",
            duration = 50.seconds,
            description = Description("Общий разогрев, активация тазобедренных суставов."),
//            primaryFocus = "Тазобедренные суставы, Общее кровообращение",
            position = ExercisePosition.STAY_UP,
            instruction = ExerciseInstruction(
                keyCues = "Плавно шагайте, соблюдая синхронизацию рук и ног.",
                safetyFocus = SafetyFocus(
                    corePrinciple = "Избегайте резких движений. Низкоударная нагрузка.",
                    modification = null,
                    biomechanicalFocus = "Регулируйте темп и сложность скоростью работы ног и рук."
                ),
                preemptiveCue = "Приготовьтесь к вращению рук."
            )
        ),
        Exercise(
            id = 102, // Уникальный ID
            name = "Вращение рук в плечах",
            duration = 50.seconds,
            description = Description("Проработка плечевого пояса и суставной жидкости."),
//            primaryFocus = "Плечевой пояс, Спина",
            position = ExercisePosition.STAY_UP,
            instruction = ExerciseInstruction(
                keyCues = "25 секунд вперед, 25 секунд назад. Максимальная контролируемая амплитуда.",
                safetyFocus = SafetyFocus(
                    corePrinciple = "Контролируйте амплитуду.",
                    modification = null,
                    biomechanicalFocus = "Не делайте рывков."
                ),
                preemptiveCue = "Приготовьтесь к динамической растяжке квадрицепса."
            )
        ),
        Exercise(
            id = 103, // Уникальный ID
            name = "Динамическая растяжка квадрицепса",
            duration = 50.seconds,
            description = Description("Динамическая активация мышц бедра и сгибателей таза."),
//            primaryFocus = "Квадрицепсы, Сгибатели таза",
            position = ExercisePosition.STAY_UP,
            instruction = ExerciseInstruction(
                keyCues = "Чередуйте ноги. Приложите пятку к ягодице, потянитесь свободной рукой вверх.",
                safetyFocus = SafetyFocus(
                    corePrinciple = "Используйте только динамические движения.",
                    modification = null,
                    biomechanicalFocus = "Избегайте длительной статической растяжки."
                ),
                preemptiveCue = "Приготовьтесь к наклонам корпуса в стороны."
            )
        ),
        Exercise(
            id = 104, // Уникальный ID
            name = "Наклоны корпуса в стороны (Side bends)",
            duration = 50.seconds,
            description = Description("Работа над боковой цепью и межреберными мышцами."),
//            primaryFocus = "Межреберные мышцы, Боковая цепь",
            position = ExercisePosition.STAY_UP,
            instruction = ExerciseInstruction(
                keyCues = "Сохраняйте тело в одной плоскости, избегайте наклона вперед. Руки над головой.",
                safetyFocus = SafetyFocus(
                    corePrinciple = "Движение должно быть медленным и контролируемым.",
                    modification = null,
                    biomechanicalFocus = "Сосредоточьтесь на растяжении бока."
                ),
                preemptiveCue = "Аккуратно переходим на пол: Скорпион."
            )
        ),
        Exercise(
            id = 105, // Уникальный ID
            name = "Динамическая ротация 'Скорпион'",
            duration = 50.seconds,
            description = Description("Мобилизация грудного отдела и тазобедренных суставов."),
            position = ExercisePosition.LAY_DOWN,
            instruction = ExerciseInstruction(
                keyCues = "Поднимите согнутую ногу, тянитесь ею к противоположной руке. Чередуйте стороны.",
                safetyFocus = SafetyFocus(
                    corePrinciple = "Контроль ротации в грудном отделе.",
                    modification = null,
                    biomechanicalFocus = "Старайтесь не сильно разворачивать корпус вбок."
                ),
                preemptiveCue = "Встаем: завершающее упражнение (скрещивание рук)."
            )
        ),
        Exercise(
            id = 106, // Уникальный ID
            name = "Скрещивание рук / Округление спины",
            duration = 50.seconds,
            description = Description("Завершающая мобильность грудного отдела и лопаток."),
//            primaryFocus = "Грудной отдел, Лопатки",
            position = ExercisePosition.STAY_UP,
            instruction = ExerciseInstruction(
                keyCues = "Вдох — раскрытие грудной клетки; Выдох — скрещивание рук с округлением спины.",
                safetyFocus = SafetyFocus(
                    corePrinciple = "Плавное завершение.",
                    modification = null,
                    biomechanicalFocus = "Избегайте резкого возврата в вертикальное положение."
                ),
                preemptiveCue = "Большой вдох. Отличная работа!"
            )
        )
    )
)

val program2 = Program(
    id = 2,
    name = "Активация Глубокого Мышечного Корсета",
    description = "Целенаправленная активация мышц кора, ягодиц и стабилизаторов для улучшения осанки.",
    programGoal = "Укрепление мышечного корсета и улучшение осанки.",
    exerciseType = "Корсет (Core Activation)",
    uxRecommendation = "Визуальная индикация нейтрального позвоночника обязательна. Напоминайте 'Пресс напряжен!'",
    durationTotal = 9.minutes,
    restInterval = 10.seconds,
    exercises = listOf(
        Exercise(
            id = 201, // Уникальный ID
            name = "Ягодичный мостик",
            duration = 50.seconds,
            description = Description("Активация ягодиц для стабилизации поясницы."),
//            primaryFocus = "Ягодицы, Стабилизация таза",
            position = ExercisePosition.LAY_DOWN,
            instruction = ExerciseInstruction(
                keyCues = "Напрягайте ягодицы, отталкиваясь пятками. Держите пятки под коленями.",
                safetyFocus = SafetyFocus(
                    corePrinciple = "Избегайте чрезмерного прогиба поясницы.",
                    modification = null,
                    biomechanicalFocus = "Контроль движения должен быть сосредоточен в ягодицах."
                ),
                preemptiveCue = "Переходим на четвереньки: Собака-птица."
            )
        ),
        Exercise(
            id = 202, // Уникальный ID
            name = "Собака-птица",
            duration = 50.seconds,
            description = Description("Упражнение на анти-ротацию и глубокий кор."),
//            primaryFocus = "Глубокий кор, Стабилизаторы",
            position = ExercisePosition.ON_KNEES,
            instruction = ExerciseInstruction(
                keyCues = "Вытяните противоположную руку и ногу. При возврате соедините локоть и колено под телом. Пресс напряжен.",
                safetyFocus = SafetyFocus(
                    corePrinciple = "Контроль положения тела.",
                    modification = null,
                    biomechanicalFocus = "Спина должна быть ровной, как доска."
                ),
                preemptiveCue = "Приготовьтесь к планке на локтях (на коленях)."
            )
        ),
        Exercise(
            id = 203, // Уникальный ID
            name = "Планка на локтях (на коленях)",
            duration = 50.seconds,
            description = Description("Удержание положения для общей стабилизации Кора."),
//            primaryFocus = "Общий кор, Мышцы пресса",
            position = ExercisePosition.ON_KNEES,
            instruction = ExerciseInstruction(
                keyCues = "Спина ровная, не проваливайте таз. Удерживайте положение.",
                safetyFocus = SafetyFocus(
                    corePrinciple = "Предотвращение прогиба в пояснице.",
                    modification = "Обязательная модификация: опора на колени.",
                    biomechanicalFocus = "Напрягите пресс и ягодицы."
                ),
                preemptiveCue = "Переворачиваемся на бок: Боковая планка (Левая)."
            )
        ),
        Exercise(
            id = 204, // Уникальный ID
            name = "Боковая планка на коленях (Левая)",
            duration = 50.seconds,
            description = Description("Работа над косыми мышцами и боковой стабилизацией."),
//            primaryFocus = "Косые мышцы, Стабилизаторы бедра",
            position = ExercisePosition.LAY_DOWN,
            instruction = ExerciseInstruction(
                keyCues = "Опорное колено, поднимите таз. Удерживайте стабильно.",
                safetyFocus = SafetyFocus(
                    corePrinciple = "Избегайте проваливания плеча.",
                    modification = "Опора на колени.",
                    biomechanicalFocus = "Локоть строго под плечом."
                ),
                preemptiveCue = "Переходим на другую сторону: Боковая планка (Правая)."
            )
        ),
        Exercise(
            id = 205, // Уникальный ID
            name = "Боковая планка на коленях (Правая)",
            duration = 50.seconds,
            description = Description("Повтор боковой стабилизации на другую сторону."),
//            primaryFocus = "Косые мышцы, Стабилизаторы бедра",
            position = ExercisePosition.LAY_DOWN,
            instruction = ExerciseInstruction(
                keyCues = "Повтор на другую ногу. Удерживайте стабильно.",
                safetyFocus = SafetyFocus(
                    corePrinciple = "Контролируйте положение таза и корпуса.",
                    modification = "Опора на колени.",
                    biomechanicalFocus = "Напрягите боковые мышцы живота."
                ),
                preemptiveCue = "Переворачиваемся на живот: Супермен."
            )
        ),
        Exercise(
            id = 206, // Уникальный ID
            name = "Супермен",
            duration = 50.seconds,
            description = Description("Активация разгибателей спины и мышц поясницы."),
//            primaryFocus = "Разгибатели спины, Мышцы поясницы",
            position = ExercisePosition.LAY_DOWN,
            instruction = ExerciseInstruction(
                keyCues = "Контролируемое одновременное поднятие рук, груди и ног.",
                safetyFocus = SafetyFocus(
                    corePrinciple = "Избегайте рывков.",
                    modification = "Поднимайтесь невысоко.",
                    biomechanicalFocus = "Фокусируйтесь на мышцах спины и ягодиц."
                ),
                preemptiveCue = "Последний цикл. Сделайте глубокий вдох."
            )
        )
    )
)

val program3 = Program(
    id = 3,
    name = "Легкое Кардио и Стимуляция Кровообращения",
    description = "Низкоударная программа для мягкого повышения ЧСС и стимуляции центральной нервной системы.",
    programGoal = "Стимуляция сердечно-сосудистой системы и повышение концентрации.",
    exerciseType = "Кардио (Low-Impact Cardio)",
    uxRecommendation = "Используйте позитивное подкрепление в конце ('Кофеин для мозга!').",
    durationTotal = 9.minutes,
    restInterval = 10.seconds,
    exercises = listOf(
        Exercise(
            id = 301, // Уникальный ID
            name = "Высокий марш с касанием",
            duration = 50.seconds,
            description = Description("Легкое кардио, улучшение координации и разогрев сгибателей бедра."),
//            primaryFocus = "Сгибатели бедра, Общее кардио",
            position = ExercisePosition.STAY_UP,
            instruction = ExerciseInstruction(
                keyCues = "Быстрый, но контролируемый марш, старайтесь коснуться коленом ладони перед грудью.",
                safetyFocus = SafetyFocus(
                    corePrinciple = "Низкоударная нагрузка. Полное исключение прыжков.",
                    modification = "Регулируйте интенсивность скоростью работы ног и рук.",
                    biomechanicalFocus = "Двигайтесь в умеренном темпе."
                ),
                preemptiveCue = "Приготовьтесь к полуприседаниям с разведением рук."
            )
        ),
        Exercise(
            id = 302, // Уникальный ID
            name = "Полуприседания с разведением рук",
            duration = 50.seconds,
            description = Description("Работа с ногами и синхронизацией дыхания."),
//            primaryFocus = "Квадрицепсы, Ягодицы",
            position = ExercisePosition.STAY_UP,
            instruction = ExerciseInstruction(
                keyCues = "Неглубокие приседания. Синхронизируйте дыхание: вдох на подъеме, выдох на приседании.",
                safetyFocus = SafetyFocus(
                    corePrinciple = "Избегайте глубоких приседаний.",
                    modification = null,
                    biomechanicalFocus = "Колени не должны выходить за носки."
                ),
                preemptiveCue = "Приготовьтесь к пульсирующим выпадам (Левая нога)."
            )
        ),
        Exercise(
            id = 303, // Уникальный ID
            name = "Выпады с пульсацией (Левая нога)",
            duration = 50.seconds,
            description = Description("Улучшение кровообращения в ногах, работа над балансом."),
//            primaryFocus = "Ноги, Баланс",
            position = ExercisePosition.STAY_UP,
            instruction = ExerciseInstruction(
                keyCues = "Выпад вперед, удерживайте колено над стопой. Пульсируйте в нижней точке.",
                safetyFocus = SafetyFocus(
                    corePrinciple = "Контроль равновесия. Не давите коленом на бедро.",
                    modification = null,
                    biomechanicalFocus = "Строго следите за тем, чтобы не было чрезмерного давления на переднее бедро."
                ),
                preemptiveCue = "Приготовьтесь к смене ноги."
            )
        ),
        Exercise(
            id = 304, // Уникальный ID
            name = "Выпады с пульсацией (Правая нога)",
            duration = 50.seconds,
            description = Description("Работа с ногами и балансом на другую сторону."),
//            primaryFocus = "Ноги, Баланс",
            position = ExercisePosition.STAY_UP,
            instruction = ExerciseInstruction(
                keyCues = "Повтор на другую ногу. Сосредоточьтесь на равновесии.",
                safetyFocus = SafetyFocus(
                    corePrinciple = "Строгий контроль формы. Убедитесь, что заднее колено не касается пола.",
                    modification = null,
                    biomechanicalFocus = "Контролируйте равновесие."
                ),
                preemptiveCue = "Приготовьтесь к круговым движениям тазом."
            )
        ),
        Exercise(
            id = 305, // Уникальный ID
            name = "Круговые движения тазом",
            duration = 50.seconds,
            description = Description("Активный отдых и проработка подвижности тазобедренного сустава."),
//            primaryFocus = "Тазобедренные суставы, Активный отдых",
            position = ExercisePosition.STAY_UP,
            instruction = ExerciseInstruction(
                keyCues = "Широкие, контролируемые круговые движения. 25 секунд по часовой, 25 секунд против.",
                safetyFocus = SafetyFocus(
                    corePrinciple = "Удерживайте корпус прямо.",
                    modification = null,
                    biomechanicalFocus = "Избегайте наклона корпуса вперед."
                ),
                preemptiveCue = "Завершающее упражнение: Шаги в сторону."
            )
        ),
        Exercise(
            id = 306, // Уникальный ID
            name = "Шаги в сторону с касанием пола",
            duration = 50.seconds,
            description = Description("Завершающий кардио-акцент и латеральная мобильность."),
//            primaryFocus = "Кардио, Латеральные мышцы",
            position = ExercisePosition.STAY_UP,
            instruction = ExerciseInstruction(
                keyCues = "Шаг в сторону, касание пола/колена. Движение плавное и ритмичное.",
                safetyFocus = SafetyFocus(
                    corePrinciple = "Полное исключение прыжков и ударной нагрузки.",
                    modification = null,
                    biomechanicalFocus = "Двигайтесь в умеренном темпе."
                ),
                preemptiveCue = "Большой вдох. Отличная работа!"
            )
        )
    )
)
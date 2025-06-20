package com.example.geoquiz41




import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.geoquiz41.Question

class MainActivity : AppCompatActivity() {

    private lateinit var btnTrue: Button
    private lateinit var btnFalse: Button
    private lateinit var btnNext: Button
    private lateinit var txtQuestion: TextView

    private var questionIndex = 0
    private var correctAnswersCount = 0
    private lateinit var answeredFlags: BooleanArray

    private val questions = listOf(
        Question(R.string.question_africa, false),
        Question(R.string.question_europe, false),
        Question(R.string.question_berlin, true),
        Question(R.string.question_nile, true),
        Question(R.string.question_moscow, false),
        Question(R.string.question_brazil, true),
        Question(R.string.question_australia, true),
        Question(R.string.question_everest, true),
        Question(R.string.question_iceland, false),
        Question(R.string.question_canada, false) // Россия — самая большая страна

    )

    companion object {
        private const val STATE_INDEX = "current_question_index"
        private const val STATE_SCORE = "correct_answer_count"
        private const val STATE_ANSWERED = "answered_flags"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnTrue = findViewById(R.id.true_button)
        btnFalse = findViewById(R.id.false_button)
        btnNext = findViewById(R.id.next_button)
        txtQuestion = findViewById(R.id.question_text_view)

        questionIndex = savedInstanceState?.getInt(STATE_INDEX) ?: 0
        correctAnswersCount = savedInstanceState?.getInt(STATE_SCORE) ?: 0
        answeredFlags = savedInstanceState?.getBooleanArray(STATE_ANSWERED) ?: BooleanArray(questions.size)

        btnTrue.setOnClickListener { handleAnswer(true) }
        btnFalse.setOnClickListener { handleAnswer(false) }
        btnNext.setOnClickListener { goToNextQuestion() }

        displayCurrentQuestion()
    }

    private fun displayCurrentQuestion() {
        txtQuestion.setText(questions[questionIndex].textResId)

        val isAnswered = answeredFlags[questionIndex]
        setAnswerButtonsEnabled(!isAnswered)

        if (questionIndex == questions.lastIndex) {
            btnNext.isEnabled = false
            btnNext.visibility = Button.INVISIBLE
        } else {
            btnNext.isEnabled = true
            btnNext.visibility = Button.VISIBLE
        }
    }

    private fun setAnswerButtonsEnabled(enabled: Boolean) {
        btnTrue.isEnabled = enabled
        btnFalse.isEnabled = enabled
        btnTrue.visibility = if (enabled) Button.VISIBLE else Button.INVISIBLE
        btnFalse.visibility = if (enabled) Button.VISIBLE else Button.INVISIBLE
    }

    private fun handleAnswer(userChoseTrue: Boolean) {
        val correctAnswer = questions[questionIndex].answer
        val resultMessage = if (userChoseTrue == correctAnswer) {
            correctAnswersCount++
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }

        Toast.makeText(this, resultMessage, Toast.LENGTH_SHORT).show()
        setAnswerButtonsEnabled(false)
        answeredFlags[questionIndex] = true

        if (questionIndex == questions.lastIndex) {
            Toast.makeText(this, "Результат: $correctAnswersCount из ${questions.size}", Toast.LENGTH_LONG).show()
        }
    }

    private fun goToNextQuestion() {
        if (questionIndex < questions.lastIndex) {
            questionIndex++
            displayCurrentQuestion()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(STATE_INDEX, questionIndex)
        outState.putInt(STATE_SCORE, correctAnswersCount)
        outState.putBooleanArray(STATE_ANSWERED, answeredFlags)
    }
}
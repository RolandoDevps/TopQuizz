package com.example.firstapp.controller

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.firstapp.R
import com.example.firstapp.model.Question
import com.example.firstapp.model.QuestionBank


class GamingActivity : AppCompatActivity() , View.OnClickListener {

    private lateinit var mQuestionTextView: TextView;
    private lateinit var mAnswerButton1: Button;
    private lateinit var mAnswerButton2: Button;
    private lateinit var mAnswerButton3: Button;
    private lateinit var mAnswerButton4: Button;

    private lateinit var mQuestionBank: QuestionBank;

    private var mRemainingQuestionCount: Int = 4;
    private var mScoreCount: Int = 0;



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gaming)

        mQuestionTextView = findViewById(R.id.game_activity_textview_question);
        mAnswerButton1 = findViewById(R.id.game_activity_button_1);
        mAnswerButton2 = findViewById(R.id.game_activity_button_2);
        mAnswerButton3 = findViewById(R.id.game_activity_button_3);
        mAnswerButton4 = findViewById(R.id.game_activity_button_4);

        mQuestionBank = QuestionBank(generataQuestion().getQuestionList());
        displayQuestion(mQuestionBank.getCurrentQuestion());

        // Use the same listener for the four buttons.
        // The view id value will be used to distinguish the button triggered
        mAnswerButton1.setOnClickListener(this);
        mAnswerButton2.setOnClickListener(this);
        mAnswerButton3.setOnClickListener(this);
        mAnswerButton4.setOnClickListener(this);

    }

    private fun displayQuestion(question: Question) {
        // Set the text for the question text view and the four buttons
        mQuestionTextView.text = question.getQuestion()
        mAnswerButton1.text = question.getChoiceList()[0]
        mAnswerButton2.text = question.getChoiceList()[1]
        mAnswerButton3.text = question.getChoiceList()[2]
        mAnswerButton4.text = question.getChoiceList()[3]
    }

    private fun generataQuestion (): QuestionBank{
        val question1 = Question(
            "Who is the creator of Android?",
            listOf(
                "Andy Rubin",
                "Steve Wozniak",
                "Jake Wharton",
                "Paul Smith"
            ),
            0
        )
        val question2 = Question(
            "When did the first man land on the moon?",
            listOf(
                "1958",
                "1962",
                "1967",
                "1969"
            ),
            3
        )
        val question3 = Question(
            "What is the house number of The Simpsons?",
            listOf(
                "42",
                "101",
                "666",
                "742"
            ),
            3
        )
        return QuestionBank(listOf(question1, question2, question3))
    }

    override fun onClick(v: View?) {
        val index: Int

        if (v == mAnswerButton1) {
            index = 0
        } else if (v == mAnswerButton2) {
            index = 1
        } else if (v == mAnswerButton3) {
            index = 2
        } else if (v == mAnswerButton4) {
            index = 3
        } else {
            throw IllegalStateException("Unknown clicked view : $v")
        }

        //Notification de la répone de l'utilisateur
        if(index == mQuestionBank.getCurrentQuestion().getAnswerIndex()){
            mScoreCount++;
            Toast.makeText(this, "Correct answer", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Incorrect answer", Toast.LENGTH_SHORT).show();
        }

        mRemainingQuestionCount--;
        if (mRemainingQuestionCount > 0) {
            displayQuestion(mQuestionBank.getNextQuestion());
        } else {
            val builder = AlertDialog.Builder(this)

            builder.setTitle("Well done!")
            builder.setMessage("Your score is $mScoreCount")
            builder.setPositiveButton(
                    "OK"
                ) { dialog, which -> finish() }
            builder.show()
        }

    }
}
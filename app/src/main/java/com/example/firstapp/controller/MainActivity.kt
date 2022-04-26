package com.example.firstapp.controller

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.firstapp.R
import com.example.firstapp.model.User


class MainActivity : AppCompatActivity() {

    private lateinit var mGreetingTextView: TextView;
    private lateinit var mNameEditText: EditText;
    private lateinit var mPlayButton: Button;
    private lateinit var mUser: User;

    //code permettant d'identifier de manière unique l'activité qui envoit la requête
    private val GAME_ACTIVITY_REQUEST_CODE = 42

    //constante représentant le nom du fichier pour la sauvegarde locale des préférances
    private val SHARED_PREF_USER_INFO = "SHARED_PREF_USER_INFO"
    //constante permettant sauvegarde locale du nom de l'utilisateur dans des préférances
    private val SHARED_PREF_USER_INFO_NAME = "SHARED_PREF_USER_INFO_NAME"
    //constante permettant sauvegarde locale du score de l'utilisateur dans des préférances
    private val SHARED_PREF_USER_INFO_SCORE = "SHARED_PREF_USER_INFO_SCORE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mGreetingTextView = findViewById(R.id.main_textview_greeting);
        mNameEditText = findViewById(R.id.main_edittext_name);
        mPlayButton = findViewById(R.id.main_button_play);

        mPlayButton.setEnabled(false);

        mNameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                // This is where we'll check the user input
                mPlayButton.setEnabled(s.toString().isNotEmpty());
            }
        })

        mPlayButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                // The user just clicked
                mUser = User(mNameEditText.text.toString())

                //Stocker le prénom de l'utilisateur
                getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE)
                    .edit()
                    .putString(SHARED_PREF_USER_INFO_NAME, mNameEditText.text.toString())
                    .apply()

                val gameActivityIntent = Intent(this@MainActivity, GameActivity::class.java)
                //startActivity(gameActivityIntent)
                startActivityForResult(gameActivityIntent, GAME_ACTIVITY_REQUEST_CODE);
            }
        })

        greetUser()
    }

    /*override fun onStart() {
        super.onStart()

        Log.d("ROlAND", "onStart() called");
    }

    override fun onResume() {
        super.onResume()

        Log.d("ROLAND", "onResume() called")
    }

    override fun onPause() {
        super.onPause()

        Log.d("ROlAND", "onPause() called");
    }

    override fun onStop() {
        super.onStop()

        Log.d("ROlAND", "onStop() called");
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d("ROlAND", "onDestroy() called");
    }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (GAME_ACTIVITY_REQUEST_CODE === requestCode && RESULT_OK == resultCode && data != null) {
            // Fetch the score from the Intent
            val score = data.getIntExtra(GameActivity::BUNDLE_EXTRA_SCORE.name, 0)
            //println(score);
            //Stocker le score de l'utilisateur
            getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE)
                .edit()
                .putInt(SHARED_PREF_USER_INFO_SCORE, score)
                .apply();

            greetUser()
        }

    }

    private fun greetUser() {
        val firstName = getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE).getString(
            SHARED_PREF_USER_INFO_NAME,
            null
        )
        val score = getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE).getInt(
            SHARED_PREF_USER_INFO_SCORE,
            -1
        )
        if (firstName != null) {
            if (score != -1) {
                mGreetingTextView.text =
                    getString(R.string.welcome_back_with_score, firstName, score, 5)
            } else {
                mGreetingTextView.text = getString(R.string.welcome_back, firstName)
            }
            mNameEditText.setText(firstName)
        }
    }
}
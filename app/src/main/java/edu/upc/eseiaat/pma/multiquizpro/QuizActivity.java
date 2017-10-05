package edu.upc.eseiaat.pma.multiquizpro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private int ids_answers[]={ R.id.answer1, R.id.answer2, R.id.answer3, R.id.answer4 };
    private int correct_answer;
    private String[] all_questions;
    private int current_question;
    private TextView text_question;
    private RadioGroup grup;
    private boolean[] answer_is_correct;
    private Button btn_next, btn_prev;
    private int[] answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Comentari
        text_question = (TextView) findViewById(R.id.text_question);
        all_questions = getResources().getStringArray(R.array.all_questions);
        grup = (RadioGroup) findViewById(R.id.answer_group);
        btn_next = (Button) findViewById(R.id.btn_check);
        btn_prev = (Button) findViewById(R.id.btn_prev);

        answer_is_correct=new boolean[all_questions.length];
        answer = new int[all_questions.length];

        for (int i =0; i<answer.length ;i++) {
            answer[i]=-1;
        }

        current_question=0;
        showquestion();

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer();

                /*
                if (respuesta==correct_answer){
                    Toast.makeText(QuizActivity.this, R.string.correct, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(QuizActivity.this, R.string.incorrect, Toast.LENGTH_SHORT).show();
                } */

                if (current_question<all_questions.length-1) {
                    current_question++;
                    showquestion();
                }
                else {
                    int correctas=0, incorrectas=0;
                    for (boolean b : answer_is_correct) {
                        if (b) {correctas++;}
                        else {incorrectas++;}
                    }
                    String resultado = String.format("Correctes: %d - Incorrectes: %d", correctas,incorrectas);
                    Toast.makeText(QuizActivity.this, resultado, Toast.LENGTH_SHORT).show();
                    //finish();

                }
                /*
                for (int i=0; i<answer_is_correct.length;i++){
                    Log.i("Kike", String.format("Resposta %d: %d (%b)", i, answer[i], answer_is_correct[i]));
                }
                */
            }
        });

        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer();
                if (current_question>0) {
                    current_question--; showquestion();
                }
            }
        });
    }

    private void checkAnswer() {
        int id= grup.getCheckedRadioButtonId();
        int ans=-1;

        for (int i=0; i<ids_answers.length; i++) {
            if (ids_answers[i]==id) {ans=i;}
        }

        answer_is_correct[current_question]=(ans==correct_answer);
        answer[current_question]=ans;
    }

    private void showquestion() {
        String q = all_questions[current_question];
        String[] parts = q.split(";");

        grup.clearCheck();

        text_question.setText(parts[0]);

        for (int i=0; i<ids_answers.length; i++) {
            RadioButton rb = (RadioButton) findViewById(ids_answers[i]);
            String ans = parts[i+1];

            if(ans.charAt(0)=='*') {
                correct_answer=i;
                ans=ans.substring(1);
            }

            rb.setText(ans);

            if(answer[current_question]== i){rb.setChecked(true);}
        }

        if (current_question==0){btn_prev.setVisibility(View.GONE);}
        else {btn_prev.setVisibility(View.VISIBLE);}

        if (current_question==all_questions.length-1) {btn_next.setText(R.string.finish);}
        else {btn_next.setText(R.string.next);}
    }
}

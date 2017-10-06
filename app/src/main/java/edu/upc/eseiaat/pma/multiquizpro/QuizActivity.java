package edu.upc.eseiaat.pma.multiquizpro;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class QuizActivity extends AppCompatActivity {

    private int ids_answers[]={R.id.answer1, R.id.answer2, R.id.answer3, R.id.answer4};
    private int correct_answer;
    private String[] all_questions;
    private int current_question;
    private TextView text_question;
    private RadioGroup grup;
    private boolean[] answer_is_correct;
    private Button btn_next, btn_prev;
    private int[] answer;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i("lifecycle","onSaveInstanceState");
        super.onSaveInstanceState(outState);
        outState.putInt("correct_answer",correct_answer);
        outState.putInt("current_question",current_question);
        outState.putIntArray("answer",answer);
        outState.putBooleanArray("answer_is_correct",answer_is_correct);
    }

    @Override
    protected void onStop() {
        Log.i("lifecycle","onStop");
        super.onStop();
    }

    @Override
    protected void onStart() {
        Log.i("lifecycle","onStart");
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        Log.i("lifecycle","onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("lifecycle","onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        text_question = (TextView) findViewById(R.id.text_question);
        all_questions = getResources().getStringArray(R.array.all_questions);
        grup = (RadioGroup) findViewById(R.id.answer_group);
        btn_next = (Button) findViewById(R.id.btn_check);
        btn_prev = (Button) findViewById(R.id.btn_prev);

        if (savedInstanceState==null){
            startOver();
        }
        else {
            correct_answer=savedInstanceState.getInt("correct_answer");
            current_question=savedInstanceState.getInt("current_question");
            answer=savedInstanceState.getIntArray("answer");
            answer_is_correct=savedInstanceState.getBooleanArray("answer_is_correct");

            showquestion();
        }

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer();
                if (current_question<all_questions.length-1) {
                    current_question++;
                    showquestion();
                }
                else {
                    checkResults();
                }
            }
        });

        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer();
                if (current_question>0) {
                    current_question--;
                    showquestion();
                }
            }
        });
    }

    private void startOver() {
        answer_is_correct=new boolean[all_questions.length];
        answer = new int[all_questions.length];
        for (int i =0; i<answer.length ;i++) {
            answer[i]=-1;
        }
        current_question=0;
        showquestion();
    }

    private void checkResults() {
        int correctas=0, incorrectas=0, nocontestades=0;
        for (int i=0; i<all_questions.length; i++) {
            if (answer_is_correct[i]) {correctas++;}
            else if (answer[i]==-1) {nocontestades++;}
            else {incorrectas++;}
        }
        String message = getString(R.string.total_cor)+": "+correctas+"\n"+getString(R.string.total_inc)+": "+incorrectas+"\n"+getString(R.string.total_not_ans)+": "+nocontestades;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.results);
        builder.setMessage(message);
        builder.setCancelable(false);

        builder.setPositiveButton(R.string.finish, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        builder.setNegativeButton(R.string.start_over, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startOver();
            }
        });
        builder.create().show();
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

package com.example.task;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;

import com.example.task.notedb.DataDatabase;
import com.example.task.notedb.model.Note;
import com.google.android.material.textfield.TextInputEditText;

import java.lang.ref.WeakReference;

public class AddDataActivity extends AppCompatActivity {

    private TextInputEditText et_name, et_salary,et_desig;
    private DataDatabase dataDatabase;
    private Note note;
    private boolean update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);
        et_name = findViewById(R.id.et_name);
        et_salary = findViewById(R.id.et_salary);
        et_desig = findViewById(R.id.et_desig);
        dataDatabase = DataDatabase.getInstance(AddDataActivity.this);
        Button button = findViewById(R.id.but_save);
        if ((note = (Note) getIntent().getSerializableExtra("note")) != null) {
            getSupportActionBar().setTitle("Update Data");
            update = true;
            button.setText("Update");
            et_name.setText(note.getName());
            et_salary.setText(note.getSalary());
            et_desig.setText(note.getDesignation());
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (update) {
                    note.setSalary(Integer.parseInt(et_salary.getText().toString()));
                    note.setName(et_name.getText().toString());
                    dataDatabase.getNoteDao().updateNote(note);
                    setResult(note, 2);
                } else {
                    note = new Note(Integer.parseInt(et_salary.getText().toString()), et_name.getText().toString()
                    ,et_desig.getText().toString());
                    new InsertTask(AddDataActivity.this, note).execute();
                }
            }
        });
    }

    private void setResult(Note note, int flag) {
        setResult(flag, new Intent().putExtra("note", note));
        finish();
    }

    private static class InsertTask extends AsyncTask<Void, Void, Boolean> {

        private WeakReference<AddDataActivity> activityReference;
        private Note note;

        // only retain a weak reference to the activity
        InsertTask(AddDataActivity context, Note note) {
            activityReference = new WeakReference<>(context);
            this.note = note;
        }

        // doInBackground methods runs on a worker thread
        @Override
        protected Boolean doInBackground(Void... objs) {
            // retrieve auto incremented note id
            long j = activityReference.get().dataDatabase.getNoteDao().insertNote(note);
            note.setNote_id(j);
            Log.e("ID ", "doInBackground: " + j);
            return true;
        }

        // onPostExecute runs on main thread
        @Override
        protected void onPostExecute(Boolean bool) {
            if (bool) {
                activityReference.get().setResult(note, 1);
                activityReference.get().finish();
            }
        }
    }


}

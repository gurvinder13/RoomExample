package com.example.task;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task.adapter.DataAdapter;
import com.example.task.notedb.DataDatabase;
import com.example.task.notedb.model.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class DataListActivity extends AppCompatActivity implements DataAdapter.OnNoteItemClick {

    private TextView textViewMsg;
    private RecyclerView recyclerView;
    private DataDatabase dataDatabase;
    private List<Note> notes;
    private DataAdapter dataAdapter;
    private int pos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeVies();
        displayList();
    }

    private void displayList() {
        dataDatabase = DataDatabase.getInstance(DataListActivity.this);
        new RetrieveTask(this).execute();
    }

    private static class RetrieveTask extends AsyncTask<Void, Void, List<Note>> {

        private WeakReference<DataListActivity> activityReference;

        // only retain a weak reference to the activity
        RetrieveTask(DataListActivity context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected List<Note> doInBackground(Void... voids) {
            if (activityReference.get() != null)
                return activityReference.get().dataDatabase.getNoteDao().getNotes();
            else
                return null;
        }

        @Override
        protected void onPostExecute(List<Note> notes) {
            if (notes != null && notes.size() > 0) {
                activityReference.get().notes.clear();
                activityReference.get().notes.addAll(notes);
                // hides empty text view
                activityReference.get().textViewMsg.setVisibility(View.GONE);
                activityReference.get().dataAdapter.notifyDataSetChanged();
            }
        }
    }

    private void initializeVies() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        textViewMsg = (TextView) findViewById(R.id.tv__empty);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(listener);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(DataListActivity.this));
        notes = new ArrayList<>();
        dataAdapter = new DataAdapter(notes, DataListActivity.this);
        recyclerView.setAdapter(dataAdapter);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivityForResult(new Intent(DataListActivity.this, AddDataActivity.class), 100);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode > 0) {
            if (resultCode == 1) {
                notes.add((Note) data.getSerializableExtra("note"));
            } else if (resultCode == 2) {
                notes.set(pos, (Note) data.getSerializableExtra("note"));
            }
            listVisibility();
        }
    }

    @Override
    public void onNoteClick(final int pos) {
       /* new AlertDialog.Builder(NoteListActivity.this)
                .setTitle("Select Options")
                .setItems(new String[]{"Delete", "Update"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                noteDatabase.getNoteDao().deleteNote(notes.get(pos));
                                notes.remove(pos);
                                listVisibility();
                                break;
                            case 1:
                                NoteListActivity.this.pos = pos;
                                startActivityForResult(
                                        new Intent(NoteListActivity.this,
                                                AddNoteActivity.class).putExtra("note", notes.get(pos)),
                                        100);

                                break;
                        }
                    }
                }).show();*/

    }

    @Override
    public void onEditClick(final int pos) {
        new AlertDialog.Builder(DataListActivity.this)
                .setTitle("Select Options")
                .setItems(new String[]{ "Update"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                                dataDatabase.getNoteDao().deleteNote(notes.get(pos));
                                notes.remove(pos);
                                listVisibility();

                    }
                }).show();
    }

    @Override
    public void onDelete(final int pos) {
        new AlertDialog.Builder(DataListActivity.this)
                .setTitle("Select Options")
                .setItems(new String[]{"Delete"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                                DataListActivity.this.pos = pos;
                                startActivityForResult(
                                        new Intent(DataListActivity.this,
                                                AddDataActivity.class).putExtra("note", notes.get(pos)),
                                        100);

                    }
                }).show();
    }

    private void listVisibility() {
        int emptyMsgVisibility = View.GONE;
        if (notes.size() == 0) { // no item to display
            if (textViewMsg.getVisibility() == View.GONE)
                emptyMsgVisibility = View.VISIBLE;
        }
        textViewMsg.setVisibility(emptyMsgVisibility);
        dataAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        dataDatabase.cleanUp();
        super.onDestroy();
    }
}

package com.example.task.notedb;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.task.notedb.dao.NoteDao;
import com.example.task.notedb.model.Note;
import com.example.task.util.Constants;
import com.example.task.util.DateRoomConverter;


@Database(entities = { Note.class }, version = 1)
@TypeConverters({DateRoomConverter.class})
public abstract class DataDatabase extends RoomDatabase {

    public abstract NoteDao getNoteDao();


    private static DataDatabase noteDB;

    // synchronized is use to avoid concurrent access in multithred environment
    public static /*synchronized*/ DataDatabase getInstance(Context context) {
        if (null == noteDB) {
            noteDB = buildDatabaseInstance(context);
        }
        return noteDB;
    }
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE users "
                    +"ADD COLUMN address TEXT");

        }
    };
    private static DataDatabase buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context,
                DataDatabase.class,
                Constants.DB_NAME).allowMainThreadQueries().build();
    }

    public  void cleanUp(){
        noteDB = null;
    }
}
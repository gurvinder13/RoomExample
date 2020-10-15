package com.example.task.notedb.model;



import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.task.util.Constants;

import java.io.Serializable;
import java.util.Date;


@Entity(tableName = Constants.TABLE_NAME_NOTE)
public class Note implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long note_id;

    @ColumnInfo(name = "note_content")
    private int salary;

    private String name;
    private String designation;

    private Date date;


    public Note(int salary, String name,String designation) {
        this.salary = salary;
        this.name = name;
        this.designation=designation;
        this.date = new Date(System.currentTimeMillis());
    }

    @Ignore
    public Note() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getNote_id() {
        return note_id;
    }

    public void setNote_id(long note_id) {
        this.note_id = note_id;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Note)) return false;

        Note note = (Note) o;

        if (note_id != note.note_id) return false;
        return name != null ? name.equals(note.name) : note.name == null;
    }


    @Override
    public int hashCode() {
        int result = (int) note_id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Note{" +
                "note_id=" + note_id +
                ", salary='" + salary + '\'' +
                ", name='" + name + '\'' +
                ", designation='" + designation + '\'' +
                ", date=" + date +
                '}';
    }
}

package com.example.patchment20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView note_list_id;
    Button note_but_id;
    Button todo_but_id;
    Button alarm_but_id;

    ArrayAdapter<String> note_array_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        note_list_id = findViewById(R.id.note_list_id);
        note_but_id = findViewById(R.id.note_but_id);
        todo_but_id = findViewById(R.id.todo_but_id);
        alarm_but_id = findViewById(R.id.alarm_but_id);

        note_array_adapter = new ArrayAdapter<String>(this, R.layout.items_layout, R.id.first_text_item_id);
        loadFilesInDirectory();
        note_list_id.setAdapter(note_array_adapter);

        NoteButton();
        todoButton();
        alarmButton();
        noteList();
    }

    private void loadFilesInDirectory(){
        String[] f_in_dir = getFilesDir().list();
        assert f_in_dir != null;
        for(String file: f_in_dir){
            note_array_adapter.add(file.replace(".txt", ""));
        }
        note_array_adapter.notifyDataSetChanged();
    }

    private void noteList() {
        note_list_id.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title = note_list_id.getItemAtPosition(position).toString();
                Intent intent = new Intent(getApplicationContext(), EditorActivity.class);
                intent.putExtra("note", title);
                startActivity(intent);
            }
        });

        note_list_id.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("do you want to delete this item")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                File file = new File(getFilesDir(),
                                        note_list_id.getItemAtPosition(position).toString()+".txt");
                                file.delete();
                                note_array_adapter.remove(note_list_id.getItemAtPosition(position).toString());
                                note_array_adapter.notifyDataSetChanged();
                                Toast.makeText(MainActivity.this, "todo deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("no", null).show();
                return true;
            }
        });
    }


    private void NoteButton() {
        note_but_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditorActivity.class);
                startActivity(intent);
            }
        });
    }

    private void alarmButton() {
        alarm_but_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AlarmActivity.class);
                startActivity(intent);
            }
        });
    }

    private void todoButton() {
        todo_but_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TodoActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

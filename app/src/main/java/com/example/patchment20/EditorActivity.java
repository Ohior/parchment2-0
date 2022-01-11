package com.example.patchment20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class EditorActivity extends AppCompatActivity {

    Button save_note_btn_id;
    EditText note_et_title_id;
    EditText note_et_detail_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        save_note_btn_id = findViewById(R.id.save_note_btn_id);
        note_et_detail_id = findViewById(R.id.note_et_detail_id);
        note_et_title_id = findViewById(R.id.note_et_title_id);

        saveNote();
        editNote(getIntent().getStringExtra("note"));
    }

    private void editNote(String title) {
        try {
            FileInputStream f_i_s = openFileInput(title+".txt");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(f_i_s))) {
                String line;
                String whole = "";
                while ((line = reader.readLine()) != null) {
                    if (whole == "") {
                        whole = whole + line;
                    } else {
                        whole = whole + "\n" + line;
                    }
                }
                note_et_detail_id.setText(whole);
                note_et_title_id.setText(title);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveNote() {
        save_note_btn_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!note_et_title_id.getText().toString().isEmpty() &&
                        !note_et_detail_id.getText().toString().isEmpty()) {
                    String title = note_et_title_id.getText().toString();
                    String notes = note_et_detail_id.getText().toString();
                    try {
                        //heading will be filename
                        FileOutputStream fileOutputStream = openFileOutput(title+".txt", Context.MODE_PRIVATE);
                        fileOutputStream.write(notes.getBytes());
                        fileOutputStream.close();
                    }catch (FileNotFoundException e){
                        Toast.makeText(getApplicationContext(), e.toString() , Toast.LENGTH_LONG).show();
                    }catch (IOException e){
                        Toast.makeText(getApplicationContext(), e.toString() , Toast.LENGTH_LONG).show();
                    }
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

}
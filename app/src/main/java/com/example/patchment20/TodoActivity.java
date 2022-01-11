package com.example.patchment20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArraySet;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;

public class TodoActivity extends AppCompatActivity {

    private static SharedPreferences sharedPreferences;
    Button add_todo_btn_id;
    ListView todo_list_id;
    private ArrayList<String> todo_array_list;
    private ArrayAdapter<String> todo_list_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        add_todo_btn_id = findViewById(R.id.add_todo_btn_id);
        todo_list_id = findViewById(R.id.todo_list_id);

        todo_array_list = getTodoItems(TodoActivity.this, "todo");
        todo_list_adapter = new ArrayAdapter<>(this, R.layout.items_layout,R.id.first_text_item_id, todo_array_list);
        todo_list_id.setAdapter(todo_list_adapter);

        addTodo();
        todoItemsLongClick();
    }

    private void todoItemsLongClick() {
        todo_list_id.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(TodoActivity.this)
                        .setMessage("do you want to delete this item")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                todo_array_list.remove(position);
                                todo_list_adapter.notifyDataSetChanged();
                                Toast.makeText(TodoActivity.this, "todo deleted", Toast.LENGTH_SHORT).show();
                                saveTodoItems(getApplicationContext(),"todo", todo_array_list);
                            }
                        })
                        .setNegativeButton("no", null).show();
                return true;
            }
        });
    }

    private void addTodo() {
        add_todo_btn_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_todo_btn_id.setVisibility(View.INVISIBLE);
                AlertDialog.Builder popup_window = new AlertDialog.Builder(TodoActivity.this);
                EditText et = new EditText(TodoActivity.this);
                et.setSingleLine(true);
                popup_window.setView(et);
                popup_window.setTitle("Enter Todo");
                popup_window.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        add_todo_btn_id.setVisibility(View.VISIBLE);
                        String txt = et.getText().toString();
                        if (!txt.isEmpty()){
                            if (todo_array_list.contains(txt)){
                                Toast.makeText(TodoActivity.this, "That item already exist", Toast.LENGTH_LONG).show();
                                return;
                            }
                            todo_list_adapter.add(txt);
                            saveTodoItems(TodoActivity.this,"todo", todo_array_list);
                            todo_list_adapter.notifyDataSetChanged();
                        }
                    }
                });
                popup_window.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        add_todo_btn_id.setVisibility(View.VISIBLE);
                    }
                });
                popup_window.show();
            }
        });
    }

    private void saveTodoItems(Context context, String key, ArrayList<String> data) {
        sharedPreferences = getSharedPreferences("TodoItems", Context.MODE_PRIVATE);
        HashSet<String> todo_hashset = new HashSet<>(data);
        sharedPreferences.edit().putStringSet(key, todo_hashset).apply();
        Toast.makeText(context, "Item added", Toast.LENGTH_LONG).show();
        todo_list_adapter.notifyDataSetChanged();
    }

    public ArrayList<String> getTodoItems(Context context, String key){
        sharedPreferences = context.getSharedPreferences("TodoItems", Context.MODE_PRIVATE);
        HashSet<String> todo_hashset = (HashSet<String>) sharedPreferences.getStringSet(key, null);
        if (todo_hashset != null) return new ArrayList<>(todo_hashset);
        return new ArrayList<String>();
    }
}
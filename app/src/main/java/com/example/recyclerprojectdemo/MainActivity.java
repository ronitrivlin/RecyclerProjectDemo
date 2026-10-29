package com.example.recyclerprojectdemo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Item> items;
    private int i = 7;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        items = new ArrayList<Item>();
        Item item1 = new Item("aa", "aaaaaa", 1);
        items.add(item1);
        Item item2 = new Item("bb", "bbbbbb", 2);
        items.add(item2);
        Item item3 = new Item("cc", "cccccc", 3);
        items.add(item3);
        Item item4 = new Item("dd", "dddddd", 4);
        items.add(item4);
        Item item5 = new Item("ee", "eeeeee", 5);
        items.add(item5);

        recyclerView = findViewById(R.id.recycler);

        MyAdapter adapter = new MyAdapter(items);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper.SimpleCallback simpleCallback =
                new ItemTouchHelper.SimpleCallback(
                        ItemTouchHelper.UP   | ItemTouchHelper.DOWN,
                        ItemTouchHelper.RIGHT   |  ItemTouchHelper.LEFT )
                {//  פונקציות לטיפול בהזזות
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
                    {
                        int fromPos = viewHolder.getAdapterPosition();
                        int toPos = target.getAdapterPosition();

                        Collections.swap(items, fromPos, toPos);
                        adapter.notifyItemMoved(fromPos, toPos);
                        return true;
                    }
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction)
                    {
                        int position = viewHolder.getAdapterPosition();
                        if (direction == ItemTouchHelper.RIGHT) {
                            // Swipe right
                            items.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                    }
                };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        Button btnAdd = findViewById(R.id.add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random rnd = new Random();
                int num = rnd.nextInt(100);
                Item item5 = new Item("ee"+num, "eeeeee", 5);
                items.add(item5);
                // adapter.notifyDataSetChanged();
                adapter.notifyItemInserted(items.size()-1);

            }
        });
    }
}
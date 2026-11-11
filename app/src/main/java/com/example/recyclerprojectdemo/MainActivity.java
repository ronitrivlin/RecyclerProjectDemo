package com.example.recyclerprojectdemo;

import static com.example.recyclerprojectdemo.FirebaseRef.refUserItems;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Item> items;
    private RecyclerView recyclerView;
    MyAdapter adapter;

    ValueEventListener itemsListener;
    @Override
    protected void onPause()
    {
        super.onPause();
        if (itemsListener!=null) {
            refUserItems.removeEventListener(itemsListener);
        }
    }


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
//        Item item1 = new Item("aa", "aaaaaa", 1);
//        items.add(item1);
//        Item item2 = new Item("bb", "bbbbbb", 2);
//        items.add(item2);
//        Item item3 = new Item("cc", "cccccc", 3);
//        items.add(item3);
//        Item item4 = new Item("dd", "dddddd", 4);
//        items.add(item4);
//        Item item5 = new Item("ee", "eeeeee", 5);
//        items.add(item5);

        readItemsFromDB();
        
        recyclerView = findViewById(R.id.recycler);

        adapter = new MyAdapter(items);
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
                View dialogView = getLayoutInflater().inflate(R.layout.add_item, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(dialogView);
                final AlertDialog customDialog = builder.create();
                customDialog.show();
                Button add = customDialog.findViewById(R.id.btnAddItem);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText name = customDialog.findViewById(R.id.etAddName);
                        EditText desc = customDialog.findViewById(R.id.etAddDesc);
                        Item item = new Item(name.getText().toString(), desc.getText().toString(), 5);

                        addToDB(item, customDialog);
                        // adapter.notifyDataSetChanged();

                    }
                });
            }
        });
    }

    private void readItemsFromDB() {
        FirebaseUser currentUser = FirebaseRef.refAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userItemsRef = refUserItems.child(userId);
            items.clear();
            userItemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dS) {
                    for (DataSnapshot data : dS.getChildren()) {
                        Item item = data.getValue(Item.class);
                        if (item != null) {
                            items.add(item);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //Log.e("Firebase", "שגיאה בטעינת הנתונים: " + databaseError.getMessage());
                }
            });
        }
//        else {
//            Log.e("Firebase", "אין משתמש מחובר ב-FirebaseAuth");
//        }
    }

    // האזנה לשינוי כך שבכל שינוי נקבל מחדש את כל המידע ולא רק מה שהשתנה
    private void getDBChanges()
    {
        FirebaseUser currentUser = FirebaseRef.refAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userItemsRef = refUserItems.child(userId);
            itemsListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dS) {
                    items.clear();
                    for (DataSnapshot data : dS.getChildren()) {
                        Item item = data.getValue(Item.class);
                        if (item != null) {
                            items.add(item);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            userItemsRef.addValueEventListener(itemsListener);
        }
    }
    private void addToDB(Item item, AlertDialog customDialog) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userItemsRef = refUserItems.child(userId);
            String id = userItemsRef.push().getKey();
            item.setKeyId(id);
            userItemsRef.child(id).setValue(item)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                items.add(item);
                                adapter.notifyItemInserted(items.size()-1);
                                customDialog.dismiss();
                                Log.d("Firebase", "הנתונים נשמרו בהצלחה!");
                            } else {
                                Log.e("Firebase", "שגיאה בכתיבה: ", task.getException());
                            }
                        }
                    });
        }
    }
}
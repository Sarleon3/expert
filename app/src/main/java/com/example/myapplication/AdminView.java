package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.ui.appl;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminView extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private DatabaseReference mDatabase;
    private String Applications = "Applications";
    private List<String> list;
    private List<appl> applList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        listView = findViewById(R.id.spisok);
        list = new ArrayList<>();
        applList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, R.layout.list, list);
        listView.setAdapter(adapter);
        setOnClickListener();
        mDatabase = FirebaseDatabase.getInstance("https://expert-b499b-default-rtdb.europe-west1.firebasedatabase.app/").getReference(Applications);
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (list.size() > 0) list.clear();
                if (applList.size() > 0) applList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    appl appl = ds.getValue(appl.class);
                    assert appl != null;
                    list.add(appl.Product_Name);
                    applList.add(appl);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabase.addValueEventListener(vListener);
    }

    private void setOnClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                appl appl = applList.get(position);
                Intent i = new Intent(AdminView.this,adminApil.class);
                i.putExtra("id",appl.Id);
                i.putExtra("Mail",appl.Mail);
                i.putExtra("Phone",appl.Phone);
                i.putExtra("Name",appl.Product_Name);
                i.putExtra("Adres",appl.Location);
                i.putExtra("Mark",appl.Marking);
                i.putExtra("Type",appl.Type);
                i.putExtra("Prise",appl.Price);
                startActivity(i);
            }
        });
    }
}
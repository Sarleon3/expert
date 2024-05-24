package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.ui.appl;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class adminApil extends AppCompatActivity {

    private TextView Mail,Phone,Name,Adres,Mark;
    private EditText Prise;
    private EditText Type;
    private EditText marking;
    private Button btn;
    EditText tagss;
    private DatabaseReference mDatabase;
    private String expert = "Expert";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_apil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mDatabase = FirebaseDatabase.getInstance("https://expert-b499b-default-rtdb.europe-west1.firebasedatabase.app/").getReference(expert);
        init();
        get();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                base(v);
                end(v);
            }
        });
    }
    private  void  get(){
        Intent i = getIntent();
        if (i != null)
        {
            Mail.setText(i.getStringExtra("Mail"));
            Type.setText(i.getStringExtra("Type"));
            Prise.setText(i.getStringExtra("Prise"));
            Phone.setText(i.getStringExtra("Phone"));
            Name.setText(i.getStringExtra("Name"));
            Adres.setText(i.getStringExtra("Adres"));
            Mark.setText(i.getStringExtra("Mark"));
        }
    }

    public void base(View v){
        String id = mDatabase.push().getKey(); // Получаем ключ узла базы данных
        String Product_Name = Name.getText().toString();
        String Location = Adres.getText().toString();
        String Types = Type.getText().toString(); // Получаем выбранный элемент из Spinner
        String tags = tagss.getText().toString();
        String Marking = marking.getText().toString();
        expert expert = new expert(id, Product_Name, Location, Types, tags, Marking);
        mDatabase.child(id).setValue(expert); // Добавляем данные по сгенерированному ключу
    }
    public void end(View v){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
    public void init() {

        Type = findViewById(R.id.Type);
        Prise = findViewById(R.id.price);
        Mail = findViewById(R.id.Emailf);
        Phone = findViewById(R.id.phonef);
        Name = findViewById(R.id.Namef);
        Adres = findViewById(R.id.Adresf);
        Mark = findViewById(R.id.Markf);
        tagss = findViewById(R.id.tagsf);
        marking = findViewById(R.id.Marking);
        btn = findViewById(R.id.btnf);
    }
}
package com.example.myapplication.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentNotificationsBinding;
import com.example.myapplication.sing_in;
import com.example.myapplication.ui.appl;
import com.example.myapplication.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NotificationsFragment extends Fragment {

    private FirebaseAuth mAuth;
    private EditText Mail,Phone,Name,Adres,Mark;
    private Spinner Type;
    private TextView Prise;
    private DatabaseReference mDatabase;
    private String Applications = "Applications";
    public FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
         Type = root.findViewById(R.id.Type);
         Prise = root.findViewById(R.id.price);
         Mail = root.findViewById(R.id.Emailf);
         Phone = root.findViewById(R.id.phonef);
         Name = root.findViewById(R.id.Namef);
         Adres = root.findViewById(R.id.Adresf);
         Mark = root.findViewById(R.id.Markf);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance("https://expert-b499b-default-rtdb.europe-west1.firebasedatabase.app/").getReference(Applications);
        Mail.setText(currentUser.getEmail());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.type, R.layout.spin);
        Type.setAdapter(adapter);
        Type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] textValues = getResources().getStringArray(R.array.prise);
                String selectedText = textValues[position];
                Prise.setText(selectedText);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button submitButton = root.findViewById(R.id.btnf);
       submitButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
             base(v);
             end(v);
           }
       });

        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void base(View v){
        String id = mDatabase.push().getKey(); // Получаем ключ узла базы данных
        String User_Mail = Mail.getText().toString();
        String User_Phone = Phone.getText().toString();
        String Product_Name = Name.getText().toString();
        String Location = Adres.getText().toString();
        String Types = Type.getSelectedItem().toString(); // Получаем выбранный элемент из Spinner
        String Marking = Mark.getText().toString();
        String price = Prise.getText().toString();
        appl app = new appl(id, User_Mail, User_Phone, Product_Name, Location, Types, Marking, price);
        mDatabase.child(id).setValue(app); // Добавляем данные по сгенерированному ключу
    }
    public void end(View v){
        Mail.setText(null);
        Phone.setText(null);
        Name.setText(null);
        Adres.setText(null);
        Mark.setText(null);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        HomeFragment homeFragment = new HomeFragment();
        NavController navController = Navigation.findNavController(v);
        navController.navigate(R.id.navigation_home);
    }
}
package com.example.myapplication.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.AdminView;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.example.myapplication.log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private FirebaseAuth mAuth;
    private Button Adminbtn;
    private TextView login;
    private DatabaseReference mDatabase;
    String LoginUser = "LoginUser";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Adminbtn = root.findViewById(R.id.admin);
        login = root.findViewById(R.id.login);
        mDatabase = FirebaseDatabase.getInstance("https://expert-b499b-default-rtdb.europe-west1.firebasedatabase.app/").getReference(LoginUser);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Adminbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                admin(v);
            }
        });
        if (currentUser != null && currentUser.getEmail().equals("lexa.zhulidov@gmail.com")) {
            Adminbtn.setVisibility(View.VISIBLE);
        }
            ValueEventListener Listener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        log log = ds.getValue(log.class);
                        if (log != null) {
                            if (log != null && log.Mail.equals(currentUser.getEmail())) {
                                login.setText(log.login);
                                return;
                            }
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Обработка ошибки
                }
            };
            mDatabase.addValueEventListener(Listener);
        return root;
    }
public void admin(View view){
        Intent intent = new Intent(getActivity(), AdminView.class);
        startActivity(intent);
}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
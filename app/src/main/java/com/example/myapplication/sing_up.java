package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.ui.appl;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class sing_up extends AppCompatActivity {

    private EditText mEditTextEmail;
    private EditText mEditTextLogin;
    private EditText mEditTextPassword;
    private EditText mEditTextPassword2;
    private Button mButtonSignOn;
    private DatabaseReference mDatabase;
    String LoginUser = "LoginUser";
    private static final int MIN_PASSWORD_LENGTH = 6;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(sing_up.this, MainActivity.class);
            startActivity(intent);
        }
    }

    FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sing_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        mEditTextEmail = findViewById(R.id.MailR);
        mEditTextPassword = findViewById(R.id.PasswordR);
        mEditTextPassword2 = findViewById(R.id.PasswordR2);
        mButtonSignOn = findViewById(R.id.BtnR);
        mEditTextLogin = findViewById(R.id.LoginR);
        mDatabase = FirebaseDatabase.getInstance("https://expert-b499b-default-rtdb.europe-west1.firebasedatabase.app/").getReference(LoginUser);

        mButtonSignOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email,password,password2,login;
                email = String.valueOf(mEditTextEmail.getText());
                password = String.valueOf(mEditTextPassword.getText());
                password2 = String.valueOf(mEditTextPassword2.getText());
                login = String.valueOf(mEditTextLogin.getText());

                if(TextUtils.isEmpty(email)){
                    mEditTextEmail.setBackgroundResource(R.drawable.input2);
                    Toast.makeText(sing_up.this, "введите почту.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(login)){
                    mEditTextLogin.setBackgroundResource(R.drawable.input2);
                    Toast.makeText(sing_up.this, "введите логин.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mEditTextPassword.setBackgroundResource(R.drawable.input2);
                    Toast.makeText(sing_up.this, "введите пароль.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password2)){
                    mEditTextPassword2.setBackgroundResource(R.drawable.input2);
                    Toast.makeText(sing_up.this, "повторите пароль.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < MIN_PASSWORD_LENGTH) {
                    mEditTextPassword.setBackgroundResource(R.drawable.input2);
                    Toast.makeText(sing_up.this, "Пароль должен содержать как минимум " + MIN_PASSWORD_LENGTH + " символов.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(password2)) {
                    // Содержание EditText не совпадает, подсветить их красным
                    mEditTextPassword.setBackgroundResource(R.drawable.input2);
                    mEditTextPassword2.setBackgroundResource(R.drawable.input2);
                    Toast.makeText(sing_up.this, "пароли не совпадают.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Содержание EditText совпадает, снять подсветку
                    mEditTextPassword.setBackgroundResource(R.drawable.input);
                    mEditTextPassword2.setBackgroundResource(R.drawable.input);
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Intent intent = new Intent(sing_up.this, sing_in.class);
                                        startActivity(intent);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(sing_up.this, "аккаунт уже существует.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    }
                base(v);

            }
        });

        // Добавляем слушатель к корневому представлению для скрытия клавиатуры при касании фона
        View rootView = findViewById(R.id.main);
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, android.view.MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });
    }

    // Метод для скрытия клавиатуры
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }
    // Метод для проверки содержания двух EditText
    public static void validateEditTexts (EditText mEditTextPassword, EditText mEditTextPassword2) {
        String text1 = mEditTextPassword.getText().toString();
        String text2 = mEditTextPassword2.getText().toString();

        if (!text1.equals(text2)) {
            // Содержание EditText не совпадает, подсветить их красным
            mEditTextPassword.setBackgroundColor(Color.RED);
            mEditTextPassword2.setBackgroundColor(Color.RED);
        } else {
            // Содержание EditText совпадает, снять подсветку
            mEditTextPassword.setBackgroundColor(Color.TRANSPARENT);
            mEditTextPassword2.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    public void base(View v){
        String id = mDatabase.push().getKey(); // Получаем ключ узла базы данных
        String Mail = mEditTextEmail.getText().toString();
        String login = mEditTextLogin.getText().toString();
        log app = new log(id, Mail,login);
        mDatabase.child(id).setValue(app); // Добавляем данные по сгенерированному ключу
    }


    public void startNewActivity2 (View v){
        Intent intent = new Intent(this, sing_in.class);
        startActivity(intent);
    }
}
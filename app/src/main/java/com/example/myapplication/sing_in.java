package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;


public class sing_in extends AppCompatActivity {
    private static final int MIN_PASSWORD_LENGTH = 6;
    private EditText mEditTextEmail;
    private EditText mEditTextPassword;
    Button google;
    private Button mButtonSignIn;
    private Button Adminbtn;
    FirebaseAuth mAuth;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(sing_in.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sing_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        mEditTextEmail = findViewById(R.id.Mail);
        mEditTextPassword = findViewById(R.id.Password);
        mButtonSignIn = findViewById(R.id.login_button);
        google = findViewById(R.id.G_login_button);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);



        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singIn();
            }
        });

        mButtonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email = String.valueOf(mEditTextEmail.getText());
                password = String.valueOf(mEditTextPassword.getText());
                if (TextUtils.isEmpty(email)) {
                    mEditTextEmail.setBackgroundResource(R.drawable.input2);
                    Toast.makeText(sing_in.this, "введите почту.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mEditTextPassword.setBackgroundResource(R.drawable.input2);
                    Toast.makeText(sing_in.this, "введите пароль.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < MIN_PASSWORD_LENGTH) {
                    mEditTextPassword.setBackgroundResource(R.drawable.input2);
                    Toast.makeText(sing_in.this, "Пароль должен содержать как минимум " + MIN_PASSWORD_LENGTH + " символов.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(sing_in.this, MainActivity.class);
                                    startActivity(intent);

                                } else {
                                    Toast.makeText(sing_in.this, "такого аккаунта не существует.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

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

    public void startNewActivity(View v) {
        Intent intent = new Intent(this, sing_up.class);
        startActivity(intent);
    }
    void singIn() {
        Intent singInIntent = gsc.getSignInIntent();
        startActivityForResult(singInIntent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) { // Проверяем requestCode
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                // Аутентификация прошла успешно
                navigateToSecondActivity();
            } catch (ApiException e) {
                // В случае ошибки при аутентификации
                Toast.makeText(getApplicationContext(), "Ошибка при аутентификации: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("GoogleSignIn", "signInResult:failed code=" + e.getStatusCode());
            }
        }
    }



    private void navigateToSecondActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
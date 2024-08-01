package id.my.sglkc.catatanku;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {
    EditText usernameInput, passwordInput;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (Account.alreadyLoggedIn()) gotoMain();

        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(view -> login());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Account.setContext(this);
    }

    protected void gotoMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    protected void login() {
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();
        Account.Responses res = Account.login(username, password);

        switch (res) {
            case NOT_FOUND:
                Toast.makeText(this, "Username belum terdaftar", Toast.LENGTH_SHORT).show();
                break;
            case ERROR:
                Toast.makeText(this, "Tidak dapat membaca akun", Toast.LENGTH_SHORT).show();
                break;
            case WRONG_PASSWORD:
                Toast.makeText(this, "Password tidak sesuai", Toast.LENGTH_SHORT).show();
                break;
            case SUCCESS:
                gotoMain();
                break;
        }
    }
}
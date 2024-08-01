package id.my.sglkc.catatanku;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterActivity extends AppCompatActivity {
    EditText usernameInput, passwordInput, emailInput, nameInput, schoolInput, addressInput;
    Button registerButton, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                goTo(LoginActivity.class);
            }
        });

        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        emailInput = findViewById(R.id.emailInput);
        nameInput = findViewById(R.id.nameInput);
        schoolInput = findViewById(R.id.schoolInput);
        addressInput = findViewById(R.id.addressInput);
        registerButton = findViewById(R.id.registerButton);
        backButton = findViewById(R.id.backButton);

        registerButton.setOnClickListener(view -> register());
        backButton.setOnClickListener(view -> getOnBackPressedDispatcher().onBackPressed());
    }

    protected void goTo(Class<?> activity) {
        Intent intent = new Intent(RegisterActivity.this, activity);
        startActivity(intent);
        finish();
    }

    protected String[] validate() throws Exception {
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();
        String email = emailInput.getText().toString();
        String name = nameInput.getText().toString();
        String school = schoolInput.getText().toString();
        String address = addressInput.getText().toString();
        String[] data = new String[]{username, password, email, name, school, address};

        for (String input: data) {
            if (input.isEmpty()) throw new Exception();
        }

        return data;
    }

    protected void register() {
        String[] data;

        try {
            data = validate();
        } catch (Exception e) {
            Toast.makeText(this, "Input tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        }

        Account.Responses res = Account.register(data);

        switch(res) {
            case ERROR:
                Toast.makeText(this, "Tidak dapat membaca akun", Toast.LENGTH_SHORT).show();
                break;
            case ALREADY_EXISTS:
                Toast.makeText(this, "Username sudah dipakai", Toast.LENGTH_SHORT).show();
                break;
            case SUCCESS:
                goTo(MainActivity.class);
                break;
        }
    }
}
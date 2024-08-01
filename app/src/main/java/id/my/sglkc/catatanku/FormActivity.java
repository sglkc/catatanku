package id.my.sglkc.catatanku;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class FormActivity extends AppCompatActivity {
    EditText titleInput, noteInput;
    Button saveButton;
    String title, note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // definisi komponen
        titleInput = findViewById(R.id.editTitle);
        noteInput = findViewById(R.id.editNote);
        saveButton = findViewById(R.id.saveButton);
        Bundle extras = getIntent().getExtras();

        // jika ada state masuk, maka mode edit
        if (extras != null) {
            getSupportActionBar().setTitle("Ubah Catatan");
            title = extras.getString("name");

            titleInput.setText(title);
            titleInput.setEnabled(false);
            readNote();
        } else {
            getSupportActionBar().setTitle("Tambah catatan");
        }

        saveButton.setOnClickListener(view -> writeNote());
    }

    protected void readNote() {
        String path = getExternalFilesDir(null) + "/catatanku";
        File file = new File(path, titleInput.getText().toString());

        if (!file.exists()) {
            Toast.makeText(this, "Catatan tidak dapat ditemukan", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();

            while (line != null) {
                text.append(line);
                line = br.readLine();
            }
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "Catatan tidak dapat ditemukan", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Isi catatan tidak dapat dibaca", Toast.LENGTH_SHORT).show();
        }

        note = text.toString();
        noteInput.setText(note);
    }

    protected void writeNote() {
        String state = Environment.getExternalStorageState();

        if (!state.equals(Environment.MEDIA_MOUNTED)) return;

        String path = getExternalFilesDir(null) + "/catatanku";
        File parent = new File(path);

        if (!parent.exists()) parent.mkdirs();

        try {
            File file = new File(path, titleInput.getText().toString());
            FileOutputStream outputStream = new FileOutputStream(file, false);

            file.createNewFile();
            outputStream.write(noteInput.getText().toString().getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            Toast.makeText(this, "Tidak dapat menyimpan catatan", Toast.LENGTH_SHORT).show();
        }

        this.finish();
    }
}

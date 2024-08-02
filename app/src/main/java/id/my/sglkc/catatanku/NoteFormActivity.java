package id.my.sglkc.catatanku;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
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
import java.nio.charset.StandardCharsets;

public class NoteFormActivity extends AppCompatActivity {
    private String NOTES_DIR;
    EditText titleInput, noteInput;
    Button saveButton, backButton;
    String title, note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_note_form);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // definisi komponen
        NOTES_DIR = Account.getNotesDir();
        titleInput = findViewById(R.id.titleInput);
        noteInput = findViewById(R.id.noteInput);
        saveButton = findViewById(R.id.saveButton);
        backButton = findViewById(R.id.backButton);
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
        backButton.setOnClickListener(view -> getOnBackPressedDispatcher().onBackPressed());
    }

    protected void readNote() {
        File file = new File(NOTES_DIR, titleInput.getText().toString());

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
        } catch (IOException e) {
            Log.e("NoteFormActivity", e.toString());
            Toast.makeText(this, "Isi catatan tidak dapat dibaca", Toast.LENGTH_SHORT).show();
        }

        note = text.toString();
        noteInput.setText(note);
    }

    protected void writeNote() {
        String state = Environment.getExternalStorageState();

        if (!state.equals(Environment.MEDIA_MOUNTED)) return;

        String title = titleInput.getText().toString();
        String note = noteInput.getText().toString();
        
        if (title.isEmpty() || note.isEmpty()) {
            Toast.makeText(this, "Judul dan isi tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        }
        File parent = new File(NOTES_DIR);

        if (!parent.exists()) parent.mkdirs();

        try {
            File file = new File(parent, title);
            FileOutputStream outputStream = new FileOutputStream(file, false);

            file.createNewFile();
            outputStream.write(note.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            Log.e("NoteFormActivity", e.toString());
            Toast.makeText(this, "Tidak dapat menyimpan catatan", Toast.LENGTH_SHORT).show();
        }

        this.finish();
    }
}

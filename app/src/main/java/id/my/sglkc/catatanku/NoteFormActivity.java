package id.my.sglkc.catatanku;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import java.text.SimpleDateFormat;
import java.util.Date;

import id.my.sglkc.catatanku.database.LoggedInTable;
import id.my.sglkc.catatanku.database.NotesTable;

public class NoteFormActivity extends AppCompatActivity {
    EditText titleInput, noteInput;
    Button saveButton, backButton;
    String id, title, note, modified;

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
        titleInput = findViewById(R.id.titleInput);
        noteInput = findViewById(R.id.noteInput);
        saveButton = findViewById(R.id.saveButton);
        backButton = findViewById(R.id.backButton);
        modified = new SimpleDateFormat("dd MMM YYYY HH:mm:ss").format(new Date());
        Bundle extras = getIntent().getExtras();

        // jika ada state masuk, maka mode edit
        if (extras != null) {
            id = extras.getString("id");
            getSupportActionBar().setTitle("Ubah Catatan");
            readNote();
        } else {
            getSupportActionBar().setTitle("Tambah catatan");
        }

        saveButton.setOnClickListener(view -> writeNote());
        backButton.setOnClickListener(view -> getOnBackPressedDispatcher().onBackPressed());
    }

    protected void readNote() {
        SQLiteDatabase db = Account.dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                NotesTable.TABLE,
                null,
                NotesTable.ID + " = ?",
                new String[] { id },
                null,
                null,
                null);

        if (!cursor.moveToFirst()) {
            Toast.makeText(this, "Catatan tidak dapat ditemukan", Toast.LENGTH_SHORT).show();
            return;
        }

        String title = cursor.getString(cursor.getColumnIndexOrThrow(NotesTable.TITLE));
        String content = cursor.getString(cursor.getColumnIndexOrThrow(NotesTable.CONTENT));

        titleInput.setText(title);
        noteInput.setText(content);
    }

    protected void writeNote() {
        SQLiteDatabase db = Account.dbHelper.getWritableDatabase();
        title = titleInput.getText().toString();
        note = noteInput.getText().toString();
        
        if (title.isEmpty() || note.isEmpty()) {
            Toast.makeText(this, "Judul dan isi tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();

        values.put(NotesTable.USERNAME, Account.username);
        values.put(NotesTable.TITLE, title);
        values.put(NotesTable.CONTENT, note);
        values.put(NotesTable.MODIFIED, modified);

        if (id != null) {
            db.update(
                    NotesTable.TABLE,
                    values,
                    NotesTable.ID + " = ?",
                    new String[] { id }
            );
        } else {
            db.insert(NotesTable.TABLE, null, values);
        }

        this.finish();
    }
}

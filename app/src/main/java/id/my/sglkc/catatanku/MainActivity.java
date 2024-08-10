package id.my.sglkc.catatanku;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import id.my.sglkc.catatanku.database.LoggedInTable;
import id.my.sglkc.catatanku.database.NotesTable;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    FloatingActionButton addButton;
    ArrayList<Map<String, Object>> notes;
    SimpleAdapter simpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // definisi komponen activity
        listView = findViewById(R.id.listView);
        addButton = findViewById(R.id.addButton);
        notes = new ArrayList();
        simpleAdapter = new SimpleAdapter(this, notes, android.R.layout.simple_list_item_2,
                new String[]{"name", "date"}, new int[]{android.R.id.text1, android.R.id.text2});

        listView.setAdapter(simpleAdapter);

        // klik catatan untuk edit
        listView.setOnItemClickListener((adapterView, view, i, l) -> updateNote(i));

        // klik tahan catatan untuk hapus
        listView.setOnItemLongClickListener((adapterView, view, i, l) -> deleteNote(i));

        // klik tombol untuk tambah
        addButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NoteFormActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logoutItem) {
            new AlertDialog.Builder(this)
                    .setTitle("Keluar dari akun ini?")
                    .setPositiveButton("Keluar", (dialog, whichButton) -> {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        Account.logout();
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("Kembali", null)
                    .show();
        } else {
            return false;
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshNotes();
    }

    protected void refreshNotes() {
        if (Account.username == null) return;

        SQLiteDatabase db = Account.dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                NotesTable.TABLE,
                null,
                NotesTable.USERNAME + " = ?",
                new String[]{ Account.username },
                null,
                null,
                null);

        notes.clear();

        while (cursor.moveToNext()) {
            Map<String, Object> note = new HashMap<>();
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(NotesTable.ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(NotesTable.TITLE));
//            String content = cursor.getString(cursor.getColumnIndexOrThrow(NotesTable.CONTENT));
            String modifed = cursor.getString(cursor.getColumnIndexOrThrow(NotesTable.MODIFIED));

            note.put("id", String.valueOf(id));
            note.put("name", title);
            note.put("date", modifed);
            notes.add(0, note);
        }

        simpleAdapter.notifyDataSetChanged();
    }

    protected void updateNote(int index) {
        Intent intent = new Intent(MainActivity.this, NoteFormActivity.class);
        Map<String, Object> data = (Map<String, Object>) simpleAdapter.getItem(index);
        String id = data.get("id").toString();
        String title = data.get("name").toString();

        Toast.makeText(this, "Mengubah catatan " + title, Toast.LENGTH_SHORT).show();
        intent.putExtra("id", id);
        startActivity(intent);
    }

    protected boolean deleteNote(int index) {
        SQLiteDatabase db = Account.dbHelper.getWritableDatabase();
        Map<String, Object> data = (Map<String, Object>) simpleAdapter.getItem(index);
        String id = data.get("id").toString();
        String title = data.get("name").toString();

        new AlertDialog.Builder(this)
                .setTitle("Hapus catatan "+ title + "?")
                .setPositiveButton("Hapus", (dialog, whichButton) -> {
                    db.delete(NotesTable.TABLE, NotesTable.ID + " = ?", new String[] { id });
                    refreshNotes();
                })
                .setNegativeButton("Kembali", null)
                .show();

        return true;
    }
}

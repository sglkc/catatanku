package id.my.sglkc.catatanku;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {
    final int REQUEST_CODE = 100;
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

        // mengizinkan akses penyimpanan
        getStoragePermission();

        // definisi komponen activity
        listView = findViewById(R.id.listView);
        addButton = findViewById(R.id.addButton);
        notes = new ArrayList<Map<String, Object>>();
        simpleAdapter = new SimpleAdapter(this, notes, android.R.layout.simple_list_item_2,
                new String[]{"name", "date"}, new int[]{android.R.id.text1, android.R.id.text2});

        listView.setAdapter(simpleAdapter);

        // klik catatan untuk edit
        listView.setOnItemClickListener((adapterView, view, i, l) -> updateNote(i));

        // klik tahan catatan untuk hapus
        listView.setOnItemLongClickListener((adapterView, view, i, l) -> deleteNote(i));

        // klik tombol untuk tambah
        addButton.setOnClickListener(view -> addNote());
    }

    private boolean getStoragePermission() {
        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            return false;
        }
        
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (REQUEST_CODE != 112) return;

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            refreshNotes();
        } else {
            Toast.makeText(this, "Izinkan akses penyimpanan untuk menyimpan catatan", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshNotes();
    }

    protected void addNote() {
        Intent intent = new Intent(MainActivity.this, NoteFormActivity.class);
        startActivity(intent);
    }

    protected void refreshNotes() {
        String path = getExternalFilesDir(null) + "/catatanku";
        File directory = new File(path);

        if (!directory.exists()) return;

        File[] files = directory.listFiles();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM YYYY HH:mm:ss");

        notes.clear();

        for (File file : files) {
            String filename = file.getName();
            String lastModified = simpleDateFormat.format(file.lastModified());
            Map<String, Object> note = new HashMap<>();

            note.put("name", filename);
            note.put("date", lastModified);
            notes.add(0, note);
        }

        simpleAdapter.notifyDataSetChanged();
    }

    protected void updateNote(int id) {
        Intent intent = new Intent(MainActivity.this, NoteFormActivity.class);
        Map<String, Object> data = (Map<String, Object>) simpleAdapter.getItem(id);
        String name = data.get("name").toString();

        Toast.makeText(this, "Mengubah catatan " + name, Toast.LENGTH_SHORT).show();
        intent.putExtra("name", name);
        startActivity(intent);
    }

    protected boolean deleteNote(int id) {
        Map<String, Object> data = (Map<String, Object>) simpleAdapter.getItem(id);
        String filename = data.get("name").toString();
        String path = getExternalFilesDir(null) + "/catatan";

        new AlertDialog.Builder(this)
                .setTitle("Hapus catatan "+ filename + "?")
                .setPositiveButton("Hapus", (dialog, whichButton) -> {
                    File file = new File(path, filename);
                    if (file.exists()) file.delete();
                })
                .setNegativeButton("Kembali", null)
                .show();

        return true;
    }
}

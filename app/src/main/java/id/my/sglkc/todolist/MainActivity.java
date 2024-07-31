package id.my.sglkc.catatanku;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    FloatingActionButton addButton;
    ArrayList<Map<String, Object>> todoItems;
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

        // definisi atribut
        listView = findViewById(R.id.listView);
        addButton = findViewById(R.id.addButton);
        todoItems = new ArrayList<Map<String, Object>>();
        simpleAdapter = new SimpleAdapter(this, todoItems, android.R.layout.simple_list_item_2,
                new String[]{"name", "date"}, new int[]{android.R.id.text1, android.R.id.text2});

        listView.setAdapter(simpleAdapter);
        addButton.setOnClickListener(view -> onAddButtonClick());
    }

    protected void onAddButtonClick() {
        final EditText editText = new EditText(this);

        editText.setHint("Lakukan sesuatu...");

        new AlertDialog.Builder(this)
                .setTitle("Tambah todo")
                .setMessage("Tambah kegiatan yang ingin dilakukan!")
                .setView(editText)
                .setPositiveButton("Tambah", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String url = editText.getText().toString();
                        moustachify(null, url);
                    }
                })
                .setNegativeButton("Kembali", null)
                .show();
    }

    protected void onDeleteClick(int id) {
        new AlertDialog.Builder(this)
                .setTitle("Tambah todo")
                .setMessage("Tambah kegiatan yang ingin dilakukan!")
                .setView(editText)
                .setPositiveButton("Tambah", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String url = editText.getText().toString();
                        moustachify(null, url);
                    }
                })
                .setNegativeButton("Kembali", null)
                .show();
    }
}
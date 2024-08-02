package id.my.sglkc.catatanku;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class Account extends AppCompatActivity {
    public enum Responses { SUCCESS, ERROR, NOT_FOUND, WRONG_PASSWORD, ALREADY_EXISTS };
    private final static String ACCOUNTS_DIR = "/accounts"
            , CREDENTIALS = "credentials.txt"
            , DELIMITER = ";;";
    private static String STORAGE_DIR, EXTERNAL_DIR;
    public static String username, password, email, name, school, address = "";

    public static void setContext(Context context) {
        // penyimpanan internal (rahasia)
        Account.STORAGE_DIR = context.getFilesDir().toString();
        // penyimpanan external (Android/data/...)
        Account.EXTERNAL_DIR = context.getExternalFilesDir(null).toString();
        File accountsDir = new File(STORAGE_DIR + ACCOUNTS_DIR);

        if (!accountsDir.exists()) accountsDir.mkdirs();
    }

    public static void setDetails(String ...args) {
        username = args[0];
        password = args[1];
        email = args[2];
        name = args[3];
        school = args[4];
        address = args[5];
    }

    public static String getNotesDir() {
        return EXTERNAL_DIR + "/" + username;
    }

    public static String readFile(File file) throws IOException {
        return readFile(file.toString());
    }

    public static String readFile(String filename) throws IOException {
        StringBuilder text = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line = br.readLine();

        while (line != null) {
            text.append(line);
            line = br.readLine();
        }

        br.close();

        return text.toString();
    }

    protected static void writeFile(File file, String contents) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(file, false);
        file.createNewFile();
        outputStream.write(contents.toString().getBytes());
        outputStream.flush();
        outputStream.close();
    }

    public static Responses register(String[] data) {
        String username = data[0];
        String contents = String.join(DELIMITER, data);
        File account = new File(STORAGE_DIR + ACCOUNTS_DIR, username);

        if (account.exists()) return Responses.ALREADY_EXISTS;

        try {
            // create notes folder
            File notes = new File(getNotesDir());
            notes.mkdirs();

            // write account details
            writeFile(account, contents);
        } catch (IOException e) {
            Log.e("Account", e.toString());
            return Responses.ERROR;
        }

        Log.i("ACCOUNT", contents);
        login(data[0], data[1]);
        return Responses.SUCCESS;
    }

    public static Responses login(String username, String password) {
        File account = new File(STORAGE_DIR + ACCOUNTS_DIR, username);

        if (!account.exists()) return Responses.NOT_FOUND;

        String contents;

        try {
            contents = readFile(account);
        } catch (IOException e) {
            Log.e("Account", e.toString());
            return Responses.ERROR;
        }

        String[] data = contents.split(DELIMITER);

        Log.i("ACCOUNT", contents);
        if (!data[1].equals(password)) return Responses.WRONG_PASSWORD;

        setDetails(data);
        saveLogin();
        return Responses.SUCCESS;
    }

    public static boolean saveLogin() {
        String credentials = username + DELIMITER + password;
        File file = new File(STORAGE_DIR, CREDENTIALS);

        try {
            writeFile(file, credentials);
        } catch (IOException e) {
            Log.e("Account", e.toString());
            return false;
        }

        return true;
    }

    public static boolean alreadyLoggedIn() {
        File file = new File(STORAGE_DIR, CREDENTIALS);

        if (!file.exists()) return false;

        try {
            String text = readFile(file);
            String[] data = text.split(DELIMITER);

            login(data[0], data[1]);
        } catch (IOException e) {
            Log.e("Account", e.toString());
            return false;
        }

        return true;
    }

    public static void logout() {
        File file = new File(STORAGE_DIR, CREDENTIALS);

        if (file.exists()) file.delete();
    }
}

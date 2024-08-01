package id.my.sglkc.catatanku;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class Account extends AppCompatActivity {
    public enum Responses { SUCCESS, ERROR, NOT_FOUND, WRONG_PASSWORD, ALREADY_EXISTS };
    private final static String ACCOUNTS_DIR = "/accounts", CREDENTIALS = "credentials.txt";
    private static String STORAGE_DIR;
    public static String username, password, email, name, school, address = "";

    public static void setContext(Context context) {
        Account.STORAGE_DIR = context.getApplicationContext().getFilesDir().toString();
    }

    public static void setDetails(String ...args) {
        username = args[0];
        password = args[1];
        email = args[2];
        name = args[3];
        school = args[4];
        address = args[5];
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

    public static Responses register(String[] data) {
        String username = data[0];
        String contents = String.join(",", data);
        File account = new File(STORAGE_DIR + ACCOUNTS_DIR, username);

        if (account.exists()) return Responses.ALREADY_EXISTS;

        try {
            writeFile(account, contents);
        } catch (IOException e) {
            return Responses.ERROR;
        }

        login(data[0], data[1]);
        return Responses.SUCCESS;
    }

    public static Responses login(String username, String password) {
        File account = new File(STORAGE_DIR + ACCOUNTS_DIR, username);

        if (!account.exists()) return Responses.NOT_FOUND;

        String text;

        try {
            text = readFile(account);
        } catch (IOException e) {
            return Responses.ERROR;
        }

        String[] data = text.split(";");

        if (!data[1].equals(password)) return Responses.WRONG_PASSWORD;

        setDetails(data);
        saveLogin();
        return Responses.SUCCESS;
    }

    protected static void writeFile(File file, String contents) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(file, false);
        file.createNewFile();
        outputStream.write(contents.toString().getBytes());
        outputStream.flush();
        outputStream.close();
    }

    public static boolean saveLogin() {
        String credentials = username + ";" + password;
        File file = new File(STORAGE_DIR, CREDENTIALS);

        try {
            writeFile(file, credentials);
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public static boolean alreadyLoggedIn() {
        File file = new File(STORAGE_DIR, CREDENTIALS);

        if (!file.exists()) return false;

        try {
            String text = readFile(file);
            String[] data = text.split(";");

            setDetails(data);
        } catch (IOException e) {
            return false;
        }

        return true;
    }
}
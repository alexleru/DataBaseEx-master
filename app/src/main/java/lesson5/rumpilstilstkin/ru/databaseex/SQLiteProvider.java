package lesson5.rumpilstilstkin.ru.databaseex;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

public class SQLiteProvider implements Closeable {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public SQLiteProvider(Context context) {
        dbHelper = new DatabaseHelper(context);
        open();
    }

    // Открывает базу данных
    void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    @Override
    public void close(){
        database.close();
    }

    // Добавить новую запись
    public void insertItem(String login, String id, String avatar) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ID, id);
        values.put(DatabaseHelper.COLUMN_LOGIN, login);
        values.put(DatabaseHelper.COLUMN_AVATAR, avatar);
        // Добавление записи
        database.insertWithOnConflict(DatabaseHelper.TABLE_NAME, null,
                values, SQLiteDatabase.CONFLICT_IGNORE);
    }
    // Выбрать записи
    public List<Users> selectItems (){
        List<Users> users = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME, null);
        if (cursor != null && cursor.moveToFirst()){
            int i = 0;
            do {
                Users user = new Users(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_LOGIN)),
                                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_LOGIN)),
                                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_LOGIN)));
                users.add(user);
            }while (cursor.moveToNext());
        }
        try { cursor.close(); } catch (Exception ignored) {}
        return users;
    }


    public int size (){
        Users [] users;
        Cursor cursor = database.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME, null);
        return cursor.getCount();
    }

    // Очистить таблицу
    public int deleteAll() {
        return database.delete(DatabaseHelper.TABLE_NAME, null, null);
    }

}

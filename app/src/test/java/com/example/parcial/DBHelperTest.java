package com.example.parcial;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowLog;

@RunWith(RobolectricTestRunner.class)
public class DBHelperTest {

    private DBHelper dbHelper;
    private SQLiteDatabase database;

    @Before
    public void setUp() {
        ShadowLog.stream = System.out;
        Context context = RuntimeEnvironment.application.getApplicationContext();
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    @After
    public void tearDown() {
        database.close();
        dbHelper.close();
    }

    @Test
    public void insertUser_insertsUserIntoDatabase() {
        String username = "testuser";
        String password = "testpassword";
        dbHelper.insertUser(username, password);

        Cursor cursor = database.rawQuery("SELECT * FROM users WHERE username = ?", new String[]{username});
        assertTrue(cursor.moveToFirst());

        int id = cursor.getInt(cursor.getColumnIndex("id"));
        String dbUsername = cursor.getString(cursor.getColumnIndex("username"));
        String dbPassword = cursor.getString(cursor.getColumnIndex("password"));

        assertEquals(username, dbUsername);
        assertEquals(password, dbPassword);

        cursor.close();
    }

    @Test
    public void checkUser_returnsTrueIfUserExistsInDatabase() {
        String username = "Antony";
        String password = "Carrascal";
        dbHelper.insertUser(username, password);

        boolean result = dbHelper.checkUser(username, password);

        assertTrue(result);
        System.out.println("El test de verificación usuario en la base de datos paso la prueba");
    }

    @Test
    public void checkUser_returnsFalseIfUserDoesNotExistInDatabase() {
        String username = "Jabonoso";
        String password = "Gutierrez";
        dbHelper.insertUser(username, password);

        boolean result = dbHelper.checkUser("Jabi", "Guti");

        assertFalse(result);
        System.out.println("El test de verificación de datos que no existen en la base de datos paso");
    }

    @Test
    public void testCheckUser() {
        dbHelper.insertUser("Jorge", "Elcurioso2");

        System.out.println("El test de verificación de usuario con datos iguales paso la prueba");
        assertTrue(dbHelper.checkUser("Jorge", "Elcurioso2"));
    }

    @Test
    public void testCheckNonExistingUser() {
        assertFalse(dbHelper.checkUser("no_existe", "contraseña"));
        System.out.println("El test de verificación de no existe el usuario no paso la prueba");
    }

    @Test
    public void testCheckUserWithIncorrectPassword() {
        dbHelper.insertUser("Federico", "cardenas33");
        assertFalse(dbHelper.checkUser("Federico", "cardenas3"));
        System.out.println("El test de verificación de datos con datos erroneos paso la prueba");
    }
}
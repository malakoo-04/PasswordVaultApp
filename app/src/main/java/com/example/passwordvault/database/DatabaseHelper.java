package com.example.passwordvault.database;

import android.content.*;import android.database.Cursor;import android.database.sqlite.*;
import com.example.passwordvault.models.PasswordModel;import java.text.SimpleDateFormat;import java.util.*;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME="vault.db"; private static final int DB_VERSION=1;
    public DatabaseHelper(Context c){super(c,DB_NAME,null,DB_VERSION);} 
    public void onCreate(SQLiteDatabase db){ db.execSQL("CREATE TABLE passwords(id INTEGER PRIMARY KEY AUTOINCREMENT, serviceName TEXT, username TEXT, passwordEncrypted TEXT, website TEXT, note TEXT, createdAt TEXT)"); }
    public void onUpgrade(SQLiteDatabase db,int o,int n){db.execSQL("DROP TABLE IF EXISTS passwords");onCreate(db);} 
    public long insertPassword(String service,String user,String pass,String web,String note){ ContentValues v=new ContentValues(); v.put("serviceName",service);v.put("username",user);v.put("passwordEncrypted",pass);v.put("website",web);v.put("note",note);v.put("createdAt",new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.getDefault()).format(new Date())); return getWritableDatabase().insert("passwords",null,v);} 
    public int updatePassword(int id,String service,String user,String pass,String web,String note){ ContentValues v=new ContentValues(); v.put("serviceName",service);v.put("username",user);v.put("passwordEncrypted",pass);v.put("website",web);v.put("note",note); return getWritableDatabase().update("passwords",v,"id=?",new String[]{String.valueOf(id)});} 
    public void deletePassword(int id){ getWritableDatabase().delete("passwords","id=?",new String[]{String.valueOf(id)}); }
    public PasswordModel getPassword(int id){ Cursor c=getReadableDatabase().rawQuery("SELECT * FROM passwords WHERE id=?",new String[]{String.valueOf(id)}); PasswordModel p=null; if(c.moveToFirst()) p=fromCursor(c); c.close(); return p; }
    public ArrayList<PasswordModel> getAllPasswords(){ ArrayList<PasswordModel> list=new ArrayList<>(); Cursor c=getReadableDatabase().rawQuery("SELECT * FROM passwords ORDER BY serviceName",null); while(c.moveToNext()) list.add(fromCursor(c)); c.close(); return list; }
    public ArrayList<PasswordModel> searchPasswords(String q){ ArrayList<PasswordModel> list=new ArrayList<>(); Cursor c=getReadableDatabase().rawQuery("SELECT * FROM passwords WHERE serviceName LIKE ? OR username LIKE ? ORDER BY serviceName",new String[]{"%"+q+"%","%"+q+"%"}); while(c.moveToNext()) list.add(fromCursor(c)); c.close(); return list; }
    private PasswordModel fromCursor(Cursor c){ return new PasswordModel(c.getInt(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6)); }
}

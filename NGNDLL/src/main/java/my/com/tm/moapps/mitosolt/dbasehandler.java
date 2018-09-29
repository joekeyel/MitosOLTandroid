package my.com.tm.moapps.mitosolt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by joe on 2/24/2016.
 */
public class dbasehandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "cabinetverifydatabase.db";
    private static final String TABLECABINET="cabinet";
    private static final String COL_ID="_id";
    private static final String  COL_CABINETID="cabinetname";
    private static final String COL_DSIDEPAIR = "dsidepair";
    private static final String COL_ESIDEPAIR = "esidepair";
    private static final String COL_DSLAMID = "dslamid";
    private static final String COL_DSLIN = "dslin";
    private static final String COL_DSLOUT = "dslout";
    private static final String COL_SUBNUMBER = "subnumber";
    private static final String COL_BNUMBER = "bnumber";

    public dbasehandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = " CREATE TABLE " + TABLECABINET + " (" +
                        COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        COL_CABINETID + " TEXT, " +
                COL_DSIDEPAIR + " TEXT, " +
                COL_ESIDEPAIR + " TEXT, " +
                COL_DSLAMID + " TEXT, " +
                COL_DSLIN + " TEXT, " +
                COL_DSLOUT + " TEXT, " +
                COL_SUBNUMBER + " TEXT, " +
                COL_BNUMBER + " TEXT " +

                ");";
            db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXIST " + TABLECABINET);
        onCreate(db);

    }

    public void addport(CabinetInventory port){

        ContentValues values =new ContentValues();
        values.put(COL_CABINETID,port.getCabinetid());
        values.put(COL_DSIDEPAIR,port.getDsidepair());

        values.put(COL_DSLIN,port.getDslin());
        values.put(COL_ESIDEPAIR,port.getEsidepair());
        values.put(COL_DSLOUT,port.getDslout());
        values.put(COL_SUBNUMBER,port.getSubnumber());
        values.put(COL_BNUMBER,port.getBnumber());


        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLECABINET, null, values);
        db.close();

    }

    public String databasetostr(String cabinet){

        String database = "";
        SQLiteDatabase db = getWritableDatabase();
        String Query = "SELECT * FROM "+ TABLECABINET+" WHERE "+ COL_CABINETID +" = '"+cabinet+"'";

        //cursor pointer

        Cursor c = db.rawQuery(Query,null);
        c.moveToFirst();

        while(!c.isAfterLast()){

            if(c.getString(c.getColumnIndex("cabinetname"))!=null){

                database +=c.getString(c.getColumnIndex("cabinetname"));
                database += ",";
                database +=c.getString(c.getColumnIndex("dsidepair"));
                database += ",";
                database +=c.getString(c.getColumnIndex("esidepair"));
                database += ",";
                database +=c.getString(c.getColumnIndex("dslin"));
                database += ",";
                database +=c.getString(c.getColumnIndex("dslout"));
                database += ",";
                database +=c.getString(c.getColumnIndex("subnumber"));
                database += ",";
                database +=c.getString(c.getColumnIndex("bnumber"));

                database += "\n";
            }
            c.moveToNext();
        }

        db.close();

        return database;

    }
}

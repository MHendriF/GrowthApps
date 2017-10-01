package master;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


/**
 *   PT Trikarya Teknologi on 08/11/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private String createNews = "CREATE TABLE " + NewsClass.table_name +
            "(" + NewsClass.column_id + " INTEGER PRIMARY KEY NOT NULL,"
            + NewsClass.column_status + " INTEGER NOT NULL,"
            + NewsClass.column_judul + " TEXT NOT NULL,"
            + NewsClass.column_headline + " TEXT NOT NULL,"
            + NewsClass.column_content + " TEXT NOT NULL,"
            + NewsClass.column_image + " TEXT NOT NULL,"
            + NewsClass.column_date + " TEXT NOT NULL)";
    private String createBanner = "CREATE TABLE " + Banner.TABLE_NAME+
            "(" + Banner.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + Banner.COLUMN_FOTO + " TEXT NOT NULL,"
            + Banner.COLUMN_TANGGAL + " TEXT NOT NULL)";
    private static final String DATABASE_NAME = "growthco_demoDB",
            KEY_KODE_AREA = "kd_area",
            KEY_TOLERANSI = "toleransi",
            TABLE_CITY = "kota",
            KEY_KODE_KOTA = "kd_kota",
            KEY_NAMA_KOTA = "nama_kota",
            TABLE_LOGGING = "logging",
            KEY_KODE_LOGGING = "kd_logging",
            KEY_DESCRIPTION = "description",
            KEY_LOG_TIME = "log_time",
            TABLE_OUTLET = "outlet",
            KEY_KODE_OUTLET = "kd_outlet",
            KEY_NAMA_OUTLET = "nm_outlet",
            KEY_ALAMAT_OUTLET = "almt_outlet",
            KEY_TIPE_OUTLET = "tipe_outlet",
            KEY_RANK_OUTLET = "rank_outlet",
            KEY_TELP_OUTLET = "telp_outlet",
            KEY_LONGITUDE = "longitude",
            KEY_LATITUDE = "latitude",
            KEY_REGISTER_STATUS = "reg_status",
            KEY_STATUS_AREA = "status_area",
            TABLE_PHOTO = "photo",
            KEY_KODE_PHOTO = "kd_photo",
            KEY_KODE_KOMPETITOR= "kd_kompetitor",
            KEY_NAMA_PHOTO = "nm_photo",
            KEY_JENIS_PHOTO = "jenis_photo",
            KEY_DATE_TAKE_PHOTO = "date_take_photo",
            KEY_ALAMAT_PHOTO = "alamat_comp_activity",
            KEY_DATE_UPLOAD_PHOTO = "date_upload_photo",
            KEY_KETERANGAN = "keterangan",
            TABLE_USER = "user",
            KEY_KODE_USER = "kd_user",
            KEY_KODE_ROLE = "kd_role",
            KEY_NIK = "NIK",
            KEY_NAMA_USER = "nama",
            KEY_ALAMAT_USER = "alamat",
            KEY_TELEPON = "telepon",
            KEY_FOTO = "foto",
            KEY_USERNAME = "username",
            KEY_PASSWORD = "password",
            KEY_EMAIL = "email",
            KEY_STATUS = "status",
            KEY_GCMID = "id_gcm",
            TABLE_VISITPLAN = "VisitPlan",
            KEY_KODE_VISITPLAN = "kd_visitplan",
            KEY_DATE_VISIT = "date_visit",
            KEY_DATE_CREATE_VISIT = "date_create_visit",
            KEY_APPROVE_VISIT = "approve_visit",
            KEY_STATUS_VISIT = "status_visit",
            KEY_DATE_VISITING = "date_visiting",
            KEY_SKIP_ORDER_REASON = "skip_order_reason",
            KEY_SKIP_REASON = "skip_reason",
            TABLE_DISTRIBUTOR = "Distributor",
            KEY_ID_DISTRIBUTOR = "id",
            KEY_KODE_DISTRIBUTOR = "kd_dist",
            KEY_NAMA_DISTRIBUTOR = "nm_dist",
            KEY_ALAMAT_DISTRIBUTOR = "almt_dist",
            KEY_TELEPON_DISTRIBUTOR = "telp_dist",
            TABLE_TIPE = "Tipe",
            KEY_KODE_TIPE = "kd_tipe",
            KEY_NAMA_TIPE = "nm_tipe",
            TABLE_TIPE_PHOTO = "TipePhoto",
            KEY_ID_TIPE = "id",
            KEY_NM_TIPE = "nama_tipe",
            TABLE_COMPETITOR = "Competitor",
            KEY_KODE_COMPETITOR = "kd_competitor",
            KEY_NAMA_COMPETITOR = "nm_competitor",
            KEY_ALAMAT_COMPETITOR = "alamat_competitor",
            TABLE_PRODUK = "Produk",
            KEY_ID_PRODUK = "id_produk",
            KEY_KODE_PRODUK = "kd_produk",
            KEY_NAMA_PRODUK = "nm_produk",
            TABLE_KONFIGURASI = "Konfigurasi",
            TABLE_ARTICLE = "article",
            KEY_ID_ARTICLE =  "id",
            KEY_JUDUL_ARTICLE = "judul",
            KEY_HEADLINE_ARTICLE = "headline",
            KEY_CONTENT_ARTICLE = "content",
            KEY_PATH_ARTICLE = "image",
            KEY_DATE_ARTICLE = "date_upload",
            KEY_STATUS_ARTICLE = "status",
            KEY_STATUS_APP = "status_app";
    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USER +
                "(" + KEY_KODE_USER + " INTEGER PRIMARY KEY NOT NULL,"
                + KEY_KODE_ROLE + " INTEGER NOT NULL,"
                + KEY_KODE_KOTA + " INTEGER NOT NULL,"
                + KEY_KODE_AREA + " INTEGER NOT NULL,"
                + KEY_NIK + " TEXT NOT NULL,"
                + KEY_NAMA_USER + " TEXT NOT NULL,"
                + KEY_ALAMAT_USER + " TEXT NOT NULL,"
                + KEY_TELEPON + " TEXT NOT NULL,"
                + KEY_FOTO + " TEXT,"
                + KEY_USERNAME + " TEXT NOT NULL,"
                + KEY_PASSWORD + " TEXT NOT NULL,"
                + KEY_EMAIL + " TEXT NOT NULL,"
                + KEY_STATUS + " INTEGER NOT NULL,"
                + KEY_TOLERANSI + " INTEGER,"
                + KEY_GCMID + " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_PHOTO +
                "(" + KEY_KODE_PHOTO + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + KEY_KODE_USER + " INTEGER NOT NULL,"
                + KEY_KODE_OUTLET + " INTEGER NOT NULL,"
                + KEY_KODE_KOMPETITOR + " INTEGER,"
                + KEY_JENIS_PHOTO + " INTEGER,"
                + KEY_NAMA_PHOTO + " TEXT NOT NULL,"
                + KEY_DATE_TAKE_PHOTO + " TEXT NOT NULL,"
                + KEY_ALAMAT_PHOTO + " TEXT,"
                + KEY_DATE_UPLOAD_PHOTO + " TEXT,"
                + KEY_FOTO + " TEXT NOT NULL,"
                + KEY_KETERANGAN + " TEXT NOT NULL)");
        db.execSQL("CREATE TABLE " + TABLE_OUTLET +
                "(" + KEY_KODE_OUTLET + " INTEGER PRIMARY KEY NOT NULL,"
                + KEY_KODE_DISTRIBUTOR + " INTEGER NOT NULL,"
                + KEY_KODE_KOTA + " INTEGER NOT NULL,"
                + KEY_KODE_USER + " INTEGER NOT NULL,"
                + KEY_NAMA_OUTLET + " TEXT NOT NULL,"
                + KEY_ALAMAT_OUTLET + " TEXT NOT NULL,"
                + KEY_TIPE_OUTLET + " TEXT NOT NULL,"
                + KEY_RANK_OUTLET + " TEXT NOT NULL,"
                + KEY_TELP_OUTLET + " TEXT NOT NULL,"
                + KEY_REGISTER_STATUS + " TEXT NOT NULL,"
                + KEY_LATITUDE + " REAL NOT NULL,"
                + KEY_LONGITUDE + " REAL NOT NULL,"
                + KEY_STATUS_AREA + " INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE " + TABLE_CITY +
                "(" + KEY_KODE_KOTA + " INTEGER PRIMARY KEY NOT NULL,"
                + KEY_KODE_AREA + " INTEGER NOT NULL,"
                + KEY_NAMA_KOTA + " TEXT NOT NULL)");
        db.execSQL("CREATE TABLE " + TABLE_LOGGING +
                "(" + KEY_KODE_LOGGING + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + KEY_DESCRIPTION + " INTEGER NOT NULL,"
                + KEY_LOG_TIME + " TEXT NOT NULL)");
        db.execSQL("CREATE TABLE " + TABLE_DISTRIBUTOR +
                "(" + KEY_ID_DISTRIBUTOR + " INTEGER PRIMARY KEY NOT NULL,"
                + KEY_KODE_DISTRIBUTOR + " TEXT NOT NULL,"
                + KEY_KODE_TIPE + " TEXT NOT NULL,"
                + KEY_KODE_KOTA + " INTEGER NOT NULL,"
                + KEY_NAMA_DISTRIBUTOR + " TEXT NOT NULL,"
                + KEY_ALAMAT_DISTRIBUTOR + " TEXT NOT NULL,"
                + KEY_TELEPON_DISTRIBUTOR + " TEXT NOT NULL)");
        db.execSQL("CREATE TABLE " + TABLE_COMPETITOR +
                "(" + KEY_KODE_COMPETITOR + " INTEGER PRIMARY KEY NOT NULL,"
                + KEY_KODE_KOTA + " INTEGER NOT NULL,"
                + KEY_NAMA_COMPETITOR + " TEXT NOT NULL,"
                + KEY_ALAMAT_COMPETITOR + " TEXT NOT NULL)");
        db.execSQL("CREATE TABLE " + TABLE_TIPE +
                "(" + KEY_KODE_TIPE + " INTEGER PRIMARY KEY NOT NULL,"
                + KEY_NAMA_TIPE + " TEXT NOT NULL)");
        db.execSQL("CREATE TABLE " + TABLE_TIPE_PHOTO +
                "(" + KEY_ID_TIPE + " INTEGER PRIMARY KEY NOT NULL,"
                + KEY_NM_TIPE + " TEXT NOT NULL)");
        db.execSQL("CREATE TABLE " + TABLE_PRODUK +
                "(" + KEY_ID_PRODUK + " INTEGER PRIMARY KEY NOT NULL,"
                + KEY_KODE_PRODUK + " TEXT NOT NULL,"
                + KEY_NAMA_PRODUK + " TEXT NOT NULL)");
        db.execSQL("CREATE TABLE " + TABLE_VISITPLAN +
                "(" + KEY_KODE_VISITPLAN + " INTEGER PRIMARY KEY NOT NULL,"
                + KEY_KODE_OUTLET + " INTEGER NOT NULL,"
                + KEY_DATE_VISIT + " TEXT NOT NULL,"
                + KEY_DATE_CREATE_VISIT + " TEXT NOT NULL,"
                + KEY_APPROVE_VISIT + " INTEGER NOT NULL,"
                + KEY_STATUS_VISIT + " INTEGER NOT NULL,"
                + KEY_DATE_VISITING + " TEXT NOT NULL,"
                + KEY_SKIP_ORDER_REASON + " TEXT NOT NULL,"
                + KEY_SKIP_REASON + " REAL NOT NULL)");
        db.execSQL("CREATE TABLE " + TABLE_KONFIGURASI +
                "(" + KEY_STATUS_APP + " INTEGER DEFAULT 0)");
//        db.execSQL("CREATE TABLE " + NewsClass.table_name +
//                "(" + NewsClass.column_id + " INTEGER PRIMARY KEY NOT NULL,"
//                + NewsClass.column_status + " INTEGER NOT NULL,"
//                + NewsClass.column_judul + " TEXT NOT NULL,"
//                + NewsClass.column_headline + " TEXT NOT NULL,"
//                + NewsClass.column_content + " TEXT NOT NULL,"
//                + NewsClass.column_image + " TEXT NOT NULL,"
//                + NewsClass.column_date + " TEXT NOT NULL)");
//        db.execSQL("CREATE TABLE " + Banner.TABLE_NAME+
//                "(" + Banner.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
//                + Banner.COLUMN_FOTO + " TEXT NOT NULL,"
//                + Banner.COLUMN_TANGGAL + " TEXT NOT NULL)");
        db.execSQL(createBanner);
        db.execSQL(createNews);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
    public void createKonfigurasi(Integer status)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_STATUS_APP,status);
        db.insert(TABLE_KONFIGURASI, null, values);
        values.clear();
        db.close();
    }
    public Integer getKonfigurasi()
    {
        Integer status_app = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_KONFIGURASI, new String[]{KEY_STATUS_APP}, null, null, null,null,null,null);
        if(cursor==null)
            return null;
        if(cursor.moveToFirst())
            status_app = cursor.getInt(0);
        cursor.close();
        db.close();
        return status_app;
    }
    public void deleteKonfigurasi()
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_KONFIGURASI, null, null);
        db.close();
    }
    public int getKonfigurasiCount()
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_KONFIGURASI, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }
    public int updateKonfigurasi(Integer status)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_STATUS_APP,status);
        int isUpdate = db.update(TABLE_KONFIGURASI,values,null,null);
        values.clear();
        db.close();
        return isUpdate;
    }
    public void createUser(User user)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_KODE_USER,user.getKode());
        values.put(KEY_KODE_ROLE,user.getKodeRole());
        values.put(KEY_KODE_KOTA,user.getKd_kota());
        values.put(KEY_KODE_AREA,user.getKd_area());
        values.put(KEY_NIK,user.getNIK());
        values.put(KEY_NAMA_USER,user.getNama());
        values.put(KEY_ALAMAT_USER,user.getAlamat());
        values.put(KEY_TELEPON,user.getTelepon());
        values.put(KEY_FOTO,user.getPath_foto());
        values.put(KEY_USERNAME,user.getUsername());
        values.put(KEY_PASSWORD,user.getPassword());
        values.put(KEY_EMAIL,user.getEmail());
        values.put(KEY_STATUS,user.getStatus());
        values.put(KEY_GCMID,user.getGcmId());
        db.insert(TABLE_USER, null, values);
        values.clear();
        db.close();
    }
    public User getUser()
    {
        SQLiteDatabase db = getReadableDatabase();
        User user = null;
        Cursor cursor = db.query(TABLE_USER, new String[]{KEY_KODE_USER, KEY_KODE_ROLE, KEY_KODE_KOTA, KEY_KODE_AREA,
                KEY_NIK, KEY_NAMA_USER, KEY_ALAMAT_USER, KEY_TELEPON, KEY_FOTO, KEY_USERNAME, KEY_PASSWORD, KEY_EMAIL,
                KEY_STATUS,KEY_TOLERANSI,KEY_GCMID}, null, null, null,null,null,null);
        if(cursor==null)
            return null;
        if(cursor.moveToFirst())
            user = new User(cursor.getInt(0),cursor.getInt(1),cursor.getInt(2), cursor.getInt(3)
                    ,cursor.getString(4),cursor.getString(5),cursor.getString(6)
                    ,cursor.getString(7),cursor.getString(8),cursor.getString(9)
                    ,cursor.getString(10),cursor.getString(11),cursor.getInt(12),cursor.getString(14));
            user.setToleransi(cursor.getInt(13));
        cursor.close();
        db.close();
        return user;
    }
    public void deleteUser()
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_USER, null, null);
        db.close();
    }
    public int getUserCount()
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }
    public int updateUser(User user)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_KODE_USER,user.getKode());
        values.put(KEY_KODE_ROLE, user.getKodeRole());
        values.put(KEY_KODE_KOTA, user.getKd_kota());
        values.put(KEY_KODE_AREA, user.getKd_area());
        values.put(KEY_NIK,user.getNIK());
        values.put(KEY_NAMA_USER,user.getNama());
        values.put(KEY_ALAMAT_USER,user.getAlamat());
        values.put(KEY_TELEPON,user.getTelepon());
        values.put(KEY_FOTO,user.getPath_foto());
        values.put(KEY_USERNAME,user.getUsername());
        values.put(KEY_PASSWORD,user.getPassword());
        values.put(KEY_EMAIL,user.getEmail());
        values.put(KEY_STATUS, user.getStatus());
        values.put(KEY_TOLERANSI, user.getToleransi());
        values.put(KEY_GCMID,user.getGcmId());
        int isUpdate = db.update(TABLE_USER,values,KEY_KODE_USER + "=?",new String[]{String.valueOf(user.getKode())});
        values.clear();
        db.close();
        return isUpdate;
    }
    public void createPhoto(PhotoActivity photoActivity)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_KODE_USER,photoActivity.getKd_user());
        values.put(KEY_KODE_OUTLET,photoActivity.getKd_outlet());
        values.put(KEY_KODE_KOMPETITOR,photoActivity.getKd_kompetitor());
        values.put(KEY_NAMA_PHOTO,photoActivity.getNama());
        values.put(KEY_JENIS_PHOTO,photoActivity.getKd_tipe());
        values.put(KEY_DATE_TAKE_PHOTO, photoActivity.getTgl_take());
        values.put(KEY_ALAMAT_PHOTO,photoActivity.getAlamat());
        values.put(KEY_DATE_UPLOAD_PHOTO,photoActivity.getTgl_upload());
        values.put(KEY_KETERANGAN,photoActivity.getKeterangan());
        values.put(KEY_FOTO,photoActivity.getFoto());
        db.insert(TABLE_PHOTO, null, values);
        values.clear();
        db.close();
    }
    public int updatePhoto(PhotoActivity photoActivity)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_KODE_USER,photoActivity.getKd_user());
        values.put(KEY_KODE_OUTLET,photoActivity.getKd_outlet());
        values.put(KEY_KODE_KOMPETITOR,photoActivity.getKd_kompetitor());
        values.put(KEY_NAMA_PHOTO,photoActivity.getNama());
        values.put(KEY_JENIS_PHOTO,photoActivity.getKd_tipe());
        values.put(KEY_DATE_TAKE_PHOTO, photoActivity.getTgl_take());
        values.put(KEY_ALAMAT_PHOTO,photoActivity.getAlamat());
        values.put(KEY_DATE_UPLOAD_PHOTO,photoActivity.getTgl_upload());
        values.put(KEY_KETERANGAN,photoActivity.getKeterangan());
        values.put(KEY_FOTO, photoActivity.getFoto());
        int isUpdate = db.update(TABLE_PHOTO,values,KEY_KODE_PHOTO + "=?",new String[]{String.valueOf(photoActivity.getId())});
        values.clear();
        db.close();
        return isUpdate;
    }
    public PhotoActivity getPhotoOut(int kd_outlet)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_PHOTO, new String[]{KEY_KODE_PHOTO,KEY_KODE_USER, KEY_KODE_OUTLET,
                KEY_KODE_KOMPETITOR, KEY_JENIS_PHOTO, KEY_NAMA_PHOTO, KEY_DATE_TAKE_PHOTO, KEY_ALAMAT_PHOTO,
                KEY_DATE_UPLOAD_PHOTO, KEY_KETERANGAN, KEY_FOTO}, KEY_KODE_OUTLET + "=?",new String[]{String.valueOf(kd_outlet)}, null,null,null,null);
        if(cursor==null)
            return null;
        PhotoActivity photoActivity = null;
        if(cursor.moveToFirst()) {
            photoActivity = new PhotoActivity(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2)
                    , cursor.getInt(3), cursor.getInt(4), cursor.getString(5)
                    , cursor.getString(6), cursor.getString(7)
                    , cursor.getString(9), cursor.getString(8), cursor.getString(10));
        }
        cursor.close();
        db.close();
        return photoActivity;
    }
    public PhotoActivity getPhoto(int kd_photo)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_PHOTO, new String[]{KEY_KODE_PHOTO, KEY_KODE_USER, KEY_KODE_OUTLET,
                KEY_KODE_KOMPETITOR, KEY_JENIS_PHOTO, KEY_NAMA_PHOTO, KEY_DATE_TAKE_PHOTO, KEY_ALAMAT_PHOTO,
                KEY_DATE_UPLOAD_PHOTO, KEY_KETERANGAN, KEY_FOTO}, KEY_KODE_PHOTO + "=?", new String[]{String.valueOf(kd_photo)}, null, null, null, null);
        if(cursor==null)
            return null;
        PhotoActivity photoActivity = null;
        if(cursor.moveToFirst()) {
             photoActivity = new PhotoActivity(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2)
                    , cursor.getInt(3), cursor.getInt(4), cursor.getString(5)
                    , cursor.getString(6), cursor.getString(7)
                    , cursor.getString(9), cursor.getString(8), cursor.getString(10));
        }
        cursor.close();
        db.close();
        return photoActivity;
    }
    public PhotoActivity getPhotoCom(int kd_competitor)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_PHOTO, new String[]{KEY_KODE_PHOTO,KEY_KODE_USER, KEY_KODE_OUTLET,
                KEY_KODE_KOMPETITOR, KEY_JENIS_PHOTO, KEY_NAMA_PHOTO, KEY_DATE_TAKE_PHOTO, KEY_ALAMAT_PHOTO,
                KEY_DATE_UPLOAD_PHOTO, KEY_KETERANGAN, KEY_FOTO}, KEY_KODE_KOMPETITOR + "=?",new String[]{String.valueOf(kd_competitor)}, null,null,null,null);
        if(cursor==null)
            return null;
        PhotoActivity photoActivity = null;
        if(cursor.moveToFirst()) {
            photoActivity = new PhotoActivity(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2)
                    , cursor.getInt(3), cursor.getInt(4), cursor.getString(5)
                    , cursor.getString(6), cursor.getString(7)
                    , cursor.getString(9), cursor.getString(8), cursor.getString(10));
        }
        cursor.close();
        db.close();
        return photoActivity;
    }
    public List<PhotoActivity> getAllPhoto()
    {
        SQLiteDatabase db = getReadableDatabase();
        List<PhotoActivity> photoActivities = new ArrayList<PhotoActivity>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PHOTO, null);
        if(cursor.moveToFirst())
            do{
                PhotoActivity photoActivity = new PhotoActivity(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2)
                        , cursor.getInt(3), cursor.getInt(4), cursor.getString(5)
                        , cursor.getString(6), cursor.getString(7)
                        , cursor.getString(10), cursor.getString(8), cursor.getString(9));
                photoActivities.add(photoActivity);
            }while (cursor.moveToNext());
        cursor.close();
        db.close();
        return photoActivities;
    }
    public void deletePhoto(int kd_foto)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_PHOTO, KEY_KODE_PHOTO + "=" + kd_foto, null);
        db.close();
    }
    public void deleteAllPhoto()
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_PHOTO, null, null);
        db.close();
    }
    public void createOutlet(Outlet outlet)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_KODE_OUTLET,outlet.getKode());
        values.put(KEY_KODE_DISTRIBUTOR,outlet.getKode_distributor());
        values.put(KEY_KODE_KOTA,outlet.getKode_kota());
        values.put(KEY_KODE_USER,outlet.getKode_user());
        values.put(KEY_NAMA_OUTLET,outlet.getNama());
        values.put(KEY_ALAMAT_OUTLET,outlet.getAlamat());
        values.put(KEY_TIPE_OUTLET,outlet.getTipe());
        values.put(KEY_RANK_OUTLET,outlet.getRank());
        values.put(KEY_TELP_OUTLET,outlet.getTelpon());
        values.put(KEY_REGISTER_STATUS,outlet.getReg_status());
        values.put(KEY_LATITUDE,outlet.getLatitude());
        values.put(KEY_LONGITUDE,outlet.getLongitude());
        values.put(KEY_STATUS_AREA,outlet.getStatus_area());
        db.insert(TABLE_OUTLET, null, values);
        values.clear();
        db.close();
    }
    public Outlet getOutlet(int id)
    {
        SQLiteDatabase db = getReadableDatabase();
        Outlet outlet = null;
        Cursor cursor = db.query(TABLE_OUTLET, new String[]{KEY_KODE_OUTLET, KEY_KODE_KOTA, KEY_KODE_USER, KEY_KODE_DISTRIBUTOR, KEY_NAMA_OUTLET, KEY_ALAMAT_OUTLET, KEY_TIPE_OUTLET, KEY_RANK_OUTLET, KEY_TELP_OUTLET, KEY_REGISTER_STATUS, KEY_LATITUDE, KEY_LONGITUDE,KEY_STATUS_AREA}, KEY_KODE_OUTLET + "=?",new String[]{String.valueOf(id)}, null,null,null,null);
        if(cursor==null)
            return null;
        if(cursor.moveToFirst()) {
             outlet = new Outlet(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2)
                    , Integer.parseInt(cursor.getString(3)), cursor.getString(4), cursor.getString(5)
                    , cursor.getInt(6), cursor.getString(7), cursor.getString(8)
                    , cursor.getString(9), cursor.getString(10), cursor.getString(11));
            outlet.setStatus_area(cursor.getInt(12));
        }
        cursor.close();
        db.close();
        return outlet;
    }
    public List<Outlet> searchOutlet(String query)
    {
        SQLiteDatabase db = getReadableDatabase();
        List<Outlet> outletList = new ArrayList<Outlet>();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_OUTLET+" WHERE "+KEY_NAMA_OUTLET+" LIKE '%"+query+"%'",null);
        if(cursor.moveToFirst())
            do{
                Outlet outlet = new Outlet(cursor.getInt(0),cursor.getInt(1),cursor.getInt(2)
                        , Integer.parseInt(cursor.getString(3)), cursor.getString(4), cursor.getString(5)
                        , cursor.getInt(6), cursor.getString(7), cursor.getString(8)
                        , cursor.getString(9), cursor.getString(10), cursor.getString(11));
                outlet.setStatus_area(cursor.getInt(12));
                outletList.add(outlet);
            }while (cursor.moveToNext());
        cursor.close();
        db.close();
        return outletList;
    }
    public List<Outlet> getAllOutlet()
    {
        SQLiteDatabase db = getReadableDatabase();
        List<Outlet> outletList = new ArrayList<Outlet>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_OUTLET, null);
        if(cursor.moveToFirst())
            do{
                Outlet outlet = new Outlet(cursor.getInt(0),cursor.getInt(1),cursor.getInt(2)
                        , Integer.parseInt(cursor.getString(3)), cursor.getString(4), cursor.getString(5)
                        , cursor.getInt(6), cursor.getString(7), cursor.getString(8)
                        , cursor.getString(9), cursor.getString(10), cursor.getString(11));
                outlet.setStatus_area(cursor.getInt(12));
                outletList.add(outlet);
            }while (cursor.moveToNext());
        cursor.close();
        db.close();
        return outletList;
    }
    public void deleteOutlet()
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_OUTLET, null, null);
        db.close();
    }
    public int getOutletCount()
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_OUTLET, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }
    public void createCompetitor(Competitor competitor)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_KODE_COMPETITOR,competitor.getId());
        values.put(KEY_KODE_KOTA,competitor.getKd_kota());
        values.put(KEY_NAMA_COMPETITOR,competitor.getNm_competitor());
        values.put(KEY_ALAMAT_COMPETITOR,competitor.getAlamat());
        db.insert(TABLE_COMPETITOR, null, values);
        values.clear();
        db.close();
    }
    public Competitor getCompetitor(int id)
    {
        SQLiteDatabase db = getReadableDatabase();
        Competitor competitor = null;
        Cursor cursor = db.query(TABLE_COMPETITOR, new String[]{KEY_KODE_COMPETITOR, KEY_KODE_KOTA, KEY_NAMA_COMPETITOR,
                KEY_ALAMAT_COMPETITOR}, KEY_KODE_COMPETITOR + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if(cursor==null)
            return null;
        if(cursor.moveToFirst()){
             competitor = new Competitor(cursor.getInt(0),cursor.getInt(1),cursor.getString(2)
                    , cursor.getString(3));
        }
        cursor.close();
        db.close();
        return competitor;
    }
    public List<Competitor> getAllCompetitor()
    {
        SQLiteDatabase db = getReadableDatabase();
        List<Competitor> competitors = new ArrayList<Competitor>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_COMPETITOR, null);
        if(cursor.moveToFirst())
            do{
                competitors.add(new Competitor(cursor.getInt(0),cursor.getInt(1),cursor.getString(2)
                        , cursor.getString(3)));
            }while (cursor.moveToNext());
        cursor.close();
        db.close();
        return competitors;
    }
    public void deleteCompetitor()
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_COMPETITOR, null, null);
        db.close();
    }
    public int getCompetitorCount()
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_COMPETITOR, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }
    public void createDistributor(Distributor distributor)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_DISTRIBUTOR,distributor.getId());
        values.put(KEY_KODE_DISTRIBUTOR,distributor.getKd_dist());
        values.put(KEY_KODE_TIPE,distributor.getKd_tipe());
        values.put(KEY_KODE_KOTA,distributor.getKd_kota());
        values.put(KEY_NAMA_DISTRIBUTOR,distributor.getNm_dist());
        values.put(KEY_ALAMAT_DISTRIBUTOR,distributor.getAlmt_dist());
        values.put(KEY_TELEPON_DISTRIBUTOR,distributor.getTelp_dist());
        db.insert(TABLE_DISTRIBUTOR, null, values);
        values.clear();
        db.close();
    }
    public Distributor getDistributor(int kd_dist)
    {
        SQLiteDatabase db = getReadableDatabase();
        Distributor distributor = null;
        Cursor cursor = db.query(TABLE_DISTRIBUTOR, new String[]{KEY_ID_DISTRIBUTOR, KEY_KODE_DISTRIBUTOR, KEY_KODE_TIPE, KEY_KODE_KOTA, KEY_NAMA_DISTRIBUTOR, KEY_ALAMAT_DISTRIBUTOR, KEY_TELEPON_DISTRIBUTOR}, KEY_ID_DISTRIBUTOR + "=?", new String[]{String.valueOf(kd_dist)}, null, null, null, null);
        if(cursor.moveToFirst()){
            distributor = new Distributor(cursor.getInt(0),cursor.getString(1),
                    cursor.getString(2),cursor.getInt(3),cursor.getString(4),
                    cursor.getString(5),cursor.getString(6));
        }
        cursor.close();
        db.close();
        return distributor;
    }
    public List<Distributor> getAllDistributor()
    {
        SQLiteDatabase db = getWritableDatabase();
        List<Distributor> distributorList = new ArrayList<Distributor>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_DISTRIBUTOR, null);
        if(cursor.moveToFirst())
            do{
                Distributor distributor = new Distributor(cursor.getInt(0),cursor.getString(1),
                        cursor.getString(2),cursor.getInt(3),cursor.getString(4),
                        cursor.getString(5),cursor.getString(6));
                distributorList.add(distributor);
            }while(cursor.moveToNext());
        cursor.close();
        db.close();
        return distributorList;
    }
    public void deleteDistributor()
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_DISTRIBUTOR, null, null);
        db.close();
    }
    public int getDistributorCount()
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_DISTRIBUTOR, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }
    public void createCity(City city)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_KODE_KOTA,city.getKode());
        values.put(KEY_KODE_AREA,city.getKodeArea());
        values.put(KEY_NAMA_KOTA,city.getNama());
        db.insert(TABLE_CITY, null, values);
        values.clear();
        db.close();
    }
    public City getCity(int kd_kota)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_CITY, new String[]{KEY_KODE_KOTA, KEY_KODE_AREA, KEY_NAMA_KOTA}, KEY_KODE_KOTA + "=?",new String[]{String.valueOf(kd_kota)}, null,null,null,null);
        if(cursor==null)
            return null;
        cursor.moveToFirst();
        City city = new City(cursor.getInt(0),cursor.getInt(1),
                cursor.getString(2));
        cursor.close();
        db.close();
        return city;
    }
    public List<City> getAllCity()
    {
        SQLiteDatabase db = getWritableDatabase();
        List<City> cityList = new ArrayList<City>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CITY, null);
        if(cursor.moveToFirst())
            do{
                City city = new City(cursor.getInt(0),cursor.getInt(1),
                        cursor.getString(2));
                cityList.add(city);
            }while(cursor.moveToNext());
        cursor.close();
        db.close();
        return cityList;
    }
    public void deleteCity()
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_CITY, null, null);
        db.close();
    }
    public int getCityCount()
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CITY, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }
    public void createLog(Logging logging)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DESCRIPTION,logging.getDescription());
        values.put(KEY_LOG_TIME,logging.getLog_time());
        db.insert(TABLE_LOGGING, null, values);
        values.clear();
        db.close();
    }
    public List<Logging> getAllLog()
    {
        SQLiteDatabase db = getWritableDatabase();
        List<Logging> loggings = new ArrayList<Logging>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_LOGGING, null);
        if(cursor.moveToFirst())
            do{
                Logging logging = new Logging(cursor.getInt(0),cursor.getString(1),
                        cursor.getString(2));
                loggings.add(logging);
            }while(cursor.moveToNext());
        cursor.close();
        db.close();
        return loggings;
    }
    public void deleteLog()
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_LOGGING, null, null);
        db.close();
    }
    public void createVisitPlan(VisitPlanDb visitPlanDb)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_KODE_VISITPLAN,visitPlanDb.getKd_visit());
        values.put(KEY_KODE_OUTLET,visitPlanDb.getKd_outlet());
        values.put(KEY_DATE_VISIT,visitPlanDb.getDate_visit());
        values.put(KEY_DATE_CREATE_VISIT,visitPlanDb.getDate_created());
        values.put(KEY_APPROVE_VISIT,visitPlanDb.getApprove_visit());
        values.put(KEY_STATUS_VISIT,visitPlanDb.getStatus_visit());
        values.put(KEY_DATE_VISITING,visitPlanDb.getDate_visiting());
        values.put(KEY_SKIP_ORDER_REASON,visitPlanDb.getSkip_order_reason());
        values.put(KEY_SKIP_REASON,visitPlanDb.getSkip_reason());
        db.insert(TABLE_VISITPLAN, null, values);
        values.clear();
        db.close();
    }
    public int updateVisitPlan(VisitPlanDb visitPlanDb)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_KODE_VISITPLAN,visitPlanDb.getKd_visit());
        values.put(KEY_KODE_OUTLET,visitPlanDb.getKd_outlet());
        values.put(KEY_DATE_VISIT,visitPlanDb.getDate_visit());
        values.put(KEY_DATE_CREATE_VISIT,visitPlanDb.getDate_created());
        values.put(KEY_APPROVE_VISIT,visitPlanDb.getApprove_visit());
        values.put(KEY_STATUS_VISIT,visitPlanDb.getStatus_visit());
        values.put(KEY_DATE_VISITING,visitPlanDb.getDate_visiting());
        values.put(KEY_SKIP_ORDER_REASON,visitPlanDb.getSkip_order_reason());
        values.put(KEY_SKIP_REASON, visitPlanDb.getSkip_reason());
        int isUpdate = db.update(TABLE_VISITPLAN, values, KEY_KODE_VISITPLAN + "=?", new String[]{String.valueOf(visitPlanDb.getKd_visit())});
        values.clear();
        db.close();
        return isUpdate;
    }
    public VisitPlanDb getVisitPlan(int kd_visit)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_VISITPLAN, new String[]{KEY_KODE_VISITPLAN,KEY_KODE_OUTLET,KEY_DATE_VISIT,KEY_DATE_CREATE_VISIT,KEY_APPROVE_VISIT,KEY_STATUS_VISIT,KEY_DATE_VISITING,KEY_SKIP_ORDER_REASON,KEY_SKIP_REASON}, KEY_KODE_VISITPLAN + "=?",new String[]{String.valueOf(kd_visit)}, null, null, null, null);
        if(cursor==null)
            return null;
        cursor.moveToFirst();
        VisitPlanDb visitPlanDb = new VisitPlanDb(cursor.getInt(0),cursor.getInt(1),cursor.getString(2)
                , cursor.getString(3), cursor.getInt(4), cursor.getInt(5)
                , cursor.getString(6), cursor.getString(7), cursor.getString(8));
        cursor.close();
        db.close();
        return visitPlanDb;
    }
    public List<VisitPlanDb> getAllVisitPlan()
    {
        SQLiteDatabase db = getReadableDatabase();
        List<VisitPlanDb> visitPlanDbs = new ArrayList<VisitPlanDb>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_VISITPLAN, null);
        if(cursor.moveToFirst())
            do{
                VisitPlanDb visitPlanDb = new VisitPlanDb(cursor.getInt(0),cursor.getInt(1),cursor.getString(2)
                        , cursor.getString(3), cursor.getInt(4), cursor.getInt(5)
                        , cursor.getString(6), cursor.getString(7), cursor.getString(8));
                visitPlanDbs.add(visitPlanDb);
            }while (cursor.moveToNext());
        cursor.close();
        db.close();
        return visitPlanDbs;
    }
    public void deleteVisitPlan()
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_VISITPLAN, null, null);
        db.close();
    }
    public int getVisitPlanCount()
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_VISITPLAN, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }
    public void createTipe(Tipe tipe)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_KODE_TIPE,tipe.getId());
        values.put(KEY_NAMA_TIPE,tipe.getNm_tipe());
        db.insert(TABLE_TIPE, null, values);
        values.clear();
        db.close();
    }
    public Tipe getTipe(int kd_tipe)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_TIPE, new String[]{KEY_KODE_TIPE, KEY_NAMA_TIPE}, KEY_KODE_TIPE + "=?",new String[]{String.valueOf(kd_tipe)}, null,null,null,null);
        if(cursor==null)
            return null;
        cursor.moveToFirst();
        Tipe tipe = new Tipe(cursor.getInt(0),cursor.getString(1));
        cursor.close();
        db.close();
        return tipe;
    }
    public List<Tipe> getAllTipe()
    {
        SQLiteDatabase db = getWritableDatabase();
        List<Tipe> tipeList = new ArrayList<Tipe>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TIPE, null);
        if(cursor.moveToFirst())
            do{
                Tipe tipe = new Tipe(cursor.getInt(0),cursor.getString(1));
                tipeList.add(tipe);
            }while(cursor.moveToNext());
        cursor.close();
        db.close();
        return tipeList;
    }
    public void deleteTipe()
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_TIPE, null, null);
        db.close();
    }
    public int getTipeCount()
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TIPE, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }
    public void createTipePhoto(TipePhoto tipePhoto)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_TIPE,tipePhoto.getId());
        values.put(KEY_NM_TIPE,tipePhoto.getNama_tipe());
        db.insert(TABLE_TIPE_PHOTO, null, values);
        values.clear();
        db.close();
    }
    public TipePhoto getTipePhoto(int kd_tipe)
    {
        SQLiteDatabase db = getReadableDatabase();
        TipePhoto tipePhoto = null;
        Cursor cursor = db.query(TABLE_TIPE_PHOTO, new String[]{KEY_ID_TIPE, KEY_NM_TIPE}, KEY_ID_TIPE + "=?",new String[]{String.valueOf(kd_tipe)}, null,null,null,null);
        if(cursor==null)
            return null;
        if(cursor.moveToFirst()){
             tipePhoto = new TipePhoto(cursor.getInt(0),cursor.getString(1));
        }
        cursor.close();
        db.close();
        return tipePhoto;
    }
    public List<TipePhoto> getAllTipePhoto()
    {
        SQLiteDatabase db = getWritableDatabase();
        List<TipePhoto> tipeList = new ArrayList<TipePhoto>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TIPE_PHOTO, null);
        if(cursor.moveToFirst())
            do{
                TipePhoto tipe = new TipePhoto(cursor.getInt(0),cursor.getString(1));
                tipeList.add(tipe);
            }while(cursor.moveToNext());
        cursor.close();
        db.close();
        return tipeList;
    }
    public void deleteTipePhoto()
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_TIPE_PHOTO, null, null);
        db.close();
    }
    public int getTipePhotoCount()
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TIPE_PHOTO, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }
    public void createProduk(Produk produk)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_PRODUK,produk.getId());
        values.put(KEY_KODE_PRODUK, produk.getKd_produk());
        values.put(KEY_NAMA_PRODUK, produk.getNm_produk());
        db.insert(TABLE_PRODUK, null, values);
        values.clear();
        db.close();
    }
    public Produk getProduk(int kd_produk)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUK, new String[]{KEY_ID_PRODUK, KEY_KODE_PRODUK, KEY_NAMA_PRODUK}, KEY_ID_PRODUK + "=?",new String[]{String.valueOf(kd_produk)}, null,null,null,null);
        if(cursor==null)
            return null;
        cursor.moveToFirst();
        Produk produk = new Produk(cursor.getInt(0),cursor.getString(1),cursor.getString(2));
        cursor.close();
        db.close();
        return produk;
    }
    public List<Produk> getAllProduk()
    {
        SQLiteDatabase db = getWritableDatabase();
        List<Produk> produkList = new ArrayList<Produk>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PRODUK, null);
        if(cursor.moveToFirst())
            do{
                Produk produk = new Produk(cursor.getInt(0),cursor.getString(1),cursor.getString(2));
                produkList.add(produk);
            }while(cursor.moveToNext());
        cursor.close();
        db.close();
        return produkList;
    }
    public void deleteProduk()
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_PRODUK, null, null);
        db.close();
    }
    public int getProdukCount()
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PRODUK, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    public void createBanner(Banner banner)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Banner.COLUMN_FOTO,banner.getImage());
        values.put(Banner.COLUMN_TANGGAL,banner.getTanggal());
        db.insert(Banner.TABLE_NAME, null, values);
        values.clear();
        db.close();
    }

    public Banner getBanner(int id)
    {
        SQLiteDatabase db = getReadableDatabase();
        Banner banner = null;
        Cursor cursor = db.query(Banner.TABLE_NAME, new String[]{Banner.COLUMN_ID, Banner.COLUMN_FOTO , Banner.COLUMN_TANGGAL}, Banner.COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if(cursor==null)
            return null;
        if(cursor.moveToFirst()){
            banner = new Banner(cursor.getInt(0),cursor.getString(1), cursor.getString(2));
        }
        cursor.close();
        db.close();
        return banner;
    }

    public List<Banner> getAllBanner() {
        SQLiteDatabase db = getReadableDatabase();
        List<Banner> banners = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Banner.TABLE_NAME , null);
        if(cursor.moveToFirst())
            do{
                banners.add(new Banner(cursor.getInt(0),cursor.getString(1), cursor.getString(2)));
            }while (cursor.moveToNext());
        cursor.close();
        db.close();
        return banners;
    }

    public void deleteAllBanner()
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Banner.TABLE_NAME, null, null);
        db.close();
    }

    public int getBannerCount()
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Banner.TABLE_NAME, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    public void createNews(NewsClass newsClass)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NewsClass.column_id,newsClass.getId());
        values.put(NewsClass.column_status,newsClass.getStatus());
        values.put(NewsClass.column_judul,newsClass.getJudul());
        values.put(NewsClass.column_headline,newsClass.getHeadline());
        values.put(NewsClass.column_content,newsClass.getContent());
        values.put(NewsClass.column_image,newsClass.getImage());
        values.put(NewsClass.column_date,newsClass.getDate_upload());
        db.insert(NewsClass.table_name, null, values);
        values.clear();
        db.close();
    }
    public NewsClass getNews(int id)
    {
        SQLiteDatabase db = getReadableDatabase();
        NewsClass newsClass = null;
        Cursor cursor = db.query(NewsClass.table_name, new String[]{NewsClass.column_id, NewsClass.column_status, NewsClass.column_judul,
                NewsClass.column_headline,NewsClass.column_content,NewsClass.column_image,NewsClass.column_date}, NewsClass.column_id + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if(cursor==null)
            return null;
        if(cursor.moveToFirst()){
            newsClass = new NewsClass(cursor.getInt(0),cursor.getInt(1),cursor.getString(2), cursor.getString(3), cursor.getString(4),
                    cursor.getString(5),cursor.getString(6));
        }
        cursor.close();
        db.close();
        return newsClass;
    }
    public List<NewsClass> getAllNews()
    {
        SQLiteDatabase db = getReadableDatabase();
        List<NewsClass> newsClasses = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + NewsClass.table_name, null);
        if(cursor.moveToFirst())
            do{
                newsClasses.add(new NewsClass(cursor.getInt(0),cursor.getInt(1),cursor.getString(2), cursor.getString(3), cursor.getString(4),
                        cursor.getString(5),cursor.getString(6)));
            }while (cursor.moveToNext());
        cursor.close();
        db.close();
        return newsClasses;
    }
    public int deleteAllNews()
    {
        SQLiteDatabase db = getWritableDatabase();
        int x = db.delete(NewsClass.table_name, "1", null);
        db.close();
        return x;
    }
    public void deleteNews(int id)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(NewsClass.table_name, NewsClass.column_id+ "=" + id, null);
        db.close();
    }
    public int getNewsCount()
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + NewsClass.table_name, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }
    public int updateNews(NewsClass newsClass)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NewsClass.column_id,newsClass.getId());
        values.put(NewsClass.column_status,newsClass.getStatus());
        values.put(NewsClass.column_judul,newsClass.getJudul());
        values.put(NewsClass.column_headline,newsClass.getHeadline());
        values.put(NewsClass.column_content,newsClass.getContent());
        values.put(NewsClass.column_image,newsClass.getImage());
        values.put(NewsClass.column_date,newsClass.getDate_upload());
        int isUpdate = db.update(NewsClass.table_name,values,NewsClass.column_id + "=?",new String[]{String.valueOf(newsClass.getId())});
        values.clear();
        db.close();
        return isUpdate;
    }

    public void deleteAll(){
        this.deleteCity();
        this.deleteDistributor();
        this.deleteOutlet();
        this.deleteTipe();
        this.deleteTipePhoto();
        this.deleteVisitPlan();
        this.deleteProduk();
        this.deleteCompetitor();
        this.deleteAllPhoto();
        //this.deleteAllBanner();
    }
}

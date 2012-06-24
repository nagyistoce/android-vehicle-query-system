package whutcs.viky.viq;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Provides access to the underlying database elements -- tables, views and
 * columns.
 * 
 * @author xyxzfj@gmail.com
 * 
 */
public class ViqSQLiteOpenHelper extends SQLiteOpenHelper {
	// private static final String TAG = "ViqSQLiteOpenHelper";

	// Database schema.
	public static final String DB_VIQ = "viq.db";
	public static final String TABLE_QUERY = "Query";
	public static final String TABLE_INFO = "Info";
	public static final String VIEW_QUERY_INFO = "QueryInfo";
	public static final int DB_VERSION = 20;

	// TABLE QUERY:
	public static final String[] TABLE_QUERY_COLUMNS_SELECTED = new String[] {
			"_id", "time", "place", "note", "photo" };
	public static final String TABLE_QUERY_SELECTION = getSelection(TABLE_QUERY_COLUMNS_SELECTED);
	// column "_licence_" is never SELECTed in selection clause.
	public static final String[] TABLE_QUERY_COLUMNS = new String[] { "_id",
			"time", "place", "note", "photo", "_licence_" };
	public static final int TABLE_QUERY_COLUMN_TIME = 1;
	public static final int TABLE_QUERY_COLUMN_PLACE = 2;
	public static final int TABLE_QUERY_COLUMN_NOTE = 3;
	public static final int TABLE_QUERY_COLUMN_PHOTO = 4;
	public static final int TABLE_QUERY_COLUMN_LICENCE = 5;

	// TABLE INFO:
	public static final String[] TABLE_INFO_COLUMNS = new String[] { "_id",
			"licence", "type", "vin", "name", "phone", "gender", "birth",
			"driving_licence", "note", "photo" };
	public static final String TABLE_INFO_SELECTION = getSelection(TABLE_INFO_COLUMNS);
	public static final int TABLE_INFO_COLUMN_LICENCE = 1;
	public static final int TABLE_INFO_COLUMN_TYPE = 2;
	public static final int TABLE_INFO_COLUMN_VIN = 3;
	public static final int TABLE_INFO_COLUMN_NAME = 4;
	public static final int TABLE_INFO_COLUMN_PHONE = 5;
	public static final int TABLE_INFO_COLUMN_GENDER = 6;
	public static final int TABLE_INFO_COLUMN_BIRTH = 7;
	public static final int TABLE_INFO_COLUMN_DRIVING_LICENCE = 8;
	public static final int TABLE_INFO_COLUMN_NOTE = 9;
	public static final int TABLE_INFO_COLUMN_PHOTO = 10;

	// VIEW QUERY_INFO:
	public static final String[] VIEW_QUERY_INFO_COLUMNS = new String[] {
			"_id", "licence", "name", "phone", "time", "place", "note", "photo" };
	public static final String VIEW_QUERY_INFO_SELECTION = getSelection(VIEW_QUERY_INFO_COLUMNS);
	public static final int VIEW_QUERY_INFO_COLUMN_LICENCE = 1;
	public static final int VIEW_QUERY_INFO_COLUMN_NAME = 2;
	public static final int VIEW_QUERY_INFO_COLUMN_PHONE = 3;
	public static final int VIEW_QUERY_INFO_COLUMN_TIME = 4;
	public static final int VIEW_QUERY_INFO_COLUMN_PLACE = 5;
	public static final int VIEW_QUERY_INFO_COLUMN_NOTE = 6;
	public static final int VIEW_QUERY_INFO_COLUMN_PHOTO = 7;

	// Creation sql of tables and views.
	/**
	 * The _licence_ field in table Query serves as a reference to table Info.
	 */
	public static final String SQL_CREATE_TABLE_QUERY = genCreateTableSql(
			TABLE_QUERY, TABLE_QUERY_COLUMNS);
	public static final String SQL_CREATE_TABLE_INFO = genCreateTableSql(
			TABLE_INFO, TABLE_INFO_COLUMNS);
	public static final String SQL_CREATE_VIEW_QUERY_INFO = "CREATE VIEW QueryInfo "
			+ "AS SELECT Query._id AS _id, _licence_ AS licence,name,phone,time,place,Query.note AS note,Query.photo AS photo "
			+ "FROM Query LEFT OUTER JOIN Info ON _licence_=licence";

	private static String genCreateTableSql(String tableName,
			String[] columnsWithIDFirst) {
		if (tableName == null || tableName.length() == 0
				|| columnsWithIDFirst.length == 0
				|| !columnsWithIDFirst[0].equals("_id")) {
			return null;
		}

		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("CREATE TABLE ").append(tableName).append("(");
		sqlBuilder.append("_id INTEGER PRIMARY KEY AUTOINCREMENT");
		for (int i = 1; i < columnsWithIDFirst.length; i++) {
			sqlBuilder.append(",").append(columnsWithIDFirst[i]);
		}
		sqlBuilder.append(")");

		return sqlBuilder.toString();
	}

	/**
	 * Generate a selection clause of OR with all columns " LIKE %?% ".
	 * 
	 * @param columns
	 * @return
	 */
	private static String getSelection(String[] columns) {
		if (columns.length == 0) {
			return null;
		}

		StringBuilder builder = new StringBuilder("(");
		for (int i = 0; i < columns.length - 1; i++) {
			builder.append(columns[i]);
			builder.append(" LIKE ? OR ");
		}
		builder.append(columns[columns.length - 1]);
		builder.append(" LIKE ?)");

		return builder.toString();
	}

	/**
	 * Get the corresponding selectionArgs with getSelection().
	 * 
	 * @param filter
	 * @return selectionArgs
	 */
	public static String[] getSelectiionArgs(String filter, int columns) {
		if (columns == 0) {
			return null;
		}

		String[] selectionArgs = new String[columns];
		for (int i = 0; i < selectionArgs.length; i++) {
			selectionArgs[i] = "%" + filter + "%";
		}

		return selectionArgs;
	}

	public ViqSQLiteOpenHelper(Context context) {
		super(context, DB_VIQ, null, DB_VERSION);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.delete(TABLE_QUERY, null, null);
		db.delete(TABLE_INFO, null, null);

		sampleInsert(db);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_TABLE_INFO);
		db.execSQL(SQL_CREATE_TABLE_QUERY);
		db.execSQL(SQL_CREATE_VIEW_QUERY_INFO);

		sampleInsert(db);

	}

	void sampleInsert(SQLiteDatabase db) {
		db.execSQL("INSERT INTO Info VALUES(null,'��A12245','A1','LSGPC52U7AF127561','С��','13667147300','��','1989-11','371322198701202314','����Ƽݳ�ǰ��','2012-05-22_18-41-32.jpg')");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12246','A2','LSGPC52U7AF127562','��С��','13667147311','Ů','1989-11','371322198701202311','2012��5����ѧ�Ὺ��','IMG_20120304_143047.jpg')");

		db.execSQL("INSERT INTO Info VALUES(null,'��A12301','A1','LSGPC52U7AF127561','����1','13667147301','��','1989-1','371322198701202301','��ע1','IMG_20120220_073130.jpg')");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12302','A2','LSGPC52U7AF127562','����2','13667147302','��','1989-2','371322198701202302','��ע2','IMG_20120304_142701.jpg')");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12303','A3','LSGPC52U7AF127563','����3','13667147303','��','1989-3','371322198701202303','��ע3','IMG_20120304_143026.jpg')");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12304','A4','LSGPC52U7AF127564','����4','13667147304','��','1989-4','371322198701202304','��ע4','2012-05-22_18-49-38.jpg')");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12305','A5','LSGPC52U7AF127565','����5','13667147305','��','1989-5','371322198701202305','��ע5','IMG_20120220_073139.jpg')");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12306','A6','LSGPC52U7AF127566','����6','13667147306','��','1989-6','371322198701202306','��ע6','IMG_20120220_073148.jpg')");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12307','A7','LSGPC52U7AF127567','����7','13667147307','��','1989-7','371322198701202307','��ע7','IMG_20120220_073157.jpg')");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12308','A8','LSGPC52U7AF127568','����8','13667147308','��','1989-8','371322198701202308','��ע8','IMG_20120220_073203.bmp')");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12309','A9','LSGPC52U7AF127569','����9','13667147309','��','1989-9','371322198701202309','��ע9','IMG_20120220_073203.jpg')");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12310','A0','LSGPC52U7AF127570','����10','13667147310','��','1989-10','371322198701202310','��ע10','IMG_20120220_073212.jpg')");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12311','A1','LSGPC52U7AF127571','����11','13667147311','��','1989-11','371322198701202311','��ע11','IMG_20120220_073225.jpg')");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12312','A2','LSGPC52U7AF127572','����12','13667147312','��','1989-12','371322198701202312','��ע12','IMG_20120304_142701.jpg')");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12313','A3','LSGPC52U7AF127573','����13','13667147313','��','1990-1','371322198701202313','��ע13','IMG_20120304_142706.jpg')");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12314','A4','LSGPC52U7AF127574','����14','13667147314','��','1990-2','371322198701202314','��ע14','IMG_20120304_142715.jpg')");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12315','A5','LSGPC52U7AF127575','����15','13667147315','��','1990-3','371322198701202315','��ע15','IMG_20120304_142847.jpg')");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12316','A6','LSGPC52U7AF127576','����16','13667147316','��','1990-4','371322198701202316','��ע16','IMG_20120304_142854.jpg')");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12317','A7','LSGPC52U7AF127577','����17','13667147317','��','1990-5','371322198701202317','��ע17','IMG_20120304_142901.jpg')");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12318','A8','LSGPC52U7AF127578','����18','13667147318','��','1990-6','371322198701202318','��ע18','IMG_20120304_142909.jpg')");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12319','A9','LSGPC52U7AF127579','����19','13667147319','��','1990-7','371322198701202319','��ע19','IMG_20120304_143011.jpg')");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12320','A0','LSGPC52U7AF127580','����20','13667147320','��','1990-8','371322198701202320','��ע20','IMG_20120304_143015.jpg')");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12321','A1','LSGPC52U7AF127581','����21','13667147321','��','1990-9','371322198701202321','��ע21','IMG_20120304_143026.jpg')");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12322','A2','LSGPC52U7AF127582','����22','13667147322','��','1990-10','371322198701202322','��ע22','IMG_20120304_143034.jpg')");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12323','A3','LSGPC52U7AF127583','����23','13667147323','��','1990-11','371322198701202323','��ע23','IMG_20120304_143042.jpg')");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12324','A4','LSGPC52U7AF127584','����24','13667147324','Ů','1990-12','371322198701202324','��ע24','IMG_20120304_143047.jpg')");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12325','A5','LSGPC52U7AF127585','����25','13667147325','Ů','1991-1','371322198701202325','��ע25',null)");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12326','A6','LSGPC52U7AF127586','����26','13667147326','Ů','1991-2','371322198701202326','��ע26',null)");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12327','A7','LSGPC52U7AF127587','����27','13667147327','Ů','1991-3','371322198701202327','��ע27',null)");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12328','A8','LSGPC52U7AF127588','����28','13667147328','Ů','1991-4','371322198701202328','��ע28',null)");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12329','A9','LSGPC52U7AF127589','����29','13667147329','Ů','1991-5','371322198701202329','��ע29',null)");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12330','A0','LSGPC52U7AF127590','����30','13667147330','Ů','1991-6','371322198701202330','��ע30',null)");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12331','A1','LSGPC52U7AF127591','����31','13667147331','Ů','1991-7','371322198701202331','��ע31',null)");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12332','A2','LSGPC52U7AF127592','����32','13667147332','Ů','1991-8','371322198701202332','��ע32',null)");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12333','A3','LSGPC52U7AF127593','����33','13667147333','Ů','1991-9','371322198701202333','��ע33',null)");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12334','A4','LSGPC52U7AF127594','����34','13667147334','Ů','1991-10','371322198701202334','��ע34',null)");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12335','A5','LSGPC52U7AF127595','����35','13667147335','Ů','1991-11','371322198701202335','��ע35',null)");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12336','A6','LSGPC52U7AF127596','����36','13667147336','Ů','1991-12','371322198701202336','��ע36',null)");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12337','A7','LSGPC52U7AF127597','����37','13667147337','Ů','1992-1','371322198701202337','��ע37',null)");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12338','A8','LSGPC52U7AF127598','����38','13667147338','Ů','1992-2','371322198701202338','��ע38',null)");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12339','A9','LSGPC52U7AF127599','����39','13667147339','Ů','1992-3','371322198701202339','��ע39',null)");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12340','A0','LSGPC52U7AF127600','����40','13667147340','Ů','1992-4','371322198701202340','��ע40',null)");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12341','A1','LSGPC52U7AF127601','����41','13667147341','Ů','1992-5','371322198701202341','��ע41',null)");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12342','A2','LSGPC52U7AF127602','����42','13667147342','Ů','1992-6','371322198701202342','��ע42',null)");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12343','A3','LSGPC52U7AF127603','����43','13667147343','Ů','1992-7','371322198701202343','��ע43',null)");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12344','A4','LSGPC52U7AF127604','����44','13667147344','Ů','1992-8','371322198701202344','��ע44',null)");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12345','A5','LSGPC52U7AF127605','����45','13667147345','Ů','1992-9','371322198701202345','��ע45',null)");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12346','A6','LSGPC52U7AF127606','����46','13667147346','Ů','1992-10','371322198701202346','��ע46',null)");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12347','A7','LSGPC52U7AF127607','����47','13667147347','Ů','1992-11','371322198701202347','��ע47',null)");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12348','A8','LSGPC52U7AF127608','����48','13667147348','Ů','1992-12','371322198701202348','��ע48',null)");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12349','A9','LSGPC52U7AF127609','����49','13667147349','Ů','1993-1','371322198701202349','��ע49',null)");
		db.execSQL("INSERT INTO Info VALUES(null,'��A12350','A0','LSGPC52U7AF127610','����50','13667147350','Ů','1993-2','371322198701202350','��ע50',null)");

		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:18','����ʡ�人����ɽ���人����ѧ�ص�1','��ע1',null,'��A12302')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:19','����ʡ�人����ɽ���人����ѧ�ص�2','��ע2',null,'��A12303')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:20','����ʡ�人����ɽ���人����ѧ�ص�3','��ע3',null,'��A12302')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:21','����ʡ�人����ɽ���人����ѧ�ص�4','��ע4',null,'��A12301')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:22','����ʡ�人����ɽ���人����ѧ�ص�5','��ע5',null,'��A12303')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:23','����ʡ�人����ɽ���人����ѧ�ص�6','��ע6',null,'��A12302')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:24','����ʡ�人����ɽ���人����ѧ�ص�7','��ע7',null,'��A12302')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:25','����ʡ�人����ɽ���人����ѧ�ص�8','��ע8',null,'��A12301')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:26','����ʡ�人����ɽ���人����ѧ�ص�9','��ע9',null,'��A12301')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:27','����ʡ�人����ɽ���人����ѧ�ص�10','��ע10',null,'��A12301')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:28','����ʡ�人����ɽ���人����ѧ�ص�11','��ע11',null,'��A12303')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:29','����ʡ�人����ɽ���人����ѧ�ص�12','��ע12',null,'��A12302')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:30','����ʡ�人����ɽ���人����ѧ�ص�13','��ע13',null,'��A12301')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:31','����ʡ�人����ɽ���人����ѧ�ص�14','��ע14',null,'��A12301')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:32','����ʡ�人����ɽ���人����ѧ�ص�15','��ע15',null,'��A12301')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:33','����ʡ�人����ɽ���人����ѧ�ص�16','��ע16',null,'��A12301')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:34','����ʡ�人����ɽ���人����ѧ�ص�17','��ע17',null,'��A12303')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:35','����ʡ�人����ɽ���人����ѧ�ص�18','��ע18',null,'��A12301')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:36','����ʡ�人����ɽ���人����ѧ�ص�19','��ע19',null,'��A12301')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:37','����ʡ�人����ɽ���人����ѧ�ص�20','��ע20',null,'��A12302')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:38','����ʡ�人����ɽ���人����ѧ�ص�21','��ע21',null,'��A12302')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:39','����ʡ�人����ɽ���人����ѧ�ص�22','��ע22',null,'��A12302')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:40','����ʡ�人����ɽ���人����ѧ�ص�23','��ע23',null,'��A12303')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:41','����ʡ�人����ɽ���人����ѧ�ص�24','��ע24',null,'��A12301')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:42','����ʡ�人����ɽ���人����ѧ�ص�25','��ע25',null,'��A12302')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:43','����ʡ�人����ɽ���人����ѧ�ص�26','��ע26',null,'��A12301')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:44','����ʡ�人����ɽ���人����ѧ�ص�27','��ע27','IMG_20120304_142901.jpg','��A12304')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:45','����ʡ�人����ɽ���人����ѧ�ص�28','��ע28',null,'��A12301')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:46','����ʡ�人����ɽ���人����ѧ�ص�29','��ע29',null,'��A12301')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:47','����ʡ�人����ɽ���人����ѧ�ص�30','��ע30',null,'��A12301')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:48','����ʡ�人����ɽ���人����ѧ�ص�31','��ע31',null,'��A12301')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:49','����ʡ�人����ɽ���人����ѧ�ص�32','��ע32',null,'��A12302')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:50','����ʡ�人����ɽ���人����ѧ�ص�33','��ע33',null,'��A12302')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:51','����ʡ�人����ɽ���人����ѧ�ص�34','��ע34',null,'��A12301')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:52','����ʡ�人����ɽ���人����ѧ�ص�35','��ע35',null,'��A12301')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:53','����ʡ�人����ɽ���人����ѧ�ص�36','��ע36',null,'��A12303')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:54','����ʡ�人����ɽ���人����ѧ�ص�37','��ע37',null,'��A12303')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:55','����ʡ�人����ɽ���人����ѧ�ص�38','��ע38','IMG_20120304_142847.jpg','��A12304')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:56','����ʡ�人����ɽ���人����ѧ�ص�39','��ע39',null,'��A12303')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:57','����ʡ�人����ɽ���人����ѧ�ص�40','��ע40','IMG_20120304_143015.jpg','��A123013')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:58','����ʡ�人����ɽ���人����ѧ�ص�41','��ע41','IMG_20120304_143011.jpg','��A12303')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:59','����ʡ�人����ɽ���人����ѧ�ص�42','��ע42','IMG_20120304_142715.jpg','��A12302')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:60','����ʡ�人����ɽ���人����ѧ�ص�43','��ע43','IMG_20120304_142706.jpg','��A12302')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:61','����ʡ�人����ɽ���人����ѧ�ص�44','��ע44','IMG_20120220_073225.jpg','��A12301')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:62','����ʡ�人����ɽ���人����ѧ�ص�45','��ע45','IMG_20120220_073212.jpg','��A12301')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:63','����ʡ�人����ɽ���人����ѧ�ص�46','��ע46','IMG_20120220_073203.jpg','��A12301')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:64','����ʡ�人����ɽ���人����ѧ�ص�47','��ע47','IMG_20120220_073203.bmp','��A12301')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:65','����ʡ�人����ɽ���人����ѧ�ص�48','��ע48','IMG_20120220_073157.jpg','��A12301')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:66','����ʡ�人����ɽ���人����ѧ�ص�49','��ע49','IMG_20120220_073148.jpg','��A12301')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:67','����ʡ�人����ɽ���人����ѧ�ص�50','��ע50','IMG_20120220_073139.jpg','��A12301')");

		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C22 16:52:20','����ʡ�人����ɽ���人����ѧ�����嶰','','IMG_20120304_143034.jpg','��A12246')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C25 16:52:20','����ʡ�人����ɽ���人����ѧ�����Ķ�','Υ��ͣ��','IMG_20120304_143042.jpg','��A12246')");
		db.execSQL("INSERT INTO Query VALUES(null,'2012�C04�C20 16:52:18','����ʡ�人����ɽ���人����ѧ','δ����ͣ����','2012-05-22_18-49-13.jpg','��A12245')");
	}
}

package ouc.cs.course.java.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.sqlite.SQLiteConnection;

import com.mysql.cj.api.jdbc.Statement;
import com.mysql.cj.api.mysqla.result.Resultset;

import ouc.cs.course.java.model.Music;
import ouc.cs.course.java.model.MusicSheet;

public class OpSqliteDB {
	private static final String Class_Name = "org.sqlite.JDBC";
	private static final String DB_URL = "jdbc:sqlite:player.db";

	public static void main(String[] args) {

	}

	// 创建Sqlite数据库连接
	public static Connection createConnection() throws SQLException, ClassNotFoundException {
		Class.forName(Class_Name);
		return DriverManager.getConnection(DB_URL);
	}

	public static ArrayList<MusicSheet> getMusicSheets() {
		ArrayList<MusicSheet> sheets = new ArrayList<MusicSheet>();

		try {
			java.sql.Statement statement = createConnection().createStatement();
			ResultSet rs = statement.executeQuery("select * from sheet");
			while (rs.next()) {
				MusicSheet sheet = new MusicSheet();
				sheet.setUuid(rs.getString("id"));
				sheet.setName(rs.getString("sheetname"));
				sheet.setCreator(rs.getString("creator"));
				sheet.setCreatorId(rs.getString("creatorid"));
				sheet.setDateCreated(rs.getString("createtime"));
				sheet.setPicture(rs.getString("picture"));
				
				ArrayList<Music> mlist = getMusicList(sheet.getUuid());
				Map<String, String> map = new HashMap();
				for(int i = 0; i < mlist.size(); i++){
					map.put(mlist.get(i).getMd5value(), mlist.get(i).getName());
				}
				sheet.setMusicItems(map);

				sheets.add(sheet);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sheets;
	}

	public static void addMusicSheet(MusicSheet sheet) {
		try {
			java.sql.Statement statement = createConnection().createStatement();

			String sqlstr = "insert into sheet (id, sheetname, createtime, creator, creatorid, picture) " + "values('"
					+ sheet.getUuid() + "'" + ", '" + sheet.getName() + "'" + ", '" + sheet.getDateCreated() + "'"
					+ ", '" + sheet.getCreator() + "'" + ",'" + sheet.getCreatorId() + "'" + ",'" + sheet.getPicture()
					+ "')";

			statement.execute(sqlstr);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void deleteSheet(String id) {
		try {
			java.sql.Statement statement = createConnection().createStatement();

			String sqlstr = "delete from sheet where id='"+id+"'";

			statement.execute(sqlstr);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static ArrayList<Music> getMusicList(String sheetid) {
		ArrayList<Music> musics = new ArrayList<Music>();

		try {
			java.sql.Statement statement = createConnection().createStatement();
			String sql = "select * from music";
			if(sheetid != null){
				sql += " where sheetid = '" + sheetid + "'";
			}
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				Music music = new Music();
				music.setId(rs.getString("id"));
				music.setName(rs.getString("musicfile"));
				music.setSheetid(rs.getString("sheetid"));
				music.setMd5value(rs.getString("filemd5"));
				music.setMusicpath(rs.getString("musicpath"));
				music.setSinger(rs.getString("singer"));
				music.setLength(rs.getInt("mlength"));

				musics.add(music);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return musics;
	}
	
	public static void addMusic(Music music){
		try {
			java.sql.Statement statement = createConnection().createStatement();

			String sqlstr = "insert into music (id, musicfile, sheetid, filemd5, musicpath, singer, mlength) " + "values('"
					+ music.getId() + "'" + ", '" + music.getName() + "'" + ", '" + music.getSheetid() + "'"
					+ ", '" + music.getMd5value() + "'" + ",'" + music.getMusicpath() + "'" + ",'" + music.getSinger()
					+ "'," + music.getLength() + ")";

			statement.execute(sqlstr);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public static void deleteMusic(String id) {
		try {
			java.sql.Statement statement = createConnection().createStatement();

			String sqlstr = "delete from music where id='"+id+"'";

			statement.execute(sqlstr);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

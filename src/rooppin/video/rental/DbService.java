package rooppin.video.rental;

import java.sql.*;
import java.util.*;

/**
 * Utilitis class to fulfill all database procedures
 * @author VADIM&ORI&MATAN
 */
class DbService 
{
	MySQLConnectionPool pool;
	ApplicationWindow parent;
	
	/**
	 * Constructor
	 * @param parent
	 */
	DbService(ApplicationWindow parent)
	{
		pool = new MySQLConnectionPool();
		this.parent=parent;
	}

	/**
	 * Permanently delete film from base
	 * @param tconst
	 */
	public void deleteFilm(String tconst) {
		final String[] tables = {"actors_in_films", "films", "films_genres", "inventory"};
		Connection conn = null;
		try
		{
			conn = pool.getConnection();
			Statement statement = conn.createStatement();
			for(String table:tables) {
				String command = "DELETE FROM `imdb`.`" + table + "` WHERE `tconst`=" + tconst + ";";
				statement.executeUpdate(command);
			}
			
		}catch (SQLException e) {
			//e.printStackTrace();
		}finally {
			if (conn != null) {
			try {
				pool.returnConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		}
	}
	
	/**
	 * Gets results of films extended search
	 * @param input
	 * @return
	 */
	HashMap<String,String> getFreeSearch(String input)
	{
		HashMap<String,String> result =new HashMap<String,String>();
		Connection conn = null;
		try
		{
			conn = pool.getConnection();
			Statement statement = conn.createStatement();
			StringBuilder tmp = new StringBuilder();
			
			ResultSet res = statement.executeQuery("call freeSearch('" + input + "', 0, 1000);");
			int columns = res.getMetaData().getColumnCount();
			while (res.next()) {
				tmp.append(res.getString(2).length()>40? res.getString(2).substring(0, 40) + "...":res.getString(2));
				if(columns>2)
				{
					tmp.append(" ( ");
					for(int i=3;i<=columns;i++) tmp.append(res.getString(i) + " ");
					
					tmp.append(")");
				}
				result.put(res.getString(1), tmp.toString());
				tmp.setLength(0);
			}
			return result;
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if (conn != null) {
			try {
				pool.returnConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
		
		return result;
	}
	
	/**
	 * Make temporary table in database for giv faster answer 
	 * @param param
	 */
	void makeTemporaryTable(String[] param)
	{
		Connection conn = null;
		String str;
		try
		{
			conn = pool.getConnection();
			Statement statement = conn.createStatement();
			str="call makeTmp('" + param[0] + "', '" + param[1] + "', " + param[2] + ", " + param[3] + ");";
			statement.executeQuery(str);
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if (conn != null) {
			try {
				pool.returnConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	}
	
	/**
	 * Gets all avialabel parameters for search
	 * 
	 * @param num
	 * @return
	 */
	public String[] getInitialValues(int num) {
		ArrayList<String> result = new ArrayList<>();
		String[] procedures = {"getTypes", "getGenres"};
		Connection conn = null;
			try
			{
				conn = pool.getConnection();
				Statement statement = conn.createStatement();
				
				ResultSet res = statement.executeQuery("call " + procedures[num] + "();");
				while (res.next()) {
					result.add(res.getString(1));
				}
				return result.toArray(new String[result.size()]);
			}catch (SQLException e) {
				e.printStackTrace();
			}finally {
				if (conn != null) {
				try {
					pool.returnConnection(conn);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				}
			}
			return (String[]) result.toArray();
	}
	
	/**
	 * Gets full film data from database if exists
	 * 
	 * @param tconst
	 * @return
	 */
	HashMap<String,String> getFullFilmData(String tconst)
	{
		HashMap<String,String> result =new HashMap<String,String>();
		Connection conn = null;
		try
		{
			conn = pool.getConnection();
			Statement statement = conn.createStatement();
			ResultSet res = statement.executeQuery("call getFullFilmData('" + tconst + "');");
			int columns = res.getMetaData().getColumnCount();
			while (res.next()) {
				for(int i=1;i<= columns;i++)
				{
					String temp = res.getMetaData().getColumnName(i);
					result.put(temp.substring(0,temp.length()-2), res.getString(i));
				}
			}
			return result;
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if (conn != null) {
			try {
				pool.returnConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
		
		return result;
	}
	
	/**
	 * Add full film data to base
	 * 
	 * @param set
	 */
	void updateFullFilmTable(final HashMap<String,String> set)
	{
		Connection conn = null;
		String keyStr="", valStr="";
		try
		{
			conn = pool.getConnection();
			Statement statement = conn.createStatement();
			
			for(String s:set.keySet())
			{
				keyStr += s + "db,";
				valStr += "'" + set.get(s).toString().replaceAll("'", "`") + "',";
			}
			
			String resStr = "insert into imdb.films_full(" + keyStr.substring(0,keyStr.length()-1)
							+ ") values(" + valStr.substring(0,valStr.length()-1) + ");";
			
			statement.executeUpdate(resStr);
		}catch (SQLException e) {
			//e.printStackTrace();
		}finally {
			if (conn != null) {
			try {
				pool.returnConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	}

	/**
	 * Get local poster filename if exists
	 * 
	 * @param uri
	 * @return
	 */
	public String getPosterFileName(String uri) {
		Connection conn = null;
		String result=null;
		try
		{
			conn = pool.getConnection();
			Statement statement = conn.createStatement();
			String comStr = "SELECT ff.imgFile FROM imdb.films_full ff WHERE Posterdb = '" + uri + "';";
			ResultSet res = statement.executeQuery(comStr);
			if(res.next()) {
				result = res.getString(1);
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if (conn != null) {
			try {
				pool.returnConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			}
		}
		return result;
	}

	/**
	 * Set localy stored poster filename
	 * @param fName
	 * @param uri
	 */
	public void updateImageFile(String fName, String uri) {
		Connection conn = null;
		try
		{
			conn = pool.getConnection();
			Statement statement = conn.createStatement();
			String comStr = "update imdb.films_full set imgFile = '" + fName + "' where Posterdb = '" + uri + "';";
			statement.executeUpdate(comStr);
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if (conn != null) {
			try {
				pool.returnConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			}
		}
		
	}
}
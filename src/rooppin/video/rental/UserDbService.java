package rooppin.video.rental;

import java.sql.*;
import java.util.*;

class UserDbService extends DbService
{
	UserDbService(ApplicationWindow parent)
	{
		super(parent);
	}
	
	boolean addNewUser(RegisteredUser u) {
		final String [] details= {"Id","Name","Telephone","Email","City","Street","House","Entry","Appartment","level"};
		StringBuilder str1 = new StringBuilder();
		StringBuilder str2 = new StringBuilder();
		
		Connection conn = null;
		try
		{
			conn = pool.getConnection();
			Statement statement = conn.createStatement();
			
			for(String s:details) {
				str1.append("`" + s + "`,");
				str2.append(u.getFieldByName(s) + ",");
			}
			str1.setLength(str1.length()-1);
			str2.setLength(str2.length()-1);
			String com = "INSERT INTO imdb.users (" + str1.toString()
					+ ") VALUES (" + str2.toString() + ");";
			statement.executeUpdate(com);
			return true;
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
		return false;
	}
	
	HashMap<String,String> getUserByEmail(String s){
		
		if(s==null || s.length()<5) return null;
		HashMap<String,String> set = new HashMap<String,String>();
		Connection conn = null;
		try
		{
			conn = pool.getConnection();
			Statement statement = conn.createStatement();
			String com = "SELECT u.* FROM imdb.users u WHERE u.Email = '" + s + "';";
			ResultSet res = statement.executeQuery(com);
			int columns = res.getMetaData().getColumnCount();
			while (res.next()) {
				for(int i=1;i<= columns;i++)
				{
					String temp = res.getMetaData().getColumnName(i);
					set.put(temp, res.getString(i));
				}
			}
			return set;
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
		return null;
	}

	boolean updateUser(RegisteredUser u) {
		final String [] details= {"Name","Telephone","Email","City","Street","House","Entry","Appartment","level"};
		StringBuilder str1 = new StringBuilder();
		
		Connection conn = null;
		try
		{
			conn = pool.getConnection();
			Statement statement = conn.createStatement();
			
			for(String s:details) {
				str1.append("`" + s + "`=" + u.getFieldByName(s) + ",");
			}
			str1.setLength(str1.length()-1);
			String com = "update `imdb`.`users` set " + str1.toString()
					+ " where Id=" + u.getFieldByName("Id") + ";";
			statement.executeUpdate(com);
			return true;
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
		return false;
	}

	String getInventory(String tconst) {
		Connection conn = null;
		try
		{
			conn = pool.getConnection();
			Statement statement = conn.createStatement();
			String userID = parent.getUserId();
			String com = "update `imdb`.`inventory` set `ordered`='" 
					+ userID + "' where `tconst`='" + tconst + "' limit 1;";
			if(statement.executeUpdate(com)>0) {
				com = "SELECT `inventory` FROM `imdb`.`inventory` where `tconst`='"
							+ tconst + "' and `ordered`='" + userID + "';";
				ResultSet res = statement.executeQuery(com);
				if(res.next()) return res.getString(1);
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
		return null;
	}

	int newOrderNumber(String userId) {
		Connection conn = null;
		try
		{
			conn = pool.getConnection();
			Statement statement = conn.createStatement();
			String com = "insert into `imdb`.`orders` (`userId`, `rentDate`) values ('" + userId + "', CURDATE());";
			if(statement.executeUpdate(com)>0) {
				com = "select orderNum from `imdb`.`orders` where `userId`='" + userId
						+ "' order by orderNum desc limit 1;";
				ResultSet res = statement.executeQuery(com);
				if(res.next()) return res.getInt(1);
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
		return -1;
	}

	boolean setPayed(int orderNumber) {
		Connection conn = null;
		try
		{
			conn = pool.getConnection();
			Statement statement = conn.createStatement();
			String com = "update imdb.orders set payed=true where orderNum="
			+ String.valueOf(orderNumber) + ";";
			if(statement.executeUpdate(com)>0) return true;
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
		return false;
	}

	boolean submitOrder(int orderNumber, HashMap<String, Film> orderedInStock) {
		Connection conn = null;
		try
		{
			String com;
			conn = pool.getConnection();
			int count = 0;
			Statement statement = conn.createStatement();
			String orderN = String.valueOf(orderNumber);
			String userID = parent.getUserId();
			Set<String> films = orderedInStock.keySet();
			for(String s:films) {
				com="update `imdb`.`inventory` set `ordered`=" + orderN
						+ " where `ordered` =" + userID + " and `invconst` = '" + s + "';\r\n";
				count += statement.executeUpdate(com);
				
			}
			com = "insert into `imdb`.`inventoryorders` (`orderNum`, `invconst`) values "; 
			for(String s:films) com+="(" + orderN + ", '" + s + "'),";
			count += statement.executeUpdate(com.substring(0, com.length()-1) + ";");
			if(count==2*films.size())return true;
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
		return false;
	}

	String orderFilm(Film film, String userID) {
		Connection conn = null;
		try
		{
			String com;
			conn = pool.getConnection();
			Statement statement = conn.createStatement();
			com="update `imdb`.`inventory` set `ordered`=" + userID 
				+ " where `tconst`='" + film.tconst + "' and `ordered` is null limit 1;";
			if(statement.executeUpdate(com)>0) {
				//statement = conn.createStatement();
				com = "select `invconst` from `imdb`.`inventory` where `tconst`='" 
						+ film.tconst + "' and `ordered`=" + userID + ";";
				ResultSet res = statement.executeQuery(com);
				if(res.next()) return  res.getString(1);
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
		return null;
	}

	boolean removeOrderFilmFromDB(String inv, String userID) {
		Connection conn = null;
		try
		{
			String com;
			conn = pool.getConnection();
			Statement statement = conn.createStatement();
			com="update `imdb`.`inventory` set `ordered`=null "
				+ " where `invconst`='" + inv + "' and `ordered`= " + userID +";";
			if(statement.executeUpdate(com)>0) return true;
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
		return false;
	}

	void logOut(String id) {
		Connection conn = null;
		try
		{
			String com;
			conn = pool.getConnection();
			Statement statement = conn.createStatement();
			com="update `imdb`.`inventory` set `ordered`= null  where `ordered`= " + id +" ;";
			statement.executeUpdate(com);
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

	public HashMap<String, String> getAdminSearch(String mode, String type, String input) {
		HashMap<String, String> result = new HashMap<String, String>();
		String command = "call adminSearch ('" + mode + "', '" + type + "', '" + input + "');";
		
		Connection conn = null;
		try
		{
			conn = pool.getConnection();
			Statement statement = conn.createStatement();
			StringBuilder tmp = new StringBuilder();
			
			ResultSet res = statement.executeQuery(command);
			int columns = res.getMetaData().getColumnCount();
			while (res.next()) {
				tmp.append(res.getString(2));
				if(columns>2)
				{
					tmp.append(" ( ");
					for(int i=3;i<=columns;i++) tmp.append(res.getString(i) + ", ");
					tmp.setLength(tmp.length()-2);
					tmp.append(")");
				}
				result.put(mode + ":" + res.getString(1), tmp.toString());
				tmp.setLength(0);
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

	public void deleteUser(String id) {
		Connection conn = null;
		try
		{
			conn = pool.getConnection();
			Statement statement = conn.createStatement();
			String com = "DELETE FROM `imdb`.`users` `u` WHERE `u`.`Id`='" + id + "';";
			statement.executeUpdate(com);
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

	public void closeOrder(int orderNumber) {
		Connection conn = null;
		try
		{
			conn = pool.getConnection();
			Statement statement = conn.createStatement();
			String com = "UPDATE `imdb`.`inventory` \r\n"
					+ "SET \r\n"
					+ "    `ordered` = NULL\r\n"
					+ "WHERE\r\n"
					+ "    `ordered` = " + orderNumber + ";";
			statement.executeUpdate(com);
			
			com = "UPDATE `imdb`.`orders` \r\n"
					+ "SET \r\n"
					+ "    `returnDate` = NOW()\r\n"
					+ "WHERE\r\n"
					+ "    `orderNum` = " + orderNumber + ";";
			statement.executeUpdate(com);
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

	public Vector<String> getOrderedFilms(int orderNumber) {
		
		Vector<String> result = new Vector<String>();
			Connection conn = null;
			try
			{
				conn = pool.getConnection();
				Statement statement = conn.createStatement();
				
				ResultSet res = statement.executeQuery("SELECT i.tconst \r\n"
						+ "FROM imdb.inventoryorders oi left join imdb.inventory i on oi.invconst=i.invconst\r\n"
						+ "where oi.orderNum =" + orderNumber + ";");
				while (res.next()) {
					result.add(res.getString(1));
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

	public boolean getPayed(int orderNumber) {
		Connection conn = null;
		try
		{
			conn = pool.getConnection();
			Statement statement = conn.createStatement();
			
			ResultSet res = statement.executeQuery("SELECT o.payed \r\n"
					+ "FROM imdb.orders o \r\n"
					+ "where o.orderNum =" + orderNumber + ";");
			if(res.next()) {
				return (res.getString(1).equals("1"));
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
		return false;
}

	public HashMap<String, String> getUserById(String id) {
		if(id==null || id.length()!=9) return null;
		HashMap<String,String> set = new HashMap<String,String>();
		Connection conn = null;
		try
		{
			conn = pool.getConnection();
			Statement statement = conn.createStatement();
			String com = "SELECT u.* FROM imdb.users u WHERE u.Id = '" + id + "';";
			ResultSet res = statement.executeQuery(com);
			int columns = res.getMetaData().getColumnCount();
			while (res.next()) {
				for(int i=1;i<= columns;i++)
				{
					String temp = res.getMetaData().getColumnName(i);
					set.put(temp, res.getString(i));
				}
			}
			return set;
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
		return null;
	}
}

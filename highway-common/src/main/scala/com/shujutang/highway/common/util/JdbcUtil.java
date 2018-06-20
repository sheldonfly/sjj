/**
 * Copyright (C) 1998-2014 DATATANG Inc.All Rights Reserved.		
 * 																	
 * FileName：JdbcUtil.java						
 *			
 * Description：  jdbc帮助类
 * 																	
 * History：
 * 版本号   作者     日期                相关操作
 *  1.0   nancr 2014-10-22  modify		
 */
package com.shujutang.highway.common.util;

//imitatedata org.slf4j.Logger;
//imitatedata org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.*;
import java.util.*;

/**
 * 
 * 
 * Datatang
 * <p>
 * 共通-数据库处理
 * <p>
 * 
 * @version 1.0
 * @author 南成如
 * @lastMonify 2014-06-11
 * @since JDK1.7
 */
public class JdbcUtil {

	// mssqlserver:"jdbc:jtds:sqlserver://192.168.1.127:1433;instance=SQL2008R2;DatabaseName=CooperateHistory";
//	static Logger log = LoggerFactory.getLogger(JdbcUtil.class);
	private static String classDriver = "com.mysql.jdbc.Driver";
	private static String dbConnection;
	private static String userName;
	private static String password;
//	static {
//		InputStream is = JdbcUtil.class.getClassLoader().getResourceAsStream("config/jdbcbydruid.properties");
//		Properties property = new Properties();
//		try {
//			Reader reader = new InputStreamReader(is);
//			property.load(reader);
//			reader.close();
//
//			dbConnection = property.getProperty("jdbc.url");
//			userName = property.getProperty("jdbc.username");
//			password = property.getProperty("jdbc.password");
//		} catch (Exception e) {
////			log.error("[JdbcUtil][getDBConfig] 错误原因：", e);
//			e.printStackTrace();
//		}
//	}

	/**
	 * 创建数据库连接
	 * 
	 * @return Connection 数据库连接
	 */
	public static Connection getDBConn() throws Exception {
		Connection connection = null;
		try {
			// 加载驱动
			Class.forName(classDriver);
			// 连接数据库
			connection = DriverManager.getConnection(dbConnection, userName, password);
		} catch (Exception e) {
//			log.error("[JdbcUtil][getDBConn] 错误原因：", e);
			e.printStackTrace();
		}
		return connection;
	}
	
	public static Connection getDBConn(String conn, String userName, String passwd) throws Exception {
		Connection connection = null;
		try {
			// 加载驱动
			Class.forName("com.mysql.jdbc.Driver");
			// 连接数据库
			connection = DriverManager.getConnection(conn, userName, passwd);
		} catch (Exception e) {
//			log.error("[JdbcUtil][getDBConn] 错误原因：", e);
			e.printStackTrace();
		}
		return connection;
	}

    public static Connection getDBConn(String classDriver, String url, String userName, String passwd) throws Exception {
        Connection connection = null;
        try {
            // 加载驱动
            Class.forName(classDriver);
            // 连接数据库
            connection = DriverManager.getConnection(url, userName, passwd);
        } catch (Exception e) {
//			log.error("[JdbcUtil][getDBConn] 错误原因：", e);
            e.printStackTrace();
        }
        return connection;
    }

	/**
	 * 
	 * 描述 得到数据库连接   指定配置文件地址
	 * 
	 * @param dbConfig
	 * @return
	 * @throws Exception
	 * @see
	 */
	public static Connection getDBConnAssignConf(String dbConfig) throws Exception {
		Connection connection = null;
		try {
			InputStream is = JdbcUtil.class.getClassLoader().getResourceAsStream("config/jdbcbydruid.properties");
			Properties property = new Properties();
			Reader reader = new InputStreamReader(is);
			property.load(reader);
			reader.close();

			String dbConnection = property.getProperty("jdbc.url" + dbConfig);
			String userName = property.getProperty("jdbc.username" + dbConfig);
			String password = property.getProperty("jdbc.password" + dbConfig);
			// 加载驱动
			Class.forName(classDriver);
			// 连接数据库
			connection = DriverManager.getConnection(dbConnection, userName, password);
		} catch (Exception e) {
//			log.error("[JdbcUtil][getDBConn] 错误原因：", e);
			e.printStackTrace();
		}
		return connection;
	}

	/**
	 * 创建数据库连接状态
	 * 
	 * @return Statement 数据库连接状态
	 */
	public static Statement getStatement(Connection connection) throws Exception {
		Statement statement = null;
		try {
			statement = connection.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
//			log.error("[JdbcUtil][getStatement] 错误原因：" + e.getMessage());
		}
		return statement;
	}

	/**
	 * (1、查询帮助类,得到一个list<Map<String, Object>>)
	 * 
	 * @param sql
	 *            salq
	 * @param args
	 *            参数
	 * @return Map 查询结果
	 * @throws
	 */
	public static List<Map<String, Object>> queryForList(String sql, String... args) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Connection connection = null;
		try {
			// 创建连接
			connection = getDBConn();
			// 创建表达式
			// log.info("[JdbcUtil][queryForList] 执行SQL文：" + sql);
			statement = connection.prepareStatement(sql);
			if (args != null && args.length != 0) {
				for (int i = 0; i < args.length; i++) {
					statement.setString(i + 1, args[i]);
					// log.info("[JdbcUtil][queryForList] SQL参数：[" + i +
					// "]: value:" + args[i]);
				}
			}
			// 4.执行指令
			resultSet = statement.executeQuery();

			// 5、对resultSet结果进行处理
			// （1）得到resultSet对象中列的类型和属性信息
			ResultSetMetaData rsmd = resultSet.getMetaData();
			// （2）得到一共有多少列
			int columCount = rsmd.getColumnCount();
			while (resultSet.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 0; i < columCount; i++) {
					String columName = rsmd.getColumnName(i + 1);
					Object value = resultSet.getObject(i + 1);
					map.put(columName, value);
				}
				list.add(map);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			closeRs(resultSet);
			closeStatement(statement);
			closeConnection(connection);
		}

		return list;
	}

	/**
	 * 
	 * 描述 查询列表-指定数据库
	 * 
	 * @param sql
	 * @param args
	 * @return
	 * @throws Exception
	 * @see
	 */
	public static List<Map<String, String>> qu4ListAssignDb(String configId, String sql, String... args) throws Exception {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Connection connection = null;
		try {
			// 创建连接
			connection = getDBConnAssignConf(configId);
			// 创建表达式
			// log.info("[JdbcUtil][queryForList] 执行SQL文：" + sql);
			statement = connection.prepareStatement(sql);
			if (args != null && args.length != 0) {
				for (int i = 0; i < args.length; i++) {
					statement.setString(i + 1, args[i]);
				}
			}
			// 4.执行指令
			resultSet = statement.executeQuery();

			// 5、对resultSet结果进行处理
			// （1）得到resultSet对象中列的类型和属性信息
			ResultSetMetaData rsmd = resultSet.getMetaData();
			// （2）得到一共有多少列
			int columCount = rsmd.getColumnCount();
			while (resultSet.next()) {
				Map<String, String> map = new HashMap<String, String>();
				for (int i = 0; i < columCount; i++) {
					String columName = rsmd.getColumnName(i + 1);
					String value = resultSet.getObject(i + 1) + "";
					map.put(columName, value);
				}
				list.add(map);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			closeRs(resultSet);
			closeStatement(statement);
			closeConnection(connection);
		}

		return list;
	}
	
	/**
	 * 
	  * 描述 查询列表  
	  * 需要 自定义connection和关闭连接操作 
	  * @param connection
	  * @param sql
	  * @param args
	  * @return
	  * @throws Exception
	  * @see
	 */
	public static List<Map<String, Object>> qu4ListAssignDb(Connection connection, String sql, Object... args) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			// 创建表达式
			// log.info("[JdbcUtil][queryForList] 执行SQL文：" + sql);
			statement = connection.prepareStatement(sql);
			if (args != null && args.length != 0) {
				for (int i = 0; i < args.length; i++) {
					statement.setObject(i + 1, args[i]);
				}
			}
			// 4.执行指令
			resultSet = statement.executeQuery();
			
			// 5、对resultSet结果进行处理
			// （1）得到resultSet对象中列的类型和属性信息
			ResultSetMetaData rsmd = resultSet.getMetaData();
			// （2）得到一共有多少列
			int columCount = rsmd.getColumnCount();
			while (resultSet.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 0; i < columCount; i++) {
					String columName = rsmd.getColumnLabel(i + 1);
					Object value = resultSet.getObject(i + 1);
					map.put(columName, value);
				}
				list.add(map);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			closeRs(resultSet);
			closeStatement(statement);
			closeConn(connection);
		}
		
		return list;
	}
	
	/**
	 * 得到string类型集合的列表---适用于redis的hmset方法
	 * @param connection
	 * @param sql
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, String>> queryList4Redis(Connection connection, String sql, Object... args) throws Exception {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			// 创建表达式
			// log.info("[JdbcUtil][queryForList] 执行SQL文：" + sql);
			statement = connection.prepareStatement(sql);
			if (args != null && args.length != 0) {
				for (int i = 0; i < args.length; i++) {
					statement.setObject(i + 1, args[i]);
				}
			}
			// 4.执行指令
			resultSet = statement.executeQuery();
			
			// 5、对resultSet结果进行处理
			// （1）得到resultSet对象中列的类型和属性信息
			ResultSetMetaData rsmd = resultSet.getMetaData();
			// （2）得到一共有多少列
			int columCount = rsmd.getColumnCount();
			while (resultSet.next()) {
				Map<String, String> map = new HashMap<String, String>();
				for (int i = 0; i < columCount; i++) {
					String columName = rsmd.getColumnLabel(i + 1);
					String value = String.valueOf(resultSet.getObject(i + 1));
					map.put(columName, value);
				}
				list.add(map);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			closeRs(resultSet);
			closeStatement(statement);
		}
		
		return list;
	}

	/**
	 * 
	 * sqlQueryUniqueObj(只有一个对象时，使用这个方法得到Object[])
	 * 
//	 * @param Connection
//	 *            connection 数据库连接
//	 * @param String
//	 *            sql 查询SQL文
//	 * @param String
	 *            [] args 查询参数
	 * @return Object[] 查询结果
	 */
	public static Object[] sqlQueryUniqueObj(String sql, String... args) throws Exception {

		Object[] obj = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Connection connection = null;
		try {
			// 创建连接
			connection = getDBConn();
			// 3.创建表达式
			statement = connection.prepareStatement(sql);
			if (args != null && args.length != 0) {
				for (int i = 0; i < args.length; i++) {
					statement.setString(i + 1, args[i]);
				}
			}

			// 4.执行指令
			resultSet = statement.executeQuery();

			// 5、对resultSet结果进行处理
			// （1）得到resultSet对象中列的类型和属性信息
			ResultSetMetaData rsmd = resultSet.getMetaData();
			// （2）得到一共有多少列
			int columCount = rsmd.getColumnCount();
			while (resultSet.next()) {
				obj = new Object[columCount];
				for (int i = 0; i < columCount; i++) {
					Object value = resultSet.getObject(i + 1);
					obj[i] = value;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			closeRs(resultSet);
			closeStatement(statement);
			closeConnection(connection);
		}

		return obj;
	}

	/**
	 * (只有一个对象时，使用这个方法得到Object[])
	 * 
	 * @param connection
	 * @param sql
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public static Object[] sqlQueryUniqueObj(Connection connection, String sql, String... args) throws Exception {

		Object[] obj = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			// 3.创建表达式
			statement = connection.prepareStatement(sql);
			if (args != null && args.length != 0) {
				for (int i = 0; i < args.length; i++) {
					statement.setString(i + 1, args[i]);
				}
			}

			// 4.执行指令
			resultSet = statement.executeQuery();

			// 5、对resultSet结果进行处理
			// （1）得到resultSet对象中列的类型和属性信息
			ResultSetMetaData rsmd = resultSet.getMetaData();
			// （2）得到一共有多少列
			int columCount = rsmd.getColumnCount();
			while (resultSet.next()) {
				obj = new Object[columCount];
				for (int i = 0; i < columCount; i++) {
					Object value = resultSet.getObject(i + 1);
					obj[i] = value;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			closeRs(resultSet);
			closeStatement(statement);
			closeConnection(connection);
		}

		return obj;
	}

	public static Object[] sqlQueryUniqueObjUncolseConnection(Connection connection, String sql, String... args) throws Exception {

		Object[] obj = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			// 3.创建表达式
			statement = connection.prepareStatement(sql);
			if (args != null && args.length != 0) {
				for (int i = 0; i < args.length; i++) {
					statement.setString(i + 1, args[i]);
				}
			}

			// 4.执行指令
			resultSet = statement.executeQuery();

			// 5、对resultSet结果进行处理
			// （1）得到resultSet对象中列的类型和属性信息
			ResultSetMetaData rsmd = resultSet.getMetaData();
			// （2）得到一共有多少列
			int columCount = rsmd.getColumnCount();
			while (resultSet.next()) {
				obj = new Object[columCount];
				for (int i = 0; i < columCount; i++) {
					Object value = resultSet.getObject(i + 1);
					obj[i] = value;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			closeRs(resultSet);
			closeStatement(statement);
		}

		return obj;
	}

	/**
	 * 得到一个字段的值（如count(*)）
	 * 
	 * @param sql
	 * @param dbConfig jdbc配置文件中的id如 url2中的2就是dbconfig
	 * @param args
	 * @return
	 */
	public static Object getONeFieldObj(String sql, String dbConfig, String... args) {
		Object obj = null;
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			// 3.创建表达式
			connection = getDBConnAssignConf(dbConfig);
			statement = connection.prepareStatement(sql);
			if (args != null && args.length != 0) {
				for (int i = 0; i < args.length; i++) {
					statement.setString(i + 1, args[i]);
				}
			}
			// 4.执行指令
			resultSet = statement.executeQuery();
			// 5、对resultSet结果进行处理
			while (resultSet.next()) {
				obj = resultSet.getObject(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			closeRs(resultSet);
			closeStatement(statement);
			closeConnection(connection);
		}

		return obj;
	}
	
	public static Object getONeFieldObj(Connection conn,String sql, String... args) {
		Object obj = null;
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			// 3.创建表达式
			statement = conn.prepareStatement(sql);
			if (args != null && args.length != 0) {
				for (int i = 0; i < args.length; i++) {
					statement.setString(i + 1, args[i]);
				}
			}
			// 4.执行指令
			resultSet = statement.executeQuery();
			// 5、对resultSet结果进行处理
			while (resultSet.next()) {
				obj = resultSet.getObject(1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			closeRs(resultSet);
			closeStatement(statement);
//			closeConnection(connection);
		}
		
		return obj;
	}

	/**
	 * 
	 * sqlQueryUniqueObj(只有一个对象时，使用这个方法得到Object[])
	 * 
//	 * @param Connection
//	 *            connection 数据库连接
//	 * @param String
//	 *            sql 查询SQL文
//	 * @param String
	 *            [] args 查询参数
	 * @return Object[] 查询结果
	 */
	public static Object[] getUniqueObj_SqlServer(String sql, String... args) throws Exception {
		Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");
		// 加载数据库引擎，返回给定字符串名的类
		String url = "jdbc:microsoft:sqlserver://localhost:1433;DatabaseName=test";
		// test为你的数据库的名称
		String user = "用户名";
		String password = "密码";
		Object[] obj = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Connection connection = null;
		try {
			// 创建连接
			connection = DriverManager.getConnection(url, user, password);
			// 3.创建表达式
			statement = connection.prepareStatement(sql);
			if (args != null && args.length != 0) {
				for (int i = 0; i < args.length; i++) {
					statement.setString(i + 1, args[i]);
				}
			}

			// 4.执行指令
			resultSet = statement.executeQuery();

			// 5、对resultSet结果进行处理
			// （1）得到resultSet对象中列的类型和属性信息
			ResultSetMetaData rsmd = resultSet.getMetaData();
			// （2）得到一共有多少列
			int columCount = rsmd.getColumnCount();
			while (resultSet.next()) {
				obj = new Object[columCount];
				for (int i = 0; i < columCount; i++) {
					Object value = resultSet.getObject(i + 1);
					obj[i] = value;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			closeRs(resultSet);
			closeStatement(statement);
			closeConnection(connection);
		}

		return obj;
	}

	/**
	 * 
	 * (2、更新 插入 删除操作帮助类)
	 * 
//	 * @param Connection
//	 *            connection 数据库连接
//	 * @param String
//	 *            sql 执行SQL文
//	 * @param String
	 *            [] args 执行参数
	 * @return
	 * @throws
	 */
	public static boolean insertOrUpdateDB(Connection connection, String sql, String... args) throws Exception {

		PreparedStatement statement = null;
		boolean b = false;
		int i = -1;
		try {
			// 3.创建表达式
			statement = connection.prepareStatement(sql);
			if (args != null && args.length != 0) {
				for (int m = 0; m < args.length; m++) {
					statement.setString(m + 1, args[m]);
				}
			}
			i = statement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			closeStatement(statement);
		}

		if (i > 0) {
			b = true;
		}
		return b;
	}

	/**
	 * 
	 * (2、更新 插入 删除操作帮助类)
	 * 
//	 * @param String
//	 *            sql 执行SQL文
//	 * @param String
	 *            [] args 执行参数
	 * @return
	 * @throws
	 */
	public static boolean insertOrUpdateDB(String sql, String... args) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		boolean b = false;
		int i = -1;
		try {
			// 创建连接
			connection = getDBConn();
			connection.setAutoCommit(false);
			// 3.创建表达式
			statement = connection.prepareStatement(sql);
			if (args != null && args.length != 0) {
				for (int m = 0; m < args.length; m++) {
					statement.setString(m + 1, args[m]);
					// log.info("[JdbcUtil][insertOrUpdateDB] SQL参数：[" + (m+1) +
					// "]: value:" + args[m]);
				}
			}
			i = statement.executeUpdate();
			connection.setAutoCommit(true);
		} catch (Exception e) {
			e.printStackTrace();
			connection.rollback();
		} finally {
			// 关闭资源
			closeStatement(statement);
			closeConnection(connection);
		}

		if (i == 1) {
			b = true;
		}
		return b;
	}
	
	/**
	 * 自定义连接的更新插入操作
	 * @param conn
	 * @param sql
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public static boolean insertOrUpdateDBByCustomConn(Connection conn, String sql, String... args) {
		PreparedStatement statement = null;
		boolean b = false;
		int i = -1;
		try {
			// 创建连接
			conn.setAutoCommit(false);
			// 3.创建表达式
			statement = conn.prepareStatement(sql);
			if (args != null && args.length != 0) {
				for (int m = 0; m < args.length; m++) {
					statement.setString(m + 1, args[m]);
				}
			}
			i = statement.executeUpdate();
			conn.setAutoCommit(true);
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			// 关闭资源
			closeStatement(statement);
		}

		if (i == 1) {
			b = true;
		}
		return b;
	}

	/**
	 * 
	 * (3、批量插入1)
	 * 
//	 * @param String
//	 *            sql 执行SQL文
//	 * @param List
	 *            argsLst 执行参数
	 * 
	 * @return
	 */
	public static boolean insertBatch(String sql, List<String[]> argsList) throws Exception {

		PreparedStatement statement = null;
		Connection connection = null;
		boolean isInsertSucess = false;
		if (argsList == null || argsList.size() == 0) {
			return false;
		}
		try {
			// 创建连接
			connection = getDBConn();
			connection.setAutoCommit(false);
			// 3.创建表达式
			statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			// log.info("[JdbcUtil][insertBatch] 执行SQL文：" + sql);
			// 取出参数
			for (int m = 0; m < argsList.size(); m++) {
				String[] args = argsList.get(m);
				if (args != null && args.length != 0) {
					for (int i = 0; i < args.length; i++) {
						statement.setString(i + 1, args[i]);
						// log.info("[JdbcUtil][insertBatch] SQL参数：[" + (i + 1)
						// + "]: value:" + args[i]);
					}
				}
				statement.addBatch();
				if (m > 0 && m % 10000 == 0) {
					statement.executeBatch();
					statement.clearBatch();
					connection.commit();
				}
			}
			if (statement != null) {
				statement.executeBatch();
				statement.clearBatch();
			}
			connection.commit();
			isInsertSucess = true;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (Exception se) {
				e.printStackTrace();
				throw e;
			}
			throw e;
		} finally {
			connection.setAutoCommit(true);
			closeStatement(statement);
			closeConnection(connection);

		}
		return isInsertSucess;
	}

	/**
	 * 
	 * (3、批量插入1)
	 * 
//	 * @param Connection
//	 *            connection 数据库连接
//	 * @param String
//	 *            sql 执行SQL文
//	 * @param List
	 *            argsLst 执行参数
	 * 
	 * @return
	 */
	public static boolean insertBatch02(Connection connection, String sql, List<String[]> argsLst) throws Exception {

		PreparedStatement statement = null;
		boolean isInsertSucess = false;
		if (argsLst == null || argsLst.size() == 0) {
			return false;
		}
		try {
			// 创建表达式
			statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			// log.info("[JdbcUtil][insertBatch02] 执行SQL文：" + sql);
			// 取出参数
			for (int m = 0; m < argsLst.size(); m++) {
				String[] args = argsLst.get(m);
				if (args != null && args.length != 0) {
					for (int i = 0; i < args.length; i++) {
						statement.setString(i + 1, args[i]);
						// log.info("[JdbcUtil][insertBatch02] SQL参数：[" + (i+1)
						// + "]: value:" + args[i]);
					}
				}
				statement.addBatch();
				if (m > 0 && m % 10000 == 0) {
					statement.executeBatch();
					statement.clearBatch();
					connection.commit();
				}
			}
			if (statement != null) {
				statement.executeBatch();
				statement.clearBatch();
			}
			connection.commit();
			isInsertSucess = true;
		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (Exception se) {
				e.printStackTrace();
			}
		} finally {
			closeStatement(statement);
		}
		return isInsertSucess;
	}

	/**
	 * (3、批量更新) 使用批量插入自定义提交方法（同时执行多个sql，失败回滚）
	 * 
	 * @return
	 */
	public static boolean customCommit(Connection connection, String... sqls) throws Exception {

		Statement statement2 = null;

		if (sqls == null || sqls.length == 0) {
			return false;
		}
		try {
			// 3.创建表达式，执行操作
			statement2 = connection.createStatement();

			if (sqls == null || sqls.length == 0) {
				return false;
			}

			for (String sql : sqls) {
				statement2.addBatch(sql);
			}
			statement2.executeBatch();
			// 提交事物
			connection.commit();
			return true;
		} catch (Exception e) {
			try {
				// 执行失败，回滚
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				throw e1;
			}
			e.printStackTrace();
		} finally {
			if (statement2 != null) {
				try {
					statement2.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
        return true;
	}

	/**
	 * 
	 * (4、调用存储过程--查询操作)
	 * 
	 * @param
	 * @return
	 * @throws
	 */
	public static String getCallableResult(Connection connection, String sql, String... args) throws Exception {
		CallableStatement callStatement = null;
		ResultSet resultSet = null;

		String value = null;
		try {
			// 创建表达式
			callStatement = connection.prepareCall(sql);
			if (args != null && args.length != 0) {
				for (int i = 0; i < args.length; i++) {
					callStatement.setString(i + 1, args[i]);
				}
			}

			resultSet = callStatement.executeQuery();
			// 取值
			while (resultSet.next()) {
				value = resultSet.getObject(1).toString();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			closeRs(resultSet);
			closeCall(callStatement);
		}

		return value;
	}

	/**
	 * 
	 * getDBConfig(得到jdbc.properties配置文件信息)
	 * 
	 * @return
	 * @return String[]
	 */
	public static String[] getDBConfig() {
		String[] dbConfig = new String[3];
		InputStream is = JdbcUtil.class.getClassLoader().getResourceAsStream("jdbcbydruid.properties");
		Properties property = new Properties();
		try {
			Reader reader = new InputStreamReader(is);
			property.load(reader);
			reader.close();

			dbConfig[0] = property.getProperty("jdbc.url");
			dbConfig[1] = property.getProperty("jdbc.username");
			dbConfig[2] = property.getProperty("jdbc.password");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dbConfig;
	}

	/**
	 * 
	 * 关闭连接
	 * 
	 * @return
	 */
	public static void closeConnection(Connection conn) {
		// 关闭资源
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * 关闭资源
	 * 
	 * @return
	 */
	public static void closeRs(ResultSet resultSet) {
		// 关闭资源
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	/**关闭connection
	 * by wangmeng 16.3.29
	 * @param connection
     */
	public static void closeConn(Connection connection){
		if(null != connection){
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * 关闭资源
	 * 
	 * @return
	 */
	public static void closeStatement(PreparedStatement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

	}

	/**
	 * 
	 * 关闭资源
	 * 
	 * @return
	 */
	public static void closeCall(CallableStatement callStatement) {
		if (callStatement != null) {
			try {
				callStatement.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

	}
}

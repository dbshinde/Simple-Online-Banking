/**
 * 
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Service;

/**
 * @author Dhananjay Shinde
 * @date Jul 16, 2014
 */
@Service
public class AdminDaoImpl implements AdminDao {

	/* (non-Javadoc)
	 * @see dao.AdminDao#authenticateAdmin(java.sql.Connection, java.lang.String, java.lang.String)
	 */
	@Override
	public int authenticateAdmin(Connection conn, String userName,String password) 
	{
		
		String sql = "SELECT * FROM admin where username = ? and password = ?";
		PreparedStatement stmt = null;
        ResultSet result = null;
        try 
         {
        	 stmt = conn.prepareStatement(sql);
        	 stmt.setString(1, userName);
        	 stmt.setString(2, password);
             result = stmt.executeQuery();

             while (result.next()) 
             {
            	 if (result.getInt("id") == 1)
            	 {
            		 return 1;
            	 }
             }

         } 
        catch (Exception e)
        {
        	return 0;
        }
        finally 
        {
             if (result != null)
				try {
					result.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
             if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
         }

		return 0;
	}

}

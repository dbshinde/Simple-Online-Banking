/**
 * 
 */
package dao;

import java.sql.Connection;

/**
 * @author Dhananjay Shinde
 * @date Jul 16, 2014
 */
public interface AdminDao 
{

	public int authenticateAdmin(Connection conn , String userName , String password);
}

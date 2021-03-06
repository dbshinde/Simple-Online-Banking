package dao;

import static helpers.Utils.isNotNullAndEmpty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.AccountType;

import org.springframework.stereotype.Repository;

import exceptions.NotFoundException;


 /**
  * Account Data Access Object (DAO).
  * This class contains all database handling that is needed to 
  * permanently store and retrieve Account object instances. 
  */

 /**
  * This sourcecode has been generated by FREE DaoGen generator version 2.4.1.
  * The usage of generated code is restricted to OpenSource software projects
  * only. DaoGen is available in http://titaniclinux.net/daogen/
  * It has been programmed by Tuomo Lukka, Tuomo.Lukka@iki.fi
  *
  * DaoGen license: The following DaoGen generated source code is licensed
  * under the terms of GNU GPL license. The full text for license is available
  * in GNU project's pages: http://www.gnu.org/copyleft/gpl.html
  *
  * If you wish to use the DaoGen generator to produce code for closed-source
  * commercial applications, you must pay the lisence fee. The price is
  * 5 USD or 5 Eur for each database table, you are generating code for.
  * (That includes unlimited amount of iterations with all supported languages
  * for each database table you are paying for.) Send mail to
  * "Tuomo.Lukka@iki.fi" for more information. Thank you!
  */


@Repository
public class AccountTypeDAOImpl implements AccountTypeDAO{


	
    /**
     * createValueObject-method. This method is used when the Dao class needs
     * to create new value object instance. The reason why this method exists
     * is that sometimes the programmer may want to extend also the valueObject
     * and then this method can be overrided to return extended valueObject.
     * NOTE: If you extend the valueObject class, make sure to override the
     * clone() method in it!
     */
    public AccountType createValueObject() {
          return new AccountType();
    }


    /**
     * getObject-method. This will create and load valueObject contents from database 
     * using given Primary-Key as identifier. This method is just a convenience method 
     * for the real load-method which accepts the valueObject as a parameter. Returned
     * valueObject will be created using the createValueObject() method.
     */
    public AccountType getObject(Connection conn, int account_type_id) throws NotFoundException, SQLException {

    	AccountType valueObject = createValueObject();
    	valueObject.setAccount_typeId(account_type_id);
    	load(conn, valueObject);
    	return valueObject;
    }


    /**
     * load-method. This will load valueObject contents from database using
     * Primary-Key as identifier. Upper layer should use this so that valueObject
     * instance is created and only primary-key should be specified. Then call
     * this method to complete other persistent information. This method will
     * overwrite all other fields except primary-key and possible runtime variables.
     * If load can not find matching row, NotFoundException will be thrown.
     *
     * @param conn         This method requires working database connection.
     * @param valueObject  This parameter contains the class instance to be loaded.
     *                     Primary-key field must be set for this to work properly.
     */
    public void load(Connection conn, AccountType valueObject) throws NotFoundException, SQLException {

          String sql = "SELECT * FROM account_type WHERE (account_typeId = ? ) "; 
          PreparedStatement stmt = null;

          try {
               stmt = conn.prepareStatement(sql);
               stmt.setInt(1, valueObject.getAccount_typeId()); 

               singleQuery(conn, stmt, valueObject);

          } finally {
              if (stmt != null)
                  stmt.close();
          }
    }


    /**
     * LoadAll-method. This will read all contents from database table and
     * build a List containing valueObjects. Please note, that this method
     * will consume huge amounts of resources if table has lot's of rows. 
     * This should only be used when target tables have only small amounts
     * of data.
     *
     * @param conn         This method requires working database connection.
     */
    public List<AccountType> loadAll(Connection conn) throws SQLException {

          String sql = "SELECT * FROM account_type ORDER BY account_typeId ASC ";
          List<AccountType> searchResults = listQuery(conn, conn.prepareStatement(sql));

          return searchResults;
    }

    /** 
     * searchMatching-Method. This method provides searching capability to 
     * get matching valueObjects from database. It works by searching all 
     * objects that match permanent instance variables of given object.
     * Upper layer should use this by setting some parameters in valueObject
     * and then  call searchMatching. The result will be 0-N objects in a List, 
     * all matching those criteria you specified. Those instance-variables that
     * have NULL values are excluded in search-criteria.
     *
     * @param conn         This method requires working database connection.
     * @param valueObject  This parameter contains the class instance where search will be based.
     *                     Primary-key field should not be set.
     */
    public List<AccountType> searchMatching(Connection conn, AccountType valueObject) throws SQLException {

          List<AccountType> searchResults;

          boolean first = true;
          StringBuffer sql = new StringBuffer("SELECT * FROM account_type WHERE 1=1 ");

          if (isNotNullAndEmpty(valueObject.getAccount_typeId()) && valueObject.getAccount_typeId() != 0) {
              if (first) { first = false; }
              sql.append("AND account_typeId = ").append(valueObject.getAccount_typeId()).append(" ").append("AND status = 'active'");
          }

          sql.append("ORDER BY account_id ASC ");

          // Prevent accidential full table results.
          // Use loadAll if all rows must be returned.
          if (first)
               searchResults = new ArrayList<AccountType>();
          else
               searchResults = listQuery(conn, conn.prepareStatement(sql.toString()));

          return searchResults;
    }


    /** 
     * getDaogenVersion will return information about
     * generator which created these sources.
     */
    public String getDaogenVersion() {
        return "DaoGen version 2.4.1";
    }


    /**
     * databaseUpdate-method. This method is a helper method for internal use. It will execute
     * all database handling that will change the information in tables. SELECT queries will
     * not be executed here however. The return value indicates how many rows were affected.
     * This method will also make sure that if cache is used, it will reset when data changes.
     *
     * @param conn         This method requires working database connection.
     * @param stmt         This parameter contains the SQL statement to be excuted.
     */
    public int databaseUpdate(Connection conn, PreparedStatement stmt) throws SQLException {

          int result = stmt.executeUpdate();

          return result;
    }



    /**
     * databaseQuery-method. This method is a helper method for internal use. It will execute
     * all database queries that will return only one row. The resultset will be converted
     * to valueObject. If no rows were found, NotFoundException will be thrown.
     *
     * @param conn         This method requires working database connection.
     * @param stmt         This parameter contains the SQL statement to be excuted.
     * @param valueObject  Class-instance where resulting data will be stored.
     */
    public void singleQuery(Connection conn, PreparedStatement stmt, AccountType valueObject) 
          throws NotFoundException, SQLException {

          ResultSet result = null;

          try {
              result = stmt.executeQuery();

              if (result.next()) {
            	  
            	   valueObject.setAccount_typeId(result.getInt("account_TypeId")); 
            	   valueObject.setAccount_type(result.getString("account_Type")); 

              } else {
                    //System.out.println("Account Object Not Found!");
                    throw new NotFoundException("AccountType Object Not Found!");
              }
          } finally {
              if (result != null)
                  result.close();
              if (stmt != null)
                  stmt.close();
          }
    }


    /**
     * databaseQuery-method. This method is a helper method for internal use. It will execute
     * all database queries that will return multiple rows. The resultset will be converted
     * to the List of valueObjects. If no rows were found, an empty List will be returned.
     *
     * @param conn         This method requires working database connection.
     * @param stmt         This parameter contains the SQL statement to be excuted.
     */
    public List<AccountType> listQuery(Connection conn, PreparedStatement stmt) throws SQLException {

          List<AccountType> searchResults = new ArrayList<AccountType>();
          ResultSet result = null;

          try {
              result = stmt.executeQuery();

              while (result.next()) {
                   AccountType temp = createValueObject();
                   temp.setAccount_typeId(result.getInt("account_TypeId")); 
                   temp.setAccount_type(result.getString("account_Type")); 
                   searchResults.add(temp);
              }

          } finally {
              if (result != null)
                  result.close();
              if (stmt != null)
                  stmt.close();
          }

          return searchResults;
    }

}
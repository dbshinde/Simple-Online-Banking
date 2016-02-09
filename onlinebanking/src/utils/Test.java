/**
 * 
 */
package utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import net.spy.memcached.MemcachedClient;

/**
 * @author Dhananjay Shinde
 * @date Jul 14, 2014
 */
public class Test 
{

	public static void main(String[] args) {
		try 
		{
			MemcachedClient mc = new MemcachedClient(new InetSocketAddress("localhost", 11211));
			Map m = new HashMap();
			m.put("name", "dhananjay B. Shinde");
			mc.add("ggf", 102000, m);
			System.out.println("test -->" + mc.get("ggf"));
			mc.delete("ggf");
			System.out.println("test -->" + mc.get("ggf"));
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}
	}
}

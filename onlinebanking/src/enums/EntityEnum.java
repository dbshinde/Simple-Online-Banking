package enums;


public enum EntityEnum {

		CUSTOMER ("customer"),
		ACCOUNT ("account");

	    private final String name;       

	    private EntityEnum(String s) {
	        name = s;
	    }

	    public boolean equalsName(String otherName) {
	        return (otherName == null) ? false : name.equals(otherName);
	    }

	    public String toString() {
	       return this.name;
	    }
	    public static void main(String[] args) throws Exception {
		}
}

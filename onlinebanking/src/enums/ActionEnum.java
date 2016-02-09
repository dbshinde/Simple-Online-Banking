package enums;



public enum ActionEnum {

	 	NEW ("new"),
	    DETAILS ("details"),
	    LOGIN ("login"),
	    SESSION ("session"),
	    DASHBOARD ("dashboard"),
	    SAVE_BANK_BRANCH("save_bank_branch"),
	    SAVE_ACCOUNT("save_account"),
	    SAVE_CUSTOMER("save_customer"),
	    SEARCH_ACCOUNT("search_account"),
	    SEARCH_CUSTOMER("search_customer"),
	    LOGOUT("logout"),
	    EDIT("edit"),
	    LIST("list"),
	    POST("post"),
	    TRANSACTION("transaction"),
	    SEARCH("search"),
	    BANK_BRANCH ("bank_branch"),
	    CUSTOMER ("customer"),
		ACCOUNT ("account");

	    private final String name;       

	    private ActionEnum(String s) {
	        name = s;
	    }

	    public  boolean equalsName(String otherName) {
	        return (otherName == null) ? false : name.equals(otherName);
	    }

	    public String toString() {
	       return this.name;
	    }
	    
	    public static ActionEnum getValue(String name)
	    {
	    	return ActionEnum.valueOf(name.toUpperCase());
	    }
	    public static void main(String[] args) throws Exception {
			System.out.println(ActionEnum.valueOf("NEW"));
		}
}

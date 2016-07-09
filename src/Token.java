public class Token {
	private int type;
	
	/*
	 NUM = 0,
	 ID = 1,  
	 LPAREN = 2,
	 RPAREN = 3,
	 PLUS = 4,   
	 MINUS = 5,       
	 DIV = 6,    
	 MUL = 7,
	 EQ = 8,
	 ASSIGN = 9,
	 ERROR = 10;
	 */
	private String value;
	//constructors
	public Token(){
		type = 0;
		value = "";
	}
	public Token(int t){
		type = t;
		value = "";
	}
	public Token(int t, String v){
		type = t;
		value = v;
	}
	//getters and setters
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

}
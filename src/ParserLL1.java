import java.util.Stack;


public class ParserLL1 {

	public static final int EOFtok = -1;
	public static final int
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
	 ERROR = 10,
	 SEMICOLON = 11,
	 SMALLERTHAN = 12,
	 //GREATERTHAN = 13,

	 IFtoken = 20,
	 THENtoken = 21,
	 ELSEtoken = 22,
	 ENDtoken = 23,
	 REPEATtoken = 24,
	 UNTILtoken = 25,
	 READtoken = 26,
	 WRITEtoken = 27;

	public static TinyScanner myScanner = null;

	Stack<String> parsingStack = new Stack<String>();
	public String EOFStack = "$";
	public Token input;
	public String inputType;

	//M[N,T]: Parsing Table where N: Non-terminals, T: Terminals
	//ArrayList<ArrayList<Integer>> M = new ArrayList<ArrayList<Integer>>();
	String[] N = {"program", "stmt_seq", "stmt_seq_d", "stmt", "if_stmt", "if_stmt_d",
			"repeat_stmt", "assign_stmt", "read_stmt", "write_stmt", "exp", "exp_d",
			"comparison_op", "simple_exp", "simple_exp_d", "addop", "term", "term_d",
			"mulop", "factor"}; //Non-terminals
	String[] T = {"(", "number", "identifier", "+", "-", "*", "/", ")", "<","=",
			"read", "write", "if", "then", "else", "end", "repeat", "until", ";", "$", ":="}; //Terminals

	String[][] M = new String[N.length][T.length];
	public ParserLL1(){

	}
	public void initializeParsingTable(){
		//initialize parsing table with zeroes
				for(int i=0; i<N.length; i++){
					for(int j=0; j<T.length; j++){
						M[i][j] = "0";
					}
				}
				//adding values to parsing table
				M[0][2] = "stmt_seq";
				M[0][10] = "stmt_seq"; M[0][11] = "stmt_seq"; M[0][12] = "stmt_seq"; M[0][16] = "stmt_seq";
				M[1][2] = "stmt stmt_seq_d";
				M[1][10] = "stmt stmt_seq_d"; M[1][11] = "stmt stmt_seq_d";
				M[1][12] = "stmt stmt_seq_d"; M[1][16] = "stmt stmt_seq_d";
				M[2][14] = ""; M[2][15] = "";
				M[2][17] = ""; M[2][19] = "";
				M[2][18] = "; stmt_seq_d";
				M[3][2] = "assign_stmt";
				M[3][10] = "read_stmt"; M[3][11] = "write_stmt";
				M[3][12] = "if_stmt"; M[3][16] = "repeat_stmt";
				M[4][12] = "if exp then stmt_seq if_stmt_d";
				M[5][14] = "else stmt_seq end"; M[5][15] = "end";
				M[6][16] = "repeat stmt_seq until exp";
				M[7][2] = "identifier := exp";
				M[8][10] = "read identifier";
				M[9][11] = "write exp";
				M[10][0] = "simple_exp exp_d"; M[10][1] = "simple_exp exp_d";
				M[10][2] = "simple_exp exp_d";
				M[11][7] = "";
				M[11][8] = "comparison_op simple_exp"; M[11][9] = "comparison_op simple_exp";
				M[11][13] = ""; M[11][14] = ""; M[11][15] = "";
				M[11][17] = ""; M[11][18] = ""; M[11][19] = "";
				M[12][8] = "<"; M[12][9] = "=";
				M[13][0] = "term simple_exp_d";
				M[13][1] = "term simple_exp_d";
				M[13][2] = "term simple_exp_d";
				M[14][3] = "addop term simple_exp_d"; M[14][4] = "addop term simple_exp_d";
				M[14][8] = ""; M[14][9] = ""; M[14][13] = ""; M[14][14] = ""; M[14][15] = "";
				M[14][17] = ""; M[14][18] = ""; M[14][19] = "";
				M[15][3] = "+"; M[15][4] = "-";
				M[16][0] = "factor term_d"; M[16][1] = "factor term_d"; M[16][2] = "factor term_d";
				M[17][3] = ""; M[17][4] = "";
				M[17][5] = "mulop factor term_d"; M[17][5] = "mulop factor term_d";
				M[17][8] = ""; M[17][9] = ""; M[17][13] = ""; M[17][14] = ""; M[17][15] = "";
				M[17][17] = ""; M[17][18] = ""; M[17][19] = "";
				M[18][5] = "*"; M[18][6] = "/";
				M[19][0] = "( exp )";
				M[19][1] = "number"; M[19][2] = "identifier";
	}
	public boolean isTerminal(String s){
		for(int i=0; i<T.length; i++){
			if(s.equals(T[i])){
				return true;
			}
		}
		return false;
	}
	public int getTerminalIndex(String s){
		for(int i=0; i<T.length; i++){
			if(s.equals(T[i])){
				return i;
			}
		}
		return -1;
	}
	public boolean isNonTerminal(String s){
		for(int i=0; i<N.length; i++){
			if(s.equals(N[i])){
				return true;
			}
		}
		return false;
	}
	public int getNonTerminalIndex(String s){
		for(int i=0; i<N.length; i++){
			if(s.equals(N[i])){
				return i;
			}
		}
		return -1;
	}
	public String getInputType(){
		String in;
		if(input.getType() == NUM){
			in = "number";
		}
		else if(input.getType() == ID){
			in = "identifier";
		}
		else if(input.getType() == EOFtok){
			in = "$";
		}
		else{
			in = T[getTerminalIndex(input.getValue())];
		}
		return in;
	}
	public boolean parse(){
		initializeParsingTable();
		input = myScanner.getToken();
		inputType = getInputType();
		parsingStack.push(EOFStack);
		parsingStack.push(N[0]);
		while(parsingStack.peek() != EOFStack && inputType != "$"){
			if(isTerminal(parsingStack.peek())){
				if(!match(parsingStack.peek())) return false;
				parsingStack.pop();
			}
			else if(isNonTerminal(parsingStack.peek())
					&& isTerminal(inputType)
					&& ( M[getNonTerminalIndex(parsingStack.peek())][getTerminalIndex(inputType)] != "0")){
				//System.out.println("Parsing Stack: " + parsingStack.peek());
				//System.out.println("Parsing Stack index: " + getNonTerminalIndex(parsingStack.peek()));
				//System.out.println("inputType: " + inputType);
				//System.out.println("inputType index: " + getTerminalIndex(inputType));
				if(!generate()) return false;

			}
			else{
				System.out.println("Parsing error");
				return false;
			}
		}
		if(inputType == "$"){
			parsingStack.pop();
		}
		if(parsingStack.peek().equals(EOFStack) && inputType == "$"){
			accept();
		}
		else{
			System.out.println("Parsing Stack end error");
			return false;
		}
		return true;
	}
	public boolean match(String expectedTokenS){
		if(inputType.equals(expectedTokenS)){
			System.out.println("Match token: " + input.getValue());
			input = myScanner.getToken(); //advance token
			inputType = getInputType();

			//System.out.println("here inputType: " + inputType);
			return true;
		}
		else{
			System.out.println("Error, statement doesn't match grammar rules");
			return false;
		}
	}
	public boolean generate(){
		String s;
		if(getNonTerminalIndex(parsingStack.peek()) != -1 && getTerminalIndex(inputType) != -1){
			s = M[getNonTerminalIndex(parsingStack.peek())][getTerminalIndex(inputType)];
			System.out.println("generate: "+ s);
			String[] stringArray = s.split(" ");
			parsingStack.pop();
			if(!s.equals("")){
				for(int i=stringArray.length-1; i>=0; i--){
					//System.out.println("Array[i]"+stringArray[i]);
					parsingStack.push(stringArray[i]);
				}
			}
		}
		else{
			System.out.println("can't generate");
			return false;
		}
		return true;
	}
	public void accept(){
		System.out.println("Accepted!");
	}

}

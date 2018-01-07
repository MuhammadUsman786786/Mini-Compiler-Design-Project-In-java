public class TinyParser {
	/*
	 	Grammar Rules:

		program -> stmt-sequence
		stmt-sequence -> stmt {; stmt}
		stmt -> if-stmt |repeat-stmt | assign-stmt | read-stmt | write-stmt
		if-stmt -> if exp then stmt-sequence [ else stmt-sequence] end
		repeat-stmt -> repeat stmt-sequence until exp
		assign-stmt -> identifier := exp
		read-stmt -> read identifier
		write-stmt -> write exp
		exp -> simple-exp [comparison-op simple-exp]
		comparison-op -> < | =
		simple-exp -> term {addop term}
		addop -> +| -
		term -> factor {mulop factor}
		mulop -> *| /
		factor -> (exp) | number | identifier

	 */
	public static TinyScanner myScanner = null;
	static Token token;
	static char t;
	//individual tokens
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
	 GREATERTHAN = 13,
	 IFtoken = 20,
	 THENtoken = 21,
	 ELSEtoken = 22,
	 ENDtoken = 23,
	 REPEATtoken = 24,
	 UNTILtoken = 25,
	 READtoken = 26,
	 WRITEtoken = 27;

	/*public static void main(String args[]){
		boolean result = false;
		//t = myScanner.getNextChar();
		myScanner = new TinyScanner("test.txt");
		token = myScanner.getToken();
		while(token.getType() != EOFtok){
			result = program();
		}
		if(result == true){
			System.out.println("** Parsing complete **");
		}
		else{
			System.out.println("** Parsing error **");
		}
	}*/
	public static boolean program(){ //program -> stmt-seq
		if(!stmt_seq()) return false;
		return true;
	}
	public static boolean stmt_seq(){ //stmt-seq -> stmt {; stmt}
		if(!stmt()) return false;
		while(token.getType() == SEMICOLON){
			if(!match(SEMICOLON)) return false;
			if(!stmt()) return false;
		}
		return true;
	}
	public static boolean stmt(){	//stmt -> if-stmt | repeat-stmt | assign-stmt | read-stmt | write-stmt
		//if-stmt
		if(token.getType() == IFtoken){
			if(!if_stmt()) return false;
		}
		//assign-stmt
		else if(token.getType() == ID){
			if(!assign_stmt()) return false;
		}
		//repeat-stmt
		else if(token.getType() == REPEATtoken){
			if(!repeat_stmt()) return false;
		}
		//read-stmt
		else if(token.getType() == READtoken){
			if(!read_stmt()) return false;
		}
		//write-stmt
		else if(token.getType() == WRITEtoken){
			if(!write_stmt()) return false;
		}
		return true;
	}
	public static boolean if_stmt(){ //if-stmt -> if exp then stmt-sequence [ else stmt-sequence] end
		if(token.getType() == IFtoken){
			if(!match(IFtoken)) return false;

			if(!exp()) return false;

			if(token.getType() == THENtoken){
				if(!match(THENtoken)) 	return false;
			}

			if(!stmt_seq()) return false;
		}

		if(token.getType() == ELSEtoken){
			if(!match(ELSEtoken)) return false;

			if(!stmt_seq()) return false;
		}
		if(token.getType() == ENDtoken){
			if(!match(ENDtoken)) return false;
		}
		return true;
	}
	public static boolean assign_stmt(){	//assign_stm -> identifier assign exp
		if(token.getType() == ID){
			if(!match(ID)) return false;
			if(token.getType() == ASSIGN){
				if(!match(ASSIGN)) return false;
			}
			if(!exp()) return false;
		}

		return true;
	}
	public static boolean repeat_stmt(){ //repeat-stmt -> repeat stmt-sequence until exp
		if(token.getType() == REPEATtoken){
			if(!match(REPEATtoken)) return false;

			if(!stmt_seq()) return false;

			if(token.getType() == UNTILtoken){
				if(!match(UNTILtoken));
			}

			if(!exp()) return false;
		}

		return true;
	}
	public static boolean read_stmt(){ //read-stmt -> read identifier
		if(token.getType() == READtoken){
			if(!match(READtoken)) return false;

			if(token.getType() == ID){
				if(!match(ID)) return false;
			}
		}

		return true;
	}
	public static boolean write_stmt(){ //write-stmt -> write exp
		if(token.getType() == WRITEtoken){
			if(!match(WRITEtoken)) return false;

		if(!exp()) return false;
		}
		return true;
	}
	public static boolean exp(){ //exp -> simple-exp [comparison-op simple-exp]
		if(!simple_exp()) return false;

		if(token.getType() == SMALLERTHAN){
			if(!match(SMALLERTHAN)) return false;

			if(!simple_exp()) return false;
		}
		else if(token.getType() == EQ){
			if(!match(EQ)) return false;

			if(!simple_exp()) return false;
		}
		else if(token.getType() == GREATERTHAN){
			if(!match(GREATERTHAN)) return false;

			if(!simple_exp()) return false;
		}

		return true;
	}
	public static boolean simple_exp(){	//simple-exp -> term { addop term }
		if(!term()) return false;
		while((token.getType() == PLUS) || (token.getType() == MINUS)){
				if(token.getType() == PLUS){
					if(!match(PLUS))
						return false;
				}
				else if(token.getType() == MINUS){
					if(!match(MINUS))
						return false;
				}
			if(!term()) return false;
		}
		return true;
	}

	public static boolean term(){		//term -> factor { mulop factor }
		if(!factor()) return false;

			while(token.getType() == MUL || token.getType() == DIV){
				if(token.getType() == MUL){
					if(!match(MUL))
						return false;
				}
				else if(token.getType() == DIV){
					if(!match(DIV))
						return false;
				}
			if(!factor()) return false;
			}
		return true;
	}
	public static boolean factor(){		//factor -> (exp) | number
			if(token.getType() == LPAREN){
				if(!match(LPAREN))
					return false;

				if(!exp()) return false;
				if(!match(RPAREN)){
					System.out.println("unexpected symbol");
					return false;
				}
			}
			else if(token.getType() == NUM){ //number
				if(!match(NUM))
					return false;
			}
			else if(token.getType() == ID){
				if(!match(ID))
					return false;
			}
			else{
				System.out.println(token.getValue() + " didn't match");
				token = myScanner.getToken();
				return false;
			}
		return true;
	}

	public static boolean match(int expectedTokenNo){
		if(token.getType() == expectedTokenNo){
			System.out.println("Match token: " + token.getValue());
			token = myScanner.getToken();
			return true;
		}
		else{
			System.out.println("Error, statement doesn't match grammar rules");
			return false;
		}
	}
}

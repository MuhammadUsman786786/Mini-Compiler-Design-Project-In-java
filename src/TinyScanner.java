import java.io.*;
import java.util.*;

public class TinyScanner {
	public final char EOF = (char) -1;
	//states
	public final int START=1;
	public final int INNUM=2;
	public final int INID=3;
	public final int INASSIGN=4;
	public final int INCOMMENT=5;
	public final int DONE=6;


	//values
	//char input;
	String tokenString;
	String lineBuf = "";
	int lineN=0, lineP=0;
	String fname = "test.txt";
	BufferedReader bufRdr;
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
	//reserved words
	public final String reservedWords[]= {
		    "if","then", "else", "end", "repeat", "until",
		    "read", "write" };
	public final int numReservedWords = reservedWords.length;

	char getNextChar(){
		char c=EOF;
		if(lineBuf == "" || lineBuf == null || (lineP >= lineBuf.length())){
			lineP = 0;
			try {
				lineBuf = bufRdr.readLine();
				lineN++;
				if(lineBuf == null)
					c=EOF;
				else if(lineBuf.equals("")){
					lineBuf+="\n";
					c = getNextChar();
				}
				else{
					lineBuf+="\n";
					lineP = 0;
					c = lineBuf.charAt(lineP++);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else{
			c = (char) lineBuf.charAt(lineP++);
		}
		return c;
	}
	void ungetNextChar(){
		lineP--;
	}

	Token getToken(){
		Token currentToken = null;
		boolean save;
		tokenString = "";
		int state = START;
		char c = EOF;
		while(state != DONE){
			c = getNextChar();
			save = true;
			switch(state){
			case START:
				if(Character.isDigit(c)){
					state = INNUM;
				}
				else if(Character.isLetter(c))
					state = INID;
				else if(c == ':')
					state = INASSIGN;
				else if(c == '{'){
					state = INCOMMENT;
					save = false;
				}
				else if(Character.isWhitespace(c))
					save = false;
				else{
					state = DONE;
					switch(c)
                    {
                      case '=':
                        currentToken=new Token(EQ);
                        break;
                      case '+':
                        currentToken= new Token (PLUS);
                        break;
                      case '-':
                        currentToken = new Token (MINUS);
                        break;
                      case '*':
                        currentToken = new Token (MUL);
                        break;
                      case '/':
                        currentToken = new Token (DIV);
                        break;
                      case '(':
                        currentToken = new Token  (LPAREN);
                        break;
                      case ')':
                        currentToken = new Token (RPAREN);
                        break;
                      case ';':
                      	currentToken = new Token(SEMICOLON);
                      	break;
                      case '<':
                      	currentToken = new Token(SMALLERTHAN);
                        break;

                      case '>':
                      	currentToken = new Token(GREATERTHAN);
                        break;
                      case EOF:
                        currentToken = new Token (EOFtok);
                        break;
                      default:
                        currentToken = new Token  (ERROR);
                    }
				}
				break;
			case INNUM:
				if(!Character.isDigit(c)){
					ungetNextChar();
					save = false;
					state = DONE;
					currentToken = new Token(NUM);	currentToken.setValue(tokenString);
				}
				break;
			case INID:
				if(!Character.isLetter(c)){
					ungetNextChar();
					save = false;
					state = DONE;
					currentToken = new Token(ID);	currentToken.setValue(tokenString);
				}
				break;
			case INASSIGN:
				state = DONE;
				if(c == '='){
					currentToken = new Token(ASSIGN);	currentToken.setValue(":=");
				}else{
					ungetNextChar();
					save = false;
					currentToken = new Token(ERROR);
				}
				break;
			case INCOMMENT:
				save = false;
				if(c =='}')
					state = START;
				break;
			default:
				System.out.println("Error at state: " + state);
				state = DONE;
				currentToken = new Token(ERROR);
				break;
			}
			if(save){
				tokenString = tokenString + c;
			}
		}
		currentToken.setValue(tokenString);

		//check for reserved words
		if(currentToken.getType() == ID){
			for(int i=0; i<reservedWords.length; i++){
				if(reservedWords[i].equals(currentToken.getValue())){
					switch(currentToken.getValue()){
						case "if": currentToken.setType(IFtoken);	break;
						case "then": currentToken.setType(THENtoken); break;
						case "else": currentToken.setType(ELSEtoken); break;
						case "end": currentToken.setType(ENDtoken); break;
						case "repeat": currentToken.setType(REPEATtoken); break;
						case "until": currentToken.setType(UNTILtoken); break;
						case "read": currentToken.setType(READtoken); break;
						case "write": currentToken.setType(WRITEtoken); break;
					}
				}
			}
		}
		return currentToken;
	}

	public TinyScanner(String fname){
		try {
			File file = new File(fname);
			bufRdr = new BufferedReader(new FileReader(file));

		} catch (FileNotFoundException e) {
			System.out.println("Error opening input file: " + fname);
		}
	}


}

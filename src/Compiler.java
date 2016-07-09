import java.util.Scanner;


public class Compiler {

static Token token;
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
	 IFtoken = 20,
	 THENtoken = 21,
	 ELSEtoken = 22,
	 ENDtoken = 23,
	 REPEATtoken = 24,
	 UNTILtoken = 25,
	 READtoken = 26,
	 WRITEtoken = 27;


	public static void main(String args[]){
		TinyParser myParser = new TinyParser();
		TinyScanner myScanner1= new TinyScanner("test.txt");
		ParserLL1 myParserLL1 = new ParserLL1();
		System.out.println(" 1 for Syntax Phase for recursive descent parser");
		System.out.println(" 2 forSyntax Phase for Top down LL1 parser");
		System.out.println(" 3 Lexical Phase for Token Generated");

		Scanner input = new Scanner(System.in);
		int choice = input.nextInt();
		if(choice == 1){
			boolean result = false;
			myParser.myScanner = new TinyScanner("test.txt");
			myParser.token = myParser.myScanner.getToken();

			while(myParser.token.getType() != myParser.EOFtok){
				result = myParser.program();
			}
			if(result == true){
				System.out.println("** Parsing complete **");
			}
			else{
				System.out.println("** Parsing error **");
			}
		}
		else if(choice == 2){
			myParserLL1.myScanner = new TinyScanner("test.txt");
			if(myParserLL1.parse()){
				System.out.println("***Parsing done***");
			}
			else{
				System.out.println("** Parsing error **");
			}
		}
        else if(choice == 3){


			myParser.myScanner = new TinyScanner("test.txt");
			myParser.token = myParser.myScanner.getToken();

			while(myParser.token.getType() != myParser.EOFtok){
		System.out.println("token generated=="+myParser.token.getType()+"\n");
		myParser.token = myParser.myScanner.getToken();


	}


		}



		else{
			System.out.println("Please type 1 or 2 or 3only");
		}
	}
}

import java.util.ArrayList;

public class Compiler {
	public static void main(String args[]){

		String inputFile= args[0];
		Lexer lexer=new Lexer();
		ArrayList tokens=new ArrayList();
		tokens=lexer.Analyser(inputFile);
		for(int i=0;i<tokens.size();i++){
			//System.out.println((String)tokens.get(i) + "" + i);
		}
		
		Parser parser=new Parser(tokens,inputFile);
		parser.Parsing();
	}
}

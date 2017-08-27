import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class Parser {
	public static ArrayList<String> tokens = new ArrayList<String>();
	public static String fileName;
	public static int count=0;
	public static int flag=1;
	public static String lookAhead= " ";
	
	//Tree Declarations
	TreeNode<String> declarationNode;
	TreeNode<String> programNode;
	TreeNode<String> beginNode;
	TreeNode<String> statementSequenceNode;
	TreeNode<String> endNode;
	TreeNode<String> varNode;
	TreeNode<String> idenNode;
	TreeNode<String> asNode;
	TreeNode<String> scNode;
	TreeNode<String> typeNode;
	TreeNode<String> intNode;
	TreeNode<String> boolNode;
	TreeNode<String> statementNode;
	TreeNode<String> assignmentNode;
	TreeNode<String> ifStatementNode;
	TreeNode<String> whileStatementNode;
	TreeNode<String> writeintNode;
	TreeNode<String> asgnNode;
	TreeNode<String> assignmentModifiedNode;
	TreeNode<String> expressionNode;
	TreeNode<String> readInt;
	TreeNode<String> ifNode;
	TreeNode<String> thenNode;
	TreeNode<String> elseClauseNode;
	TreeNode<String> elseNode;
	TreeNode<String> whileNode;
	TreeNode<String> doNode;
	TreeNode<String> compareNode;
	TreeNode<String> compNode;
	TreeNode<String> simpleExpressionNode;
	TreeNode<String> writeNode;
	TreeNode<String> termNode;
	TreeNode<String> addNode;
	TreeNode<String> additiveNode;
	TreeNode<String> rpNode;
	TreeNode<String> lpNode;
	TreeNode<String> boollitNode;
	TreeNode<String> numNode;
	TreeNode<String> multiNode;
	TreeNode<String> factorNode;
	TreeNode<String> multiplicativeNode;
	TreeNode<String> emptyNode;
	TreeNode<String> whileStatementSequenceNode;
	TreeNode<String> ifStatementSequenceNode;
	public static int whileToken=1;
	public static int ifToken=1;
	Hashtable symbolTable = new Hashtable();
			
	//Tree Declarations Ends
	
	public Parser(ArrayList tokens,String fileName){
		this.tokens=tokens;
		this.fileName=fileName;
		lookAhead = (String)tokens.get(count);
	}
	
	public void Parsing(){
		this.program();
		if(lookAhead.equals("END") && flag==1){
			System.out.println("Parsing Success");
		}
		
		for(TreeNode<String> n: programNode){
			if(n.data.equals("expression")){
				n.data=n.children.get(0).data;
				n.children.clear();
			}
			/*if(n.data.startsWith("<=")){
				n.children.get(0).data="*";
				n.add("SQRT:int");
				n.children.get(0).children.get(1).data="SQRT:int";
			}*/
		}
		CodeGeneration cg = new CodeGeneration(programNode,fileName);
		
		TypeCheck check=new TypeCheck();
		programNode =check.checking(programNode);
	
		DotGeneration dot=new DotGeneration(fileName);
		dot.Generator(programNode);
		
		
		
	}
	
	public void match(String token){
		if(token.equals(lookAhead)&&count<tokens.size()-1){
			lookAhead=(String)tokens.get(++count);
		}
		else if(!token.equals(lookAhead)){
			System.out.println("Parsing Error Expected: " + token + " Found: " + lookAhead);
			flag=0;
		}
	}
	
	// <program>::=PROGRAM <declarations> BEGIN <statementSequence> END
	public void program(){
		if(lookAhead.equals("PROGRAM")){
			programNode = new TreeNode<String>("program");
			this.match("PROGRAM");			
			declarationNode = programNode.addChild("decl list");
			this.declarations();
			this.match("BEGIN");
			statementSequenceNode = programNode.addChild("stmt list");
			this.statementSequence();
			this.match("END");
		}
	}
	
	//<declarations>::=VAR ident AS <type> SC <declarations>|E
		static String iden="";
		static String typeTable="";
		public void declarations(){
			if(lookAhead.equals("VAR")){
				this.match("VAR");
				if(lookAhead.contains("iden(")){
					iden=lookAhead;
					this.match(lookAhead);
				}
				this.match("AS");
				typeTable=lookAhead;
				symbolTable.put(iden.toLowerCase(),typeTable.toLowerCase());
				this.type();
				idenNode = declarationNode.addChild("decl:'" + iden.substring(5,iden.length()-1) + "':" + type);
				this.match("SC(;)");
				
				this.declarations();
			}
			else{
				return;
			}
		}
		static String type;
		//<type>::=INT||BOOL
		public void type(){
			if(lookAhead.equals("INT")){
				this.match("INT");
				type="int";
			}
			else if(lookAhead.equals("BOOL")){
				this.match("BOOL");
				type="bool";
			}
		}
	
	//<statementSequence>::=<statement> SC <statementSequence> | E
	public void statementSequence(){
		if(lookAhead.contains("iden(")||lookAhead.equals("IF")||lookAhead.equals("WHILE")||lookAhead.equals("WRITEINT")){
			this.statement();
			this.match("SC(;)");
			this.statementSequence();
		}
		else{
			return;
		}
	}
	
	//<statement>::=<assignment>||<ifStatement>||<whileStatement>||<writeInt>
	public void statement(){
		if(lookAhead.contains("iden(")){
			ifToken=1;
			if(whileToken==0){
				assignmentNode = whileStatementSequenceNode.addChild(":=");
			}
			else{
				assignmentNode = statementSequenceNode.addChild(":=");
			}
			this.assignment();
		}
		else if(lookAhead.equals("IF")){
			if(whileToken==0){
				ifStatementNode = whileStatementSequenceNode.addChild("if");
			}
			else if(ifToken==0){
				assignmentNode = ifStatementSequenceNode.addChild(":=");
			}
			else{
			ifStatementNode = statementSequenceNode.addChild("if");	
			}
			this.ifStatement();
		}
		else if(lookAhead.equals("WHILE")){
			whileToken= 0 ;
			if(ifToken==0){
				assignmentNode = ifStatementSequenceNode.addChild(":=");
			}
			else{
				whileStatementNode = statementSequenceNode.addChild("while");
			}
			this.whileStatement();
		}
		else if(lookAhead.equals("WRITEINT")){
			if(whileToken==0){
				writeintNode = whileStatementSequenceNode.addChild("writeint");
			}
			else{
				writeintNode = statementSequenceNode.addChild("writeint");
			}
			this.writeInt();
		}
	}
	
	//<assignment>::=ident ASGN <assignmentModified>
	public void assignment(){
		if(lookAhead.contains("iden(")){
			idenNode = assignmentNode.addChild(lookAhead.substring(5,lookAhead.length()-1)+ ":" +symbolTable.get(lookAhead.toLowerCase()));
			this.match(lookAhead);
			this.match("ASGN(:=)");
			this.assignmentModified();
		}
	}
	
	//<assignmentModified>::=<expression>|READINT
	public void assignmentModified(){
		if(lookAhead.contains("iden(")||lookAhead.contains("num(")||lookAhead.contains("boollit(")||lookAhead.equals("LP(()")){
			expressionNode = assignmentNode.addChild("expression");
			this.expression();
		}
		else if(lookAhead.equals("READINT")){
			readInt = assignmentNode.addChild("readint:int");
			this.match("READINT");
		}
	}
	
	//<ifStatement>::=IF<expression> THEN <statementSequence> <elseClause> END
	public void ifStatement(){
		if(lookAhead.equals("IF")){
			this.match("IF");
			expressionNode = ifStatementNode.addChild("expression");
			this.expression();
			this.match("THEN");
			ifStatementSequenceNode = ifStatementNode.addChild("StatementSequence");
			this.statementSequence();
			elseClauseNode = ifStatementNode.addChild("elseClause");
			this.elseClause();
			this.match("END");
			ifToken=1;
		}
	}
	
	//<elseClause>::=ELSE<statementSequence>|E
	public void elseClause(){
		if(lookAhead.equals("ELSE")){
			elseNode = elseClauseNode.addChild("else");
			this.match("ELSE");
			statementSequenceNode = elseClauseNode.addChild("StatementSequnce");
			this.statementSequence();
		}
		else{
			return;
		}
	}
	
	//<whileStatement>::=WHILE<expression> DO <statementSequence> END
	public void whileStatement(){
		if(lookAhead.equals("WHILE")){
			this.match("WHILE");
			expressionNode = whileStatementNode.addChild("expression");
			this.expression();
			this.match("DO");
			whileStatementSequenceNode = whileStatementNode.addChild("StatementSequence");
			this.statementSequence();
			this.match("END");
			whileToken=1;
		}
	}
	
	//<writeint>::=WRITEINT<expression>
	public void writeInt(){
		if(lookAhead.equals("WRITEINT")){
			this.match("WRITEINT");
			expressionNode = writeintNode.addChild("expression");
			this.expression();
		}
	}
	
	//<expression>::=<simpleExpression><comp>
	static int com=0;
	public void expression(){
		if(lookAhead.contains("iden(")||lookAhead.contains("num(")||lookAhead.contains("boollit(")||lookAhead.equals("LP(()")){
			//simpleExpressionNode = expressionNode.addChild("SimpleExpression");
			this.simpleExpression();
				//compNode = expressionNode.addChild("Compare");	
			this.comp();
		}
		}
	
	
	//<comp>::=COMPARE<expression>|E
	public void comp(){
		if(lookAhead.contains("COMPARE")){
			expressionNode.data=lookAhead.substring(8,lookAhead.length()-1)+":bool";
			//compareNode = expressionNode.addChild(lookAhead.substring(8,lookAhead.length()-1)+":bool");
			this.match(lookAhead);
			//expressionNode = expressionNode.addChild("Expression");
			this.expression();
		}
		else{
			return;
		}
	}
	
	//<simpleExpression>::=<term><add>
	public void simpleExpression(){
		if(lookAhead.contains("iden(")||lookAhead.contains("num(")||lookAhead.contains("boollit(")||lookAhead.equals("LP(()")){
			//termNode = simpleExpressionNode.addChild("Term");
			this.term();
			//addNode = simpleExpressionNode.addChild("Add");
			this.add();
		}
	}
	
	//<add>::==ADDITIVE<simpleExpression>|E
	public void add(){
		if(lookAhead.contains("ADDITIVE(")){
			expressionNode.data=lookAhead.substring(9,lookAhead.length()-1)+":int";
			//additiveNode = expressionNode.addChild(lookAhead.substring(9,lookAhead.length()-1)+":int");
			this.match(lookAhead);
			//simpleExpressionNode = simpleExpressionNode.addChild("SimpleExpression");
			this.simpleExpression();
		}
		else{
			return;
		}
	}
	
	//<term>::=<factor><multi>
	public void term(){
		if(lookAhead.contains("iden(")||lookAhead.contains("num(")||lookAhead.contains("boollit(")||lookAhead.equals("LP(()")){
			//factorNode = termNode.addChild("Factor");
			this.factor();
			//multiNode = termNode.addChild("Multi");
			this.multi();
		}
	}
	
	//<multi>::=MULTIPLICATIVE<term>|E
	public void multi(){
		if(lookAhead.contains("MULTIPLICATIVE(")){
			expressionNode.data=lookAhead.substring(15,lookAhead.length()-1)+"int";
			//multiplicativeNode = expressionNode.addChild(lookAhead.substring(15,lookAhead.length()-1)+"int");
			this.match(lookAhead);
			//termNode = simpleExpressionNode.addChild("Term");
			this.term();
		}
		else{
			return;
		}
	}
	
	//<factor>::=ident|num|boollit|LP<expression>RP
	public void factor(){
		if(lookAhead.contains("iden(")){
			idenNode = expressionNode.addChild(lookAhead.substring(5,lookAhead.length()-1)+":"+symbolTable.get(lookAhead.toLowerCase()));
			this.match(lookAhead);
		}
		else if(lookAhead.contains("num(")){
			numNode = expressionNode.addChild(lookAhead.substring(4,lookAhead.length()-1) + ":int");
			this.match(lookAhead);
		}
		else if(lookAhead.contains("boollit(")){
			boollitNode = expressionNode.addChild(lookAhead.substring(8,lookAhead.length()-1) + ":bool");
			this.match(lookAhead);
		}
		else if(lookAhead.equals("LP(()")){
			lpNode = expressionNode.addChild(lookAhead.substring(3,lookAhead.length()-1));
			this.match("LP(()");
			expressionNode = expressionNode.addChild("expression");
			this.expression();
			rpNode = expressionNode.addChild(lookAhead.substring(3,lookAhead.length()-1));
			this.match("RP())");
		}
	}
}

import java.util.ArrayList;

public class CodeGeneration {
	TreeNode<String> programNode;
	String splitString;
	public static int register=-1;
	public static int block = 0;
	ArrayList<String> registerList = new ArrayList<String>();
	public CodeGeneration(TreeNode<String> programNode,String fileName){
		this.programNode=programNode;
		new Instruction("","B"+ ++block,"","");
		for(TreeNode<String> n: programNode.children.get(0).children){
			splitString=n.data.split(":")[1].substring(1, n.data.split(":")[1].length()-1);
			new Instruction("loadI","0","","r_"+ splitString);
			registerList.add("r_"+ splitString);
		}
		statementNode(programNode.children.get(1));
		
		new Instruction("exit","","","");
		new Instruction(fileName);
	}
	
	public void statementNode(TreeNode<String> statement){
		for(TreeNode<String> n: statement.children){
			switch(n.data){
			case ":=":
				assignmentStatement(n);
				break;
			case "while":
				new Instruction("jumpl","" ,"","B"+ ++block);
				new Instruction("","B"+ block++,"","");
				whileStatement(n);
				break;
			case "if":
				ifStatement(n);
				break;
			case "writeint":
				writeInt(n);
				break;
			}
		} 
	}
	public void assignmentStatement(TreeNode<String> assignmentNode){
		if(assignmentNode.children.get(1).data.startsWith("readint")){
			readint(assignmentNode.children.get(1));
		}
		else if(assignmentNode.children.get(1).data.startsWith("+") || assignmentNode.children.get(1).data.startsWith("-") || assignmentNode.children.get(1).data.startsWith("*") || assignmentNode.children.get(1).data.startsWith("div") || assignmentNode.children.get(1).data.startsWith("mod") ){
			new Instruction("i2i",binary(assignmentNode.children.get(1)),"","r_"+assignmentNode.children.get(0).data.split(":")[0]);
		}else if(assignmentNode.children.get(1).data.split(":")[0].matches("^[1-9][0-9]*")||assignmentNode.children.get(1).data.startsWith("0")){
			new Instruction("loadI",assignmentNode.children.get(1).data.split(":")[0],"","r_"+ ++register);
			new Instruction("i2i","r_"+ register ,"","r_"+ assignmentNode.children.get(0).data.split(":")[0]);
		}else{
			//new Instruction("loadI",assignmentNode.children.get(1).data.split(":")[0],"","r_"+ ++register);
			new Instruction("i2i","r_"+ assignmentNode.children.get(1).data.split(":")[0] ,"","r_"+ assignmentNode.children.get(0).data.split(":")[0]);
		}

	}
	
	public void readint(TreeNode<String> readNode){
		new Instruction("readint","","","r_"+readNode.parent.children.get(0).data.split(":")[0]);
		
	}
	
	public String binary(TreeNode<String> binaryNode){
		register++;
		if(binaryNode.children.get(0).data.split(":")[0].matches("^[1-9][0-9]*") ){
			new Instruction("loadI",binaryNode.children.get(0).data.split(":")[0],"","r_"+register);
		}
		else if(binaryNode.children.get(1).data.split(":")[0].matches("^[1-9][0-9]*") ){
			new Instruction("loadI",binaryNode.children.get(1).data.split(":")[0],"","r_"+register);
		}
		
		if(binaryNode.data.startsWith("+")){
			if(binaryNode.children.get(0).data.split(":")[0].matches("^[1-9][0-9]*")){
				new Instruction("addu","r_"+register++,"r_"+binaryNode.children.get(1).data.split(":")[0],"r_"+register);
			}
			else if(binaryNode.children.get(1).data.split(":")[0].matches("^[1-9][0-9]*")){
				new Instruction("addu","r_"+binaryNode.children.get(0).data.split(":")[0],"r_"+register++,"r_"+register);
			}
			else{
				new Instruction("addu","r_"+binaryNode.children.get(0).data.split(":")[0],"r_"+binaryNode.children.get(1).data.split(":")[0],"r_"+register);
			}
		}else if(binaryNode.data.startsWith("-")){
			if(binaryNode.children.get(0).data.split(":")[0].matches("^[1-9][0-9]*")){
				new Instruction("subu","r_"+register++,"r_"+binaryNode.children.get(1).data.split(":")[0],"r_"+register);
			}
			else if(binaryNode.children.get(1).data.split(":")[0].matches("^[1-9][0-9]*")){
				new Instruction("subu","r_"+binaryNode.children.get(0).data.split(":")[0],"r_"+register++,"r_"+register);
			}
			else{
				new Instruction("subu","r_"+binaryNode.children.get(0).data.split(":")[0],"r_"+binaryNode.children.get(1).data.split(":")[0],"r_"+register);
			}
		} else if(binaryNode.data.startsWith("*")){
			if(binaryNode.children.get(0).data.split(":")[0].matches("^[1-9][0-9]*")){
				new Instruction("mul","r_"+register++,"r_"+binaryNode.children.get(1).data.split(":")[0],"r_"+register);
			}
			else if(binaryNode.children.get(1).data.split(":")[0].matches("^[1-9][0-9]*")){
				new Instruction("mul","r_"+binaryNode.children.get(0).data.split(":")[0],"r_"+register++,"r_"+register);
			}
			else{
				new Instruction("mul","r_"+binaryNode.children.get(0).data.split(":")[0],"r_"+binaryNode.children.get(1).data.split(":")[0],"r_"+register);
			}
		}
		return "r_"+register;
	}
	
	public void whileStatement(TreeNode<String> whileStatementNode){
		new Instruction("mul","r_"+whileStatementNode.children.get(0).children.get(0).data.split(":")[0],"r_"+whileStatementNode.children.get(0).children.get(1).data.split(":")[0],"r_"+ ++register);
		switch(whileStatementNode.children.get(0).data.split(":")[0]){
			case "<=":
				new Instruction("sle","r_"+ register,"r_"+whileStatementNode.children.get(0).children.get(2).data.split(":")[0],"r_"+ ++register);
				break;
			case ">=":
				new Instruction("sge","r_"+ register,"r_"+whileStatementNode.children.get(0).children.get(2).data.split(":")[0],"r_"+ ++register);
				break;
			case "==":
				new Instruction("seq","r_"+ register,"r_"+whileStatementNode.children.get(0).children.get(2).data.split(":")[0],"r_"+ ++register);
				break;
			case ">":
				new Instruction("sgt","r_"+ register,"r_"+whileStatementNode.children.get(0).children.get(2).data.split(":")[0],"r_"+ ++register);
				break;
			case "<":
				new Instruction("slt","r_"+ register,"r_"+whileStatementNode.children.get(0).children.get(2).data.split(":")[0],"r_"+ ++register);
				break;
			case "!=":
				new Instruction("sne","r_"+ register,"r_"+whileStatementNode.children.get(0).children.get(2).data.split(":")[0],"r_"+ ++register);
				break;
		}
		new Instruction("cbr","r_"+ register,"","B"+ block +",B"+ ++block);
		new Instruction("","B"+ (block-1),"","");
		statementNode(whileStatementNode.children.get(1));
		new Instruction("jumpl","","","B"+(block-2));
		new Instruction("","B"+block,"","");
	}
	
	public void ifStatement(TreeNode ifStatementNode){
		
	}
	
	public void writeInt(TreeNode<String> writeintStatement){
		if(writeintStatement.children.get(0).data.startsWith("+") || writeintStatement.children.get(0).data.startsWith("*")){
			binary(writeintStatement.children.get(0));
			new Instruction("writeint","","","r_"+register);
		}else{
			new Instruction("writeint","","","r_"+writeintStatement.children.get(0).data.split(":")[0]);
		}
	}
}

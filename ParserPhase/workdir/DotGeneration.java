import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class DotGeneration {
	
	public static int numberOfNodes = 0;
	public static int count=0;
	public static int nodeCount=2;
	public static int level=1;
	public static String fileName;
	public DotGeneration(String fileName){
		this.fileName=fileName;
	}
	public void Generator(TreeNode<String> programNode){
		try{
		FileWriter outputfile = new FileWriter(fileName.substring(0,fileName.length()-3) + ".ast.dot");
		BufferedWriter fop = new BufferedWriter(outputfile);
		fop.write("digraph tl12Ast {");
		fop.newLine();
		fop.write("ordering=out;");
		fop.newLine();
		fop.write("node [shape = box, style = filled, fillcolor=\"white\"];");
		fop.newLine();
		fop.write( "n"+ ++numberOfNodes + " [label=\""+ programNode.data + "\",shape=box]");
		fop.newLine();
		while(level<=100){
			for(TreeNode<String> node : programNode){
				for(TreeNode<String> n : node.children){
					if(n.getLevel()==level){
						/*if(n.data.length()>2){
							System.out.println(n.data.substring(0,3));
						}*/
						if(n.data.length()>2 && n.data.substring(0,3).equals("red")){
							//System.out.println("error");
							fop.write("n"+ ++numberOfNodes + " [label=\""+ n.data.substring(3,n.data.length()) + "\",shape=box, fillcolor=\"red\"]");
						}
						else{
						fop.write("n"+ ++numberOfNodes + " [label=\""+ n.data + "\",shape=box]");
						}
						fop.newLine();		
					}
				}			
			}
			level++;
		}
		level=1;
		while(level<=100){
			for(TreeNode<String> node : programNode){
				if(node.getLevel()==level-1 ){
					count++;
					for(TreeNode<String> n : node.children){
						fop.write("n" + count + " -> " + "n" + nodeCount);
						fop.newLine();
						nodeCount++;		
					}
				}		
			}
			level++;
		}
		fop.write("}");
		fop.newLine();
		fop.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
		}
		
	
	
}
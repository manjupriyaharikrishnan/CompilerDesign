import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CfgGeneration {
	public static int nodeNumber=0;
	public CfgGeneration(ArrayList<ArrayList<String>> instructionSet,String fileName){
		try{
			FileWriter outputfile = new FileWriter(fileName.substring(0,fileName.length()-3) + ".3A.cfg.dot");
			BufferedWriter fop = new BufferedWriter(outputfile);
			fop.write("digraph graphviz {");
			fop.newLine();
			fop.write("node [shape = none];");
			fop.newLine();
			fop.write("edge [tailport = s];");
			fop.newLine();
			fop.write("entry");
			fop.newLine();
			fop.write("subgraph cluster {");
			fop.newLine();
			fop.write("color=\"/x11/white\" ");
			//System.out.println(CodeGeneration.block);
			int b=1;
			int insnum=0;
			int know=0;
			ArrayList jump = new ArrayList();
			ArrayList cbr = new ArrayList();
			while(nodeNumber<CodeGeneration.block){
				fop.write("n"+ nodeNumber + " [label=<<table border=\"0\">");
				for(ArrayList<String> n: instructionSet){
					insnum++;
					if(insnum<know){
						continue;
					}
					//fop.write("<tr><td>"+insnum+"</td></tr>");	
					if(n.get(1).startsWith("B"+(b+1))){
						b++;
						know=insnum;
						break;
					}
					if(n.get(1).startsWith("B"+b)){
						fop.write("<tr>");
						fop.write("<td border=\"1\" colspan=\"3\">"+n.get(1)+"</td>");
						fop.write("</tr>");
					}else{
						fop.write("<tr>");
						fop.write("<td align=\"left\">"+n.get(0)+"</td>");
						fop.write("<td align=\"left\">"+n.get(1)+"</td>");
						fop.write("<td align=\"left\">"+n.get(2)+"</td>");
						fop.write("<td align=\"left\"> =&gt; </td>");
						fop.write("<td align=\"left\">"+n.get(3)+"</td>");
						fop.write("</tr>");
					}
					if(n.get(0).equals("jumpl")){
						jump.add(nodeNumber);
					}
					if(n.get(0).equals("cbr")){
						cbr.add(nodeNumber);
					}
				}
				fop.write("</table>>,fillcolor=\"/x11/white\",shape=box]");
				nodeNumber++;
				insnum=0;
			}
			fop.newLine();
			fop.write("}");
			fop.newLine();
			fop.write("entry -> n0"); 
			fop.newLine();
			int count=0;
			int j=0;
			for(ArrayList<String> n: instructionSet){
				if(n.get(0).equals("jumpl") && count<jump.size()){
					fop.write("n" + jump.get(count) +"-> n"+(Integer.parseInt(n.get(3).substring(1,n.get(3).length()))-1));
					count++;
					fop.newLine();
				}
				else if(n.get(0).equals("cbr")&& j<cbr.size()){
					fop.write("n" + cbr.get(j) +"-> n"+ (Integer.parseInt(n.get(3).substring(1,2))-1));
					fop.newLine();
					fop.write("n" + cbr.get(j) +"-> n"+ (Integer.parseInt(n.get(3).substring(4,5))-1));
					j++;
					fop.newLine();
				}
			}
			fop.newLine();
			fop.write("n"+(nodeNumber-1)+"->exit");
			fop.newLine();
			fop.write("}");
			fop.newLine();
			fop.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}

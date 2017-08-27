import java.io.*;
import java.util.*;

public class Lexer {
	public static void main(String args[]){
		
		try{
			String inputFile= args[0];
			String outputFileNameArray = inputFile.substring(0,inputFile.length()-3);
			String outputFileName = outputFileNameArray + ".tok";
			FileWriter outputfile = new FileWriter(outputFileName);
			File file = new File(inputFile);
			FileInputStream fis = new FileInputStream(file);
			BufferedWriter fop = new BufferedWriter(outputfile);
			if (!file.exists()) {
				file.createNewFile();
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String line = null;
			String currentLine = null;
			String[] parts = null;
			int flag=0;
		
			while ((line = br.readLine()) != null) {
				if(!line.equals("")){
					line = line.trim();
					line = line.replaceAll("\t"," ");
					line = line.replaceAll("\\s{2,}"," ");
					
					if(line.contains("%")){
						currentLine = line.substring(0, line.indexOf("%"));
						if(currentLine.equals("")){
							continue;
						}
					}
					else{
						currentLine = line;
					}
					parts = currentLine.split(" ");
					
					for(int i=0;i<parts.length;i++){
						switch(parts[i]){
							case "(":
								fop.write(("LP(" + parts[i] + ")"));
								fop.newLine();
								break;
								
							case ")":
								fop.write("RP(" + parts[i] + ")");
								fop.newLine();
								break;
							
							case ":=":
								fop.write("ASGN(" + parts[i] + ")");
								fop.newLine();
								break;
								
							case ";":
								fop.write("SC(" + parts[i] + ")");
								fop.newLine();
								break;
								
							case "*":
							case "div":
							case "mul":
								fop.write("MULTIPLICATIVE(" + parts[i] + ")");
								fop.newLine();
								break;
								
							case "+":
							case "-":
								fop.write("ADDITIVE(" + parts[i] + ")");
								fop.newLine();
								break;
								
							case "=":
							case "!=":
							case "<":
							case ">":
							case "<=":
							case ">=":
								fop.write("COMPARE(" + parts[i] + ")");
								fop.newLine();
								break;
								
							case "if":
								fop.write("IF");
								fop.newLine();
								break;
							
							case "then":
								fop.write("THEN");
								fop.newLine();
								break;
								
							case "else":
								fop.write("ELSE");
								fop.newLine();
								break;
								
							case "begin":
								fop.write("BEGIN");
								fop.newLine();
								break;
								
							case "end":
								fop.write("END");
								fop.newLine();
								break;
								
							case "while":
								fop.write("WHILE");
								fop.newLine();
								break;
								
							case "do":
								fop.write("DO");
								fop.newLine();
								break;
								
							case "program":
								fop.write("PROGRAM");
								fop.newLine();
								break;
								
							case "var":
								fop.write("VAR");
								fop.newLine();
								break;
								
							case "as":
								fop.write("AS");
								fop.newLine();
								break;
								
							case "int":
								fop.write("INT");
								fop.newLine();
								break;
								
							case "bool":
								fop.write("BOOL");
								fop.newLine();
								break;
								
							case "writeint":
								fop.write("WRITEINT");
								fop.newLine();
								break;
								
							case "readint":
								fop.write("READINT");
								fop.newLine();
								break;
							
							case "false":
							case "true":
								fop.write("boollit(" + parts[i] + ")");
								fop.newLine();
								break;
								
							default:{
								if(parts[i].matches("^[1-9][0-9]*") || parts[i].matches("0")){
									fop.write("num(" + parts[i] + ")");
									fop.newLine();
								}else if(parts[i].matches("[A-Z][A-Z0-9]*")){
									fop.write("iden(" + parts[i] + ")");
									fop.newLine();
								}
								else{
									System.out.println("Lexical error : " + parts[i]);
									flag=1;
								}
							}		
						}
					}
					if(flag==1){
						break;
					}
				}
			}
			br.close();
			fop.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}

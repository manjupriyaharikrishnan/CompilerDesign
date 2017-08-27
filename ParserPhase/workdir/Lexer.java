import java.io.*;
import java.util.*;

public class Lexer {
	public ArrayList Analyser(String inputFile){
		ArrayList tokens = new ArrayList();
		try{
			
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
								tokens.add("LP(" + parts[i] + ")");
								break;
								
							case ")":
								tokens.add("RP(" + parts[i] + ")");
								break;
							
							case ":=":
								tokens.add("ASGN(" + parts[i] + ")");
								break;
								
							case ";":
								tokens.add("SC(" + parts[i] + ")");
								break;
								
							case "*":
							case "div":
							case "mod":
								tokens.add("MULTIPLICATIVE(" + parts[i] + ")");
								break;
								
							case "+":
							case "-":
								tokens.add("ADDITIVE(" + parts[i] + ")");
								break;
								
							case "=":
							case "!=":
							case "<":
							case ">":
							case "<=":
							case ">=":
								tokens.add("COMPARE(" + parts[i] + ")");
								break;
								
							case "if":
								tokens.add("IF");
								break;
							
							case "then":
								tokens.add("THEN");
								break;
								
							case "else":
								tokens.add("ELSE");
								break;
								
							case "begin":
								tokens.add("BEGIN");
								break;
								
							case "end":
								tokens.add("END");
								break;
								
							case "while":
								tokens.add("WHILE");
								break;
								
							case "do":
								tokens.add("DO");
								break;
								
							case "program":
								tokens.add("PROGRAM");
								break;
								
							case "var":
								tokens.add("VAR");
								break;
								
							case "as":
								tokens.add("AS");
								break;
								
							case "int":
								tokens.add("INT");
								break;
								
							case "bool":
								tokens.add("BOOL");
								break;
								
							case "writeint":
								tokens.add("WRITEINT");
								break;
								
							case "readint":
								tokens.add("READINT");
								break;
							
							case "false":
							case "true":
								tokens.add("boollit(" + parts[i] + ")");
								break;
								
							default:{
								if(parts[i].matches("^[1-9][0-9]*") || parts[i].matches("0")){
									tokens.add("num(" + parts[i] + ")");
								}else if(parts[i].matches("[A-Z][A-Z0-9]*")){
									tokens.add("iden(" + parts[i] + ")");
								}
								else{
									System.out.println("Lexical error : " + parts[i]);
									flag=1;
								}
							}	
						}
						if(flag==1){
							break;
						}
					}
				}
			}
			for(int i=0;i<tokens.size();i++){
				fop.write((String)tokens.get(i));
				fop.newLine();
			}
			br.close();
			fop.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
		return tokens;
	}
}

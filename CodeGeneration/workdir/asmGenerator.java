import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

public class asmGenerator {
	private Hashtable<String, Integer> stack= new Hashtable<String, Integer>();
    private Integer stackPointer = 4;
	public asmGenerator(ArrayList<ArrayList<String>> instructionSet,String fileName){
		try{
			FileWriter outputfile = new FileWriter(fileName.substring(0,fileName.length()-3) + ".s");
			BufferedWriter fop = new BufferedWriter(outputfile);
			fop.write("\t.data");
			fop.newLine();
			fop.write("newline: .asciiz \"\\n\"");
			fop.newLine();
			fop.write("\t.text");
			fop.newLine();
			fop.write("\t.globl main");
			fop.newLine();
			fop.write("main:");
			fop.newLine();
			fop.write("\tli $fp, 0x7ffffffc");
			fop.newLine();
			
			for(ArrayList<String> n: instructionSet){
				fop.write("\t# "+ n.get(0)+ " " + n.get(1) + " " + n.get(2) + " " + n.get(3) );
				fop.newLine();
				if(n.get(1).startsWith("B")){
					fop.write(n.get(1)+":");
					fop.newLine();
				}
				else if(n.get(0).equals("loadI")){
					fop.write("\tli $t0, "+n.get(1));
					fop.newLine();
					fop.write("\tsw $t0, "+ stackCheck(n.get(3)) +"($fp)");
					fop.newLine();
				}
				else if(n.get(0).equals("readint")){
					fop.write("\tli $v0, 5");
					fop.newLine();
					fop.write("\tsyscall");
					fop.newLine();
					fop.write("\tadd $t0, $v0, $zero");
					fop.newLine();
					fop.write("\tsw $t0, "+ stackCheck(n.get(3)) +"($fp)");
					fop.newLine();
				}
				else if(!n.get(0).isEmpty() && !n.get(1).isEmpty() && !n.get(2).isEmpty() && !n.get(3).isEmpty()){
					fop.write("\tlw $t1,"+ stackCheck(n.get(1)) +"($fp)");
					fop.newLine();
					fop.write("\tlw $t2,"+ stackCheck(n.get(2)) +"($fp)");
					fop.newLine();
					fop.write("\t"+n.get(0)+" $t0, $t1, $t2");
					fop.newLine();
					fop.write("\tsw $t0, "+ stackCheck(n.get(3)) +"($fp)");
					fop.newLine();
				}
				else if(n.get(0).equals("exit")){
					fop.write("\tli $v0, 10");
					fop.newLine();
					fop.write("\tsyscall");
					fop.newLine();
				}
				else if(n.get(0).equals("writeint")){
					fop.write("\tli $v0, 1");
					fop.newLine();
					fop.write("\tlw $t1,"+ stackCheck(n.get(3)) + "($fp)");
					fop.newLine();
					fop.write("\tadd $a0, $t1, $zero");
					fop.newLine();
					fop.write("\tsyscall");
					fop.newLine();
					fop.write("\tli $v0, 4");
					fop.newLine();
					fop.write("\tlw $a0, newline");
					fop.newLine();
					fop.write("\tsyscall");
					fop.newLine();
				}
				else if(n.get(0).equals("i2i")){
					fop.write("\tlw $t1,"+ stackCheck(n.get(1)) + "($fp)");
					fop.newLine();
					fop.write("\tadd $t0, $t1, $zero");
					fop.newLine();
					fop.write("\tsw $t0, "+ stackCheck(n.get(3)) + "($fp)");
					fop.newLine();
				}
				else if(n.get(0).equals("jumpl")){
					fop.write("\tj "+ n.get(3));
					fop.newLine();
				}
				else if(n.get(0).equals("cbr")){
					fop.write("\tlw $t0,"+ stackCheck(n.get(1)) + "($fp)");
					fop.newLine();
					fop.write("\tbne $t0, $zero "+ n.get(3).split(",")[0]);
					fop.newLine();
					fop.write("L1:");
					fop.newLine();
					fop.write("\tj "+ n.get(3).split(",")[1]);
					fop.newLine();
				}
				fop.newLine();
			}
			
			
			
			fop.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int stackCheck(String register){
		if(stack.containsKey(register)){
			return stack.get(register);
		}
		else{
			stackPointer-=4;
			stack.put(register,stackPointer);
			return stackPointer;
		}
	}
}

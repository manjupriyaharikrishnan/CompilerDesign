import java.util.ArrayList;

public class Instruction {
	static ArrayList<ArrayList<String>> instructionSet = new ArrayList<ArrayList<String>>();
	
	public Instruction(String value1, String value2, String value3, String value4){
		ArrayList<String> instruction = new ArrayList<String>();
		instruction.add(value1);
		instruction.add(value2);
		instruction.add(value3);
		instruction.add(value4);
		instructionSet.add(instruction);
	}
	public Instruction(String fileName){
		for(ArrayList<String> n: instructionSet){
			System.out.println(n);
		}
		new CfgGeneration(instructionSet,fileName);
		new asmGenerator(instructionSet,fileName);
	}
}


public class TypeCheck {
	
	public TreeNode<String> checking(TreeNode<String> programNode){
		for(TreeNode<String> node : programNode){
			if(node.data.startsWith("+") || node.data.startsWith("*") || node.data.startsWith("-") || node.data.startsWith("div") || node.data.startsWith("mod") || node.data.startsWith(":=")){
				if(node.children.get(0).data.contains(":") && node.children.get(1).data.contains(":")){
					String[] value1= node.children.get(0).data.split(":");
					String[] value2= node.children.get(1).data.split(":");
				//System.out.println(value1);
				//System.out.println(value2);
					if(!value1[1].equals(value2[1])){
						node.data="red"+node.data;
						//System.out.println("yes");
					}
				}
			}
		}
		return programNode;
	}
}

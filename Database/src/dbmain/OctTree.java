package dbmain;

public class OctTree {
    private Node root;
    

    public OctTree() {
        this.root = new Node();
        this.root.newParent();
        
    }
    
    public OctTree(Node parent)//nezabat el list bta3et el node ely da5lalna enhom ykono distributed 3la children + ely da5el gded
    {
    	this.root = parent;
    	this.root.newParent();
    }

    public void insert(double x, double y, double z) {
        root.insert(x, y, z);
    }
}
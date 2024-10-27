
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;



/**
 *
 * @author abby gamboa
 */
public class lab6 {
    public static int v; 
    public static graphs g;
    
    public static void readFile(File read){//reads a given file 
        try{
            String line = "";
            try (Scanner reader = new Scanner(read)) {
                if (reader.hasNextLine()){
                    line = reader.nextLine();/* reads first line which indicates
                    vertices, edges and directed properties*/
                }
                //System.out.println(line);
                String[] vals = line.split(" ");
                v = Integer.parseInt(vals[0]);//first value in string (nvertices)
                g = new graphs(v);
                g.directed = true;
                
                keepReading(g, reader);
            }
        } catch (FileNotFoundException e){
        }
    }
    
    public static void keepReading(graphs n, Scanner reader){
        String next;
        while (reader.hasNextLine()){/*while there is more lines in a given file, 
            read the line (each of which will be an edge)*/
            next = reader.nextLine();
            
            String[] vals = next.split(" ");
            int one = Integer.parseInt(vals[0]);
            int two = Integer.parseInt(vals[1]);
            n.addEdge(one, two);//adds second value to edges 
            System.out.println(one + " " + two);
        }
            
    }
    
    public static void display(){
        System.out.println("Topological Ordering(s): ");

        g.DFS();
        
        System.out.println("Edge Classifications");
        edgeTypes();
        
    }
    
    public static void edgeTypes(){
        System.out.print("\t Back edges: ");
        if (g.backCount == 0){
            System.out.println("None");
        }else{
            for (int i = 0; i< g.backEdges.size(); i+=2){
                System.out.print(g.backEdges.get(i) + " " + g.backEdges.get(i));
                System.out.print("; ");
            }
            System.out.print("\b");
        }
        
        System.out.print("\t Forward edges: ");
        if (g.forwardCount == 0){
            System.out.println("None");
        }else{
            for (int i = 0; i< g.forwardEdges.size(); i+=2){
                System.out.print(g.forwardEdges.get(i) + " " + g.forwardEdges.get(i));
                System.out.print("; ");
            }
            System.out.print("\b");
        }
        
        System.out.print("\t Cross edges: ");
        if (g.crossCount == 0){
            System.out.println("None");
        }else{
            for (int i = 0; i< g.crossEdges.size(); i+=2){
                System.out.print(g.crossEdges.get(i) + " " + g.crossEdges.get(i+1));
                System.out.print("; ");
            }
            System.out.print("\b\b");
        }
                
    }

    public static void main(String[] args) {
        File read = new File(args[0]);
        readFile(read);
        
        
        display();
        System.out.println(Arrays.toString(g.entryTime));
        System.out.println(Arrays.toString(g.exitTime));

    }
}

class graphs{
    int nvertices; 
    int nedges;
    boolean directed;
    List<Integer>[] edges;
    
    ArrayList<Integer> backEdges = new ArrayList<>();
    ArrayList<Integer> forwardEdges = new ArrayList<>();
    ArrayList<Integer> crossEdges = new ArrayList<>();
   
    
    int[] parent;
    boolean[] discovered;
    boolean[] processed;
    int[] degree;
    int[] entryTime;
    int[] exitTime;
    int intime;
    
    int TREE = 1;
    int BACK = 2;
    int FORWARD = 3;
    int CROSS = 4;
    
    int backCount;
    int forwardCount;
    int crossCount;
    
    public graphs(int nvertices){
        this.nvertices = nvertices;
        this.edges = new LinkedList[nvertices];
        discovered = new boolean[nvertices];
        for (int i = 0; i < nvertices; i++) {
            edges[i] = new LinkedList<>();
        }
    }
    
    public void addEdge(int x, int y) {
        edges[x].add(y);
        if(!directed){
            edges[y].add(x);
        }
    }
    
    void allTopologicalSortsUtil(Stack<Integer> stack) {
        boolean flag = false;
 
        for (int i = 0; i < nvertices; i++) {
            
            if (!discovered[i] && degree[i] == 0) {

                discovered[i] = true;
                stack.add(i);
                for (int adjacent : edges[i]) {
                    degree[adjacent]--;
                }
                allTopologicalSortsUtil(stack);
                 
                discovered[i] = false;
                stack.remove(stack.size() - 1);
                for (int adjacent : edges[i]) {
                    degree[adjacent]++;
                }
 
                flag = true;
            }
        }

        if (!flag) {
            stack.forEach(i -> System.out.print(i + " "));
            System.out.println();
        }
 
    }
     
     
    void DFS()
    {
        //discovered = new boolean[nvertices];
        parent = new int[nvertices];
        entryTime = new int[nvertices];
        processed = new boolean[nvertices];
        exitTime = new int[nvertices];
        degree = new int[nvertices];
        intime = 0;
        
        Stack<Integer> sort = new Stack<>();
        //DFSUtil(5);
        topSort();

        
        if (backCount == 0) 
        {
            for(int i = 0; i< nvertices ;i++){
                for (int var : edges[i]) {
                    degree[var]++;
                }
            }
            discovered = new boolean[nvertices];
            allTopologicalSortsUtil(sort);
        }
        
        
    }
    
    void DFSUtil(int v){
        
        discovered[v] = true;
        intime = intime +1;
        entryTime[v] = intime;
        
        //Stack<Integer> stack = new Stack<>();
        //stack.push(v);
        
        Iterator<Integer> i = edges[v].listIterator();
        while (i.hasNext()) {
            int n = i.next();
            //System.out.println(v + " " + n);
            if (!discovered[n]){
                parent[n] = v;
                //discovered[n] = true;
                processEdge(v,n);
                DFSUtil(n);  
            }
            
            else if ((!processed[n]) && (parent[v] != n) || directed){
                //processEdge(v,n);
            }
        } 
        //stack.push(v);
        intime = intime + 1;
        exitTime[v] = intime;
        processed[v] = true;
    }
    
    void topSort(){
        Stack<Integer> sort = new Stack<>();
        for(int i = 0; i<nvertices; i++){
            if (!discovered[i]){
                DFSUtil(i);
            }
        }
        printStack(sort);
    }

    
    void processEdge(int x, int y){
        System.out.printf(" ", x, y);
        int eclass;
        eclass = edgeClass(x,y);
        if (eclass == BACK){
            System.out.printf("Warning: directed cycle found, not a DAG\n");
        }
        
    }
    
    int edgeClass(int x, int y) {
        if (parent[y] == x) {
            return(TREE);
        }
        if (discovered[y] && !processed[y]) {
            backEdges.add(x); 
            backEdges.add(y);
            backCount++;
            return(BACK);
        }
        if (processed[y] && (entryTime[y]>entryTime[x])) {
            forwardEdges.add(x); 
            forwardEdges.add(y);
            forwardCount++;
            return(FORWARD);
        }
        
        if (processed[y] && (entryTime[y]< entryTime[x])) {
            //crossEdges.add(x); 
            //crossEdges.add(y);
            //crossCount++;
            return(CROSS);
        }
        System.out.printf("Warning: self loop (%d,%d)\n", x, y);
        return -1;
    }
    
    //void findSimilar(){
        //for (int i = 0; i<edges[4].size();i++){
            //if (edges[4].get(i) == ){
                
            //}
        //}
    //}
    
    private void printStack(Stack<Integer> stack) {
        while (!stack.isEmpty()) {
            System.out.print(stack.pop() + " ");
        }
        System.out.println();
    }
    //try after run if 5 and 4 have a similar vertice then cross edge from 4 to that vertice
} 
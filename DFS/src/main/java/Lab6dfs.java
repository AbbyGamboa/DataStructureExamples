
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author abbygamboa
 */
public class Lab6dfs {
    
    public static int v; 
    public static hparg graph;
    
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
                graph = new hparg(v);
                graph.directed = true;
                
                keepReading(graph, reader);
            }
        } catch (FileNotFoundException e){
        }
    }
    
    public static void keepReading(hparg n, Scanner reader){
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
    
    public static void main(String[] args) {
        File read = new File(args[0]);
        readFile(read);
        graph.allTopologicalSorts();
        //graph.DFS();
        System.out.println();
        System.out.println(Arrays.toString(graph.discovered));
        System.out.println(Arrays.toString(graph.enterT));
        System.out.println(Arrays.toString(graph.exitT));
        System.out.println(Arrays.toString(graph.processed));
    }
}

class hparg{
    int nvertices; 
    int nedges;
    boolean directed;
    List<Integer>[] edges;
    
    //for dfs:
    boolean[] processed;
    boolean[] discovered;
    int[] parent;
    
    int time;
    int[] exitT;
    int[] enterT;
    
    //test for top
    int[] inDegree;
    
    public hparg(int nvertices){
        this.nvertices = nvertices;
        this.edges = new LinkedList[nvertices];
        discovered = new boolean[nvertices];
        inDegree = new int[nvertices];
        for (int i = 0; i < nvertices; i++) {
            edges[i] = new LinkedList<>();
        }
    }
    
    void addEdge(int x, int y){
        edges[x].add(y);
        if (!directed){
            edges[y].add(x);
        }
    }
    
    void dfsWork(int v, Stack<Integer> stack)
    {
        //boolean flag = false;
        // Mark the current node as visited
        discovered[v] = true;
        stack.push(v);
        time += 1;
        //if first item in... 
        enterT[v] = time;
 
        // Recur for all adjacent vertices
        Iterator<Integer> i = edges[v].listIterator();
        
        /*while the iterator has more items (item order will be based on 
        insertion order from reading the .txt file) */
        while (i.hasNext()){
            //get instance, so a vertice with an edge connected to start
           
            int n = i.next();
            
            //for testing purposes
            //System.out.print(n + " "); 
            
            //if n not in discovered state
            if (!discovered[n]){
                //make start the parent of n;
                parent[n] = v;
                //System.out.print(n + " ");
                //replace processedEdge()
                processEdge(v, n);
                
                //look for the vertices connected to n (a recursive call)
                dfsWork(n, stack);
                
            }
            
            /*now what about the backedge?
            if n is not processed, so if n has more edges to account for and if 
            the parent of start is not n there is a backedge OR if the graph is 
            directed each edge needs to be processed
            */
            else if ((!processed[n]) && (parent[v] != n) || (directed)){
                //System.out.print(start + " ");
                processEdge(v, n);
            }
            
            
        }

        //flag = true;
        time += 1;
        //...must be last item out
        exitT[v] = time;
        /* once all vertices connected to start are accounted for, start is done
        and is now in the processed state*/
        processed[v] = true;
        
        //stack.push(v);
        //flag = true;
        // Push current vertex to stack which stores the
        // result
        //stack.push(v);
    }
    
    void processEdge(int x, int y){
        int eclass = edgeClass(x,y);

        if (eclass == BACK){
            //if there is back edge there cannot be a DAG
            System.out.println("Error: directed cycle found, not a DAG");
            System.exit(0);
        }
    }
 
    // Function to perform Topological Sort
    void DFS(){
        // Stack to store the result
        //Stack<Integer> stack = new Stack<>();
        discovered = new boolean[nvertices];
        processed = new boolean[nvertices];
        parent = new int[nvertices];
        enterT = new int[nvertices];
        exitT = new int[nvertices];
        inDegree = new int[nvertices];
        time = 0;
        
        
        for (int i = 0; i < nvertices; i++) {
 
            for (int var : edges[i]) {
                inDegree[var]++;
            }
        }
        
        // Call the recursive helper function to store
        // Topological Sort starting from all vertices one
        // by one
        Stack<Integer> stack = new Stack<>();
        topSort(stack);
        
    }
    
    void topSort(Stack<Integer> sort){

        //Stack<Integer> sort = new Stack<>();
        boolean flag = false;
        //checks every vertice so in this case 0-5
        for(int i = 0; i< nvertices; i++){
            //if the vertice is not discovered run dfs on that vertice
            if (!discovered[i] && (inDegree[i] == 0)){
                dfsWork(i,sort);
                
                topSort(sort);
                discovered[i] = false;
                sort.pop();

                for (int adjacent : edges[i]) {
                    inDegree[adjacent]++;
                }
                flag = true;
            } 
        }
        if (!flag) {
            sort.forEach(i -> System.out.print(i + " "));
            System.out.println();
        }

        
    }
    
    private void allTopologicalSortsUtil(Stack<Integer> stack) {
        // To indicate whether all topological are found
        // or not
        boolean flag = false;
 
        for (int i = 0; i < nvertices; i++) {
            // If indegree is 0 and not yet visited then
            // only choose that vertex
            if (!discovered[i] && inDegree[i] == 0) {
                 
                // including in result
                discovered[i] = true;
                stack.push(i);
                for (int adjacent : edges[i]) {
                    inDegree[adjacent]--;
                }
                allTopologicalSortsUtil(stack);
                 
                // resetting visited, res and indegree for
                // backtracking
                discovered[i] = false;
                if (stack.empty() == false){
                    stack.pop();
                }
                
                for (int adjacent : edges[i]) {
                    inDegree[adjacent]++;
                }
 
                flag = true;
            }
        }
        // We reach here if all vertices are visited.
        // So we print the solution here
        if (!flag) {
            
            stack.forEach(i -> System.out.print(i + " "));
            System.out.println();
        }
 
    }
     
    // The function does all Topological Sort.
    // It uses recursive alltopologicalSortUtil()
    public void allTopologicalSorts() {
        // Mark all the vertices as not visited
        discovered = new boolean[nvertices];
 
        inDegree = new int[nvertices];
 
        for (int i = 0; i < nvertices; i++) {
 
            for (int var : edges[i]) {
                inDegree[var]++;
            }
        }
 
        Stack<Integer> stack = new Stack<>();
 
        allTopologicalSortsUtil(stack);
    }
    
    void printStack(Stack<Integer> stack) {
        //while the stack has values pop out values 
        while (!stack.isEmpty()) {
            System.out.print(stack.pop() + " ");
        }
        System.out.println();
    }
    
    int TREE = 1;
    int BACK = 2;
    int FORWARD = 3;
    int CROSS = 4;
    
    int backCount = 0;
    int forwardCount = 0;
    int crossCount = 0;
    
    List<Integer> backEdges = new LinkedList<>();
    List<Integer> forwardEdges = new LinkedList<>();
    List<Integer> crossEdges = new LinkedList<>();
    
    int edgeClass(int x, int y){
        //normal tree edge
        if (parent[y] == x){
            return TREE;
        }
        
        /*if y is discovered but y is not processed, there is a back edge. Why?
        B/c backedges occur when there is an edge between a vertice "further" 
        down connected to a vertice from a "higher" level. y in this instance
        would be discovered already but not have been processed as all edges 
        adjacent to y are not accounted for. */
        if (discovered[y] && !processed[y]){
            backEdges.add(x);
            backEdges.add(y);
            backCount++;
            return BACK;
        }
        
        /* if y is processed, meaning all descendents are accounted for, and the 
        entry time of y is greater then entery time of x there is a forward 
        edge. WHY? B/c a forward edge occurs when there is an edge from a vertice
        with may decsendents to a vertice with few descendents. SO.. since y is 
        processed, this means all its descendents were discovered and if the 
        entry time of y is greater then x that means x was insterted prior to x. 
        Since y came after x and there is now a forward edge as x -> y.
        */
        if (processed[y] && (enterT[y] > enterT[x])){
            forwardEdges.add(x);
            forwardEdges.add(y);
            forwardCount++;
            return FORWARD;
        }
        
        /*if y is processed and the entry time of y is greater then x, there is 
        a cross edge. WHY? A cross edge occurs there is an edge between two vertices
        of the same parent OR between two vertices in which one is a descendent 
        of the others parent. SO.. since y is processed, this means all its 
        descendents are discovered and if the entry time of y is less then that 
        of x if means that y was inserted prior to x. Mentally think of this as 
        if y is closer to the root while x is further from the root. */
        if (processed[y] && (enterT[y] < enterT[x])){
            crossEdges.add(x);
            crossEdges.add(y);
            crossCount++;
            return CROSS;
        }
        
        return -1;
    }
}
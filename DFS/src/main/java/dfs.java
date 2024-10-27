
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
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
public class dfs {
    
    public static int v; 
    public static gaph graph;
    
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
                graph = new gaph(v);
                graph.directed = true;
                
                keepReading(graph, reader);
            }
        } catch (FileNotFoundException e){
        }
    }
    
    public static void keepReading(gaph n, Scanner reader){
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
        System.out.println("Edge Classifications: ");
        System.out.print("\t Back Edges: ");
        if (graph.backCount == 0) System.out.print("None");
        else{
            for (int i = 0; i< graph.backEdges.size(); i+=2){
                System.out.print(graph.backEdges.get(i) + " " + graph.backEdges.get(i+1));
                System.out.print(", ");
            }
            System.out.print("\b\b");
        }
        System.out.println();
        
        System.out.print("\t Forward Edges: ");
        if (graph.forwardCount == 0) System.out.print("None");
        else{
            for (int i = 0; i< graph.forwardEdges.size(); i+=2){
                System.out.print(graph.forwardEdges.get(i) + " " + graph.forwardEdges.get(i+1));
                System.out.print(", ");
            }
            System.out.print("\b\b");
        }
        System.out.println();
        
        System.out.print("\t Cross Edges: ");
        if (graph.crossCount == 0) System.out.print("None");
        else{
            for (int i = 0; i< graph.crossEdges.size(); i+=2){
                System.out.print(graph.crossEdges.get(i) + " " + graph.crossEdges.get(i+1));
                System.out.print(", ");
            }
            System.out.print("\b\b");
        }
    }
    
    public static void main(String[] args) {
        File read = new File(args[0]);
        readFile(read);
        //graph.setIn();
        //System.out.println(Arrays.toString(graph.inDegree));
        
        //for (int i =0; i<graph.nvertices; i++){
            //graph.dfs(i);
            //System.out.println();
        //}
        
       
        //System.out.println("Prints all topologial sorts: ");
        //for (int i =0 ; i<graph.nvertices; i++){
            //Stack<Integer> sta = new Stack<>();
            //graph.dfs(i, sta);
        //}
        graph.dfs();
        
        /*for(int i =0; i<graph.nvertices; i++){
            //graph.dfs(i);
            System.out.println(Arrays.toString(graph.parent));
            System.out.println(Arrays.toString(graph.edges));
            System.out.println();
        }*/
        //System.out.println("Printed singular topological sort: ");
        display();

       

    }
}


class gaph{
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
    
    //Stack<Integer> alltopStack = new Stack<>();
    
    public gaph(int nvertices){
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
    
    
    void printStack(Stack<Integer> stack) {
        //while the stack has values pop out values 
        while (!stack.isEmpty()) {
            System.out.print(stack.pop() + " ");
        }
        System.out.println();
    }
    
    void dfs(){
        //must intialize all arrays prior to depthFirst
        Stack<Integer> stack = new Stack<>();
                
        discovered = new boolean[nvertices];
        processed = new boolean[nvertices];
        parent = new int[nvertices];
        enterT = new int[nvertices];
        exitT = new int[nvertices];
        time = 0;
        int[] indegree = new int[nvertices];
        
        for(int i = 0; i<nvertices; i++){
            if (!discovered[i]){
                depthSecond(i, stack);
            }
            for(int var: edges[i]){
                indegree[var] ++;
            }
        }
        
        System.out.println("Topological sorting fo the graph: ");
        while(!stack.empty()){
            System.out.print(stack.pop() + " ");
        }

    }
    

    /*this will print out a single topological sort. The question is how do we 
    print out all topological sorts? */
    void topSort(){
        
        Stack<Integer> sort = new Stack<>();
        //checks every vertice so in this case 0-5
        for(int i = 0; i< nvertices; i++){
            //if the vertice is not discovered run dfs on that vertice
            if (!discovered[i]){
                //dfs(i, sort);
                //sort.add(i);
            }
            //recursive so last vertice to discovered will be first in the sort
            
        }

        //since stacks are LIFO, corrects the order of the sort to be accurate.
        printStack(sort);
    }
    
    void depthSecond(int start, Stack<Integer> stack){
        
        //state of root is to be discovered
        discovered[start] = true;
        time += 1;
        //if first item in... 
        enterT[start] = time;
        
        /*sets i to a list of elements to iterator over, the list will be values
        adjacent to the start value. */
        Iterator<Integer> i = edges[start].listIterator();
        
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
                parent[n] = start;
                //System.out.print(n + " ");
                //replace processedEdge()
                processEdge(start, n);
                //look for the vertices connected to n (a recursive call)
                depthSecond(n, stack);
            }
            
            /*now what about the backedge?
            if n is not processed, so if n has more edges to account for and if 
            the parent of start is not n there is a backedge OR if the graph is 
            directed each edge needs to be processed
            */
            else if ((!processed[n]) && (parent[start] != n) || (directed)){
                //System.out.print(start + " ");
                processEdge(start, n);
            }
            
        }
        //stack.push(start);

        time += 1;
        //...must be last item out
        exitT[start] = time;
        /*once all vertices connected to start are accounted for, start is done
        and is now in the processed state*/
        processed[start] = true;
        stack.push(start);
    }
    
    /*so we need to randomly choose a vertice that has not been visited and has 
    "indegree" of 0, meaning it can have edges going out but not coming in. we
    have to pretend we are taking that vertice out and putting it on a stack, 
    while erasing it from the equation, so now ever adjacent vertice has an 
    indegree -= 1. add that vertice to the result, then call recursive method.
    */
    
    /*now consider how to use our dfs to do this. What does dfs do? it takes a 
    single line from an ancestor to a leaf, then goes back up until the ancestor
    is either fully processed or has another descendent. */
    
    /*1) Create a graph with n vertices and m-directed edges.
2)Initialize a stack and a visited array of size n.
3)For each unvisited vertex in the graph, do the following:
        Call the DFS function with the vertex as the parameter.
        In the DFS function, mark the vertex as visited and recursively call the DFS function for all unvisited neighbors of the vertex.
        Once all the neighbors have been visited, push the vertex onto the stack.
4)After all, vertices have been visited, pop elements from the stack and append them to the output list until the stack is empty.
5)The resulting list is the topologically sorted order of the graph.*/
    
    void allTopologicalSorts() {
        // Mark all the vertices as not visited
        boolean[] visited = new boolean[nvertices];
 
        int[] indegree = new int[nvertices];
 
        for (int i = 0; i < nvertices; i++) {
 
            for (int var : edges[i]) {
                indegree[var]++;
            }
        }
 
        Stack<Integer> stack = new Stack<>();
 
        allTopUtil(visited, indegree, stack);
    }
    
    void allTopUtil(boolean[] visited, int[] indegree, Stack<Integer> stack) {
        // To indicate whether all topological are found
        // or not
        boolean flag = false;
 
        for (int i = 0; i < nvertices; i++) {
            // If indegree is 0 and not yet visited then
            // only choose that vertex
            if (!visited[i] && indegree[i] == 0) {
                 
                // including in result
                visited[i] = true;
                stack.push(i);
                for (int adjacent : edges[i]) {
                    indegree[adjacent]--;
                }
                allTopUtil(visited, indegree, stack);
                 
                // resetting visited, res and indegree for
                // backtracking
                visited[i] = false;
                stack.remove(stack.size() - 1);
                for (int adjacent : edges[i]) {
                    indegree[adjacent]++;
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
    
    
    void processEdge(int x, int y){
        int eclass = edgeClass(x,y);

        System.out.printf("processed edge (%d,%d)\n", x, y);

        if (eclass == BACK){
            //if there is back edge there cannot be a DAG
            System.out.println("Error: directed cycle found, not a DAG");
            System.exit(0);
        }
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

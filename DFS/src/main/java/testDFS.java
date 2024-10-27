/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

/**
 *
 * @author abbygamboa
 */
public class testDFS {
    
    public static int v;
    
    public static Graph g;
    
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
                g = new Graph(v);
                
                keepReading(g, reader);
            }
        } catch (FileNotFoundException e){
        }
    }
    
    public static void keepReading(Graph n, Scanner reader){
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
         
        System.out.println(Arrays.toString(g.edges));
        g.topSort();
    }
}



class Graph {
    List<Integer> edges[];
    int degree[];
    int nvertices; /* number of vertices in the graph */
    int nedges; /* number of edges in the graph */
    int directed; /* is the graph directed? */

    boolean[] discovered;
    boolean[] processed;
    int[] entryTime = new int[nvertices];
    int[] exitTime = new int[nvertices];
    int[] parent = new int[nvertices];
    int time;
    boolean finished;
    int TREE = 0;
    int BACK = 1;
    int FORWARD = 2;
    int CROSS = 3;
    
    public Graph(int maxv) {
        this.nvertices = maxv;
        edges = new LinkedList[maxv];
        discovered = new boolean[maxv];
        for (int i = 0; i < edges.length; i++) {
            edges[i] = new LinkedList<>();
        }
    }
    
    void addEdge(int v, int w) { 
        edges[v].add(w);//adding an edge to the adjacency list (edges are bidirectional in this example)
    } 
    
    void dfs(int start){
        processed = new boolean[nvertices];
        discovered = new boolean[nvertices];
        entryTime = new int[nvertices];
        exitTime = new int[nvertices];
        parent = new int[nvertices];
        
        System.out.println(discovered.length);

        Stack<Integer> stack = new Stack<>(); 
        
        stack.push(start);
        int a = 0;
        parent[start] = -1;
        time = time + 1;
        entryTime[start] = time;

        while(!stack.empty()){ 
            start = stack.peek();
            stack.pop();

            if(discovered[start] == false){ 
                parent[start] = start;
                System.out.print(start + " "); 
                discovered[start] = true; 
            } 

            for (int i = 0; i < edges[start].size(); i++){
                a = edges[start].get(i);
                if (!discovered[a]){
                    stack.push(a); 
                }
            }  

        } 
        time = time+1;
        exitTime[start] = time;
        processed[start] = true;
    }

    void DFSUtil(int vertex)
    {

        discovered[vertex] = true;//mark the node as explored
        System.out.print(vertex + " ");
        int a = 0;
 
        for (int i = 0; i < edges[vertex].size(); i++)  //iterate through the linked list and then propagate to the next few nodes
        {
            a = edges[vertex].get(i);
            if (!discovered[a])//only propagate to next nodes which haven't been explored
            {
                DFSUtil(a);
            }
        }  
    }

    void DFS(int v){
        DFSUtil(v);
    }
    
    void topSort(){
        Stack<Integer> st = new Stack<>();
        for(int i = 0; i < nvertices; i++){
            if (!discovered[i]){
                DFS(i);
            }
        }
        printStack(st);
    }
    
    void printStack(Stack<Integer> stac){
        while (!stac.isEmpty()) {
            System.out.print(stac.pop() + " ");
        }
        System.out.println();
    }
    
    void processEdge(int v, int y) {
        int eclass;
        
        eclass = edgeClass(v,y);
        
        if (eclass == BACK){
            System.out.printf("Warning: directed cycle found, not a DAG \n");
        }
    }
    
    int edgeClass(int x, int y){
        if (parent[y] == x){
            return (TREE);
        }
        if (discovered[y] && !processed[y]){
            return (BACK);
            
        }
        
        if (processed[y] && (entryTime[y] < entryTime[x])){
            return (FORWARD);
        }
        
        if (processed[y] && (entryTime[y] < entryTime[x])){
            return (CROSS);
        }
        return -1;
    }
}

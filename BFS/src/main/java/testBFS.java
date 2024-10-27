
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt 
to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit 
this template
 */

/**
 * I certify that the codes/answers of this assignment are entirely my own work.
 * @author abbygamboa
 */
public class testBFS {
    public static int v; // number of vertices 
    public static int direct; // has direction in number 1 = defined, 0 = undefined
    public static LinkedList<Integer> edgen = new LinkedList<>();
    
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
                direct = Integer.parseInt(vals[2]);//third value in string (direction)
                
                keepReading(reader);
            }
        } catch (FileNotFoundException e){
        }
    }
    
    public static void keepReading(Scanner reader){
        String next;
        while (reader.hasNextLine()){/*while there is more lines in a given file, 
            read the line (each of which will be an edge)*/
            next = reader.nextLine();
            
            String[] vals = next.split(" ");
            int one = Integer.parseInt(vals[0]);
            //System.out.print(one + " ");
            edgen.add(one);//adds the first value to edges 
            int two = Integer.parseInt(vals[1]);
            edgen.add(two);//adds second value to edges 
            //System.out.println(two);
        }
            
    }
    
    //defines the file given a 0 or 1
    public static boolean defineDirect(int directed){
        return directed == 1;

    }
    
    public static graph g;
    
    public static void display(graph g){
        Scanner console = new Scanner(System.in);
        System.out.print("Root node of the tree: ");
        int root = Integer.parseInt(console.nextLine());
        g.bfs(root);
        
        
        System.out.print("Find the path between vertices: ");
        String pathval = console.nextLine();
        String[] split = pathval.split(" ");
        int p1 = Integer.parseInt(split[0]);
        int p2 = Integer.parseInt(split[1]);
        g.findPath(p1, p2, g.parent);
        
        
        System.out.println();
        System.out.print("Descendants of vertex: ");
        int desc = Integer.parseInt(console.next());
        g.findDescendents(desc);
        System.out.print("\b");
        
        //checks if each edge is a nonTreeEdge
        System.out.println();
        System.out.println("Non-tree edges in the tree: " + g.NontreeCount);
        if (g.NontreeCount == 0){
            System.out.println("No nontree edges in the tree. ");
        } else{
            //prints out each nontree edge
            for (int a = 0; a < g.nontreeEdges.size(); a+=2){
                System.out.println(g.nontreeEdges.get(a) + " " + g.nontreeEdges.get(a+1));
            }
            
        }
        
         
        System.out.println("Back edges in the tree: " + g.backCount);
        if (g.backCount == 0){
            System.out.println("No back edges in the tree. ");
        } else{
            //prints out each backedge
            for (int a = 0; a < g.backEdges.size(); a+=2){
                System.out.println(g.backEdges.get(a) + " " + g.backEdges.get(a+1));
            } 
        }
        
        
        
        System.out.println("Number of cycles in the graph: " + g.cycleCount);
        if (g.cycleCount == 0){
            System.out.println("No cycles in the tree");
        }else{
            int c = 1;
            //System.out.println("Cycle " + c + ": " + g.allCycles.get(0));
            for (int i = 0; i<g.allCycles.size(); i++){
                System.out.println("Cycle " + c + ": " + g.allCycles.get(i));
                c++;
            }
            System.out.print("\b");
        }
        
        
    }
    
    public static void main(String args[]){
        File read = new File(args[0]);//use directed.txt or undirected.txt
        readFile(read);
        
        //creates a graph with v vertices and is either directed or undirected
        graph w = new graph(v,defineDirect(direct));
        
        //adds all the edges in from the file
        for(int i = 0; i < edgen.size(); i+= 2){
            w.addEdge(edgen.get(i), edgen.get(i+1));
            
            //if the graph is undirected, must add the reverse edges 
        }
        display(w);
    }
}

class graph{
    int nvertices;// number of vertices
    //int nedges;
    boolean directed;// is the graph directed
    
    LinkedList<Integer> edges[];// list of edges
    int[] parent;// notifies what parent 
    
    //keeps track of descendents of a given value, must be deleted after each instance

    ArrayList<Integer> backEdges = new ArrayList<>();
    ArrayList<Integer> nontreeEdges = new ArrayList<>();
    
    ArrayList<String> allCycles = new ArrayList<>();
    String out = "";
    
    boolean[] processed;
    boolean[] discovered;
    
    int NontreeCount;// count for nontree edges
    int backCount;// count for back edges
    int cycleCount;//count for amount of cycles
    
    graph(int v, boolean dirt){
        nvertices = v;
        directed = dirt;
        edges = new LinkedList[v + 1];
        //degree = new int[nvertices + 1];
        for (int i = 1; i <= nvertices; i++){
            edges[i] = new LinkedList();//there will be nvertices linkedlists in edges
        }
    }
    
    void addEdge(int v, int w){
        edges[v].add(w);//adds w to the v linkedlist
        
        if (!directed){
            edges[w].add(v);
        }
    }
    
    void findPath(int start, int end, int parents[]){
        
        if ((start == end) || (end == -1)){
            System.out.printf(" %d", start);
            
        } else{
            findPath(start, parents[end], parents);
            System.out.printf(" %d", end);
            
        }
 
    }
    
    String cycle(int start, int end, int parents[]){
        if ((start == end) || (end == -1)){
            //System.out.printf("\n %d", start);
            out += start + ", ";
            
        } else{
            cycle(start, parents[end], parents);
            //System.out.printf(" %d", end);
            out+= end + ", ";
            
        }
        
        return out;
    }
    
    void bfs(int start){
        /*each vertices needs to be processed and discovered so nvertices+1 
        space must be allocated for each boolean array*/
        processed = new boolean[nvertices+1];
        discovered = new boolean[nvertices+1];
        
        /*Similarly,each vertice must have a parent so nvertices+1 must be 
        allocated to the parent array*/
        parent = new int[nvertices+1];
        
        //the order of the bfs in linkedlist format
        Queue<Integer> q = new LinkedList<>();
        
        // the root is now discovered
        discovered[start] = true;
        
        //add start to the linkedlist
        q.add(start);
        
        //make the roots parent equal to -1 as it has no parent
        parent[start] = -1;
        
        
        //int count = 0;
        //while q is no longer empty
        while(!q.isEmpty()){
            //make start now the push of q
            start = q.poll();
            
            // the root is now processed
            processed[start] = true;
            
            Iterator<Integer> i = edges[start].listIterator();/*cycle through 
            list at edges[start]*/
            
            //while there is more edges to iterator over
            while(i.hasNext()){
                // n is the next iterator 
                int n = i.next();
                
                /*if n is not yet processed or if the graph is dirrected
                process the edge between start and n*/
                if ((processed[n] == false) || (directed)){
                    processEdge(start, n);
                    
                }
                
                /*if n is not yet discovered, make it discovered, add n to 
                the bfs, then make n's parent start as n is the child of start*/
                if(!discovered[n]){
                    discovered[n] = true;
                    q.add(n);
                    parent[n] = start;
                }
                
                
            }
           
        }
        
    }
    
    void processEdge(int x, int y){
        
        //if y is discovered and the parent of y is not x there is nontree edge
        if (discovered[y] && parent[y] != x){
            
            nontreeEdges.add(x); 
            nontreeEdges.add(y);
            NontreeCount++;
            
            //if y is an ancestor of x there is a backedge
            if (isAncester(x,y)){
                backEdges.add(x); 
                backEdges.add(y);
                backCount++;
                
                //additionally if there is a backedge there is a cycle
                cycleCount++;
                
                String one = cycle(y,x,parent);
                one += Integer.toString(y);
                /*adds all strings(cycles) to one ArrayList to be printed out 
                in display*/
                allCycles.add(one);
                
            } 
        }
        
    }
    
    boolean isAncester(int x, int y){
        int p = parent[x];
        
        // while the parent of x is not equal to y and x is not the root
        while(p!= y && p!= -1){
            /* subsequentially check the parent of the parent of x and so on 
            until you reach the node*/
            p = parent[p];
        }
        
        //if y is an ancestor of x returns true, if not returns false
        return p == y;
    }
    
    void findDescendents(int val){

        //searches parent for a value that equals val
        for (int i = 1; i < parent.length; i++){
            if (parent[i] == val){// if the parent of i is the val
                System.out.printf(" %d", i);
                System.out.print(",");
                findDescendents(i); /* recursively calls findDescendents of i 
                until val has no descendents*/
            }  
        } 
    }

}

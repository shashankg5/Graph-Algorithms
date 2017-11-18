/*
README:

->Coded this project in Java.
->Input file name used for this project is "lesmis.gml" which is provided in the program itself.
->Command to provide to form a graph is "Graph"
->3 algorithms are implemented in this project namely Prim's, Dijkstra's, Bellman-ford
->Command to apply Prim's algorithm on generated graph is  -  Prim
->Prim algorithm- 
1. key for the source vertex provided in the program will be set to 0 
2. Priority queue will return the vertex with minimum key 
3. keys for the vertices returned from Adjecency list of the minimum key given by priority key is comapared to existing value 
and minimum value will be maintained in priority key.
4. Priority queue will maintain the vertices with minimum weights 
-> Prim algorithm will return the all the edges which are accessible from the provided source vertex in the program to form
minimum spanning tree 
->Command to apply Dijkstra's algorithm on the generated graph is- Dijkstras node'node_source' node'node_destination' for ex. Dijkstras node70 node12
->Dijkstras algo-
1.Initialize single source : distances and predecessor to some default values
2.set distance[source] to 0
3.Till vertices exists
4.Find the vertex u with the minimum distance
5.For such a vertex u, all the adjacent vertices v are checked to relax edge (u,v) 
->Command to apply Bellman-ford algorithm on the generated graph is- Bellman node'node_source' node'node_destination'  for ex. Bellman node70 node12
->Bellman ford algo-
1.Bellman Ford runs two loops 
2.The outer loop runs for (no of vertices - 1) times
3.The inner loop runs for each edge (u,v) to relax the edge
4.After the end of the two loops
5.Another loop is run to check whether any edge(u,v) can be further relax
If such is the case then negative weight cycle exists and return false
->The command "print" prints all the outputs calculated using above commands
->The command "quit" causes the program to exit.

Total classes used:
GraphMain  (main class)
Graph
Vertex
Edge
ParseGraph


 */
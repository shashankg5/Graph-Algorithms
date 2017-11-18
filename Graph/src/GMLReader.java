import java.lang.reflect.*;
import java.util.*;
import java.io.*; 
import java.awt.*; 



public class GMLReader {
	
	 /**
	   * Stream tokenizer used in parsing the file.
	   */
	  private static StreamTokenizer st;
	  int countv=0,counte=0;
	 
	  /**
	   * Gets the next token from the stream.
	   *
	   * @return  token from stream
	   */
	  private static int nextToken() {
	    // Try to read next token from stream tokenizer.
	    try {
	      return st.nextToken();
	    }
	    // If something bad happened then return end-of-file.
	    catch ( IOException e ) {
	      return StreamTokenizer.TT_EOF;
	    }
	  } // nextToken
	  
	  public static Graph read(FileInputStream fis){
		    
		    // Initiate the stream tokenizer for parsing GML files.
		    Reader r = new BufferedReader( new InputStreamReader( fis ) );
		    st = new StreamTokenizer( r );
		    st.commentChar( '#' );
		    st.wordChars( 95, 95 ); 
		    // holds current state of parser
		    int state = 0;
		    // holds current token value
		    int token;
		    // indicates whether to loop or not
		    boolean loop = true;
		    // indicates if parse was successful (assume failure)
		    boolean success = false;
		    // graph object which will contain new graph read from file
		    Graph graph = null;
		    // current node being created
		    boolean node = false;
		    int openAttributes = 0;
		    int nodeOpenAttributes = 0;
		    int edgeOpenAttributes = 0;

		    // attributes of the current node
		    int id = 0;
		    int x = 0;
		    int y = 0;  
		    String nodeLabel = null;
		    String color;
		    int w = 50;
		    int h = 50;
		  //  int type = Shapes.OVAL;
		   // Color nodeFill = VertexPic.DEFAULT_COLOR;  
		    
		    
		    // attributes of the current edge
		    int edgeFrom = 0;
		    int edgeTo = 0;
		   // Color edgeFill = EdgePic.DEFAULT_COLOR; 
		    int value=0;

		    // Beginning of the finite state machine. This loops until
		    // loop equals false, which only occurs when there is a parse
		    // error, we have reached the end of the file, or when we are
		    // finished parsing the graph (which may be before the EOF).
		    while ( loop ) {
		      // Read in the next token and switch based on current state.
		      token = nextToken();


		      // If we reached end of file, break out of while loop.
		      if ( token == StreamTokenizer.TT_EOF ) {
			break;
		      }
		      switch ( state ) {
		      case 0 :
			// Look for the opening "graph" keyword.
			if ( token == StreamTokenizer.TT_WORD ) {
			  if ( st.sval.equalsIgnoreCase( "graph" ) ) {
			    // Create the new graph object.
			   graph = new Graph();
			    state = 1;
			  }
			  else {
			    System.out.println( "Missing graph keyword" );
			    loop = false;
			  }
			}
			else {
			  System.out.println( "No valid word token found" );
			  loop = false;
			}
			break;
		      case 1 :
			// A [ must follow the "graph" keyword.
			if ( token == '[' ) {
			  state = 2;
			}
			else {
			  System.out.println( "Missing [ after graph" );
			  loop = false;
			}
			break;
		      case 2 :
			// Look for "node", "edge", or ].
			if ( token == StreamTokenizer.TT_WORD ) {
			  if ( st.sval.equalsIgnoreCase( "node" ) ) {
			    state = 3;
			  }
			  else if ( st.sval.equalsIgnoreCase( "edge" ) ) {
			    // Begin edge definition.
			    edgeFrom = 0;
			    edgeTo = 0;
			    value = 0;
			    state = 10;
			  }
			}
			else if (token == '['){
			  openAttributes++;
			}
			else if ( token == ']' ) {
			  if(openAttributes == 0){
			    // Stop parsing file and return "success".
			    success = true;
			    loop = false;
			  }
			  else
			    openAttributes--;
			}
			// else we ignore the token
			break;
		      case 3 :
			// A [ must follow the "node" keyword.
			if ( token == '[' ) {
			  state = 4;
			}
			else {
			  System.out.println( "Missing [ after node" );
			  loop = false;
			}
			break;
		      case 4 :
			// Look for "id", "graphics", "label", or ].
			if ( token == StreamTokenizer.TT_WORD ) {
			  if ( st.sval.equalsIgnoreCase( "id" ) ) {
			    state = 5;
			  }
			  else if ( st.sval.equalsIgnoreCase( "label" ) ) {
			    state = 14;
			  }
			  else if ( st.sval.equalsIgnoreCase( "graphics" ) ) {
			    state = 6;
			  }
			}
			else if (token == '['){
			  nodeOpenAttributes++;
			}
			else if ( token == ']' )  {
			  if(nodeOpenAttributes == 0){
			    
			      // Create new vertex and add to graph. 
			    	Vertex v=new Vertex(nodeLabel,id);
			      node = graph.addVertex(v,true);
			      
			      
			      
			      
			      id = 0;
			   
			      nodeLabel = null;
			

			    // Node definition complete.
			    state = 2;
			  }
			  else
			    nodeOpenAttributes--;
			}
			// else we ignore the token
			break;
		      case 5 :
			// A number must follow the "id" keyword.
			if ( token == StreamTokenizer.TT_NUMBER ){
			  id = (int)st.nval;
			  state = 4;
			}
			else {
			  System.out.println( "Missing node id value" );
			  loop = false;
			}
			break;
		      case 6 :
			// A [ must follow the "graphics" keyword.
			if ( token == '[' ) {
			  state = 7;
			}
			else {
			  System.out.println( "Missing [ after graphics" );
			  loop = false;
			}
			break;
		      case 7 :
			// Look for "x", "y", "type", "w", "h", "fill", or ].
			if ( token == StreamTokenizer.TT_WORD ) {
			  if ( st.sval.equalsIgnoreCase( "x" ) ) {
			    state = 8;
			  }
			  else if ( st.sval.equalsIgnoreCase( "y" ) ) {
			    state = 9;
			  }
			  else if ( st.sval.equalsIgnoreCase( "type" ) ) {
			    state = 15;
			  }
			  else if ( st.sval.equalsIgnoreCase( "w" ) ) {
			    state = 16;
			  }
			  else if ( st.sval.equalsIgnoreCase( "h" ) ) {
			    state = 17;
			  }
			  else if ( st.sval.equalsIgnoreCase( "fill" ) ) {
			    state = 18;
			  }
			}
			else if ( token == ']' ) {
			  // Node graphics definition complete.
			  state = 4;
			}
			break;
		      case 8 :
			// A number must follow the "x" keyword.
			if ( token == StreamTokenizer.TT_NUMBER ) {
			  // Set node x position.
			  x = (int)st.nval;
			  state = 7;
			}
			else {
			  System.out.println( "Missing x value" );
			  loop = false;
			}
			break;
		      case 9 :
			// A number must follow the "y" keyword.
			if ( token == StreamTokenizer.TT_NUMBER ) {
			  // Set node y position.
			  y = (int)st.nval;
			  state = 7;
			}
			else {
			  System.out.println( "Missing y value" );
			  loop = false;
			}
			break;
		      case 10 :
			// A [ must follow the "edge" keyword.
			if ( token == '[' ) {
			  state = 11;
			}
			else {
			  System.out.println( "Missing [ after edge" );
			  loop = false;
			}
			break;
		      case 11 :
			// Look for "source", "target", "fill", or ].
			if ( token == StreamTokenizer.TT_WORD ) {
			  if ( st.sval.equalsIgnoreCase( "source" ) ) {
			    state = 12;
			  }
			  else if ( st.sval.equalsIgnoreCase( "target" ) ) {
			    state = 13;
			  }
			  else if ( st.sval.equalsIgnoreCase( "value" ) ) {
			    state = 19;
			  }
//			  else if ( st.sval.equalsIgnoreCase( "directed" ) ) {
//			    // Current edge is directed.
//			    edgeDirected = true;
//			  }
			}
			else if (token == '[' )
			  edgeOpenAttributes++;
			else if ( token == ']' ){
			  if(edgeOpenAttributes == 0){
			    // Edge definition complete, add it to the graph.
			    // If an edge already exists, it won't be added.
//			    try{
//			      EdgePic e = graph.addEdge( edgeFrom , edgeTo , 
//							 edgeDirected, edgeFill); 
//			      edgeFill = EdgePic.DEFAULT_COLOR;
//			      edgeDirected = false;
//			    }
//			    catch(VertexNotExistException e){
//			      System.out.println( "An edge between invalid vertices" );
//			      loop = false;
//			    }
				  
//				  Vertex v1=new Vertex();
//				  v1.setId(edgeFrom);
//				  Vertex v2=new Vertex();
//				  v2.setId(edgeTo);
				  graph.addEdge(graph.getVertex(edgeFrom),graph.getVertex(edgeTo),value);
				  
				  
			    state = 2; 
			    
			  //  System.out.println("added one edge");
			  }
			  else
			    edgeOpenAttributes--;
			}
			// else we ignore the token
			break;
		      case 12 :
			// A number must follow the "source" keyword.
			if ( token == StreamTokenizer.TT_NUMBER ) {
			  // Set edge source.
			  edgeFrom = (int)st.nval;
			  state = 11;
			}
			else {
			  System.out.println( "Missing edge source value" );
			}
			break;
		      case 13 :
			// A number must follow the "target" keyword.
			if ( token == StreamTokenizer.TT_NUMBER ) {
			  // Set edge target.
			  edgeTo = (int)st.nval;
			  state = 11;
			}
			else {
			  System.out.println( "Missing edge target value" );
			}
			break;
		      case 14 :
			// A String must follow the label keyword.
			if ( token == '"' ) {
			  //set the nodeLabel variable
			  nodeLabel = st.sval;
			  state = 4;
			}
			else {
			  System.out.println( "Label must be followed by a string" );
			  loop = false;
			}
			break;	
		      case 15 :
			// A string must follow the "type" keyword.
			if ( token == '"' ) {
			  if(st.sval.equalsIgnoreCase( "oval" )){
			   //  type = Shapes.OVAL;
			     state = 7;
			  }
			  else if(st.sval.equalsIgnoreCase( "rectangle" )){
			  //  type = Shapes.RECTANGLE;   
			    state = 7;
			  }
			  else {
			    System.out.println( "Warning: Unknown node shape" );
			    loop = false;
			  }
			}
			else {
			    System.out.println( "Type must be followed by a string" );
			    loop = false;
			}
			break;	

		      case 16 :
			// A number must follow the "x" keyword.
			if ( token == StreamTokenizer.TT_NUMBER ) {
			  // Set node width.
			  w = (int)st.nval;
			  state = 7;
			}
			else {
			  System.out.println( "Missing the width value" );
			  loop = false;
			}
			break;
		      case 17 :
			// A number must follow the "y" keyword.
			if ( token == StreamTokenizer.TT_NUMBER ) {
			  // Set node height.
			  h = (int)st.nval;
			  state = 7;
			}
			else {
			  System.out.println( "Missing the height value" );
			  loop = false;
			}
			break;	

		      case 18 :
			// A String must follow the fill keyword.
			if ( token == '"' ) {
			  try{
			    //set the fill variable
			//    nodeFill = Color.decode(st.sval);
			    state = 7;
			  }
			  catch(NumberFormatException exception){
			    System.out.println( "fill value is not a hexadecimal" );
			    loop = false;
			  }
			}
			else {
			  System.out.println( "fill must be followed by a string" );
			  loop = false;
			}
			break;
		      case 19 :
			// A String must follow the value keyword.
			if (  token == StreamTokenizer.TT_NUMBER ) {
				value=(int)st.nval;
			
			    state = 11;
			  
			}
			else {
			  System.out.println( "value must be followed by a integer" );
			  loop = false;
			}
			break;	
		      default :
			// Something really strange happened.
			System.out.println( "Error: Invalid state value" );
			loop = false;
			break;
		      }// switch state
		    } // while true
		    
		    // If we were successful return the graph, otherwise null.
		    if ( success ) {
		      return graph;
		    }
		    else {
		      return null;
		    }
		  }

	  
	
	
	
	
	
	
	
	
	

//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//
//		
//		Graph graph;
//		
//		
//		
//		
//		
//	}

}

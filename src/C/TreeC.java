package C;

import javax.swing.*;
import javax.swing.tree.*;

import java.io.*;        
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;

public class TreeC extends JFrame implements ActionListener {
	Scanner scanner;
	File f;
	
	/*int som räknar slut/ starttaggar*/
	int start = 0;
	int slut = 0;
	
	/*noden man jobbar med*/
	MyNode c;
	
	public TreeC() {
      Container co = getContentPane();
      //*** Build the tree and a mouse listener to handle clicks
      f = new File("Liv.xml");
      try {
		scanner = new Scanner(f);
	} catch (FileNotFoundException e) {
		System.out.println("Filen finns inte!");
		System.exit(0);
	} 
      try{
    	  root = readNode();
      }catch(Exception e){
    	  System.out.println(e);
    	  System.exit(0);
      }
      treeModel = new DefaultTreeModel( root );
      tree = new JTree( treeModel );
      MouseListener ml = 
        new MouseAdapter() {
          public void mouseClicked( MouseEvent e ) {
            if ( box.isSelected() )
              showDetails( tree.getPathForLocation( e.getX(), 
                                            e.getY() ) );       
          }
        };
      tree.addMouseListener( ml );      
      //*** panel the JFrame to hold controls and the tree
      controls = new JPanel();
      box = new JCheckBox( showString );
      init(); //** set colors, fonts, etc. and add buttons
      co.add( controls, BorderLayout.NORTH );
      co.add( tree, BorderLayout.CENTER );   
      setVisible( true ); //** display the framed window
   } 

   public void actionPerformed( ActionEvent e ) {
      String cmd = e.getActionCommand();
      if ( cmd.equals( closeString ) )
        dispose();
   }

   private void init() {
      tree.setFont( new Font( "Dialog", Font.BOLD, 12 ) );
      controls.add( box );
      addButton( closeString );
      controls.setBackground( Color.lightGray );
      controls.setLayout( new FlowLayout() );    
      setSize( 400, 400 );
   }

   private void addButton( String n ) {
      JButton b = new JButton( n );
      b.setFont( new Font( "Dialog", Font.BOLD, 12 ) );
      b.addActionListener( this );
      controls.add( b );
   }
   /*Kollar efter fel*/
   private void kollaFel(String in) throws Exception {	   
	   /*kollar efter fel <>*/
	   if (in.contains("<") && !in.contains(">") || !in.contains("<") && in.contains(">")){
		   System.out.println("Nu blev det fel");
		   throw new Exception("fel i >><<<");
	   }
   }
   
   /*kollar om en ny nod ska skapas*/
   private boolean newNode(String in){
	   if (in.contains("</")) {
		   slut++;
	   }else if (in.contains("<")) {
		   start++;
	   }
	   return !in.contains("</");
   }
   
   /*skapar en ny nod*/
   private MyNode createNode(String in) {
	   int indexStart = in.indexOf("namn=")+5;
	   String s = in.substring(indexStart+1);
	   int indexStop = s.indexOf('"');
	   //System.out.println(s);
	   String namn = s.substring(0,indexStop);
	   
	   indexStart = s.indexOf('>');
	   String text = s.substring(indexStart+1, s.length());
	   
	   return new MyNode(namn,text);
   }
    
   public MyNode readNode() throws Exception {
	   String s = scanner.nextLine(); 
	   if (s.contains("xml")){
	   		s = scanner.nextLine(); //den hoppar över den första raden
	   }
	   boolean a = newNode(s); // bara för att köra newNode
	   
	   MyNode rooten = createNode(s);
	   c = rooten;
	   while (scanner.hasNextLine()) {
		   s = scanner.nextLine();
		   try{
			   kollaFel(s);
		   }catch(Exception e){
			   System.out.println(e);
			   System.exit(0);
		   }
		   if (newNode(s)){
			   MyNode ny = createNode(s);
			   c.add(ny);
			   c = ny; 
		   }
		   else{
			   if (c == null) {
				   throw new Exception("för många sluttagar");
			   }
			   c = (MyNode) c.getParent(); 
		   }
	   }
	   if (start != slut){
		   throw new Exception("fel antal tagar");
	   }
	   return rooten;
   }

   private void showDetails( TreePath p ) {
      if ( p == null )
        return;
      c = (MyNode) p.getLastPathComponent();
      String info = c.getInfo() +"\n Men allt som";
      
      while (c != null) {
    	  info += " är "+c.toString();
    	  c = (MyNode) c.getParent();  
      }
      JOptionPane.showMessageDialog(this,info);     
   }

   public static void main( String[ ] args ) {
       new TreeC();
   }

   private JCheckBox box;
   private JTree tree;
   private MyNode root;
   private DefaultTreeModel treeModel;
   private JPanel controls;
   private static final String closeString = " Close ";
   private static final String showString = " Show Details ";
   
   
   /*mynode class*/
   class MyNode extends DefaultMutableTreeNode{
	   String level;
	   String text;
	   
	   /**MyNode(String namn, String info)*/
	   MyNode(String l, String t){
		   level = l;
		   text = t;	   
	   }   
	   
	    @Override
		public String toString() {
			return level;
		}
	   
	   	public String getText() {
			return level;
		}
	   	
	   	public String getInfo() {
	   		return text;
	   	}
   }
}

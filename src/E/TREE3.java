package E;

import javax.swing.*;
import javax.swing.tree.*;

import java.io.*;        
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;

public class TREE3 extends JFrame implements ActionListener {
	Scanner scanner;
	File f;
	/*noden man jobbar med*/
	MyNode c;
	
	public TREE3() {
      Container c = getContentPane();
      //*** Build the tree and a mouse listener to handle clicks
      f = new File("Liv.xml");
      try {
		scanner = new Scanner(f);
	} catch (FileNotFoundException e) {
		System.out.println(e);
	} 
      root = readNode();
    	  
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
      c.add( controls, BorderLayout.NORTH );
      c.add( tree, BorderLayout.CENTER );   
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
   
   /*kollar om en ny nod ska skapas*/
   private boolean newNode(String in) {
	   return !in.contains("</");
   }
   
   /*skapar en ny nod*/
   private MyNode createNode(String in) {
	   int indexStart = in.indexOf('"');
	   int indexStop = in.indexOf("> ");
	   
	   if (indexStart < 0 || indexStop < 0){
		   System.out.println(indexStart+" "+indexStop);
		   return new MyNode("Nublevdetfel","dasasdas");
	   }
	   
	   String namn = in.substring(indexStart+1, indexStop-1);
	   String text = in.substring(indexStop+1, in.length());
	   
	   return new MyNode(namn,text);
   }
    
   public MyNode readNode(){
	   String s = scanner.nextLine(); 
	   //System.out.println(s);
	   s = scanner.nextLine(); //den hoppar över den första raden
	   MyNode rooten = createNode(s);
	   c = rooten;
	   while (scanner.hasNextLine()) {
		   s = scanner.nextLine();
		   //System.out.println(s);
		   if (newNode(s)){
			   MyNode ny = createNode(s);
			   c.add(ny);
			   c = ny; 
		   }
		   else{
			   //if (c.getParent() != null);
			   c = (MyNode) c.getParent();
		   }
	   }
	   
	   return rooten;
   }

   private void showDetails( TreePath p ) {
      if ( p == null )
        return;
 
      JOptionPane.showMessageDialog(this,((MyNode) p.getLastPathComponent()).getInfo());     
   }

   public static void main( String[ ] args ) {
       new TREE3();
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
			// TODO Auto-generated method stub
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

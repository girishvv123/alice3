/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package org.alice.ide.operations.help;

/**
 * @author Dennis Cosgrove
 */
class AllSystemPropertiesOperation extends org.alice.ide.operations.InconsequentialActionOperation {
	public AllSystemPropertiesOperation() {
		this.putValue( javax.swing.Action.NAME, "Show All Properties..." );
	}
	@Override
	protected void performInternal( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
		java.util.Properties properties = System.getProperties();
		java.util.Map< String, String > map = new java.util.HashMap< String, String >();
		java.util.Enumeration< String > nameEnum = (java.util.Enumeration< String >)properties.propertyNames();
		java.util.SortedSet< String > names = new java.util.TreeSet< String >();
		int max = 0;
		while( nameEnum.hasMoreElements() ) {
			String name = nameEnum.nextElement();
			names.add( name );
			max = Math.max( max, name.length() );
		}
		String formatString = "%-" + (max+1) + "s";
		StringBuffer sb = new StringBuffer();
		for( String name : names ) {
			java.util.Formatter formatter = new java.util.Formatter();
			sb.append( formatter.format( formatString, name ) );
			sb.append( ": " );
			sb.append( System.getProperty( name ) );
			sb.append( "\n" );
		}
		javax.swing.JTextArea textArea = new javax.swing.JTextArea( sb.toString() );
		java.awt.Font font = textArea.getFont();
		textArea.setFont( new java.awt.Font( "Monospaced", font.getStyle(), font.getSize() ) );
		javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane( textArea );
		scrollPane.setPreferredSize( new java.awt.Dimension( 640, 480 ) );
		javax.swing.JOptionPane.showMessageDialog( this.getIDE(), scrollPane, "System Properties", javax.swing.JOptionPane.INFORMATION_MESSAGE ); 
	}
}

/**
 * @author Dennis Cosgrove
 */
class PathPropertyOperation extends org.alice.ide.operations.InconsequentialActionOperation {
	private String propertyName;
	public PathPropertyOperation( String propertyName ) {
		this.propertyName = propertyName;
		this.putValue( javax.swing.Action.NAME, "Show..." );
	}
	@Override
	protected void performInternal( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
		edu.cmu.cs.dennisc.croquet.swing.FormPane formPane = new edu.cmu.cs.dennisc.croquet.swing.FormPane( 8, 2 ) {
			private java.awt.Component[][] createComponentRowsForSystemProperty( String name, String separator ) {
				String value = System.getProperty( name );
				assert value != null;
				String[] array = value.split( separator );
				java.awt.Component[][] rv = new java.awt.Component[ array.length ][];
				for( int i=0; i<array.length; i++ ) {
					String prefix;
					if( i==0 ) {
						prefix = name;
					} else {
						prefix = "";
					}
					rv[ i ] = edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createColumn0Label( prefix+"[" + i + "]:" ), edu.cmu.cs.dennisc.zoot.ZLabel.acquire( array[ i ] ) );
				}
				return rv;
			}
			@Override
			protected java.util.List<java.awt.Component[]> addComponentRows(java.util.List<java.awt.Component[]> rv) {
				String pathSepartor = System.getProperty( "path.separator" );
				for( java.awt.Component[] componentRow : createComponentRowsForSystemProperty( propertyName, pathSepartor ) ) {
					rv.add( componentRow );
				}
				return rv;
			}
		};
		javax.swing.JOptionPane.showMessageDialog( this.getIDE(), formPane, "System Property: " + this.propertyName, javax.swing.JOptionPane.INFORMATION_MESSAGE ); 
	}
}

/**
 * @author Dennis Cosgrove
 */
public class DisplaySystemPropertiesOperation extends org.alice.ide.operations.InconsequentialActionOperation {
	public DisplaySystemPropertiesOperation() {
		this.putValue( javax.swing.Action.NAME, "Display System Properties..." );
	}
	@Override
	protected void performInternal( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
		edu.cmu.cs.dennisc.croquet.swing.FormPane formPane = new edu.cmu.cs.dennisc.croquet.swing.FormPane( 8, 2 ) {
			private java.awt.Component[] createComponentRowForSystemProperty( String name ) {
				String value = System.getProperty( name );
				assert value != null;
				return edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createColumn0Label( name+":" ), edu.cmu.cs.dennisc.zoot.ZLabel.acquire( value ) );
			}
			@Override
			protected java.util.List<java.awt.Component[]> addComponentRows(java.util.List<java.awt.Component[]> rv) {
				rv.add( createComponentRowForSystemProperty( "java.version" ) );
				rv.add( createComponentRowForSystemProperty( "os.name" ) );
				rv.add( createComponentRowForSystemProperty( "os.version" ) );
				rv.add( createComponentRowForSystemProperty( "os.arch" ) );
				rv.add( createComponentRowForSystemProperty( "sun.arch.data.model" ) );
				rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( javax.swing.Box.createVerticalStrut( 8 ), null ) );
				for( String propertyName : new String[] { "java.class.path", "java.library.path" } ) {				
					rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createColumn0Label( propertyName+":" ), new edu.cmu.cs.dennisc.zoot.ZHyperlink( new PathPropertyOperation(propertyName) ) ) );
				}
				rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( javax.swing.Box.createVerticalStrut( 8 ), null ) );
				rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( null, new edu.cmu.cs.dennisc.zoot.ZHyperlink( new AllSystemPropertiesOperation() ) ) );
				return rv;
			}
		};
//		edu.cmu.cs.dennisc.croquet.swing.PageAxisPane pane = new edu.cmu.cs.dennisc.croquet.swing.PageAxisPane(
//			formPane,
//			javax.swing.Box.createVerticalStrut( 16 ),
//			new edu.cmu.cs.dennisc.zoot.ZButton( new AllSystemPropertiesOperation() ),
//			javax.swing.Box.createVerticalStrut( 8 )
//		);
		javax.swing.JOptionPane.showMessageDialog( this.getIDE(), formPane, "System Properties", javax.swing.JOptionPane.INFORMATION_MESSAGE ); 
	}
	
	public static void main( String[] args ) {
		DisplaySystemPropertiesOperation displaySystemPropertiesOperation = new DisplaySystemPropertiesOperation();
		edu.cmu.cs.dennisc.zoot.ZManager.performIfAppropriate( displaySystemPropertiesOperation, null, false );
	}
}

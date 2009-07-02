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

package edu.cmu.cs.dennisc.toolkit.issue;

import edu.cmu.cs.dennisc.issue.IssueReportWorker;
import edu.cmu.cs.dennisc.issue.ReportGenerator;
import edu.cmu.cs.dennisc.issue.ReportSubmissionConfiguration;

/**
 * @author Dennis Cosgrove
 */
public class ProgressPane extends javax.swing.JPanel {
	private javax.swing.JTextPane console = new javax.swing.JTextPane();
	private javax.swing.JButton background = new javax.swing.JButton( "run in background" );
	private IssueReportWorker issueReportWorker;
	private boolean isDone = false;
	private boolean isSuccessful = false;
	private boolean isBackgrounded = false;

	public ProgressPane() {
		this.console.setPreferredSize( new java.awt.Dimension( 400, 240 ) );

		this.background.addActionListener( new java.awt.event.ActionListener() {
			public void actionPerformed( java.awt.event.ActionEvent e ) {
				ProgressPane.this.handleRunInBackground( e );
			}
		} );
		javax.swing.JPanel buttonPane = new javax.swing.JPanel();
		buttonPane.setLayout( new java.awt.FlowLayout() );
		buttonPane.add( this.background );

		this.setLayout( new java.awt.BorderLayout() );
		this.add( new javax.swing.JScrollPane( this.console ), java.awt.BorderLayout.CENTER );
		this.add( new javax.swing.JScrollPane( buttonPane ), java.awt.BorderLayout.SOUTH );
	}
	public void initializeAndExecuteWorker( ReportGenerator issueReportGenerator, ReportSubmissionConfiguration reportSubmissionConfiguration ) {
		this.issueReportWorker = new IssueReportWorker( this, issueReportGenerator, reportSubmissionConfiguration );
		this.issueReportWorker.execute();
	}
	private void hideRoot() {
		java.awt.Component root = javax.swing.SwingUtilities.getRoot( this );
		if( root != null ) {
			root.setVisible( false );
		}
	}
	private void handleRunInBackground( java.awt.event.ActionEvent e ) {
		this.isBackgrounded = true;
		this.hideRoot();
	}
	public void handleProcess( java.util.List< String > chunks ) {
		for( String chunk : chunks ) {
			javax.swing.text.Document document = ProgressPane.this.console.getDocument();
			try {
				document.insertString( document.getLength(), chunk, null );
			} catch( javax.swing.text.BadLocationException ble ) {
				throw new RuntimeException( ble );
			}
			System.out.print( chunk );
		}
	}
	public void handleDone( boolean isSuccessful ) {
		this.isDone = true;
		this.isSuccessful = isSuccessful;
		this.hideRoot();
	}
	public boolean isDone() {
		return this.isDone;
	}
	public boolean isBackgrounded() {
		return this.isBackgrounded;
	}
	public boolean isSuccessful() {
		return this.isSuccessful;
	}
}

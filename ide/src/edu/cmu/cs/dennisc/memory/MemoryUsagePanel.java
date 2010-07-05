/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package edu.cmu.cs.dennisc.memory;

class MemoryUsageGraph extends edu.cmu.cs.dennisc.croquet.JComponent< javax.swing.JComponent > {
	private static final long K = 1024;
	//private static final long M = K*K;
	private java.util.List< java.lang.management.MemoryUsage > samples = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
	public MemoryUsageGraph() {
		this.setBackgroundColor( java.awt.Color.BLACK );
		this.setForegroundColor( java.awt.Color.WHITE );
	}
	public void addSample( java.lang.management.MemoryUsage heapUsage ) {
		this.samples.add( heapUsage );
		this.repaint();
	}
	private void paintComponent( java.awt.Graphics2D g2 ) {
		java.awt.geom.GeneralPath path = null;
		final int xDelta = 10;
		float x = 0.0f;
		int width = this.getWidth();
		int height = this.getHeight();
		int sampleCount = width/xDelta;
		
		final int N = this.samples.size();
		int i0 = Math.max( N-sampleCount, 0 );
		for( int i=i0; i<N; i++ ) {
			java.lang.management.MemoryUsage sample = this.samples.get( i );
			double portion = sample.getUsed()/K / (double)(sample.getMax()/K);
			float y = (float)((1.0-portion)*height);
			if( path != null ) {
				path.lineTo( x, y );
			} else {
				path = new java.awt.geom.GeneralPath();
				path.moveTo( x, y );
			}
			x += xDelta;
		}
		if( this.samples.size() > 1 ) {
			g2.draw( path );
		}
	}
	@Override
	protected javax.swing.JComponent createAwtComponent() {
		javax.swing.JComponent rv = new javax.swing.JComponent() {
			@Override
			protected void paintComponent( java.awt.Graphics g ) {
				//super.paintComponent( g );
				java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
				g2.setColor( this.getBackground() );
				g2.fill( g.getClipBounds() );
				g2.setColor( this.getForeground() );
				MemoryUsageGraph.this.paintComponent( g2 );
			}
			@Override
			public java.awt.Dimension getPreferredSize() {
				return new java.awt.Dimension( 640, 480 );
			}
		};
		rv.setOpaque( false );
		return rv;
	}
};

class GarbageCollectAction extends edu.cmu.cs.dennisc.croquet.ActionOperation {
	private static final edu.cmu.cs.dennisc.croquet.Group SYSTEM_GROUP = new edu.cmu.cs.dennisc.croquet.Group( java.util.UUID.fromString( "7261a372-2b8d-4862-9669-852ba5e217e6" ), "SYSTEM_GROUP" );
	public GarbageCollectAction() {
		super( SYSTEM_GROUP, java.util.UUID.fromString( "04dd2f4c-31d8-400e-8467-22a810e089b4" ) );
		this.setName( "garbage collect" );
	}
	@Override
	protected void perform( edu.cmu.cs.dennisc.croquet.ActionOperationContext context ) {
		System.gc();
		context.finish();
	}
}

public class MemoryUsagePanel extends edu.cmu.cs.dennisc.croquet.BorderPanel {
	private GarbageCollectAction garbageCollectAction = new GarbageCollectAction();
	private MemoryUsageGraph memoryUsageGraph = new MemoryUsageGraph();
	private javax.swing.Timer timer = new javax.swing.Timer( 500, new java.awt.event.ActionListener() {
		public void actionPerformed( java.awt.event.ActionEvent e ) {
			java.lang.management.MemoryMXBean memory = java.lang.management.ManagementFactory.getMemoryMXBean();
			java.lang.management.MemoryUsage heapUsage = memory.getHeapMemoryUsage();
			MemoryUsagePanel.this.memoryUsageGraph.addSample( heapUsage );
		}
	} );
	@Override
	protected void handleAddedTo( edu.cmu.cs.dennisc.croquet.Component< ? > parent ) {
		super.handleAddedTo( parent );
		this.timer.start();
	}
	@Override
	protected void handleRemovedFrom( edu.cmu.cs.dennisc.croquet.Component< ? > parent ) {
		this.timer.stop();
		super.handleRemovedFrom( parent );
	}
	
	public MemoryUsagePanel() {
		this.addComponent( this.memoryUsageGraph, Constraint.CENTER );
		this.addComponent( this.garbageCollectAction.createButton(), Constraint.EAST );
	}

	public static void main(String[] args) {
		edu.cmu.cs.dennisc.croquet.Application application = new edu.cmu.cs.dennisc.croquet.Application() {
			@Override
			protected edu.cmu.cs.dennisc.croquet.Component< ? > createContentPane() {
				return new MemoryUsagePanel();
			}
			@Override
			protected void handleAbout( java.util.EventObject e ) {
			}
			@Override
			protected void handlePreferences( java.util.EventObject e ) {
			}
			@Override
			protected void handleQuit( java.util.EventObject e ) {
				System.exit( 0 );
			}
			@Override
			protected void handleWindowOpened( java.awt.event.WindowEvent e ) {
			}
		};
		application.initialize( args );
		application.getFrame().pack();
		application.getFrame().setVisible( true );
	}
}

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
package edu.cmu.cs.dennisc.croquet;

/**
 * @author Dennis Cosgrove
 */
public abstract class Model<M> implements TrackableShape, Resolver< M > {
	private Group group;
	private java.util.UUID inividualUUID;
	public Model( Group group, java.util.UUID inividualUUID ) {
		this.group = group;
		this.inividualUUID = inividualUUID;
	}
	public Group getGroup() {
		return this.group;
	}
	public java.util.UUID getIndividualUUID() {
		return this.inividualUUID;
	}
	
	public M getResolved() {
		return (M)this;
	}

//	public CompositeContext getCurrentCompositeContext() {	
//		Application application = Application.getSingleton();
//		return application.getCurrentCompositeContext();
//	}
//	protected java.awt.Component getSourceComponent( Context< ? > context ) {
//		if( context != null ) {
//			java.util.EventObject e = context.getEvent();
//			return edu.cmu.cs.dennisc.java.lang.ClassUtilities.getInstance( e.getSource(), java.awt.Component.class );
//		} else {
//			return null;
//		}
//	}


//	private boolean isVisible = true;
//	public boolean isVisible() {
//		return this.isVisible;
//	}
//	public void setVisible( boolean isVisible ) {
//		if( this.isVisible != isVisible ) {
//			this.isVisible = isVisible;
//			synchronized( this.components ) {
//				for( Component<?> component : this.components ) {
//					component.setVisible( this.isVisible );
//				}
//			}
//		}
//	}
	private boolean isEnabled = true;
	public boolean isEnabled() {
		return this.isEnabled;
	}
	public void setEnabled( boolean isEnabled ) {
		if( this.isEnabled != isEnabled ) {
			this.isEnabled = isEnabled;
			synchronized( this.components ) {
				for( JComponent<?> component : this.components ) {
					component.setEnabled( this.isEnabled );
				}
			}
		}
	}

	private String toolTipText = null;
	public String getToolTipText() {
		return this.toolTipText;
	}
	public void setToolTipText( String toolTipText ) {
		if( edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( this.toolTipText, toolTipText ) ) {
			//pass
		} else {
			this.toolTipText = toolTipText;
			synchronized( this.components ) {
				for( JComponent<?> component : this.components ) {
					component.setToolTipText( this.toolTipText );
				}
			}
		}
	}

	private java.util.List< JComponent<?> > components = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
	
	protected void addComponent( JComponent<?> component ) {
		synchronized( this.components ) {
			this.components.add( component );
		}
		component.setEnabled( this.isEnabled );
		component.setToolTipText( this.toolTipText );
	}
	protected void removeComponent( JComponent<?> component ) {
		synchronized( this.components ) {
			this.components.remove( component );
		}
	}

	protected void repaintAllComponents() {
		synchronized( this.components ) {
			for( JComponent<?> component : this.components ) {
				component.repaint();
			}
		}
	}
	protected void revalidateAndRepaintAllComponents() {
		synchronized( this.components ) {
			for( JComponent<?> component : this.components ) {
				component.revalidateAndRepaint();
			}
		}
	}
	
	/*package-private*/ Iterable< JComponent<?> > getComponents() {
		return this.components;
	}
	
	private JComponent< ? > firstComponentHint;
	@Deprecated
	public <J extends JComponent< ? > > J getFirstComponent( Class<J> cls ) {
		if( this.firstComponentHint != null ) {
			return cls.cast( this.firstComponentHint );
		} else {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "getFirstComponent:", this );
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "count:", this.components.size() );
			for( JComponent< ? > component : this.components ) {
				if( cls.isAssignableFrom( component.getClass() ) ) {
					if( component.getAwtComponent().isShowing() ) {
						edu.cmu.cs.dennisc.print.PrintUtilities.println( "isShowing:", component.getAwtComponent().getClass() );
						return cls.cast( component );
					} else {
						//pass
					}
				}
			}
			for( JComponent< ? > component : this.components ) {
				if( cls.isAssignableFrom( component.getClass() ) ) {
					if( component.getAwtComponent().isVisible() ) {
						edu.cmu.cs.dennisc.print.PrintUtilities.println( "isVisible:", component.getAwtComponent().getClass() );
						return cls.cast( component );
					} else {
						//pass
					}
				}
			}
		}
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "return null" );
		return null;
	}
	@Deprecated
	public JComponent< ? > getFirstComponent() {
		JComponent< ? > rv = getFirstComponent( JComponent.class );
//		assert rv != null;
//		if( rv.isInView() ) {
//			//pass
//		} else {
//			edu.cmu.cs.dennisc.print.PrintUtilities.println( "not in view:", rv );
//			edu.cmu.cs.dennisc.print.PrintUtilities.println( "components:", this.components );
//			edu.cmu.cs.dennisc.print.PrintUtilities.println( "hint:", this.firstComponentHint );
//		}
		return rv;
	}
	@Deprecated
	public void setFirstComponentHint( JComponent< ? > firstComponentHint ) {
//		Thread.dumpStack();
		assert this.components.contains( firstComponentHint );
		if( this.firstComponentHint != firstComponentHint ) {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "prevFirstComponentHint", this.firstComponentHint );
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "nextFirstComponentHint", firstComponentHint );
			this.firstComponentHint = firstComponentHint;
		}
	}
	public java.awt.Shape getShape( edu.cmu.cs.dennisc.croquet.Component< ? > asSeenBy, java.awt.Insets insets ) {
		Component< ? > component = this.getFirstComponent();
		if( component != null ) {
			return component.getShape( asSeenBy, insets );
		} else {
			return null;
		}
	}
	public java.awt.Shape getVisibleShape( edu.cmu.cs.dennisc.croquet.Component< ? > asSeenBy, java.awt.Insets insets ) {
		Component< ? > component = this.getFirstComponent();
		if( component != null ) {
			return component.getVisibleShape( asSeenBy, insets );
		} else {
			return null;
		}
	}
	public ScrollPane getScrollPaneAncestor() {
		Component< ? > component = this.getFirstComponent();
		if( component != null ) {
			return component.getScrollPaneAncestor();
		} else {
			return null;
		}
	}
	public boolean isInView() {
		Component< ? > component = this.getFirstComponent();
		if( component != null ) {
			return component.isInView();
		} else {
			return false;
		}
	}
//	@Deprecated
//	public <J extends Component<?>> J getFirstComponent( Class<J> cls ) {
//		for( Component<?> component : this.components ) {
//			if( cls.isAssignableFrom( component.getClass() ) ) {
//				return (J)component;
//			}
//		}
//		return null;
//	}
	
//	protected abstract void perform( C context );
//	protected abstract C createContext( CompositeContext parentContext, java.util.EventObject e, CancelEffectiveness cancelEffectiveness );
//	protected final void performAsChildInCurrentContext( java.util.EventObject e, CancelEffectiveness cancelEffectiveness ) {
//		CompositeContext parentContext = Application.getSingleton().getCurrentCompositeContext();
//		C context = this.createContext( parentContext, e, cancelEffectiveness );
//		parentContext.performAsChild( this, context );
//	}
}

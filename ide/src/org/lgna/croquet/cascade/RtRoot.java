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

package org.lgna.croquet.cascade;

import org.lgna.croquet.*;
import org.lgna.croquet.components.*;

/**
 * @author Dennis Cosgrove
 */
abstract class RtElement<E extends Element> {
	private final E element;

	public RtElement( E element ) {
		assert element != null;
		this.element = element;
	}
	public E getElement() {
		return this.element;
	}
}

abstract class RtNode<M extends Element, N extends org.lgna.croquet.history.Node< ? >> extends RtElement< M > {
	private final N node;
	private RtNode< ?, ? > parent;
	private RtNode< ?, ? > nextSibling;

	public RtNode( M element, N node ) {
		super( element );
		assert node != null : element;
		this.node = node;
	}
	public N getNode() {
		return this.node;
	}
	protected RtNode< ?, ? > getParent() {
		return this.parent;
	}
	protected RtNode< ?, ? > getNextSibling() {
		return this.nextSibling;
	}
	public void setParent( RtNode< ?, ? > parent ) {
		this.parent = parent;
	}
	public void setNextSibling( RtNode< ?, ? > nextSibling ) {
		this.nextSibling = nextSibling;
	}

	protected void updateParentsAndNextSiblings( RtNode< ?, ? >[] rtNodes ) {
		for( RtNode< ?, ? > rtNode : rtNodes ) {
			rtNode.setParent( this );
		}
		if( rtNodes.length > 0 ) {
			RtNode< ?, ? > rtNodeA = rtNodes[ 0 ];
			for( int i = 1; i < rtNodes.length; i++ ) {
				RtNode< ?, ? > rtNodeB = rtNodes[ i ];
				rtNodeA.setNextSibling( rtNodeB );
				rtNodeA = rtNodeB;
			}
			rtNodeA.setNextSibling( null );
		}
	}

	public RtRoot< ?,? > getRtRoot() {
		return this.parent.getRtRoot();
	}

	protected abstract RtNode[] getChildren();
	protected abstract RtNode< ? extends Element, ? extends org.lgna.croquet.cascade.CascadeNode< ?, ? > > getNextNode();
	public abstract RtBlank< ? > getNearestBlank();
	public RtBlank< ? > getNextBlank() {
		RtBlank< ? > blank = this.getNearestBlank();
		if( blank != null && blank.getNextSibling() != null ) {
			return (RtBlank< ? >)blank.getNextSibling();
		} else {
			if( this.parent != null ) {
				return this.parent.getNextBlank();
			} else {
				return null;
			}
		}
	}
	protected void addNextNodeMenuItems( MenuItemContainer parent ) {
		RtNode nextNode = this.getNextNode();
		RtNode[] children = nextNode.getChildren();
		for( RtNode child : children ) {
			assert child instanceof RtBlank == false;
			RtItem< ?, ?, ?, ? > rtItem = (RtItem< ?, ?, ?, ? >)child;
			ViewController< ?, ? > menuItem = rtItem.getMenuItem();
			if( menuItem != null ) {
				if( menuItem instanceof CascadeMenu ) {
					parent.addCascadeMenu( (CascadeMenu)menuItem );
				} else if( menuItem instanceof CascadeMenuItem ) {
					parent.addCascadeMenuItem( (CascadeMenuItem)menuItem );
				} else {
					assert false : menuItem;
				}
			} else {
				parent.addSeparator();
			}
		}

		final boolean IS_SPRING_LAYOUT_ATTEMPT_DESIRED = true;
		if( IS_SPRING_LAYOUT_ATTEMPT_DESIRED ) {
			javax.swing.SpringLayout springLayout = null;
			for( RtNode child : children ) {
				RtItem< ?, ?, ?, ? > rtItem = (RtItem< ?, ?, ?, ? >)child;
				CascadeBlankChild owner = rtItem.getOwner();
				int itemCount = owner != null ? owner.getItemCount() : 0;
				if( itemCount > 1 ) {
					springLayout = new javax.swing.SpringLayout();
					break;
				}
			}
			javax.swing.JComponent jParent = parent.getViewController().getAwtComponent();
			if( jParent instanceof javax.swing.JMenu ) {
				javax.swing.JMenu jMenu = (javax.swing.JMenu)jParent;
				jParent = jMenu.getPopupMenu();
			}
			assert jParent instanceof javax.swing.JPopupMenu : jParent;
			if( springLayout != null ) {
				jParent.setLayout( springLayout );

				CascadeBlank blank = (CascadeBlank)nextNode.getElement();
				CascadeBlankChild[] blankChildren = blank.getFilteredChildren( (BlankNode)nextNode.getNode() );

				javax.swing.Spring widthA = javax.swing.Spring.constant( 0 );
				javax.swing.Spring widthB = javax.swing.Spring.constant( 0 );
				int index;

				index = 0;
				for( CascadeBlankChild child : blankChildren ) {
					java.awt.Component componentA = jParent.getComponent( index + 0 );
					javax.swing.SpringLayout.Constraints contraintsA = springLayout.getConstraints( componentA );
					widthA = javax.swing.Spring.max( widthA, contraintsA.getWidth() );
					int itemCount = child.getItemCount();
					if( itemCount == 2 ) {
						java.awt.Component componentB = jParent.getComponent( index + 1 );
						javax.swing.SpringLayout.Constraints contraintsB = springLayout.getConstraints( componentB );

						widthB = javax.swing.Spring.max( widthB, contraintsB.getWidth() );
						//todo: try to find a better way to account for the wasted icon/text space
						if( componentB instanceof javax.swing.JMenu ) {
							javax.swing.JMenu jMenu = (javax.swing.JMenu)componentB;
							if( jMenu.getText() != null || jMenu.getIcon() != null ) {
								//pass
							} else {
								//widthB = Spring.constant( 24 );
								widthB = contraintsA.getHeight();
							}
						}
					}
					index += itemCount;
				}
				javax.swing.Spring xA = javax.swing.Spring.constant( 0 );
				javax.swing.Spring xB = javax.swing.Spring.sum( xA, widthA );
				javax.swing.Spring widthAB = javax.swing.Spring.sum( xB, widthB );

				javax.swing.Spring y = javax.swing.Spring.constant( 0 );
				index = 0;
				for( CascadeBlankChild child : blankChildren ) {

					java.awt.Component componentA = jParent.getComponent( index + 0 );
					javax.swing.SpringLayout.Constraints contraintsA = springLayout.getConstraints( componentA );

					contraintsA.setX( xA );
					contraintsA.setY( y );

					javax.swing.Spring height;
					int itemCount = child.getItemCount();
					if( itemCount == 1 ) {
						contraintsA.setWidth( widthAB );
						height = contraintsA.getHeight();
					} else {
						assert itemCount == 2;
						contraintsA.setWidth( widthA );
						java.awt.Component componentB = jParent.getComponent( index + 1 );
						javax.swing.SpringLayout.Constraints contraintsB = springLayout.getConstraints( componentB );
						contraintsB.setX( xB );
						contraintsB.setY( y );
						contraintsB.setWidth( widthB );
						height = javax.swing.Spring.max( contraintsA.getHeight(), contraintsB.getHeight() );
						contraintsA.setHeight( height );
						contraintsB.setHeight( height );
					}
					y = javax.swing.Spring.sum( y, height );
					index += itemCount;
				}
				javax.swing.SpringLayout.Constraints pCons = springLayout.getConstraints( jParent );
				pCons.setConstraint( javax.swing.SpringLayout.SOUTH, y );
				pCons.setConstraint( javax.swing.SpringLayout.EAST, widthAB );
			}
		}
	}
	protected void removeAll( MenuItemContainer parent ) {
		parent.removeAllMenuItems();
	}
}

class RtBlank<B> extends RtNode< CascadeBlank< B >, org.lgna.croquet.cascade.BlankNode< B > > {
	private static boolean isDevoidOfNonSeparators( java.util.List< RtItem > rtItems ) {
		for( RtItem rtItem : rtItems ) {
			if( rtItem instanceof RtSeparator ) {
				//pass
			} else {
				return false;
			}
		}
		return true;
	}

	private RtItem[] rtItems;
	private RtItem< B, ?, ?, ? > rtSelectedFillIn;

	public RtBlank( CascadeBlank< B > element ) {
		super( element, BlankNode.createInstance( element ) );
		this.getNode().setRtBlank( this );
	}
	
	public org.lgna.croquet.cascade.AbstractItemNode getSelectedFillInContext() {
		if( this.rtSelectedFillIn != null ) {
			return this.rtSelectedFillIn.getNode();
		} else {
			return null;
		}
	}

	@Override
	protected RtItem[] getChildren() {
		if( this.rtItems != null ) {
			//pass
		} else {
			java.util.List< RtItem > baseRtItems = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			for( CascadeBlankChild blankChild : this.getElement().getFilteredChildren( this.getNode() ) ) {
				final int N = blankChild.getItemCount();
				for( int i = 0; i < N; i++ ) {
					CascadeItem item = blankChild.getItemAt( i );
					RtItem rtItem;
					if( item instanceof CascadeMenuModel ) {
						CascadeMenuModel menu = (CascadeMenuModel)item;
						rtItem = new RtMenu< B >( menu, blankChild, i );
					} else if( item instanceof CascadeFillIn ) {
						CascadeFillIn fillIn = (CascadeFillIn)item;
						rtItem = new RtFillIn( fillIn, blankChild, i );
						//					} else if( item instanceof CascadeRoot ) {
						//						CascadeRoot root = (CascadeRoot)item;
						//						rtItem = new RtRoot( root );
					} else if( item instanceof CascadeSeparator ) {
						CascadeSeparator separator = (CascadeSeparator)item;
						rtItem = new RtSeparator( separator, blankChild, i );
					} else if( item instanceof CascadeCancel ) {
						CascadeCancel cancel = (CascadeCancel)item;
						rtItem = new RtCancel( cancel, blankChild, i );
					} else {
						rtItem = null;
					}
					baseRtItems.add( rtItem );
				}
			}

			if( isDevoidOfNonSeparators( baseRtItems ) ) {
				baseRtItems.add( new RtCancel( CascadeUnfilledInCancel.getInstance(), null, -1 ) );
			}

			this.rtItems = edu.cmu.cs.dennisc.java.lang.ArrayUtilities.createArray( baseRtItems, RtItem.class );
			this.updateParentsAndNextSiblings( this.rtItems );
		}
		return this.rtItems;
	}
	@Override
	protected RtNode< ? extends Element, ? extends org.lgna.croquet.cascade.CascadeNode< ?, ? > > getNextNode() {
		return this;
	}
	@Override
	public RtBlank< ? > getNearestBlank() {
		return this;
	}

	public void setSelectedFillIn( RtItem< B, ?, ?, ? > item ) {
		this.rtSelectedFillIn = item;
		RtNode parent = this.getParent();
		if( parent instanceof RtFillIn< ?, ? > ) {
			RtFillIn< ?, ? > parentFillIn = (RtFillIn< ?, ? >)parent;
			for( RtBlank blank : parentFillIn.getChildren() ) {
				if( blank.rtSelectedFillIn != null ) {
					//pass
				} else {
					return;
				}
			}
			parentFillIn.select();
		}
	}

	private RtFillIn getOneAndOnlyOneFillInIfAppropriate() {
		RtFillIn rv = null;
		RtItem[] children = this.getChildren();
		for( RtItem child : children ) {
			if( child instanceof RtFillIn ) {
				if( rv != null ) {
					return null;
				} else {
					rv = (RtFillIn)child;
				}
			} else if( child instanceof RtCancel ) {
				return null;
			} else if( child instanceof RtMenu ) {
				return null;
			} else if( child instanceof RtRoot ) {
				//??
				return null;
			} else if( child instanceof RtSeparator ) {
				//pass
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "unhandled child", child );
				return null;
			}
		}
		return rv;
	}
	
	public boolean isFillInAlreadyDetermined() {
		RtFillIn rtFillIn = this.getOneAndOnlyOneFillInIfAppropriate();
		if( rtFillIn != null && rtFillIn.isAutomaticallySelectedWhenSoleOption() ) {
			this.rtSelectedFillIn = rtFillIn;
		}
		return this.rtSelectedFillIn != null;
	}

	public B createValue() {
		if( this.rtSelectedFillIn != null ) {
			return this.rtSelectedFillIn.createValue();
		} else {
			throw new RuntimeException();
		}
	}
}

abstract class RtItem<F, B, M extends CascadeItem< F, B >, C extends org.lgna.croquet.cascade.AbstractItemNode< F, B, M >> extends RtNode< M, C > {
	private final RtBlank< B >[] rtBlanks;
	private final CascadeBlankChild< F > owner;
	private final int index;
	//	private javax.swing.JMenuItem menuItem = null;
	private ViewController< ?, ? > menuItem = null;
	private boolean wasLast = false;

	public RtItem( M element, C node, CascadeBlankChild< F > owner, int index ) {
		super( element, node );
		this.owner = owner;
		this.index = index;
		CascadeBlank< B >[] blanks = this.getModelBlanks();
		final int N;
		if( blanks != null ) {
			N = blanks.length;
		} else {
			N = 0;
		}
		this.rtBlanks = new RtBlank[ N ];
		for( int i = 0; i < this.rtBlanks.length; i++ ) {
			assert blanks[ i ] != null : this;
			this.rtBlanks[ i ] = new RtBlank< B >( blanks[ i ] );
		}
		this.updateParentsAndNextSiblings( this.rtBlanks );
	}

	public CascadeBlankChild< F > getOwner() {
		return this.owner;
	}
	public int getIndex() {
		return this.index;
	}
	protected abstract CascadeBlank< B >[] getModelBlanks();

	public int getBlankStepCount() {
		return this.rtBlanks.length;
	}
	public org.lgna.croquet.cascade.BlankNode< B > getBlankStepAt( int i ) {
		return this.rtBlanks[ i ].getNode();
	}
	@Override
	public RtBlank< ? > getNearestBlank() {
		RtNode< ?, ? > parent = this.getParent();
		return parent.getNearestBlank();
	}
	@Override
	protected RtBlank< B >[] getChildren() {
		return this.rtBlanks;
	}
	public boolean isGoodToGo() {
		if( this.rtBlanks.length > 0 ) {
			for( RtBlank< B > rtBlank : this.rtBlanks ) {
				if( rtBlank.isFillInAlreadyDetermined() ) {
					//pass
				} else {
					return false;
				}
			}
		}
		return true;
	}
	@Override
	protected RtNode< ? extends Element, ? extends org.lgna.croquet.cascade.CascadeNode< ?, ? > > getNextNode() {
		if( this.rtBlanks.length > 0 ) {
			return this.rtBlanks[ 0 ];
		} else {
			return this.getNextBlank();
		}
	}
	public F createValue() {
		return this.getElement().createValue( this.getNode() );
	}
	protected boolean isLast() {
		return this.getNextNode() == null;
	}
	public void select() {
		//todo
		RtBlank< ? > nearestBlank = this.getNearestBlank();
		assert nearestBlank != null : this;
		nearestBlank.setSelectedFillIn( (RtItem)this );
		//		ContextManager.pushContext( this.getContext() );
	}
	public void deselect() {
		//		AbstractModelContext< ? > stepFillIn = ContextManager.popContext();
		//		assert stepFillIn == this.getContext() : stepFillIn;
	}

	private java.awt.event.ActionListener actionListener = new java.awt.event.ActionListener() {
		public void actionPerformed( java.awt.event.ActionEvent e ) {
			RtItem.this.select();
			RtItem.this.getRtRoot().handleActionPerformed( e );
		}
	};
	private javax.swing.event.MenuListener menuListener = new javax.swing.event.MenuListener() {
		public void menuSelected( javax.swing.event.MenuEvent e ) {
			RtItem.this.addNextNodeMenuItems( (CascadeMenu)RtItem.this.getMenuItem() );
			RtItem.this.select();
		}
		public void menuDeselected( javax.swing.event.MenuEvent e ) {
			RtItem.this.deselect();
			RtItem.this.removeAll( (CascadeMenu)RtItem.this.getMenuItem() );
		}
		public void menuCanceled( javax.swing.event.MenuEvent e ) {
		}
	};

	protected ViewController< ?, ? > createMenuItem( CascadeItem< F, B > item, boolean isLast ) {
		ViewController< ?, ? > rv;
		javax.swing.JMenuItem jMenuItem;
		if( isLast ) {
			CascadeMenuItem menuItem = new CascadeMenuItem( item );
			menuItem.getAwtComponent().addActionListener( this.actionListener );
			jMenuItem = menuItem.getAwtComponent();
			rv = menuItem;
		} else {
			CascadeMenu menu = new CascadeMenu( item );
			menu.getAwtComponent().addMenuListener( this.menuListener );
			jMenuItem = menu.getAwtComponent();
			rv = menu;
		}
		//		String text = item.getMenuItemText( this.getStep() );
		//		jMenuItem.setText( text != null ? text : "" );
		jMenuItem.setText( item.getMenuItemText( this.getNode() ) );
		jMenuItem.setIcon( item.getMenuItemIcon( this.getNode() ) );
		return rv;
	}
	public ViewController< ?, ? > getMenuItem() {
		CascadeItem< F, B > item = this.getElement();
		boolean isLast = this.isLast();
		if( this.menuItem != null ) {
			if( this.wasLast == isLast ) {
				//pass
			} else {
				if( this.menuItem instanceof Menu ) {
					((Menu)this.menuItem).getAwtComponent().removeMenuListener( this.menuListener );
				} else if( this.menuItem instanceof CascadeMenuItem ) {
					((CascadeMenuItem)this.menuItem).getAwtComponent().removeActionListener( this.actionListener );
				}
				this.menuItem = null;
			}
		} else {
			//pass
		}
		if( this.menuItem != null ) {
			//pass
		} else {
			this.menuItem = this.createMenuItem( item, isLast );
		}
		return this.menuItem;
	}
}

abstract class RtBlankOwner<F, B, M extends CascadeBlankOwner< F, B >, C extends org.lgna.croquet.cascade.BlankOwnerNode< F, B, M >> extends RtItem< F, B, M, C > {
	public RtBlankOwner( M element, C step, CascadeBlankChild< F > owner, int index ) {
		super( element, step, owner, index );
		this.getNode().setRtBlankOwner( this );
	}
	@Override
	protected final CascadeBlank< B >[] getModelBlanks() {
		return this.getElement().getBlanks();
	}
}

class RtFillIn<F, B> extends RtBlankOwner< F, B, CascadeFillIn< F, B >, org.lgna.croquet.cascade.FillInNode< F, B > > {
	public RtFillIn( CascadeFillIn< F, B > element, CascadeBlankChild< F > owner, int index ) {
		super( element, FillInNode.createInstance( element ), owner, index );
	}
	public boolean isAutomaticallySelectedWhenSoleOption() {
		return this.getElement().isAutomaticallySelectedWhenSoleOption();
	}
}

class RtMenu<FB> extends RtBlankOwner< FB, FB, CascadeMenuModel< FB >, org.lgna.croquet.cascade.MenuNode< FB >> {
	public RtMenu( CascadeMenuModel< FB > element, CascadeBlankChild< FB > owner, int index ) {
		super( element, MenuNode.createInstance( element ), owner, index );
	}
}

class RtSeparator extends RtItem< Void, Void, CascadeSeparator, org.lgna.croquet.cascade.SeparatorNode > {
	public RtSeparator( CascadeSeparator element, CascadeBlankChild< Void > owner, int index ) {
		super( element, SeparatorNode.createInstance( element ), owner, index );
	}
	@Override
	protected CascadeBlank< Void >[] getModelBlanks() {
		return new CascadeBlank[] {};
	}
	@Override
	public final RtBlank getNearestBlank() {
		return null;
	}
	@Override
	protected final RtNode getNextNode() {
		return null;
	}
	@Override
	protected ViewController< ?, ? > createMenuItem( CascadeItem< Void, Void > item, boolean isLast ) {
		//todo
		if( item.getMenuItemText( null ) != null || item.getMenuItemIcon( null ) != null ) {
			ViewController< ?, ? > rv = super.createMenuItem( item, isLast );
			rv.getAwtComponent().setEnabled( false );
			return rv;
		} else {
			return null;
		}
	}
}

class RtCancel<F> extends RtItem< F, Void, CascadeCancel< F >, org.lgna.croquet.cascade.CancelNode< F > > {
	public RtCancel( CascadeCancel< F > element, CascadeBlankChild< F > owner, int index ) {
		super( element, CancelNode.createInstance( element ), owner, index );
	}
	@Override
	protected CascadeBlank< Void >[] getModelBlanks() {
		return new CascadeBlank[] {};
	}
}

/**
 * @author Dennis Cosgrove
 */
public class RtRoot<T,CS extends org.lgna.croquet.history.CompletionStep<?>> extends RtBlankOwner< T[], T, CascadeRoot< T, CS >, RootNode< T,CS > > {
	public RtRoot( CascadeRoot< T,CS > element ) {
		super( element, RootNode.createInstance( element ), null, -1 );
	}
	@Override
	public RtRoot< T,CS > getRtRoot() {
		return this;
	}
	@Override
	public RtBlank< ? > getNearestBlank() {
		return null;
	}
	@Override
	public void select() {
	}

	protected T[] createValues( Class< T > componentType ) {
		RtBlank< T >[] rtBlanks = this.getChildren();
		T[] rv = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newTypedArrayInstance( componentType, rtBlanks.length );
		for( int i = 0; i < rtBlanks.length; i++ ) {
			rv[ i ] = rtBlanks[ i ].createValue();
		}
		return rv;
	}

	public void cancel( CS completionStep, org.lgna.croquet.triggers.Trigger trigger, CancelException ce ) {
		this.getElement().handleCancel( completionStep, trigger, ce );
	}

	public CS complete( org.lgna.croquet.triggers.Trigger trigger ) {
		CascadeRoot< T,CS > root = this.getElement();
		CS completionStep = root.createCompletionStep( trigger );
		try {
			T[] values = this.createValues( root.getComponentType() );
			root.handleCompletion( completionStep, values );
		} catch( CancelException ce ) {
			this.cancel( completionStep, trigger, ce );
		}
		return completionStep;
	}
	protected void handleActionPerformed( java.awt.event.ActionEvent e ) {
		this.complete( new org.lgna.croquet.triggers.ActionEventTrigger( e ) );
	}

	public javax.swing.event.PopupMenuListener createPopupMenuListener( final MenuItemContainer menuItemContainer ) {
		return new javax.swing.event.PopupMenuListener() {
			public void popupMenuWillBecomeVisible( javax.swing.event.PopupMenuEvent e ) {
				RtRoot.this.addNextNodeMenuItems( menuItemContainer );
			}
			public void popupMenuWillBecomeInvisible( javax.swing.event.PopupMenuEvent e ) {
				RtRoot.this.removeAll( menuItemContainer );
			}
			public void popupMenuCanceled( javax.swing.event.PopupMenuEvent e ) {
				RtRoot.this.cancel( null, new org.lgna.croquet.triggers.PopupMenuEventTrigger( e ), null );
			}
		};
	}
}

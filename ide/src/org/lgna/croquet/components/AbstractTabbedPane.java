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

package org.lgna.croquet.components;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractTabbedPane<E extends org.lgna.croquet.TabComposite<?>> extends ItemSelectablePanel<E> {
	private final javax.swing.event.ListSelectionListener listSelectionListener = new javax.swing.event.ListSelectionListener() {
		public void valueChanged( javax.swing.event.ListSelectionEvent e ) {
			if( e.getValueIsAdjusting() ) {
				//pass
			} else {
				org.lgna.croquet.ListSelectionState<E> model = getModel();
				int indexFromSwingModel = model.getSwingModel().getListSelectionModel().getLeadSelectionIndex();
				E cardFromSwingModel = (E)model.getSwingModel().getComboBoxModel().getElementAt( indexFromSwingModel );
				AbstractTabbedPane.this.handleValueChanged( cardFromSwingModel );
			}
		}
	};

	public AbstractTabbedPane( org.lgna.croquet.ListSelectionState<E> model ) {
		super( model );
	}

	@Override
	public void setFont( java.awt.Font font ) {
		super.setFont( font );
		for( BooleanStateButton<?> button : this.getAllButtons() ) {
			button.setFont( font );
		}
	}

	protected void customizeTitleComponent( org.lgna.croquet.BooleanState booleanState, BooleanStateButton<?> button, E item ) {
		item.customizeTitleComponentAppearance( button );
	}

	protected void releaseTitleComponent( org.lgna.croquet.BooleanState booleanState, BooleanStateButton<?> button, E item ) {
	}

	protected abstract BooleanStateButton<? extends javax.swing.AbstractButton> createTitleButton( E item, org.lgna.croquet.BooleanState itemSelectedState );

	@Override
	protected void handleDisplayable() {
		this.getModel().getSwingModel().getListSelectionModel().addListSelectionListener( this.listSelectionListener );
		this.handleValueChanged( this.getModel().getValue() );
		super.handleDisplayable();
	}

	@Override
	protected void handleUndisplayable() {
		super.handleUndisplayable();
		this.getModel().getSwingModel().getListSelectionModel().removeListSelectionListener( this.listSelectionListener );
	}

	protected abstract void handleValueChanged( E card );

	@Override
	protected org.lgna.croquet.components.BooleanStateButton<?> createButtonForItemSelectedState( final E item, org.lgna.croquet.BooleanState itemSelectedState ) {
		BooleanStateButton<?> rv = this.createTitleButton( item, itemSelectedState );
		this.customizeTitleComponent( itemSelectedState, rv, item );
		return rv;

	}

	public JComponent<?> getMainComponentFor( E item ) {
		if( item != null ) {
			return item.getView();
		} else {
			return null;
		}
	}

	public ScrollPane getScrollPaneFor( E item ) {
		if( item != null ) {
			return item.getScrollPaneIfItExists();
		} else {
			return null;
		}
	}

	public JComponent<?> getRootComponentFor( E item ) {
		if( item != null ) {
			return item.getRootComponent();
		} else {
			return null;
		}
	}
}

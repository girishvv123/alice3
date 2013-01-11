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
package org.alice.ide.custom.components;

/**
 * @author Dennis Cosgrove
 */
public class ArrayCustomExpressionCreatorView extends RowBasedCustomExpressionCreatorView {
	private static abstract class ItemAtIndexCascade<T> extends org.lgna.croquet.CascadeWithInternalBlank<T> {
		private final org.lgna.croquet.data.ListData<T> data;
		private final int index;

		public ItemAtIndexCascade( org.lgna.croquet.Group group, java.util.UUID migrationId, org.lgna.croquet.data.ListData<T> data, int index ) {
			super( group, migrationId, data.getItemCodec().getValueClass() );
			this.data = data;
			this.index = index;
		}

		public org.lgna.croquet.data.ListData<T> getData() {
			return this.data;
		}

		public int getIndex() {
			return this.index;
		}

		@Override
		protected org.lgna.croquet.edits.Edit<? extends org.lgna.croquet.Cascade<T>> createEdit( org.lgna.croquet.history.CompletionStep<org.lgna.croquet.Cascade<T>> completionStep, T[] values ) {
			T[] items = this.data.toArray();
			items[ this.index ] = values[ 0 ];
			this.data.internalSetAllItems( items );
			return null;
		}
	}

	private static class ExpressionAtIndexCascade extends ItemAtIndexCascade<org.lgna.project.ast.Expression> {
		private final org.lgna.project.ast.AbstractType<?, ?, ?> componentType;

		public ExpressionAtIndexCascade( org.lgna.croquet.data.ListData<org.lgna.project.ast.Expression> data, int index, org.lgna.project.ast.AbstractType<?, ?, ?> componentType ) {
			super( org.lgna.croquet.Application.INHERIT_GROUP, java.util.UUID.fromString( "bbdd16fe-0ea0-41ae-8e09-fde5ee075e06" ), data, index );
			this.componentType = componentType;
		}

		@Override
		protected java.util.List<org.lgna.croquet.CascadeBlankChild> updateBlankChildren( java.util.List<org.lgna.croquet.CascadeBlankChild> rv, org.lgna.croquet.cascade.BlankNode<org.lgna.project.ast.Expression> blankNode ) {
			org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
			ide.getExpressionCascadeManager().appendItems( rv, blankNode, this.componentType, null );
			return rv;
		}
	}

	private static class DeleteItemAtIndexOperation<T> extends org.lgna.croquet.ActionOperation {
		private final org.lgna.croquet.data.ListData<T> data;
		private final int index;

		public DeleteItemAtIndexOperation( org.lgna.croquet.data.ListData<T> data, int index ) {
			super( org.lgna.croquet.Application.INHERIT_GROUP, java.util.UUID.fromString( "138a9eae-eeb8-40ab-972a-64569a9e16e8" ) );
			this.data = data;
			this.index = index;
		}

		public org.lgna.croquet.data.ListData<T> getData() {
			return this.data;
		}

		public int getIndex() {
			return this.index;
		}

		@Override
		protected void perform( org.lgna.croquet.history.Transaction transaction, org.lgna.croquet.triggers.Trigger trigger ) {
			org.lgna.croquet.history.CompletionStep<?> step = transaction.createAndSetCompletionStep( this, trigger );
			this.data.internalRemoveItem( this.data.getItemAt( this.index ) );
			step.finish();
		}
	}

	private static class ExpressionDropDown extends org.lgna.croquet.components.DropDown<org.lgna.croquet.PopupPrepModel> {
		private class MainComponent extends org.lgna.croquet.components.BorderPanel {
			private final org.alice.ide.x.AstI18nFactory factory;

			public MainComponent( org.alice.ide.x.AstI18nFactory factory ) {
				this.factory = factory;
			}

			@Override
			protected void internalRefresh() {
				super.internalRefresh();
				this.forgetAndRemoveAllComponents();
				org.lgna.project.ast.Expression expression = cascade.getData().getItemAt( cascade.getIndex() );
				this.addLineStartComponent( factory.createExpressionPane( expression ) );
				this.addCenterComponent( new org.lgna.croquet.components.Label() );
				this.revalidateAndRepaint();
			}
		};

		private final ExpressionAtIndexCascade cascade;
		private final MainComponent mainComponent;

		public ExpressionDropDown( ExpressionAtIndexCascade cascade, org.alice.ide.x.AstI18nFactory factory ) {
			super( cascade.getRoot().getPopupPrepModel() );
			this.cascade = cascade;
			this.mainComponent = new MainComponent( factory );
			this.setMainComponent( this.mainComponent );
			this.setMaximumSizeClampedToPreferredSize( true );
		}

		public ExpressionAtIndexCascade getCascade() {
			return this.cascade;
		}

		public void refreshInternalLater() {
			this.mainComponent.refreshLater();
		}
	};

	private static class ExpressionList extends org.lgna.croquet.components.MutableList<org.lgna.project.ast.Expression> {
		private final org.lgna.project.ast.AbstractType<?, ?, ?> componentType;

		private class JExpressionItemAtIndexButton extends JItemAtIndexButton {
			private final javax.swing.JLabel prefixLabel = new javax.swing.JLabel() {
				@Override
				public java.awt.Color getForeground() {
					return JExpressionItemAtIndexButton.this.getModel().isSelected() ? java.awt.Color.WHITE : java.awt.Color.BLACK;
				}
			};
			private final ExpressionDropDown expressionDropDown;

			public JExpressionItemAtIndexButton( int index, javax.swing.Action deleteAction ) {
				this.prefixLabel.setText( "[" + index + "]" );
				edu.cmu.cs.dennisc.java.awt.font.FontUtilities.setFontToDerivedFont( this.prefixLabel, edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD );

				ExpressionAtIndexCascade cascade = new ExpressionAtIndexCascade( ExpressionList.this.getData(), index, ExpressionList.this.componentType );
				this.expressionDropDown = new ExpressionDropDown( cascade, org.alice.ide.x.DialogAstI18nFactory.getInstance() );

				this.setLayout( new net.miginfocom.swing.MigLayout( "insets 0", "[][]push[]" ) );
				this.add( this.prefixLabel );
				this.add( this.expressionDropDown.getAwtComponent() );
				if( deleteAction != null ) {
					javax.swing.JButton closeButton = new edu.cmu.cs.dennisc.javax.swing.components.JCloseButton( true );
					closeButton.setAction( deleteAction );
					this.add( closeButton, "aligny top" );
				}
			}

			@Override
			public void update() {
				this.expressionDropDown.refreshInternalLater();
			}
		}

		public ExpressionList( org.lgna.croquet.data.MutableListData<org.lgna.project.ast.Expression> data, org.lgna.project.ast.AbstractType<?, ?, ?> componentType ) {
			super( (org.lgna.croquet.data.MutableListData<org.lgna.project.ast.Expression>)data );
			this.componentType = componentType;
		}

		@Override
		protected JExpressionItemAtIndexButton createJItemAtIndexButton( int index ) {
			DeleteItemAtIndexOperation<org.lgna.project.ast.Expression> deleteOperation = new DeleteItemAtIndexOperation<org.lgna.project.ast.Expression>( this.getData(), index );
			return new JExpressionItemAtIndexButton( index, deleteOperation.getSwingModel().getAction() );
		}

		@Override
		protected void handleDisplayable() {
			super.handleDisplayable();
			this.registerKeyboardActions();
		}

		@Override
		protected void handleUndisplayable() {
			this.unregisterKeyboardActions();
			super.handleUndisplayable();
		}
	}

	private java.awt.event.MouseListener mouseListener = new java.awt.event.MouseListener() {
		public void mousePressed( java.awt.event.MouseEvent e ) {
			expressionList.clearSelection();
		}

		public void mouseReleased( java.awt.event.MouseEvent e ) {
		}

		public void mouseEntered( java.awt.event.MouseEvent e ) {
		}

		public void mouseExited( java.awt.event.MouseEvent e ) {
		}

		public void mouseClicked( java.awt.event.MouseEvent e ) {
		}
	};

	private final ExpressionList expressionList;

	public ArrayCustomExpressionCreatorView( org.alice.ide.custom.ArrayCustomExpressionCreatorComposite composite ) {
		super( composite );
		this.expressionList = new ExpressionList( composite.getData(), composite.getArrayType().getComponentType() );
		this.expressionList.setBackgroundColor( this.getBackgroundColor() );
	}

	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		this.addMouseListener( this.mouseListener );
	}

	@Override
	protected void handleUndisplayable() {
		this.removeMouseListener( this.mouseListener );
		super.handleUndisplayable();
	}

	@Override
	protected void appendRows( java.util.List<org.lgna.croquet.components.LabeledFormRow> rows ) {
		org.alice.ide.custom.ArrayCustomExpressionCreatorComposite composite = (org.alice.ide.custom.ArrayCustomExpressionCreatorComposite)this.getComposite();
		rows.add( new org.lgna.croquet.components.LabeledFormRow( composite.getArrayTypeLabel(), new org.lgna.croquet.components.Label( org.alice.ide.common.TypeIcon.getInstance( composite.getArrayType() ) ) ) );

		org.lgna.croquet.components.ScrollPane scrollPane = new org.lgna.croquet.components.ScrollPane( this.expressionList );
		org.lgna.croquet.components.PopupButton popupButton = composite.getAddItemCascade().getRoot().getPopupPrepModel().createPopupButton();
		scrollPane.setAlignmentX( 0.0f );
		popupButton.setAlignmentX( 0.0f );
		rows.add(
				new org.lgna.croquet.components.LabeledFormRow(
						composite.getValueLabel(),
						new org.lgna.croquet.components.PageAxisPanel( scrollPane, popupButton )
						, org.lgna.croquet.components.VerticalAlignment.TOP
				)
				);
	}
}

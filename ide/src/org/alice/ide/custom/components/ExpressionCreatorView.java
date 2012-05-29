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
public abstract class ExpressionCreatorView extends org.alice.ide.preview.components.PanelWithPreview {
	public ExpressionCreatorView( org.alice.ide.custom.ExpressionCreatorComposite<?> composite ) {
		super( composite );
	}
	
	private org.lgna.project.ast.Expression createValue() {
		org.alice.ide.custom.ExpressionCreatorComposite<?> composite = (org.alice.ide.custom.ExpressionCreatorComposite<?>)this.getComposite();
		return composite.getPreviewValue();
	}
	
	@Override
	public org.lgna.croquet.components.JComponent< ? > createPreviewSubComponent() {
		org.lgna.project.ast.Expression expression;
		try {
			expression = this.createValue();
		} catch( RuntimeException re ) {
			//re.printStackTrace();
			expression = new org.lgna.project.ast.NullLiteral();
		}
		org.lgna.croquet.components.BorderPanel rv = new org.lgna.croquet.components.BorderPanel();
		rv.addComponent( org.alice.ide.x.PreviewAstI18nFactory.getInstance().createExpressionPane( expression ), org.lgna.croquet.components.BorderPanel.Constraint.LINE_START );
		return rv;
	}
	

	protected org.lgna.croquet.components.Component<?> createLabel( String text ) {
		return org.lgna.croquet.components.SpringUtilities.createTrailingLabel( text );
	}
	private static final String[] LABEL_TEXTS = { "value:" };
	protected String[] getLabelTexts() {
		return LABEL_TEXTS;
	}
	protected abstract org.lgna.croquet.components.Component< ? >[] getRowComponents();
	public java.util.List< org.lgna.croquet.components.Component< ? >[] > updateRows( java.util.List< org.lgna.croquet.components.Component< ? >[] > rv ) {
		String[] labelTexts = this.getLabelTexts();
		org.lgna.croquet.components.Component< ? >[] components = this.getRowComponents();
		final int N = labelTexts.length;
		for( int i=0; i<N; i++ ) {
			rv.add( 
					org.lgna.croquet.components.SpringUtilities.createRow( 
						this.createLabel( labelTexts[ i ] ), 
						new org.lgna.croquet.components.LineAxisPanel( 
								components[ i ],
								org.lgna.croquet.components.BoxUtilities.createHorizontalGlue()
						)
					) 
			);
		}
		return rv;
	}
	@Override
	public org.lgna.croquet.components.RowsSpringPanel createMainComponent() {
		org.lgna.croquet.components.RowsSpringPanel rowsSpringPanel = new org.lgna.croquet.components.RowsSpringPanel() {
			@Override
			protected java.util.List<org.lgna.croquet.components.Component<?>[]> updateComponentRows(java.util.List<org.lgna.croquet.components.Component<?>[]> rv) {
				return ExpressionCreatorView.this.updateRows( rv );
			}
		};
		return rowsSpringPanel;
	}
}

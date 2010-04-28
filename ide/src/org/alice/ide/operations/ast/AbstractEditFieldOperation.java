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
package org.alice.ide.operations.ast;

import edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractEditFieldOperation extends org.alice.ide.operations.AbstractActionOperation {
	public AbstractEditFieldOperation( java.util.UUID groupUUID, String name ) {
		super( groupUUID );
		this.setName( name );
	}
	protected final void perform( edu.cmu.cs.dennisc.zoot.ActionContext actionContext, final FieldDeclaredInAlice field, java.util.Set< FieldDeclaredInAlice > referencedFields, java.util.Set< FieldDeclaredInAlice > reassignedFields ) {
		if( field != null ) {
			final edu.cmu.cs.dennisc.alice.ast.FieldModifierFinalVolatileOrNeither prevFinalVolatileOrNeither = field.finalVolatileOrNeither.getValue();
			final edu.cmu.cs.dennisc.alice.ast.AbstractType prevValueType = field.valueType.getValue();
			final String prevName = field.getName();
			final edu.cmu.cs.dennisc.alice.ast.Expression prevInitializer = field.initializer.getValue();
			org.alice.ide.declarationpanes.EditFieldPane editFieldPane = new org.alice.ide.declarationpanes.EditFieldPane( field, referencedFields.contains( field ), reassignedFields.contains( field ) );
			FieldDeclaredInAlice tempField = editFieldPane.showInJDialog( this.getIDE().getJFrame() );
			if( tempField != null ) {
				final edu.cmu.cs.dennisc.alice.ast.FieldModifierFinalVolatileOrNeither nextFinalVolatileOrNeither = tempField.finalVolatileOrNeither.getValue();
				final edu.cmu.cs.dennisc.alice.ast.AbstractType nextValueType = tempField.valueType.getValue();
				final String nextName = tempField.getName();
				final edu.cmu.cs.dennisc.alice.ast.Expression nextInitializer = tempField.initializer.getValue();
				actionContext.commitAndInvokeDo( new edu.cmu.cs.dennisc.zoot.AbstractEdit() {
					@Override
					public void doOrRedo( boolean isDo ) {
						field.finalVolatileOrNeither.setValue( nextFinalVolatileOrNeither );
						field.valueType.setValue( nextValueType );
						field.name.setValue( nextName );
						field.initializer.setValue( nextInitializer );
					}
					@Override
					public void undo() {
						field.finalVolatileOrNeither.setValue( prevFinalVolatileOrNeither );
						field.valueType.setValue( prevValueType );
						field.name.setValue( prevName );
						field.initializer.setValue( prevInitializer );
					}
					@Override
					protected StringBuffer updatePresentation( StringBuffer rv, java.util.Locale locale ) {
						rv.append( "edit: " );
						edu.cmu.cs.dennisc.alice.ast.Node.safeAppendRepr( rv, field, locale );
						return rv;
					}
				} );
			} else {
				actionContext.cancel();
			}
		} else {
			actionContext.cancel();
		}
	}
}

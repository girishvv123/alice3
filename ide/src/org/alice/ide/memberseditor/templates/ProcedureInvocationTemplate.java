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
package org.alice.ide.memberseditor.templates;

/**
 * @author Dennis Cosgrove
 */
public class ProcedureInvocationTemplate extends ExpressionStatementTemplate {
	private edu.cmu.cs.dennisc.alice.ast.AbstractMethod method;
	private edu.cmu.cs.dennisc.property.event.ListPropertyListener< edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice > parameterAdapter = new edu.cmu.cs.dennisc.property.event.SimplifiedListPropertyAdapter< edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice >() {
		@Override
		protected void changing( edu.cmu.cs.dennisc.property.event.ListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice > e ) {
		}
		@Override
		protected void changed( edu.cmu.cs.dennisc.property.event.ListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice > e ) {
			ProcedureInvocationTemplate.this.refresh();
		}
	};
	/*package-private*/ ProcedureInvocationTemplate( edu.cmu.cs.dennisc.alice.ast.AbstractMethod method ) {
		this.method = method;
		java.util.List< edu.cmu.cs.dennisc.croquet.Model > operations = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		//operations.add( org.alice.ide.IDE.getSingleton().createPreviewOperation( this ) );
		if( method instanceof edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice ) {
			edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice methodInAlice = (edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice)method;
			operations.add( org.alice.ide.croquet.models.ast.rename.RenameMethodOperation.getInstance( methodInAlice ) );
			operations.add( org.alice.ide.operations.ast.FocusCodeOperation.getInstance( methodInAlice ) );
			operations.add( edu.cmu.cs.dennisc.croquet.MenuModel.SEPARATOR );
			operations.add( new org.alice.ide.operations.ast.DeleteMethodOperation( methodInAlice ) );
		}
		this.setPopupMenuOperation( new edu.cmu.cs.dennisc.croquet.DefaultMenuModel( java.util.UUID.fromString( "96831579-1fb6-4c15-a509-ccdcc51458a8" ), operations).getPopupMenuOperation() );
	}
	@Override
	protected void handleAddedTo(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
		super.handleAddedTo( parent );
		if( this.method instanceof edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice ) {
			((edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice)this.method).parameters.addListPropertyListener( this.parameterAdapter );
			this.refresh();
		}
	}
	@Override
	protected void handleRemovedFrom(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
		if( this.method instanceof edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice ) {
			((edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice)this.method).parameters.removeListPropertyListener( this.parameterAdapter );
		}
		super.handleRemovedFrom( parent );
	}
	
	public edu.cmu.cs.dennisc.alice.ast.AbstractMethod getMethod() {
		return this.method;
	}
	
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.Expression createIncompleteExpression() {
		return org.alice.ide.ast.NodeUtilities.createIncompleteMethodInvocation( this.method );
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.AbstractType[] getBlankExpressionTypes() {
		return org.alice.ide.ast.NodeUtilities.getDesiredParameterValueTypes( this.method );
	}
	
	@Override
	protected String getTitleAt( int index ) {
		return this.method.getParameters().get( index ).getName();
	}
	
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.Expression createExpression( edu.cmu.cs.dennisc.alice.ast.Expression... expressions ) {
		edu.cmu.cs.dennisc.alice.ast.MethodInvocation rv = org.alice.ide.ast.NodeUtilities.createIncompleteMethodInvocation( method );
		org.alice.ide.ast.NodeUtilities.completeMethodInvocation( rv, expressions );
		return rv;
	}
}

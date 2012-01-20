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
package org.alice.ide.declarationseditor.events.components;

import org.lgna.croquet.components.BoxUtilities;
import org.lgna.croquet.components.PageAxisPanel;
import org.lgna.project.ast.BlockStatement;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.ExpressionStatement;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.project.ast.NodeProperty;
import org.lgna.project.ast.Statement;
import org.lgna.project.ast.StatementListProperty;
import org.lgna.project.ast.UserCode;

import edu.cmu.cs.dennisc.matt.EventListenerComponent;
import edu.cmu.cs.dennisc.property.event.ListPropertyEvent;
import edu.cmu.cs.dennisc.property.event.ListPropertyListener;
import edu.cmu.cs.dennisc.property.event.SimplifiedListPropertyAdapter;

/**
 * @author Matt May
 */
public class EventsContentPanel extends org.alice.ide.codedrop.CodeDropReceptor {
	private final ListPropertyListener< Statement > statementsListener = new SimplifiedListPropertyAdapter< Statement >() {
		@Override
		protected void changing( ListPropertyEvent< Statement > e ) {
		}
		@Override
		protected void changed( ListPropertyEvent< Statement > e ) {
			EventsContentPanel.this.refreshLater();
		}
	};

	private final org.lgna.project.ast.AbstractCode code;
	private final PageAxisPanel panel = new PageAxisPanel();
	
	public EventsContentPanel( org.lgna.project.ast.AbstractCode code ) {
		this.code = code;
		this.addComponent( this.panel, Constraint.PAGE_START );
		this.panel.setBorder( javax.swing.BorderFactory.createBevelBorder( javax.swing.border.BevelBorder.LOWERED ) );
	}
	
	@Override
	public org.lgna.project.ast.AbstractCode getCode() {
		return this.code;
	}
	@Override
	protected org.lgna.croquet.components.Component< ? > getAsSeenBy() {
		return this;
	}

	@Override
	protected void internalRefresh() {
		super.internalRefresh();
		panel.forgetAndRemoveAllComponents();
		for( Statement statement : getStatements() ) {
			if( statement instanceof ExpressionStatement ) {
				ExpressionStatement expressionStatement = (ExpressionStatement)statement;
				Expression expression = expressionStatement.expression.getValue();
				if( expression instanceof MethodInvocation ) {
					MethodInvocation methodInvocation = (MethodInvocation)expression;
					org.alice.ide.common.AddEventListenerStatementPanel statementPanel = new org.alice.ide.common.AddEventListenerStatementPanel( expressionStatement );
					statementPanel.addComponent( new EventListenerComponent( methodInvocation ) );
					panel.addComponent( statementPanel );
				}
			}
		}
		panel.addComponent( BoxUtilities.createVerticalGlue() );
	}

	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		this.getStatements().addListPropertyListener( this.statementsListener );
		this.refreshLater();
	}
	@Override
	protected void handleUndisplayable() {
		this.getStatements().removeListPropertyListener( this.statementsListener );
		super.handleUndisplayable();
	}

	private StatementListProperty getStatements() {
		return this.getBodyProperty().getValue().statements;
	}

	private NodeProperty< ? extends BlockStatement > getBodyProperty() {
		return ((UserCode)this.code).getBodyProperty();
	}
	
	public <R extends org.lgna.croquet.DropReceptor> org.lgna.croquet.resolvers.CodableResolver< org.lgna.croquet.DropReceptor > getCodableResolver() {
		edu.cmu.cs.dennisc.java.util.logging.Logger.todo();
		return null;
	}
	public org.lgna.croquet.components.TrackableShape getTrackableShape( org.lgna.croquet.DropSite potentialDropSite ) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.todo( potentialDropSite );
		return null;
	}
	public java.lang.String getTutorialNoteText( org.lgna.croquet.Model model, org.lgna.croquet.edits.Edit< ? > edit, org.lgna.croquet.UserInformation userInformation ) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.todo( model, edit );
		return null;
	}
	public org.lgna.croquet.components.JComponent< ? > getViewController() {
		return this.panel;
	}
}

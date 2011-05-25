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
package org.alice.ide.croquet.edits.ast;

public class InsertStatementEdit extends edu.cmu.cs.dennisc.croquet.Edit< edu.cmu.cs.dennisc.croquet.CascadePopupPrepModel.CascadeCloser< edu.cmu.cs.dennisc.alice.ast.Expression > > {
	public static final int AT_END = Short.MAX_VALUE;
	private edu.cmu.cs.dennisc.alice.ast.BlockStatement blockStatement;
	private edu.cmu.cs.dennisc.alice.ast.Statement statement;
	private int specifiedIndex;
	private edu.cmu.cs.dennisc.alice.ast.Expression[] initialExpressions;
	private InsertStatementEdit( org.lgna.croquet.steps.CompletionStep completionStep, edu.cmu.cs.dennisc.alice.ast.BlockStatement blockStatement, int index, edu.cmu.cs.dennisc.alice.ast.Statement statement, edu.cmu.cs.dennisc.alice.ast.Expression[] initialExpressions ) {
		super( completionStep );
		this.blockStatement = blockStatement;
		this.specifiedIndex = index;
		this.statement = statement;
		this.initialExpressions = initialExpressions;
	}
	public InsertStatementEdit( org.lgna.croquet.steps.CompletionStep< ? > completionStep, org.alice.ide.codeeditor.BlockStatementIndexPair blockStatementIndexPair, edu.cmu.cs.dennisc.alice.ast.Statement statement, edu.cmu.cs.dennisc.alice.ast.Expression[] initialExpressions ) {
		this( completionStep, blockStatementIndexPair.getBlockStatement(), blockStatementIndexPair.getIndex(), statement, initialExpressions );
	}

	//todo
	public InsertStatementEdit( org.lgna.croquet.steps.CompletionStep< ? > completionStep, edu.cmu.cs.dennisc.alice.ast.BlockStatement blockStatement, int index, edu.cmu.cs.dennisc.alice.ast.Statement statement ) {
		this( completionStep, blockStatement, index, statement, new edu.cmu.cs.dennisc.alice.ast.Expression[] {} );
	}
	public InsertStatementEdit( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder, Object step ) {
		super( binaryDecoder, step );
		org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
		edu.cmu.cs.dennisc.alice.Project project = ide.getProject();
		java.util.UUID blockStatementId = binaryDecoder.decodeId();
		this.blockStatement = edu.cmu.cs.dennisc.alice.project.ProjectUtilities.lookupNode( project, blockStatementId );
		this.specifiedIndex = binaryDecoder.decodeInt();
		java.util.UUID statementId = binaryDecoder.decodeId();
		this.statement = edu.cmu.cs.dennisc.alice.project.ProjectUtilities.lookupNode( project, statementId );
		java.util.UUID[] ids = binaryDecoder.decodeIdArray();
		final int N = ids.length;
		this.initialExpressions = new edu.cmu.cs.dennisc.alice.ast.Expression[ N ];
		for( int i=0; i<N; i++ ) {
			this.initialExpressions[ i ] = edu.cmu.cs.dennisc.alice.project.ProjectUtilities.lookupNode( project, ids[ i ] );
		}
	}
	@Override
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		super.encode( binaryEncoder );
		binaryEncoder.encode( this.blockStatement.getUUID() );
		binaryEncoder.encode( this.specifiedIndex );
		binaryEncoder.encode( this.statement.getUUID() );
		final int N = this.initialExpressions.length;
		java.util.UUID[] ids = new java.util.UUID[ N ];
		for( int i=0; i<N; i++ ) {
			ids[ i ] = this.initialExpressions[ i ].getUUID();
		}
		binaryEncoder.encode( ids );
	}

	public edu.cmu.cs.dennisc.alice.ast.Expression[] getInitialExpressions() {
		return this.initialExpressions;
	}
	
	private int getActualIndex() {
		return Math.min( this.specifiedIndex, this.blockStatement.statements.size() );
	}
	@Override
	protected final void doOrRedoInternal( boolean isDo ) {
		int actualIndex = this.getActualIndex();
		this.blockStatement.statements.add( actualIndex, this.statement );
	}

	@Override
	protected final void undoInternal() {
		int actualIndex = this.getActualIndex();
		if( this.blockStatement.statements.get( actualIndex ) == this.statement ) {
			this.blockStatement.statements.remove( actualIndex );
		} else {
			throw new javax.swing.undo.CannotUndoException();
		}
	}
	
	public edu.cmu.cs.dennisc.alice.ast.Statement getStatement() {
		return this.statement;
	}

//	@Override
//	public edu.cmu.cs.dennisc.croquet.Edit< edu.cmu.cs.dennisc.croquet.ActionOperation > getAcceptableReplacement( edu.cmu.cs.dennisc.croquet.Retargeter retargeter ) {
//		edu.cmu.cs.dennisc.alice.ast.BlockStatement replacementBlockStatement = retargeter.retarget( this.blockStatement );
//		edu.cmu.cs.dennisc.alice.ast.Statement replacementStatement = retargeter.retarget( this.statement );
//		return new InsertStatementEdit( replacementBlockStatement, this.index, replacementStatement );
//	}
	
	@Override
	protected StringBuilder updatePresentation( StringBuilder rv, java.util.Locale locale ) {
		//super.updatePresentation( rv, locale );
		rv.append( "drop: " );
		edu.cmu.cs.dennisc.alice.ast.NodeUtilities.safeAppendRepr( rv, this.statement, locale );
		return rv;
	}
	@Override
	public edu.cmu.cs.dennisc.croquet.ReplacementAcceptability getReplacementAcceptability( edu.cmu.cs.dennisc.croquet.Edit< ? > replacementCandidate, edu.cmu.cs.dennisc.croquet.UserInformation userInformation ) {
		if( replacementCandidate instanceof InsertStatementEdit ) {
			InsertStatementEdit insertStatementEdit = (InsertStatementEdit)replacementCandidate;
			final int N = this.initialExpressions.length;
			if( insertStatementEdit.initialExpressions.length == N ) {
				edu.cmu.cs.dennisc.croquet.ReplacementAcceptability rv = edu.cmu.cs.dennisc.croquet.ReplacementAcceptability.TO_BE_HONEST_I_DIDNT_EVEN_REALLY_CHECK;
				//todo
				if( N == 1 ) {
					for( int i=0; i<N; i++ ) {
						edu.cmu.cs.dennisc.alice.ast.Expression originalI = this.initialExpressions[ i ];
						edu.cmu.cs.dennisc.alice.ast.Expression replacementI = insertStatementEdit.initialExpressions[ i ];
						if( originalI instanceof edu.cmu.cs.dennisc.alice.ast.AbstractValueLiteral ) {
							if( replacementI instanceof edu.cmu.cs.dennisc.alice.ast.AbstractValueLiteral ) {
								Object originalValue = ((edu.cmu.cs.dennisc.alice.ast.AbstractValueLiteral)originalI).getValueProperty().getValue();
								Object replacementValue = ((edu.cmu.cs.dennisc.alice.ast.AbstractValueLiteral)replacementI).getValueProperty().getValue();
								if( edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( originalValue, replacementValue ) ) {
									rv = edu.cmu.cs.dennisc.croquet.ReplacementAcceptability.PERFECT_MATCH;
								} else {
									StringBuilder sb = new StringBuilder();
									sb.append( "original value: " );
									sb.append( originalValue );
									sb.append( "; changed to: " );
									sb.append( replacementValue );
									sb.append( "." );
									rv = edu.cmu.cs.dennisc.croquet.ReplacementAcceptability.createDeviation( edu.cmu.cs.dennisc.croquet.ReplacementAcceptability.DeviationSeverity.POTENTIAL_SOURCE_OF_PROBLEMS, sb.toString() );
								}
							}
						}
					}
				}
				return rv;
			} else {
				return edu.cmu.cs.dennisc.croquet.ReplacementAcceptability.createRejection( "expressions count not the same" ); 
			}
		} else {
			return edu.cmu.cs.dennisc.croquet.ReplacementAcceptability.createRejection( "replacement is not an instance of InsertStatementEdit" ); 
		}
	}
	
	@Override
	protected StringBuilder updateTutorialTransactionTitle( StringBuilder rv, edu.cmu.cs.dennisc.croquet.UserInformation userInformation ) {
		rv.append( "insert " );
		rv.append( this.statement.getRepr( userInformation.getLocale() ) );
		return rv;
	}

	
//	public InsertStatementEdit createTutorialCompletionEdit( edu.cmu.cs.dennisc.croquet.Retargeter retargeter, edu.cmu.cs.dennisc.alice.ast.Statement replacementStatement ) {
//		edu.cmu.cs.dennisc.alice.ast.BlockStatement replacementBlockStatement = retargeter.retarget( this.blockStatement );
//		retargeter.addKeyValuePair( this.statement, replacementStatement );
//		final int N = this.initialExpressions.length;
//
//		System.err.println( "todo: replacementExpressions" );
//		edu.cmu.cs.dennisc.alice.ast.Expression[] replacementExpressions = this.initialExpressions;
//		
//		return new InsertStatementEdit( replacementBlockStatement, this.specifiedIndex, replacementStatement, replacementExpressions );
//	}
	@Override
	public void retarget( edu.cmu.cs.dennisc.croquet.Retargeter retargeter ) {
		super.retarget( retargeter );
		this.blockStatement = retargeter.retarget( this.blockStatement );
		this.statement = retargeter.retarget( this.statement );
		if( this.statement instanceof edu.cmu.cs.dennisc.alice.ast.ExpressionStatement ) {
			edu.cmu.cs.dennisc.alice.ast.ExpressionStatement expressionStatement = (edu.cmu.cs.dennisc.alice.ast.ExpressionStatement)statement;
			edu.cmu.cs.dennisc.alice.ast.Expression expression = expressionStatement.expression.getValue();
			if( expression instanceof edu.cmu.cs.dennisc.alice.ast.MethodInvocation ) {
				edu.cmu.cs.dennisc.alice.ast.MethodInvocation methodInvocation = (edu.cmu.cs.dennisc.alice.ast.MethodInvocation)expression;
				methodInvocation.method.setValue( retargeter.retarget( methodInvocation.method.getValue() ) );
			}
		}
	}
	@Override
	public void addKeyValuePairs( edu.cmu.cs.dennisc.croquet.Retargeter retargeter, edu.cmu.cs.dennisc.croquet.Edit< ? > edit ) {
		super.addKeyValuePairs( retargeter, edit );
		InsertStatementEdit replacementEdit = (InsertStatementEdit)edit;
		retargeter.addKeyValuePair( this.blockStatement, replacementEdit.blockStatement );
		retargeter.addKeyValuePair( this.statement, replacementEdit.statement );
		System.err.println( "TODO: recursive retarget" );
		if( this.statement instanceof edu.cmu.cs.dennisc.alice.ast.AbstractStatementWithBody ) {
			retargeter.addKeyValuePair( ((edu.cmu.cs.dennisc.alice.ast.AbstractStatementWithBody)this.statement).body.getValue(), ((edu.cmu.cs.dennisc.alice.ast.AbstractStatementWithBody)replacementEdit.statement).body.getValue() );
		}
		final int N = this.initialExpressions.length;
		assert N == replacementEdit.initialExpressions.length;
		for( int i=0; i<N; i++ ) {
			retargeter.addKeyValuePair( this.initialExpressions[ i ], replacementEdit.initialExpressions[ i ] );
		}
	}
}

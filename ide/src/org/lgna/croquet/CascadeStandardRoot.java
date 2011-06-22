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

package org.lgna.croquet;

/**
 * @author Dennis Cosgrove
 */
public final class CascadeStandardRoot<T> extends CascadeRoot< T, org.lgna.croquet.history.CascadeCompletionStep< T > > {
	private final Cascade< T > cascade;
	/*package-private*/CascadeStandardRoot( Cascade< T > cascade ) {
		super( java.util.UUID.fromString( "40fe9d1b-003d-4108-9f38-73fccb29b978" ) );
		this.cascade = cascade;
	}
	@Override
	public Cascade< T > getCompletionModel() {
		return this.cascade;
	}
	@Override
	public Class< T > getComponentType() {
		return this.cascade.getComponentType();
	}

	@Override
	public void prologue() {
		this.cascade.prologue();
	}
	@Override
	public void epilogue() {
		this.cascade.epilogue();
	}
	@Override
	public org.lgna.croquet.history.CascadeCompletionStep< T > createCompletionStep( org.lgna.croquet.triggers.Trigger trigger ) {
		return org.lgna.croquet.history.TransactionManager.addCascadeCompletionStep( this.cascade, trigger );
	}
	@Override
	public void handleCompletion( org.lgna.croquet.history.CascadeCompletionStep< T > completionStep, T[] values ) {
		try {
			org.lgna.croquet.edits.Edit< ? extends Cascade< T > > edit = this.cascade.createEdit( completionStep, values );
			completionStep.commitAndInvokeDo( edit );
		} finally {
			this.getPopupPrepModel().handleFinally();
		}
	}
	@Override
	public void handleCancel( org.lgna.croquet.history.CascadeCompletionStep< T > completionStep, org.lgna.croquet.triggers.Trigger trigger, CancelException ce ) {
		try {
			if( completionStep != null ) {
				completionStep.cancel();
			} else {
				org.lgna.croquet.history.TransactionManager.addCancelCompletionStep( this.cascade, trigger );
			}
		} finally {
			this.getPopupPrepModel().handleFinally();
		}
	}

}

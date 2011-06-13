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
package org.lgna.croquet.history;

/**
 * @author Dennis Cosgrove
 */
public abstract class CompletionStep< M extends org.lgna.croquet.CompletionModel > extends Step< M > {
	private final TransactionHistory transactionHistory;
	private org.lgna.croquet.edits.Edit<M> edit;
	private boolean isSuccessfullyCompleted;
	private boolean isPending = true;
	public CompletionStep( Transaction parent, M model, org.lgna.croquet.Trigger trigger, TransactionHistory transactionHistory ) {
		super( parent, model, trigger );
		parent.setCompletionStep( this );
		this.transactionHistory = transactionHistory;
		if( this.transactionHistory != null ) {
			this.transactionHistory.setParent( this );
			TransactionManager.pushTransactionHistory( this.transactionHistory );
		}
	}
	public CompletionStep( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super( binaryDecoder );
		this.isPending = binaryDecoder.decodeBoolean();
		this.isSuccessfullyCompleted = binaryDecoder.decodeBoolean();
		this.edit = binaryDecoder.decodeBinaryEncodableAndDecodable( this );
		this.transactionHistory = binaryDecoder.decodeBinaryEncodableAndDecodable();
		if( this.transactionHistory != null ) {
			this.transactionHistory.setParent( this );
		}
	}
	@Override
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		super.encode( binaryEncoder );
		binaryEncoder.encode( this.isPending );
		binaryEncoder.encode( this.isSuccessfullyCompleted );
		binaryEncoder.encode( this.edit );
		binaryEncoder.encode( this.transactionHistory );
	}
	
	public boolean isValid() {
		return this.getModel() != null && ( this.edit == null || this.edit.isValid() );
	}

	@Override
	public void retarget( org.lgna.croquet.Retargeter retargeter ) {
		super.retarget( retargeter );
		if( this.transactionHistory != null ) {
			this.transactionHistory.retarget( retargeter );
		}
		if( this.edit != null ) {
			this.edit.retarget( retargeter );
		}
	}

	public TransactionHistory getTransactionHistory() {
		return this.transactionHistory;
	}
	private void popTransactionHistoryIfNecessary() {
		if( this.transactionHistory != null ) {
			TransactionHistory pop = TransactionManager.popTransactionHistory();
			assert pop == this.transactionHistory;
		}
	}
	public boolean isPending() {
		return this.isPending;
	}
	public boolean isSuccessfullyCompleted() {
		return this.isSuccessfullyCompleted;
	}
	public boolean isCanceled() {
		return this.isPending() && this.isSuccessfullyCompleted() == false;
	}

	public org.lgna.croquet.edits.Edit< ? > getEdit() {
		return this.edit;
	}
	/*package-private*/ void setEdit( org.lgna.croquet.edits.Edit<M> edit ) {
		this.isSuccessfullyCompleted = true;
		this.edit = edit;
		this.isPending = false;
	}
	public void commitAndInvokeDo( org.lgna.croquet.edits.Edit edit ) {
		this.getParent().reify();
		org.lgna.croquet.history.event.EditCommittedEvent e = new org.lgna.croquet.history.event.EditCommittedEvent( this, edit );
		this.fireChanging( e );
		this.setEdit( edit );
		edit.doOrRedo( true );
		this.fireChanged( e );
		this.popTransactionHistoryIfNecessary();
	}
	public void finish() {
		this.getParent().reify();
		this.isSuccessfullyCompleted = true;
		this.edit = null;
		this.isPending = false;
		this.popTransactionHistoryIfNecessary();
	}
	public void cancel() {
		this.getParent().reify();
		this.isSuccessfullyCompleted = false;
		this.edit = null;
		this.isPending = false;
		this.popTransactionHistoryIfNecessary();
	}
	

	public String getTutorialTransactionTitle( org.lgna.croquet.UserInformation userInformation ) {
		return this.getModel().getTutorialTransactionTitle( this, userInformation );
	}
	@Override
	protected StringBuilder updateRepr( StringBuilder rv ) {
		rv = super.updateRepr( rv );
		rv.append( ";edit=" );
		rv.append( this.edit );
//		rv.append( ";isActive=" );
//		rv.append( this.isActive );
		return rv;
	}
}

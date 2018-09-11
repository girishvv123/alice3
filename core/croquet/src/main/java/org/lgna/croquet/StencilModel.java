/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.lgna.croquet;

import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.UserActivity;

import javax.swing.SwingUtilities;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CyclicBarrier;

/**
 * @author Dennis Cosgrove
 */
public abstract class StencilModel extends AbstractCompletionModel implements Triggerable {
	private final CyclicBarrier barrier = new CyclicBarrier( 2 );
	private String text;

	public StencilModel( UUID migrationId ) {
		super( Application.INFORMATION_GROUP, migrationId );
	}

	@Override
	public List<List<PrepModel>> getPotentialPrepModelPaths( Edit edit ) {
		return Collections.emptyList();
	}

	@Override
	protected void localize() {
		this.text = this.findDefaultLocalizedText();
	}

	public String getText() {
		return this.text;
	}

	protected void barrierAwait() {
		try {
			this.barrier.await();
		} catch( Throwable t ) {
			throw new RuntimeException( t );
		}
	}

	protected abstract void showStencil();

	protected abstract void hideStencil();

	@Override
	protected final void performInActivity( UserActivity userActivity ) {
		Logger.outln( this );
		userActivity.setCompletionModel( this );
		this.barrier.reset();
		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				showStencil();
			}
		} );
		this.barrierAwait();
		this.hideStencil();
		userActivity.finish();
	}

	@Override
	public void fire( UserActivity activity ) {
		if( this.isEnabled() ) {
			this.initializeIfNecessary();
			this.performInActivity( activity );
		}
	}
}

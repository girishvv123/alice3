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

package org.alice.ide.declarationseditor;

/**
 * @author Dennis Cosgrove
 */
public class DeclarationsEditorComposite extends org.lgna.croquet.SimpleComposite<org.alice.ide.declarationseditor.components.TypeEditor> {
	private static class SingletonHolder {
		private static DeclarationsEditorComposite instance = new DeclarationsEditorComposite();
	}

	public static DeclarationsEditorComposite getInstance() {
		return SingletonHolder.instance;
	}

	private final org.alice.ide.declarationseditor.DeclarationMenu declarationMenu = new org.alice.ide.declarationseditor.DeclarationMenu();
	private final DeclarationTabState tabState = new DeclarationTabState();
	private final BackwardForwardComposite backwardForwardComposite;

	private final org.lgna.croquet.meta.MetaState<org.lgna.project.ast.NamedUserType> metaState = new org.lgna.croquet.meta.StateTrackingMetaState<org.lgna.project.ast.NamedUserType, DeclarationComposite>( this.tabState ) {
		@Override
		public org.lgna.project.ast.NamedUserType getValue() {
			DeclarationComposite<?, ?> declarationComposite = tabState.getValue();
			if( declarationComposite != null ) {
				return edu.cmu.cs.dennisc.java.lang.ClassUtilities.getInstance( declarationComposite.getType(), org.lgna.project.ast.NamedUserType.class );
			} else {
				return null;
			}
		}
	};

	private DeclarationsEditorComposite() {
		super( java.util.UUID.fromString( "bdf8f46f-1c77-4e01-83d1-952cbf63504e" ) );
		this.backwardForwardComposite = this.registerSubComposite( new BackwardForwardComposite() );
		this.registerTabSelectionState( this.tabState );
	}

	public org.alice.ide.declarationseditor.DeclarationMenu getDeclarationMenu() {
		return this.declarationMenu;
	}

	public org.lgna.croquet.meta.MetaState<org.lgna.project.ast.NamedUserType> getMetaState() {
		return this.metaState;
	}

	public DeclarationTabState getTabState() {
		return this.tabState;
	}

	public org.lgna.croquet.Composite<?> getControlsComposite() {
		return this.backwardForwardComposite;
	}

	@Override
	protected org.alice.ide.declarationseditor.components.TypeEditor createView() {
		return new org.alice.ide.declarationseditor.components.TypeEditor( this );
	}
}

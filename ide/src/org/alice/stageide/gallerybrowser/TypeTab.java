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

package org.alice.stageide.gallerybrowser;

/**
 * @author Dennis Cosgrove
 */
public class TypeTab extends org.lgna.croquet.PredeterminedTab {
	private static class SingletonHolder {
		private static TypeTab instance = new TypeTab();
	}
	public static TypeTab getInstance() {
		return SingletonHolder.instance;
	}
	private TypeTab() {
		super( java.util.UUID.fromString( "86ebb5e5-8cae-4f3b-ae46-35f3a7f4a00c" ) );
	}
	@Override
	protected org.lgna.croquet.components.JComponent< ? > createMainComponent() {
		org.lgna.croquet.components.BorderPanel rv = new org.lgna.croquet.components.BorderPanel();
		//rv.addComponent( TypeDeclarationOperation.getInstance().createButton(), org.lgna.croquet.components.BorderPanel.Constraint.LINE_START );
		//todo
		
//		org.lgna.croquet.components.LineAxisPanel panel = new org.lgna.croquet.components.LineAxisPanel();
//		
//		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
//		org.lgna.project.Project project = ide.getProject();
//		edu.cmu.cs.dennisc.tree.Node< org.lgna.project.ast.NamedUserType > root = org.lgna.project.project.ProjectUtilities.getNamedUserTypesAsTree( project );
//		
//		java.util.List< org.lgna.project.ast.JavaType > javaTypes = ide.getApiConfigurationManager().getTopLevelGalleryTypes();
//		java.util.List< org.lgna.project.ast.NamedUserType > userTypes = org.alice.ide.typemanager.TypeManager.getNamedUserTypesFor( javaTypes );
//		for( org.lgna.project.ast.NamedUserType userType : userTypes ) {
//			panel.addComponent( new org.alice.ide.croquet.components.gallerybrowser.GalleryDragComponent( TypeDragModel.getInstance( userType ) ) );
//		}
		
		rv.addComponent( new MyTypesView(), org.lgna.croquet.components.BorderPanel.Constraint.CENTER );

		org.lgna.croquet.components.BorderPanel lineEndPanel = new org.lgna.croquet.components.BorderPanel();
		lineEndPanel.addComponent( org.alice.stageide.croquet.models.gallerybrowser.CreateMyInstanceOperation.getInstance().createButton(), org.lgna.croquet.components.BorderPanel.Constraint.PAGE_END );
		rv.addComponent( lineEndPanel, org.lgna.croquet.components.BorderPanel.Constraint.LINE_END );

		rv.setBackgroundColor( GalleryBrowser.BACKGROUND_COLOR );
		return rv;
	}
}

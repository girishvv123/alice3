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
class CreateFieldFromBillboardPane extends org.alice.ide.declarationpanes.CreateLargelyPredeterminedFieldPane {
	public CreateFieldFromBillboardPane( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType, org.alice.apis.moveandturn.Billboard billboard ) {
		super( declaringType, billboard.getClass(), null );
		org.alice.apis.moveandturn.ImageSource frontImageSource = billboard.getFrontImageSource();
		if( frontImageSource != null ) {
			org.alice.virtualmachine.resources.ImageResource imageResource = frontImageSource.getImageResource();
			if( imageResource != null ) {
				java.awt.image.BufferedImage bufferedImage = edu.cmu.cs.dennisc.image.ImageFactory.getBufferedImage( imageResource );
				if( bufferedImage != null ) {
					edu.cmu.cs.dennisc.javax.swing.components.JImageView imageView = new edu.cmu.cs.dennisc.javax.swing.components.JImageView( bufferedImage, 240 );
					this.add( new edu.cmu.cs.dennisc.javax.swing.components.JLineAxisPane( javax.swing.Box.createHorizontalStrut( 8 ), imageView ), java.awt.BorderLayout.EAST );
				} else {
					//todo?
				}
			}
		}
	}
}

/**
 * @author Dennis Cosgrove
 */
class CreateBillboardActionOperation extends AbstractGalleryDeclareFieldOperation {
	public CreateBillboardActionOperation() {
		this.setName( "Create Billboard..." );
	}
	@Override
	protected edu.cmu.cs.dennisc.pattern.Tuple2< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice, Object > createFieldAndInstance( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice ownerType ) {
		org.alice.ide.resource.prompter.ImageResourcePrompter imageResourcePrompter = org.alice.ide.resource.prompter.ImageResourcePrompter.getSingleton();
		try {
			org.alice.virtualmachine.resources.ImageResource frontImageResource = imageResourcePrompter.promptUserForResource( this.getIDE().getJFrame() );
			if( frontImageResource != null ) {
				edu.cmu.cs.dennisc.alice.Project project = this.getIDE().getProject();
				if( project != null ) {
					project.addResource( frontImageResource );
				}
				org.alice.apis.moveandturn.ImageSource frontImageSource = new org.alice.apis.moveandturn.ImageSource( frontImageResource );
				org.alice.apis.moveandturn.Billboard billboard = new org.alice.apis.moveandturn.Billboard();
				billboard.setFrontImageSource( frontImageSource );

				edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType = this.getIDE().getSceneType();
				CreateFieldFromBillboardPane createFieldFromBillboardPane = new CreateFieldFromBillboardPane( declaringType, billboard );
				edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field = createFieldFromBillboardPane.showInJDialog( this.getIDE().getJFrame() );
				if( field != null ) {
					//ide.getSceneEditor().handleFieldCreation( declaringType, field, person );
					return new edu.cmu.cs.dennisc.pattern.Tuple2< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice, Object >( field, billboard );
				} else {
					return null;
				}

				//				String name = "billboard";
				//				edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type = this.getIDE().getTypeDeclaredInAliceFor(org.alice.apis.moveandturn.Billboard.class);
				//				edu.cmu.cs.dennisc.alice.ast.Expression initializer = org.alice.ide.ast.NodeUtilities.createInstanceCreation(type);
				//				edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field = new edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice(name, type, initializer);
				//				field.finalVolatileOrNeither.setValue(edu.cmu.cs.dennisc.alice.ast.FieldModifierFinalVolatileOrNeither.FINAL);
				//				field.access.setValue(edu.cmu.cs.dennisc.alice.ast.Access.PRIVATE);
				//				return new edu.cmu.cs.dennisc.pattern.Tuple2<edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice, Object>( field, billboard );
			} else {
				return null;
			}
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}
}

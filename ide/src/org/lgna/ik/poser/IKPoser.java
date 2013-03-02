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

package org.lgna.ik.poser;

import java.util.ArrayList;

import org.lgna.ik.walkandtouch.PoserScene;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.UserType;
import org.lgna.story.ImplementationAccessor;
import org.lgna.story.MoveDirection;
import org.lgna.story.SBiped;
import org.lgna.story.SCamera;
import org.lgna.story.SProgram;
import org.lgna.story.TurnDirection;

import test.ik.IkTestApplication;

/**
 * @author Matt May
 */
class IkPoser extends SProgram {
	private static final boolean SHOULD_I_ANIMATE = true;
	private final SCamera camera = new SCamera();
	private final SBiped biped;
	public final PoserScene scene;

	private PoserSplitComposite composite;
	private UserType userType;

	public IkPoser( NamedUserType userType, boolean isAnimationDesired ) {
		//		this.biped = userType.getDeclaredConstructor().;
		//		Object value = userType.superType.getValue();
		//		TypeManager.getNamedUserTypeFromSuperType( (JavaType)value );
		this.biped = deriveBipedFromUserType( userType );
		scene = new PoserScene( camera, biped );
		this.userType = userType;
		composite = new PoserSplitComposite( this, isAnimationDesired );
	}

	private SBiped deriveBipedFromUserType( NamedUserType type ) {

		org.lgna.project.virtualmachine.ReleaseVirtualMachine vm = new org.lgna.project.virtualmachine.ReleaseVirtualMachine();

		org.lgna.story.resources.BipedResource bipedResource = org.lgna.story.resources.biped.OgreResource.BROWN;
		//org.lgna.story.resources.BipedResource bipedResource = org.lgna.story.resources.biped.AlienResource.DEFAULT;

		org.lgna.project.ast.NamedUserConstructor userConstructor = type.constructors.get( 0 );
		final int N = userConstructor.requiredParameters.size();
		Object[] arguments = new Object[ N ];
		switch( N ) {
		case 0:
			break;
		case 1:
			arguments[ 0 ] = bipedResource;
			break;
		case 2:
			assert false : N;
		}
		org.lgna.project.virtualmachine.UserInstance userInstance = vm.ENTRY_POINT_createInstance( type, arguments );
		return userInstance.getJavaInstance( SBiped.class );
	}

	private org.lgna.story.implementation.JointedModelImp<?, ?> getSubjectImp() {
		return ImplementationAccessor.getImplementation( this.biped );
	}

	private org.lgna.story.implementation.JointImp getEndImp() {
		org.lgna.story.resources.JointId endId = test.ik.croquet.EndJointIdState.getInstance().getValue();
		return this.getSubjectImp().getJointImplementation( endId );
	}

	public SBiped getBiped() {
		return this.biped;
	}

	private void initializeTest() {
		this.setActiveScene( this.scene );
		this.camera.turn( TurnDirection.RIGHT, .5 );
		this.camera.move( MoveDirection.BACKWARD, 8 );
		this.camera.move( MoveDirection.UP, 1 );
	}

	private PoserSplitComposite getComposite() {
		return composite;
	}

	public ArrayList<JointSelectionSphere> getJointSelectionSheres() {
		return scene.getJointSelectionSheres();
	}

	public void setAdapter( PoserControllerAdapter adapter ) {
		scene.setAdapter( adapter );
	}

	public Pose getPose() {
		return Pose.createPoseFromBiped( biped );
	}

	public UserType<?> getDeclaringType() {
		return userType;
	}

	public static void main( String[] args ) {
		IkTestApplication app = new IkTestApplication();
		app.initialize( args );

		org.lgna.project.virtualmachine.ReleaseVirtualMachine vm = new org.lgna.project.virtualmachine.ReleaseVirtualMachine();

		org.lgna.story.resources.BipedResource bipedResource = org.lgna.story.resources.biped.OgreResource.BROWN;
		//org.lgna.story.resources.BipedResource bipedResource = org.lgna.story.resources.biped.AlienResource.DEFAULT;

		org.lgna.project.ast.JavaType ancestorType = org.lgna.project.ast.JavaType.getInstance( SBiped.class );
		org.lgna.project.ast.JavaField argumentField = org.lgna.project.ast.JavaField.getInstance( bipedResource.getClass(), bipedResource.toString() );
		org.lgna.project.ast.NamedUserType type = org.alice.ide.typemanager.TypeManager.getNamedUserTypeFromArgumentField( ancestorType, argumentField );

		org.lgna.project.ast.NamedUserConstructor userConstructor = type.constructors.get( 0 );
		final int N = userConstructor.requiredParameters.size();
		Object[] arguments = new Object[ N ];
		switch( N ) {
		case 0:
			break;
		case 1:
			arguments[ 0 ] = bipedResource;
			break;
		case 2:
			assert false : N;
		}
		org.lgna.project.virtualmachine.UserInstance userInstance = vm.ENTRY_POINT_createInstance( type, arguments );
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( userInstance );
		userInstance.getJavaInstance( SBiped.class );
		IkPoser program = new IkPoser( type, SHOULD_I_ANIMATE );
		app.getFrame().setMainComposite( program.getComposite() );

		test.ik.croquet.IsLinearEnabledState.getInstance().setValueTransactionlessly( true );
		test.ik.croquet.IsAngularEnabledState.getInstance().setValueTransactionlessly( false );

		test.ik.croquet.SceneComposite.getInstance().getView().initializeInAwtContainer( program );
		program.initializeTest();

		app.getFrame().setSize( 1200, 800 );
		app.getFrame().setVisible( true );
	}
}

/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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

/**
 * @author Dennis Cosgrove
 */
public class PluginCommon {
	/*package-private*/static String substituteVersionTexts( Config config, String s ) {
		s = s.trim();
		s = s.replaceAll( "___ALICE_VERSION___", org.lgna.project.ProjectVersion.getCurrentVersionText() );
		s = s.replaceAll( "___JOGL_VERSION___", config.getJoglVersion() );
		s = s.replaceAll( "___ALICE_MODEL_SOURCE_VERSION___", config.getAliceModelSourceVersion() );
		s = s.replaceAll( "___NEBULOUS_MODEL_SOURCE_VERSION___", config.getNebulousModelSourceVersion() );
		return s;
	}

	public static java.util.List<String> getJarPathsToCopyFromMaven( Config config ) {
		java.util.List<String> list = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		list.add( substituteVersionTexts( config, "org/jogamp/gluegen/gluegen-rt/___JOGL_VERSION___/gluegen-rt-___JOGL_VERSION___.jar" ) );
		list.add( substituteVersionTexts( config, "org/jogamp/jogl/jogl-all/___JOGL_VERSION___/jogl-all-___JOGL_VERSION___.jar" ) );
		list.add( "javax/media/jmf/2.1.1e/jmf-2.1.1e.jar" );
		list.add( "com/sun/javamp3/1.0/javamp3-1.0.jar" );
		list.add( substituteVersionTexts( config, "org/alice/alice-model-source/___ALICE_MODEL_SOURCE_VERSION___/alice-model-source-___ALICE_MODEL_SOURCE_VERSION___.jar" ) );
		list.add( substituteVersionTexts( config, "org/alice/nonfree/nebulous-model-source/___NEBULOUS_MODEL_SOURCE_VERSION___/nebulous-model-source-___NEBULOUS_MODEL_SOURCE_VERSION___.jar" ) );
		return java.util.Collections.unmodifiableList( list );
	}
}

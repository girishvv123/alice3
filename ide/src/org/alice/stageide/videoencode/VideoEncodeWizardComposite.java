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

package org.alice.stageide.videoencode;

/**
 * @author Dennis Cosgrove
 */
public class VideoEncodeWizardComposite extends org.lgna.croquet.WizardDialogMainComposite {
	private static class SingletonHolder {
		private static VideoEncodeWizardComposite instance = new VideoEncodeWizardComposite();
	}
	public static VideoEncodeWizardComposite getInstance() {
		return SingletonHolder.instance;
	}
	
	private final RecordEventsPage recordEventsPage = new RecordEventsPage();
	private final CaptureImagesPage captureImagesPage = new CaptureImagesPage();
	private final UploadPage uploadPage = new UploadPage();
	private VideoEncodeWizardComposite() {
		super( java.util.UUID.fromString( "cc531529-314d-457c-bb30-d707dfd2b8d8" ), org.alice.ide.IDE.EXPORT_GROUP );
		this.addPage( this.recordEventsPage );
		this.addPage( this.captureImagesPage );
		this.addPage( this.uploadPage );
	}
	
	@Override
	protected org.lgna.croquet.StringValue getExplanation( org.lgna.croquet.history.CompletionStep<?> step ) {
		return null;
	}
	
	public static void main( String[] args ) {
		javax.swing.UIManager.LookAndFeelInfo lookAndFeelInfo = edu.cmu.cs.dennisc.javax.swing.plaf.PlafUtilities.getInstalledLookAndFeelInfoNamed( "Nimbus" );
		if( lookAndFeelInfo != null ) {
			try {
				javax.swing.UIManager.setLookAndFeel( lookAndFeelInfo.getClassName() );
//				edu.cmu.cs.dennisc.javax.swing.plaf.nimbus.NimbusUtilities.installModifiedNimbus( lookAndFeelInfo );
			} catch( Throwable t ) {
				t.printStackTrace();
			}
		}
		
		javax.swing.JComponent.setDefaultLocale( new java.util.Locale( "zh", "TW" ) );
		org.lgna.croquet.Application app = new org.lgna.croquet.simple.SimpleApplication();
		VideoEncodeWizardComposite composite = new VideoEncodeWizardComposite();
		composite.getGatedCommitDialogComposite().getOperation().fire();
		System.exit( 0 );
	}
}

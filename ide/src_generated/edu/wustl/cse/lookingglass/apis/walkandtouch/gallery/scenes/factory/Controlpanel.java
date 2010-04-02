/*
 * Copyright (c) 2008-2010, Washington University in St. Louis. All rights reserved.
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
 * 3. Products derived from the software may not be called "Looking Glass", nor 
 *    may "Looking Glass" appear in their name, without prior written permission
 *    of Washington University in St. Louis.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Washington University in St. Louis"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  ANY AND ALL 
 * EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A PARTICULAR PURPOSE, 
 * TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHORS, 
 * COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
 * INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.scenes.factory;

public class Controlpanel extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryModel { 
	edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean> On= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean>();  
	public Controlpanel() {
		super( "factory/controlpanel" );
		edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean> On= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean>();  
}
	public enum Part {
		Button( "button" ),
		Lever( "lever" ),
		Button01( "button01" ),
		Button02( "button02" ),
		Button03( "button03" );
		private String[] m_path;
		Part( String... path ) {
			m_path = path;
		}
		public String[] getPath() {
			return m_path;
		}
	}
	public edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel getPart( Part part ) {
		return getDescendant( edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel.class, part.getPath() );
	}

	public void TurnOn( ) {
		this.getPart(Controlpanel.Part.Lever).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2) );
		On.value = true;
	}

	public void Blink( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					// DoInOrder { 
				Controlpanel.this.getPart(Controlpanel.Part.Button01).setColor( new org.alice.apis.moveandturn.Color(1.0f, 0.0f, 0.0f), 0.25 );
				Controlpanel.this.getPart(Controlpanel.Part.Button01).setColor( new org.alice.apis.moveandturn.Color(1.0f, 1.0f, 1.0f), 0.25 );
//			}
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				Controlpanel.this.getPart(Controlpanel.Part.Button02).setColor( new org.alice.apis.moveandturn.Color(1.0f, 0.64705884f, 0.0f), 0.5 );
				Controlpanel.this.getPart(Controlpanel.Part.Button02).setColor( new org.alice.apis.moveandturn.Color(1.0f, 1.0f, 1.0f), 0.5 );
//			}
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				Controlpanel.this.getPart(Controlpanel.Part.Button03).setColor( new org.alice.apis.moveandturn.Color(1.0f, 1.0f, 0.4f), 0.25 );
				Controlpanel.this.getPart(Controlpanel.Part.Button03).setColor( new org.alice.apis.moveandturn.Color(1.0f, 1.0f, 1.0f), 0.25 );
//			}
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				Controlpanel.this.getPart(Controlpanel.Part.Button).setColor( new org.alice.apis.moveandturn.Color(1.0f, 0.0f, 0.0f), 0.5 );
				Controlpanel.this.getPart(Controlpanel.Part.Button).setColor( new org.alice.apis.moveandturn.Color(1.0f, 1.0f, 1.0f), 0.5 );
//			}
				}
			}
		);

	}
}

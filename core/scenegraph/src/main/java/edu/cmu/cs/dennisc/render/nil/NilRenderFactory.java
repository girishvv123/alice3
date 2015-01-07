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
package edu.cmu.cs.dennisc.render.nil;

/**
 * @author Dennis Cosgrove
 */
public enum NilRenderFactory implements edu.cmu.cs.dennisc.render.RenderFactory {
	INSTANCE;

	@Override
	public edu.cmu.cs.dennisc.render.ImageBuffer createImageBuffer( edu.cmu.cs.dennisc.color.Color4f backgroundColor ) {
		return new NrImageBuffer( backgroundColor );
	}

	@Override
	public edu.cmu.cs.dennisc.render.ImageBuffer createTransparentBackgroundImageBuffer() {
		return this.createImageBuffer( null );
	}

	@Override
	public edu.cmu.cs.dennisc.render.HeavyweightOnscreenRenderTarget createHeavyweightOnscreenRenderTarget() {
		return new NrHeavyweightOnscreenRenderTarget();
	}

	@Override
	public edu.cmu.cs.dennisc.render.LightweightOnscreenRenderTarget createLightweightOnscreenRenderTarget() {
		return new NrLightweightOnscreenRenderTarget();
	}

	@Override
	public edu.cmu.cs.dennisc.render.OffscreenRenderTarget createOffscreenRenderTarget( int width, int height, edu.cmu.cs.dennisc.render.RenderTarget renderTargetToShareContextWith ) {
		return new NrOffscreenRenderTarget( width, height, renderTargetToShareContextWith );
	}

	@Override
	public void acquireRenderingLock() {
	}

	@Override
	public void releaseRenderingLock() {
	}

	@Override
	public void addAutomaticDisplayListener( edu.cmu.cs.dennisc.render.event.AutomaticDisplayListener automaticDisplayListener ) {
		this.automaticDisplayListeners.add( automaticDisplayListener );
	}

	@Override
	public void removeAutomaticDisplayListener( edu.cmu.cs.dennisc.render.event.AutomaticDisplayListener automaticDisplayListener ) {
		this.automaticDisplayListeners.remove( automaticDisplayListener );
	}

	@Override
	public Iterable<edu.cmu.cs.dennisc.render.event.AutomaticDisplayListener> getAutomaticDisplayListeners() {
		return java.util.Collections.unmodifiableList( this.automaticDisplayListeners );
	}

	@Override
	public int getAutomaticDisplayCount() {
		return this.automaticDisplayCount;
	}

	@Override
	public void incrementAutomaticDisplayCount() {
		this.automaticDisplayCount++;
	}

	@Override
	public void decrementAutomaticDisplayCount() {
		this.automaticDisplayCount--;
	}

	@Override
	public void invokeLater( Runnable runnable ) {
	}

	@Override
	public void invokeAndWait( Runnable runnable ) throws InterruptedException, java.lang.reflect.InvocationTargetException {
	}

	@Override
	public void invokeAndWait_ThrowRuntimeExceptionsIfNecessary( Runnable runnable ) {
	}

	private final java.util.List<edu.cmu.cs.dennisc.render.event.AutomaticDisplayListener> automaticDisplayListeners = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();
	private int automaticDisplayCount;
}

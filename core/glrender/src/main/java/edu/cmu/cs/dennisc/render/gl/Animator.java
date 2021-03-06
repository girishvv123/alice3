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

package edu.cmu.cs.dennisc.render.gl;

import edu.cmu.cs.dennisc.java.lang.ThreadUtilities;
import edu.cmu.cs.dennisc.print.PrintUtilities;

/**
 * @author Dennis Cosgrove
 */
/*package-private*/abstract class Animator implements Runnable {
  private static final long DEFAULT_SLEEP_MILLIS = 16;
  private boolean isActive = false;
  private long tStart;
  private int frameCount;
  private int frameRate;
  private long sleepMillis = DEFAULT_SLEEP_MILLIS;

  public enum ThreadDeferenceAction {
    SLEEP, YIELD
  }

  public void start() {
    this.isActive = true;
    this.frameCount = 0;
    frameRate = 0;
    this.tStart = System.currentTimeMillis();
    new Thread(this).start();
  }

  public void stop() {
    this.isActive = false;
  }

  public int getFrameRate() {
    return frameRate;
  }

  public int getFrameCount() {
    return this.frameCount;
  }

  public long getStartTimeMillis() {
    return this.tStart;
  }

  public long getSleepMillis() {
    return this.sleepMillis;
  }

  public void setSleepMillis(long sleepMillis) {
    this.sleepMillis = sleepMillis;
  }

  protected abstract ThreadDeferenceAction step();

  @Override
  public void run() {
    long tPrev = 0;
    int fps = 0;
    long millisSinceLastFps = 0;

    while (this.isActive) {
      while (true) {
        long tCurrent = System.currentTimeMillis();
        if ((tCurrent - tPrev) < sleepMillis) {
          ThreadUtilities.sleep(sleepMillis);
        } else {
          millisSinceLastFps += (tCurrent - tPrev);
          fps++;
          if (millisSinceLastFps > 1000) {
            frameRate = fps;
            millisSinceLastFps = 0;
            fps = 0;
          }
          tPrev = tCurrent;
          break;
        }
      }
      ThreadDeferenceAction threadAction = this.step();
      if (threadAction == ThreadDeferenceAction.SLEEP) {
        ThreadUtilities.sleep(this.sleepMillis);
      } else if (threadAction == ThreadDeferenceAction.YIELD) {
        Thread.yield();
      } else {
        PrintUtilities.println("threadAction", threadAction);
      }
      this.frameCount++;
    }
  }
}

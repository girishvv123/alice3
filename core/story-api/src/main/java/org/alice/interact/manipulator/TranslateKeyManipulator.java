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
package org.alice.interact.manipulator;

import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import org.alice.interact.MovementKey;

/**
 * @author David Culyba
 */
public abstract class TranslateKeyManipulator extends KeyManipulator {
  TranslateKeyManipulator(MovementKey[] directionKeys) {
    super(directionKeys);
  }

  @Override
  public String getUndoRedoDescription() {
    return "Object Move";
  }

  @Override
  protected boolean shouldApplyEnding(double currentTime, double amountToMove) {
    if (!super.shouldApplyEnding(currentTime, amountToMove)) {
      return false;
    }
    Point3 positionDif = Point3.createSubtraction(manipulatedTransformable.getAbsoluteTransformation().translation, initialPoint);
    double distanceAlreadyMoved = positionDif.calculateMagnitude();
    return amountToMove > distanceAlreadyMoved;
  }

  @Override
  protected void manipulate(double amountToMove, MovementKey key) {
    key.applyTranslation(manipulatedTransformable, amountToMove);
    enforceBounds();
  }

  public void setBounds(AxisAlignedBox bounds) {
    this.bounds = bounds;
  }

  private void enforceBounds() {
    if (this.bounds != null) {
      Point3 currentPos = this.manipulatedTransformable.getTranslation(AsSeenBy.SCENE);
      if (currentPos.x > this.bounds.getXMaximum()) {
        currentPos.x = this.bounds.getXMaximum();
      }
      if (currentPos.x < this.bounds.getXMinimum()) {
        currentPos.x = this.bounds.getXMinimum();
      }
      if (currentPos.y > this.bounds.getYMaximum()) {
        currentPos.y = this.bounds.getYMaximum();
      }
      if (currentPos.y < this.bounds.getYMinimum()) {
        currentPos.y = this.bounds.getYMinimum();
      }
      if (currentPos.z > this.bounds.getZMaximum()) {
        currentPos.z = this.bounds.getZMaximum();
      }
      if (currentPos.z < this.bounds.getZMinimum()) {
        currentPos.z = this.bounds.getZMinimum();
      }

      this.manipulatedTransformable.setTranslationOnly(currentPos, AsSeenBy.SCENE);
    }
  }

  private AxisAlignedBox bounds;
}

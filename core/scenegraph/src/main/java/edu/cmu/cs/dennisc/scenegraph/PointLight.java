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

package edu.cmu.cs.dennisc.scenegraph;

import edu.cmu.cs.dennisc.property.DoubleProperty;

/**
 * a point light emits rays in all directions.<br>
 * the position is inherent from absolute transformation of its composite.<br>
 * useful in simulating a light bulb.<br>
 * <p>
 * when calculating a light's contribution to the illumination of a vertex, that
 * light's color is multiplied a polynomial function of distance.<br>
 * this polynomial is controllable via constant, linear, and quadratic
 * attenuation properties.<br>
 *
 * <pre>
 *    d = distance from light to vertex position
 *    c = constant attenuation
 *    l = linear attenuation
 *    q = quadratic attenuation
 *
 *                              1
 *    attenuation factor = ------------
 *                         c + ld + qdd
 * </pre>
 *
 * note: the default values of ( constant=1, linear=0, quadratic=0 ) reduce the
 * attenuation factor to 1.
 *
 * @author Dennis Cosgrove
 */
public class PointLight extends Light {
  //todo Double -> Float?
  public final DoubleProperty constantAttenuation = new DoubleProperty(this, 1.0);
  //todo Double -> Float?
  public final DoubleProperty linearAttenuation = new DoubleProperty(this, 0.0);
  //todo Double -> Float?
  public final DoubleProperty quadraticAttenuation = new DoubleProperty(this, 0.0);
}

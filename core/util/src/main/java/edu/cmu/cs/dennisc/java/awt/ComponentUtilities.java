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
package edu.cmu.cs.dennisc.java.awt;

import edu.cmu.cs.dennisc.pattern.Criterion;
import edu.cmu.cs.dennisc.pattern.HowMuch;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class ComponentUtilities {
  public static void makeStandOut(Component component) {
    assert component != null;
    if (component instanceof JComponent) {
      JComponent jComponent = (JComponent) component;
      jComponent.setBorder(BorderFactory.createLineBorder(Color.RED, 4));
      jComponent.setOpaque(true);
    }
    component.setBackground(Color.GREEN);
  }

  private static Point getLocation(Point rv, Component c, Component ancestor) {
    assert c != null;
    if (c == ancestor) {
      return rv;
    } else {
      rv.x += c.getX();
      rv.y += c.getY();
      return getLocation(rv, c.getParent(), ancestor);
    }
  }

  private static Point getLocation(Component c, Component ancestor) {
    return getLocation(new Point(), c, ancestor);
  }

  public static Point convertPoint(Component src, int srcX, int srcY, Component dst) {
    Component srcRoot = SwingUtilities.getRoot(src);
    Component dstRoot = SwingUtilities.getRoot(dst);
    //avoid tree lock, if possible
    if ((srcRoot != null) && (srcRoot == dstRoot)) {
      Point srcPt = getLocation(src, srcRoot);
      Point dstPt = getLocation(dst, dstRoot);
      Point rv = srcPt;
      rv.x -= dstPt.x;
      rv.y -= dstPt.y;
      rv.x += srcX;
      rv.y += srcY;
      return rv;
    } else {
      return SwingUtilities.convertPoint(src, srcX, srcY, dst);
    }
  }

  public static Point convertPoint(Component src, Point srcPt, Component dst) {
    return convertPoint(src, srcPt.x, srcPt.y, dst);
  }

  public static Rectangle convertRectangle(Component src, Rectangle srcRect, Component dst) {
    Point pt = convertPoint(src, srcRect.x, srcRect.y, dst);
    return new Rectangle(pt.x, pt.y, srcRect.width, srcRect.height);
  }

  public static final HowMuch DEFAULT_HOW_MUCH = HowMuch.COMPONENT_AND_DESCENDANTS;

  private static <E extends Component> E getFirstToAccept(boolean isComponentACandidate, boolean isChildACandidate, boolean isGrandchildAndBeyondACandidate, Component component, Class<E> cls, Criterion<?>... criterions) {
    assert component != null;
    E rv = null;
    boolean isAcceptedByAll;
    if (isComponentACandidate) {
      if ((cls == null) || cls.isAssignableFrom(component.getClass())) {
        isAcceptedByAll = true;
        if (criterions == null) {
          //pass
        } else {
          for (Criterion criterion : criterions) {
            if (criterion.accept(component)) {
              //pass
            } else {
              isAcceptedByAll = false;
              break;
            }
          }
        }
      } else {
        isAcceptedByAll = false;
      }
    } else {
      isAcceptedByAll = false;
    }
    if (isAcceptedByAll) {
      rv = (E) component;
    } else {
      if (isChildACandidate) {
        if (component instanceof Container) {
          for (Component componentI : ((Container) component).getComponents()) {
            rv = getFirstToAccept(isChildACandidate, isGrandchildAndBeyondACandidate, isGrandchildAndBeyondACandidate, componentI, cls, criterions);
            if (rv != null) {
              break;
            }
          }
        }
      }
    }
    return rv;
  }

  private static <E extends Component> void updateAllToAccept(boolean isComponentACandidate, boolean isChildACandidate, boolean isGrandchildAndBeyondACandidate, List<E> list, Component component, Class<E> cls, Criterion<?>... criterions) {
    assert component != null;

    if (isComponentACandidate) {
      if ((cls == null) || cls.isAssignableFrom(component.getClass())) {
        boolean isAcceptedByAll = true;
        if (criterions == null) {
          //pass
        } else {
          for (Criterion criterion : criterions) {
            if (criterion.accept(component)) {
              //pass
            } else {
              isAcceptedByAll = false;
              break;
            }
          }
        }
        if (isAcceptedByAll) {
          list.add((E) component);
        }
      }
    }

    if (isChildACandidate) {
      if (component instanceof Container) {
        for (Component componentI : ((Container) component).getComponents()) {
          updateAllToAccept(isChildACandidate, isGrandchildAndBeyondACandidate, isGrandchildAndBeyondACandidate, list, componentI, cls, criterions);
        }
      }
    }
  }

  private static <E extends Component> E getFirstToAccept(HowMuch candidateMask, Component component, Class<E> cls, Criterion<?>... criterions) {
    return getFirstToAccept(candidateMask.isComponentACandidate(), candidateMask.isChildACandidate(), candidateMask.isGrandchildAndBeyondACandidate(), component, cls, criterions);
  }

  private static <E extends Component> void updateAllToAccept(HowMuch candidateMask, List<E> list, Component component, Class<E> cls, Criterion<?>... criterions) {
    updateAllToAccept(candidateMask.isComponentACandidate(), candidateMask.isChildACandidate(), candidateMask.isGrandchildAndBeyondACandidate(), list, component, cls, criterions);
  }

  public static <E extends Component> E findFirstMatch(Component component, HowMuch howMuch, Class<E> cls, Criterion<?>... criterions) {
    return ComponentUtilities.getFirstToAccept(howMuch, component, cls, criterions);
  }

  public static <E extends Component> E findFirstMatch(Component component, Class<E> cls, Criterion<?>... criterions) {
    return findFirstMatch(component, DEFAULT_HOW_MUCH, cls, criterions);
  }

  public static <E extends Component> E findFirstMatch(Component component, Class<E> cls) {
    return findFirstMatch(component, cls, (Criterion<?>[]) null);
  }

  public static Component findFirstMatch(Component component, Criterion<?>... criterions) {
    return findFirstMatch(component, null, criterions);
  }

  public static <E extends Component> List<E> findAllMatches(Component component, HowMuch howMuch, Class<E> cls, Criterion<?>... criterions) {
    List<E> list = new LinkedList<E>();
    ComponentUtilities.updateAllToAccept(howMuch, list, component, cls, criterions);
    return list;
  }

  public static <E extends Component> List<E> findAllMatches(Component component, Class<E> cls, Criterion<?>... criterions) {
    return findAllMatches(component, DEFAULT_HOW_MUCH, cls, criterions);
  }

  public static <E extends Component> List<E> findAllMatches(Component component, Class<E> cls) {
    return findAllMatches(component, cls, (Criterion<?>[]) null);
  }

  public static List<Component> findAllMatches(Component component, Criterion<?>... criterions) {
    return findAllMatches(component, null, criterions);
  }

  public static List<Component> findAllMatches(Component component) {
    return findAllMatches(component, null, (Criterion<?>[]) null);
  }

  public static <E extends Component> E findFirstAncestor(Component component, boolean isComponentIncludedInSearch, Class<E> cls, Criterion<?>... criterions) {
    Component c;
    if (isComponentIncludedInSearch) {
      c = component;
    } else {
      c = component.getParent();
    }
    while (c != null) {
      boolean isAcceptedByAll;
      if ((cls == null) || cls.isAssignableFrom(c.getClass())) {
        isAcceptedByAll = true;
        if (criterions == null) {
          //pass
        } else {
          for (Criterion criterion : criterions) {
            if (criterion.accept(component)) {
              //pass
            } else {
              isAcceptedByAll = false;
              break;
            }
          }
        }
      } else {
        isAcceptedByAll = false;
      }
      if (isAcceptedByAll) {
        return (E) c;
      }
      c = c.getParent();
    }
    return null;
  }

  public static <E extends Component> E findFirstAncestor(Component component, boolean isComponentIncludedInSearch, Class<E> cls) {
    return findFirstAncestor(component, isComponentIncludedInSearch, cls, (Criterion<?>[]) null);
  }

  public static Component findFirstAncestor(Component component, boolean isComponentIncludedInSearch, Criterion<?>... criterions) {
    return findFirstAncestor(component, isComponentIncludedInSearch, null, criterions);
  }

  public static void doLayoutTree(Component c) {
    //c.doLayout();
    if (c instanceof Container) {
      Container container = (Container) c;
      for (Component component : container.getComponents()) {
        doLayoutTree(component);
      }
    }
    c.doLayout();
  }

  public static void setSizeToPreferredSizeTree(Component c) {
    if (c instanceof Container) {
      Container container = (Container) c;
      for (Component component : container.getComponents()) {
        setSizeToPreferredSizeTree(component);
      }
    }
    c.setSize(c.getPreferredSize());
  }

  public static void invalidateTree(Component c) {
    c.invalidate();
    if (c instanceof Container) {
      Container container = (Container) c;
      for (Component component : container.getComponents()) {
        invalidateTree(component);
      }
    }
  }

  public static void validateTree(Component c) {
    c.validate();
    if (c instanceof Container) {
      Container container = (Container) c;
      for (Component component : container.getComponents()) {
        validateTree(component);
      }
    }
  }

  public static void revalidateTree(Component c) {
    if (c instanceof JComponent) {
      JComponent jc = (JComponent) c;
      jc.revalidate();
    }
    if (c instanceof Container) {
      Container container = (Container) c;
      for (Component component : container.getComponents()) {
        revalidateTree(component);
      }
    }
  }

  public static Frame getRootFrame(Component c) {
    Component root = SwingUtilities.getRoot(c);
    if (root instanceof Frame) {
      return (Frame) root;
    } else {
      return null;
    }
  }

  public static JFrame getRootJFrame(Component c) {
    Component root = SwingUtilities.getRoot(c);
    if (root instanceof JFrame) {
      return (JFrame) root;
    } else {
      return null;
    }
  }

  public static Dialog getRootDialog(Component c) {
    Component root = SwingUtilities.getRoot(c);
    if (root instanceof Dialog) {
      return (Dialog) root;
    } else {
      return null;
    }
  }

  public static JDialog getRootJDialog(Component c) {
    Component root = SwingUtilities.getRoot(c);
    if (root instanceof JDialog) {
      return (JDialog) root;
    } else {
      return null;
    }
  }
}

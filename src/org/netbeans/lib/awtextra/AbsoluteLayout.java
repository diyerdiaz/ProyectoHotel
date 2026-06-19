package org.netbeans.lib.awtextra;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AbsoluteLayout implements LayoutManager2, Serializable {
    private static final long serialVersionUID = 1L;
    private final Map<Component, AbsoluteConstraints> constraints = new HashMap<>();

    @Override
    public void addLayoutComponent(Component comp, Object constraint) {
        if (constraint instanceof AbsoluteConstraints) {
            constraints.put(comp, (AbsoluteConstraints) constraint);
        } else {
            constraints.put(comp, new AbsoluteConstraints(0, 0));
        }
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
        constraints.put(comp, new AbsoluteConstraints(0, 0));
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        constraints.remove(comp);
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        int maxX = 0;
        int maxY = 0;
        for (Component comp : parent.getComponents()) {
            if (!comp.isVisible()) {
                continue;
            }
            Rectangle bounds = boundsFor(comp);
            maxX = Math.max(maxX, bounds.x + bounds.width);
            maxY = Math.max(maxY, bounds.y + bounds.height);
        }
        Insets insets = parent.getInsets();
        return new Dimension(maxX + insets.left + insets.right, maxY + insets.top + insets.bottom);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return preferredLayoutSize(parent);
    }

    @Override
    public Dimension maximumLayoutSize(Container target) {
        return preferredLayoutSize(target);
    }

    @Override
    public void layoutContainer(Container parent) {
        Insets insets = parent.getInsets();
        for (Component comp : parent.getComponents()) {
            if (!comp.isVisible()) {
                continue;
            }
            Rectangle bounds = boundsFor(comp);
            comp.setBounds(bounds.x + insets.left, bounds.y + insets.top, bounds.width, bounds.height);
        }
    }

    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0.5f;
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0.5f;
    }

    @Override
    public void invalidateLayout(Container target) {
        // Nothing cached.
    }

    private Rectangle boundsFor(Component comp) {
        AbsoluteConstraints c = constraints.get(comp);
        if (c == null) {
            c = new AbsoluteConstraints(0, 0);
        }
        Dimension pref = comp.getPreferredSize();
        int width = c.width >= 0 ? c.width : pref.width;
        int height = c.height >= 0 ? c.height : pref.height;
        return new Rectangle(c.x, c.y, width, height);
    }
}

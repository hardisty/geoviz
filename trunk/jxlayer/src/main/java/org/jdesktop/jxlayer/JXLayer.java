/**
 * Copyright (c) 2006-2008, Alexander Potochkin
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above
 *     copyright notice, this list of conditions and the following
 *     disclaimer in the documentation and/or other materials provided
 *     with the distribution.
 *   * Neither the name of the JXLayer project nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.jdesktop.jxlayer;

import org.jdesktop.jxlayer.plaf.AbstractLayerUI;
import org.jdesktop.jxlayer.plaf.LayerUI;
import org.jdesktop.jxlayer.plaf.item.LayerItemChangeEvent;
import org.jdesktop.jxlayer.plaf.item.LayerItemListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.im.InputContext;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;

/**
 * The universal decorator for Swing components
 * with which you can implement various advanced painting effects
 * as well as receive notification of all {@code MouseEvent}s,
 * {@code KeyEvent}s and {@code FocusEvent}s which generated within its borders.
 * <p/>
 * {@code JXLayer} delegates its painting and input events handling
 * to its {@link LayerUI} object which performs the actual decoration.
 * <p/>
 * The custom painting and events notification automatically work
 * for {@code JXLayer} itself and all its subcomponents.
 * This powerful combination makes it possible to enrich existing components
 * with new advanced functionality such as temporary locking of a hierarchy,
 * data tips for compound components, enhanced mouse scrolling etc...
 * <p/>
 * {@code JXLayer} is a great solution if you just need to do custom painting
 * over compound component or catch input events of its subcomponents.
 * <p/>
 * <pre>
 *         // create a component to be decorated with the layer
 *        JPanel panel = new JPanel();
 *        panel.add(new JButton("JButton"));
 * <p/>
 *        // This custom layerUI will fill the layer with translucent green
 *        // and print out all mouseMotion events generated within its borders
 *        AbstractLayerUI&lt;JPanel&gt; layerUI = new AbstractLayerUI&lt;JPanel&gt;() {
 * <p/>
 *            protected void paintLayer(Graphics2D g2, JXLayer&lt;JPanel&gt; l) {
 *                // this paints the layer as is
 *                super.paintLayer(g2, l);
 *                // fill it with the translucent green
 *                g2.setColor(new Color(0, 128, 0, 128));
 *                g2.fillRect(0, 0, l.getWidth(), l.getHeight());
 *            }
 * <p/>
 *            // overridden method which catches MouseMotion events
 *            protected void processMouseMotionEvent(MouseEvent e) {
 *                System.out.println("MouseMotionEvent detected: "
 *                        + e.getX() + " " + e.getY());
 *            }
 *        };
 * <p/>
 *        // create the layer for the panel using our custom layerUI
 *        JXLayer&lt;JPanel&gt; layer = new JXLayer&lt;JPanel&gt;(panel, layerUI);
 * <p/>
 *        // work with the layer as with any other Swing component
 *        frame.add(layer);
 * </pre>
 * <p/>
 * <b>Note:</b> When a {@code LayerUI} instance is disabled or not set,
 * its {@code JXLayer}s temporary lose all their decorations.
 * <b>Note:</b> {@code JXLayer} is very friendly to your application,
 * it uses only public Swing API and doesn't rely on any global settings
 * like custom {@code RepaintManager} or {@code AWTEventListener}.
 * It neither change the opaque state of its subcomponents
 * nor use the glassPane of its parent frame.
 * <p/>
 * {@code JXLayer} can be used under restricted environment
 * (e.g. unsigned applets)
 *
 * @see #setUI(LayerUI)
 * @see LayerUI
 * @see AbstractLayerUI
 */
public final class JXLayer<V extends JComponent> extends JComponent
        implements Scrollable, LayerItemListener, PropertyChangeListener {
    private V view;
    private JComponent glassPane;
    private boolean isPainting;
    private static final LayerLayout sharedLayoutInstance = new LayerLayout();
    private final InputContext inputContext = new LayerInputContext();
    private boolean isProxyInputContextEnabled = true;

    /**
     * Creates a new {@code JXLayer} object with empty view component
     * and empty {@link LayerUI}.
     *
     * @see #setView
     * @see #setUI
     */
    public JXLayer() {
        this(null);
    }

    /**
     * Creates a new {@code JXLayer} object with empty {@link LayerUI}.
     *
     * @param view the component to be decorated with this {@code JXLayer}
     * @see #setUI
     */
    public JXLayer(V view) {
        this(view, null);
    }

    /**
     * Creates a new {@code JXLayer} object with provided view component
     * and {@link LayerUI} object.
     *
     * @param view the component to be decorated
     * @param ui   the {@link LayerUI} deleagate to be used by this {@code JXLayer}
     */
    public JXLayer(V view, LayerUI<V> ui) {
        setLayout(sharedLayoutInstance);
        setGlassPane(new JXGlassPane());
        setView(view);
        setUI(ui);
    }

    /**
     * Returns the view component for this {@code JXLayer}.
     * <br/>This is a bound property.
     *
     * @return the view component for this {@code JXLayer}
     */
    public V getView() {
        return view;
    }

    /**
     * Sets the view component (the component to be decorated)
     * for this {@code JXLayer}.<br/>This is a bound property.
     *
     * @param view the view component for this {@code JXLayer}
     */
    public void setView(V view) {
        JComponent oldView = getView();
        if (oldView != null) {
            super.remove(oldView);
        }
        if (view != null) {
            super.addImpl(view, null, getComponentCount());
        }
        this.view = view;
        firePropertyChange("view", oldView, view);
        revalidate();
        repaint();
    }

    /**
     * Sets the {@link LayerUI} which will perform painting
     * and receive input events for this {@code JXLayer}.
     *
     * @param ui the {@link LayerUI} for this {@code JXLayer}
     */
    public void setUI(LayerUI<V> ui) {
        if (isValidUI()) {
            disableEvents(getUI().getLayerEventMask());
        }
        super.setUI(ui);
        if (isValidUI()) {
            getGlassPane().setVisible(true);
            enableEvents(ui.getLayerEventMask());
        } else {
            getGlassPane().setVisible(false);
        }
    }

    /**
     * Returns the {@link LayerUI} for this {@code JXLayer}.
     *
     * @return the {@link LayerUI} for this {@code JXLayer}
     */
    @SuppressWarnings("unchecked")
    public LayerUI<V> getUI() {
        return (LayerUI<V>) ui;
    }

    /**
     * Returns the glassPane component of this {@code JXLayer}.
     * <br/>This is a bound property.
     *
     * @return the glassPane component of this {@code JXLayer}
     */
    public JComponent getGlassPane() {
        return glassPane;
    }

    /**
     * Sets the glassPane component of this {@code JXLayer}.
     * <br/>This is a bound property.
     *
     * @param glassPane the glassPane component of this {@code JXLayer}
     */
    public void setGlassPane(JComponent glassPane) {
        if (glassPane == null) {
            throw new IllegalArgumentException("GlassPane can't be set to null");
        }
        JComponent oldGlassPane = getGlassPane();
        if (oldGlassPane != null) {
            super.remove(oldGlassPane);
        }
        super.addImpl(glassPane, null, 0);
        this.glassPane = glassPane;
        firePropertyChange("glassPane", oldGlassPane, glassPane);
        revalidate();
        repaint();
    }

    /**
     * {@code JXLayer} can have only two direct children:
     * the view component and the glassPane,
     * so this method throws {@code UnsupportedOperationException}.
     * <p/>
     * {@inheritDoc}
     *
     * @see #setView
     * @see #setGlassPane
     */
    protected void addImpl(Component comp, Object constraints, int index) {
        if (comp instanceof JComponent) {
            setView((V) comp);
        } else {
            throw new IllegalArgumentException("Component is not instance of JComponent");
        }
    }

    /**
     * Removes the {@code JXLayer}'s view component.
     *
     * @param comp the component to be removed
     */
    public void remove(Component comp) {
        if (comp == getView()) {
            view = null;
        } else if (comp == getGlassPane()) {
            throw new IllegalArgumentException("GlassPane can't be removed");
        }
        super.remove(comp);
    }

    /**
     * Removes the {@code JXLayer}'s view component.
     */
    public void removeAll() {
        setView(null);
    }

    private boolean isValidUI() {
        return getUI() != null && getUI().isEnabled();
    }

    /**
     * Delegates all painting to the {@link LayerUI} object.
     * <p/>
     * If no view component or {@code LayerUI} object is provided,
     * {@link LayerUI#isEnabled()} returns {@code false},
     * any of {@code JXLayer}'s size is less than {@code 1}
     * or {@code g} is not instance of Graphics2D
     * then the super implementation of {@code paint} method is called.
     *
     * @param g the {@code Graphics} to render to
     */
    public void paint(Graphics g) {
        LayerUI<V> layerUI = getUI();
        if (!isPainting && g instanceof Graphics2D && isValidUI()
                && getWidth() > 0 && getHeight() > 0) {
            Graphics2D g2 = (Graphics2D) g.create();
            isPainting = true;
            layerUI.paint(g2, this);
            isPainting = false;
            g2.dispose();
        } else {
            super.paint(g);
        }
    }

    /**
     * To enable the correct painting of the glassPane and view component,
     * the {@code JXLayer} overrides the default implementation of
     * this method to return {@code false} when the glassPane is visible.
     *
     * @return false
     */
    public boolean isOptimizedDrawingEnabled() {
        return !glassPane.isVisible();
    }

    /**
     * This method is public as an implementation side effect.
     * {@code JXLayer} can be registered as a {@code LayerItemListener}
     * and usually receives the  {@code LayerItemChangeEvent}s
     * from its {@code LayerUI}.
     *
     * @param e the {@link LayerItemChangeEvent}
     */
    public void layerItemChanged(LayerItemChangeEvent e) {
        Shape clip = e.getClip(getWidth(), getHeight());
        if (clip != null) {
            repaint(clip.getBounds());
        } else {
            repaint();
        }
    }

    /**
     * {@inheritDoc} 
     */
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        if ("enabled".equals(propertyName)) {
            if (getUI().isEnabled()) {
                getGlassPane().setVisible(true);
                enableEvents(getUI().getLayerEventMask());
            } else {
                getGlassPane().setVisible(false);
                disableEvents(getUI().getLayerEventMask());
            }
        } else if ("layerEventMask".equals(propertyName)) {
            if (getUI().isEnabled()) {
                disableEvents((Long) evt.getOldValue());
                enableEvents(getUI().getLayerEventMask());
            }
        }
    }

    /**
     * Delegates its functionality to the {@link LayerUI#updateUI(JXLayer)} method,
     * if {@code LayerUI} is set and enabled.
     */
    public void updateUI() {
        if (isValidUI()) {
            getUI().updateUI(this);
        }
    }

    /**
     * Delegates its functionality to the {@link LayerUI#contains(JComponent, int, int)} method,
     * if {@code LayerUI} is set and enabled.
     * <p/>
     * {@inheritDoc}
     */
    public boolean contains(int x, int y) {
        if (isValidUI()) {
            return getUI().contains(this, x, y);
        }
        // the implementation of Component.inside(int, int) method
        // which is eventually called by super.contains(int, int)
        // inside() is deprecated, so I copied this line here
        return (x >= 0) && (x < getWidth()) && (y >= 0) && (y < getHeight());
    }

    /**
     * Returns the preferred size of the viewport for a view component.
     * <p/>
     * If the ui delegate of this layer is not null, this method delegates its
     * implementation to the {@code LayerUI.getPreferredScrollableViewportSize(JXLayer)}
     *
     * @return the preferred size of the viewport for a view component
     * @see Scrollable
     * @see LayerUI#getPreferredScrollableViewportSize(JXLayer)
     */
    public Dimension getPreferredScrollableViewportSize() {
        if (getUI() != null) {
            return getUI().getPreferredScrollableViewportSize(this);
        }
        return getPreferredSize();
    }

    /**
     * Components that display logical rows or columns should compute
     * the scroll increment that will completely expose one block
     * of rows or columns, depending on the value of orientation.
     * <p/>
     * If the ui delegate of this layer is not null, this method delegates its
     * implementation to the {@code LayerUI.getScrollableBlockIncrement(JXLayer,Rectangle,int,int)}
     *
     * @return the "block" increment for scrolling in the specified direction
     * @see Scrollable
     * @see LayerUI#getScrollableBlockIncrement(JXLayer, Rectangle, int, int)
     */
    public int getScrollableBlockIncrement(Rectangle visibleRect, 
                                           int orientation, int direction) {
        if (getUI() != null) {
            return getUI().getScrollableBlockIncrement(this, visibleRect,
                    orientation, direction);
        }
        return (orientation == SwingConstants.VERTICAL) ? visibleRect.height :
                visibleRect.width;
    }

    /**
     * Returns false to indicate that the height of the viewport does not
     * determine the height of the layer, unless the preferred height
     * of the layer is smaller than the viewports height.
     * <p/>
     * If the ui delegate of this layer is not null, this method delegates its
     * implementation to the {@code LayerUI.getScrollableTracksViewportHeight(JXLayer)}
     *
     * @return whether the layer should track the height of the viewport
     * @see Scrollable
     * @see LayerUI#getScrollableTracksViewportHeight(JXLayer)
     */
    public boolean getScrollableTracksViewportHeight() {
        if (getUI() != null) {
            return getUI().getScrollableTracksViewportHeight(this);
        }
        if (getParent() instanceof JViewport) {
            return ((getParent()).getHeight() > getPreferredSize().height);
        }
        return false;
    }

    /**
     * Returns false to indicate that the width of the viewport does not
     * determine the width of the layer, unless the preferred width
     * of the layer is smaller than the viewports width.
     * <p/>
     * If the ui delegate of this layer is not null, this method delegates its
     * implementation to the {@code LayerUI.getScrollableTracksViewportWidth(JXLayer)}
     *
     * @return whether the layer should track the width of the viewport
     * @see Scrollable
     * @see LayerUI#getScrollableTracksViewportWidth(JXLayer)
     */
    public boolean getScrollableTracksViewportWidth() {
        if (getUI() != null) {
            return getUI().getScrollableTracksViewportWidth(this);
        }
        if (getParent() instanceof JViewport) {
            return ((getParent()).getWidth() > getPreferredSize().width);
        }
        return false;
    }

    /**
     * Components that display logical rows or columns should compute
     * the scroll increment that will completely expose one new row
     * or column, depending on the value of orientation.  Ideally,
     * components should handle a partially exposed row or column by
     * returning the distance required to completely expose the item.
     * <p/>
     * Scrolling containers, like JScrollPane, will use this method
     * each time the user requests a unit scroll.
     * <p/>
     * If the ui delegate of this layer is not null, this method delegates its
     * implementation to the {@code LayerUI.getScrollableUnitIncrement(JXLayer,Rectangle,int,int)}
     *
     * @return The "unit" increment for scrolling in the specified direction.
     *         This value should always be positive.
     * @see Scrollable
     * @see LayerUI#getScrollableUnitIncrement(JXLayer, Rectangle, int, int)
     */
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation,
                                          int direction) {
        if (getUI() != null) {
            return getUI().getScrollableUnitIncrement(
                    this, visibleRect, orientation, direction);
        }
        return 1;
    }

    /**
     * Returns {@code true} is the proxy InputContext is enabled for this JXLayer.
     * <p/>
     * When the proxy InputContext is enabled, the JXLayer will notify its LayerUI
     * about keyboard, mouse & focus events that are generated for this JXLayer
     * or any of its subcomponent
     * <p/>
     * The default value for this property is {@code true}
     * <p/>
     * For more informaiton please see
     * <a href="http://weblogs.java.net/blog/alexfromsun/archive/2008/07/jxlayer_30_even.html">JXLayer 3.0 - Event handling</a>
     *
     * @return {@code true} is the proxy InputContext is enabled for this JXLayer
     * @see LayerUI#eventDispatched(AWTEvent, JXLayer)
     */
    public boolean isProxyInputContextEnabled() {
        return isProxyInputContextEnabled;
    }

    /**
     * Sets if the the proxy InputContext is enabled for this JXLayer
     * <p/>
     * If the proxy InputContext is enabled, the JXLayer will notify its LayerUI
     * about keyboard, mouse & focus events that are generated for this JXLayer
     * or any of its subcomponent
     * <p/>
     * A LayerUI subclass may implement a different way of catching events,
     * via ordinary listeners or global {@link AWTEventListener}
     * <p/>
     * The default value for this property is {@code true}
     * <p/>
     * For more informaiton please see
     * <a href="http://weblogs.java.net/blog/alexfromsun/archive/2008/07/jxlayer_30_even.html">JXLayer 3.0 - Event handling</a>
     *
     * @param isEnabled {@code true} if the proxy InputContext is enabled,
     *                  otherwise {@code false}
     * @see LayerUI#eventDispatched(AWTEvent, JXLayer)
     */
    public void setProxyInputContextEnabled(boolean isEnabled) {
        isProxyInputContextEnabled = isEnabled;
    }

    /**
     * Returns the proxy input context which is used to catch all input events
     * and focus events from the subcomponents of this {@code JXLayer}.
     * <p/>
     * When input event is happened and {@code LayerUI} is set and enabled
     * then this proxy input context notifies
     * the {@link LayerUI#eventDispatched(AWTEvent, JXLayer)} method
     * and then calls the super implementation.
     *
     * @return the private proxy {@code InputContext} instance
     */
    public InputContext getInputContext() {
        if (isProxyInputContextEnabled()) {
            return super.getInputContext() == null ? null : inputContext;
        }
        return super.getInputContext();
    }

    private class LayerInputContext extends InputContext {

        private InputContext getContext() {
            return JXLayer.super.getInputContext();
        }

        public void dispatchEvent(AWTEvent event) {
            if(getContext() == null) {
                return;
            }
            if (isValidUI() && getUI().isEventEnabled(event.getID())
                    && (event instanceof InputEvent || event instanceof FocusEvent)) {
                getUI().eventDispatched(event, JXLayer.this);
            }
            getContext().dispatchEvent(event);
        }

        public void dispose() {
            if(getContext() == null) {
                return;
            }
            getContext().dispose();
        }

        public void endComposition() {
            if(getContext() == null) {
                return;
            }
            getContext().endComposition();
        }

        public Object getInputMethodControlObject() {
            if(getContext() == null) {
                return null;
            }
            return getContext().getInputMethodControlObject();
        }

        public Locale getLocale() {
            if(getContext() == null) {
                return null;
            }
            return getContext().getLocale();
        }

        public boolean isCompositionEnabled() {
            if(getContext() == null) {
                return false;
            }
            return getContext().isCompositionEnabled();
        }

        public void reconvert() {
            if(getContext() == null) {
                return;
            }
            getContext().reconvert();
        }

        public void removeNotify(Component client) {
            if(getContext() == null) {
                return;
            }
            getContext().removeNotify(client);
        }

        public boolean selectInputMethod(Locale locale) {
            if(getContext() == null) {
                return false;
            }
            return getContext().selectInputMethod(locale);
        }

        public void setCharacterSubsets(Character.Subset[] subsets) {
            if(getContext() == null) {
                return;
            }
            getContext().setCharacterSubsets(subsets);
        }

        public void setCompositionEnabled(boolean enable) {
            if(getContext() == null) {
                return;
            }
            getContext().setCompositionEnabled(enable);
        }
    }
}



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

package org.jdesktop.jxlayer.plaf;

import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.jxlayer.plaf.item.LayerItemChangeEvent;
import org.jdesktop.jxlayer.plaf.item.LayerItemChangeSupport;
import org.jdesktop.jxlayer.plaf.item.LayerItemListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * The {@code AbstractLayerUI} provided default implementation for most
 * of the abstract methods in the {@link LayerUI} class.
 * It takes care of the management of {@code LayerItemListener}s and
 * defines the hook method to configure the {@code Graphics2D} instance
 * specified in the {@link #paint(Graphics,JComponent)} method.
 * It also provides convenient methods named
 * {@code process<eventType>Event} to process the given class of event.
 * <p/>
 * If state of the {@code AbstractLayerUI} is changed, call {@link #setDirty(boolean)}
 * with {@code true} as the parameter, it will repaint all {@code JXLayer}s
 * connected with this {@code AbstractLayerUI}
 *
 * @see JXLayer#setUI(LayerUI)
 */
public class AbstractLayerUI<V extends JComponent>
        extends LayerUI<V> {
    private static final LayerEventController eventController = new LayerEventController();
    private long eventMask = super.getLayerEventMask();

    private final LayerItemChangeSupport layerChangeSupport = new LayerItemChangeSupport(this);
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private static final Map<RenderingHints.Key, Object> emptyRenderingHintMap =
            Collections.unmodifiableMap(new HashMap<RenderingHints.Key, Object>(0));
    private boolean enabled = true;
    private boolean isDirty;

    /**
     * Configures the specified {@link JXLayer} appropriate for this {@code AbstractLayerUI}.
     * <p/>
     * This method is invoked when the {@code LayerUI} instance is being installed
     * as the UI delegate on the specified {@code JXLayer}.
     * Subclasses can install any listeners to the passed {@code JXLayer},
     * configure its glassPane or do any other setting up.
     * The default implementation registers the passed {@code JXLayer}
     * as a {@link LayerItemListener} for this {@code AbstractLayerUI}.
     * <p/>
     * <b>Note:</b> Subclasses can safely cast the passed component
     * to the {@code JXLayer<V>}
     *
     * @param c the {@code JXLayer} where this UI delegate is being installed
     * @see #uninstallUI(JComponent)
     */
    @Override
    @SuppressWarnings("unchecked")
    public void installUI(JComponent c) {
        JXLayer l = (JXLayer) c;
        addLayerItemListener(l);
        addPropertyChangeListener(l);
        if (isAWTEventListenerEnabled()) {
            l.setProxyInputContextEnabled(false);
            registerAWTEventListener(l, getLayerEventMask());
        }
    }

    /**
     * Reverses configuration which was done on the specified component during
     * {@code installUI(JComponent)}.  This method is invoked when this
     * {@code LayerUI} instance is being removed as the UI delegate
     * for the specified {@link JXLayer}.
     * <p/>
     * {@code uninstallUI(JComponent)} should undo the
     * configuration performed in {@code installUI(JComponent)}, being careful to
     * leave the {@code JXLayer} instance in a clean state
     * (e.g. all previously set listeners must be removed in this method).
     * The default implementation removes the passed {@code JXLayer}
     * from the {@link LayerItemListener}'s list of this {@code AbstractLayerUI}.
     * <p/>
     * <b>Note:</b> Subclasses can safely cast the passed component
     * to the {@code JXLayer<V>}
     *
     * @param c the {@code JXLayer} where this UI delegate is being installed
     * @see #installUI(JComponent)
     */
    @Override
    @SuppressWarnings("unchecked")
    public void uninstallUI(JComponent c) {
        JXLayer l = (JXLayer) c;
        removeLayerItemListener(l);
        removePropertyChangeListener(l);
        if (isAWTEventListenerEnabled()) {
            unregisterAWTEventListener(l);
            l.setProxyInputContextEnabled(true);
        }
    }

    /**
     * This is an alternative implementation of catching JXLayer's events.
     * When this method is called, the proxy InputContext of the passed JXLayer
     * is disabled and an AWTEventListener starts listen events for this JXLayer
     * and all its subcomponents.
     * <p/>
     * If there is a security manager, its <code>checkPermission</code>
     * method is called with an
     * <code>AWTPermission("listenToAllAWTEvents")</code> permission.
     * This may result in a SecurityException.
     * <p/>
     * <code>eventMask</code> is a bitmask of event types to receive.
     * It is constructed by bitwise OR-ing together the event masks
     * defined in <code>AWTEvent</code>.
     * <p/>
     * For example, the following mask includes all mouse events:
     * <pre>
     * AWTEvent.MOUSE_EVENT_MASK |
     *           AWTEvent.MOUSE_MOTION_EVENT_MASK |
     *           AWTEvent.MOUSE_WHEEL_EVENT_MASK
     * </pre>
     * It is guaranteed that this {@link AbstractLayerUI#eventDispatched(AWTEvent, JXLayer)}
     * will be notified of any event inside layer's hierarchy that matched with the eventMask
     * A specific AWTEventListener's implementation may also pass some events
     * that don't match with the eventMask, but you shouldn't rely on it.
     * <p/>
     * <b>Note:</b> It is unlikely that you will ever need to manually call this method,
     * if you need to enable the AWTEventListener, just override {@link #isAWTEventListenerEnabled()}
     * together with {@link #getLayerEventMask()}.
     * <p/>
     * For more informaiton please see
     * <a href="http://weblogs.java.net/blog/alexfromsun/archive/2008/07/jxlayer_30_even.html">JXLayer 3.0 - Event handling</a>
     *
     * @param l         the {@code JXLayer} to listen events for
     * @param eventMask the bitmask of event types to receive
     * @see #isAWTEventListenerEnabled()
     * @see #getLayerEventMask()
     * @see #unregisterAWTEventListener(JXLayer)
     */
    protected void registerAWTEventListener(JXLayer<V> l, long eventMask) {
        eventController.register(l, eventMask);
    }

    /**
     * Unregister an event listener which was previously set
     * by {@link #registerAWTEventListener(JXLayer, long)}
     * and also enables back the proxy InputContext of the passed JXLayer.
     * <p/>
     * <b>Note:</b> If you call {@link #registerAWTEventListener (JXLayer, long)}
     * from the {@code installUI()}, you must call this method in the
     * {@link #uninstallUI(JComponent)} to release resources.
     * <p/>
     * <b>Note:</b> It is unlikely that you will ever need to manually call this method,
     * if you need to enable the AWTEventListener, just override {@link #isAWTEventListenerEnabled()}
     * together with {@link #getLayerEventMask()}.
     *
     * @param l the {@code JXLayer} to unregister the listener for
     * @see #registerAWTEventListener(JXLayer, long)
     */
    protected void unregisterAWTEventListener(JXLayer<V> l) {
        eventController.unregister(l);
    }

    /**
     * Returns {@code true} if this {@code AbstractLayerUI} catches AWT events
     * with help of {@code AWTEventListener},
     * in this case {@link #registerAWTEventListener(JXLayer, long)}
     * will be called from {@code installUI(JComponent)}
     * with {@link #getLayerEventMask()} as the second parameter
     * and {@link #unregisterAWTEventListener(JXLayer)}
     * will be called from {@code uninstallUI(JComponent)}
     * <p/>
     * If this method is overridden to return {@code true}
     * don't forget to override {@code getAWTEventListenerEventMask()}
     * <p/>
     * The default implementation returns {@code false}
     *
     * @return {@code true} if this {@code AbstractLayerUI} catches AWT events
     *         with help of {@code AWTEventListener}
     * @see #getLayerEventMask ()
     * @see #registerAWTEventListener(JXLayer, long)
     */
    protected boolean isAWTEventListenerEnabled() {
        return false;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * By default only mouse, mouse motion, mouse wheel, keyboard and focus events are supported,
     * all other events included in the eventMask will be ignored.
     * To enable the rest of the events, like {@code HierarchyEvent}s
     * you should override {@code isAWTEventListenerEnabled()} to return true.
     * 
     * @see #setLayerEventMask(long) 
     * @see #isAWTEventListenerEnabled() 
     */
    public long getLayerEventMask() {
        return eventMask;
    }

    /**
     * Sets the eventMask for this {@code AbstractLayerUI}
     * and all its {@code JXLayer}s
     * <p/>
     * It means that {@link #eventDispatched(AWTEvent, JXLayer)} method
     * will only receive events that match the event mask.
     * <pre>
     *    //Call from the ui's constructor to receive only keyboard and focus events:
     *    setLayerEventMask(AWTEvent.KEY_EVENT_MASK | AWTEvent.FOCUS_EVENT_MASK);
     * </pre>
     * <p/>
     * By default only mouse, mouse motion, mouse wheel, keyboard and focus events are supported,
     * all other events included in the eventMask will be ignored.
     * To enable the rest of the events, like {@code HierarchyEvent}s
     * you should override {@code isAWTEventListenerEnabled()} to return true.
     *
     * @param layerEventMask the bitmask of event types to receive
     * @see #getLayerEventMask()
     */
    public void setLayerEventMask(long layerEventMask) {
        long oldEventMask = getLayerEventMask();
        this.eventMask = layerEventMask;
        firePropertyChange("layerEventMask", oldEventMask, layerEventMask);
    }

    /**
     * {@inheritDoc}
     */
    public void addLayerItemListener(LayerItemListener l) {
        layerChangeSupport.addLayerItemListener(l);
    }

    /**
     * {@inheritDoc}
     */
    public LayerItemListener[] getLayerItemListeners() {
        return layerChangeSupport.getLayerItemListeners();
    }

    /**
     * {@inheritDoc}
     */
    public void removeLayerItemListener(LayerItemListener l) {
        layerChangeSupport.removeLayerItemListener(l);
    }

    /**
     * Notifies all {@code LayerItemListener}s that
     * have been added to this object.
     *
     * @see #addLayerItemListener(LayerItemListener)
     */
    protected void fireLayerItemChanged() {
        layerChangeSupport.fireLayerItemChanged();
    }

    /**
     * Notifies all {@code LayerItemListener}s that
     * have been added to this object.
     *
     * @param event the {@code LayerItemChangeEvent}
     * @see #addLayerItemListener(LayerItemListener)
     */
    protected void fireLayerItemChanged(LayerItemChangeEvent event) {
        layerChangeSupport.fireLayerItemChanged(event);
    }

    /**
     * Adds a PropertyChangeListener to the listener list. The listener is
     * registered for all bound properties of this class.
     * <p/>
     * If <code>listener</code> is <code>null</code>,
     * no exception is thrown and no action is performed.
     *
     * @param listener the property change listener to be added
     * @see #removePropertyChangeListener
     * @see #getPropertyChangeListeners
     * @see #addPropertyChangeListener(String, PropertyChangeListener)
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Removes a PropertyChangeListener from the listener list. This method
     * should be used to remove PropertyChangeListeners that were registered
     * for all bound properties of this class.
     * <p>
     * If listener is null, no exception is thrown and no action is performed.
     *
     * @param listener the PropertyChangeListener to be removed
     *
     * @see #addPropertyChangeListener
     * @see #getPropertyChangeListeners
     * @see #removePropertyChangeListener(String, PropertyChangeListener)
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    /**
     * Returns an array of all the property change listeners
     * registered on this component.
     *
     * @return all of this ui's <code>PropertyChangeListener</code>s
     *         or an empty array if no property change
     *         listeners are currently registered
     *
     * @see      #addPropertyChangeListener
     * @see      #removePropertyChangeListener
     * @see      #getPropertyChangeListeners(String)
     */
    public PropertyChangeListener[] getPropertyChangeListeners() {
        return propertyChangeSupport.getPropertyChangeListeners();
    }

    /**
     * Adds a PropertyChangeListener to the listener list for a specific
     * property. 
     * <p>
     * If <code>propertyName</code> or <code>listener</code> is <code>null</code>,
     * no exception is thrown and no action is taken.
     *
     * @param propertyName one of the property names listed above
     * @param listener the property change listener to be added
     *
     * @see #removePropertyChangeListener(String, PropertyChangeListener)
     * @see #getPropertyChangeListeners(String)
     * @see #addPropertyChangeListener(String, PropertyChangeListener)
     */
    public void addPropertyChangeListener(String propertyName,
                                          PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    /**
     * Removes a <code>PropertyChangeListener</code> from the listener
     * list for a specific property. This method should be used to remove
     * <code>PropertyChangeListener</code>s
     * that were registered for a specific bound property.
     * <p>
     * If <code>propertyName</code> or <code>listener</code> is <code>null</code>,
     * no exception is thrown and no action is taken.
     *
     * @param propertyName a valid property name
     * @param listener the PropertyChangeListener to be removed
     *
     * @see #addPropertyChangeListener(String, PropertyChangeListener)
     * @see #getPropertyChangeListeners(String)
     * @see #removePropertyChangeListener(PropertyChangeListener)
     */
    public void removePropertyChangeListener(String propertyName,
                                             PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
    }

    /**
     * Returns an array of all the listeners which have been associated 
     * with the named property.
     *
     * @return all of the <code>PropertyChangeListener</code>s associated with
     *         the named property; if no such listeners have been added or
     *         if <code>propertyName</code> is <code>null</code>, an empty
     *         array is returned
     *
     * @see #addPropertyChangeListener(String, PropertyChangeListener)
     * @see #removePropertyChangeListener(String, PropertyChangeListener)
     * @see #getPropertyChangeListeners
     */
    public PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
        return propertyChangeSupport.getPropertyChangeListeners(propertyName);
    }

    /**
     * Support for reporting bound property changes for Object properties. 
     * This method can be called when a bound property has changed and it will
     * send the appropriate PropertyChangeEvent to any registered
     * PropertyChangeListeners.
     *
     * @param propertyName the property whose value has changed
     * @param oldValue the property's previous value
     * @param newValue the property's new value
     */
    protected void firePropertyChange(String propertyName,
                                      Object oldValue, Object newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    /**
     * {@inheritDoc}
     * <p/>
     * {@code AbstractLayerUI} is enabled initially by default.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Enables or disables this component, depending on the value of the
     * parameter {@code enabled}. An enabled {@code LayerUI} paints
     * the {@link JXLayer}s components they are set to and receives
     * their input and focuse events.
     * <p/>
     * {@code AbstractLayerUI} is enabled initially by default.
     *
     * @param enabled If {@code true}, this {@code AbstractLayerUI} is
     *                enabled; otherwise this {@code AbstractLayerUI} is disabled
     */
    public void setEnabled(boolean enabled) {
        boolean oldEnabled = isEnabled();
        this.enabled = enabled;
        firePropertyChange("enabled", oldEnabled, enabled);
        if (oldEnabled != enabled) {
            // I don't use setDirty(true) here because I need the layer 
            // to be repainted even if the dirty flag was already set
            fireLayerItemChanged();
        }
    }

    /**
     * Returns the "dirty bit".
     * If {@code true}, then the {@code AbstractLayerUI} is considered dirty
     * and in need of being repainted.
     *
     * @return {@code true} if the {@code AbstractLayerUI} state has changed
     *         and the {@link JXLayer}s it is set to need to be repainted.
     */
    protected boolean isDirty() {
        return isDirty;
    }

    /**
     * Sets the "dirty bit".
     * If {@code isDirty} is {@code true}, then the {@code AbstractLayerUI}
     * is considered dirty and it triggers the repainting
     * of the {@link JXLayer}s this {@code AbstractLayerUI} it is set to.
     *
     * @param isDirty whether this {@code AbstractLayerUI} is dirty or not.
     */
    protected void setDirty(boolean isDirty) {
        boolean oldDirty = isDirty();
        this.isDirty = isDirty;
        if (isDirty && !oldDirty) {
            fireLayerItemChanged();
        }
    }

    /**
     * {@inheritDoc}
     * <p/>
     * <b>Note:</b> It is rarely necessary to override this method, for
     * custom painting override {@link #paintLayer(Graphics2D,JXLayer)} instead
     * <p/>
     * This method configures the passed {@code Graphics} with help of the
     * {@link #configureGraphics(Graphics2D,JXLayer)} method,
     * then calls {@code paintLayer(Graphics2D,JXLayer)}
     * and resets the "dirty bit" at the end.
     *
     * @see #configureGraphics(Graphics2D,JXLayer)
     * @see #paintLayer(Graphics2D,JXLayer)
     * @see #setDirty(boolean)
     */
    @SuppressWarnings("unchecked")
    public void paint(Graphics g, JComponent c) {
        Graphics2D g2 = (Graphics2D) g;
        JXLayer<V> l = (JXLayer<V>) c;
        configureGraphics(g2, l);
        paintLayer(g2, l);
        setDirty(false);
    }

    /**
     * Subclasses should implement this method
     * and perform custom painting operations here.
     * <p/>
     * The default implementation paints the passed {@code JXLayer} as is.
     *
     * @param g2 the {@code Graphics2D} context in which to paint
     * @param l  the {@code JXLayer} being painted
     */
    protected void paintLayer(Graphics2D g2, JXLayer<V> l) {
        l.paint(g2);
    }

    /**
     * This method is called by the {@link #paint} method prior to
     * any drawing operations to configure the {@code Graphics2D} object.
     * The default implementation sets the {@link Composite}, the clip,
     * {@link AffineTransform} and rendering hints
     * obtained from the corresponding hook methods.
     *
     * @param g2 the {@code Graphics2D} object to configure
     * @param l  the {@code JXLayer} being painted
     * @see #getComposite(JXLayer)
     * @see #getClip(JXLayer)
     * @see #getTransform(JXLayer)
     * @see #getRenderingHints(JXLayer)
     */
    protected void configureGraphics(Graphics2D g2, JXLayer<V> l) {
        Composite composite = getComposite(l);
        if (composite != null) {
            g2.setComposite(composite);
        }
        Shape clip = getClip(l);
        if (clip != null) {
            g2.clip(clip);
        }
        AffineTransform transform = getTransform(l);
        if (transform != null) {
            g2.transform(transform);
        }
        Map<RenderingHints.Key, Object> hints = getRenderingHints(l);
        if (hints != null) {
            for (RenderingHints.Key key : hints.keySet()) {
                Object value = hints.get(key);
                if (value != null) {
                    g2.setRenderingHint(key, hints.get(key));
                }
            }
        }
    }

    /**
     * Returns the {@link Composite} to be used during painting of this {@code JXLayer},
     * the default implementation returns {@code null}.
     *
     * @param l the {@code JXLayer} being painted
     * @return the {@link Composite} to be used during painting for the {@code JXLayer}
     */
    protected Composite getComposite(JXLayer<V> l) {
        return null;
    }

    /**
     * Returns the {@link AffineTransform} to be used during painting of this {@code JXLayer},
     * the default implementation returns {@code null}.
     *
     * @param l the {@code JXLayer} being painted
     * @return the {@link AffineTransform} to be used during painting of the {@code JXLayer}
     */
    protected AffineTransform getTransform(JXLayer<V> l) {
        return null;
    }

    /**
     * Returns the {@link Shape} to be used as the clip during painting of this {@code JXLayer},
     * the default implementation returns {@code null}.
     *
     * @param l the {@code JXLayer} being painted
     * @return the {@link Shape} to be used as the clip during painting of the {@code JXLayer}
     */
    protected Shape getClip(JXLayer<V> l) {
        return null;
    }

    /**
     * Returns the map of rendering hints to be used during painting of this {@code JXLayer},
     * the default implementation returns the empty unmodifiable map.
     *
     * @param l the {@code JXLayer} being painted
     * @return the map of rendering hints to be used during painting of the {@code JXLayer}
     */
    protected Map<RenderingHints.Key, Object> getRenderingHints(JXLayer<V> l) {
        return emptyRenderingHintMap;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * This method calls the appropriate
     * {@code process<eventType>Event}
     * method for the given class of event.
     */
    @Override
    public void eventDispatched(AWTEvent e, JXLayer<V> l) {
        if (e instanceof FocusEvent) {
            processFocusEvent((FocusEvent) e, l);
        } else if (e instanceof MouseEvent) {
            switch (e.getID()) {
                case MouseEvent.MOUSE_PRESSED:
                case MouseEvent.MOUSE_RELEASED:
                case MouseEvent.MOUSE_CLICKED:
                case MouseEvent.MOUSE_ENTERED:
                case MouseEvent.MOUSE_EXITED:
                    processMouseEvent((MouseEvent) e, l);
                    break;
                case MouseEvent.MOUSE_MOVED:
                case MouseEvent.MOUSE_DRAGGED:
                    processMouseMotionEvent((MouseEvent) e, l);
                    break;
                case MouseEvent.MOUSE_WHEEL:
                    processMouseWheelEvent((MouseWheelEvent) e, l);
                    break;
            }
        } else if (e instanceof KeyEvent) {
            processKeyEvent((KeyEvent) e, l);
        }
    }

    /**
     * Processes {@code FocusEvent} occurring on the {@link JXLayer}
     * or any of its subcomponents.
     *
     * @param e the {@code FocusEvent} to be processed
     * @param l the layer this LayerUI is set to
     */
    protected void processFocusEvent(FocusEvent e, JXLayer<V> l) {
    }

    /**
     * Processes {@code MouseEvent} occurring on the {@link JXLayer}
     * or any of its subcomponents.
     *
     * @param e the {@code MouseEvent} to be processed
     * @param l the layer this LayerUI is set to
     */
    protected void processMouseEvent(MouseEvent e, JXLayer<V> l) {
    }

    /**
     * Processes mouse motion events occurring on the {@link JXLayer}
     * or any of its subcomponents.
     *
     * @param e the {@code MouseEvent} to be processed
     * @param l the layer this LayerUI is set to
     */
    protected void processMouseMotionEvent(MouseEvent e, JXLayer<V> l) {
    }

    /**
     * Processes {@code MouseWheelEvent} occurring on the {@link JXLayer}
     * or any of its subcomponents.
     *
     * @param e the {@code MouseWheelEvent} to be processed
     * @param l the layer this LayerUI is set to
     */
    protected void processMouseWheelEvent(MouseWheelEvent e, JXLayer<V> l) {
    }

    /**
     * Processes {@code KeyEvent} occurring on the {@link JXLayer}
     * or any of its subcomponents.
     *
     * @param e the {@code KeyEvent} to be processed
     * @param l the layer this LayerUI is set to
     */
    protected void processKeyEvent(KeyEvent e, JXLayer<V> l) {
    }

    /**
     * static AWTEventListener to be shared with all AbstractLayerUIs
     *
     * @see AbstractLayerUI#registerAWTEventListener(JXLayer, long)
     * @see AbstractLayerUI#unregisterAWTEventListener(JXLayer)
     */
    private static class LayerEventController implements AWTEventListener {
        private WeakHashMap<Component, Long> layerMap = new WeakHashMap<Component, Long>();
        private boolean isAWTEventListenerAdded;
        private long currentEventMask;

        @SuppressWarnings("unchecked")
        public void eventDispatched(AWTEvent event) {
            Object source = event.getSource();
            if (source instanceof Component) {
                if (layerMap.isEmpty()) {
                    removeAWTEventListener();
                } else {
                    Component component = (Component) source;
                    while (component != null) {
                        if (component instanceof JXLayer) {
                            JXLayer l = (JXLayer) component;
                            if (layerMap.containsKey(l)
                                    && l.getUI() != null && l.getUI().isEnabled()
                                    && l.getUI().isEventEnabled(event.getID())) {
                                l.getUI().eventDispatched(event, l);
                            }
                        }
                        component = component.getParent();
                    }
                }
            }
        }

        public void register(Component layer, long eventMask) {
            if (layer == null) {
                throw new IllegalArgumentException("Layer is null");
            }
            if (eventMask != 0) {
                layerMap.put(layer, eventMask);
                if ((eventMask & getCurrentEventMask()) != eventMask) {
                    updateAWTEventListener();
                }
            }
        }

        private void updateAWTEventListener() {
            if (isAWTEventListenerAdded()) {
                removeAWTEventListener();
            }
            if (!layerMap.isEmpty()) {
                long combinedMask = 0;
                for (Long mask : layerMap.values()) {
                    combinedMask |= mask;
                }
                addAWTEventListener(combinedMask);
            }
        }

        public void unregister(Component layer) {
            if (layer == null) {
                throw new IllegalArgumentException("Layer is null");
            }
            Long mask = layerMap.remove(layer);
            if (mask != null) {
                updateAWTEventListener();
            }
        }

        private long getCurrentEventMask() {
            return currentEventMask;
        }

        public boolean isAWTEventListenerAdded() {
            return isAWTEventListenerAdded;
        }

        private void addAWTEventListener(long eventMask) {
            Toolkit.getDefaultToolkit().addAWTEventListener(this, eventMask);
            currentEventMask = eventMask;
            isAWTEventListenerAdded = true;
        }

        private void removeAWTEventListener() {
            Toolkit.getDefaultToolkit().removeAWTEventListener(this);
            isAWTEventListenerAdded = false;
        }
    }
}

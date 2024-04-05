package org.maggdadev.forestpixel.canvas.events;

public interface CanvasEventHandler<EventType> {
    /**
     *
     * @param event
     * @return returns whether the event handling left the canvas dirty
     */
    public boolean handleEvent(EventType event);

    public static <T> CanvasEventHandler<T> empty() {
        return event -> false;
    }
}

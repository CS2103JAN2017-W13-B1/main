package utask.staging.ui.helper;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

/**
 *  Provides a way to delay execution in order to prevent race conditions
 *  Inspired by: http://tomasmikula.github.io/blog/2014/06/04/timers-in-javafx-and-reactfx.html
 */
public class DelayedExecution {
    private static final int DEFAULT_WAIT_TIME = 300;
    private Timeline function;

    public DelayedExecution(EventHandler<ActionEvent> delegate) {
        this(DEFAULT_WAIT_TIME, delegate);
    }

    public DelayedExecution(int waitTimeInMilliSeconds, EventHandler<ActionEvent> delegate) {
        assert delegate != null : "Function cannot be null";
        assert waitTimeInMilliSeconds >= 0 : "Time to wait cannot be negative";

        KeyFrame keyFrame = new KeyFrame(Duration.millis(waitTimeInMilliSeconds), delegate);
        function = new Timeline(keyFrame);
    }

    public void run() {
        function.play();
    }
}

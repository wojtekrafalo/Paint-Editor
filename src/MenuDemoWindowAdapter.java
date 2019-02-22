import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Class used to close whole Project after closing a window.
 */
public class MenuDemoWindowAdapter extends WindowAdapter {
    /**
     * method inherited from WindowAdapter, describes action after shutting down a window.
     * @param e event of a closing window.
     */
    public void windowClosing(WindowEvent e) { System.exit(0); }
}


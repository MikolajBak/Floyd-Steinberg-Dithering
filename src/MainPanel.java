import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class MainPanel extends JPanel implements ChangeListener {

    private final ImagePanel imagePanel;

    public MainPanel() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));



        imagePanel = new ImagePanel();

        JSlider thresholdSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, (int)(imagePanel.getThreshold() * 100));
        thresholdSlider.addChangeListener(this);


        add(imagePanel);
        add(thresholdSlider);

        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));




    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();
        if(!source.getValueIsAdjusting()) {
            int threshold = source.getValue();
            imagePanel.updateThreshold(threshold / 100.);
            update(this.getGraphics());
        }
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Floyd-Steinberg");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MainPanel mainPanel = new MainPanel();

        frame.add(mainPanel, BorderLayout.CENTER);

        frame.setSize(1920, 1080);
        frame.setVisible(true);

    }

    public static void main(String[] args){
        javax.swing.SwingUtilities.invokeLater(MainPanel::createAndShowGUI);
    }
}

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Main extends JFrame implements ChangeListener {

    private final ImagePanel imagePanel;

    public Main() {
        super("Floyd-Steinberg");

        JPanel mainPanel = new JPanel();

        this.imagePanel = new ImagePanel("imgs/photo.png");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        mainPanel.add(imagePanel);

        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        slider.addChangeListener(this);
        mainPanel.add(slider);



    }

    public static void main(String[] args) {
        new Main();
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
}






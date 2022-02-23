import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;

public class MainPanel extends JPanel implements ChangeListener {

    private final ImagePanel imagePanel;

    public MainPanel(String imagePath) {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));


        imagePanel = new ImagePanel(imagePath);

        JSlider thresholdSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, (int)(imagePanel.getThreshold() * 100));
        thresholdSlider.addChangeListener(this);


        add(imagePanel);
        add(thresholdSlider);

        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));




    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();

        int threshold = source.getValue();
        imagePanel.updateThreshold(threshold / 100.);
        update(this.getGraphics());

    }

    private static void createAndShowGUI() {
        JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getDefaultDirectory());
        int res = chooser.showOpenDialog(null);
        String imagePath = "imgs/photo.png";
        if(res == JFileChooser.APPROVE_OPTION)
            imagePath = chooser.getSelectedFile().getAbsolutePath();


        JFrame frame = new JFrame("Floyd-Steinberg");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MainPanel mainPanel = new MainPanel(imagePath);

        frame.add(mainPanel, BorderLayout.CENTER);

        frame.setSize(mainPanel.imagePanel.width + 36, mainPanel.imagePanel.height + 90);
        frame.setVisible(true);

    }

    public static void main(String[] args){
        javax.swing.SwingUtilities.invokeLater(MainPanel::createAndShowGUI);
    }
}

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class MainPanel extends JPanel implements ChangeListener, ActionListener {

    private final ImagePanel imagePanel;

    public MainPanel(String imagePath) {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));


        imagePanel = new ImagePanel(imagePath);

        JSlider thresholdSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, (int)(imagePanel.getThreshold() * 100));
        thresholdSlider.addChangeListener(this);

        JLabel sliderLabel = new JLabel("Threshold", JLabel.CENTER);
        sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JRadioButton thresRadioButton = new JRadioButton("Threshold");
        JRadioButton fsRadioButton = new JRadioButton("Floyd-Steinberg", true);
        JRadioButton maeRadioButton = new JRadioButton("Minimised Average Error");

        thresRadioButton.setActionCommand("thres");
        fsRadioButton.setActionCommand("fs");
        maeRadioButton.setActionCommand("mae");

        thresRadioButton.addActionListener(this);
        fsRadioButton.addActionListener(this);
        maeRadioButton.addActionListener(this);


        ButtonGroup group = new ButtonGroup();
        group.add(thresRadioButton);
        group.add(fsRadioButton);
        group.add(maeRadioButton);

        add(imagePanel);

        add(thresRadioButton);
        add(fsRadioButton);
        add(maeRadioButton);

        add(thresholdSlider);
        //add(sliderLabel);
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));




    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();

        int threshold = source.getValue();
        imagePanel.updateThreshold(threshold / 100.);
        update(this.getGraphics());

    }

    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("fs"))
            imagePanel.updateAlgorithm(ImagePanel.FSDithering);
        else if(e.getActionCommand().equals("mae"))
            imagePanel.updateAlgorithm(ImagePanel.JJNDithering);
        else if(e.getActionCommand().equals("thres"))
            imagePanel.updateAlgorithm(ImagePanel.ThresholdDithering);

        update(this.getGraphics());
    }

    private static void createAndShowGUI() {
        JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getDefaultDirectory());
        int res = chooser.showOpenDialog(null);
        String imagePath = "imgs/photo.png";
        if(res == JFileChooser.APPROVE_OPTION)
            imagePath = chooser.getSelectedFile().getAbsolutePath();


        JFrame frame = new JFrame("1-bit Dithering");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MainPanel mainPanel = new MainPanel(imagePath);

        frame.add(mainPanel, BorderLayout.CENTER);




        frame.setSize(mainPanel.imagePanel.width + 36, mainPanel.imagePanel.height + 150);
        frame.setVisible(true);

    }

    public static void main(String[] args){
        javax.swing.SwingUtilities.invokeLater(MainPanel::createAndShowGUI);
    }
}

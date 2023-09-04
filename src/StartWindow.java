import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class StartWindow extends JFrame implements ActionListener {

    private int width = 600;
    private int height = 400;
    private JButton startGame;
    private JButton startMusic;
    private JButton stopMusic;
    private Sequencer sequencer;
    private boolean switchMusic=false;
    private String stufe;
    private int zeit;
    private int leben;
    private int ziel;
    private int maxLeben;
    private int gegnerSchwer;
    boolean isInit;

    public StartWindow(){
        super("Space Game");
        setSize(width, height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width-width)/2, (screenSize.height-height)/2);

        //Borderlayout auf das Fenster legen
        setLayout(new BorderLayout());
        //der Nordbereich soll flowen
        JPanel panelNorth = new JPanel();
        panelNorth.setLayout(new FlowLayout());
        panelNorth.setBackground(Color.BLACK);

        //Buttons bauen
        startGame = new JButton();
        startGame.setBorderPainted(false);
        startGame.setBackground(new Color(14, 162, 118));
        startGame.setOpaque(true);
        startGame.setText("Start GAME");
        panelNorth.add(startGame);

        startMusic = new JButton();
        startMusic.setBorderPainted(false);
        startMusic.setBackground(Color.GREEN);
        startGame.setOpaque(true);
        startMusic.setText("Start Music");
        panelNorth.add(startMusic);

        stopMusic = new JButton();
        stopMusic.setBorderPainted(false);
        stopMusic.setBackground(Color.RED);
        stopMusic.setOpaque(true);
        stopMusic.setText("Stop Music");
        stopMusic.setVisible(false);
        panelNorth.add(stopMusic);


        JTextArea gameManual = new JTextArea();
        gameManual.setForeground(Color.WHITE);
        gameManual.setBackground(Color.BLACK);
        gameManual.setFont(new Font("Times New Roman", Font.ITALIC, 20));
        gameManual.setMargin(new Insets(30, 30, 30, 30));
        gameManual.setText("Der Weltraum, unendliche Weiten\r\n"+
                            "zumindest war es mal so....\r\n"+
                            "Das Universum fällt auseinander\r\n"+
                            "rette die verbleibenden Galaxien, bevor dies geschieht\r\n" +
                            "Doch nimm dich vor den schwarzen Löchern in Acht,\r\n"+
                            "dein Raumschiff kann ihnen nur so oft wiederstehen,\r\n" +
                            "wenn du Glück hast findest du Booster, \r\n" +
                            "sie können deine Geschwindigkeit erhöhen, \r\n"+
                            "oder das Schwarze Loch für kurze Zeit einfrieren \r\n" +
                            "Viel Glück auf deiner Mission. ");

        add(gameManual, BorderLayout.CENTER);

        add(panelNorth, BorderLayout.NORTH);
        setVisible(true);

        startGame.addActionListener(this);
        startMusic.addActionListener(this);
        stopMusic.addActionListener(this);
        try {
            startTheMusicNow();
        } catch (MidiUnavailableException ex) {
            throw new RuntimeException(ex);
        } catch (InvalidMidiDataException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==startGame){
            String[] options = {"Einfach:   Zeit: 5 min, Leben: 5, Ziel: 25",
                                "Mittel:       Zeit: 3 min, Leben: 3, Ziel: 20",
                                "Schwer:   Zeit: 3 min, Leben: 2, Ziel: 20",
                                "Benutzerdefiniert"};
            String[] option = {"Einfach", "Mittel", "Schwer", "Benutzerdefiniert"};
            Object ob = JOptionPane.showInputDialog(null, options,
                    "Schwierigkeit auswählen", JOptionPane.QUESTION_MESSAGE, null,
                    option, option[0]);
            stufe = (String) ob;
            if (ob == null){
                stufe = "Einfach";
            }

            if(stufe.equals("Benutzerdefiniert")){
                String[] gOptions = {"Einfach: Gegner sind langsamer und seltener",
                        "Mittel: Gegner haben normale Geschwindigkeit und Häufigkeit",
                        "Schwer:   Gegner sind schneller und häufiger"};
                String[] gOption = {"Einfach", "Mittel", "Schwer"};
                Object op = JOptionPane.showInputDialog(null, gOptions,
                        "Schwierigkeit  der Gegner auswählen", JOptionPane.QUESTION_MESSAGE, null,
                        gOption, gOption[0]);
                if (op == null){
                    gegnerSchwer = 1;
                } else if (op.equals("Schwer")){
                    gegnerSchwer = 3;
                }else if (op.equals("Mittel")){
                    gegnerSchwer = 2;
                }else {
                    gegnerSchwer = 1;
                }
                System.out.println("gegner: " + gegnerSchwer);
                String zeitStr = JOptionPane.showInputDialog(null,"Gib deine max Spielzeit ein (in Minuten/max 9)",3 );
                try {
                    zeit = Integer.parseInt(zeitStr);
                } catch (Exception exception){
                    System.out.println("Input inkorrekt");
                    zeit = 3;
                }
                if (zeit > 9){
                    zeit = 9;
                }
                if (zeit < 1){
                    zeit = 1;
                }
                String lebenStr = JOptionPane.showInputDialog(null,"Gib deine Startleben ein (max 99)",5 );
                try {
                    leben = Integer.parseInt(lebenStr);
                } catch (Exception exception){
                    System.out.println("Input inkorrekt");
                    leben = 5;
                }
                if (leben>99){
                    leben = 99;
                }
                if (leben < 1){
                    leben = 5;
                }
                String maxLebenStr = JOptionPane.showInputDialog(null,"Gib deine max Leben ein (max 99)",9 );
                try {
                    maxLeben = Integer.parseInt(maxLebenStr);
                } catch (Exception exception){
                    System.out.println("Input inkorrekt");
                    maxLeben = 9;
                }
                if (maxLeben<leben){  //Negatives Maxleben wird hier bereits abgefangen, da leben vorher in den Positiven bereich gesetzt wurde
                    maxLeben = leben;
                }
                if (maxLeben > 99){
                    maxLeben = 99;
                }
                    String zielStr = JOptionPane.showInputDialog(null,"Gib ein wieviele Sterne du sammeln willst(max 99, min 5)",20 );
                try {
                    ziel = Integer.parseInt(zielStr);
                    System.out.println("gg" + ziel);
                } catch (Exception exception){
                    System.out.println("Input inkorrekt");
                    ziel = 20;
                }
                if (ziel > 99){
                    ziel = 99;
                }
                if (ziel<5){
                    ziel = 5;
                }
                isInit = true;
                System.out.println("Ziel " + ziel);
            }


            else if(stufe.equals("Mittel")){
                zeit = 3;
                leben = 3;
                ziel = 20;
                maxLeben = 6;
                gegnerSchwer = 2;
                isInit = true;

            } else if (stufe.equals("Schwer")) {
                zeit = 3;
                leben = 2;
                ziel = 20;
                maxLeben = 3;
                gegnerSchwer = 3;
                isInit = true;
            }else{
                zeit = 5;
                leben = 5;
                ziel = 25;
                maxLeben = 9;
                gegnerSchwer = 1;
                isInit = true;
            }
            if (isInit){
                new SpaceWindow(zeit, leben, ziel, maxLeben, gegnerSchwer);
            }



            System.out.println("StartGameButton gedrückt");
        }else if(e.getSource()==startMusic){
            if (!switchMusic){
                startMusic.setBackground(Color.YELLOW);
                startMusic.setText("Pause Music");
                switchMusic = true;
                stopMusic.setVisible(true);
                sequencer.start();
                stopMusic.setText("Stop Music");
            }else {
                startMusic.setBackground(Color.GREEN);
                startMusic.setText("Continue Music");
                switchMusic = false;
                sequencer.stop();
                stopMusic.setText("Reset Music");
            }
        } else if (e.getSource()==stopMusic) {
            sequencer.stop();
            switchMusic = false;
            startMusic.setBackground(Color.GREEN);
            startMusic.setText("Start Music");
            stopMusic.setVisible(false);
            try {
                startTheMusicNow();
            } catch (MidiUnavailableException ex) {
                throw new RuntimeException(ex);
            } catch (InvalidMidiDataException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void startTheMusicNow() throws MidiUnavailableException, InvalidMidiDataException, IOException {

        var synth = MidiSystem.getSynthesizer();
        synth.loadAllInstruments(synth.getDefaultSoundbank());

        sequencer = MidiSystem.getSequencer();

        var sequence = MidiSystem.getSequence(new File("infinityspace.mid"));

        sequencer.open();
        sequencer.setSequence(sequence);
        sequencer.setTempoFactor(1f);
    }
}

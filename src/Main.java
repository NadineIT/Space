import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        //Beginn der App
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                //Starte das ganze Spiel
                new StartWindow();

            }
        });

    }
}
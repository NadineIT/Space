import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.imageio.ImageIO;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class SpaceWindow extends JFrame{

    private int width = 1500, height = 900;

    public SpaceWindow(int z, int l, int zi, int mL, int gS){

        super("Rette das Universum");

        setSize(width, height);
        setResizable(false);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width-width)/2,
                (screenSize.height-height)/2);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        setVisible(true);

        //Ränder bekommen in denen nichts angezeigt werden kann
        int titleHeight = getInsets().top;;
        int borderRight = getInsets().right;
        int borderLeft = getInsets().left;
        int borderBottom = getInsets().bottom;

        //add(panelNorth, BorderLayout.NORTH);
        add(new SpaceGame(width-borderLeft-borderRight,
                height-titleHeight-borderBottom,
                z, l, zi, mL, gS));
    }


    public class SpaceGame extends JPanel implements ActionListener {

        private Image spaceShip, hintergrund;
        private Image speedUp, freezeTime, shieldPowerUp, lifeUp;
        private Image star, blackHole, shield;
        private Image zNull, zEins, zZwei, zDrei, zVier, zFuenf, zSechs, zSieben, zAcht, zNeun, slash, heart;
        private ArrayList<Image> numbers;
        private int spaceWidth, spaceHeight;
        private int shipX, shipY, shipXdouble, shipYdouble, shipXdouble2, shipYdouble2,
                shipXdouble3, shipYdouble3;
        private int speedUpX, speedUpY, freezeTimeX, freezeTimeY, shieldX, shieldY, lifeX, lifeY;
        private int starX, starY, blackHoleX, blackHoleY, rotation;
        private int shipWidth, shipHeight;
        private int bonusWidth, bonusHeight;
        private int starWidth, starHeight, blackHoleHeight, blackHoleWidth;
        private int speed = 10;
        private int bewegung, richtung;
        private int bonus;
        private int speedBoost = 5;
        private int schwierigkeit;
        private boolean speedUpAvailable = false, freezeTimeAvailable = false, lifeUpAvailable = false, shieldAvailabe = false;
        private boolean isInit;
        private boolean isLeftPressed, isRightPressed, isUpPressed, isDownPressed;
        private boolean starInit = false, blackHoleAvailable = false;
        private boolean timeFrozen = false, shieldActive = false;
        private int delay = 40;
        private int lifetime = 5000;
        private int bonusDauer = 3000;
        private int multiple = 0;
        private int lifepoints, maxLife;
        private int min, sec;
        private int points = 0, ziel;
        private int zeit;
        private Timer starTimer = new Timer(120, this);
        private Timer timer = new Timer(delay, this);
        private Timer bonusTimer = new Timer(1000, this);
        private Timer speedLife = new Timer(lifetime, this);
        private Timer freezeLife = new Timer(lifetime, this);
        private Timer shieldLife = new Timer(lifetime, this);
        private Timer lifeLife = new Timer(lifetime, this);
        private Timer speedTimer = new Timer(bonusDauer, this);
        private Timer freezeTimer = new Timer(bonusDauer, this);
        private Timer shieldTimer = new Timer(bonusDauer, this);
        private Timer blackHoleLife = new Timer(60000, this);
        private  java.util.Timer gameOver = new java.util .Timer();

        public void gameLost(String grund){
            System.out.println("Test 1");
            closeGame();
            SpaceWindow.this.dispose();
            System.out.println("test 2");
            System.out.println("gameOver");
            JOptionPane.showConfirmDialog(null, "Verloren: " + grund, "Game Over", JOptionPane.DEFAULT_OPTION);
        }

        private void closeGame() {
            isInit = false;
            starTimer.stop();
            timer.stop();
            bonusTimer.stop();
            speedLife.stop();
            freezeLife.stop();
            shieldLife.stop();
            blackHoleLife.stop();
            speedTimer.stop();
            freezeTimer.stop();
            shieldTimer.stop();
            gameOver.cancel();
            lifeLife.stop();
            lifepoints = 10;
            points = 0;
            bonusDauer = 3000;
        }

        public SpaceGame(int w, int h, int ze, int l, int zi, int mL, int gS) {

            isInit = true;
            zeit = ze * 60;
            lifepoints = l;
            maxLife = mL;
            numbers = new ArrayList<>();
            System.out.println(points);
            System.out.println(lifepoints);
            Random random = new Random();
            spaceWidth = w;
            spaceHeight = h;
            ziel = zi;
            shipX = spaceWidth/2-shipWidth;
            shipY = spaceHeight/4*3-shipHeight;
            shipXdouble = shipXdouble2 = shipXdouble3 = shipX;
            shipYdouble =  shipYdouble2 = shipYdouble3 = shipY;
            shipWidth = shipHeight = 100;
            if (gS == 3){
                System.out.println("Schwer 1");
                bonusHeight = 40;
                starHeight = 40;
                blackHoleWidth = 90;
                schwierigkeit = 1;
                bonusDauer = bonusDauer/2;
            } else if (gS == 2) {
                System.out.println("Mittel 2");
                bonusHeight = 50;
                starHeight = 60;
                blackHoleWidth = 70;
                schwierigkeit = 2;
            }else {
                System.out.println("leicht 3");
                bonusHeight = 60;
                starHeight = 80;
                blackHoleWidth = 60;
                schwierigkeit = 3;
                bonusDauer = bonusDauer * 2;
            }
            bonusWidth = bonusHeight;
            starWidth =  starHeight;
            blackHoleHeight = blackHoleWidth;
            class CountDown {
                public CountDown(int t) {
                    final int[] timeLeft = {t};
                    java.util.TimerTask task = new java.util.TimerTask() {
                        @Override
                        public void run() {
                            if (timeLeft[0] > 0) {
                                min = timeLeft[0] / 60;
                                sec = timeLeft[0] % 60;
                                System.out.println("Verbleibende Zeit: " + min + ":" + sec);
                            } else {
                                gameOver.cancel();
                                gameLost("Keine Zeit mehr");
                            }
                            timeLeft[0]--;
                        }
                    };
                    gameOver.schedule(task, 0, 1000);
                }
            }
            new CountDown(zeit);
            try {
                hintergrund = ImageIO.read(new File("hintergrund.jpg"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                spaceShip = ImageIO.read(new File("spaceShip.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                zNull = ImageIO.read(new File("0.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                zEins = ImageIO.read(new File("1.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                zZwei = ImageIO.read(new File("2.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                zDrei = ImageIO.read(new File("3.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                zVier = ImageIO.read(new File("4.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                zFuenf = ImageIO.read(new File("5.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                zSechs = ImageIO.read(new File("6.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                zSieben = ImageIO.read(new File("7.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                zAcht = ImageIO.read(new File("8.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                zNeun = ImageIO.read(new File("9.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                slash = ImageIO.read(new File("slash.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                heart = ImageIO.read(new File("heart.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                star = ImageIO.read(new File("star.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                shield = ImageIO.read(new File("shield.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                shieldPowerUp = ImageIO.read(new File("shieldPowerUp.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                lifeUp = ImageIO.read(new File("lifeUp.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                blackHole = ImageIO.read(new File("black_hole.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                speedUp = ImageIO.read(new File("speedUp.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                freezeTime = ImageIO.read(new File("freezeTime.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //erstellen eines Arrays aller Nummern, damit das zeigen später vereinfacht wird
            numbers.add(zNull);numbers.add(zEins);numbers.add(zZwei);numbers.add(zDrei);numbers.add(zVier);numbers.add(zFuenf);
            numbers.add(zSechs);numbers.add(zSieben);numbers.add(zAcht);numbers.add(zNeun);
            setBackground(Color.BLACK);
            setFocusable(true);
            requestFocusInWindow();
            fly();
            speedLife.setInitialDelay(lifetime);
            speedLife.addActionListener((new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    speedUpAvailable = false;
                    speedLife.stop();
                }
            }));
            freezeLife.setInitialDelay(lifetime);
            freezeLife.addActionListener((new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    freezeTimeAvailable = false;
                    freezeLife.stop();
                }
            }));
            shieldLife.setInitialDelay(lifetime);
            shieldLife.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    shieldAvailabe = false;
                    speedLife.stop();
                }
            });
            blackHoleLife.setInitialDelay(30000);
            blackHoleLife.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    blackHoleAvailable = false;
                    blackHoleLife.stop();
                }
            });

            speedTimer.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    speed = speed - speedBoost;
                    System.out.println(speed + " boost over");
                    multiple = multiple - 1;
                    if (multiple <= 1){
                        speedTimer.setRepeats(false);
                    }
                }
            });

            freezeTimer.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    timeFrozen = false;
                }
            });

            shieldTimer.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    shieldActive = false;
                }
            });

            bonusTimer.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Random random = new Random();
                    bonus = random.nextInt(1,21);
                    System.out.println(bonus);
                    if (bonus == 16 && !freezeTimeAvailable){
                        freezeTimeAvailable = true;
                        freezeTimeX = random.nextInt(0, spaceWidth-bonusWidth);
                        freezeTimeY = random.nextInt(0, spaceHeight-bonusHeight);
                        freezeLife.restart();
                    } else if ((bonus == 14 && !lifeUpAvailable) &&
                            (lifepoints<maxLife)) {
                        System.out.println(bonus + "Life");
                        lifeUpAvailable = true;
                        lifeX = random.nextInt(0, spaceWidth-bonusWidth);
                        lifeY = random.nextInt(0, spaceHeight-bonusHeight);
                    } else if (bonus == 6 && !speedUpAvailable){
                        speedUpAvailable = true;
                        speedUpX = random.nextInt(shipWidth/2, spaceWidth-shipWidth/2-bonusWidth);
                        speedUpY = random.nextInt(shipHeight/2, spaceHeight-shipHeight/2-bonusHeight);
                        speedLife.restart();
                    } else if (bonus == 2 && !shieldAvailabe) {
                        shieldAvailabe = true;
                        shieldX = random.nextInt(shipWidth/2, spaceWidth-shipWidth/2-bonusWidth);
                        shieldY = random.nextInt(shipHeight/2, spaceHeight-shipHeight/2-bonusHeight);
                    } else if (((bonus == 1)||(bonus==10)||(bonus == 20)//wahrscheinlichkeit bei einfach
                            ||((bonus==5||bonus==15)&&schwierigkeit==2) //zusätzliche wahrscheinlichkeit bei mittel
                            ||((bonus==3||bonus==7||bonus==13||bonus==17)&&(schwierigkeit==1)))//andere zusätzliche wahrscheinlichkeit bei schwer
                            &&!blackHoleAvailable) {
                        blackHoleAvailable = true;
                        blackHoleX = random.nextInt(shipWidth/2, spaceWidth-shipWidth/2-blackHoleWidth);
                        blackHoleY = random.nextInt(shipHeight/2, spaceHeight-shipHeight/2-blackHoleHeight);
                        blackHoleLife.restart();
                        System.out.println("Black Hole");
                    }
                }
            });
            if (isInit){
                bonusTimer.setRepeats(true);
                bonusTimer.start();
            }


            starTimer.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!starInit){
                        starInit = true;
                        starX = random.nextInt(shipWidth/2, spaceWidth-shipWidth/2-starWidth);
                        starY = random.nextInt(shipHeight/2, spaceHeight-shipHeight/2-starHeight);
                        System.out.println(starX + " || " + starY);
                    }else {
                        bewegung = random.nextInt(0,6);
                        if (starX + starWidth + bewegung > spaceWidth){
                            starX = starX - bewegung;
                        }else if(starX - bewegung < 0){
                            starX = starX + bewegung;
                        }else{
                            richtung = random.nextInt(1, 11);
                            if (richtung%2==0){
                                starX = starX + bewegung;
                            }else {
                                starX = starX - bewegung;
                            }
                        }
                        if (starY + starHeight + bewegung > spaceWidth){
                            starY = starY - bewegung;
                        }else if(starY - bewegung < 0){
                            starY = starY + bewegung;
                        }else{
                            richtung = random.nextInt(1, 11);
                            if (richtung%2==0){
                                starY = starY + bewegung;
                            }else {
                                starY = starY - bewegung;
                            }
                        }
                    }
                }
            });
            if (isInit){
                timer.start();
                starTimer.start();
            }
        }
        private void fly() {
            addKeyListener(new KeyAdapter(){

                @Override
                public void keyPressed(KeyEvent e) {

                    int action = e.getKeyCode();

                    switch (action){
                        case KeyEvent.VK_RIGHT:
                            isRightPressed = true;
                            break;
                        case KeyEvent.VK_LEFT:
                            isLeftPressed = true;
                            break;
                        case KeyEvent.VK_UP:
                            isUpPressed = true;
                            break;
                        case KeyEvent.VK_DOWN:
                            isDownPressed = true;
                            break;
                        case KeyEvent.VK_ESCAPE:
                            closeGame();
                            SpaceWindow.this.dispose();
                            JOptionPane.showConfirmDialog(null, "Du hast das Spiel beendet", "Spiel beendet",
                                    JOptionPane.DEFAULT_OPTION);
                            break;
                    }
                }
                @Override
                public void keyReleased(KeyEvent e) {
                    super.keyReleased(e);

                    int action = e.getKeyCode();

                    switch (action){
                        case KeyEvent.VK_RIGHT:
                            isRightPressed = false;
                            break;
                        case KeyEvent.VK_LEFT:
                            isLeftPressed = false;
                            break;
                        case KeyEvent.VK_UP:
                            isUpPressed = false;
                            break;
                        case KeyEvent.VK_DOWN:
                            isDownPressed = false;
                            break;
                    }
                }
            });
        }
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);

            g.drawImage(hintergrund, 0, 0,spaceWidth,spaceHeight,this);

            if (starInit){
                g.drawImage(star,starX, starY, starWidth, starHeight, this);
            }
            if (speedUpAvailable){
                g.drawImage(speedUp,speedUpX, speedUpY, bonusWidth, bonusHeight, this);
            }
            if (freezeTimeAvailable){
                g.drawImage(freezeTime, freezeTimeX, freezeTimeY, bonusWidth, bonusHeight, this);
            }
            if (blackHoleAvailable){
                if(!timeFrozen){
                    Graphics2D g2d = (Graphics2D)g;
                    rotation += 5;
                    g2d.rotate(Math.toRadians(rotation),blackHoleX+blackHoleWidth/2, blackHoleY+blackHoleHeight/2);
                    g2d.drawImage(blackHole, blackHoleX, blackHoleY, blackHoleWidth, blackHoleHeight, this);
                    g2d.rotate(Math.toRadians(-rotation),blackHoleX+blackHoleWidth/2, blackHoleY+blackHoleHeight/2);
                }else {
                    g.drawImage(blackHole, blackHoleX, blackHoleY, blackHoleWidth, blackHoleHeight, this);
                }

            }
            if (shieldAvailabe){
                g.drawImage(shieldPowerUp, shieldX, shieldY, bonusWidth, bonusHeight, this);
            }
            if (lifeUpAvailable){
                g.drawImage(lifeUp, lifeX, lifeY, bonusWidth, bonusHeight, this);
            }
            g.drawImage(spaceShip,shipX,shipY, shipWidth, shipHeight, this);
            g.drawImage(spaceShip, shipXdouble, shipYdouble, shipWidth, shipHeight, this);
            g.drawImage(spaceShip,shipXdouble2, shipYdouble2, shipWidth, shipHeight, this);
            g.drawImage(spaceShip,shipXdouble3, shipYdouble3, shipWidth, shipHeight, this);
            if(shieldActive){
                g.drawImage(shield,shipX-10,shipY-10, shipWidth+20, shipHeight+20, this);
                if (shipXdouble != shipX || shipYdouble != shipY){
                    g.drawImage(shield,shipXdouble-10,shipYdouble-10, shipWidth+20, shipHeight+20, this);
                }
                if (shipXdouble2 != shipX || shipYdouble2 != shipY){
                    g.drawImage(shield,shipXdouble2-10,shipYdouble2-10, shipWidth+20, shipHeight+20, this);
                }
                if (shipXdouble3 != shipX || shipYdouble3 != shipY){
                    g.drawImage(shield,shipXdouble3-10,shipYdouble3-10, shipWidth+20, shipHeight+20, this);
                }
            }
            g.drawImage(star, 20, 20, 20, 20, this);
            g.drawImage(numbers.get((points/10)), 45,20,20,20, this);
            g.drawImage(numbers.get(points%10), 70, 20,20,20, this);
            g.drawImage(slash, 95,20,10,20, this);
            g.drawImage(numbers.get(ziel/10), 110,20,20,20, this);
            g.drawImage(numbers.get(ziel%10), 135, 20,20,20, this);
            g.drawImage(heart, spaceWidth-40, 20, 20,20, this);
            g.drawImage(numbers.get(maxLife%10), spaceWidth-65, 20, 20, 20, this);
            g.drawImage(numbers.get(maxLife/10), spaceWidth-90, 20, 20, 20, this);
            g.drawImage(slash, spaceWidth-115,20,10,20, this);
            g.drawImage(numbers.get(lifepoints%10), spaceWidth-140, 20, 20, 20, this);
            g.drawImage(numbers.get(lifepoints/10), spaceWidth-165, 20, 20, 20, this);
            g.drawImage(numbers.get(min%10), spaceWidth/2-25, 20,20,20, this);
            g.drawImage(slash, spaceWidth/2-5, 20,10,20, this);
            g.drawImage(numbers.get(sec/10), spaceWidth/2+5,20,20,20, this);
            g.drawImage(numbers.get(sec%10), spaceWidth/2+25, 20,20,20, this);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            Random random = new Random();

            if (isLeftPressed){
                if(shipX-speed >= 0){
                    shipX -= speed;
                    shipXdouble -= speed;
                    shipXdouble2 -= speed;
                    shipXdouble3 -= speed;
                }else{
                    if (shipX+shipWidth/2-speed >= 0){
                        shipX -= speed;
                        shipXdouble = shipX + spaceWidth;
                        shipXdouble2 = shipX;
                        shipXdouble3 = shipXdouble;

                    }else {
                        shipXdouble = shipX-speed;
                        shipX = shipXdouble+spaceWidth;
                        shipXdouble2 = shipX;
                        shipXdouble3 = shipXdouble;
                    }
                }
            }
            if (isRightPressed){
                if((shipX+speed <= spaceWidth-shipWidth)){
                    shipX += speed;
                    shipXdouble += speed;
                    shipXdouble2 += speed;
                    shipXdouble3 += speed;
                }else{
                    if (shipX+shipWidth+speed<=spaceWidth+(shipWidth/2)){
                        shipX += speed;
                        shipXdouble = shipX-spaceWidth;
                        shipXdouble2 = shipX;
                        shipXdouble3 = shipXdouble;
                    }else {
                        shipXdouble = shipX+speed;
                        shipX = -shipWidth+(spaceWidth-shipX+speed);
                        shipXdouble2 = shipX;
                        shipXdouble3 = shipXdouble;
                    }
                }
            }
            if (isUpPressed){
                if(shipY-speed >= 0){
                    shipY -= speed;
                    shipYdouble -= speed;
                    shipYdouble2 = shipYdouble;
                    shipYdouble3 = shipY;
                }else{
                    if (shipY+shipHeight/2-speed >= 0){
                        shipY -= speed;
                        shipYdouble = shipY + spaceHeight;
                        shipYdouble2 = shipYdouble;
                        shipYdouble3 = shipY;
                    }else {
                        shipYdouble = shipY-speed;
                        shipY = shipYdouble+spaceHeight;
                        shipYdouble2 = shipYdouble;
                        shipYdouble3 = shipY;
                    }
                }
            }
            if (isDownPressed){
                if((shipY+speed <= spaceHeight-shipHeight)){
                    shipY += speed;
                    shipYdouble += speed;
                    shipYdouble2 = shipYdouble;
                    shipYdouble3 = shipY;
                }else{
                    if (shipY+shipHeight+speed<=spaceHeight+(shipHeight/2)){
                        shipY += speed;
                        shipYdouble = shipY-spaceHeight;
                        shipYdouble2 = shipYdouble;
                        shipYdouble3 = shipY;
                    }else {
                        shipYdouble = shipY+speed;
                        shipY = -shipHeight+(spaceHeight-shipY+speed);
                        shipYdouble2 = shipYdouble;
                        shipYdouble3 = shipY;
                    }
                }
            }
            //Black Hole Bewegung
            if (blackHoleAvailable){
                if (!timeFrozen){
                    int pursuit = random.nextInt(4,7);
                    if (schwierigkeit == 3){
                        pursuit = pursuit/2;
                    }else if (schwierigkeit == 1){
                        pursuit = pursuit*2;
                    }
                    if (shieldActive){          //Black Hole flieht
                        pursuit = -pursuit;
                    }
                    if (shipX < blackHoleX){
                        if (blackHoleX-pursuit < shipX){
                            blackHoleX = shipX;
                        }else {
                            blackHoleX = blackHoleX - pursuit;
                        }
                        if(blackHoleX+blackHoleWidth > spaceWidth){
                            blackHoleX=spaceWidth-blackHoleWidth;
                        }
                    }else {
                        if (blackHoleX+pursuit>shipX){
                            blackHoleX = shipX;
                        }else {
                            blackHoleX = blackHoleX + pursuit;
                        }
                        if(blackHoleX < 0){
                            blackHoleX=0;
                        }
                    }
                    if (shipY < blackHoleY) {
                        if (blackHoleY-pursuit<shipY) {
                            blackHoleY = shipY;
                        }else {
                            blackHoleY = blackHoleY - pursuit;
                        }
                        if (blackHoleY + blackHoleHeight > spaceHeight){
                            blackHoleY = spaceHeight - blackHoleHeight;
                        }
                    } else {
                        if (blackHoleY + pursuit>shipY){
                            blackHoleY = shipY;
                        }else {
                            blackHoleY = blackHoleY + pursuit;
                        }
                        if (blackHoleY < 0) {
                            blackHoleY = 0;
                        }
                    }
                }
                if (((shipX + shipWidth > blackHoleX && shipX < blackHoleX + blackHoleWidth)&&   //Kollision mit BlackHole
                        (shipY + shipHeight > blackHoleY && shipY < blackHoleY + blackHoleHeight)) ||
                        ((shipXdouble + shipWidth > blackHoleX && shipXdouble < blackHoleX + blackHoleWidth)&&
                                (shipYdouble + shipHeight > blackHoleY && shipYdouble < blackHoleY + blackHoleHeight)))
                {
                    blackHoleAvailable = false;
                    if (!shieldActive){
                        lifepoints = lifepoints - 1;
                        System.out.println(lifepoints);
                        if (lifepoints == 0){
                            gameLost("Keine Leben mehr übrig");
                        }
                    }

                }
            }
            if(freezeTimeAvailable){                //Kollision mit freezeTime
                if ((shipX + shipWidth > freezeTimeX && shipX < freezeTimeX + bonusWidth)&&
                        (shipY + shipHeight > freezeTimeY && shipY < freezeTimeY + bonusHeight))
                {
                    freezeTimeAvailable = false;
                    frozeTime();
                    freezeLife.stop();
                }
            }
            if (speedUpAvailable){                  //Kollision mit speedUp
                if ((shipX + shipWidth > speedUpX && shipX < speedUpX + bonusWidth)&&
                        (shipY + shipHeight > speedUpY && shipY < speedUpY + bonusHeight))
                {
                    System.out.println("Boost starten");
                    speedBoosten();
                    speedUpAvailable = false;
                    speedLife.stop();
                }
            }
            if (shieldAvailabe){                  //Kollision mit shield
                if ((shipX + shipWidth > shieldX && shipX < shieldX + bonusWidth)&&
                        (shipY + shipHeight > shieldY && shipY < shieldY + bonusHeight))
                {
                    System.out.println("Boost starten");
                    starteShield();
                    shieldAvailabe = false;
                    shieldLife.stop();
                }
            }
            if (lifeUpAvailable) {              //Kollision mit lifeUp
                if ((shipX + shipWidth > lifeX && shipX < lifeX + bonusWidth)&&
                        (shipY + shipHeight > lifeY && shipY < lifeY + bonusHeight))
                {
                    lifeUpAvailable = false;
                    System.out.println(lifepoints);
                    System.out.println("Leben erhalten");
                    lifepoints = lifepoints + 1;
                    System.out.println(lifepoints);
                    lifeLife.stop();
                }
            }

            if (starInit){                          //Kollision mit Star
                if (((shipX + shipWidth > starX && shipX < starX + starWidth)&&
                        (shipY + shipHeight > starY && shipY < starY + starHeight)) ||
                        ((shipXdouble + shipWidth > starX && shipXdouble < starX + starWidth)&&
                                (shipYdouble + shipHeight > starY && shipYdouble < starY + starHeight)))
                {
                    starInit = false;
                    points = points + 1;
                    if (points == ziel){
                        SpaceWindow.this.dispose();
                        closeGame();
                        JOptionPane.showConfirmDialog(null, "Gewonnen", "Gewonnen",
                                JOptionPane.DEFAULT_OPTION);

                    }
                }
            }
            repaint();
        }
        private void starteShield() {
            shieldTimer.setInitialDelay(bonusDauer);
            shieldTimer.setRepeats(false);
            shieldActive = true;
            shieldTimer.restart();
        }

        private void frozeTime() {
            freezeTimer.setInitialDelay(bonusDauer);
            freezeTimer.setRepeats(false);
            timeFrozen = true;
            freezeTimer.restart();
        }

        private void speedBoosten() {
            speed = speed + speedBoost;
            System.out.println(speed + "Boost start");
            multiple = multiple + 1;
            System.out.println("nicht timer" + multiple);
            System.out.println(speed + " Boosted");
            if (multiple > 1){
                speedTimer.setRepeats(true);
                System.out.println("repeats");
            }else {
                speedTimer.setRepeats(false);
                System.out.println("no repeats");
            }
            speedTimer.start();

            System.out.println(speed);
        }

    }
}

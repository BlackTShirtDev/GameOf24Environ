/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blacktshirtdev;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
//import javax.swing.Timer;


/**
 *
 * @author logan
 */
public class gameState {
    
    public static int A,B,C,D = 0;
    public static int ASafe,BSafe,CSafe,DSafe = 0;
    public static int ansCount = 0;
    public static int difficulty = 0;
    public static int points = 0;
    public static int highScore = 0;
    public static int resetCounter = 0;
    public static int chaosCounter = 0;
    public static String name = "";
    public static String grade = "";
    public static String teacher = "";
    
    public static HashMap<String, Integer> highScoresMap= new HashMap<String, Integer>();
    
    
    public static String[] hints = new String[20];
    public static String[] displyHints = new String[3];
    public static List expressions = new ArrayList();
    
    public static boolean addButtTog, SubButtTog, muliButtTog, divButtTog, numButt1,
            numButt2, numButt3, numButt4 = false;
    
    public static boolean ResetFlag = false;
    public static boolean TwoPlayerFlag = false;
//    public static boolean coopFlag = false;
    public static boolean compFlag = false;
    public static boolean chaosFlag = false;
    
    public static Stack<Integer> buttPress = new Stack<Integer>(); //LOL
    public static Stack<JToggleButton> buttPressOrder = new Stack<JToggleButton>();
    
//    public static ButtonGroup num1ButtGroup = new ButtonGroup();
//    public static ButtonGroup num2ButtGroup = new ButtonGroup();
    public static ButtonGroup operButtGroup = new ButtonGroup();
    public static ButtonGroup hintButtGroup = new ButtonGroup();
    public static ButtonGroup twoPlayerButtGroup = new ButtonGroup();
         
    public static Scanner scanner = new Scanner(System.in);
    
    //Screens      
    public static EntryGUI2 gui = new EntryGUI2();
    public static hintGUI hintGUI = new hintGUI();
    public static helpGUI helpGUI = new helpGUI();
    
    //Two Player Variables
    public static ServerSocket ss=null;
    public static Socket s=null;
    public static DataInputStream din = null;
    public static DataOutputStream dout = null;
    public static BufferedReader br = null;
    public static boolean twoPlayerFlag = false;
    
    //Files
    public static String userDirectoryLog = System.getProperty("user.dir") + "//UserLog.txt";
    public static File fileLog = new File(userDirectoryLog);
    
    //Timer
    public static int delay = 1000;
    public static Timer chaosTimer = null;
    
    public static Timer compTimer = null;
    

    public gameState(EntryGUI2 window, boolean isTwoPlayer, boolean isChaosMode) {
        
        chaosFlag = isChaosMode;
        
        String userDirectory = System.getProperty("user.dir") + "//UserSettings.txt";
        File fileUser = new File(userDirectory);
        
        try {
            Scanner scUser = new Scanner(fileUser);
            if(scUser.hasNextLine()){
                name = scUser.next();
                scUser.nextLine();
                grade = scUser.nextLine();
                teacher = scUser.nextLine();
            }   
        } catch (FileNotFoundException ex) {
            Logger.getLogger(gameState.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        String userDirectoryHighScore = System.getProperty("user.dir") + "//RecordedHighScores.txt";
        File fileHighScores = new File(userDirectoryHighScore);
        
        if(!fileHighScores.exists()){
            
            if(!fileHighScores.canWrite())
                fileHighScores.setWritable(true);
            
            try {
                FileWriter fout = new FileWriter(fileHighScores, true);
                
                fout.write(" ");
                fout.flush();
                fout.close();
                
            } catch (IOException ex) {
                Logger.getLogger(gameState.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            
        }
        
        if(fileHighScores.exists() && fileHighScores.canRead()){
        
            try {
                Scanner sc = new Scanner(fileHighScores);
                sc.useDelimiter("\n");  
                while(sc.hasNextLine()){
                    String str = sc.nextLine();
                    Scanner lineScanner = new Scanner(str);
                    lineScanner.useDelimiter(" ");
                    while(lineScanner.hasNext()){
                        highScoresMap.put(lineScanner.next(), Integer.valueOf(lineScanner.next()));
                    }
                        
                }
                
            } catch (FileNotFoundException ex) {
                Logger.getLogger(gameState.class.getName()).log(Level.SEVERE, null, ex);
            }
           
            if (!highScoresMap.isEmpty() && highScoresMap != null){
                highScore = highScoresMap.get(name);
                gui.highScoreAmount.setText(name + " " + String.valueOf(highScore));
            }
                
        }
        
        if(!fileLog.exists() && fileLog.canWrite()){
            
            try {
                
                FileWriter logWriter = new FileWriter(fileLog, true);
                logWriter.write(name + " has started a new session at " + Calendar.getInstance().getTime().toString());
            } catch (IOException ex) {
                Logger.getLogger(gameState.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        } else {
            fileLog.setWritable(true);
            
            try {
                
                FileWriter logWriter = new FileWriter(fileLog, true);
                logWriter.write(name + " has started a new session at " + Calendar.getInstance().getTime().toString());
                logWriter.write("\n");
                logWriter.flush();
                logWriter.close();
                
            } catch (IOException ex) {
                Logger.getLogger(gameState.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        gui = window;
        
        if (isTwoPlayer == false){
            
            gui.nameSettingLabel.setText(name);
            gui.gradeSettingLabel.setText(grade);
            gui.teacherSettingLabel .setText(teacher);
            
            CardLayout card = (CardLayout) gui.mainPanel.getLayout();
            card.show(gui.mainPanel, "panelSinglePlayerOptions");
    
        } else if (isTwoPlayer == true){
            
            CardLayout card = (CardLayout) gui.mainPanel.getLayout();
            card.show(gui.mainPanel, "panelTwoPlayerOption");
            
            compFlag = true;
            
        }
        
        if (isChaosMode == false){
        
            gui.nameSettingLabel.setText(name);
            gui.gradeSettingLabel.setText(grade);
            gui.teacherNameTextField.setText(teacher);
            
//            CardLayout card = (CardLayout) gui.mainPanel.getLayout();
//            card.show(gui.mainPanel, "panelSinglePlayerOptions");
        
        } else if (isChaosMode == true){
        
            chaosMode();
        }
        
        loadNumbersStart();
        
        gui.leaderBoardTextArea.setEditable(false);
        
        gui.logTextArea.setEditable(false);
        
        gui.leaderBoardTextArea.setEditable(false);
        
        //Change this agent photo to what ever the user choose
       
       
                
        
        class numButtonChanged implements ItemListener {
           
           int buttonAmount;
           JToggleButton btnPressed;
           
           public numButtonChanged (int i, JToggleButton button){
           
               this.buttonAmount = i;
               this.btnPressed = button;
                  
           }
           
            @Override
            public void itemStateChanged(ItemEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                
                JToggleButton toggle = new JToggleButton();
                
                if (e.getStateChange() == 1){
                
                    toggle = (JToggleButton) e.getItem();
                    buttonAmount = Integer.parseInt(toggle.getText());
                }
                
                
                if (buttPress.size() < 2){
                    if (e.getStateChange() == 1){
                        buttPress.push(buttonAmount);
                        buttPressOrder.push(btnPressed);
           
                    }
                    else if (e.getStateChange()== 2){
                        removeAmountOnStack(buttonAmount, buttPress);
                        removeButtonOnStack(btnPressed, buttPressOrder);
                    }

                    //When toggleButton deselected
                    System.out.println(e.getStateChange() + " is the button state");
                    System.out.println(buttonAmount + " is the amount on the button");
                    System.out.println(buttPress.size() + " is the size of the stack");
                    
                } else if (buttPress.size() == 2 & e.getStateChange() == 2){
                
                    toggle = buttPressOrder.pop();
                    toggle.setSelected(false);
                    
                    buttPress.pop();
                    
                     //When toggleButton deselected
                    System.out.println(e.getStateChange() + " is the button state");
                    System.out.println(buttonAmount + " is the amount on the button");
                    System.out.println(buttPress.size() + " is the size of the stack");
                
                }
                
                //Refresh number labels
                refreshLabels(" ");
                
                
            }
        }
        
        class operButtonChanged implements ItemListener {
           
           String operator;
           JToggleButton toggleOper;
           
           public operButtonChanged (String operator, JToggleButton toggleOper){
           
               this.operator = operator;
               this.toggleOper = toggleOper;
               
           }

            @Override
            public void itemStateChanged(ItemEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                
                JToggleButton toggle = new JToggleButton();
                
                if (buttPress.size() == 2){
                    
                    toggle = buttPressOrder.pop();
                    System.out.println(toggle.getText() + " number of button that is going to be deselected");
                    toggle.setText((String.valueOf(userCalculation(buttPress, operator))));
                    
                    gui.logTextArea.setEditable(true);
                    
                    try {
                        FileWriter logWriter = new FileWriter(fileLog, true);
                        logWriter.write("Student Work: " + gui.firstNumLabel.getText() + " " + operator + " " + gui.secondNumLabel.getText() + " = " + String.valueOf(userCalculation(buttPress, operator)) + " Time: " + Calendar.getInstance().getTime().toString() );
                        logWriter.flush();
                        logWriter.close();
                    } catch (IOException ex) {
                        Logger.getLogger(gameState.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    gui.logTextArea.setText(gui.firstNumLabel.getText() + " " + operator +" "+
                             gui.secondNumLabel.getText() + 
                            " = " + String.valueOf(userCalculation(buttPress, operator)) + "\r\n" + gui.logTextArea.getText());
                    
                    gui.logTextArea.setText(gui.logTextArea.getText() + "\r\n");
                    
                    gui.logTextArea.setEditable(false);
                    
                    buttPress.clear();
//                    buttPressOrder.clear();

                    //Set Reset Flag to true when operation has been run and it is good to go
                    ResetFlag = true;
                  
                    toggle.setSelected(false);
                     
                    toggle = buttPressOrder.pop();
                    toggle.setText("0");
                    toggle.setEnabled(false);
                    
                    buttPressOrder.clear();
                    
                    operButtGroup.clearSelection();
                    
                    ResetFlag = false;
                    
                    
                                     
                } else {
                
                    operButtGroup.clearSelection();
                    
                
                }
                
            }
               
        }
        
        class checkForTheWin implements ItemListener {

            @Override
            public void itemStateChanged(ItemEvent e) {
                SwingUtilities.invokeLater(gameState::checkForWin);
                try{
                    FileWriter logWriter = new FileWriter(fileLog, true);
                    logWriter.write(name + " has created 24 from the give numbers, Score: " + gui.pointsAmountLabel.getText());
                } catch(IOException exc){
                
                }
            }
               
        }
        
       difficulty = gui.difficultySlider.getValue() / 10;
       
       if(isChaosMode == false)
            gui.pointsAmountLabel.setText("0");
       
       
       hintButtGroup.add(hintGUI.hint1Button);
       hintButtGroup.add(hintGUI.hint2Button);
       hintButtGroup.add(hintGUI.hint3Button);
              
       gui.NumButt1.addItemListener(new numButtonChanged(Integer.parseInt(gui.NumButt1.getText()), gui.NumButt1));
       gui.NumButt2.addItemListener(new numButtonChanged(Integer.parseInt(gui.NumButt2.getText()), gui.NumButt2));
       gui.NumButt3.addItemListener(new numButtonChanged(Integer.parseInt(gui.NumButt3.getText()), gui.NumButt3));
       gui.NumButt4.addItemListener(new numButtonChanged(Integer.parseInt(gui.NumButt4.getText()), gui.NumButt4));
       
       gui.AddButt.addItemListener(new operButtonChanged(gui.AddButt.getText(), gui.AddButt));
       gui.SubButt.addItemListener(new operButtonChanged(gui.SubButt.getText(), gui.SubButt));
       gui.DivButt.addItemListener(new operButtonChanged(gui.DivButt.getText(), gui.DivButt));
       gui.MultiButt.addItemListener(new operButtonChanged(gui.MultiButt.getText(), gui.MultiButt));
       
       gui.AddButt.addItemListener(new checkForTheWin());
       gui.SubButt.addItemListener(new checkForTheWin());
       gui.DivButt.addItemListener(new checkForTheWin());
       gui.MultiButt.addItemListener(new checkForTheWin());
       
       
       
       
       //Two Player button
       
       twoPlayerButtGroup.add(gui.connectToRadioButton);
       twoPlayerButtGroup.add(gui.beServerRadioButton);
       
       gui.startTwoPlayerButt.addActionListener((ActionEvent e) -> {
           twoPlayerFlag = true;
           String message = " ";
            try {
                if (gui.beServerRadioButton.isSelected() == true && !gui.serverPortTextField.getText().equalsIgnoreCase("")) {
                    //This instance will be a server
                    
                    String port = gui.serverPortTextField.getText();
                    int portNum = Integer.valueOf(port);
                    
                    ss = new ServerSocket(portNum);
                    gui.addressToConnectToLabel.setText("Tell friend to connect to address " + ss.getInetAddress() +
                            " and port number " + port);
                    s  = ss.accept();
                    
                    
                    DataInputStream din1 = new DataInputStream(s.getInputStream());
                    DataOutputStream dout1 = new DataOutputStream(s.getOutputStream());
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    twoPlayerFlag = true;
                    CardLayout card = (CardLayout) gui.mainPanel.getLayout();
                    //card.show(gui.mainPanel, "panelTwoPlayer");
                    card.show(gui.mainPanel, "panelSinglePlayer");
                    String str ="", str2 = "";
                    str = gui.NumButt1.getText();
                    dout1.writeUTF(str);
                    dout1.flush();
                    str = gui.NumButt2.getText();
                    dout1.writeUTF(str);
                    dout1.flush();
                    str = gui.NumButt3.getText();
                    dout1.writeUTF(str);
                    dout1.flush();
                    str = gui.NumButt4.getText();
                    dout1.writeUTF(str);
                    dout1.flush();
                    str = name;
                    dout1.writeUTF(str);
                    dout1.flush();
                    str = String.valueOf(highScore);
                    dout1.writeUTF(str);
                    dout1.flush();
                    str = "stop";
                    dout1.writeUTF(str);
                    dout1.flush();
                } else if (gui.connectToRadioButton.isSelected() == true && !gui.connectToPortTextField.getText().equalsIgnoreCase("")) {
                    //This instance will be a client
                    String port = gui.connectToPortTextField.getText();
                    int portNum = Integer.valueOf(port);
                    Socket s1 = new Socket("localhost",portNum);
                    DataInputStream din2 = new DataInputStream(s1.getInputStream());
                    DataOutputStream dout2 = new DataOutputStream(s1.getOutputStream());
                    BufferedReader br2 = new BufferedReader(new InputStreamReader(System.in));
                    twoPlayerFlag = true;
                    CardLayout card = (CardLayout) gui.mainPanel.getLayout();
                    //card.show(gui.mainPanel, "panelTwoPlayer");
                    card.show(gui.mainPanel, "panelSinglePlayer");
                    String str = "";
                    while (!str.equals("stop")) {
                        for (int i = 0; i < 5; i++) {
                            if (i == 0) {
                                str = din2.readUTF();
                                gui.NumButt1.setText(str);
                                ASafe = Integer.valueOf(str);
                            }
                            if (i == 1) {
                                str = din2.readUTF();
                                gui.NumButt2.setText(str);
                                BSafe = Integer.valueOf(str);
                            }
                            if (i == 2) {
                                str = din2.readUTF();
                                gui.NumButt3.setText(str);
                                CSafe = Integer.valueOf(str);
                            }
                            if (i == 3) {
                                str = din2.readUTF();
                                gui.NumButt4.setText(str);
                                DSafe = Integer.valueOf(str);
                            }
                            if (i == 4) {
                                str = din2.readUTF();
                                //gui.NumButt4.setText(str);
                            }
                        }
                        highScoresMap.put(din2.readUTF(), Integer.valueOf(din2.readUTF()));                    
                    }
                }
            }catch(IOException ex){
                System.out.println(ex.toString());
            }
            highScoresMap.forEach((key, value) -> {
                String userDirectoryHighScore1 = System.getProperty("user.dir") + "//RecordedHighScores.txt";
                File fileHighScores1 = new File(userDirectoryHighScore1);
                FileWriter fout;
                try {
                    fout = new FileWriter(fileHighScores1);
                    fout.write(key + " "+ value);
                }catch (IOException ex) {
                    Logger.getLogger(gameState.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            
            TimerTask task = new TimerTask() {
               @Override
               public void run() {
                   while(!message.equals("YouLose")){
                       try {
                           Socket s = new Socket("localhost",Integer.getInteger(gui.connectToPortTextField.getText()));
                           DataInputStream dinComp = new DataInputStream(s.getInputStream());
                       } catch (IOException ex) {
                           Logger.getLogger(gameState.class.getName()).log(Level.SEVERE, null, ex);
                       }  
                   } 
                       
                   CardLayout card = (CardLayout) gui.mainPanel.getLayout();
                   //card.show(gui.mainPanel, "panelTwoPlayer");
                   card.show(gui.mainPanel, "panelYouLose");
               }
           };

           compTimer = new Timer("Comp");
           
           compTimer.schedule(task, 1, 1000); 
            
        });
       
       gui.youLoseQuitButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {    
                CardLayout card = (CardLayout) gui.mainPanel.getLayout();
                //card.show(gui.mainPanel, "panelTwoPlayer");
                card.show(gui.mainPanel, "panelStartMenu");
            }
        });
       
       gui.startSettingsButt.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout card = (CardLayout) gui.mainPanel.getLayout();
                //card.show(gui.mainPanel, "panelTwoPlayer");
                card.show(gui.mainPanel, "panelStartSettings");
            }
       });
       
       gui.twoPlayerQuitButt.addActionListener((ActionEvent e) -> {
           goBackToMainPanel();
        });
       
       
       gui.compStartButton.addActionListener((ActionEvent e) -> {
           CardLayout card = (CardLayout) gui.mainPanel.getLayout();
           //card.show(gui.mainPanel, "panelTwoPlayer");
           card.show(gui.mainPanel, "panelTwoPlayerOption");
           
           compFlag = true;
        });
       

       
       
       //Single Player Buttons
       
       gui.singlePlayerButton.addActionListener((ActionEvent e) -> {
           gotoSinglePlayerOptions();
        });
       
       gui.buttonStartOptions.addActionListener((ActionEvent e) -> {
           //Change Panel to single player game
           gotoSinglePlayerGame();
           loadNumbersStart();
           
            try {
                FileWriter logWriter = new FileWriter(fileLog, true);
                logWriter.write("Game Start: Numbers at start: " + String.valueOf(ASafe) + " " + String.valueOf(BSafe) + " "
                    + String.valueOf(CSafe) + " " + String.valueOf(DSafe) +"," + " Difficulty: " + String.valueOf(difficulty)
                    + "," + "Points: " + gui.pointsAmountLabel.getText() + "," + "Solution Expressions: " );
                logWriter.write("\n");
                
                expressions.forEach((express) -> {
                    
                    try {
                        logWriter.write(express.toString());
                        logWriter.write("\n");
                    } catch (IOException ex) {
                        Logger.getLogger(gameState.class.getName()).log(Level.SEVERE, null, ex);
                    }
                
                });
                
                logWriter.flush();
                logWriter.close();
            } catch (IOException ex) {
                Logger.getLogger(gameState.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
       
       gui.buttonGoBack.addActionListener((ActionEvent e) -> {
           goBackToMainPanel();
        });
       
       gui.quitButton.addActionListener((ActionEvent e) -> {
           goBackToMainPanel();
        });
       
       gui.resetButton.addActionListener((ActionEvent e) -> {
           resetGame();
           resetCounter++;
           try {
                FileWriter logWriter = new FileWriter(fileLog,  true);
                logWriter.write(name + " reset the numbers to original state, Numer of current resets: " + String.valueOf(resetCounter));
                logWriter.write("\n");
            } catch (IOException ex) {
                Logger.getLogger(gameState.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
       
       gui.shuffleButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
               
                shuffleNumbers();
            
            }
       
       });
       
       gui.buttQuit.addActionListener((ActionEvent e) -> {
           goBackToMainPanel();
        });
       
       gui.buttPlayAgain.addActionListener((ActionEvent e) -> {
           CardLayout card = (CardLayout) gui.mainPanel.getLayout();
           card.show(gui.mainPanel, "panelSinglePlayer");
           
           shuffleNumbers();
        });
       
       gui.hintButton.addActionListener((ActionEvent e) -> {
           //Launch hint JFrame to display small hint
           hintGUI.setVisible(true);
           
           hintGUI.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
           
           getHintText();
           
           hintGUI.hintTextArea.setText(displyHints[0]);
           hintGUI.hint1Button.setEnabled(true);
        });
       
       hintGUI.hintOKButton.addActionListener((ActionEvent e) -> {
           hintGUI.setVisible(false);
           hintGUI.hintTextArea.setText("");
        });
       
       hintGUI.hint1Button.addActionListener((ActionEvent e) -> {
           hintGUI.hintTextArea.setText(displyHints[0]);
        });
       
        hintGUI.hint2Button.addActionListener((ActionEvent e) -> {
            hintGUI.hintTextArea.setText(displyHints[1]);
        });
        
        hintGUI.hint3Button.addActionListener((ActionEvent e) -> {
            hintGUI.hintTextArea.setText(displyHints[2]);
        });
        
        gui.helpButton.addActionListener((ActionEvent e) -> {
            helpGUI.setVisible(true);
            helpGUI.helpTextArea.setEditable(false);
            helpGUI.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        });
        
        helpGUI.okayHintButton.addActionListener((ActionEvent e) -> {
            helpGUI.setVisible(false);
        });
        
        
       
    }
    
    public static void getHintText(){
    
        String hint1 = "";
        String hint2 = "";
        String hint3 = "";
        
        
        for (String hint : hints) {
            if (hint.contains("(")) {
                hint1 = "Try multiplying or dividing to make a small product";
                hint2 = "Working backwards might help";
                hint3 = "Try starting with this \n";
                hint3 = hint3 + " " + hint.substring(hint.indexOf("("), hint.indexOf(")") + 1);
            } else {
                //hint1 = hints[0];
            }
        }
        
        displyHints[0] = hint1;
        displyHints[1] = hint2;
        displyHints[2] = hint3;

    }
    
    //Chaos Mode Option On
    
    public static void chaosMode(){
        
            gui.nameSettingLabel.setText(name);
            gui.gradeSettingLabel.setText(grade);
            gui.teacherNameTextField.setText(teacher);
    
           CardLayout card = (CardLayout) gui.mainPanel.getLayout();
           card.show(gui.mainPanel, "panelSinglePlayerOptions");
            
           gui.pointsAmountLabel.setText("1000");
           
           
           TimerTask task = new TimerTask() {
               @Override
               public void run() {
                   int amount = Integer.valueOf(gui.pointsAmountLabel.getText());
                   int amount2 = amount - 1;
                   gui.pointsAmountLabel.setText(String.valueOf(amount2));
                   System.out.println(String.valueOf(amount2));
               }
           };

           chaosTimer = new Timer("Chaos");
           
           chaosTimer.schedule(task, 1, 1000);
    
    }
    
    public static void checkForWin(){
    
        if("24".equals(gui.NumButt1.getText()) && 
                "0".equals(gui.NumButt2.getText()) &&
                "0".equals(gui.NumButt3.getText()) &&
                "0".equals(gui.NumButt4.getText())){
            displayWin();
        } else if("0".equals(gui.NumButt1.getText()) && 
                "24".equals(gui.NumButt2.getText()) &&
                "0".equals(gui.NumButt3.getText()) &&
                "0".equals(gui.NumButt4.getText())) {
            displayWin();
        } else if("0".equals(gui.NumButt1.getText()) && 
                "0".equals(gui.NumButt2.getText()) &&
                "24".equals(gui.NumButt3.getText()) &&
                "0".equals(gui.NumButt4.getText())) {
            displayWin();
        } else if("0".equals(gui.NumButt1.getText()) && 
                "0".equals(gui.NumButt2.getText()) &&
                "0".equals(gui.NumButt3.getText()) &&
                "24".equals(gui.NumButt4.getText())) {
            displayWin();
        }
        
    }
    
    public static void displayWin(){
        
        String response = " ";
        
        if(chaosFlag == false){
            points = points + 500;
        } else if(chaosFlag == true){
            chaosCounter++;
            points = Integer.valueOf(gui.pointsAmountLabel.getText()) + points;
            points = points + (chaosCounter * 100);
            
        }
        
        if(compFlag == true){
        
            if(ss != null){
                    
                while(!response.equals("YouWin!")){
                    try {
                        dout.writeUTF("YouLost!");
                        dout.flush();
                    } catch (IOException ex) {
                        Logger.getLogger(gameState.class.getName()).log(Level.SEVERE, null, ex);
                    } 
                }
            } 
        }
        
        
        highScore = highScore + points;
        
        gui.pointsAmountLabel.setText(String.valueOf(points));
    
        CardLayout card = (CardLayout) gui.mainPanel.getLayout();
        card.show(gui.mainPanel, "panelWinning"); 
        
        gui.highScoreAmount.setText(String.valueOf(highScore + points));
    
    }
    
    
    public static List<Integer> getNumbers(){
    
        List<Integer> numbers = new ArrayList<>();
        
        numbers.add(Integer.parseInt(gui.NumButt1.getText()));
        numbers.add(Integer.parseInt(gui.NumButt2.getText()));
        numbers.add(Integer.parseInt(gui.NumButt3.getText()));
        numbers.add(Integer.parseInt(gui.NumButt4.getText()));
        
        return numbers;
    
    }
    
    
    public static void shuffleNumbers(){
    
        resetGame();
        getNewNumbers();
        loadNumbersStart();
        
    }
    
    static void loadNumbersStart(){
        
        difficulty = gui.difficultySlider.getValue() / 10;
        
        ansCount = 0;
        
        while (ansCount < difficulty){
            
            ansCount = 0;
            for(int i = 0; i < hints.length; i++){
                hints[i] = "";
            }
            getNewNumbers();
            System.out.println("New Random Numbers Produced");
                         
            ansCount = makeSolutions(A, B, C, D);
        }
        
        gui.NumButt1.setLabel(String.valueOf(A));
        gui.NumButt2.setLabel(String.valueOf(B));
        gui.NumButt3.setLabel(String.valueOf(C));
        gui.NumButt4.setLabel(String.valueOf(D));
        
        operButtGroup.add(gui.AddButt);
        operButtGroup.add(gui.SubButt);
        operButtGroup.add(gui.DivButt);
        operButtGroup.add(gui.MultiButt);
        
        gui.firstNumLabel.setText(" ");
        //gui.operLabel.setText(" ");
        gui.secondNumLabel.setText(" ");
    
    }
    
    static void resetGame(){
    
        gui.NumButt1.setLabel(String.valueOf(ASafe));
        gui.NumButt2.setLabel(String.valueOf(BSafe));
        gui.NumButt3.setLabel(String.valueOf(CSafe));
        gui.NumButt4.setLabel(String.valueOf(DSafe));
        
        gui.NumButt1.setEnabled(true);
        gui.NumButt2.setEnabled(true);
        gui.NumButt3.setEnabled(true);
        gui.NumButt4.setEnabled(true);
        
        gui.NumButt1.setSelected(false);
        gui.NumButt2.setSelected(false);
        gui.NumButt3.setSelected(false);
        gui.NumButt4.setSelected(false);
        
        gui.logTextArea.setText(" ");
        
        buttPress.clear();
        buttPressOrder.clear();
    }
    
    static void getNewNumbers(){
    
        if(Math.random() > 0.9){
            while((A == B) || (B == C) || (C == D) || (D == A) || (C == A) || (B == D)){
                A = (int) (Math.random() * (13 - 1 + 1) + 1);
                B = (int) (Math.random() * (13 - 1 + 1) + 1);
                C = (int) (Math.random() * (13 - 1 + 1) + 1);
                D = (int) (Math.random() * (13 - 1 + 1) + 1);
                ASafe = A;
                BSafe = B;
                CSafe = C;
                DSafe = D;
            }    
        } else {
            A = (int) (Math.random() * (13 - 1 + 1) + 1);
            B = (int) (Math.random() * (13 - 1 + 1) + 1);
            C = (int) (Math.random() * (13 - 1 + 1) + 1);
            D = (int) (Math.random() * (13 - 1 + 1) + 1);
            ASafe = A;
            BSafe = B;
            CSafe = C;
            DSafe = D;
        }
    
    }
    
    static void refreshLabels(String operator){
    
        if (buttPress.size() == 1){
        
            gui.firstNumLabel.setText(String.valueOf(buttPress.elementAt(0)));
           // gui.operLabel.setText(operator);
            gui.secondNumLabel.setText(" ");
        } else if (buttPress.size() == 2){
            
            gui.firstNumLabel.setText(String.valueOf(buttPress.elementAt(0)));
          //  gui.operLabel.setText(operator);
            gui.secondNumLabel.setText(String.valueOf(buttPress.elementAt(1)));
        }
        
        if (buttPress.empty()){
        
            gui.firstNumLabel.setText(" ");
          //gui.operLabel.setText(" ");
            gui.secondNumLabel.setText(" ");
        
        }
    
    }
    
    static int userCalculation(Stack<Integer> numStack, String oper){
    
            
        int num1 = numStack.elementAt(0); //Chosen first
        int num2 = numStack.elementAt(1); //Chosen second
        int answer = 0;
        
        if (oper.equals("+")){
        
            answer = num2 + num1;
            refreshLabels("+");
        
        }
        if (oper.equals("-")){
        
            answer = num1 - num2;
            refreshLabels("-");
            
        }
        if (oper.equals("*")){
        
            answer = num2 * num1;
            refreshLabels("*");
            
        }
        if (oper.equals("/")){
        
            answer = num1 / num2;
            refreshLabels("/");
            
        }
        
        
        return answer;
        
    
    }
    
    static void removeAmountOnStack(Integer buttonAmount, Stack<Integer> buttonStack){
    
        Stack<Integer> tmpStack = new Stack<>();
    
        if (buttonStack.empty()){
            //do nothin; 
        }
        else
        {
            for (int i = 0; i < buttonStack.indexOf(buttonAmount) + 1; i++)
                tmpStack.push(buttonStack.pop());

            Integer removedElement = tmpStack.pop();

            while (!tmpStack.isEmpty())
                buttonStack.push(tmpStack.pop());

            //return removedElement;
        }
        
    }
    
      static void removeButtonOnStack(JToggleButton button, Stack<JToggleButton> buttonStack){
    
        Stack<JToggleButton> tmpStack = new Stack<>();
        
        if (ResetFlag == false){
            if (buttonStack.empty()){
                //do nothin; 
            }
            else
            {
                for (int i = 0; i < buttonStack.indexOf(button) + 1; i++)
                    tmpStack.push(buttonStack.pop());

                JToggleButton removedElement = tmpStack.pop();

                while (!tmpStack.isEmpty())
                    buttonStack.push(tmpStack.pop());

                //return removedElement;
            }
        }
      }
      
    
    static void gotoSinglePlayerOptions(){
        
        gui.nameSettingLabel.setText(name);
        gui.gradeSettingLabel.setText(grade);
        gui.teacherNameTextField.setText(teacher);
    
        CardLayout card = (CardLayout) gui.mainPanel.getLayout();
        card.show(gui.mainPanel, "panelSinglePlayerOptions"); 
    
    }
    
    static void gotoSinglePlayerGame(){
    
        CardLayout card = (CardLayout) gui.mainPanel.getLayout();
        card.show(gui.mainPanel, "panelSinglePlayer");   
    
    }
    
    static void goBackToMainPanel(){
    
       
            CardLayout card = (CardLayout) gui.mainPanel.getLayout();
            card.show(gui.mainPanel, "panelStartMenu");
            
        try {
            if (ss != null){
                ss.close();
            }
            if (s != null){
                s.close();
            }
            if (din != null){
                din.close();
            }
            if (dout != null){
                dout.close();
            }
            twoPlayerFlag = false;
        } catch (IOException ex) {
            Logger.getLogger(gameState.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
    public static void exitGame(){
    
        System.exit(0);
    }
    
    public static int makeSolutions(int ATemp, int BTemp, int CTemp, int DTemp){

        
        int[] operands = {ATemp, BTemp, CTemp, DTemp};
        
        int count = 0;
        
        expressions = new ArrayList();

        char[] operations = new char[] { '+', '-', '*', '/' };
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    
                    String exp = operands[0] +" "+ operations[i] +" "+ operands[1] +" "+ operations[j]
                            +" "+ operands[2] +" "+ operations[k] +" "+ operands[3];
                    
                    //String res = engine.eval(exp).toString();
                    String res = DijkstraTwoStack(exp);
                    
                    if (Double.valueOf(res).intValue() == 24) {
                        System.out.println(exp);
                        expressions.add(exp);
                        count ++;
                        //return true;
                    }
                }
            }
        }
        
        return count;
    
    }
    
    private static void permutation(String str){
        permutation("", str);
    }
    
    private static void permutation(String prefix, String str){
         int n = str.length();
        if (n == 0) System.out.println(prefix);
        else {
            for (int i = 0; i < n; i++)
                permutation(prefix + str.charAt(i), str.substring(0, i) + str.substring(i+1, n));
        }
    }
    
    private static String DijkstraTwoStack(String scanner){
    
        //Scanner scanner = new Scanner(System.in);
        String exp[] = scanner.split(" ");
        
        Stack<String> ops = new Stack<>();
        Stack<Double> vals = new Stack<>();

        for (String Field : exp) {
            switch (Field) {
                case "(":
                    break;
                case "+":
                case "*":
                case "/":
                case "-":
                    ops.push(Field);
                    break;
                case ")":
                    getComp(ops, vals);
                    break;
                default:
                    vals.push(Double.parseDouble(Field));
                    break;
            }
            
            if(vals.size() == 2 && ops.size() == 1){
                getComp(ops, vals);
            }
        }
        //getComp(ops, vals);
        //System.out.println(vals.pop());
        return String.valueOf(vals.pop());
    
    }
    
    private static void getComp(Stack<String> ops, Stack<Double> vals) {
        String op = ops.pop();
        switch (op) {
            case "+":
                vals.push(vals.pop() + vals.pop());
                break;
            case "*":
                vals.push(vals.pop() * vals.pop());
                break;
            case "-":
                double sub = vals.pop();
                vals.push(vals.pop() - sub);
                break;
            case "/":
                double divisor = vals.pop();
                vals.push(vals.pop() / divisor);
                break;
            default:
                break;
        }
    }
}

    


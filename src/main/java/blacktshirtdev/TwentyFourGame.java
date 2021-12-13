package blacktshirtdev;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;
import javax.swing.ButtonGroup;
import javax.swing.JToggleButton;
import javax.swing.Timer;


/**
 * Hello world!
 *
 */
public class TwentyFourGame 
{
    public static int A,B,C,D = 0;
    public static int ASafe,BSafe,CSafe,DSafe = 0;
    public static int ansCount = 0;
    public static int difficulty = 0;
    public static int points = 0;
    
    
    public static String[] hints = new String[20];
    public static String[] displyHints = new String[3];
    
    public static boolean addButtTog, SubButtTog, muliButtTog, divButtTog, numButt1,
            numButt2, numButt3, numButt4 = false;
    
    public static boolean ResetFlag = false;
    public static boolean TwoPlayerFlag = false;
    
    public static Stack<Integer> buttPress = new Stack<Integer>(); //LOL
    public static Stack<JToggleButton> buttPressOrder = new Stack<JToggleButton>();
    
//    public static ButtonGroup num1ButtGroup = new ButtonGroup();
//    public static ButtonGroup num2ButtGroup = new ButtonGroup();
    public static ButtonGroup operButtGroup = new ButtonGroup();
    public static ButtonGroup hintButtGroup = new ButtonGroup();
    public static ButtonGroup twoPlayerButtGroup = new ButtonGroup();
         
    public static Scanner scanner = new Scanner(System.in);
    
    public static FileWriter fout = null;
    
    //Screens      
    public static EntryGUI2 gui = new EntryGUI2();
      
    public static void main( String[] args ) 
    {
        try{
            String userDirectory = System.getProperty("user.dir") + "//UserSettings.txt";
            File file = new File(userDirectory);
            
            if(!file.canRead())
                file.setReadable(true);
            
            FileReader fileReader = new FileReader(file);
            
            CardLayout card = (CardLayout) gui.mainPanel.getLayout();
            card.show(gui.mainPanel, "panelStartMenu");
            
            gui.leaderBoardTextArea.setEditable(false);

            gui.logTextArea.setEditable(false);

            gui.leaderBoardTextArea.setEditable(false);
            
        } catch (FileNotFoundException e){
            CardLayout card = (CardLayout) gui.mainPanel.getLayout();
            card.show(gui.mainPanel, "firstTimeUsePanel");
        }
        
        gui.setVisible(true);
        
        
        gui.firstUseQuitButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
             }
        });
        
        gui.startContinueButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    String userDirectory = System.getProperty("user.dir") + "//UserSettings.txt";
                    File file = new File(userDirectory);
                    
                    if(!file.canWrite())
                        file.setWritable(true);
                    
                    FileWriter fout = new FileWriter(file);

                    fout.write(gui.nameTextField.getText());
                    fout.write('\n');
                    fout.write(gui.gradeTextField.getText());
                    fout.write('\n');
                    fout.write(gui.teacherNameTextField.getText());
                    fout.write('\n');
                    fout.flush();
                    fout.close();
                }catch(IOException ex){
                    System.out.println(ex);
                } finally {
                    try {
                        if(fout != null){
                            fout.flush();
                            fout.close();
                        }
                    } catch (IOException exc){
                        exc.printStackTrace();
                    }
                }
                
                
                
                CardLayout card = (CardLayout) gui.mainPanel.getLayout();
                card.show(gui.mainPanel, "panelStartMenu");

                gui.leaderBoardTextArea.setEditable(false);

                gui.logTextArea.setEditable(false);

                gui.leaderBoardTextArea.setEditable(false);
            }
        });
        
        gui.singlePlayerButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e){
               
//              gotoSinglePlayerOptions();
                gameState window =  new gameState(gui, false, false); 
           }
       });
       
       gui.buttonStartOptions.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e){
               //Change Panel to single player game
               //gotoSinglePlayerGame();
               //loadNumbersStart();
           }
       });
       
       gui.twoPlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                  gameState window =  new gameState(gui, true, false); 
            }
        });
       
       gui.chaosModeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameState window = new gameState(gui, false, true);
            }
        });
       
       gui.startSettingsButt.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
       });
       
    
    }
    
}
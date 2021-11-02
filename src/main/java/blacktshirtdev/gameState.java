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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

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
    
    public static String[] hints = new String[20];
    public static String[] displyHints = new String[3];
    
    public static boolean addButtTog, SubButtTog, muliButtTog, divButtTog, numButt1,
            numButt2, numButt3, numButt4 = false;
    
    public static boolean ResetFlag = false;
    
    public static Stack<Integer> buttPress = new Stack<Integer>(); //LOL
    public static Stack<JToggleButton> buttPressOrder = new Stack<JToggleButton>();
    
//    public static ButtonGroup num1ButtGroup = new ButtonGroup();
//    public static ButtonGroup num2ButtGroup = new ButtonGroup();
    public static ButtonGroup operButtGroup = new ButtonGroup();
    public static ButtonGroup hintButtGroup = new ButtonGroup();
         
    public static Scanner scanner = new Scanner(System.in);
    
    //Screens      
    public static EntryGUI2 gui = new EntryGUI2();
    public static hintGUI hintGUI = new hintGUI();
    public static helpGUI helpGUI = new helpGUI();
    
    public gameState() {
    
        gui.setVisible(true);
        
        CardLayout card = (CardLayout) gui.mainPanel.getLayout();
        card.show(gui.mainPanel, "panelStartMenu");
        
        //Math.random() * (max - min + 1) + min
        
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
                    
                    gui.logTextArea.setText(gui.logTextArea.getText() + gui.firstNumLabel.getText() +" "+
                             gui.secondNumLabel.getText() + 
                            " = " + String.valueOf(userCalculation(buttPress, operator)));
                    
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
                SwingUtilities.invokeLater( new Runnable(){
                    @Override
                    public void run() {
                        checkForWin();
                    }
                
                });
                
            }
               
        }
        
       difficulty = gui.difficultySlider.getValue() / 10;
       
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
       
       
       gui.singlePlayerButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e){
               
               gotoSinglePlayerOptions();
           }
       });
       
       gui.buttonStartOptions.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e){
               //Change Panel to single player game
               gotoSinglePlayerGame();
               loadNumbersStart();
           }
       });
       
       gui.buttonGoBack.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
           
               goBackToMainPanel();
           }
       });
       
       gui.quitButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
           
               goBackToMainPanel();
           }
       });
       
       gui.resetButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                
                resetGame();
            }
        });
       
       gui.shuffleButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
               
                shuffleNumbers();
            
            }
       
       });
       
       gui.buttQuit.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
               
                goBackToMainPanel();
            
            }
       
       });
       
       gui.buttPlayAgain.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
               
                CardLayout card = (CardLayout) gui.mainPanel.getLayout();
                card.show(gui.mainPanel, "panelSinglePlayer"); 
                
                shuffleNumbers();
            
            }
       });
       
       gui.hintButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            //Launch hint JFrame to display small hint   
                hintGUI.setVisible(true);
                
                hintGUI.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                
                getHintText();
                
                hintGUI.hintTextArea.setText(displyHints[0]);
                hintGUI.hint1Button.setEnabled(true);
                    
            }
       });
       
       hintGUI.hintOKButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            
                hintGUI.setVisible(false);
                hintGUI.hintTextArea.setText("");
                
            }
       });
       
       hintGUI.hint1Button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
           
                hintGUI.hintTextArea.setText(displyHints[0]);
            }

       });
       
        hintGUI.hint2Button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
           
                hintGUI.hintTextArea.setText(displyHints[1]);
            }
                
       });
        
        hintGUI.hint3Button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
           
                hintGUI.hintTextArea.setText(displyHints[2]);
            }

       });
        
        gui.helpButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
               helpGUI.setVisible(true);
               helpGUI.helpTextArea.setEditable(false);
               helpGUI.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            }
        });
        
        helpGUI.okayHintButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) { 
            
               helpGUI.setVisible(false);
            
            }
        });
       
    }
    
    //Agent Methods
    
    public static void AtoSmessage(String message){
    
        gui.agentTestMessage.setText(message);
        
    }
    
    
    
    //End Methods
    
    public static void getHintText(){
    
        String hint1 = "";
        String hint2 = "";
        String hint3 = "";
        
        
        for(int i = 0; i < hints.length; i++){
        
            if(hints[i].contains("(")){
                
                hint1 = "Try multiplying or dividing to make a small product";
                
                hint2 = "Working backwards might help";
                
                hint3 = "Try starting with this \n";
            
                hint3 = hint3 + " " + hints[i].substring(hints[i].indexOf("("), hints[i].indexOf(")") + 1);
                      
            } else {
                //hint1 = hints[0];
            }
        }
        
        displyHints[0] = hint1;
        displyHints[1] = hint2;
        displyHints[2] = hint3;

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
        
        points = points + 500;
        
        gui.pointsAmountLabel.setText(String.valueOf(points));
    
        CardLayout card = (CardLayout) gui.mainPanel.getLayout();
        card.show(gui.mainPanel, "panelWinning"); 
    
    }
    
    
    public static List<Integer> getNumbers(){
    
        List<Integer> numbers = new ArrayList<Integer>();
        
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
        
            for (Expression exp : getExpressions(Arrays.asList(A, B, C, D))){
                if (exp.evaluate() == 24) {
                    System.out.println(exp); 
                    hints[ansCount] = exp.toString();
                    ansCount += 1;
                    //gui.label1.setText(String.valueOf(ansCount)); ;
                }
            }
                         
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
    
        A = (int) (Math.random() * (13 - 1 + 1) + 1);
        B = (int) (Math.random() * (13 - 1 + 1) + 1);
        C = (int) (Math.random() * (13 - 1 + 1) + 1);
        D = (int) (Math.random() * (13 - 1 + 1) + 1);
        ASafe = A;
        BSafe = B;
        CSafe = C;
        DSafe = D;
    
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
        
        if (oper.equals("Add")){
        
            answer = num2 + num1;
            refreshLabels("+");
        
        }
        if (oper.equals("Sub")){
        
            answer = num1 - num2;
            refreshLabels("-");
            
        }
        if (oper.equals("Multi")){
        
            answer = num2 * num1;
            refreshLabels("*");
            
        }
        if (oper.equals("Divide")){
        
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
    
    }
    
    public static void exitGame(){
    
        System.exit(0);
    }
    
    
    static Set<Expression> getExpressions(Collection<Integer> numbers) {
    Set<Expression> expressions = new HashSet<Expression>();
    if (numbers.size() == 1) {
      expressions.add(new ConstantExpression(numbers.iterator().next()));
      return expressions;
    }
    
      for (Collection<Collection<Integer>> grouping : getGroupings(numbers))
      if (grouping.size() > 1)
        expressions.addAll(getExpressionsForGrouping(grouping));
      return expressions;
    }
    
    static Set<Expression> getExpressionsForGrouping(Collection<Collection<Integer>> groupedNumbers) {
    Collection<Set<Expression>> groupExpressionOptions = new ArrayList<Set<Expression>>();
    for (Collection<Integer> group : groupedNumbers)
      groupExpressionOptions.add(getExpressions(group));

    Set<Expression> result = new HashSet<Expression>();
    for (Collection<Expression> expressions : getCombinations(groupExpressionOptions)) {
      boolean containsAdditive = false, containsMultiplicative = false;
      for (Expression exp : expressions) {
        containsAdditive |= exp instanceof AdditiveExpression;
        containsMultiplicative |= exp instanceof MultiplicativeExpression;
      }

      Expression firstExpression = expressions.iterator().next();
      Collection<Expression> restExpressions = new ArrayList<Expression>(expressions);
      restExpressions.remove(firstExpression);

      for (int i = 0; i < 1 << restExpressions.size(); ++i) {
        Iterator<Expression> restExpressionsIter = restExpressions.iterator();
        Collection<Expression> a = new ArrayList<Expression>(), b = new ArrayList<Expression>();
        for (int j = 0; j < restExpressions.size(); ++j)
          Arrays.asList(a, b).get(i >> j & 1).add(restExpressionsIter.next());
        if (!containsAdditive)
          result.add(new AdditiveExpression(firstExpression, a, b));
        if (!containsMultiplicative)
          try {
            result.add(new MultiplicativeExpression(firstExpression, a, b));
          } catch (ArithmeticException e) {}
      }
    }
    return result;
  }
     

  // Sample input/output:
  // [ {a,b} ]               -> { [a], [b] }
  // [ {a,b}, {a} ]          -> { [a,b], [a,a] }
  // [ {a,b,c}, {d}, {e,f} ] -> { [a,d,e], [a,d,f], [b,d,e], [b,d,f], [c,d, e], [c,d,f] }
  static <T> Set<Collection<T>> getCombinations(Collection<Set<T>> collectionOfOptions) {
    if (collectionOfOptions.isEmpty())
      return new HashSet<Collection<T>>() {{ add(new ArrayList<T>()); }};

    Set<T> firstOptions = collectionOfOptions.iterator().next();
    Collection<Set<T>> restCollectionOfOptions = new ArrayList<Set<T>>(collectionOfOptions);
    restCollectionOfOptions.remove(firstOptions);

    Set<Collection<T>> result = new HashSet<Collection<T>>();
    for (T first : firstOptions)
      for (Collection<T> restCombination : getCombinations(restCollectionOfOptions))
        result.add(Util.concat(restCombination, first));
    return result;
  }

  static <T> Set<Collection<Collection<T>>> getGroupings(final Collection<T> values) {
    Set<Collection<Collection<T>>> result = new HashSet<Collection<Collection<T>>>();
    if (values.isEmpty()) {
      result.add(new ArrayList<Collection<T>>());
      return result;
    }

    for (Collection<T> firstGroup : getSubcollections(values)) {
      if (firstGroup.size() == 0)
        continue;

      Collection<T> rest = new ArrayList<T>(values);
      for (T value : firstGroup)
        rest.remove(value);

      for (Collection<Collection<T>> restGrouping : getGroupings(rest)) {
        result.add(Util.concat(restGrouping, firstGroup));
      }
    }
    return result;
  }

  static <T> Set<Collection<T>> getSubcollections(final Collection<T> values) {
    if (values.isEmpty())
      return new HashSet<Collection<T>>() {{ add(values); }};

    T first = values.iterator().next();
    Collection<T> rest = new ArrayList<T>(values);
    rest.remove(first);

    Set<Collection<T>> result = new HashSet<Collection<T>>();
    for (Collection<T> subcollection : getSubcollections(rest)) {
      result.add(subcollection);
      result.add(Util.concat(subcollection, first));
    }
    return result;
  }
}

abstract class Expression {
  abstract double evaluate();

  @Override
  public abstract boolean equals(Object o);

  @Override
  public int hashCode() {
    return new Double(evaluate()).hashCode();
  }

  @Override
  public abstract String toString();
}

abstract class AggregateExpression extends Expression {}

class AdditiveExpression extends AggregateExpression {
  final Expression firstOperand;
  final Collection<Expression> addOperands;
  final Collection<Expression> subOperands;

  AdditiveExpression(Expression firstOperand, Collection<Expression> addOperands,
                     Collection<Expression> subOperands) {
    this.firstOperand = firstOperand;
    this.addOperands = addOperands;
    this.subOperands = subOperands;
  }

  @Override
  double evaluate() {
    double result = firstOperand.evaluate();
    for (Expression exp : addOperands)
      result += exp.evaluate();
    for (Expression exp : subOperands)
      result -= exp.evaluate();
    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof AdditiveExpression) {
      AdditiveExpression that = (AdditiveExpression) o;
      return Util.equalsIgnoreOrder(Util.concat(this.addOperands, this.firstOperand),
                                    Util.concat(that.addOperands, that.firstOperand))
          && Util.equalsIgnoreOrder(this.subOperands, that.subOperands);
    }
    return false;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(firstOperand.toString());
    for (Expression exp : addOperands)
      sb.append('+').append(exp);
    for (Expression exp : subOperands)
      sb.append('-').append(exp);
    return sb.toString();
  }
}

class MultiplicativeExpression extends AggregateExpression {
  final Expression firstOperand;
  final Collection<Expression> mulOperands;
  final Collection<Expression> divOperands;

  MultiplicativeExpression(Expression firstOperand, Collection<Expression> mulOperands,
                           Collection<Expression> divOperands) {
    this.firstOperand = firstOperand;
    this.mulOperands = mulOperands;
    this.divOperands = divOperands;
    for (Expression exp : divOperands)
      if (exp.evaluate() == 0.0)
        throw new ArithmeticException();
  }

  @Override
  double evaluate() {
    double result = firstOperand.evaluate();
    for (Expression exp : mulOperands)
      result *= exp.evaluate();
    for (Expression exp : divOperands)
      result /= exp.evaluate();
    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof MultiplicativeExpression) {
      MultiplicativeExpression that = (MultiplicativeExpression) o;
      return Util.equalsIgnoreOrder(Util.concat(this.mulOperands, this.firstOperand),
                                    Util.concat(that.mulOperands, that.firstOperand))
          && Util.equalsIgnoreOrder(this.divOperands, that.divOperands);
    }
    return false;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(maybeAddParens(firstOperand));
    for (Expression exp : mulOperands)
      sb.append('*').append(maybeAddParens(exp));
    for (Expression exp : divOperands)
      sb.append('/').append(maybeAddParens(exp));
    return sb.toString();
  }

  static String maybeAddParens(Expression exp) {
    return String.format(exp instanceof AdditiveExpression ? "(%s)" : "%s", exp);
  }
}

class ConstantExpression extends Expression {
  final int value;

  ConstantExpression(int value) {
    this.value = value;
  }

  @Override
  double evaluate() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof ConstantExpression && value == ((ConstantExpression) o).value;
  }

  @Override
  public String toString() {
    return Integer.toString(value);
  }
}

class Util {
  static <T> boolean equalsIgnoreOrder(Collection<T> a, Collection<T> b) {
    Map<T, Integer> aCounts = new HashMap<T, Integer>(), bCounts = new HashMap<T, Integer>();
    for (T value : a) aCounts.put(value, (aCounts.containsKey(value) ? aCounts.get(value) : 0) + 1);
    for (T value : b) bCounts.put(value, (bCounts.containsKey(value) ? bCounts.get(value) : 0) + 1);
    return aCounts.equals(bCounts);
  }

  static <T> Collection<T> concat(Collection<T> xs, final T x) {
    List<T> result = new ArrayList<T>(xs);
    result.add(x);
    return result;
  }
}

    


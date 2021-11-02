/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twentyFourei;

import blacktshirtdev.gameState;

import eis.eis2java.annotation.AsAction;
import eis.eis2java.annotation.AsPercept;
import eis.eis2java.translation.Filter;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author logan
 */
public class Entity {
    
    private gameState controller;
    
    public Entity(gameState controller){this.controller = controller; }
    
    //Percepts and Actions here
    @AsPercept(name = "numbers", multiplePercepts = true, multipleArguments = true, filter = Filter.Type.ONCE)
    public List<Integer> discs() {
        List<Integer> numbers = new ArrayList<Integer>();

        if (controller.getNumbers() != null) {
            for (Integer number : controller.getNumbers()) {
                numbers.add(number);
            }
        }

        return numbers;
    }
    
    @AsAction(name = "press")
    public void pressButton(int buttonNumber) {
        
        // buttonNumber corrsponds to buttons from top left going clockwise

        // Perform checks..

        // Execute actual command.
        //controller.moveDisc(controller.getPins()[from], controller.getPins()[to]);
    }
    
    @AsAction(name = "AtoSmessage")
    public void AtoSmessage(String message) {
        
        
        controller.AtoSmessage(message);
        
        // buttonNumber corrsponds to buttons from top left going clockwise

        // Perform checks..

        // Execute actual command.
        //controller.moveDisc(controller.getPins()[from], controller.getPins()[to]);
    }
      
}

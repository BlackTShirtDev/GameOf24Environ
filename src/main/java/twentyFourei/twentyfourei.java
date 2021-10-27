/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twentyFourei;

import blacktshirtdev.gameState;

import eis.eis2java.environment.AbstractEnvironment;
import eis.exceptions.EntityException;
import eis.exceptions.ManagementException;
import eis.iilang.Action;
import eis.iilang.EnvironmentState;
import eis.iilang.Parameter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author logan
 */
public class twentyfourei extends AbstractEnvironment{
    
    private gameState game = null;
    
    public void init(Map<String, Parameter> parameters) throws ManagementException{
    
  
        reset(parameters);
        try
        {
          registerEntity("entity", "gamecontroller", new Entity(this.game));
        } catch (EntityException e) {
          throw new ManagementException("Could not create an entity", e);
        }
    }
    
    public void reset(Map<String, Parameter> parameters) throws ManagementException {
       
        List start = new ArrayList();
        Parameter p = (Parameter) parameters.get("discs"); // Will need to call number I think
        
        if(p != null){
            
            //Not sure what goes in here
        } else {
        
            start.add(Integer.valueOf(0)); //Maybe I put the numbers on the button labels in here
            start.add(Integer.valueOf(0));
            start.add(Integer.valueOf(0));
            start.add(Integer.valueOf(0));
        
        }
        
        if(this.game == null){
        
            
              this.game = new gameState();
                
//            this.game.addWindowListener(new WindowAdapter(){
//            
//                public void windowClosing(WindowEvent e){
//                    try{
//                    
//                        twentyfourei.this.game.dispose();
//                        twentyfourei.this.kill();
//                    
//                    } catch (ManagementException el){
//                        el.printStackTrace();
//                    }
//                }
//            });
            
            
        }
        

        setState(EnvironmentState.PAUSED);
    }
    
    public void kill() throws ManagementException
      {
        if (this.game != null)
          //this.game.removeAll();
          //End game elegantly
          this.game.exitGame();
        try
        {
          deleteEntity("entity");
        } catch (Exception e) {
          e.printStackTrace();
        }
        if (!getState().equals(EnvironmentState.KILLED))
          setState(EnvironmentState.KILLED);
      }
    
    protected boolean isSupportedByEnvironment(Action action)
      {
        if ((action.getName().equals("add")) && (action.getParameters().size() == 2))
          return true;
        
        return false;
      }

  protected boolean isSupportedByType(Action action, String type)
      {
        return isSupportedByEnvironment(action);
      }
    
}

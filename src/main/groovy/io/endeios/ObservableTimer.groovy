package io.endeios

import javafx.application.Platform;

class ObservableTimer extends Observable{
   private Timer t = new Timer('ObservableTimer',true) 
   public ObservableTimer(Long interval){
    t.schedule(new TimerTask(){
            public void run(){
        Platform.runLater(new Runnable(){
            @Override
            public void run(){
               setChanged();
               notifyObservers(new Date());  
            }
        })       
        }
    },0,interval)
   }
}


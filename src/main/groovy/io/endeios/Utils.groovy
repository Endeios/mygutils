package io.endeios
import javafx.application.Platform

class Utils{
    public static void inFX(Closure c){
        Platform.runLater(new Runnable(){
            public void run(){
                c()
            }
        })


    }
}

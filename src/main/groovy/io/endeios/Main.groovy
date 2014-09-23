package io.endeios

import java.nio.file.*
import io.endeios.DirectoryNotifier

public class Main{
    public static void main(String[] args){
        def fs = FileSystems.getDefault()
        def path = fs.getPath('.')

        def dn = new DirectoryNotifier(path)
        println dn
        def observer = new myObserver()
        dn.addObserver(observer)
        def th = new Thread(dn,"notifierThread")
        println th
        th.setDaemon(false)
        th.start()
    }

}

class myObserver implements Observer{
    public void update(Observable obs, Object obj){
        if(obs instanceof DirectoryNotifier ){
            println "File ${obj.file} has been updated with ${obj.data}"
        }
    }
}

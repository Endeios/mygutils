package io.endeios

import java.nio.file.Path
import groovy.transform.*
import java.nio.file.FileSystems
import java.nio.file.FileSystem
import java.nio.file.WatchService
import java.nio.file.StandardWatchEventKinds as swe

@groovy.util.logging.Slf4j
@ToString
class DirectoryNotifier extends Observable implements Runnable {
    FileSystem fs
    WatchService watcher
    def key;

    Path baseDir;
    Map<String,Long> sizes;
    Boolean exit = false;

    public DirectoryNotifier(Path baseDir){
        this.baseDir = baseDir;
        sizes = new HashMap<String,Long>()
        fs = FileSystems.getDefault()
        watcher = fs.newWatchService()
        key = baseDir.register(watcher,swe.ENTRY_MODIFY,swe.ENTRY_CREATE,swe.ENTRY_DELETE)
    }

    @Override
    public void run(){
        while(!exit){
            try{
                log.debug("Waiting for events")
                key = watcher.take()
            }catch(InterruptedException x){
               log.error(x);
              return

            }
            key.pollEvents().each{event->
                def ev_kind = event.kind()
                def ev_context = event.context()
                def filename = baseDir.toString()+File.separator+ev_context
                def oldSize = sizes.get(filename)
                if(oldSize==null){
                    oldSize = 0
                }
                def aFile = new File(filename)
                def fr = new FileReader(aFile)
                log.debug("Skipping ${oldSize} bytes")
                fr.skip(oldSize)
                def buff = new char[1024]
                def redChars = 0;
                def sb = new StringBuffer()
                while((redChars=fr.read(buff))!=-1){
                    sb.append(Arrays.copyOfRange(buff,0,redChars))
                }
                sizes[filename]= aFile.size()
                setChanged()
                notifyObservers([data:sb.toString(),file:filename]);


            }
            boolean valid = key.reset();
            if(!valid) 
                break
        }
    } 
}


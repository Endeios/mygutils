import io.endeios.*

import static groovyx.javafx.GroovyFX.start


start{
    /**
    Defs here
     */
    def queryTextField;
    def queryButton;
    def queryTableView;
    def headersTextField;
    def dataTextField;
    def listButton;
    def listTableView;
    stage(title:"MyFactories example",visible:true){
        scene(fill:GREEN, width:500,height:600){
            tabPane(){
                tab(text:"Database"){
                    vbox(padding:10,fillWidth:true){
                        hbox(padding:0,spacing:20,fillHeight:true){
                            //label
                            label(text:"Query field")
                            //textfield
                            queryTextField=textField(prefColumnCount:20,height:140)
                            //button
                            queryButton=button(text:"Query")
                        }
                        //tableView
                        queryTableView=tableView()
                    }
                }
                tab(text:"List of Values"){
                    vbox(padding:20){
                        //label
                        label(text:"Headers, comma separated")
                        //headers textfield
                        headersTextField=textField(prefColumnCount:20,height:40)
                        //label
                        label(text:"Data,in ordered lines, json style")
                        //data textfield
                        dataTextField=textField(prefColumnCount:20,height:40)
                        //execute button
                        listButton=button(text:"List data")
                        //tableView
                        listTableView=tableView()
                    }
                }
            }
        }
    }
}

import io.endeios.*

import static groovyx.javafx.GroovyFX.start
import groovy.sql.Sql
import javafx.collections.FXCollections
import javafx.scene.control.*

def classLoader = Thread.currentThread().getContextClassLoader()

datafixUri = classLoader.findResource("people.db")
people = Sql.newInstance("jdbc:sqlite:${datafixUri.getFile()}",'','','org.sqlite.JDBC')


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
                            queryTextField=textField(prefColumnCount:20,height:140,text:"Select * from person")
                            //button
                            queryButton=button(text:"Query",onAction:{event->
                                def list = people.rows queryTextField.text 
                                def data = FXCollections.observableArrayList(list)
                                def cols = queryTableView.getColumns()
                                cols.removeAll(cols)
                                if(list.size>0){
                                    list[0].keySet().each{
                                        def col = new TableColumn("$it")
                                        col.setCellValueFactory(new GroovyResultValueFactory("$it"))
                                        cols.add(col)
                                    }
                                    queryTableView.items = data;
                                }

                           })
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

import io.endeios.*

import static groovyx.javafx.GroovyFX.start
import groovy.sql.Sql
import javafx.collections.FXCollections
import javafx.scene.control.*
import org.apache.commons.lang3.StringUtils
def classLoader = Thread.currentThread().getContextClassLoader()

datafixUri = classLoader.findResource("people.db")
people = Sql.newInstance("jdbc:sqlite:${datafixUri.getFile()}",'','','org.sqlite.JDBC')

csv = 
"""a;b;c
1;2;3
4;5;6
"""

start{
    /**
    Defs here
     */
    def queryTextField;
    def queryButton;
    def queryTableView;
    def csvTextArea;
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
                        label(text:"Header and Data,in ordered lines, csv(semicolon ';' separated ) style")
                        //data textfield
                        dataTextField=textArea(prefColumnCount:20,height:140,text:csv)
                        //execute button
                        listButton=button(text:"List data",onAction:{event->
                            def rawData = dataTextField.text
                            def lines = StringUtils.split(rawData,"\n")
                            def headers = StringUtils.split(lines.head(),";")
                            def cols = listTableView.getColumns()
                            cols.removeAll(cols)
                            if(headers!=null){
                                headers.eachWithIndex{header,index->
                                    def col = new TableColumn("$header")
                                    col.setCellValueFactory(new ListResultValueFactory(index))
                                    cols.add(col)
                                }
                            }
                            
                           def list =lines.tail().collect{line->
                                StringUtils.split(line,";")
                           }

                           def data = FXCollections.observableArrayList(list)
                           listTableView.items=data

                        })
                        //tableView
                        listTableView=tableView()
                    }
                }
            }
        }
    }
}

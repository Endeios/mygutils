package io.endeios 


import javafx.scene.control.cell.PropertyValueFactory;
import groovy.sql.GroovyRowResult
import javafx.beans.property.*
import javafx.beans.value.*
import javafx.scene.control.TableColumn.CellDataFeatures
import javafx.util.*

class GroovyResultValueFactory implements Callback < CellDataFeatures < GroovyRowResult, String >, ObservableValue< String > >{

    String val;

    GroovyResultValueFactory(String val){
        this.val = val
    }

    public ObservableValue<String> call(CellDataFeatures<GroovyRowResult, String> p){
        new SimpleObjectProperty(p.getValue()[val])
    }
}



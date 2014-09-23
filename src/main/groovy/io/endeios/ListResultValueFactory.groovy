package io.endeios

import javafx.beans.property.*
import javafx.beans.value.*
import javafx.scene.control.TableColumn.CellDataFeatures
import javafx.util.*

class ListResultValueFactory<T> implements Callback < CellDataFeatures < List<T>, String >, ObservableValue< String > >{

    Integer position;

    ListResultValueFactory(Integer val){
        this.position = val
    }

    public ObservableValue<String> call(CellDataFeatures<List<T>, String> p){
        new SimpleObjectProperty(p.getValue().getAt(position))
    }
}


package io.endeios

//for standalone use!
//@Grab(group='org.apache.xmlrpc', module='xmlrpc-client', version='3.1.3')

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;


class XMLRPCClient {

    XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();    
    XmlRpcClient client = new XmlRpcClient();
    private String prefix = ""

    XMLRPCClient(URL url){
        config.setServerURL(url)        
        client.setConfig(config);
    }

    Object propertyMissing(String name){
        if(prefix  == ""){
            prefix = name +"."
        } else {
            prefix = prefix+name+"."
        }
        return this
    }
    
    def methodMissing(String name, Object args) {
        def old_prefix = prefix;
        prefix = ""
        client.execute(old_prefix+name,args)
    }
}

My Utils
=======

Under package io.endeios

*XMLRPCClient*
--------------
 Simple wrap for apache's xmlrpcclient library

        [source,groovy]
        --- 
        import io.endeios.XMLRPCClient
        def serverProxy = new XMLRPCClient(new URL('http://rpc.example.com:8000/RPC2'))
        //then simply "call methods"
        serverProxy.system.listMethods().each{println it}
        // let' say that there is the usual sum(integer integer) method
        def sum = serverProxy.sum(41,1)
        ---






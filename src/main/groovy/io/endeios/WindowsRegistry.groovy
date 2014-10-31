package io.endeios

/**
 * @author Oleg Ryaboy, based on work by Miguel Enriquez
 * found on http://stackoverflow.com/questions/62289/read-write-to-windows-registry-using-java
 * and http://stackoverflow.com/questions/3866823/how-to-use-java-runtime-exec-with-windows-reg-utility-to-read-update-delete-en
 * info also on http://hawkee.com/snippet/9723/
 */
public class WindowsRegistry
{

    /**
     * 
     * @param location
     *          path in the registry
     * @param key
     *          registry key
     * @return registry value or null if not found
     */
    public static final String readRegistry(String location, String key)
    {
        try
        {
            // Run reg query, then read output with StreamReader (internal class)
            def execString = "reg query \"" + location + "\" /v \"" + key + "\""
            println "$execString"
            Process process = Runtime.getRuntime().exec(execString);

            StreamReader reader = new StreamReader(process.getInputStream());
            reader.start();
            process.waitFor();
            reader.join();
            String output = reader.getResult();

            // Output has the following format:
            // \n<Version information>\n\n<key>\t<registry type>\t<value>
            //  println "$output \n\n->-> ${output.contains("\t")}"
            // under windows 7 the format changed :P
            switch(System.getProperty("os.name")){
            case("Windows 7"):windows7:{
                def magic = WindowsRegistry.getMagic(output)
                if(magic==null){
                    //sorry didnt find anything
                    return null;
                }
                else{
                    String[] parsed = output.split(magic);
                    return parsed[parsed.length-1].trim()
                }
                break
            }
                
            default:defCase:{
                if (!output.contains("\t"))
                {
                    return null;
                }
    
                // Parse out the value
                String[] parsed = output.split("\t");
                if(parsed.length > 0)
                {
                    String result = parsed[parsed.length - 1].trim();
                    result = result.substring(1, result.length() - 1);
                    return result;
                }
                break;
             }
          }
        }
        catch (Exception e)
        {
        }
        return null;
    }

    static class StreamReader extends Thread
    {
        private InputStream is;
        private StringWriter sw = new StringWriter();;

        public StreamReader(InputStream is)
        {
            this.is = is;
        }

        public void run()
        {
            try
            {
                int c;
                while ((c = is.read()) != -1)
                    sw.write(c);
            }
            catch (IOException e)
            {
            }
            finally
            {
                try
                {
                    is.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        public String getResult()
        {
            return sw.toString();
        }
    }

    public static boolean deleteValue(String key, String valueName)
    {
        try
        {
            // Run reg query, then read output with StreamReader (internal class)
            Process process = Runtime.getRuntime().exec("reg delete \"" + key + "\" /v \"" + valueName + "\" /f");

            StreamReader reader = new StreamReader(process.getInputStream());
            reader.start();
            process.waitFor();
            reader.join();
            String output = reader.getResult();

            // Output has the following format:
            // \n<Version information>\n\n<key>\t<registry type>\t<value>
            return output.contains("The operation completed successfully");
        }
        catch (Exception e)
        {
        }
        return false;
    }
    
    private static String getMagic(String data){
        def magics = ["REG_SZ","REG_BINARY","REG_DWORD"]
        return magics.find{magic->data.contains(magic)}
    }

    public static boolean addValue(String key, String valName, String val)
    {
        try
        {
            // Run reg query, then read output with StreamReader (internal class)
            Process process = Runtime.getRuntime().exec(
                    "reg add \"" + key + "\" /v \"" + valName + "\" /d \"\\\"" + val + "\\\"\" /f");

            StreamReader reader = new StreamReader(process.getInputStream());
            reader.start();
            process.waitFor();
            reader.join();
            String output = reader.getResult();

            // Output has the following format:
            // \n<Version information>\n\n<key>\t<registry type>\t<value>
            return output.contains("The operation completed successfully");
        }
        catch (Exception e)
        {
        }
        return false;
    }

}

package com.britesnow.snow.test;

import java.io.File;
import java.io.IOException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppClassLoader;
import org.eclipse.jetty.webapp.WebAppContext;

public class JettyServer {
    public static void main(String[] args){
        Server server = new Server(8080);
        try {
            
            WebAppContext webapp = new WebAppContext();
            webapp.setContextPath("/");
            webapp.setResourceBase("src/test/resources/simpleApp");
            
            StringBuilder extraClasspathSB = null;
            File webinfClassesDir = new File("target/classes");
            if (webinfClassesDir.exists()){
                extraClasspathSB = (extraClasspathSB == null) ? new StringBuilder() : extraClasspathSB.append(';');
                extraClasspathSB.append(webinfClassesDir.getAbsolutePath());
            }
            File classesDir = new File("target/test-classes");
            if (classesDir.exists()){
                extraClasspathSB = (extraClasspathSB == null) ? new StringBuilder() : extraClasspathSB.append(';');
                extraClasspathSB.append(classesDir.getAbsolutePath());
            }
            
            File jettyLibDir = new File("/Users/friping/apps/jetty-distribution-7.3.1.v20110307", "lib");
            loadJars(jettyLibDir,extraClasspathSB);
            
            if (extraClasspathSB != null) {
                webapp.setExtraClasspath(extraClasspathSB.toString());
            }
            ClassLoader serverCL = webapp.getClass().getClassLoader();
            CustomWebAppClassLoader webAppCL = new CustomWebAppClassLoader(serverCL, webapp);
            webapp.setClassLoader(webAppCL);
            
            server.setHandler(webapp);
            server.start();
            server.join();
            
        } catch (Exception e) {
            e.printStackTrace();
            try {
                server.stop();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
    
    private static StringBuilder loadJars(File file,StringBuilder sb){
        for (File libFile : file.listFiles()) {
            if (libFile.getAbsolutePath().endsWith(".jar")) {
                // create SB or add ';'
                sb = (sb == null) ? new StringBuilder() : sb.append(';');
                sb.append(libFile.getAbsolutePath());
            }else if(libFile.isDirectory()){
                sb.append(loadJars(libFile,sb));
            }
        }
        return sb;
    }
    
}

class CustomWebAppClassLoader extends WebAppClassLoader {
    
    public CustomWebAppClassLoader(ClassLoader parentCL, WebAppContext webAppContext) throws IOException {
        super(parentCL, webAppContext);
    }
    
}
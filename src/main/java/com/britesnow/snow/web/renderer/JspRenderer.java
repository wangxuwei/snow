package com.britesnow.snow.web.renderer;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.inject.Singleton;
import javax.servlet.ServletException;

import com.britesnow.snow.web.RequestContext;
import com.britesnow.snow.web.path.ResourceFileLocator;
import com.google.inject.Inject;

@Singleton
public class JspRenderer {
//    static private Logger logger = LoggerFactory.getLogger(JspRenderer.class);
    
    @Inject
    private ResourceFileLocator resourceFileResolver;
    
    public boolean hasTemplate(String resourcePath, RequestContext rc){
        File jspFile = resourceFileResolver.locate(resourcePath + ".jsp", rc);
        return (jspFile != null)?true:false;
    }

    public void foward(RequestContext rc) {
        try {
            for(Iterator ite = rc.getWebModel().keySet().iterator(); ite.hasNext(); ){
                String key = (String) ite.next();
                rc.getReq().setAttribute(key, rc.getWebModel().get(key));
            }
            rc.getReq().getRequestDispatcher(rc.getResourcePath()+".jsp").forward(rc.getReq(), rc.getRes());
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

}

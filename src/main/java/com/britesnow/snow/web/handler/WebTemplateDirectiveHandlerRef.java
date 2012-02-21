/* Copyright 2009 Jeremy Chone - Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package com.britesnow.snow.web.handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

import com.britesnow.snow.web.RequestContext;
import com.britesnow.snow.web.param.WebParamRef;
import com.britesnow.snow.web.param.WebParameterParser;

import freemarker.core.Environment;

public class WebTemplateDirectiveHandlerRef extends BaseWebHandlerRef {

    
    public WebTemplateDirectiveHandlerRef(Object object, Method method, Map<Class<? extends Annotation>,WebParameterParser> webParameterParserMap) {
        super(object, method, webParameterParserMap);
        initWebParamRefs();
    }    
    
    public void invokeWebTemplateDirective(Environment env,Map paramMap,RequestContext rc) throws Exception{
        Object[] paramValues = new Object[webArgRefs.size()];
        
        //the first param of a WebModel MUST be the Model Map
        paramValues[0] = env;
        paramValues[1] = paramMap;
        
        if (webArgRefs.size() > 2){
            for (int i = 2; i < paramValues.length;i++){
                WebParamRef webParamRef = webArgRefs.get(i);
                paramValues[i] = webParamRef.getValue(method, rc);
            }
        }
        
        method.invoke(webHandler,paramValues);
    }
}

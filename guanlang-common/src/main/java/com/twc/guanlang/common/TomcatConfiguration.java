package com.twc.guanlang.common;


import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 自定义嵌入tomcatserver
 */
@Configuration
public class TomcatConfiguration {


    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();


        tomcat.addAdditionalTomcatConnectors(createSslConnector());
        return tomcat;
    }

    private Connector createSslConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        //Connector监听的http的端口号
        connector.setPort(803);
        connector.setSecure(false);
        //监听到http的端口号后转向到的https的端口号
        connector.setRedirectPort(8443);
        return connector;
    }

    /**
     * 创建wss协议接口
     *
     * @return
     */
//    @Bean
//    public TomcatContextCustomizer tomcatContextCustomizer() {
//        System.out.println("init");
//        return new TomcatContextCustomizer() {
//            public void customize(Context context) {
//                System.out.println("init   customize");
//                context.addServletContainerInitializer(new WsSci(), null);
//            }
//        };
//    }


}

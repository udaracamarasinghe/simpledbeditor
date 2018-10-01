package org.github.udaracamarasinghe.simpledbeditorwebui.configs;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


/**
 * 
 * @author Udara Amarasinghe
 */
@Configuration
@ComponentScan(basePackages = "lk.dialog.ccs.crmbackwebcom.configctrl")
@PropertySource("classpath:/application.properties")
public class ModuleConfigs {

}

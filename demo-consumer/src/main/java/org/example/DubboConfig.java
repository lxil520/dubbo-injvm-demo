package org.example;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.Configuration;

@EnableDubbo
@Configuration
public class DubboConfig {
    /**
     * true：添加rpc链路id；
     * false：不添加rpc链路id；
     * <p>默认添加</p>
     */
    private boolean mdcSwitch = true;

    public boolean getMdcSwitch() {
        return mdcSwitch;
    }

    public void setMdcSwitch(boolean mdcSwitch) {
        this.mdcSwitch = mdcSwitch;
    }
}

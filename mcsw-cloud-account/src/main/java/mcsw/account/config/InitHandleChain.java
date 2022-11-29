package mcsw.account.config;

import mcsw.account.util.filter.HandleRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class InitHandleChain {

    @Autowired
    List<HandleRegister> handleRegisters;

    @Bean(name = "registerChain")
    public HandleRegister buildFirst(){
        for (int i = 0; i < handleRegisters.size() - 1; i++) {
            handleRegisters.get(i).setNext(handleRegisters.get(i + 1));
        }
        return handleRegisters.get(0);
    }
}

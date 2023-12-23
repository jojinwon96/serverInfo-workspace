package kr.smartisoft.demo.Perfomance.common;

import kr.smartisoft.demo.Perfomance.entity.Servers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class ServerInfoProcessBuilder {

    private final String serverSpec;

    private ServerInfoProcessBuilder(@Value("${serverSpec}") String serverSpec) {
        this.serverSpec = serverSpec;
    }

}

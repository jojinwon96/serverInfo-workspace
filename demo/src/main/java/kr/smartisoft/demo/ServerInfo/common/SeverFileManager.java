package kr.smartisoft.demo.ServerInfo.common;

import com.google.gson.Gson;
import kr.smartisoft.demo.ServerInfo.entity.Servers;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class SeverFileManager {

    private static final String FILE_PATH = "/home/server_info/server_info.json";

    public Servers saveToFile(Servers servers) {

        File file = new File(FILE_PATH);

        // 파일이 존재하지 않으면 디렉토리와 함께 파일 생성
        if (!file.exists()) {
            File directory = file.getParentFile();
            if (!directory.exists()) {
                directory.mkdirs(); // 디렉토리 생성
            }

            try {
                file.createNewFile(); // 파일 생성
            } catch (IOException e) {
                e.printStackTrace();
            }

            try (FileWriter fileWriter = new FileWriter(file)) {
                String json = new Gson().toJson(servers);
                fileWriter.write(json);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return readJsonFromFile();
    }

    public Servers readJsonFromFile() {
        FileReader fileReader = null;
        Gson gson = new Gson();
        try {
            fileReader = new FileReader(FILE_PATH);
        } catch (FileNotFoundException e) {
            return null;
        }
        return gson.fromJson(fileReader, Servers.class);
    }
}

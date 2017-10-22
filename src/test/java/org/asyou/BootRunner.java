package org.asyou;

import org.asyou.model.LoanModel;
import pro.tools.data.text.ToolRandoms;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

/**
 * Created on 17/10/22 20:48 星期日.
 *
 * @author sd
 */
public class BootRunner {

    public static LoanModel buildNewModel() {
        return new LoanModel()
                .setId(ToolRandoms.getRightNanoTime())
                .setLoanName(ToolRandoms.getRandomStr())
                .setAddDate(new Date())
                ;
    }

    public static Properties load(String fileName) {
        Properties newProp = new Properties();
        InputStream resource = BootRunner.class.getClassLoader().getResourceAsStream(fileName);
        try {
            newProp.load(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newProp;
    }
}

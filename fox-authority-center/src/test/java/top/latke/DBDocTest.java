package top.latke;

import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 数据库表文档生成
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class DBDocTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void buildDBDoc() {
        DataSource dataSource = applicationContext.getBean(DataSource.class);
        EngineConfig engineConfig = EngineConfig.builder()
                .fileOutputDir("C:\\Users\\fire\\Desktop\\fire\\work\\github\\gxssy-cloud\\doc")
                .openOutputDir(false)
                .fileType(EngineFileType.HTML)
                .produceType(EngineTemplateType.freemarker)
                .build();
        Configuration configuration = Configuration.builder()
                .version("1.0.0")
                .description("gxssy-cloud")
                .dataSource(dataSource)
                .engineConfig(engineConfig)
                .produceConfig(getProduceConfig())
                .build();
        new DocumentationExecute(configuration).execute();
    }

    /**
     * 配置想要生成的数据表和想要忽略的数据表
     * @return
     */
    private ProcessConfig getProduceConfig() {
        List<String> ignoreTableName = Collections.singletonList("undo_log");
        List<String> ignorePrefix = Arrays.asList("a","b");
        List<String> ignoreSuffix = Arrays.asList("c","d");

        return ProcessConfig.builder()
                .designatedTableName(Collections.emptyList())
                .designatedTablePrefix(Collections.emptyList())
                .designatedTableSuffix(Collections.emptyList())
                .ignoreTableName(ignoreTableName)
                .ignoreTablePrefix(ignorePrefix)
                .ignoreTableSuffix(ignoreSuffix)
                .build();
    }
}

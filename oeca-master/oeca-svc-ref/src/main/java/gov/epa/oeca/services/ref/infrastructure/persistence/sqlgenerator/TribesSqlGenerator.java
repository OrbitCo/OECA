package gov.epa.oeca.services.ref.infrastructure.persistence.sqlgenerator;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author dfladung
 */
public class TribesSqlGenerator {

    private static final Logger logger = LoggerFactory.getLogger(TribesSqlGenerator.class);

    private static final String sql = "insert into oeca_ref_tribes(id, tribal_code, tribal_name) values(%s,%s,%s);";

    public static void main(String[] args) {
        try {
            File input = new File(Thread.currentThread().getContextClassLoader().getResource("reference/tribes.csv").toURI());
            int count = 0;

            for (String line : FileUtils.readLines(input, Charset.defaultCharset())) {
                List<String> fields = Lists.newArrayList(Splitter.on(Pattern.compile(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)")).split(line));
                String tribalCode = fields.get(0);
                String tribeName = StringUtils.remove(fields.get(1), "\"").replace("'", "''").replace(";", ",");
                String sql = String.format(TribesSqlGenerator.sql, ++count, getString(tribalCode), getString(tribeName));
                System.out.println(sql);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private static String getString(String str) {
        if (!StringUtils.isEmpty(str)) {
            return "'" + str + "'";
        } else {
            return "NULL";
        }
    }
}

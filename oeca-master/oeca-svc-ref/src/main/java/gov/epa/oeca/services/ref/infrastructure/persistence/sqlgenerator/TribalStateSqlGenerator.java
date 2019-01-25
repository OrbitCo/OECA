package gov.epa.oeca.services.ref.infrastructure.persistence.sqlgenerator;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author dfladung
 */
public class TribalStateSqlGenerator {

    private static final String sql = "insert into oeca_ref_tribe_states(tribal_code, state_code) " +
            "values(%s,%s);";

    public static void main(String[] args) {
        try {
            File input = new File(Thread.currentThread().getContextClassLoader().getResource("reference/tribal_states.csv").toURI());
            int count = 0;

            for (String line : FileUtils.readLines(input, Charset.defaultCharset())) {
                List<String> fields = Lists.newArrayList(Splitter.on(Pattern.compile(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)")).split(line));
                String tribalCode = fields.get(0);
                String state = fields.get(1);
                String sql = String.format(TribalStateSqlGenerator.sql, getString(tribalCode), getString(state));
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

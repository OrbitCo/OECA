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
public class StateSqlGenerator extends RefSqlGenerator {

    private static final String sql = "insert into oeca_ref_states(id, region_code, state_code, state_name, state_usage, state_fed_operator_text) values(%s, %s,%s,%s,%s,%s);";

    public static void main(String[] args) {
        try {
            File input = new File(Thread.currentThread().getContextClassLoader().getResource("reference/states.csv").toURI());
            int count = 0;

            for (String line : FileUtils.readLines(input, Charset.defaultCharset())) {
                List<String> fields = Lists.newArrayList(Splitter.on(Pattern.compile(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)")).split(line));
                String region = fields.get(0);
                String state = fields.get(1);
                String stateName = fields.get(2);
                String usage = fields.get(4);
                String fedOperatorText = StringUtils.remove(fields.get(6), "\"").replace("'", "''").replace(";", ",");
                String sql = String.format(StateSqlGenerator.sql, ++count, getString(region), getString(state),
                        getString(stateName), getString(usage), getString(fedOperatorText));
                System.out.println(sql);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}

package gov.epa.oeca.common.domain.registration;

import gov.epa.oeca.common.domain.BaseValueObject;

import java.util.Comparator;

/**
 * @author dfladung
 */
public class Dataflow extends BaseValueObject {

    private static final long serialVersionUID = 1L;

    String acronym;
    String name;

    public Dataflow(){
    }

    public Dataflow(String acronym, String name) {
        this.acronym = acronym;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Dataflow{" +
                "acronym='" + acronym + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Comparator<Dataflow> DataflowNameComparator
            = new Comparator<Dataflow>() {

        public int compare(Dataflow df1, Dataflow df2) {

            String dfName1 = df1.getName().toUpperCase();
            String dfName2 = df2.getName().toUpperCase();

            //ascending order
            return dfName1.compareTo(dfName2);

        }

    };
}

package gov.epa.oeca.common.domain.registration;

import gov.epa.oeca.common.domain.BaseValueObject;

/**
 * @author dfladung
 */
public class Answer extends BaseValueObject {

    private static final long serialVersionUID = 1L;

    Question question;
    String answer;

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}

package hello;

import java.util.ArrayList;
import java.util.List;

public class Question {
	
	private String question ;
	private String message ;
	private List<Answer> answers = new ArrayList<Answer>();

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}
	
	

}

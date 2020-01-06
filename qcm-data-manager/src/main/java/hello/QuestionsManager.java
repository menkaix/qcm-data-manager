package hello;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QuestionsManager {

	public void clearQuestions() {
		questions.clear();
	}

	public Question firsQuestion() {
		currentIndex = 0;
		return currentQuestion();
	}

	public Question LastQuestion() {
		currentIndex = this.questions.size() - 1;
		return currentQuestion();
	}

	public Question previousQuestion() {
		currentIndex--;
		if (currentIndex < 0) {
			currentIndex = 0;
		}
		return currentQuestion();
	}

	public Question nextQuestion() {
		currentIndex++;
		if (currentIndex >= this.questions.size()) {
			currentIndex = this.questions.size() - 1;
		}
		return currentQuestion();
	}

	public Question currentQuestion() {
		if (questions.size() <= 0) {
			currentIndex = 0;
			questions.add(new Question());
		}
		
		if(currentIndex>=this.questions.size()) {
			currentIndex = this.questions.size() - 1;
		}

		return this.questions.get(currentIndex);
	}

	public Question goToQuestion(int newIndex) {

		if (newIndex <= 0) {
			currentIndex = 0;
		} else if (newIndex >= this.questions.size()) {
			currentIndex = this.questions.size() - 1;
		} else {
			currentIndex = newIndex;
		}

		return currentQuestion();
	}

	public Question newQuestion() {

		questions.add(new Question());

		return LastQuestion();
	}

	public Question deleteCurrentQuestion() {

		return questions.remove(currentIndex);

	}

	public int getCurrentIndex() {
		return currentIndex;
	}
	
	public int getQuestionsNumber() {
		return questions.size();
	}

	public void loadFromFile(String path) {

		ObjectMapper objectMapper = new ObjectMapper();

		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		File file = new File(path);

		try {
			questions = objectMapper.readValue(file, new TypeReference<List<Question>>() {
			});
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void saveToFile(String path) {

		ObjectMapper mapper = new ObjectMapper();

		try {
			mapper.writeValue(new FileOutputStream(path), questions);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// ===================== properties =====================

	private List<Question> questions;
	private int currentIndex;

	// ======================= Singleton ====================

	private static QuestionsManager instance;

	private QuestionsManager() {

		questions = new ArrayList<Question>();
	}

	public static QuestionsManager getInstance() {

		if (instance == null) {
			instance = new QuestionsManager();
		}

		return instance;
	}

}

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FactoringQuiz extends JFrame implements ActionListener {
	// Main frame container
	private Container content = this.getContentPane();

	// Screens for the quiz
	private IntroScreen introScreen = new IntroScreen();
	private QuizScreen quizScreen = new QuizScreen();
	private SummaryScreen summaryScreen = new SummaryScreen();
	
	/**
	 * Initializes the GUI and the quiz.
	 */
	private FactoringQuiz() {
		// Main frame setup
		this.setVisible(true);
		this.setSize(700, 500);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("Factoring Quadratics Quiz");
		this.setResizable(false);

		// Action listeners for switching between screens
		introScreen.addActionListener(this);
		quizScreen.addActionListener(this);
		summaryScreen.addActionListener(this);
		
		//Start the quiz
		introScreen.initIntroScreen();
		content.add(introScreen, BorderLayout.CENTER);
	}
	
	/**
	 * Event handler for switching between quiz screens.
	 * @param e - ActionEvent that triggered by requested screen switch.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();

		content.removeAll();

		if(action == "startQuiz") {
			quizScreen.initQuizScreen(introScreen.getNumQuestions());
			content.add(quizScreen, BorderLayout.CENTER);
		}
		else if(action == "endQuiz"){
			summaryScreen.initSummaryScreen(quizScreen.getQuizSummary());
			content.add(summaryScreen, BorderLayout.CENTER);
		}
		else if(action == "restartQuiz") {
			introScreen.initIntroScreen();
			content.add(introScreen, BorderLayout.CENTER);
		}

		//Fixes glitches with screen display
		this.setSize(699, 499);
		this.setSize(700, 500);
	}
	
	/**
	 * Launches the Factoring Quiz.
	 * @param args - Unused.
	 */
	public static void main(String[] args) {
		new FactoringQuiz();
	}
}
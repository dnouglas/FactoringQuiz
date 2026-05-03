/**
 * Program created using Eclipse IDE for Java Programming.
 * Factoring Quiz that tests on the ability to factor basic quadratics.
 * Utilizes functionality from:
 * - String class (store/process/display character strings) by Lee Boynton, Arthur van Hoff, Martin Buchholz, and Ulf Zibis
 * - Math class (for randomization of questions) by Joseph D. Darcy
 * - JFrame (for visual displays) by Jeff Dinkins, Georges Saab, and David Kloba
 * - ActionListener/ActionEvent (for user input) by Carl Quinn
 * - As well as several other swing and awt components (by various authors, too many to list all)
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FactoringQuiz extends JFrame implements ActionListener{
	//Variables necessary for storage and tracking
	private int numQs, qNum, numCorrect;
	private boolean intro, quiz, answered, summary;
	/* 2D array that will contain data that summarizes the quiz
	 * there will be numQs rows, which represents numQs questions 
	 * 4 columns:
	 * 	column (index) 0 contains the actual questions, 
	 * 	column 1 contains possible correct answers for each question, 
	 * 	column 2 contains what the user answered, 
	 *	column 3 contains the response status (user answer correctly or incorrectly?)
	 */
	private String[][] quizSummary;

	private Container content = this.getContentPane();
	
	//GUI components needed for introScreen before quiz
	private JPanel introScreen = new JPanel(new BorderLayout());
	
		private JLabel lblTitle = new JLabel("Quiz: Factoring Quadratic Equations", JLabel.CENTER);
		
		private JPanel introPane = new JPanel(new BorderLayout());
			private JPanel pnlNumQues = new JPanel(new GridLayout(2, 1));
				private JLabel lblToggleInst = new JLabel("", JLabel.CENTER);
				private JPanel pnlToggleCont = new JPanel(new GridLayout(1, 3));
					private JPanel pnlToggleQues = new JPanel(new BorderLayout());
						private JLabel lblnumQues = new JLabel("", JLabel.CENTER);
						private JButton btnMore = new JButton("+");
						private JButton btnLess = new JButton("-");
			private JTextArea taInfo = new JTextArea("");
			
		private JButton btnStartQuiz = new JButton("Start Quiz");
	
	
	//GUI components needed for quizScreen during quiz
	private JPanel quizScreen = new JPanel(new GridLayout(3, 1));
	
		private JPanel questionPane = new JPanel(new BorderLayout());
			private JPanel pnlQuestionBar = new JPanel(new FlowLayout(FlowLayout.TRAILING));
				private JLabel[] lblArrQStatus;
			private JPanel pnlQuestion = new JPanel(new GridLayout(2, 1));
				private JLabel lblQuestion = new JLabel("", JLabel.CENTER);
				private JLabel lblEquation = new JLabel("", JLabel.CENTER);
	
		private JPanel responsePane = new JPanel(new GridLayout(3,1));
			private JLabel lblFeedback = new JLabel("", JLabel.CENTER);
			private JPanel pnlNextQ = new JPanel(new GridLayout(1, 3));
				private JButton btnNextQ = new JButton("Next Question");
			private JPanel pnlResponse = new JPanel(new FlowLayout());
				private JTextField tfAns = new JTextField(20);
				private JButton btnSubmit = new JButton("Check");
	
		private JPanel keyPane = new JPanel(new GridLayout(3, 5, 5, 5));
			private JButton[] btnArrNums = new JButton[9];
			private JButton btnx = new JButton("x");
			private JButton btnPlus = new JButton("+");
			private JButton btnMinus = new JButton("-");
			private JButton btnOpenPar = new JButton("(");
			private JButton btnClosePar = new JButton(")");
			private JButton btnBackSpace = new JButton("⌫");
			
			
	//GUI components needed for summaryScreen after quiz
	private JPanel summaryScreen = new JPanel(new BorderLayout());
			
		private JLabel lblScore = new JLabel("", JLabel.CENTER);
				
		private JPanel summaryPane = new JPanel(new BorderLayout());
			private JPanel pnlArrows = new JPanel(new BorderLayout());
				private JLabel lblQuesNav = new JLabel("", JLabel.CENTER);
				private JButton btnNext = new JButton("►");
				private JButton btnPrev = new JButton("◄");
			private JTextArea taStats = new JTextArea("");
					
		private JButton btnRestartQuiz = new JButton("Retake Quiz");

	
	/**
	 * Sets up the GUI, formats the three screens, and displays the introScreen
	 */
	private FactoringQuiz() {
		this.setVisible(true);
		this.setSize(700, 500);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("Factoring Quadratics Quiz");
		this.setResizable(false);
		
		
		//Set up introScreen 
		introScreen.add(lblTitle, BorderLayout.NORTH);
			lblTitle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
					
		introScreen.add(introPane, BorderLayout.CENTER);
			introPane.add(taInfo, BorderLayout.CENTER);
				taInfo.setEditable(false);
				taInfo.setOpaque(false);
				taInfo.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
				taInfo.append("About the Quiz:"
						+ "\n   - This quiz will test your knowledge on factoring simple quadratic equations"
						+ "\n   - Use the toggle bar above to change the number of questions in the quiz"
						+ "\n   - During the quiz, use the built-in keyboard to type in your answers"
						+ "\n   - Enter your answers as factors of the original equation"
						+ "\n   - Your progress will be shown on the top right of the screen"
						+ "\n   - You will be able to see your results and review answers after the quiz"
						+ "\n   - Click on the button below to begin"
						+ "\n   - Good Luck!");
			introPane.add(pnlNumQues, BorderLayout.NORTH);
				pnlNumQues.add(lblToggleInst);
					lblToggleInst.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 15));
				pnlNumQues.add(pnlToggleCont);
					pnlToggleCont.add(new JLabel(" "));
					pnlToggleCont.add(pnlToggleQues);
						pnlToggleQues.add(lblnumQues, BorderLayout.CENTER);
						pnlToggleQues.add(btnMore, BorderLayout.EAST);
							btnMore.addActionListener(this);
						pnlToggleQues.add(btnLess, BorderLayout.WEST);
							btnLess.addActionListener(this);
					pnlToggleCont.add(new JLabel(" "));
						
		introScreen.add(btnStartQuiz, BorderLayout.SOUTH);
			btnStartQuiz.addActionListener(this);
		
		
		//Set up quizScreen
		//Top section of the quiz screen
		quizScreen.add(questionPane);
			questionPane.add(pnlQuestion, BorderLayout.CENTER);
				pnlQuestion.add(lblQuestion);
					lblQuestion.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 40));
				pnlQuestion.add(lblEquation);
					lblEquation.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 35));
					lblEquation.setBackground(Color.LIGHT_GRAY);
					lblEquation.setOpaque(true);
			questionPane.add(pnlQuestionBar, BorderLayout.NORTH);
			
		//Middle section of the quiz screen
		quizScreen.add(responsePane);
			responsePane.add(lblFeedback);
				lblFeedback.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 15));
			responsePane.add(pnlNextQ);
				pnlNextQ.add(new JLabel());
				pnlNextQ.add(btnNextQ);
					btnNextQ.addActionListener(this);
					btnNextQ.setVisible(false);
				pnlNextQ.add(new JLabel());
			responsePane.add(pnlResponse);
				pnlResponse.add(tfAns);
					tfAns.setEditable(false);
				pnlResponse.add(btnSubmit);
					btnSubmit.addActionListener(this);
		
		//Bottom section of the quiz screen: keyboard for entering responses
		quizScreen.add(keyPane);
			keyPane.add(btnx);
				btnx.addActionListener(this);
			keyPane.add(btnPlus);
				btnPlus.addActionListener(this);
			keyPane.add(btnMinus);
				btnMinus.addActionListener(this);
			keyPane.add(btnOpenPar);
				btnOpenPar.addActionListener(this);
			keyPane.add(btnClosePar);
				btnClosePar.addActionListener(this);
			
			for (int i = 1; i <= btnArrNums.length; i++) {
				btnArrNums[i-1] = new JButton("" + i);
				btnArrNums[i-1].addActionListener(this);
				keyPane.add(btnArrNums[i-1]);
			}
			keyPane.add(btnBackSpace);
				btnBackSpace.addActionListener(this);
				
				
		//Set up summaryScreen
		summaryScreen.add(lblScore, BorderLayout.NORTH);
			lblScore.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
		
		summaryScreen.add(summaryPane, BorderLayout.CENTER);
			summaryPane.add(taStats, BorderLayout.CENTER);
				taStats.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				taStats.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
				taStats.setEditable(false);
				taStats.setOpaque(false);
			summaryPane.add(pnlArrows, BorderLayout.NORTH);
				pnlArrows.add(btnNext, BorderLayout.EAST);
					btnNext.addActionListener(this);
				pnlArrows.add(btnPrev, BorderLayout.WEST);
					btnPrev.addActionListener(this);
				pnlArrows.add(lblQuesNav, BorderLayout.CENTER);
					lblQuesNav.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
				
		summaryScreen.add(btnRestartQuiz, BorderLayout.SOUTH);
			btnRestartQuiz.addActionListener(this);
			
			
		//Start the quiz
		this.showIntro();
	}
	
	/**
	 * Controls for all the buttons used in the program (responses to user input).
	 * When a button is pressed, this method is invoked.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton btn = (JButton) e.getSource();
		
		if (intro) {
			if (btn == btnMore)
				this.toggleNumQues(true);
			else if (btn == btnLess)
				this.toggleNumQues(false);
			else if (btn == btnStartQuiz)
				this.startQuiz();
		}
		else if (quiz) {
			//keyboard and submit button only work when question is unanswered
			if (!answered) {
				lblFeedback.setText("Enter answers as (ax+b)(cx+d) OR (b+ax)(d+cx). OMIT a if a=1");
				
				if (btn == btnSubmit)
					this.checkAnswer();
				else if (btn == btnx) 
					tfAns.setText(tfAns.getText() + "x");
				else if (btn == btnPlus) 
					tfAns.setText(tfAns.getText() + "+");
				else if (btn == btnMinus) 
					tfAns.setText(tfAns.getText() + "-");
				else if (btn == btnOpenPar)
					tfAns.setText(tfAns.getText() + "(");
				else if (btn == btnClosePar) 
					tfAns.setText(tfAns.getText() + ")");
				else if (btn == btnBackSpace && tfAns.getText().length() != 0) 
					tfAns.setText(tfAns.getText().substring(0, tfAns.getText().length()-1));
				
				for (int i = 1; i <= btnArrNums.length; i++) {
					if (btn == btnArrNums[i-1]) {
						tfAns.setText(tfAns.getText() + i);
						break;
					}
				}
				
				//make sure user doesn't enter too long answers
				if (tfAns.getText().length() >= 20)
					tfAns.setText(tfAns.getText().substring(0, 20));
			}
			else if (btn == btnNextQ)
				this.nextQuestion();
		}
		else if (summary){
			if (btn == btnRestartQuiz) 
				this.showIntro();
			else if (btn == btnNext)
				this.toggleSummaryScreen(true);
			else if (btn == btnPrev)
				this.toggleSummaryScreen(false);
		}
	}
	
	/**
	 * Displays the intro screen.
	 */
	private void showIntro() {
		content.removeAll();
		content.add(introScreen, BorderLayout.CENTER);
		
		intro = true;
		summary = false;
		numQs = 5;
		lblnumQues.setText("" + numQs);
		lblToggleInst.setText("Use the + and - buttons to modify the number of questions in the quiz");
		
		//Fixes glitches with screen display
		this.setSize(699, 499);
		this.setSize(700, 500);
	}
	
	/**
	 * Controls for the toggle buttons on the intro screen.
	 * 10 questions max, 3 questions min.
	 * @param add - true indicates +1 questions, false indicates -1 questions
	 */
	private void toggleNumQues(boolean add) {
		if (add) {
			if (numQs != 10) {
				lblnumQues.setText("" + ++numQs);
				lblToggleInst.setText("Click the 'Start Quiz' button below to begin the quiz with " + numQs + " questions.");
			}
		}
		else {
			if (numQs != 3) {
				lblnumQues.setText("" + --numQs);
				lblToggleInst.setText("Click the 'Start Quiz' button below to begin the quiz with " + numQs + " questions.");
			}
		}
	}

	/**
	 * Starts the quiz and displays the quiz screen.
	 */
	private void startQuiz() {
		content.removeAll();
		content.add(quizScreen, BorderLayout.CENTER);
		
		this.createQuestions();
		
		qNum = 1;
		numCorrect = 0;
		intro = false;
		quiz = true;
		answered = false;
		
		lblArrQStatus = new JLabel[numQs];
		pnlQuestionBar.removeAll();
		JLabel lblProgress = new JLabel("Progress: ", JLabel.CENTER);
			lblProgress.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		pnlQuestionBar.add(lblProgress);
		
		for (int i = 0; i < lblArrQStatus.length; i++) {
			lblArrQStatus[i] = new JLabel("", JLabel.CENTER);
			lblArrQStatus[i].setText("☐");
			lblArrQStatus[i].setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
			lblArrQStatus[i].setForeground(Color.BLACK);
			
			pnlQuestionBar.add(lblArrQStatus[i]);
			
		}
		lblArrQStatus[0].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
		
		lblQuestion.setText("Question " + qNum);
		lblEquation.setText(quizSummary[qNum-1][0]);
		lblFeedback.setText("Enter answers as (ax+b)(cx+d) OR (b+ax)(d+cx). OMIT a if a=1");
		
		
		//Fixes glitches with screen displays
		this.setSize(699, 499);
		this.setSize(700, 500);
	}
	
	/**
	 * Create questions and all allowed answers for each question.
	 * Populates the first two columns of quizSummary array.
	 */
	private void createQuestions() {
		quizSummary = new String[numQs][4];
		int a, b, c, d, term1, term2, term3;
		String fact1, fact2, fact1inv, fact2inv; //factors to construct answers, standard and inverted
		
		//for each question provide information to the 2D array
		for (int i = 0; i < quizSummary.length; i++) {
			// For a and c, the leading coefficients of each factor, 1/3 chance of it being 2, 2/3 chance of 1.
			// For b and d, the constant of each factor, number between 1 to 9 with equal chance of it being positive/negative.
			a = (int)(Math.random()*3) == 1 ? 2 : 1; 
			b = ((int)(Math.random()*9) + 1) * ((int)(Math.random()*2) == 1 ? 1 : -1);
			c = (int)(Math.random()*3) == 1 ? 2 : 1;
			d = ((int)(Math.random()*9) + 1) * ((int)(Math.random()*2) == 1 ? 1 : -1);
			
			//get rid of any GCFs
			if (b%a == 0) {
				a /= a;
				b /= a;
			}
			if (d%c == 0) {
				c /= c;
				d /= c;
			}
			
			//create terms
			term1 = a*c;
			term2 = a*d + b*c;
			term3 = b*d;
			
			//create equation (accounts for difference of squares)
			quizSummary[i][0] = "Factor: " + (term1 == 1 ? "x²" : term1 + "x²");
			if (term2 > 0)
				quizSummary[i][0] += "+" + term2 + "x";
			else if (term2 < 0)
				quizSummary[i][0] += term2 + "x";
			quizSummary[i][0] += (term3 > 0 ? "+" + term3 : term3);
			
			//create all accepted answers
			fact1 = "(" + (a == 1 ? "" : a) + "x" + (b > 0 ? "+" + b : b) + ")";
			fact2 = "(" + (c == 1 ? "" : c) + "x" + (d > 0 ? "+" + d : d) + ")";
			fact1inv = "(" + b + "+" + (a == 1 ? "" : a) + "x)";
			fact2inv = "(" + d + "+" + (c == 1 ? "" : c) + "x)";
			
			// accepted answers separated by comma and space, extra ", , " for later display purposes.
			if (fact1.equals(fact2))//if it is perfect square trinomial, prevents repetition
				quizSummary[i][1] = fact1 + fact2 + ",  , " + fact1inv + fact2inv +  ", " +
									fact1 + fact2inv + ", " + fact1inv + fact2;
			else
				quizSummary[i][1] = fact1 + fact2 + ", " + fact2 + fact1 + ",  , " +
									fact1inv + fact2inv + ", " + fact2inv + fact1inv + ", " +
									fact1 + fact2inv + ", " + fact2inv + fact1 + ", " +
									fact1inv + fact2 + ", " + fact2 + fact1inv;
		}
	}
	
	/**
	 * When user submits an answer, this method will run to determine if the question was right or wrong.
	 */
	private void checkAnswer() {
		String ans = quizSummary[qNum-1][1], userAns = tfAns.getText();
		int status = this.isCorrect(ans, userAns);
		
		//If no answer was entered by the user.
		if (status == 0) {
			lblFeedback.setText("Please use the built-in keyboard below to enter your answer");
			return;
		}
			
		//answer is either right or wrong, either way the following must happen to move to next Question:
		btnNextQ.setVisible(true);
		if (qNum == numQs)
			btnNextQ.setText("View Results");
		quizSummary[qNum-1][2] = userAns;
		answered = true;
		
		//If the status is 1, aka answer is correct
		if(status == 1) {
			numCorrect++;
			quizSummary[qNum-1][3] = "Correct!";
				
			lblFeedback.setText("Well Done!");
			lblArrQStatus[qNum-1].setText("☑");
			lblArrQStatus[qNum-1].setForeground(new Color(0, 200, 0));
			return;
		}
		
		//If the status is -1, aka no match.
		quizSummary[qNum-1][3] = "Incorrect.";
					
		lblFeedback.setText("Incorrect. Correct answers include: " + ans.substring(0, ans.indexOf(",  , ")));
		lblArrQStatus[qNum-1].setText("☒");
		lblArrQStatus[qNum-1].setForeground(new Color(200, 0, 0));
	}
	
	/**
	 * Helper method for checkAnswer(). Determines if userAns is a valid answer.
	 * @param ans - String containing all accepted answers, separated by a comma and a space --> ", "
	 * @param userAns - String to search for within all of the accepted answers in ans.
	 * @return 0 if userAns is empty (user did not provide an answer.
	 * 		   1 if userAns is found in ans (correct answer).
	 * 		   -1 if userAns is not found in ans (incorrect answer).
	 */
	private int isCorrect(String ans, String userAns) {
		if(userAns.length() == 0)
			return 0;
		
		while(ans.indexOf(", ") != -1){
			if (ans.substring(0, ans.indexOf(", ")).equals(userAns))
				return 1;
			ans = ans.substring(ans.indexOf(", ") + 2);
		}
		
		return -1;
	}
	
	/**
	 * Move on to the nextQuestion.
	 */
	private void nextQuestion() {
		btnNextQ.setVisible(false);
		answered = false;
		tfAns.setText("");
		lblFeedback.setText("Enter answers as (ax+b)(cx+d) OR (b+ax)(d+cx). OMIT a if a=1");
		
		if(qNum != numQs) {
			lblArrQStatus[qNum-1].setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		
			lblQuestion.setText("Question " + ++qNum);
			lblArrQStatus[qNum-1].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
			lblEquation.setText(quizSummary[qNum-1][0]);
			
		}
		else {
			this.showSummary();
			btnNextQ.setText("Next Question");
		}
	}
	
	/**
	 * Displays the summary screen and the score that the user got.
	 */
	private void showSummary() {
		content.removeAll();
		content.add(summaryScreen, BorderLayout.CENTER);
		
		quiz = false;
		summary = true;
		
		/*formatted percentage score to prevent repeating/long decimal sequences
		 * ex. if there are 3 questions and 2 were correct, 66.67 will be outputted instead of 66.666...
		 */
		lblScore.setText(String.format("Score: %.2f%%", (double)numCorrect/numQs*100));
		
		qNum = 2;
		this.toggleSummaryScreen(false);
		
		//fixes glitches with screen displays
		this.setSize(699, 499);
		this.setSize(700, 500);
		
	}
	
	/**
	 * Controls for the toggle buttons on the summary screen.
	 * @param nextQ - true indicates next question, false indicates previous question.
	 */
	private void toggleSummaryScreen(boolean nextQ) {
		if (nextQ && qNum != numQs)
			lblQuesNav.setText("Question " + (++qNum));
		else if (!nextQ && qNum != 1)
			lblQuesNav.setText("Question " + (--qNum));
		else return;
		
		String possAns = quizSummary[qNum-1][1];
		taStats.setText(quizSummary[qNum-1][0] + 
				"\n\nCorrect Answers: " + possAns.substring(0, possAns.indexOf(",  , ")) +
				"\nYour Answer: " + quizSummary[qNum-1][2] + 
				"\n\n" + quizSummary[qNum-1][3]);
	}
	
	/**
	 * Launches the Factoring Quiz.
	 * @param args - Unused.
	 */
	public static void main(String[] args) {
		new FactoringQuiz();
	}
}
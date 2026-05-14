import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class QuizScreen extends JComponent implements ActionListener {
    // Internal Processing Variables
    private int numQuestions;
    private int currentQuestionNum;
    private boolean questionAnswered;

    private ArrayList<QuadraticProblem> questions;

    //GUI components needed for quizScreen during quiz
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


    /**
     * Constructor for the quiz screen. Initializes the GUI components and sets up the layout of the screen.
     */
    public QuizScreen() {
        //Set up quizScreen
        this.setLayout(new GridLayout(3, 1));

		//Top section of the quiz screen
		this.add(questionPane);
			questionPane.add(pnlQuestion, BorderLayout.CENTER);
				pnlQuestion.add(lblQuestion);
					lblQuestion.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 40));
				pnlQuestion.add(lblEquation);
					lblEquation.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 35));
					lblEquation.setBackground(Color.LIGHT_GRAY);
					lblEquation.setOpaque(true);
			questionPane.add(pnlQuestionBar, BorderLayout.NORTH);
			
		//Middle section of the quiz screen
		this.add(responsePane);
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
		this.add(keyPane);
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
    }

    /**
     * Initializes the quiz screen with the specified number of questions. 
     * Generates the questions and sets up the initial display for the quiz.
     * @param numQuestions - the number of questions to be included in the quiz.
     */
    public void initQuizScreen(int numQuestions) {
        this.numQuestions = numQuestions;
        this.currentQuestionNum = 1;
        this.questionAnswered = false;

        this.questions = new ArrayList<QuadraticProblem>();
		
		for (int i = 0; i < numQuestions; i++)
			this.questions.add(new QuadraticProblem());

        lblArrQStatus = new JLabel[this.numQuestions];
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
		
		lblQuestion.setText("Question " + this.currentQuestionNum);
		lblEquation.setText(questions.get(this.currentQuestionNum-1).getQuestion());
		lblFeedback.setText("Enter answers as (ax+b)(cx+d) OR (b+ax)(d+cx). OMIT a if a=1");
    }

    /**
     * Retrieve the quiz data.
     * @return questions - an ArrayList of QuadraticProblem objects.
     */
    public ArrayList<QuadraticProblem> getQuizSummary() {
        return questions;
    }

	/**
     * Event handler for the buttons in the quiz screen. 
     * Handles user input for answering questions and navigating through the quiz.
     * @param e - ActionEvent triggered by the button press.
     */
    @Override
	public void actionPerformed(ActionEvent e) {
        JButton btnPressed = (JButton) e.getSource();

        if (!this.questionAnswered) {
			lblFeedback.setText("Enter answers as (ax+b)(cx+d) OR (b+ax)(d+cx). OMIT a if a=1");
				
			if (btnPressed == btnSubmit)
				this.checkAnswer();
			else if (btnPressed == btnx) 
				tfAns.setText(tfAns.getText() + "x");
			else if (btnPressed == btnPlus) 
				tfAns.setText(tfAns.getText() + "+");
			else if (btnPressed == btnMinus) 
				tfAns.setText(tfAns.getText() + "-");
			else if (btnPressed == btnOpenPar)
				tfAns.setText(tfAns.getText() + "(");
			else if (btnPressed == btnClosePar) 
				tfAns.setText(tfAns.getText() + ")");
			else if (btnPressed == btnBackSpace && tfAns.getText().length() != 0) 
				tfAns.setText(tfAns.getText().substring(0, tfAns.getText().length()-1));
			
			for (int i = 1; i <= btnArrNums.length; i++) {
				if (btnPressed == btnArrNums[i-1]) {
					tfAns.setText(tfAns.getText() + i);
					break;
				}
			}
				
			//make sure user doesn't enter too long answers
			if (tfAns.getText().length() >= 20)
				tfAns.setText(tfAns.getText().substring(0, 20));
		}
		else if (btnPressed == btnNextQ)
			this.nextQuestion();
    }

    /**
	 * When user submits an answer, this method will run to determine if the question was right or wrong.
	 */
	private void checkAnswer() {
		String userAns = tfAns.getText();
		
		//If no answer was entered by the user.
		if (userAns.length() == 0) {
			lblFeedback.setText("Please use the built-in keyboard below to enter your answer");
			return;
		}

		QuadraticProblem currentQuestion = questions.get(this.currentQuestionNum-1);
		ArrayList<String> answers = currentQuestion.getAcceptedFactorizations();
		boolean isCorrect = currentQuestion.proposeFactorization(userAns);
			
		//answer is either right or wrong, either way the following must happen to move to next Question:
		btnNextQ.setVisible(true);
		if (this.currentQuestionNum == this.numQuestions)
			btnNextQ.setText("View Results");
		this.questionAnswered = true;
		
		if(isCorrect) {
			lblFeedback.setText("Correct!");
			lblArrQStatus[this.currentQuestionNum-1].setText("☑");
			lblArrQStatus[this.currentQuestionNum-1].setForeground(new Color(0, 200, 0));
			return;
		}
		
		//If incorrect
		lblFeedback.setText("Incorrect. Correct answers include: " + answers.get(0) + " ," + answers.get(1));
		lblArrQStatus[this.currentQuestionNum-1].setText("☒");
		lblArrQStatus[this.currentQuestionNum-1].setForeground(new Color(200, 0, 0));
	}
	
	/**
	 * Move on to the nextQuestion.
	 */
	private void nextQuestion() {
		btnNextQ.setVisible(false);
		this.questionAnswered = false;
		tfAns.setText("");
		lblFeedback.setText("Enter answers as (ax+b)(cx+d) OR (b+ax)(d+cx). OMIT a if a=1");
		
		if(this.currentQuestionNum != this.numQuestions) {
			lblArrQStatus[this.currentQuestionNum-1].setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		
			lblQuestion.setText("Question " + ++this.currentQuestionNum);
			lblArrQStatus[this.currentQuestionNum-1].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
			lblEquation.setText(questions.get(this.currentQuestionNum-1).getQuestion());
			
		}
		else {
			this.fireActionPerformed();
			btnNextQ.setText("Next Question");
		}
	}

    /*
     * ActionListener management
     */
    private final ArrayList<ActionListener> actionListeners = new ArrayList<>();

    /**
     * Adds an ActionListener to the quiz screen.
     * @param listener - the ActionListener to be added to the quiz screen.
     */
    public void addActionListener(ActionListener listener) {
        actionListeners.add(listener);
    }

    /**
     * Removes an ActionListener from the quiz screen.
     * @param listener - the ActionListener to be removed from the quiz screen.
     */
    public void removeActionListener(ActionListener listener) {
        actionListeners.remove(listener);
    }

    /**
     * Fires an ActionEvent to all registered ActionListeners. 
     * Used to signal that the user has completed the quiz and is now requesting to see the quiz summary.
     */
    protected void fireActionPerformed() {
        ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "endQuiz"); 
        for (ActionListener listener : actionListeners)
            listener.actionPerformed(event);
    }
}
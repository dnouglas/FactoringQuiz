import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class QuizScreen extends JComponent implements ActionListener {
    // Internal Processing Variables
    private int numQuestions;
    private int currentQuestionNum;
    private boolean questionAnswered;

    /* 2D array that contains quiz data
	 * there will be numQs rows, which represents numQs questions 
	 * 4 columns:
	 * 	column (index) 0 contains the actual questions, 
	 * 	column 1 contains possible correct answers for each question, 
	 * 	column 2 contains what the user answered, 
	 *	column 3 contains the response status (user answer correctly or incorrectly?)
	 */
	private String[][] quizSummary;

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

        this.createQuestions();

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
		lblEquation.setText(quizSummary[this.currentQuestionNum-1][0]);
		lblFeedback.setText("Enter answers as (ax+b)(cx+d) OR (b+ax)(d+cx). OMIT a if a=1");
    }

    /**
     * Returns the quiz summary data, which includes the questions, possible answers, user answers, and response status for each question.
     * @return quizSummary - the 2D array containing the quiz questions, possible answers, user answers, and response status for each question.
     */
    public String[][] getQuizSummary() {
        return quizSummary;
    }

    /**
	 * Create questions and all allowed answers for each question.
	 * Populates the first two columns of quizSummary array.
	 */
    private void createQuestions() {
		quizSummary = new String[this.numQuestions][4];
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
		String ans = quizSummary[this.currentQuestionNum-1][1], userAns = tfAns.getText();
		
		//If no answer was entered by the user.
		if (userAns.length() == 0) {
			lblFeedback.setText("Please use the built-in keyboard below to enter your answer");
			return;
		}

		boolean isCorrect = false;
        String ansCopy = ans;
        // Recall that ans is a String containing all accepted answers, separated by a comma and a space --> ", "
        while (ansCopy.indexOf(", ") != -1) {
            if (ansCopy.substring(0, ansCopy.indexOf(", ")).equals(userAns)) {
                isCorrect = true;
                break;
            }
            ansCopy = ansCopy.substring(ans.indexOf(", ") + 2);
        }
			
		//answer is either right or wrong, either way the following must happen to move to next Question:
		btnNextQ.setVisible(true);
		if (this.currentQuestionNum == this.numQuestions)
			btnNextQ.setText("View Results");
		quizSummary[this.currentQuestionNum-1][2] = userAns;
		this.questionAnswered = true;
		
		if(isCorrect) {
			quizSummary[this.currentQuestionNum-1][3] = "Correct!";
				
			lblFeedback.setText("Correct!");
			lblArrQStatus[this.currentQuestionNum-1].setText("☑");
			lblArrQStatus[this.currentQuestionNum-1].setForeground(new Color(0, 200, 0));
			return;
		}
		
		//If incorrect
		quizSummary[this.currentQuestionNum-1][3] = "Incorrect.";
					
		lblFeedback.setText("Incorrect. Correct answers include: " + ans.substring(0, ans.indexOf(",  , ")));
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
			lblEquation.setText(quizSummary[this.currentQuestionNum-1][0]);
			
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
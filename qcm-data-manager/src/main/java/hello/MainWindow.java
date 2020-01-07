package hello;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class MainWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5197239002223121921L;

	private String filePath = "";

	private JPanel contentPane;
	private JTextField textField;
	private JLabel lblQuestions;

	private JTextArea txtrMessageCl;
	private JTextArea txtrQuestion;
	private JTextArea txtrRponseJuste;
	private JTextArea txtrRponseFausse;
	private JTextArea txtrRponseFausse_1;

	private void bind() {

		updateRead();
	}

	private void saveToFile() {

		if (filePath.length() <= 0) {
			JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

			int returnValue = jfc.showSaveDialog(this);

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				File selectedFile = jfc.getSelectedFile();
				System.out.println(selectedFile.getAbsolutePath());

				filePath = selectedFile.getAbsolutePath();

				this.setTitle("Editeur de QCM : " + filePath);
			}
		}

		QuestionsManager.getInstance().saveToFile(filePath);
	}

	private void updateWrite() {

		QuestionsManager.getInstance().currentQuestion().setMessage(txtrMessageCl.getText());
		QuestionsManager.getInstance().currentQuestion().setQuestion(txtrQuestion.getText());

		if (QuestionsManager.getInstance().currentQuestion().getAnswers().size() <= 0) {
			QuestionsManager.getInstance().currentQuestion().getAnswers().add(new Answer());
		}
		if (QuestionsManager.getInstance().currentQuestion().getAnswers().size() <= 1) {
			QuestionsManager.getInstance().currentQuestion().getAnswers().add(new Answer());
		}
		if (QuestionsManager.getInstance().currentQuestion().getAnswers().size() <= 2) {
			QuestionsManager.getInstance().currentQuestion().getAnswers().add(new Answer());
		}

		QuestionsManager.getInstance().currentQuestion().getAnswers().get(0).setValue(txtrRponseJuste.getText());
		QuestionsManager.getInstance().currentQuestion().getAnswers().get(0).setIsTrue(true);

		QuestionsManager.getInstance().currentQuestion().getAnswers().get(1).setValue(txtrRponseFausse.getText());
		QuestionsManager.getInstance().currentQuestion().getAnswers().get(1).setIsTrue(false);

		QuestionsManager.getInstance().currentQuestion().getAnswers().get(2).setValue(txtrRponseFausse_1.getText());
		QuestionsManager.getInstance().currentQuestion().getAnswers().get(2).setIsTrue(false);

	}

	private void updateRead() {

		lblQuestions.setText("Edittion de la Question " + (QuestionsManager.getInstance().getCurrentIndex() + 1)
				+ " sur " + QuestionsManager.getInstance().getQuestionsNumber());

		txtrMessageCl.setText(QuestionsManager.getInstance().currentQuestion().getMessage());
		txtrQuestion.setText(QuestionsManager.getInstance().currentQuestion().getQuestion());

		String juste = "";
		String faux1 = "";
		String faux2 = "";

		int t = 0;

		for (Answer ans : QuestionsManager.getInstance().currentQuestion().getAnswers()) {
			if (ans.isIsTrue()) {
				juste = ans.getValue();
			} else {
				if (t == 0) {
					faux1 = ans.getValue();
					t++;
				} else {
					faux2 = ans.getValue();
				}
			}
		}

		txtrRponseJuste.setText(juste);
		txtrRponseFausse.setText(faux1);
		txtrRponseFausse_1.setText(faux2);

	}

	private void clearFields() {

		txtrMessageCl.setText("");
		txtrQuestion.setText("");
		txtrRponseJuste.setText("");
		txtrRponseFausse.setText("");
		txtrRponseFausse_1.setText("");

		updateWrite();
	}

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 715, 614);
		setResizable(false);
		setTitle("Editeur de QCM");

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFichier = new JMenu("Fichier");
		mnFichier.setMnemonic('f');
		menuBar.add(mnFichier);

		JMenuItem mntmOuvrir = new JMenuItem("Ouvrir");
		mntmOuvrir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (filePath.length() <= 0) {
					JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

					jfc.setAcceptAllFileFilterUsed(false);
					jfc.setDialogTitle("Selectionner le fichier de données");
					FileNameExtensionFilter filter = new FileNameExtensionFilter("Données JSON", "json");
					jfc.addChoosableFileFilter(filter);

					int returnValue = jfc.showOpenDialog(MainWindow.this);

					if (returnValue == JFileChooser.APPROVE_OPTION) {
						File selectedFile = jfc.getSelectedFile();
						System.out.println(selectedFile.getAbsolutePath());

						filePath = selectedFile.getAbsolutePath();

						MainWindow.this.setTitle("Editeur de QCM : " + filePath);
					}
				}

				QuestionsManager.getInstance().loadFromFile(filePath);
				QuestionsManager.getInstance().firsQuestion();
				updateRead();

			}
		});

		JMenuItem mntmNouveauQuestionnaire = new JMenuItem("Nouveau");
		mntmNouveauQuestionnaire.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				filePath = "";
				MainWindow.this.setTitle("Editeur de QCM");
				QuestionsManager.getInstance().clearQuestions();
				updateRead();

			}
		});
		mntmNouveauQuestionnaire.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		mnFichier.add(mntmNouveauQuestionnaire);
		mntmOuvrir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mnFichier.add(mntmOuvrir);

		JMenuItem mntmEnregistrer = new JMenuItem("Enregistrer");
		mntmEnregistrer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateWrite();
				saveToFile();
			}
		});
		mntmEnregistrer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mnFichier.add(mntmEnregistrer);

		JMenuItem mntmEnregistrerSous = new JMenuItem("Enregistrer sous");
		mntmEnregistrerSous.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				filePath = "";
				updateWrite();
				saveToFile();
			}
		});
		mntmEnregistrerSous
				.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnFichier.add(mntmEnregistrerSous);

		JMenuItem mntmQuitter = new JMenuItem("Quitter");
		mntmQuitter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		JMenuItem mntmRafraichir = new JMenuItem("Recharger");
		mntmRafraichir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (filePath.length() > 0) {

					QuestionsManager.getInstance().loadFromFile(filePath);
					updateRead();
				}

			}
		});
		mntmRafraichir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
		mnFichier.add(mntmRafraichir);
		mntmQuitter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		mnFichier.add(mntmQuitter);

		JMenu mnEdition = new JMenu("Edition");
		mnEdition.setMnemonic('e');
		menuBar.add(mnEdition);

		JMenuItem mntmCopierLeJson = new JMenuItem("Copier le JSON");
		mntmCopierLeJson.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				QuestionsManager.getInstance().sendAllToClipboard();
			}
		});
		mntmCopierLeJson
				.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnEdition.add(mntmCopierLeJson);

		JMenuItem mntmToutEffacer = new JMenuItem("Tout effacer");
		mntmToutEffacer
				.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mntmToutEffacer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int input = JOptionPane.showConfirmDialog(MainWindow.this,
						"Voulez vous vraiment effacer toutes les questions ?", "Effacer toutes les questions ?",
						JOptionPane.OK_CANCEL_OPTION);
				if (input == 0) {
					QuestionsManager.getInstance().clearQuestions();
					updateRead();
				}
			}
		});
		mnEdition.add(mntmToutEffacer);

		JMenu mnAide = new JMenu("Aide");
		mnAide.setMnemonic('a');
		menuBar.add(mnAide);

		JMenuItem mntmAPropos = new JMenuItem("A propos");
		mntmAPropos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int input = JOptionPane.showConfirmDialog(MainWindow.this,
						"Les changements sont pris en compte automatiquement. Enregister le fichier seulement si tout va bien.",
						"Editer plus facilement des QCM", JOptionPane.DEFAULT_OPTION);
				// 0=ok
				System.out.println(input);
			}
		});
		mntmAPropos.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		mnAide.add(mntmAPropos);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		lblQuestions = new JLabel("Edition de questions");

		JLabel lblMessage = new JLabel("Message cl\u00E9");

		txtrMessageCl = new JTextArea();
		txtrMessageCl.setWrapStyleWord(true);
		txtrMessageCl.setLineWrap(true);
		txtrMessageCl.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				updateWrite();
			}

			@Override
			public void focusLost(FocusEvent e) {
				updateWrite();
			}
		});
		txtrMessageCl.setText("Message cl\u00E9");

		JLabel lblQuestion = new JLabel("Question");

		txtrQuestion = new JTextArea();
		txtrQuestion.setWrapStyleWord(true);
		txtrQuestion.setLineWrap(true);
		txtrQuestion.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				updateWrite();
			}

			@Override
			public void focusGained(FocusEvent e) {
				updateWrite();
			}
		});
		txtrQuestion.setText("Question");

		JLabel lblRponseJuste = new JLabel("R\u00E9ponse Juste");

		txtrRponseJuste = new JTextArea();
		txtrRponseJuste.setWrapStyleWord(true);
		txtrRponseJuste.setLineWrap(true);
		txtrRponseJuste.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				updateWrite();
			}

			@Override
			public void focusLost(FocusEvent e) {
				updateWrite();
			}
		});
		txtrRponseJuste.setText("R\u00E9ponse juste");

		JLabel lblRponseFausse = new JLabel("R\u00E9ponse fausse 1");

		txtrRponseFausse = new JTextArea();
		txtrRponseFausse.setWrapStyleWord(true);
		txtrRponseFausse.setLineWrap(true);
		txtrRponseFausse.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				updateWrite();
			}

			@Override
			public void focusLost(FocusEvent e) {
				updateWrite();
			}
		});
		txtrRponseFausse.setText("R\u00E9ponse fausse 1");

		JLabel lblRponseFausse_1 = new JLabel("R\u00E9ponse fausse 2");

		txtrRponseFausse_1 = new JTextArea();
		txtrRponseFausse_1.setWrapStyleWord(true);
		txtrRponseFausse_1.setLineWrap(true);
		txtrRponseFausse_1.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				updateWrite();
			}

			@Override
			public void focusLost(FocusEvent e) {
				updateWrite();
			}
		});
		txtrRponseFausse_1.setText("R\u00E9ponse fausse 2");

		JButton btnEffacer = new JButton("Effacer les champs");
		btnEffacer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearFields();
			}
		});

		JLabel lblNavigation = new JLabel("Navigation");

		JPanel panel = new JPanel();

		JButton btnSaveFile = new JButton("Enregister fichier");
		btnSaveFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveToFile();
			}
		});

		JButton btnEnregisterEtNouvelle = new JButton("Valider et Nouvelle");
		btnEnregisterEtNouvelle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				QuestionsManager.getInstance().newQuestion();
				updateRead();
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
						.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
								.createSequentialGroup()
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
										.createParallelGroup(Alignment.TRAILING)
										.addGroup(gl_contentPane.createSequentialGroup()
												.addComponent(btnEffacer, GroupLayout.PREFERRED_SIZE, 151,
														GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED, 168, Short.MAX_VALUE)
												.addComponent(btnEnregisterEtNouvelle, GroupLayout.PREFERRED_SIZE, 151,
														GroupLayout.PREFERRED_SIZE))
										.addComponent(txtrRponseFausse_1, GroupLayout.DEFAULT_SIZE, 470,
												Short.MAX_VALUE)
										.addComponent(txtrRponseFausse, GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
										.addComponent(txtrRponseJuste, GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
										.addComponent(txtrMessageCl, GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
										.addComponent(txtrQuestion, GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE))
										.addGroup(gl_contentPane.createSequentialGroup().addComponent(lblRponseJuste)
												.addGap(341))
										.addGroup(gl_contentPane.createSequentialGroup().addComponent(lblRponseFausse)
												.addGap(326))
										.addGroup(gl_contentPane.createSequentialGroup().addComponent(lblRponseFausse_1)
												.addGap(326)))
								.addGap(18))
								.addGroup(gl_contentPane.createSequentialGroup().addComponent(lblQuestions)
										.addPreferredGap(ComponentPlacement.RELATED)))
						.addGroup(gl_contentPane.createSequentialGroup().addComponent(lblMessage)
								.addPreferredGap(ComponentPlacement.RELATED)))
						.addGroup(gl_contentPane.createSequentialGroup().addComponent(lblQuestion)
								.addPreferredGap(ComponentPlacement.RELATED)))
				.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false).addGroup(gl_contentPane
						.createSequentialGroup()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(lblNavigation)
								.addComponent(panel, GroupLayout.PREFERRED_SIZE, 186, GroupLayout.PREFERRED_SIZE))
						.addGap(15))
						.addGroup(gl_contentPane.createSequentialGroup().addComponent(btnSaveFile,
								GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addContainerGap()))));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_contentPane
						.createParallelGroup(Alignment.BASELINE).addComponent(lblNavigation).addComponent(lblQuestions))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING).addGroup(gl_contentPane
						.createSequentialGroup().addComponent(lblMessage).addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(txtrMessageCl, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
						.addGap(11).addComponent(lblQuestion).addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(txtrQuestion, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblRponseJuste)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(txtrRponseJuste, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblRponseFausse)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(txtrRponseFausse, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblRponseFausse_1)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(txtrRponseFausse_1, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE))
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 425, GroupLayout.PREFERRED_SIZE))
				.addGap(18)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(btnSaveFile, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE).addComponent(btnEffacer)
								.addComponent(btnEnregisterEtNouvelle)))
				.addContainerGap()));

		JButton btnNouvelleQuestion = new JButton("Nouvelle Question");
		btnNouvelleQuestion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				QuestionsManager.getInstance().newQuestion();
				updateRead();
			}
		});

		JButton btnPremireQuestion = new JButton("Premi\u00E8re Question");
		btnPremireQuestion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				QuestionsManager.getInstance().firsQuestion();
				updateRead();
			}
		});

		JButton btnQuestionPrcdente = new JButton("Question pr\u00E9c\u00E9dante");
		btnQuestionPrcdente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				QuestionsManager.getInstance().previousQuestion();
				updateRead();
			}
		});

		textField = new JTextField();
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.setText("1");
		textField.setColumns(10);

		JButton btnAller = new JButton("Aller \u00E0");
		btnAller.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sTarget = textField.getText();

				int iTarget = Integer.parseInt(sTarget);

				QuestionsManager.getInstance().goToQuestion(iTarget - 1);
				updateRead();
			}
		});

		JButton btnQuestionSuivante = new JButton("Question suivante");
		btnQuestionSuivante.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				QuestionsManager.getInstance().nextQuestion();
				updateRead();
			}
		});

		JButton btnDernireQuestion = new JButton("Derni\u00E8re question");
		btnDernireQuestion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				QuestionsManager.getInstance().LastQuestion();
				updateRead();
			}
		});

		JButton btnEffacerLaQuestion = new JButton("Effacer la question");
		btnEffacerLaQuestion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int input = JOptionPane.showConfirmDialog(MainWindow.this,
						QuestionsManager.getInstance().currentQuestion().getMessage(),
						"Voulez-vous effacer la question de la liste ?", JOptionPane.OK_CANCEL_OPTION);

				if (input == 0) {
					QuestionsManager.getInstance().deleteCurrentQuestion();
					updateRead();
				}

			}
		});

		JButton btnDupliquerLaQuestion = new JButton("Dupliquer la Question");
		btnDupliquerLaQuestion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				QuestionsManager.getInstance().newQuestion();
				updateWrite();
				updateRead();
			}
		});
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(btnQuestionPrcdente, GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
						.addComponent(btnQuestionSuivante, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 166,
								Short.MAX_VALUE)
						.addComponent(btnPremireQuestion, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 166,
								Short.MAX_VALUE)
						.addComponent(btnDernireQuestion, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 166,
								Short.MAX_VALUE)
						.addGroup(gl_panel.createSequentialGroup()
								.addComponent(textField, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(btnAller, GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE))
						.addComponent(btnNouvelleQuestion, GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
						.addComponent(btnDupliquerLaQuestion, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 166,
								Short.MAX_VALUE)
						.addComponent(btnEffacerLaQuestion, GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE))
				.addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addContainerGap().addComponent(btnQuestionPrcdente)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(btnQuestionSuivante).addGap(18)
						.addComponent(btnPremireQuestion).addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(btnDernireQuestion).addGap(18)
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(btnAller))
						.addGap(18).addComponent(btnNouvelleQuestion).addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(btnDupliquerLaQuestion).addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(btnEffacerLaQuestion).addContainerGap(152, Short.MAX_VALUE)));
		panel.setLayout(gl_panel);
		contentPane.setLayout(gl_contentPane);

		bind();
	}
}

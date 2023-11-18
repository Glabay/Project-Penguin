package glabtech.Core.Engine;

import javax.swing.JFrame;

public class Engine {

	public static void main(String[] args) {
		JFrame window = new JFrame("Project Penguins");
		window.setResizable(false);
		window.setTitle("Project Penguins");
		window.setContentPane(new GamePanel());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}
}
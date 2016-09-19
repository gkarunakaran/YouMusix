/***********************************************************************************
 * youmusix/Interface.java: The main interface for YouMusix
 ***********************************************************************************
 * MIT License
 *
 * Copyright (c) 2016 Karanvir Singh
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 **********************************************************************************/

package youmusix;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javazoom.jl.player.Player;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.net.URL;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Interface extends JFrame {

	private JPanel contentPane;
	private JTextField txtEnterUrl;
	Thread thread;
	Player mp3player;
	JButton btnStop;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Interface frame = new Interface();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Interface() {
		setTitle("YouMusix");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnPlay = new JButton("Play");
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				thread = new Thread(new Runnable() {
					@Override
					public void run() {
						String Server_1 = "http://youtubeinmp3.com/fetch/?video=";
						String song = Server_1 + txtEnterUrl.getText();
						BufferedInputStream in = null;
						try {
							in = new BufferedInputStream(new URL(song).openStream());
							mp3player = new Player(in);
							mp3player.play();
							mp3player.close();
							thread.interrupt();
						} catch (Exception e1) {
							System.out.println("Error:" + e1);
						}
					}
				});
				thread.start();
			}
		});
		btnPlay.setBounds(12, 99, 117, 25);
		contentPane.add(btnPlay);

		txtEnterUrl = new JTextField();
		txtEnterUrl.setBounds(12, 12, 418, 19);
		contentPane.add(txtEnterUrl);
		txtEnterUrl.setColumns(10);

		JButton btnPause = new JButton("Pause");
		btnPause.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent arg0) {
				thread.suspend();
			}
		});
		btnPause.setBounds(155, 99, 117, 25);
		contentPane.add(btnPause);

		JButton btnResume = new JButton("Resume");
		btnResume.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				thread.resume();
			}
		});
		btnResume.setBounds(292, 99, 117, 25);
		contentPane.add(btnResume);

		btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mp3player.close();
				thread.interrupt();
			}
		});
		btnStop.setBounds(155, 155, 117, 25);
		contentPane.add(btnStop);
	}
}
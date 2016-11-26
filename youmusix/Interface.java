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

import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.awt.event.ActionEvent;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.Label;
import javax.swing.JLabel;
import javazoom.jl.player.Player;
import javax.swing.ImageIcon;

@SuppressWarnings("serial")
public class Interface extends JFrame {

	Integer RunningPlayerInstances = 0;
	JLabel lblAppStatus, lblServerStatus;
	JTextField txtEnterUrl;
	JPanel contentPane;
	JButton btnPlay, btnStop, btnPause, btnResume;
	Thread thread;
	static Label ElapsedTime;
	static Player mp3player;
	static Runnable BackgroundThread;
	static long millis;
	static String hms;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Interface frame = new Interface();
					frame.setVisible(true);
					BackgroundThread = new Runnable() {
						public void run() {
							try {
								boolean temp = true;
								while (temp == true) {
									millis = 0;
									millis = mp3player.getPosition();
									hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
											TimeUnit.MILLISECONDS.toMinutes(millis)
													- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
											TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES
													.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
									ElapsedTime.setText(hms);
								}
							} catch (Exception e1) {
								System.out.println(e1);
							}
						}
					};

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Interface() {
		setResizable(false);
		setTitle("YouMusix");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 640, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		btnPlay = new JButton("Play");
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblAppStatus.setText("Now Playing...");
				thread = new Thread(new Runnable() {
					@Override
					public void run() {
						String Server_1 = "http://youtubeinmp3.com/fetch/?video=";
						String song = Server_1 + txtEnterUrl.getText();
						BufferedInputStream in = null;
						try {
							in = new BufferedInputStream(new URL(song).openStream());
							mp3player = new Player(in);
							new Thread(BackgroundThread).start();
							mp3player.play();
							mp3player.close();
							thread.interrupt();
						} catch (Exception e1) {
							System.out.println("Error:" + e1);
						}
					}
				});
				if (txtEnterUrl.getText().startsWith("https://")) {
					try {
						btnResume.setVisible(false);
						btnPause.setVisible(true);
						btnPlay.setVisible(false);
						btnStop.setVisible(true);
						thread.start();
					} catch (Exception e1) {
						System.out.println(e1);
					}
				} else {
					lblAppStatus.setText("Please enter a valid URL");
				}
			}
		});

		ElapsedTime = new Label();
		ElapsedTime.setAlignment(Label.CENTER);
		ElapsedTime.setBounds(240, 37, 155, 21);
		contentPane.add(ElapsedTime);

		btnPlay.setBounds(240, 75, 155, 25);
		contentPane.add(btnPlay);

		txtEnterUrl = new JTextField();
		txtEnterUrl.setBounds(12, 12, 598, 19);
		contentPane.add(txtEnterUrl);
		txtEnterUrl.setColumns(10);

		btnPause = new JButton("Pause");
		btnPause.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent arg0) {
				thread.suspend();
				btnResume.setVisible(true);
				btnPause.setVisible(false);
				lblAppStatus.setText("Paused");
			}
		});
		btnPause.setBounds(240, 75, 155, 25);
		contentPane.add(btnPause);

		btnResume = new JButton("Resume");
		btnResume.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				btnResume.setVisible(false);
				btnPause.setVisible(true);
				lblAppStatus.setText("Now Playing...");
				thread.resume();
			}
		});
		btnResume.setBounds(240, 75, 155, 25);
		contentPane.add(btnResume);

		btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
					btnPlay.setVisible(true);
					btnPause.setVisible(false);
					btnResume.setVisible(false);
					thread.resume();
					mp3player.close();
					thread.interrupt();
					btnStop.setVisible(false);
					lblAppStatus.setText("Stopped");
			}
		});
		btnStop.setBounds(465, 75, 155, 25);
		contentPane.add(btnStop);

		lblAppStatus = new JLabel("");
		lblAppStatus.setBounds(12, 231, 165, 24);
		contentPane.add(lblAppStatus);

		lblServerStatus = new JLabel("");
		lblServerStatus.setBounds(244, 231, 376, 24);
		contentPane.add(lblServerStatus);

		JLabel lblBackground = new JLabel("");
		lblBackground.setIcon(new ImageIcon(Interface.class.getResource("/graphics/background1.png")));
		lblBackground.setBounds(0, 0, 632, 267);
		contentPane.add(lblBackground);

		btnPlay.setVisible(true);
		btnStop.setVisible(false);
		btnResume.setVisible(false);
		btnPause.setVisible(false);

		try {
			String ServerStatusCheck = "http://youtubeinmp3.com/fetch/?video=https://www.youtube.com/watch?v=i62Zjga8JOM";
			HttpURLConnection connection = (HttpURLConnection) new URL(ServerStatusCheck).openConnection();
			connection.setRequestMethod("HEAD");
			int responseCode = connection.getResponseCode();
			if (responseCode == 200) {
				lblServerStatus.setForeground(Color.GREEN);
				lblServerStatus.setText("Connected to server");
			} else {
				lblServerStatus.setForeground(Color.RED);
				lblServerStatus.setText("Service unable. Please try again later!");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
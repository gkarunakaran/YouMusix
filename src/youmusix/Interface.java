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
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

@SuppressWarnings("serial")
public class Interface extends JFrame {

	JLabel lblAppStatus, lblThumbnail;
	JTextField VideoURL;
	JPanel contentPane;
	JButton btnPlay, btnStop, btnPause, btnResume, btnRepeatDisabled, btnRepeatEnabled;
	Thread thread;
	String VideoID, RepeatMusic;
	Boolean InitialURLBarClick = false;
	static Player mp3player;
	static Runnable BackgroundThread;
	static long MilliSeconds = 0;
	static String hms;
	private JMenuItem mntmCalculateMpStream;
	private JMenuItem mntmExit;
	private JMenu mnHelp;
	private JMenuItem mntmAbout;
	private JMenuItem mntmLicence;
	private static JLabel lblElapsedTime;

	public static void main(String[] args) {
		Interface frame = new Interface();
		frame.setVisible(true);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				BackgroundThread = new Runnable() {
					public void run() {
						try {
							boolean temp = true;
							while (temp == true) {
								MilliSeconds = 0;
								MilliSeconds = mp3player.getPosition();
								hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(MilliSeconds),
										TimeUnit.MILLISECONDS.toMinutes(MilliSeconds)
												- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(MilliSeconds)),
										TimeUnit.MILLISECONDS.toSeconds(MilliSeconds) - TimeUnit.MINUTES
												.toSeconds(TimeUnit.MILLISECONDS.toMinutes(MilliSeconds)));
								System.out.println("Timer working...");
								lblElapsedTime.setText(hms);
							}
						} catch (Exception e1) {
							System.out.println(e1);
						}
					}
				};
			}
		});
	}

	public Interface() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Interface.class.getResource("/graphics/YouMusix_Icon.png")));
		setResizable(false);
		setTitle("YouMusix ♪");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 640, 382);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnMenu = new JMenu("Menu");
		menuBar.add(mnMenu);

		mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mnMenu.add(mntmExit);

		JMenu mnTools = new JMenu("Tools");
		menuBar.add(mnTools);

		JMenuItem mntmSeverStatus = new JMenuItem("Sever status");
		mntmSeverStatus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					String ServerStatusCheck = "http://youtubeinmp3.com/fetch/?video=https://www.youtube.com/watch?v=i62Zjga8JOM";
					HttpURLConnection connection = (HttpURLConnection) new URL(ServerStatusCheck).openConnection();
					connection.setRequestMethod("HEAD");
					int responseCode = connection.getResponseCode();
					if ((responseCode == 200)) {
						JOptionPane.showMessageDialog(null, "Server is working normally");
					} else {
						JOptionPane.showMessageDialog(null, "Service currently unavailable!");
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		mntmCalculateMpStream = new JMenuItem("Calculate MP3 stream size");
		mntmCalculateMpStream.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String VideoURL = JOptionPane
						.showInputDialog("Enter YouTube video URL, ex. https://www.youtube.com/watch?v=EP625xQIGzs");
				try {
					String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
					Pattern compiledPattern = Pattern.compile(pattern);
					Matcher matcher = compiledPattern.matcher(VideoURL);
					if (matcher.find()) {
						long size;
						URL url = new URL("http://youtubeinmp3.com/fetch/?video=" + VideoURL);
						URLConnection conn = url.openConnection();
						size = conn.getContentLength();
						size = size / 1024 / 1024;
						if (size < 0) {
							JOptionPane.showMessageDialog(null, "Error");
						} else {
							JOptionPane.showMessageDialog(null, "MP3 stream size: " + size + " MB");
						}
						conn.getInputStream().close();
					} else {
						JOptionPane.showMessageDialog(null, "Please enter a valid URL", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception e1) {
					System.out.println(e1);
				}
			}
		});
		mnTools.add(mntmCalculateMpStream);
		mnTools.add(mntmSeverStatus);

		mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				About obj = new About();
				obj.setVisible(true);
			}
		});
		mnHelp.add(mntmAbout);

		mntmLicence = new JMenuItem("Licence");
		mntmLicence.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				License obj = new License();
				obj.setVisible(true);
			}
		});
		mnHelp.add(mntmLicence);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		btnPlay = new JButton("");
		btnPlay.setVisible(true);
		btnPlay.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				btnPlay.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Play_Hover.png")));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				btnPlay.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Play_Standard.png")));
			}
		});
		btnPlay.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Play_Standard.png")));
		btnPlay.setOpaque(false);
		btnPlay.setContentAreaFilled(false);
		btnPlay.setBorderPainted(false);
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblAppStatus.setText("Now Playing...");
				thread = new Thread(new Runnable() {
					@Override
					public void run() {
						BufferedInputStream in = null;
						try {
							String Server_1 = "http://youtubeinmp3.com/fetch/?video=";
							URL Video = new URL(Server_1 + VideoURL.getText());
							String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
							Pattern compiledPattern = Pattern.compile(pattern);
							Matcher matcher = compiledPattern.matcher(VideoURL.getText());
							if (matcher.find()) {
								VideoID = matcher.group();
								URL thumbnailurl = new URL("https://i1.ytimg.com/vi/" + VideoID + "/default.jpg");
								BufferedImage image = ImageIO.read(thumbnailurl);
								lblThumbnail.setIcon(new ImageIcon(image));
							}
							in = new BufferedInputStream(Video.openStream());
							mp3player = new Player(in);
							new Thread(BackgroundThread).start();
							mp3player.play();
							mp3player.close();
							thread.interrupt();
							if (RepeatMusic.equalsIgnoreCase("true")) {
								btnStop.doClick();
								btnPlay.doClick();
							}
							if (RepeatMusic.equalsIgnoreCase("false")) {
								lblAppStatus.setText("Stopped");
							}
						} catch (JavaLayerException DecoderError) {
							lblAppStatus.setText("Connection error! Trying again...");
							btnStop.doClick();
							lblAppStatus.setText("Playing...");
							thread.start();
						} catch (Exception e) {
							System.out.println("Error: " + e);
						}
					}
				});
				if (VideoURL.getText().startsWith("https://")) {
					try {
						btnResume.setVisible(false);
						btnPause.setVisible(true);
						btnStop.setVisible(true);
						btnPlay.setVisible(false);
						thread.start();
					} catch (Exception e1) {
						System.out.println(e1);
					}
				} else {
					lblAppStatus.setText("Please enter a valid URL");
				}
			}
		});

		btnRepeatEnabled = new JButton("");
		btnRepeatEnabled.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				btnRepeatEnabled.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Repeat_Hover.png")));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				btnRepeatEnabled.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Repeat_Standard.png")));
			}
		});
		btnRepeatEnabled.setOpaque(false);
		btnRepeatEnabled.setContentAreaFilled(false);
		btnRepeatEnabled.setBorderPainted(false);
		btnRepeatEnabled.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnRepeatEnabled.setVisible(false);
				btnRepeatDisabled.setVisible(true);
				AppSettings.Write_Repeating_the_music_is_disabled();
			}
		});

		btnRepeatDisabled = new JButton("");
		btnRepeatDisabled.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnRepeatDisabled.setIcon(new ImageIcon(Interface.class.getResource("/graphics/No_Repeat_Hower.png")));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				btnRepeatDisabled
						.setIcon(new ImageIcon(Interface.class.getResource("/graphics/No_Repeat_Standard.png")));
			}
		});
		btnRepeatDisabled.setOpaque(false);
		btnRepeatDisabled.setContentAreaFilled(false);
		btnRepeatDisabled.setBorderPainted(false);
		btnRepeatDisabled.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnRepeatDisabled.setVisible(false);
				btnRepeatEnabled.setVisible(true);
				AppSettings.Write_Repeating_the_music_is_enabled();
			}
		});

		lblElapsedTime = new JLabel("");
		lblElapsedTime.setBounds(255, 37, 129, 21);
		contentPane.add(lblElapsedTime);
		btnRepeatDisabled.setIcon(new ImageIcon(Interface.class.getResource("/graphics/No_Repeat_Standard.png")));
		btnRepeatDisabled.setBounds(337, 75, 64, 50);
		contentPane.add(btnRepeatDisabled);
		btnRepeatEnabled.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Repeat_Standard.png")));
		btnRepeatEnabled.setBounds(337, 75, 64, 50);
		contentPane.add(btnRepeatEnabled);

		lblThumbnail = new JLabel("");
		lblThumbnail.setBounds(470, 37, 120, 80);
		contentPane.add(lblThumbnail);

		btnPlay.setBounds(232, 75, 75, 50);
		contentPane.add(btnPlay);

		VideoURL = new JTextField();
		VideoURL.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (InitialURLBarClick == false) {
					VideoURL.setText("");
					InitialURLBarClick = true;
				}
			}
		});
		VideoURL.setForeground(Color.GRAY);
		VideoURL.setText("Enter YouTube video URL, ex. https://www.youtube.com/watch?v=EP625xQIGzs");
		VideoURL.setBounds(0, 0, 632, 31);
		contentPane.add(VideoURL);
		VideoURL.setColumns(10);

		btnPause = new JButton("");
		btnPause.setVisible(false);
		btnPause.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnPause.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Pause_Hover.png")));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				btnPause.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Pause_Standard.png")));
			}
		});
		btnPause.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Pause_Standard.png")));
		btnPause.setOpaque(false);
		btnPause.setContentAreaFilled(false);
		btnPause.setBorderPainted(false);
		btnPause.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent arg0) {
				thread.suspend();
				btnResume.setVisible(true);
				btnPause.setVisible(false);
				btnStop.setVisible(true);
				lblAppStatus.setText("Paused");
			}
		});
		btnPause.setBounds(232, 75, 75, 50);
		contentPane.add(btnPause);

		btnResume = new JButton("");
		btnResume.setVisible(false);
		btnResume.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnResume.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Play_Hover.png")));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				btnResume.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Play_Standard.png")));
			}
		});
		btnResume.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Play_Standard.png")));
		btnResume.setOpaque(false);
		btnResume.setContentAreaFilled(false);
		btnResume.setBorderPainted(false);
		btnResume.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				lblAppStatus.setText("Now Playing...");
				btnResume.setVisible(false);
				btnPause.setVisible(true);
				thread.resume();
			}
		});
		btnResume.setBounds(232, 75, 75, 50);
		contentPane.add(btnResume);

		btnStop = new JButton("");
		btnStop.setVisible(false);
		btnStop.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnStop.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Stop_Hover.png")));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				btnStop.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Stop_Standard.png")));
			}
		});
		btnStop.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Stop_Standard.png")));
		btnStop.setOpaque(false);
		btnStop.setContentAreaFilled(false);
		btnStop.setBorderPainted(false);
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
		btnStop.setBounds(288, 75, 64, 50);
		contentPane.add(btnStop);

		lblAppStatus = new JLabel("");
		lblAppStatus.setHorizontalAlignment(SwingConstants.CENTER);
		lblAppStatus.setBounds(255, 241, 129, 24);
		contentPane.add(lblAppStatus);

		JLabel lblBackground = new JLabel("");
		lblBackground.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Background.png")));
		lblBackground.setBounds(0, 30, 632, 298);
		contentPane.add(lblBackground);

		btnRepeatEnabled.setVisible(false);
		btnRepeatDisabled.setVisible(false);
		AppSettings.Load_the_settings();
		RepeatMusic = AppSettings.RepeatMusic;

		if (RepeatMusic.equalsIgnoreCase("true")) {
			btnRepeatEnabled.setVisible(true);
		}
		if (RepeatMusic.equalsIgnoreCase("false")) {
			btnRepeatDisabled.setVisible(true);
		}

	}
}
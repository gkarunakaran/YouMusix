/*****************************************************************************
 * youmusix/About.java: About JFrame for YouMusix
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
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.net.URL;

@SuppressWarnings("serial")
public class About extends JFrame {

	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					About frame = new About();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public About() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(About.class.getResource("/graphics/YouMusix_Icon.png")));
		setResizable(false);
		setType(Type.UTILITY);
		setTitle("About");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 540, 260);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(238, 238, 238));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnGitHub = new JButton("GitHub");
		btnGitHub.setToolTipText("Access YouMusix github repository");
		btnGitHub.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Desktop.getDesktop().browse(new URL("https://github.com/kvsjxd/YouMusix/").toURI());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		JLabel lblTemporary = new JLabel("Temporary");
		lblTemporary.setForeground(Color.WHITE);
		lblTemporary.setBounds(319, 53, 80, 25);
		contentPane.add(lblTemporary);
		btnGitHub.setBounds(25, 112, 183, 25);
		contentPane.add(btnGitHub);

		JLabel lblApplicationVersion = new JLabel("Version: Alpha Build 161119");
		lblApplicationVersion.setFont(new Font("Dialog", Font.BOLD, 14));
		lblApplicationVersion.setBounds(25, 72, 260, 18);
		contentPane.add(lblApplicationVersion);

		JLabel lblDeveloper = new JLabel("Developer");
		lblDeveloper.setFont(new Font("Dialog", Font.BOLD, 16));
		lblDeveloper.setBounds(25, 12, 228, 24);
		contentPane.add(lblDeveloper);

		JLabel lblMyname = new JLabel("Karanvir Singh");
		lblMyname.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Desktop.getDesktop().browse(new URL("https://twitter.com/iamkaranvir").toURI());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				lblMyname.setForeground(Color.BLUE);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				lblMyname.setForeground(Color.RED);
			}
		});
		lblMyname.setForeground(Color.RED);
		lblMyname.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 16));
		lblMyname.setBounds(25, 36, 228, 24);
		contentPane.add(lblMyname);

		JLabel lblGoogle = new JLabel(
				"YouTube, is trademark of Google, Inc. We are not affliated with Google, Inc. in any way.");
		lblGoogle.setHorizontalAlignment(SwingConstants.LEFT);
		lblGoogle.setFont(new Font("Dialog", Font.PLAIN, 10));
		lblGoogle.setBounds(25, 191, 495, 24);
		contentPane.add(lblGoogle);

		JLabel lblAppLogo = new JLabel("");
		lblAppLogo.setIcon(new ImageIcon(About.class.getResource("/graphics/YouMusix_Logo.png")));
		lblAppLogo.setBounds(285, 0, 150, 150);
		contentPane.add(lblAppLogo);

		JLabel lblIconNotice = new JLabel(
				"Material design icons are licensed under Apache License 2.0 and are provided by Google, Inc.");
		lblIconNotice.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					Desktop.getDesktop().browse(new URL("https://material.io/icons/").toURI());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				lblIconNotice.setForeground(Color.BLUE);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				lblIconNotice.setForeground(new Color(51, 51, 51));
			}
		});
		lblIconNotice.setHorizontalAlignment(SwingConstants.LEFT);
		lblIconNotice.setFont(new Font("Dialog", Font.PLAIN, 10));
		lblIconNotice.setBounds(25, 170, 495, 24);
		contentPane.add(lblIconNotice);

		JLabel lblJLayerNotice = new JLabel(
				"JLayer decoder library is used for decoding the MP3 stream which is licensed under LGPL");
		lblJLayerNotice.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lblJLayerNotice.setForeground(Color.BLUE);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				lblJLayerNotice.setForeground(new Color(51, 51, 51));
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Desktop.getDesktop().browse(new URL("http://www.javazoom.net/javalayer/sources.html").toURI());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		lblJLayerNotice.setHorizontalAlignment(SwingConstants.LEFT);
		lblJLayerNotice.setFont(new Font("Dialog", Font.PLAIN, 10));
		lblJLayerNotice.setBounds(25, 149, 495, 24);
		contentPane.add(lblJLayerNotice);
	}
}
